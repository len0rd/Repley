package com.leotech.monitor.model.result.group;

import com.leotech.monitor.datamanager.sql.MySqlTunnelAccessHelper;
import com.leotech.monitor.model.modifier.Modifier;
import com.leotech.monitor.model.modifier.ModifierContainer;
import com.leotech.monitor.model.result.QueryResult;
import com.leotech.monitor.model.sql.Row;
import com.leotech.monitor.model.sql.SqlResult;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GroupByCount implements QueryResult {
    private static final String BASIC_QUERY_STRUCT = "SELECT %s0 COUNT(*) FROM %s1 %s2 GROUP BY %s3";
    private String query;
    private List<String> columns;
    private String countMask;

    public GroupByCount(List<String> selectWithCount, String table, String groupByColumn, @Nullable List<Modifier> modifiers) {
        StringBuilder selects = new StringBuilder("");
        for (String column : selectWithCount) {
            selects.append(column).append(", ");
        }
        this.columns = selectWithCount;
        this.countMask = "Count";

        //build the query, replacing all our placeholders with their actual values
        query = BASIC_QUERY_STRUCT.replace("%s0", selects.toString()).replace("%s1", table)
                .replace("%s2", ModifierContainer.generateModifierString(modifiers)).replace("%s3", groupByColumn);


    }
    public GroupByCount(String selectWithCount, String table, String groupByColumn, List<Modifier> modifiers) {
        this(new ArrayList<String>() {{
            add(selectWithCount); }}, table, groupByColumn, modifiers);
    }
    public GroupByCount(String selectWithCount, String table, String groupByColumn, @Nullable Modifier modifier) {
        this(new ArrayList<String>() {{
            add(selectWithCount); }}, table, groupByColumn,
                (modifier != null ? new ArrayList<Modifier>(){{ add(modifier); }} : null));
    }
    public GroupByCount(String selectWithCount, String table, String groupByColumn) {
        this(new ArrayList<String>() {{
            add(selectWithCount); }}, table, groupByColumn, null);
    }

    public void setCountColumnMask(String countMask) {
        this.countMask = countMask;
    }

    @Override
    public ArrayList<ColumnDescription> getColumns() {
        ArrayList<ColumnDescription> cd = new ArrayList<>();
        for (String column : columns) {
            cd.add(new ColumnDescription(column, ValueType.TEXT, column));
        }
        cd.add(new ColumnDescription(countMask, ValueType.NUMBER, countMask));

        return cd;
    }

    @Override
    public DataTable getData() {
        ArrayList<ColumnDescription> cd = getColumns();
        DataTable data = new DataTable();
        data.addColumns(cd);

        MySqlTunnelAccessHelper queryManager = new MySqlTunnelAccessHelper();
        //MySqlAccessHelper queryManager = new MySqlAccessHelper("localhost:3306", "scraper", "u23xhtu3");
        System.out.println("query to run::" + query);

        SqlResult result = queryManager.getQueryResults(query, null);
        queryManager.close();

        if (result != null && !result.isEmpty()) {
            for (Row r : result.getRows()) {
                if (!r.isEmpty()) {
                    try {
                       data.addRow(r.getGoogTableRow());
                    } catch (TypeMismatchException e) {
                        System.out.println("Invalid type!");
                    }
                }
            }
            return data;
        }

        return null;
    }

    @Override
    public String getFormattedQuery() {
        return query;
    }

}
