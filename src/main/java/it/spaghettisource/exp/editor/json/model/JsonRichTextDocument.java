package it.spaghettisource.exp.editor.json.model;

import java.util.ArrayList;
import java.util.List;

public class JsonRichTextDocument {

    protected List<JsonParagraph> paragraph;

	public List<JsonParagraph> getParagraph() {
	   if (paragraph == null) {
		   paragraph = new ArrayList<JsonParagraph>();
        }
        return this.paragraph;
	}

}
