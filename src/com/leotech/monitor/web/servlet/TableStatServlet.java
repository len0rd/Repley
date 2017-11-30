package com.leotech.monitor.web.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TableStatServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //String json = new Gson().toJson(someObject);

        //response.getWriter().write(json);
    }
}
