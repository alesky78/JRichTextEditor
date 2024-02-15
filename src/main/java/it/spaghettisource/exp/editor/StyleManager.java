package it.spaghettisource.exp.editor;

import javax.swing.text.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StyleManager {

    private List<String> styleNames = new ArrayList<String>();

    public List<String> getStyleNames() {
        return Collections.unmodifiableList(styleNames);
    }

    public void registerStyles(StyledDocument document) {

        MutableAttributeSet attr;

        //deafult text
        attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, false);
        StyleConstants.setItalic(attr, false);
        StyleConstants.setFontSize(attr,12);
        registerStyle(document, "default", attr);

        //title style
        attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, true);
        StyleConstants.setItalic(attr, false);
        StyleConstants.setFontSize(attr,16);
        registerStyle(document, "title", attr);

    }

    private void registerStyle(StyledDocument document, String styleName, AttributeSet attributes) {
        Style style = document.addStyle(styleName, null);
        style.addAttributes(attributes);

        styleNames.add(styleName);
    }


}
