package it.spaghettisource.exp.editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the controller of the application, it manage the action of the user
 * and delegate the operation to the model
 */
public class EditorActionController implements ActionListener {


    public static final String COPY = "COPY";
    public static final String CUT = "CUT";
    public static final String PASTE = "PASTE";
    public static final String UNDO = "UNDO";
    public static final String REDO = "REDO";
    public static final String ALIGN_JUSTIFY = "ALIGN_JUSTIFY";
    public static final String ALIGN_LEFT = "ALIGN_LEFT";
    public static final String ALIGN_CENTER = "ALIGN_CENTER";
    public static final String ALIGN_RIGHT = "ALIGN_RIGHT";
    public static String NEW_DOCUMENT = "NEW_DOCUMENT";
    public static final String FILE_OPEN = "FILE_OPEN";
    public static final String FILE_SAVE = "FILE_SAVE";
    public static final String CLOSE = "CLOSE";
    public static final String STYLE_APPLY = "STYLE_APPLY";
    public static final String STYLE_FONT_BOLD = "STYLE_FONT_BOLD";
    public static final String STYLE_FONT_ITALIC = "STYLE_FONT_ITALIC";
    public static final String STYLE_FONT_SIZE = "STYLE_FONT_SIZE";


    private final Component parent;

    private final Editor editor;
    private final JFrame frame;
    private JFileChooser rtfFileChooser;

    private boolean enableActions = true;

    public EditorActionController(JFrame frame, Component parent, Editor editor) {
        super();
        this.frame = frame;
        this.parent = parent;
        this.editor = editor;

        initializeFileChooser();
    }

    public void enableActions() {
        this.enableActions = true;
    }

    public void disableActions() {
        this.enableActions = false;
    }

    protected void initializeFileChooser() {
        rtfFileChooser = new JFileChooser();
        rtfFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        rtfFileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(java.io.File f) {
                return f.getName().toLowerCase().endsWith(".rtf") || f.isDirectory();
            }

            public String getDescription() {
                return "RTF files";
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if(!enableActions){
            return;
        }

        String command = event.getActionCommand();

        try{

            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if(command.equals(NEW_DOCUMENT)) {
                editor.createNewDocument();

            }else if (command.equals(FILE_OPEN)) {
                int returnVal = rtfFileChooser.showOpenDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    editor.loadFromFile(rtfFileChooser.getSelectedFile());
                }

            }else if(command.equals(FILE_SAVE)){
                int returnVal = rtfFileChooser.showSaveDialog(parent);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    editor.saveToFile(rtfFileChooser.getSelectedFile());
                }

            }else if(command.equals(CLOSE)){
                System.exit(0);

            }else if(command.equals(STYLE_FONT_BOLD)){
                JToggleButton source = (JToggleButton) event.getSource();
                editor.setFontStyleBoldActualSelection(source.isSelected());

            }else if(command.equals(STYLE_FONT_ITALIC)) {
                JToggleButton source = (JToggleButton) event.getSource();
                editor.setFontStyleItalicActualSelection(source.isSelected());

            }else if(command.equals(STYLE_FONT_SIZE)){
                JComboBox<String> source = (JComboBox) event.getSource();
                editor.setFontSizeActualSelection((String) source.getSelectedItem());

            }else if(command.equals(COPY)){
                editor.copy();

            }else if(command.equals(CUT)) {
                editor.cut();

            }else if(command.equals(PASTE)){
                editor.paste();

            }else if(command.equals(UNDO)){
                editor.undo();

            }else if(command.equals(REDO)){
                editor.redo();

            }else if(command.equals(STYLE_APPLY)){
                JComboBox<String> source = (JComboBox) event.getSource();
                editor.setStyle((String) source.getSelectedItem());

            }else if(command.equals(ALIGN_JUSTIFY)) {
                editor.setAlignmentJustify();
            }else if(command.equals(ALIGN_LEFT)) {
                editor.setAlignmentLeft();
            }else if(command.equals(ALIGN_CENTER)) {
                editor.setAlignmentCenter();
            }else if(command.equals(ALIGN_RIGHT)) {
                editor.setAlignmentRight();
            }


        }catch (Exception ex){
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }finally {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }

}
