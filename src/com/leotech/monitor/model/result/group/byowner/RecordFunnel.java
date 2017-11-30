package com.leotech.monitor.model.result.group.byowner;

import com.leotech.monitor.datamanager.sql.MySqlTunnelAccessHelper;
import com.leotech.monitor.model.modifier.Modifier;
import com.leotech.monitor.model.modifier.ModifierContainer;
import com.leotech.monitor.model.result.QueryResult;
import com.leotech.monitor.model.sql.SqlResult;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.TableRow;
import com.google.visualization.datasource.datatable.value.ValueType;

import java.util.ArrayList;
import java.util.List;

public class RecordFunnel implements QueryResult {
    private static final String BASE_QUERY_STRUCT = "SELECT (\n" +
            "SELECT COUNT(*) \n" +
            "FROM re_%sBYO_front.re_fsbo \n" +
            "WHERE re_%sBYO_front.re_fsbo.latestExtractionDate %sMOD\n" +
            ") AS scraped, \n" +

            "( SELECT COUNT(*) \n" +
            "FROM re_%sBYO_front.re_fsbo\n" +
            "WHERE re_%sBYO_front.re_fsbo.firstExtractionDate %sMOD\n" +
            ") AS new,\n" +

            "( SELECT COUNT(*) \n" +
            "FROM re_%sBYO_front.re_fsbo\n" +
            "WHERE re_%sBYO_front.re_fsbo.firstExtractionDate %sMOD AND (stage = '0' OR stage = '2' OR stage like '1%')\n" +
            ") AS processable,\n" +

            "( SELECT COUNT(*) \n" +
            "FROM re_%sBYO_back.re_fsbo\n" +
            "WHERE re_%sBYO_back.re_fsbo.firstExtractionDate %sMOD\n" +
            ") AS staged,\n" +

            "( SELECT COUNT(*) \n" +
            "FROM re_%sBYO_back.re_fsbo_complete\n" +
            "WHERE re_%sBYO_back.re_fsbo_complete.firstExtractionDate %sMOD\n" +
            ") AS completed\n" +

            "FROM dual";
    private final String QUERY;


    public RecordFunnel(String byOwnerType, List<Modifier> modifiers) {
        System.out.println("Byowner::" + byOwnerType);
        if (modifiers != null && !modifiers.isEmpty()) {
            System.out.println("modifiers not empty");
        }
        assert byOwnerType != null && !byOwnerType.isEmpty();
        assert modifiers != null && !modifiers.isEmpty();
        if (ModifierContainer.containsModifier(modifiers, ModifierContainer.Time_Modifier.YESTERDAY)) {
            //then take into account that the yesterday modifier is going to return additional ?
        }

        this.QUERY = BASE_QUERY_STRUCT.replaceAll("%sBYO", byOwnerType.toLowerCase())
                .replaceAll("%sMOD", ModifierContainer.generateClauselessModifierString(modifiers));


    }
    public RecordFunnel(String byOwnerType, Modifier modifier) {
        this(byOwnerType, new ArrayList<Modifier>(){{ add(modifier); }});
    }

    @Override
    public ArrayList<ColumnDescription> getColumns() {
        System.out.println("===Column Desc creation");
        ArrayList<ColumnDescription> cd = new ArrayList<>();

        cd.add(new ColumnDescription("title", ValueType.TEXT, "title"));
        System.out.println("Adding records as a number column");
        cd.add(new ColumnDescription("records", ValueType.NUMBER, "records"));

        return cd;
    }

    @Override
    public DataTable getData() {
        ArrayList<ColumnDescription> cd = getColumns();
        DataTable data = new DataTable();
        data.addColumns(cd);

        MySqlTunnelAccessHelper queryManager = new MySqlTunnelAccessHelper();
        //MySqlAccessHelper queryManager = new MySqlAccessHelper("localhost:3306", "scraper", "u23xhtu3");
        SqlResult result = queryManager.getQueryResults(QUERY, null);
        queryManager.close();

        if (result != null && !result.isEmpty()) {
            System.out.println("FUNNEL HAS DATA");
            List<TableRow> rowsToAdd = result.getRows().get(0).splitRowIntoGoogTableRows();
            try {
                data.addRows(rowsToAdd);
            } catch (TypeMismatchException e) {
                System.out.println("Invalid type!");
            }
            return data;
        }

        //need to return the row as separate columns
        return null;
    }

    @Override
    public String getFormattedQuery() {
        return QUERY;
    }
}
