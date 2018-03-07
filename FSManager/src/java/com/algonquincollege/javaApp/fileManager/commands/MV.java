package com.algonquincollege.javaApp.fileManager.commands;
import com.algonquincollege.javaApp.fileManager.Command;
import java.text.StringCharacterIterator;

/**
 * 
 * @author John Pilon
 * A class to be serialized for Server to FSManager communication
 * Make Directory command
 */
public class MV extends Command{

	private String from;
	private String to;
	
        public MV(){
            from = null;
            to = null;
        }
        
        public String getFrom(){
            return from;
        }
        
        public String getTo(){
            return to;
        }
        
	public MV(String from, String to) {
		this.from = from;
		this.to = to;
	}

        /**
        * Validates the path
        *
        * @param from The path to validate
        * @return boolean: whether the path is valid
        */
	private boolean validatePath(String from) {
		boolean hasContent = (from != null) && (!from.equals(""));
		if(!hasContent) {
			return false;
		}
		from = from.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(from);
		char c = it.current();
		while(c!=StringCharacterIterator.DONE) { 
			boolean isValidChar = (Character.isLetter(c)|| Character.isSpaceChar(c) ||c == '\\' || c == '.');
			if(!isValidChar) {
				String message = "Can only contain letters, spaces, and backslashes";
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
            return from+"\n"+to+"\n"+password;
        }

        /**
        * Reconstructs the class from a serialized string
        *
        * @param str the serialized class
        * @return boolean: Whether the class reconstructed successfully
        */
	public boolean fromString(String str){
            String[] values = str.split("\n");
            if(values.length==3){
                from=values[0];
                to=values[1];
                password = values[2];
                return isValid() && validatePath(to) && validatePath(from);
            }else return false;
	}
	
}
