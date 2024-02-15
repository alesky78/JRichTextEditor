package it.spaghettisource.exp.editor;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class EditorBarMenu extends JMenuBar{

    private final Editor editor;
    private final JFrame frame;
    private final EditorActionController editorActionController;


    public EditorBarMenu(JFrame frame, Editor editor) {
        super();
        this.frame = frame;
        this.editor = editor;
        this.editorActionController = new EditorActionController(frame, this, editor);

        initializeMenu();
    }

    private void initializeMenu() {
        JMenu menu;
        JMenuItem item;
        Icon icon;

        ///////////
        //File menu
        menu = new JMenu("File");
        add(menu);

        item = new JMenuItem("New file");
        item.setActionCommand(EditorActionController.NEW_DOCUMENT);
        item.addActionListener(editorActionController);
        menu.add(item);

        item = new JMenuItem("Open file");
        item.setActionCommand(EditorActionController.FILE_OPEN);
        item.addActionListener(editorActionController);
        menu.add(item);

        item = new JMenuItem("Save file");
        item.setActionCommand(EditorActionController.FILE_SAVE);
        item.addActionListener(editorActionController);
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Close");
        item.setActionCommand(EditorActionController.CLOSE);
        item.addActionListener(editorActionController);
        menu.add(item);

        ///////////
        //Edit menu
        menu = new JMenu("Edit");
        add(menu);

        item = new JMenuItem("Copy");
        icon = new ImageIcon(this.getClass().getResource("/icons/copy.png"));
        item.setIcon(icon);
        item.setActionCommand(EditorActionController.COPY);
        item.addActionListener(editorActionController);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK));
        menu.add(item);

        item = new JMenuItem("Cut");
        icon = new ImageIcon(this.getClass().getResource("/icons/cut.png"));
        item.setIcon(icon);
        item.setActionCommand(EditorActionController.CUT);
        item.addActionListener(editorActionController);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK));
        menu.add(item);

        item = new JMenuItem("Paste");
        icon = new ImageIcon(this.getClass().getResource("/icons/paste.png"));
        item.setIcon(icon);
        item.setActionCommand(EditorActionController.PASTE);
        item.addActionListener(editorActionController);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK));
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Undo");
        icon = new ImageIcon(this.getClass().getResource("/icons/undo.png"));
        item.setIcon(icon);
        item.setActionCommand(EditorActionController.UNDO);
        item.addActionListener(editorActionController);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
        menu.add(item);

        item = new JMenuItem("Redo");
        icon = new ImageIcon(this.getClass().getResource("/icons/redo.png"));
        item.setIcon(icon);
        item.setActionCommand(EditorActionController.REDO);
        item.addActionListener(editorActionController);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        menu.add(item);

    }
}


