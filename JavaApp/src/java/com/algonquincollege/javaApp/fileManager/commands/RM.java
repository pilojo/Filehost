//lol

package com.algonquincollege.javaApp.fileManager.commands;


import com.algonquincollege.javaApp.fileManager.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.StringCharacterIterator;

public class RM extends Command implements Serializable{

	private static final long serialVersionUID = -8022882916338366053L;
	
	private String dst;
	
	public RM(String dst){
		this.dst = dst;
	}
	
	private void validatePath(String dst) {
		boolean hasContent = (dst != null) && (!dst.equals(""));
		if(!hasContent) {
			throw new IllegalArgumentException("Must be non-null and non-empty.");
		}
		dst = dst.replace('/', '\\');
		StringCharacterIterator it = new StringCharacterIterator(dst);
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
		validatePath(dst);
	}
	
	public void writeObject(ObjectOutputStream ostream) throws IOException{
		ostream.defaultWriteObject();
	}
}