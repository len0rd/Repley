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
import net.lenords.repley.model.sql.SqlResult;
import org.json.JSONArray;

@WebServlet("/repley/*")
public class ReporterServlet extends HttpServlet {


  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {}

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("q");

    if (param.equals("chart")) {
      ConnectionHelper builder = new ConnectionHelper();
      System.out.println("Connection setup");
      Accessor sqlAccessor = builder.getAccessor();
      String query = "SELECT est, cst, mst, pst FROM re_stat.export_total WHERE type_id = 1 AND date > DATE_SUB(CURDATE(), INTERVAL 1 DAY)";
      SqlResult result = sqlAccessor.getQueryResult(query);
      System.out.println("Queried");

      if (!result.isEmpty()) {
        System.out.println("result isnt empty");
        List<Integer> data = Arrays.asList(result.getRows().get(0).getValues());
        System.out.println(data.toString());
        ChartDataset cds = new ChartDataset("# of ads", data);
        cds.setBorderWidth(1);
        cds.generateRandomColorsForDataset();
        ChartData cd = new ChartData(Arrays.asList("est", "cst", "mst", "pst"),
            Collections.singletonList(cds));
        Chart chart = new Chart("pie", cd);
        System.out.println("Built Chart");
        Gson gson = new Gson();

        //JSONArray toReturn = result.getRows().get(0).valuesToArray();
        System.out.println("Serialize!");
        String jsonResult = gson.toJson(chart, Chart.class);
        System.out.println(jsonResult);
        response.setContentType("application/json");
        response.getWriter().write(jsonResult);
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

}
