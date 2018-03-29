/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.transferTasks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

    
/**
 *
 * @author Devon
 */
public abstract class TransferTask implements Runnable{
    
    protected final String root = "D:\\fileHostRoot";
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    public TransferTask(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    public abstract boolean getSuccess();
}
