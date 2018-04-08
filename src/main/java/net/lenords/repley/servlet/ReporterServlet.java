package net.lenords.repley.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import net.lenords.repley.model.queries.QueryModelContainer;
import net.lenords.repley.model.sql.SqlResult;
import org.json.JSONArray;

@WebServlet("/repley/*")
public class ReporterServlet extends HttpServlet {


  private String type;

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {}

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("q");
    String type  = request.getParameter("t");
    String byo   = request.getParameter("byo");
    String days  = request.getParameter("days");

    if (param.equals("chart")) {
      String byoType = byo != null && byo.equalsIgnoreCase("frbo") ? "frbo" : "fsbo";
      int typeId = byoType.equals("frbo") ? 2 : 1;

      ConnectionHelper builder = new ConnectionHelper();
      System.out.println("Connection setup");
      Accessor sqlAccessor = builder.getAccessor();


      if (type.equals("stage")) {
        String query = "SELECT stage, COUNT(*) FROM re_" + byoType + "_front.re_fsbo GROUP BY stage";
        SqlResult result = sqlAccessor.getQueryResult(query);

        if (!result.isEmpty()) {
          ChartDataset cds = new ChartDataset("# of ads", result.getColumns().get(1).getValues());
          cds.setBorderWidth(1);
          cds.generateRandomColorsForDataset();
          ChartData cData = new ChartData(result.getColumns().get(0).getValues(), Collections.singletonList(cds));
          Chart chart = new Chart(ChartType.PIE, cData);

          sendChartResult(response, chart);
        }

      } else if (type.equals("funnel")) {

        String query = "SELECT ( "
            + "  SELECT COUNT(*)  "
            + "  FROM re_%byo_front.re_fsbo "
            + "  WHERE re_%byo_front.re_fsbo.latestExtractionDate > CURDATE() "
            + ") AS frontendTotalCount, "
            + "( "
            + "  SELECT COUNT(*)  "
            + "  FROM re_%byo_front.re_fsbo "
            + "  WHERE re_%byo_front.re_fsbo.firstExtractionDate > CURDATE() "
            + ") AS frontendUniqueCount, "
            + "( "
            + " SELECT COUNT(*)  "
            + " FROM re_%byo_front.re_fsbo "
            + " WHERE re_%byo_front.re_fsbo.firstExtractionDate > CURDATE() AND (stage = '0' OR stage = '2' OR stage like '1%') "
            + ") AS frontendProcessableCount, "
            + "( "
            + " SELECT COUNT(*)  "
            + " FROM re_%byo_back.re_fsbo "
            + " WHERE re_%byo_back.re_fsbo.firstExtractionDate > CURDATE() "
            + ") AS stagerCount, "
            + "( "
            + " SELECT COUNT(*)  "
            + " FROM re_%byo_back.re_fsbo_complete "
            + " WHERE re_%byo_back.re_fsbo_complete.firstExtractionDate > CURDATE() "
            + ") AS completeCount, "
            + "( "
            + " SELECT COUNT(*)  "
            + " FROM re_%byo_back.re_fsbo_complete "
            + " WHERE re_%byo_back.re_fsbo_complete.firstExtractionDate > CURDATE() "
            + "    AND (Area_Name IS NULL OR Area_Name<>'Other') "
            + ") AS validExport "
            + "FROM DUAL";
        query = query.replace("%byo", byoType);
        SqlResult result = sqlAccessor.getQueryResult(query);

        if (!result.isEmpty()) {
          ChartDataset cds = new ChartDataset("# of Ads",
              Arrays.asList(result.getRows().get(0).getValues()));
          cds.setBorderWidth(2);
          cds.generateSingleColorForDataset();
          ChartData chartData = new ChartData(Arrays.asList(result.getRows().get(0).getKeys()), Collections.singletonList(cds));
          Chart chart = new Chart(ChartType.LINE, chartData);

          sendChartResult(response, chart);
        }

      } else if (type.equals("expototal")) {

        String dayLimit = "LIMIT 30";
        if (days != null) {
          if (days.equals("7")) {
            dayLimit = "LIMIT 7";
          } else if (days.equals("X")) {
            dayLimit = "";
          }
        }

        String query = "SELECT DATE_FORMAT(expo.date, '%d %M %Y'), breakdown.complete "
            + "FROM re_stat.export_total AS expo "
            + "INNER JOIN re_stat.export_type ON expo.type_id = export_type.id "
            + "INNER JOIN re_stat.export_breakdown AS breakdown ON expo.breakdown_id = breakdown.id "
            + "WHERE expo.type_id = " + typeId
            + " ORDER BY date DESC "
            + dayLimit;
        SqlResult result = sqlAccessor.getQueryResult(query);

        if (!result.isEmpty()) {
          ChartDataset cds = new ChartDataset("Ads exported", result.getColumns().get(1).getValues());
          cds.setBorderWidth(2);
          cds.generateSingleColorForDataset();
          ChartData chartData = new ChartData(result.getColumns().get(0).getValues(), Collections.singletonList(cds));
          Chart chart = new Chart(ChartType.LINE, chartData);

          sendChartResult(response, chart);
        }

      }

      System.out.println("Done");
      sqlAccessor.close();
    }

  }


  private void importQueries() {
    Gson gson = new Gson();
    System.out.println("Import Query info");
    //QueryModelContainer qmc = gson.fromJson();
  }

  private void sendChartResult(HttpServletResponse response, Chart chart) throws IOException {
    final GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(ChartDataset.class, new ChartDatasetAdaptor());
    final Gson gson = gsonBuilder.create();
    //System.out.println("Serialize!");
    String jsonResult = gson.toJson(chart, Chart.class);
    response.setContentType("application/json");
    response.getWriter().write(jsonResult);
  }

}
