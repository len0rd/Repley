package net.lenords.repley.servlet;

import com.google.gson.Gson;
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
import net.lenords.repley.model.chart.ChartType;
import net.lenords.repley.model.sql.SqlResult;
import org.json.JSONArray;

@WebServlet("/repley/*")
public class ReporterServlet extends HttpServlet {


  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {}

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("q");
    String type  = request.getParameter("t");

    if (param.equals("chart")) {
      ConnectionHelper builder = new ConnectionHelper();
      System.out.println("Connection setup");
      Accessor sqlAccessor = builder.getAccessor();

      if (type.equals("stage")) {
        String query = "SELECT stage, COUNT(*) FROM re_fsbo_front.re_fsbo GROUP BY stage";
        SqlResult result = sqlAccessor.getQueryResult(query);
        System.out.println("Queried");

        if (!result.isEmpty()) {
          ChartDataset cds = new ChartDataset("# of ads", result.getColumns().get(1).getValues());
          cds.setBorderWidth(1);
          cds.generateRandomColorsForDataset();
          ChartData cData = new ChartData(result.getColumns().get(0).getValues(), Collections.singletonList(cds));
          Chart chart = new Chart(ChartType.PIE, cData);
          System.out.println("Built Chart");

          sendChartResult(response, chart);
        }

      } else {
        String query = "SELECT est, cst, mst, pst FROM re_stat.export_total WHERE type_id = 1 AND date > DATE_SUB(CURDATE(), INTERVAL 1 DAY)";
        SqlResult result = sqlAccessor.getQueryResult(query);
        System.out.println("Queried");

        if (!result.isEmpty()) {
          List<Integer> data = Arrays.asList(result.getRows().get(0).getValues());
          ChartDataset cds = new ChartDataset("# of ads", data);
          cds.setBorderWidth(1);
          cds.generateRandomColorsForDataset();
          ChartData cd = new ChartData(Arrays.asList(result.getRows().get(0).getKeys()),
              Collections.singletonList(cds));
          Chart chart = new Chart(ChartType.PIE, cd);
          System.out.println("Built Chart");

          sendChartResult(response, chart);
        }
      }

      System.out.println("Done");
      sqlAccessor.close();
    }


    /*if (param.equals("test")) {
      String data = "General Kenobi!" + param;
      response.setContentType("text/plain");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(data);
    } else {
      int[] dataPnts = {1, 3, 5, 4, 2, 6};
      Gson gson = new Gson();

      String jsonResult = gson.toJson(dataPnts);
      System.out.println(jsonResult);
      response.setContentType("application/json");
      response.getWriter().write(jsonResult);
    }*/

  }

  private void sendChartResult(HttpServletResponse response, Chart chart) throws IOException {
    Gson gson = new Gson();
    System.out.println("Serialize!");
    String jsonResult = gson.toJson(chart, Chart.class);
    System.out.println(jsonResult);
    response.setContentType("application/json");
    response.getWriter().write(jsonResult);
  }

}
