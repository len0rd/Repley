package net.lenords.repley.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lenords.repley.datamanager.sql.Accessor;
import net.lenords.repley.datamanager.sql.ConnectionHelper;
import net.lenords.repley.model.chart.Chart;
import net.lenords.repley.model.chart.ChartData;
import net.lenords.repley.model.chart.ChartDataset;
import net.lenords.repley.model.chart.ChartDatasetAdaptor;
import net.lenords.repley.model.chart.ChartType;
import net.lenords.repley.model.queries.QueryDataset;
import net.lenords.repley.model.queries.QueryModel;
import net.lenords.repley.model.queries.QueryModelContainer;
import net.lenords.repley.model.sql.SqlResult;

@WebServlet("/repley/*")
public class ReporterServlet extends HttpServlet {


  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {}

  //TODO:This is a steaming pile of trash::
  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("q");
    String type  = request.getParameter("t");
    String byo   = request.getParameter("byo");
    String days  = request.getParameter("days");
    String value = request.getParameter("v");
    String byoType = byo != null && byo.equalsIgnoreCase("frbo") ? "frbo" : "fsbo";

    ConnectionHelper builder = new ConnectionHelper();
    System.out.println("Connection setup");
    Accessor sqlAccessor = builder.getAccessor();
    int typeId = byoType.equals("frbo") ? 2 : 1;

