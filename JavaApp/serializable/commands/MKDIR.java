package serializable.commands;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;

public class MKDIR implements Serializable{

	private static final long serialVersionUID = 3636619772744681249L;
	
	private String path;
	
	public MKDIR(String path) {
		this.path = path;
	}
	
	public void validatePath(String Path){
		boolean hasContent = (Path != null) && (!Path.equals(""));
		if(!hasContent) {
			throw new IllegalArgumentException("Must be non-null and non-empty.");
		}
		Path = Path.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(Path);
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
	
	public void readObject(ObjectInputStream istream) throws ClassNotFoundException, IOException{
		istream.defaultReadObject();
		validatePath(path);
	}
	
	public void writeObject(ObjectOutputStream ostream) throws IOException{
		ostream.defaultWriteObject();
	}
	
}