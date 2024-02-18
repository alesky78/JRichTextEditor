package it.spaghettisource.exp.editor.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.spaghettisource.exp.editor.json.model.JsonContent;
import it.spaghettisource.exp.editor.json.model.JsonParagraph;
import it.spaghettisource.exp.editor.json.model.JsonRichTextDocument;
import it.spaghettisource.exp.utils.JsonConverter;

public class JsonReader {
	
	private static Logger log = LoggerFactory.getLogger(JsonReader.class);
	
	/** The object to which the parsed text is sent. */
	private StyledDocument target;

	/** This holds the current document attributes. */
	private MutableAttributeSet documentAttributes;

	static public void readDocument(StyledDocument document, InputStream from)throws IOException, BadLocationException{
		
	    log.debug("JsonReader read json and convert in document");
		
		StringBuilder stringBuilder = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(from, StandardCharsets.UTF_8)) {
            char[] buffer = new char[1024];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, bytesRead);
            }
        }
        
        JsonRichTextDocument jsonDocument;
		try {
			jsonDocument = JsonConverter.jsonToObject(stringBuilder.toString(), JsonRichTextDocument.class);
		} catch (Exception e) {
			throw new IOException(e);
		}
        
		JsonReader builder = new JsonReader(document);
		builder.parseRoot(jsonDocument);
		
	    if(log.isDebugEnabled()) {
		    log.debug("Document generated");
			((DefaultStyledDocument) document).dump(System.out);	    	
	    }

		
	} 


	public JsonReader(StyledDocument document) {
		this.target = document;
		this.documentAttributes = new SimpleAttributeSet();
	}


	private void parseRoot(JsonRichTextDocument jsonDocument) throws BadLocationException {
		
		List<JsonParagraph> paragraphs = jsonDocument.getParagraph();
		for (JsonParagraph paragraph : paragraphs) {
			parseParagraph(paragraph);
		}
				
	}
	
	private void parseParagraph(JsonParagraph part) throws BadLocationException {
		
		int startPoint =  target.getLength();
		
		List<JsonContent> contents = part.getContent();
		for (JsonContent content : contents) {
			parseContent(content);
		}

		int endPoint =  target.getLength();

		
		//add paragraph attributes:
		//JsonParagraphStyle style = part.getStyle();
		
		MutableAttributeSet paragraphAttributes = new SimpleAttributeSet();

		//paragraphAttributes.setName( style.getName());
//		StyleConstants.setBold(paragraphAttributes, style.isBold());
//		StyleConstants.setItalic(paragraphAttributes, style.isItalic());
//		StyleConstants.setFontSize(paragraphAttributes, style.getSize());

		if(part.getAlignment()!=null) {
			StyleConstants.setAlignment(paragraphAttributes, new Integer(part.getAlignment()));
		}
		
		target.setParagraphAttributes(startPoint, endPoint-startPoint, paragraphAttributes, false);
    	
	}



	private void parseContent(JsonContent content) throws BadLocationException {
		
		MutableAttributeSet contentAttributes = new SimpleAttributeSet();
		StyleConstants.setBold(contentAttributes, content.isBold());
		StyleConstants.setItalic(contentAttributes, content.isItalic());
		StyleConstants.setFontSize(contentAttributes,content.getSize());
		
		target.insertString(target.getLength(),content.getValue(),contentAttributes);
		
	}
}
