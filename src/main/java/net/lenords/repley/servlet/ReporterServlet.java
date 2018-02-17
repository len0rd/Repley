package net.lenords.repley.servlet;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/repley/*")
public class ReporterServlet extends HttpServlet {


  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    /*
    int[] dataPnts = {1, 3, 5, 4, 2, 6, 7};
    Gson gson = new Gson();

    String jsonResult = gson.toJson(dataPnts);
    System.out.println(jsonResult);
    response.setContentType("application/json");
    response.getWriter().write(jsonResult);
    */

  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    String data = "General Kenobi!";
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(data);
  }

  /*
  <canvas id="myChart" width="400" height="200"></canvas>
  <script>
  var ctx = document.getElementById("myChart").getContext('2d');
  var myChart = new Chart(ctx,
      {
    type: 'line',
    data: {
      labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
      datasets: [{
        label: '# of Votes',
        data: [12, 19, 3, 5, 2, 3],
        backgroundColor: [
          'rgba(255, 99, 132, 0.2)'
        ],
        borderColor: [
          'rgba(255,99,132,1)'
        ],
        borderWidth: 1
      },
      {
        label: '# of People',
        data: [3, 19, 15, 2, 2, 10],
        backgroundColor: [
          'rgba(10,15,255,0.1)'
        ],
        borderColor: [
          'rgba(10,15,255,1)'
        ],
        borderWidth: 1
      }]
    },
      options: {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero:true
            }
          }]
        }
      }
  });
  </script>*/
}
