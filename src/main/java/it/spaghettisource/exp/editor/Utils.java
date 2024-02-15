package it.spaghettisource.exp.editor;
/**
  * class Utils to use in FindDialog
  * @author UlmDesign
 */
public class Utils {
	
	public static final char[] WORD_SEPARATORS = {' ', '\t', '\n','\r', '\f', '.', ',', ':', '-', '(', ')', '[', ']', '{','}', '<', '>', '/', '|', '\\', '\'', '\"'};
	
	  public static boolean isSeparator(char ch) {
	    for (int k=0; k<WORD_SEPARATORS.length; k++)
	      if (ch == WORD_SEPARATORS[k])
	        return true;
	    return false;
	  }
	  
}
