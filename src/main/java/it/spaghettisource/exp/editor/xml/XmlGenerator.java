package it.spaghettisource.exp.editor.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.spaghettisource.exp.editor.xml.model.Content;
import it.spaghettisource.exp.editor.xml.model.ObjectFactory;
import it.spaghettisource.exp.editor.xml.model.Part;
import it.spaghettisource.exp.editor.xml.model.PartStyle;
import it.spaghettisource.exp.editor.xml.model.XmlDocument;


/**
 * Generates an XML output stream (java.io.OutputStream) from rich text  
 */
public class XmlGenerator {

	static Logger log = LoggerFactory.getLogger(XmlGenerator.class);
	
	/* where all the text is going */
	private OutputStream outputStream;

	/* for efficiency's sake (ha) */
	private Segment workingSegment;


	static public void writeDocument(Document document, OutputStream to)throws IOException{
		XmlGenerator builder = new XmlGenerator(to);

		ObjectFactory factory = new ObjectFactory();
		XmlDocument xmlDocument = factory.createXmlDocument();
		
		Element root = document.getDefaultRootElement();
	    int max = root.getElementCount();

		//TODO - log to remove
		((DefaultStyledDocument)document).dump(System.out);
	    
		log.debug("Parse root");
	    log.debug("numer of elements:"+max);
	    
	    for(int idx = 0; idx < max; idx++) {
	    	builder.writeParagraphElement(xmlDocument, root.getElement(idx));
	    }

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(xmlDocument.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(xmlDocument, to);
			//TODO - log to remove
			marshaller.marshal(xmlDocument, System.out);
		} catch (JAXBException ex) {
			throw new IOException(ex);
		}

	}    


	public XmlGenerator(OutputStream to) {
		workingSegment = new Segment();
		outputStream = to;
	}

	private void writeParagraphElement(XmlDocument xmlDocument, Element element) throws IOException {
	    log.debug("check paragraph element:"+element);
		AttributeSet paragraphAttributes = element.getAttributes();

		Part part = new Part();
		List<Part> list = xmlDocument.getPart();
		list.add(part);
		
		updateParagraphAttribute(part, paragraphAttributes);
		
	    int sub_count = element.getElementCount();
		log.debug("Parse paragraph Elements");
	    log.debug("numer of elements:"+sub_count);
	    for(int idx = 0; idx < sub_count; idx ++) {
	        writeTextElement(part, element.getElement(idx));
	    }
	    
	}

	private void updateParagraphAttribute(Part part, AttributeSet attr){
		
		PartStyle style = new PartStyle();
		part.setPartStyle(style);
		
	    while(attr != null) {
	        if (attr instanceof Style) {
	        	style.setName(((Style) attr).getName());
	    		style.setBold(StyleConstants.isBold(attr));
	    		style.setItalic(StyleConstants.isItalic(attr));
	    		style.setSize(StyleConstants.getFontSize(attr));
	    		
	        }else {
	        	
	        	Object aligment = attr.getAttribute(StyleConstants.Alignment);
		        if(aligment!=null) {
		        	part.setAlignment(aligment.toString());
		        }
			    	        	
	        }

	        attr = attr.getResolveParent();
	    }
	}
	

	private void writeTextElement(Part part, Element element) throws IOException {
	    log.debug("check text element:"+element);
	    
		AttributeSet contentAttributes = element.getAttributes();
		
		Content content = new Content();
		List<Content> list = part.getContent();
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


	private void updateContentAttribute(Content content, AttributeSet attr) {
	    if(attr != null) {
	    	content.setBold(StyleConstants.isBold(attr));
	    	content.setItalic(StyleConstants.isItalic(attr));
	    	content.setSize(StyleConstants.getFontSize(attr));
	    }
	}
	
	public void writeText(Content content, Segment s) throws IOException{
		content.setValue(s.toString());    
	}	
	

}
