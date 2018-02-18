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
      HttpServletResponse response) throws ServletException, IOException {}

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    String param = request.getParameter("q");
    if (param.equals("test")) {
      String data = "General Kenobi!" + param;
      response.setContentType("text/plain");
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(data);
    } else {
      int[] dataPnts = {1, 3, 5, 4, 2, 6, 7};
      Gson gson = new Gson();

      String jsonResult = gson.toJson(dataPnts);
      System.out.println(jsonResult);
      response.setContentType("application/json");
      response.getWriter().write(jsonResult);
    }

  }

}
