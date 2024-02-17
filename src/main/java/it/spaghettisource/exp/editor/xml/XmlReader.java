package it.spaghettisource.exp.editor.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.spaghettisource.exp.editor.xml.model.Content;
import it.spaghettisource.exp.editor.xml.model.Part;
import it.spaghettisource.exp.editor.xml.model.XmlDocument;

public class XmlReader {

	static Logger log = LoggerFactory.getLogger(XmlReader.class);
	
	/** The object to which the parsed text is sent. */
	private StyledDocument target;

	/** This holds the current document attributes. */
	private MutableAttributeSet documentAttributes;

	static public void readDocument(StyledDocument document, InputStream from)throws IOException, BadLocationException{

		XmlDocument xmlDocument = null;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(XmlDocument.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();		
			xmlDocument = (XmlDocument) unmarshaller.unmarshal(from);			
		} catch (JAXBException ex) {
			throw new IOException("invalid xml", ex);
		}

		XmlReader builder = new XmlReader(document);
		builder.parseRoot(xmlDocument);
		
		
	} 


	public XmlReader(StyledDocument document) {
		this.target = document;
		this.documentAttributes = new SimpleAttributeSet();
	}


	private void parseRoot(XmlDocument xmlDocument) throws BadLocationException {
		
		List<Part> partList = xmlDocument.getPart();
		for (Part part : partList) {
			parsePart(part);
		}
		
		//TODO - log to remove
		((DefaultStyledDocument)target).dump(System.out);
		
	}

	//target.addStyle(styleName, basis);
    //target.insertString(target.getLength(),text,currentTextAttributes());
	//target.setParagraphAttributes(pgfEndPosition, 1, pgfAttributes, true);

	
	private void parsePart(Part part) throws BadLocationException {
		
		int startPoint =  target.getLength();
		
		List<Content> contents = part.getContent();
		for (Content content : contents) {
			parseContent(content);
		}

		int endPoint =  target.getLength();
		
		if(part.getAlignment()!=null) {
			//add paragraph attributes:
			MutableAttributeSet paragraphAttributes = new SimpleAttributeSet();
			StyleConstants.setAlignment(paragraphAttributes, new Integer(part.getAlignment()));
			target.setParagraphAttributes(startPoint, endPoint-startPoint, paragraphAttributes, true);
		}
    	
	}


	private void parseContent(Content content) throws BadLocationException {
		
		MutableAttributeSet contentAttributes = new SimpleAttributeSet();
		StyleConstants.setBold(contentAttributes, content.isBold());
		StyleConstants.setItalic(contentAttributes, content.isItalic());
		StyleConstants.setFontSize(contentAttributes,content.getSize());
		
		target.insertString(target.getLength(),content.getValue(),contentAttributes);
		
	}
}
