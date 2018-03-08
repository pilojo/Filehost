/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.webhost.servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Devon St. John
 */


public class DownloadServlet extends HttpServlet {

    /**
     * UID to satisfy Serializable interface
     */
    private static final long serialVersionUID = 8321120808388866663L;

    /**
     * Temp file path
     */
    private static final String repopath = "D:\\fileHostRoot";
    //private static final String filepath = "\\data.txt";

    @Override
    public void init() {
        //System.out.println("I was just created");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Starting download...");
        
        String requestedFile = request.getPathInfo();
        
        System.out.println(requestedFile);

        // Check if file is actually supplied to the request URI.
        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        String filepath = URLDecoder.decode(requestedFile, "UTF-8");
        System.out.println(filepath);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        response.setContentType("APPLICATION/OCTET-STREAM");
        String[] paths = requestedFile.split("/");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + paths[paths.length - 1]);

        FileInputStream fileInputStream = new FileInputStream(Paths.get(repopath, filepath).toString());

        int i;
        while ((i = fileInputStream.read()) != -1) {
            out.write(i);
        }
        fileInputStream.close();
        out.close();
    }

}
