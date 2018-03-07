package com.algonquincollege.javaApp.fileManager.commands;
import com.algonquincollege.javaApp.fileManager.Command;
import java.text.StringCharacterIterator;

/**
 * 
 * @author John Pilon
 * A class to be serialized for Server to FSManager communication
 * Copy command
 */
public class CP extends Command{
	private String from;
	private String to;
        
        public CP(){
            from = null;
            to = null;
        }
        
        public String getFrom(){
            return from;
        }
        
        public String getTo(){
            return to;
        }
	
	public CP(String from, String to) {
		this.from = from;
		this.to = to;
                password = validPassword;
	}
	
        /**
        * Validates the path
        *
        * @param path The path to validate
        * @return boolean: whether the path is valid
        */
	private boolean validatePath(String path) {
		boolean hasContent = (path != null) && (!path.equals(""));
		if(!hasContent) {
			return false;
		}
		path = path.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(path);
		char c = it.current();
		while(c!=StringCharacterIterator.DONE) { 
			boolean isValidChar = (Character.isLetter(c)|| Character.isSpaceChar(c) ||c == '\\');
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
        @Override
        public String toString()
        {
            return from+"\n"+to+"\n"+password;
        }
        
        /**
        * Reconstructs the class from a serialized string
        *
        * @param str the serialized class
        * @return boolean: Whether the class reconstructed successfully
        */
        @Override
        public boolean fromString(String str)
        {
            String[] values = str.split("\n");
            if(values.length==3){
                from=values[0];
                to=values[1];
                password = values[2];
            }
            return isValid() && validatePath(to) && validatePath(from);
        }
	
}
