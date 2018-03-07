package com.algonquincollege.javaApp.fileManager.commands;

import com.algonquincollege.javaApp.fileManager.Command;
import java.text.StringCharacterIterator;

/**
 *
 * @author John Pilon A class to be serialized for Server to FSManager
 * communication Make Directory command
 */
public class MKDIR extends Command {

    private String path;

    public MKDIR() {
        path = null;
    }

    public MKDIR(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    /**
     * Validates the path
     *
     * @param path The path to validate
     * @return boolean: whether the path is valid
     */
    public boolean validatePath(String path) {
        boolean hasContent = (path != null) && (!path.equals(""));
        if (!hasContent) {
            return false;
        }
        path = path.replace('/', '\\');
        StringCharacterIterator it = new StringCharacterIterator(path);
        char c = it.current();
        while (c != StringCharacterIterator.DONE) {
            boolean isValidChar = (Character.isLetter(c) || Character.isSpaceChar(c) || c == '\\');
            if (!isValidChar) {
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
    public String toString() {
        return path + "\n" + password;
    }

    /**
     * Reconstructs the class from a serialized string
     *
     * @param str the serialized class
     * @return boolean: Whether the class reconstructed successfully
     */
    public boolean fromString(String str) {
        String[] serial = str.split("\n");
        if (serial.length == 2) {
            path = serial[0];
            password = serial[1];
        }
        return this.isValid() && validatePath(path);
    }

}
