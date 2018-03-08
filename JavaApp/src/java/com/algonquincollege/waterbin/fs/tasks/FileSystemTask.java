/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.waterbin.fs.tasks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

    
/**
 *
 * @author Devon
 */
public abstract class FileSystemTask implements Runnable, Future<Boolean>{
    
    protected CountDownLatch latch;
    
    protected final String root = "D:\\fileHostRoot";
    
    protected boolean abort;
    protected boolean success;
    
    protected FileSystemTask(){
        latch = new CountDownLatch(1);
        success = false;
    }

    @Override
    public void run(){
        latch.countDown();
    }
    
    @Override
    public Boolean get() throws InterruptedException {
        latch.await();
        return success;
    }

    @Override
    public Boolean get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return success;
        } else {
            throw new TimeoutException();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    public boolean getSuccess() {
        return success;
    }
}
