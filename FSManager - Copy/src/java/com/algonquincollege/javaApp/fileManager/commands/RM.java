//lol

package com.algonquincollege.javaApp.fileManager.commands;

import com.algonquincollege.javaApp.fileManager.Command;
import java.text.StringCharacterIterator;

/**
 * 
 * @author John Pilon
 * A class to be serialized for Server to FSManager communication
 * Make Directory command
 */
public class RM extends Command{
	
	private String path;
        
        public RM(){
            path = null;
        }
	
	public RM(String dst){
		this.path = dst;
	}
	
        public String getPath(){
            return path;
        }
        
        /**
        * Validates the path
        *
        * @param dst The path to validate
        * @return boolean: whether the path is valid
        */
	private boolean validatePath(String dst) {
		boolean hasContent = (dst != null) && (!dst.equals(""));
		if(!hasContent) {
			return false;
		}
		dst = dst.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(dst);
		char c = it.current();
		while(c!=StringCharacterIterator.DONE) { 
			boolean isValidChar = (Character.isLetter(c)|| Character.isSpaceChar(c) ||c == '\\' || c == '.');
			if(!isValidChar) {
				return false;
			}
			c = it.next();
		}		
                return true;
	}
	
        /**
        * Serializes the class
        *
        * @return String: serialized class
        */
	public String toString(){
            return path+"\n"+password;
	}
	/**
        * Reconstructs the class from a serialized string
        *
        * @param str the serialized class
        * @return boolean: Whether the class reconstructed successfully
        */
	public boolean fromString(String str){
            String[] values = str.split("\n");
            if(values.length==2){
                path=values[0];
                password = values[1];
                return isValid() && validatePath(path);
            }
            return false;
        }
}