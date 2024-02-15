package it.spaghettisource.exp;


import it.spaghettisource.exp.editor.Editor;
import it.spaghettisource.exp.editor.EditorBarMenu;

import javax.swing.*;
import java.awt.*;

public class App {

    private JFrame frame;
    private Editor editor;
    private EditorBarMenu menuBar;

    public static void main(String[] args) {

        App app = new App();
        app.init();

    }


    public void init(){

            frame = new JFrame("HelloWorldSwing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            editor = new Editor(frame);
            frame.getContentPane().add(editor, BorderLayout.CENTER);

            menuBar = new EditorBarMenu(frame,editor);
            frame.setJMenuBar(menuBar);

            //frame.pack(); set manually size
            frame.setSize(800, 600);
            frame.setVisible(true);
    }


}
