package gui;

import java.io.FileNotFoundException;
import java.io.FileReader;


public class TextF {
	String filePath;
	String textContext;
	
	
	boolean readLogFile(String filePath) {
		try {
			this.textContext="";
			FileReader fr = new FileReader(filePath);
			char[] ch = new char[1024];
			while (fr.read(ch, 0, 1024) != -1) {
				textContext += new String(ch);
				ch = new char[1024];
			}
			textContext += new String(ch);
			return true;
		} catch (FileNotFoundException e) {
	        System.out.println("Can't find the specified file");
	        return false;
	      } catch (Exception e) {
	        System.out.println("Read error");
	        return false;
	      }
	}
	
	String getLog(String filePath){
		if(readLogFile(filePath)){
			return this.textContext;
		}
		return null;
	}
	
}
