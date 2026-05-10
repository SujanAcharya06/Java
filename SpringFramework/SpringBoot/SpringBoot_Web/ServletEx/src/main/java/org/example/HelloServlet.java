package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@WebServlet("/hello") this works when using external tomcat
public class HelloServlet extends HttpServlet {

    // `service()` is an important method in servlet
    // which gets executed whenever we send a request

    // to send data from client to server we need object
    // we can pass two parameters one for the request and one for response
    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("In service method");

        res.setContentType("text/html");

//        res.getWriter().println("Hello World");
//        or

        PrintWriter out = res.getWriter();
        out.println("<h1><b>Hello World</b><h2>");
    }
}
