
package it.spaghettisource.exp.editor.json.model;

import java.util.ArrayList;
import java.util.List;

public class JsonParagraph {

    protected String alignment;    
    protected List<JsonContent> content;

	public List<JsonContent> getContent() {
        if (content == null) {
            content = new ArrayList<JsonContent>();
        }
        return this.content;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String value) {
        this.alignment = value;
    }

}
