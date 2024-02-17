package it.spaghettisource.exp.editor.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

/**
 * This is the default implementation of XML editing functionality.  
 * 
 *
 * @author  Alessandro D'Ottavio
 */
public class XmlEditorKit extends StyledEditorKit {

	
	public XmlEditorKit() {
		super();
	}

    /**
     * Get the MIME type of the data that this kit represents support for.  This kit supports
     * the type <code>text/xml</code>.
     *
     * @return the type
     */
    public String getContentType() {
        return "text/xml";
    }
    
    
    /**
     * Write content from a document to the given stream
     * in a format appropriate for this kind of content handler.
     *
     * @param out  The stream to write to
     * @param doc The source for the write.
     * @param pos The location in the document to fetch the
     *   content.
     * @param len The amount to write out.
     * @exception IOException on any I/O error
     * @exception BadLocationException if pos represents an invalid
     *   location within the document.
     */
    public void write(OutputStream out, Document doc, int pos, int len) throws IOException, BadLocationException {
    	XmlGenerator.writeDocument(doc, out);
    }
    
    /**
     * Insert content from the given stream which is expected
     * to be in a format appropriate for this kind of content
     * handler.
     *
     * @param in  The stream to read from
     * @param doc The destination for the insertion.
     * @param pos The location in the document to place the
     *   content.
     * @exception IOException on any I/O error
     * @exception BadLocationException if pos represents an invalid
     *   location within the document.
     */
    public void read(InputStream in, Document doc, int pos) throws IOException, BadLocationException {

    	if (doc instanceof StyledDocument) {
        	XmlReader.readDocument((StyledDocument) doc, in);    		
    	}else {
    		throw new IOException("StyledDocument type is expected");
    	}

    }
    
	
}
