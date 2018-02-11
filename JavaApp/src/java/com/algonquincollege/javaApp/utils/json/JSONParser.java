/*
A simple json parser that only splits 1 scope json files. does not handle objects. Should be replaced
*/
package com.algonquincollege.javaApp.utils.json;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Byzantian
 */
public class JSONParser
{
    public Map<String, String> map;
    
    public JSONParser() {
        this.map = new HashMap();
    }
    
    
    /*
    Parses the single object json if it matches the regex;returns false otherwise
    */
    public boolean parse(String json, String regex){
        if(json.matches(regex))
        {
            try{
                //Trims the opening and closing brackets
                json=json.substring(1, json.length()-1);
                String[] rows = json.split(",");
                for(int i=0;i<rows.length;i++){
                    String[] temp = rows[i].split(":");
                    map.put(temp[0].substring(0,temp[0].length()-1),temp[1].substring(0,temp[1].length()-1));
                }
                return true;
            }catch(Exception e){return false;}
        }
        return false;
    }
    
    public boolean parseLogin(String json)
    {
        return parse(json, "\\{\"username\":\"([a-z]|[A-Z]|[0-9])+\"\\}");
    }
}
