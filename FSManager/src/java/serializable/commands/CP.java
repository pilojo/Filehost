package serializable.commands;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;

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
	}
	
	private void validatePath(String from) {
		boolean hasContent = (from != null) && (!from.equals(""));
		if(!hasContent) {
			throw new IllegalArgumentException("Must be non-null and non-empty.");
		}
		from = from.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(from);
		char c = it.current();
		while(c!=StringCharacterIterator.DONE) { 
			boolean isValidChar = (Character.isLetter(c)|| Character.isSpaceChar(c) ||c == '\\');
			if(!isValidChar) {
				String message = "Can only contain letters, spaces, and backslashes";
				throw new IllegalArgumentException(message);
			}
			c = it.next();
		}		
	}
        
        @Override
        public String toString()
        {
            return from+"\n"+to+"\n"+password;
        }
        
        @Override
        public boolean fromString(String str)
        {
            String[] values = str.split("\n");
            if(values.length==3){
                from=values[0];
                to=values[1];
                password = values[2];
            }
            return false;
        }
	
}
