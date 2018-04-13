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
    public boolean parse(String toParse){
        char json[];
        byte pos=0;
        boolean valid;
        String entry;
        byte start;
        int end;
        try{
            json=toParse.toCharArray();
            this.map=new HashMap();
            valid=json[pos++]=='{';
            String key="";
            String value="";
            do{
                entry="";
                if(json[pos++]=='\"'){
                    start=pos;
                    while(json[pos++]!='\"'){if(json[pos]=='\\'){valid=false;break;}}
                    end=pos-1;
                    if(valid){entry=toParse.substring(start,end);}
                }
                if(valid&&json[pos++]==':'){
                    key=entry;
                    entry="";
                    if(json[pos++]=='\"'){
                        start=pos;
                        while(json[pos++]!='\"'){if(json[pos]=='\\'){valid=false;break;}}
                        end=pos-1;
                        //valid=valid&&start!=end;
                        if(valid){entry=toParse.substring(start,end);}
                    }
                    if(valid){value=entry;}
                }else{valid=false;}
                map.put(key, value);
            }while(valid&&json[pos]==',' && (pos++)>0);
            return valid&&json[pos++]=='}';
        }catch(Exception e){return false;}
    }
    
    public boolean parseLogin(String json){
        return parse(json);
    }
    
    public boolean parseSignUp(String json){
        return parse(json);
    }
    
    public boolean parseMkdir(String json){
        return parse(json);
    }
    
    public boolean parseCp(String json){
        return parse(json);
    }
    
    public boolean parseMv(String json){
        return parse(json);
    }
    
    public boolean parseRm(String json){
        return parse(json);
    }
    
    public boolean parseSearchFile(String json){
        return parse(json);
    }
    
    public boolean parseSearchUser(String json){
        return parse(json);
    }
    
    public boolean parseAddUserToGroup(String json){
        return parse(json);
    }
    
    public boolean parseRemoveUserFromGroup(String json){
        return parse(json);
    }
    
    public boolean parseChangePermission(String json){
        return parse(json);
    }
    
    public boolean parseCreateGroup(String json){
        return parse(json);
    }
    
    public boolean parseListUsersInGroup(String json){
        return parse(json);
    }
    
    public boolean parseDeleteGroup(String json){
        return parse(json);
    }
    
    public boolean parseListGroups(String json){
        return true;
    }
    
    public boolean parseShareWithGroup(String json){
        return parse(json);
    }
    
    public boolean parseListItemGroups(String json){
        return parse(json);
    }
    
    public boolean parseRemoveGroupFromItem(String json){
        return parse(json);
    }
}