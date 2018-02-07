package serializable.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;

public class MKDIR extends Command{
	
	private String path;
	
        public MKDIR(){
            path = null;
        }
        
	public MKDIR(String path) {this.path = path;}
	
	public boolean validatePath(String Path){
		boolean hasContent = (Path != null) && (!Path.equals(""));
		if(!hasContent) {
			return false;
		}
		Path = Path.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(Path);
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
	
	public String toString()
        {
            return path;
        }
        
        public boolean fromString(String str)
        {
            String[] serial = str.split("\n");
            if(serial.length == 2){
                path = serial[0];
                password = serial[1];
            }
            return validatePath(path);
        }
	
}