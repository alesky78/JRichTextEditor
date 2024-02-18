package it.spaghettisource.exp.editor;

import javax.swing.text.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StyleManager {

    private List<String> styleNames = new ArrayList<String>();
    private Style defaultStyle;
    
    public List<String> getStyleNames() {
        return Collections.unmodifiableList(styleNames);
    }

    public void registerStyles(StyledDocument document) {

        MutableAttributeSet attr;
        
        //normal style
        attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, false);
        StyleConstants.setItalic(attr, false);
        StyleConstants.setFontSize(attr,14); //TODO - dimension based on the certgen properties
        StyleConstants.setFontFamily(attr, "serif");
        defaultStyle = registerStyle(document, "normal", attr);
        
        //title style
        attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setItalic(attr, false);
        StyleConstants.setFontSize(attr,20); //TODO - dimension based on the certgen properties
        StyleConstants.setFontFamily(attr, "serif");        
        registerStyle(document, "title", attr);
        
    }

    public void applyDefaultStyle(StyledDocument document) {
    	
    	//paragraph attribute
        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_JUSTIFIED);   
    	document.setParagraphAttributes(0, document.getLength(), attr, false);
    	
    	//content attribute
		document.setLogicalStyle(0, defaultStyle);		
	}

    private Style registerStyle(StyledDocument document, String styleName, AttributeSet attributes) {
        Style style = document.addStyle(styleName, null);
        style.addAttributes(attributes);

        styleNames.add(styleName);
        return style;
        
    }


}
