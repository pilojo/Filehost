//lol

package serializable.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;

public class RM implements Serializable{
	
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
        
	private boolean validatePath(String dst) {
		boolean hasContent = (dst != null) && (!dst.equals(""));
		if(!hasContent) {
			return false;
		}
		dst = dst.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(dst);
		char c = it.current();
		while(c!=StringCharacterIterator.DONE) { 
			boolean isValidChar = (Character.isLetter(c)|| Character.isSpaceChar(c) ||c == '\\');
			if(!isValidChar) {
				String message = "Can only contain letters, spaces, and backslashes";
				return false;
			}
			c = it.next();
		}		
                return true;
	}
	
	public String toString(){
            return path;
	}
	
	public boolean fromString(String str){
            path = str;
            return validatePath(path);
        }
}