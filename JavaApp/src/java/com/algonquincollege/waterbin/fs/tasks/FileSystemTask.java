/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.tasks;
    
/**
 *
 * @author Devon
 */
public abstract class FileSystemTask implements Runnable{

    protected final String root = "D:\\fileHostRoot";
    
    protected boolean abort;
    protected boolean success;
    
    public abstract boolean getSuccess();
}
