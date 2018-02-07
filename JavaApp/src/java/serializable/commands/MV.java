package serializable.commands;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;

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

	private boolean validatePath(String from) {
		boolean hasContent = (from != null) && (!from.equals(""));
		if(!hasContent) {
			return false;
		}
		from = from.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(from);
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
            return from+"\n"+to;
        }
	
	public boolean fromString(String str){
            String[] values = str.split("\n");
            if(values.length==3){
                from=values[0];
                to=values[1];
                password = values[2];
            }if(validatePath(from))return validatePath(to);
            else return false;
	}
	
}