    if (param.equals("chart")) {
      Gson gson = new Gson();
      Chart chart = null;

      //NEWWWW:
      QueryModelContainer container = gson.fromJson(new FileReader(
              "/Users/tylermiller/code/Repley/conf/query_conf-sample-simple.json"),
          QueryModelContainer.class);

      String dayLimit = "LIMIT 30";
      if (days != null) {
        if (days.matches("\\d+")) {
          dayLimit = "LIMIT " + days;
        } else {
          dayLimit = "";
        }
      }

      switch (type) {
        case "funnel":
        case "stage":

          QueryModel model = container.getQueryByName(type);
          SqlResult result = sqlAccessor.getQueryResult(model.generateQueryFromParams(request));
          if (!result.isEmpty()) {
            chart = model.generateChartFromResult(result);
          }
          break;
        case "expototal":

          String ttlQuery = "SELECT DATE_FORMAT(expo.date, '%d %M %Y'), breakdown.complete "
              + "FROM re_stat.export_total AS expo "
              + "INNER JOIN re_stat.export_type ON expo.type_id = export_type.id "
              + "INNER JOIN re_stat.export_breakdown AS breakdown ON expo.breakdown_id = breakdown.id "
              + "WHERE expo.type_id = " + typeId
              + " ORDER BY date DESC "
              + dayLimit;
          SqlResult result2 = sqlAccessor.getQueryResult(ttlQuery);

          if (!result2.isEmpty()) {
            ChartDataset cds = new ChartDataset("Ads exported", result2.getColumns().get(1).getValues());
            cds.setBorderWidth(2);
            cds.generateSingleColorForDataset();
            ChartData chartData = new ChartData(result2.getColumns().get(0).getValues(), Collections.singletonList(cds));
            chart = new Chart(ChartType.LINE, chartData);

          }
          break;
        case "areas":

          String area = request.getParameter("a");
          SqlResult result3 = null;
          ChartType type1;


          if ((area == null || area.isEmpty()) && value != null) {
            type1 = ChartType.BAR;
            System.out.println("generating a state overview");
            String state = "IS NULL";
            state = !value.isEmpty() && !value.equals("null") && value.length() < 3 ? "= '" + value + "'" : state;
            String sumLimit = "totals.date > DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY) AND";
            if (days != null) {
              if (days.matches("\\d+")) {
                sumLimit = "totals.date > DATE_SUB(CURRENT_DATE, INTERVAL " + days + " DAY) AND";
              } else if (days.equals("X")) {
                sumLimit = "";
              }
            }

            //then we're throwing down a overview of the state as a bar chart:
            //this query is gettin a bit out of hand...
            String stateAreaTotal = "SELECT areas.area_name, CAST(IFNULL(SUM(`names`.complete), 0) AS UNSIGNED), CAST(IFNULL(SUM(`names`.sent), 0) AS UNSIGNED) "
                + "FROM re_stat.area_name_id AS areas "
                + "INNER JOIN re_stat.export_area_name AS `names` ON `names`.area_name_id = areas.id "
                + "INNER JOIN re_stat.export_total AS totals ON `names`.export_id = totals.id "
                + "WHERE " + sumLimit
                + " totals.type_id = " + typeId
                + " AND areas.area_st " + state
                + " GROUP BY areas.area_st, areas.area_name";

            System.out.println("QUERY::\n" + stateAreaTotal);
            result3 = sqlAccessor.getQueryResult(stateAreaTotal);

          } else {
            type1 = ChartType.LINE;
            //generate a line chart of history export
            String state = "IS NULL";
            state = value != null && !value.isEmpty() && !value.equals("null") && value.length() < 3 ? "= '" + value + "'" : state;
            String areaQuery = "IS NULL";
            areaQuery = area != null && !area.isEmpty() && !area.equals("null") ? "= '" + area + "'" : areaQuery;

            String areanameHist = "SELECT DATE_FORMAT(totals.date, '%d %M %Y'), CAST(IFNULL(`names`.complete, 0) AS UNSIGNED), CAST(IFNULL(`names`.sent, 0) AS UNSIGNED) "
                + "FROM re_stat.area_name_id AS areas "
                + "INNER JOIN re_stat.export_area_name AS `names` ON `names`.area_name_id = areas.id "
                + "INNER JOIN re_stat.export_total AS totals ON `names`.export_id = totals.id "
                + "WHERE totals.type_id = " + typeId
                + " AND areas.area_st " + state
                + " AND areas.area_name  " + areaQuery
                + "ORDER BY totals.date DESC "
                + dayLimit;

            result3 = sqlAccessor.getQueryResult(areanameHist);

          }

          if (result3 != null && !result3.isEmpty()) {

            ChartDataset expo = new ChartDataset("Ads exported", result3.getColumns().get(1).getValues());
            expo.setBorderWidth(1);

            ChartDataset sent = new ChartDataset("Ads sent", result3.getColumns().get(2).getValues());
            sent.setBorderWidth(1);

            ChartData data = new ChartData(result3.getColumns().get(0).getValues(), Arrays.asList(expo, sent));
            data.generateRandomColorsForData();
            chart = new Chart(type1, data);

          }
          break;
      }

      if (chart != null) {
        sendChartResult(response, chart);
      }


    } else if (param.equals("list")) {

      switch (type) {
        case "states":
          String getStates = "SELECT areas.area_st "
              + "FROM re_stat.area_name_id AS areas "
              + "INNER JOIN re_stat.export_area_name AS `names` ON `names`.area_name_id = areas.id "
              + "INNER JOIN re_stat.export_total AS totals ON `names`.export_id = totals.id "
              + "WHERE totals.type_id = " + typeId
              + " GROUP BY areas.area_st";
          SqlResult result = sqlAccessor.getQueryResult(getStates);
          if (!result.isEmpty()) {
            List states = result.getColumns().get(0).getValues();
            sendList(response, states);
          }
          break;
        case "areas":
          String state = "IS NULL";
          state = value != null && !value.equals("null") && value.length() < 3 ? "= '" + value + "'" : state;
          String getAreas = "SELECT areas.area_name "
              + "FROM re_stat.area_name_id AS areas "
              + "INNER JOIN re_stat.export_area_name AS `names` ON `names`.area_name_id = areas.id "
              + "INNER JOIN re_stat.export_total AS totals ON `names`.export_id = totals.id "
              + "WHERE totals.type_id = " + typeId + " and areas.area_st " + state + " "
              + "GROUP BY areas.area_name";
          SqlResult areaResult = sqlAccessor.getQueryResult(getAreas);
          if (!areaResult.isEmpty()) {
            List areas = areaResult.getColumns().get(0).getValues();
            sendList(response, areas);
          }
          break;
      }

    }

    System.out.println("Done");
    sqlAccessor.close();

  }

  private void sendChartResult(HttpServletResponse response, Chart chart) throws IOException {
    final GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(ChartDataset.class, new ChartDatasetAdaptor());
    final Gson gson = gsonBuilder.create();
    System.out.println("Serialize!");
    String jsonResult = gson.toJson(chart, Chart.class);
    System.out.println(jsonResult);
    response.setContentType("application/json");
    response.getWriter().write(jsonResult);


  }

  private void sendList(HttpServletResponse response, List values) throws IOException{
    final Gson gson = new Gson();
    String json = gson.toJson(values);
    System.out.println(values);
    response.setContentType("application/json");
    response.getWriter().write(json);
  }

}
