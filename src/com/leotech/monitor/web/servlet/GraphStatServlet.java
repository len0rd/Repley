package com.leotech.monitor.web.servlet;

import com.leotech.monitor.model.modifier.Modifier;
import com.leotech.monitor.model.modifier.ModifierContainer;
import com.leotech.monitor.model.request.QueryType;
import com.leotech.monitor.model.result.QueryResult;
import com.leotech.monitor.model.result.group.byowner.RecordFunnel;
import com.leotech.monitor.model.result.group.byowner.RecordsByRobot;
import com.leotech.monitor.model.result.group.byowner.RecordsByStage;
import com.google.visualization.datasource.DataSourceServlet;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.query.Query;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class GraphStatServlet extends DataSourceServlet{

    private static final String QUERY_PARAM_STR = "q";
    private static final String MOD_PARAM_STR = "m";
    private static final String BY_OWNER_PARAM_STR = "byo";

    @Override
    public DataTable generateDataTable(Query query, HttpServletRequest request) {

        //ComputerResource.getInfo();

        QueryResult qr = parseParams(request);

        assert qr != null;

        return qr.getData();
        //GroupByCount gbc = new RecordsByStage();

        //return gbc.getData();

        /*
        // Create a data table.
        DataTable data = new DataTable();
        ArrayList<ColumnDescription> cd = new ArrayList<>();
        cd.add(new ColumnDescription("stage", ValueType.TEXT, "Toppings"));
        cd.add(new ColumnDescription("count", ValueType.NUMBER, "Count"));

        data.addColumns(cd);

        // Fill the data table.
        try {
            data.addRowFromValues("0", 3291);
            data.addRowFromValues("2", 36363);
            data.addRowFromValues("no-pe", 14162);
            data.addRowFromValues("CL Lookup unassigned", 6477);
        } catch (TypeMismatchException e) {
            System.out.println("Invalid type!");
        }
        return data;*/
    }

    private QueryResult parseParams(HttpServletRequest request) {
        String queryArg     = request.getParameter(QUERY_PARAM_STR);
        String modifierStr  = request.getParameter(MOD_PARAM_STR);
        String byOwnerType  = request.getParameter(BY_OWNER_PARAM_STR);
        QueryResult queryToRun = null;
        System.out.println("Query==" + queryArg + ", mod==" + modifierStr + ", byo==" + byOwnerType);

        if (queryArg != null && !queryArg.isEmpty()) {
            if (queryArg.equalsIgnoreCase(QueryType.byownerfunnel.name())) {
                queryToRun = new RecordFunnel(byOwnerType, ModifierContainer.getModifiers(modifierStr));
            }
            else if (queryArg.equalsIgnoreCase(QueryType.stagevalues.name())) {
                queryToRun = new RecordsByStage(byOwnerType);
            }
            else if (queryArg.equalsIgnoreCase(QueryType.threadhistory.name())) {

            }
            else if (queryArg.equalsIgnoreCase(QueryType.robotvalues.name())) {
                System.out.println("Made it into the robot values!!");
                List<Modifier> modifiers = ModifierContainer.getModifiers(modifierStr);
                if (modifiers != null) {
                    for (Modifier mod : modifiers) {
                        mod.setParam(new String[]{"firstExtractionDate"});
                    }
                }
                queryToRun = new RecordsByRobot(byOwnerType, modifiers);
            }
        }
        return queryToRun;
    }

    /**
     * NOTE: By default, this function returns true, which means that cross
     * domain requests are rejected.
     * This check is disabled here so examples can be used directly from the
     * address bar of the browser. Bear in mind that this exposes your
     * data source to xsrf attacks.
     * If the only use of the data source url is from your application,
     * that runs on the same domain, it is better to remain in restricted mode.
     */
    @Override
    protected boolean isRestrictedAccessMode() {
        return false;
    }
}
