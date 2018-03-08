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
    public static Map<String, String> map;
    
    public JSONParser() {
        this.map = new HashMap();
    }
    
    
    /*
    Parses the single object json if it matches the regex;returns false otherwise
    */
    public static boolean parse(String json, String regex){
        if(json.matches(regex))
        {
            try{
                //Trims the opening and closing brackets
                json=json.substring(1, json.length()-1);
                String[] rows = json.split(",");
                for (String row : rows) {
                    String[] temp = row.split(":");
                    map.put(temp[0].substring(1,temp[0].length()-1),temp[1].substring(1,temp[1].length()-1));
                }
                System.out.println("Parsed");
                return true;
            }catch(Exception e){return false;}
        }
        return false;
    }
    
    public static boolean parseLogin(String json){
        return parse(json, "\\{\\n*\"email\":\"\\w+@\\w+\\.\\w+\",\"password\":\"\\w+\"\\n*\\}");
    }
    
    public static boolean parseSignUp(String json){
        return parse(json, "\\{\"firstName\":\"[A-Za-z]+\",\"lastName\":\"[A-Za-z]+\",\"email\":\"\\w+@\\w+\\.\\w+\",\"username\":\"\\w+\",\"password\":\".+\"\\}");
    }
    
    public static boolean parseMkdir(String json){
        return parse(json, "\\{\\\"path\\\"\\:\\\"(\\/\\w+)+\\/?(\\w+)?\\\"\\}");
    }
    
    public static boolean parseCp(String json){
        return parse(json, "\\{\\\"from\\\"\\:\\\"((\\/\\w+)+\\/?(\\w+)?)?(\\.?\\w+)*\\\",\\\"to\\\"\\:\\\"((\\/\\w+)+\\/?(\\w+)?)?(\\.?\\w+)*\\\"\\}");
    }
    
    public static boolean parseMv(String json){
        return parse(json, "\\{\\\"from\\\"\\:\\\"((\\/\\w+)+\\/?(\\w+)?)?(\\.?\\w+)*\\\",\\\"to\\\"\\:\\\"((\\/\\w+)+\\/?(\\w+)?)?(\\.?\\w+)*\\\"\\}");
    }
    
    public static boolean parseRm(String json){
        return parse(json, "\\{\\\"path\\\"\\:\\\"((\\/\\w+)+\\/?(\\w+)?)?(\\.?\\w+)*\\\"\\}");
    }
    
    public static boolean parseList(String json){
        return parse(json, "\\{\\\"username\\\"\\:\\\"\\w+\\\",\\\"parent\\\"\\:\\\"(\\/\\w+)+\\/?(\\w+)?\\\"\\}");
    }
}
