package net.lenords.repley.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lenords.repley.datamanager.sql.Accessor;
import net.lenords.repley.datamanager.sql.ConnectionHelper;
import net.lenords.repley.model.chart.Chart;
import net.lenords.repley.model.chart.ChartDataset;
import net.lenords.repley.model.chart.ChartDatasetAdaptor;
import net.lenords.repley.model.queries.QueryModel;
import net.lenords.repley.model.queries.QueryModelContainer;
import net.lenords.repley.model.sql.SqlResult;

@WebServlet("/repley/*")
public class ReporterServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {}

  // TODO:This is a steaming pile of trash::
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("incoming query string:: " + request.getQueryString());
    String param = request.getParameter("q");
    String type = request.getParameter("t");
    String byo = request.getParameter("byo");
    String days = request.getParameter("days");
    String value = request.getParameter("v");
    String byoType = byo != null && byo.equalsIgnoreCase("frbo") ? "frbo" : "fsbo";

    ConnectionHelper builder = new ConnectionHelper();
    System.out.println("Connection setup");
    Accessor sqlAccessor = builder.getAccessor();
    int typeId = byoType.equals("frbo") ? 2 : 1;

    if (param.equals("chart")) {
      Gson gson = new Gson();
      Chart chart = null;

      String jsonToLoad = "";
      try {
        jsonToLoad = getRunningAbsolutePath() + "conf/byo-query.json";
      } catch (Exception e) {
        System.out.println("ERR:: Failed to get path: '" + jsonToLoad + "'");
      }

      QueryModelContainer container =
          gson.fromJson(new FileReader(jsonToLoad), QueryModelContainer.class);

      QueryModel model = container.getQueryByName(type);
      SqlResult result =
          model.executeQueryFromParams(
              sqlAccessor,
              request); // sqlAccessor.getQueryResult(model.generateQueryFromParams(request));
      if (!result.isEmpty()) {
        chart = model.generateChartFromResult(result);
      }

      if (chart != null) {
        sendChartResult(response, chart);
      }

    } else if (param.equals("list")) {

      switch (type) {
        case "states":
          String getStates =
              "SELECT areas.area_st "
                  + "FROM re_stat.area_name_id AS areas "
                  + "INNER JOIN re_stat.export_area_name AS `names` ON `names`.area_name_id = areas.id "
                  + "INNER JOIN re_stat.export_total AS totals ON `names`.export_id = totals.id "
                  + "WHERE totals.type_id = "
                  + typeId
                  + " GROUP BY areas.area_st";
          SqlResult result = sqlAccessor.getQueryResult(getStates);
          if (!result.isEmpty()) {
            List states = result.getColumns().get(0).getValues();
            sendList(response, states);
          }
          break;
        case "areas":
          String state = "IS NULL";
          state =
              value != null && !value.equals("null") && value.length() < 3
                  ? "= '" + value + "'"
                  : state;
          String getAreas =
              "SELECT areas.area_name "
                  + "FROM re_stat.area_name_id AS areas "
                  + "INNER JOIN re_stat.export_area_name AS `names` ON `names`.area_name_id = areas.id "
                  + "INNER JOIN re_stat.export_total AS totals ON `names`.export_id = totals.id "
                  + "WHERE totals.type_id = "
                  + typeId
                  + " and areas.area_st "
                  + state
                  + " "
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

  private void sendList(HttpServletResponse response, List values) throws IOException {
    final Gson gson = new Gson();
    String json = gson.toJson(values);
    System.out.println(values);
    response.setContentType("application/json");
    response.getWriter().write(json);
  }

  private static String getRunningAbsolutePath() throws URISyntaxException {
    String path = getAbsoluteJarPath();
    path = path.substring(0, path.lastIndexOf(File.separator) + 1);
    return path;
  }

  private static String getAbsoluteJarPath() throws URISyntaxException {
    return ReporterServlet.class
        .getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .toURI()
        .getPath();
  }
}
