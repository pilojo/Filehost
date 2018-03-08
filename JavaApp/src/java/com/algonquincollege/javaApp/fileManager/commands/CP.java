package com.algonquincollege.javaApp.fileManager.commands;
/**
 * 
 * @author John Pilon
 * A class to be serialized for Server to FSManager communication
 * Copy command
 */
import com.algonquincollege.javaApp.fileManager.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;

public class CP extends Command implements Serializable{

	protected final long serialVersionUID = 3809268679719475381L;
        
	private String from;
	private String to;
	
	public CP(String from, String to) {
		this.from = from;
		this.to = to;
	}
	
	private void validateState() {
		validatePath(this.from);
		validatePath(this.to);
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
			boolean isValidChar = (Character.isLetter(c)|| Character.isSpaceChar(c) ||c == '\\' || c=='.');
			if(!isValidChar) {
				String message = "Can only contain letters, spaces, and backslashes";
				throw new IllegalArgumentException(message);
			}
			c = it.next();
		}		
	}
	
	public void readObject(ObjectInputStream istream) throws ClassNotFoundException, IOException {
		istream.defaultReadObject();
		validateState();
	}
	
	public void writeObject(ObjectOutputStream ostream) throws IOException{
		ostream.defaultWriteObject();
	}
	
}
