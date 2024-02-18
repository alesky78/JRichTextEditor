package it.spaghettisource.exp.editor;

import io.github.geniot.jortho.FileUserDictionary;
import io.github.geniot.jortho.SpellChecker;

public class OrthoManager {

	private Editor editor;
	
	private static boolean initialized = false;
	
	public static void init() {
		if(!initialized) {
			initialized = true;
			SpellChecker.setUserDictionaryProvider(new FileUserDictionary());
			//SpellChecker.setUserDictionaryProvider(new FileUserDictionary("C:\\Users\\Alessandro\\Desktop\\dizionario"));  //TODO - aggiungi il dizionario in una cartella perstabilita in modo che utente no deve ricrearlo da 0 sempre 
			SpellChecker.registerDictionaries(null, null );			
		}
	}
	
	public OrthoManager(Editor editor) {
		super();
		this.editor = editor;	
	}

	public void registerEditor() {
		SpellChecker.register(editor.getTextPane());
	}
	
	public void unregisterEditor() {
		SpellChecker.unregister(editor.getTextPane());
	}
	
	public void showDialog() {
		SpellChecker.showSpellCheckerDialog(editor.getTextPane(), null);
	}
	
	
}
