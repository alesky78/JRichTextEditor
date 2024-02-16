package it.spaghettisource.exp.editor;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.undo.UndoManager;

public class Editor extends JPanel {

	protected JFrame frame;
	protected JTextPane textPane;
	protected StyleContext styleContext;
	protected DefaultStyledDocument document;
	protected RTFEditorKit editorKit;
	protected EditorBarTool toolBar;
	protected UndoManager undoManager;
	protected FindDialog findDialog;
	protected OrthoManager orthoManager;

	protected StyleManager styleManger;

	public Editor(JFrame parentFrame) {
		super();

		this.frame = parentFrame;

		setLayout(new BorderLayout());

		//create the editor, Make sure we install the editor kit before creating the initial document.
		textPane = new JTextPane();
		editorKit = new RTFEditorKit();
		textPane.setEditorKit(editorKit);

		//add the document
		styleContext = new StyleContext();
		document = new DefaultStyledDocument(styleContext);
		textPane = new JTextPane(document);

		//register the styles
		styleManger= new StyleManager();
		styleManger.registerStyles(document);

		//create the undo manager
		undoManager = new UndoManager();

		//add the listener
		textPane.addCaretListener(new CaretEventListener());
		document.addUndoableEditListener(new UndoableEditEventListener());

		//create the UI
		JScrollPane editorSCrScrollPane = new JScrollPane(textPane);
		toolBar = new EditorBarTool(frame,this);
		add(editorSCrScrollPane, BorderLayout.CENTER);
		add(toolBar, BorderLayout.NORTH);

		//Initialize other components
		findDialog = new FindDialog(this);
		
		OrthoManager.init();
		
		orthoManager = new OrthoManager(this);
		orthoManager.registerEditor();
	}

	public JTextPane getTextPane() {
		return textPane;
	}

	public DefaultStyledDocument getDocument() {
		return document;
	}

	public String[] findManagerStylesNames() {
		return styleManger.getStyleNames().toArray(new String[0]);
	}

	public void setSelection(int xStart, int xFinish, boolean moveUp) {
		if (moveUp) {
			textPane.setCaretPosition(xFinish);
			textPane.moveCaretPosition(xStart);
		} else
			textPane.select(xStart, xFinish);
	}	

	public void createNewDocument() {
		document = new DefaultStyledDocument(styleContext);
		styleManger.registerStyles(document);

		textPane.setDocument(document);
	}

	public void loadFromFile(File selectedFile) throws Exception{
		try(InputStream in = new FileInputStream(selectedFile)){
			document = new DefaultStyledDocument(styleContext);
			styleManger.registerStyles(document);

			editorKit.read(in, document, 0);
			textPane.setDocument(document);
		}
	}

	public void saveToFile(File selectedFile)  throws Exception{
		try(OutputStream out = new FileOutputStream(selectedFile)){
			editorKit.write(out, document, 0, document.getLength());
		}
	}

	public void setFontStyleBoldActualSelection(boolean enable) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setBold(attr, enable);
		setAttribute(attr, false,false);
		textPane.grabFocus();

	}

	public void setFontStyleItalicActualSelection(boolean enable) {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setItalic(attr, enable);
		setAttribute(attr, false,false);
		textPane.grabFocus();
	}

	public void setFontSizeActualSelection(String fontSize) {
		int size;
		try{
			size  = Integer.parseInt(fontSize);
		} catch (NumberFormatException e) {
			return;
		}

		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setFontSize(attr,size);
		setAttribute(attr, false,false);
		textPane.grabFocus();
	}

	public void setAlignmentJustify() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setAlignment(attr, StyleConstants.ALIGN_JUSTIFIED);
		setAttribute(attr, true,false);
		textPane.grabFocus();
	}

	public void setAlignmentLeft() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
		setAttribute(attr, true,false);
		textPane.grabFocus();
	}

	public void setAlignmentCenter() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
		setAttribute(attr, true,false);
		textPane.grabFocus();
	}

	public void setAlignmentRight() {
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
		setAttribute(attr, true,false);
		textPane.grabFocus();
	}

	public void setStyle(String styleName) {
		Style style = document.getStyle(styleName);
		setAttribute(style, false,false);
		textPane.grabFocus();
	}

	public void copy(){
		textPane.copy();
		textPane.grabFocus();
	}

	public void cut(){
		textPane.cut();
		textPane.grabFocus();
	}

	public void paste(){
		textPane.paste();
		textPane.grabFocus();
	}

	public void undo(){
		if (undoManager.canUndo()) {
			undoManager.undo();
		}
		textPane.grabFocus();
	}

	public void redo(){
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
		textPane.grabFocus();
	}
	
	public void findText() {
		findDialog.setSelectedIndex(0);
		findDialog.setLocationRelativeTo(this);
		findDialog.setVisible(true);
		
	}	
	
	public void replaceText() {
		findDialog.setSelectedIndex(1);
		findDialog.setLocationRelativeTo(this);
		findDialog.setVisible(true);
	}
	
	public void checkSpelling() {
		orthoManager.showDialog();
	}

	private void setAttribute(AttributeSet attr, boolean paragraphAttribute, boolean replaceAttribute) {

		int xStart = textPane.getSelectionStart();
		int xFinish = textPane.getSelectionEnd();

		if (paragraphAttribute){
			document.setParagraphAttributes(xStart, xFinish - xStart, attr, replaceAttribute);
		} else if (xStart != xFinish){
			document.setCharacterAttributes(xStart, xFinish - xStart, attr, replaceAttribute);
		}else {
			MutableAttributeSet inputAttributes = editorKit.getInputAttributes();
			inputAttributes.addAttributes(attr);
		}

	}

	public AttributeSet findAttributeSet(int offset) {
		StyledDocument doc = (StyledDocument) textPane.getDocument();
		Element element = doc.getCharacterElement(offset);
		return element.getAttributes();

	}


	private class CaretEventListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			toolBar.updateToolBar(e);
		}
	}

	private class UndoableEditEventListener implements UndoableEditListener {
		@Override
		public void undoableEditHappened(UndoableEditEvent e) {
			undoManager.addEdit(e.getEdit());
		}
	}



}


