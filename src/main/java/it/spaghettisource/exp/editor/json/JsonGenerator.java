package it.spaghettisource.exp.editor.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.spaghettisource.exp.editor.json.model.JsonContent;
import it.spaghettisource.exp.editor.json.model.JsonParagraph;
import it.spaghettisource.exp.editor.json.model.JsonRichTextDocument;
import it.spaghettisource.exp.utils.JsonConverter;


/**
 * Generates an XML output stream (java.io.OutputStream) from rich text  
 */
public class JsonGenerator {
	
	private static Logger log = LoggerFactory.getLogger(JsonGenerator.class);
	
	/* where all the text is going */
	private OutputStream outputStream;

	/* for efficiency's sake (ha) */
	private Segment workingSegment;


	static public void writeDocument(Document document, OutputStream to)throws IOException{
		
	    log.debug("JsonGenerator read document and convert in json");
		
		JsonGenerator builder = new JsonGenerator(to);

		JsonRichTextDocument jsonDocument = new JsonRichTextDocument();
		
		Element root = document.getDefaultRootElement();
	    int max = root.getElementCount();

	    if(log.isDebugEnabled()) {
			((DefaultStyledDocument)document).dump(System.out);	    	
	    }    
	    
	    for(int idx = 0; idx < max; idx++) {
	    	builder.writeParagraphElement(jsonDocument, root.getElement(idx));
	    }

		try {
			String json = JsonConverter.objectToJson(jsonDocument);
	       try (OutputStreamWriter writer = new OutputStreamWriter(to, StandardCharsets.UTF_8)) {
	            writer.write(json);
	        }
	       
		    if(log.isDebugEnabled()) {
		    	log.debug(json);
		    }

		} catch (Exception ex) {
			throw new IOException(ex);
		}

	}    


	public JsonGenerator(OutputStream to) {
		workingSegment = new Segment();
		outputStream = to;
	}

	private void writeParagraphElement(JsonRichTextDocument rootDocument, Element element) throws IOException {
	    
		AttributeSet paragraphAttributes = element.getAttributes();

		JsonParagraph paragraph = new JsonParagraph();
		List<JsonParagraph> list = rootDocument.getParagraph();
		list.add(paragraph);
		
		updateParagraphAttribute(paragraph, paragraphAttributes);
		
	    int sub_count = element.getElementCount();
	    for(int idx = 0; idx < sub_count; idx ++) {
	        writeTextElement(paragraph, element.getElement(idx));
	    }
	    
	}

	private void updateParagraphAttribute(JsonParagraph paragraph , AttributeSet attr){
		
		//JsonParagraphStyle style = new JsonParagraphStyle();
		//paragraph.setStyle(style);
		
	    while(attr != null) {
	        if (attr instanceof Style) {
	        	//FIX: originally there i was misslead but this element in the document: 
	        	//resolver=NamedStyle:normal {name=normal,bold=false,italic=false,size=14,family=serif,}
	        	//this are not attrbute to parse but is the default resolver, i mean is the default stile used by the document
	        	
//	        	style.setName(((Style) attr).getName());
//	    		style.setBold(StyleConstants.isBold(attr));
//	    		style.setItalic(StyleConstants.isItalic(attr));
//	    		style.setSize(StyleConstants.getFontSize(attr));
	    		
	        }else {
	        	
	        	Object aligment = attr.getAttribute(StyleConstants.Alignment);
		        if(aligment!=null) {
		        	paragraph.setAlignment(aligment.toString());
		        }
			    	        	
	        }

	        attr = attr.getResolveParent();
	    }
	}
	

	private void writeTextElement(JsonParagraph paragraph, Element element) throws IOException {
	    
		AttributeSet contentAttributes = element.getAttributes();
		
		JsonContent content = new JsonContent();
		List<JsonContent> list = paragraph.getContent();
		list.add(content);
		
		updateContentAttribute(content, contentAttributes);
		
	    if (element.isLeaf()) {
	        try {
	        	element.getDocument().getText(element.getStartOffset(),element.getEndOffset() - element.getStartOffset(),workingSegment);
	        } catch (BadLocationException ble) {
	            ble.printStackTrace();
	            throw new InternalError(ble.getMessage());
	        }
	        
			writeText(content, workingSegment);
			
	    } else {
            throw new InternalError("Unexpected annidation");
	    }
		
	}


	private void updateContentAttribute(JsonContent content, AttributeSet attr) {
	    if(attr != null) {
	    	content.setBold(StyleConstants.isBold(attr));
	    	content.setItalic(StyleConstants.isItalic(attr));
	    	content.setSize(StyleConstants.getFontSize(attr));
	    }
	}
	
	public void writeText(JsonContent content, Segment s) throws IOException{
		content.setValue(s.toString());    
	}	
	

}
