/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.algonquincollege.javaApp.fileManager.utils;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jp972
 */
public class ByteReconstruct {
    public static String byteToString(HttpServletRequest request) throws IOException{
        byte[] tmpdata;
        byte[] bytes = new byte[1];
        //Beginning of String reconstruction
        ArrayList<Byte> bindata = new ArrayList();
        while(-1 != request.getInputStream().read(bytes)){
            bindata.add(bytes[0]);
        }
        tmpdata = new byte[bindata.size()];
        for(int i = 0; i < bindata.size(); i++){
            tmpdata[i] = bindata.get(i);
        }//End of String reconstruction
        return new String(tmpdata);
    }
}
