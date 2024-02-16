package it.spaghettisource.exp.editor;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;


public class EditorBarTool extends JToolBar {

	private final Editor editor;
	private final JFrame frame;
	private final EditorActionController editorActionController;

	private JToggleButton styleBoldButton;
	private JToggleButton styleItalicButton;

	private JComboBox<String> fontSizeComboBox;

	private JComboBox<String> styleComboBox;

	public EditorBarTool(JFrame frame, Editor editor) {
		super();
		this.frame = frame;
		this.editor = editor;
		this.editorActionController = new EditorActionController(frame, this, editor);

		initializeToolBar();
	}


	private void initializeToolBar() {
		JButton button;
		Icon icon;
		InputMap inputMap;
		ActionMap actionMap;

		icon = new ImageIcon(this.getClass().getResource("/icons/action_new_document.png"));
		button = new JButton(icon);
		button.setToolTipText("New document");
		button.setActionCommand(EditorActionController.NEW_DOCUMENT);
		button.addActionListener(editorActionController);		
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/action_open.png"));
		button = new JButton(icon);
		button.setToolTipText("Open document");
		button.setActionCommand(EditorActionController.FILE_OPEN);
		button.addActionListener(editorActionController);                   
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/action_save.png"));
		button = new JButton(icon);
		button.setToolTipText("Save document");
		button.setActionCommand(EditorActionController.FILE_SAVE);
		button.addActionListener(editorActionController);
		add(button);

		addSeparator();

		fontSizeComboBox = new JComboBox(new String[]{"8", "10", "12", "14", "16", "18", "24"});
		fontSizeComboBox.setMaximumSize(fontSizeComboBox.getPreferredSize());
		fontSizeComboBox.setActionCommand(EditorActionController.STYLE_FONT_SIZE);
		fontSizeComboBox.addActionListener(editorActionController);
		add(fontSizeComboBox);

		addSeparator();

		styleComboBox = new JComboBox(editor.findManagerStylesNames());
		styleComboBox.setMaximumSize(styleComboBox.getPreferredSize());
		styleComboBox.setActionCommand(EditorActionController.STYLE_APPLY);
		styleComboBox.addActionListener(editorActionController);
		add(styleComboBox);

		addSeparator();

		icon = new ImageIcon(this.getClass().getResource("/icons/style_bold.png"));
		styleBoldButton = new JToggleButton(icon);
		styleBoldButton.setToolTipText("Bold");
		styleBoldButton.setActionCommand(EditorActionController.STYLE_FONT_BOLD);
		styleBoldButton.addActionListener(editorActionController);
		add(styleBoldButton);

		icon = new ImageIcon(this.getClass().getResource("/icons/style_italic.png"));
		styleItalicButton = new JToggleButton(icon);
		styleItalicButton.setToolTipText("Italic");
		styleItalicButton.setActionCommand(EditorActionController.STYLE_FONT_ITALIC);
		styleItalicButton.addActionListener(editorActionController);
		add(styleItalicButton);

		addSeparator();

		icon = new ImageIcon(this.getClass().getResource("/icons/copy.png"));
		button = new JButton(icon);
		button.setToolTipText("Copy");
		button.setActionCommand(EditorActionController.COPY);
		button.addActionListener(editorActionController);
		inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = button.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK), EditorActionController.COPY);
		actionMap.put(EditorActionController.COPY, editorActionController);			
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/cut.png"));
		button = new JButton(icon);
		button.setToolTipText("Cut");
		button.setActionCommand(EditorActionController.CUT);
		button.addActionListener(editorActionController);
		inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = button.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK), EditorActionController.CUT);
		actionMap.put(EditorActionController.CUT, editorActionController);			
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/paste.png"));
		button = new JButton(icon);
		button.setToolTipText("Paste");
		button.setActionCommand(EditorActionController.PASTE);
		button.addActionListener(editorActionController);
		inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = button.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK), EditorActionController.PASTE);
		actionMap.put(EditorActionController.PASTE, editorActionController);			
		add(button);

		addSeparator();

		icon = new ImageIcon(this.getClass().getResource("/icons/undo.png"));
		button = new JButton(icon);
		button.setToolTipText("Undo");
		button.setActionCommand(EditorActionController.UNDO);
		button.addActionListener(editorActionController);
		inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = button.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK), EditorActionController.UNDO);
		actionMap.put(EditorActionController.UNDO, editorActionController);			
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/redo.png"));
		button = new JButton(icon);
		button.setToolTipText("Redo");
		button.setActionCommand(EditorActionController.REDO);
		button.addActionListener(editorActionController);
		inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = button.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK), EditorActionController.REDO);
		actionMap.put(EditorActionController.REDO, editorActionController);			
		add(button);

		addSeparator();

		icon = new ImageIcon(this.getClass().getResource("/icons/align-justify.png"));
		button = new JButton(icon);
		button.setToolTipText("Justify");
		button.setActionCommand(EditorActionController.ALIGN_JUSTIFY);
		button.addActionListener(editorActionController);
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/align-left.png"));
		button = new JButton(icon);
		button.setToolTipText("Left");
		button.setActionCommand(EditorActionController.ALIGN_LEFT);
		button.addActionListener(editorActionController);
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/align-center.png"));
		button = new JButton(icon);
		button.setToolTipText("Center");
		button.setActionCommand(EditorActionController.ALIGN_CENTER);
		button.addActionListener(editorActionController);
		add(button);

		icon = new ImageIcon(this.getClass().getResource("/icons/align-right.png"));
		button = new JButton(icon);
		button.setToolTipText("Right");
		button.setActionCommand(EditorActionController.ALIGN_RIGHT);
		button.addActionListener(editorActionController);
		add(button);

		addSeparator();

		icon = new ImageIcon(this.getClass().getResource("/icons/find-replace.png"));
		button = new JButton(icon);
		button.setToolTipText("Trova");
		button.setActionCommand(EditorActionController.FIND);
		button.addActionListener(editorActionController);
		inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap = button.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.CTRL_MASK), EditorActionController.FIND);
		actionMap.put(EditorActionController.FIND, editorActionController);			
		add(button);		
			
	}

	public void updateToolBar(CaretEvent e) {
		int offset = e.getDot();
		AttributeSet set = editor.findAttributeSet(offset);
		editorActionController.disableActions();

		styleBoldButton.setSelected(StyleConstants.isBold(set));
		styleItalicButton.setSelected(StyleConstants.isItalic(set));
		fontSizeComboBox.setSelectedItem(String.valueOf(StyleConstants.getFontSize(set)));

		editorActionController.enableActions();
	}


}
