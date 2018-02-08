/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webhost;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import database.DBConnection;
/**
 *
 * @author jp972
 */
public class UserServices extends HttpServlet{
    private DBConnection db = new DBConnection();
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        System.out.println("POST");
        db.connect();
        System.out.println(db.login("ass", "shit") ? "true" : "false");
        db.disconnect();
    }
}
