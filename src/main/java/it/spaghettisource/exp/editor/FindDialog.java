package it.spaghettisource.exp.editor;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * class FindDialog extends JDialog to find text in textArea to use in JWordProcessor
 */

public class FindDialog extends JDialog {

	private Editor editor;
	private JTabbedPane tabPanel;
	private JTextField txtFind1;
	private JTextField txtFind2;
	private Document docFind;
	private Document docReplace;
	private ButtonModel modelWord;
	private ButtonModel modelCase;
	private ButtonModel modelUp;
	private ButtonModel modelDown;
	private int searchIndex = -1;
	private boolean searchUp = false;
	private String  searchData;

	public FindDialog(Editor owner, int index) {
		super();
		editor = owner;
		tabPanel = new JTabbedPane();

		// "Find" panel
		JPanel findPanel = new JPanel(new BorderLayout());
		JPanel pc1 = new JPanel(new BorderLayout());
		JPanel pf = new JPanel();
		pf.setLayout(new DialogLayout(20, 5));
		pf.setBorder(new EmptyBorder(8, 5, 8, 0));
		pf.add(new JLabel("Find what:"));
		txtFind1 = new JTextField();
		docFind = txtFind1.getDocument();
		pf.add(txtFind1);
		pc1.add(pf, BorderLayout.CENTER);
		JPanel po = new JPanel(new GridLayout(2, 2, 8, 2));
		po.setBorder(new TitledBorder(new EtchedBorder(),"Options"));
		JCheckBox chkWord = new JCheckBox("Whole words only");
		chkWord.setMnemonic('w');
		modelWord = chkWord.getModel();
		po.add(chkWord);
		ButtonGroup bg = new ButtonGroup();
		JRadioButton rdUp = new JRadioButton("Search up");
		rdUp.setMnemonic('u');
		modelUp = rdUp.getModel();
		bg.add(rdUp);
		po.add(rdUp);
		JCheckBox chkCase = new JCheckBox("Match case");
		chkCase.setMnemonic('c');
		modelCase = chkCase.getModel();
		po.add(chkCase);
		JRadioButton rdDown = new JRadioButton("Search down", true);
		rdDown.setMnemonic('d');
		modelDown = rdDown.getModel();
		bg.add(rdDown);
		po.add(rdDown);
		pc1.add(po, BorderLayout.SOUTH);
		findPanel.add(pc1, BorderLayout.CENTER);
		JPanel p01 = new JPanel(new FlowLayout());
		JPanel p = new JPanel(new GridLayout(2, 1, 2, 8));
		JButton btFind = new JButton("Find Next");

		btFind.addActionListener(new findAction());
		btFind.setMnemonic('f');
		p.add(btFind);
		JButton btClose = new JButton("Close");
		btClose.addActionListener(new closeAction());
		btClose.setDefaultCapable(true);
		p.add(btClose);
		p01.add(p);
		findPanel.add(p01, BorderLayout.EAST);
		tabPanel.addTab("Find", findPanel);

		// "Replace" panel
		JPanel replacePanel = new JPanel(new BorderLayout());
		JPanel pc2 = new JPanel(new BorderLayout());
		JPanel pc = new JPanel();
		pc.setLayout(new DialogLayout(20, 5));
		pc.setBorder(new EmptyBorder(8, 5, 8, 0));
		pc.add(new JLabel("Find what:"));
		txtFind2 = new JTextField();
		txtFind2.setDocument(docFind);
		pc.add(txtFind2);
		pc.add(new JLabel("Replace:"));
		JTextField txtReplace = new JTextField();
		docReplace = txtReplace.getDocument();
		pc.add(txtReplace);
		pc2.add(pc, BorderLayout.CENTER);
		po = new JPanel(new GridLayout(2, 2, 8, 2));
		po.setBorder(new TitledBorder(new EtchedBorder(),"Options"));
		chkWord = new JCheckBox("Whole words only");
		chkWord.setMnemonic('w');
		chkWord.setModel(modelWord);
		po.add(chkWord);
		bg = new ButtonGroup();
		rdUp = new JRadioButton("Search up");
		rdUp.setMnemonic('u');
		rdUp.setModel(modelUp);
		bg.add(rdUp);
		po.add(rdUp);
		chkCase = new JCheckBox("Match case");
		chkCase.setMnemonic('c');
		chkCase.setModel(modelCase);
		po.add(chkCase);
		rdDown = new JRadioButton("Search down", true);
		rdDown.setMnemonic('d');
		rdDown.setModel(modelDown);
		bg.add(rdDown);
		po.add(rdDown);
		pc2.add(po, BorderLayout.SOUTH);
		replacePanel.add(pc2, BorderLayout.CENTER);
		JPanel p02 = new JPanel(new FlowLayout());
		p = new JPanel(new GridLayout(3, 1, 2, 8));
		JButton btReplace = new JButton("Find/Replace");
		btReplace.addActionListener(new replaceAction());
		btReplace.setMnemonic('r');
		p.add(btReplace);
		JButton btReplaceAll = new JButton("Replace All");
		btReplaceAll.addActionListener(new replaceAllAction());
		btReplaceAll.setMnemonic('a');
		p.add(btReplaceAll);
		btClose = new JButton("Close");
		btClose.addActionListener(new closeAction());
		btClose.setDefaultCapable(true);
		p.add(btClose);
		p02.add(p);
		replacePanel.add(p02, BorderLayout.EAST);

		// Make button columns the same size
		p01.setPreferredSize(p02.getPreferredSize());
		tabPanel.addTab("Replace", replacePanel);

		tabPanel.setSelectedIndex(index);
		getContentPane().add(tabPanel, BorderLayout.CENTER);


		WindowListener flst = new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				searchIndex = -1;
				if (tabPanel.getSelectedIndex()==0)
					txtFind1.grabFocus();
				else
					txtFind2.grabFocus();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				searchData = null;
			}
		};
		addWindowListener(flst);
		pack();
		setResizable(false);

	}

	public void setSelectedIndex(int index) {
		tabPanel.setSelectedIndex(index);
		setVisible(true);
		searchIndex = -1;
	}


	public int findNext(boolean doReplace, boolean showWarnings) {

		JTextPane monitor = editor.getTextPane();
		int pos = monitor.getCaretPosition();
		if (modelUp.isSelected() != searchUp) {
			searchUp = modelUp.isSelected();
			searchIndex = -1;
		}
		if (searchIndex == -1) {
			try {
				Document doc = editor.getDocument();
				if (searchUp)
					searchData = doc.getText(0, pos);
				else
					searchData = doc.getText(pos, doc.getLength()-pos);
				searchIndex = pos;
			} catch (BadLocationException ex) {
				warning(ex.toString());
				return -1;
			}
		}

		String key = "";
		try { 
			key = docFind.getText(0, docFind.getLength()); 
		}catch (BadLocationException ex) {}
		if (key.length()==0) {
			warning("Please enter the target to search");
			return -1;
		}

		if (!modelCase.isSelected()) {
			searchData = searchData.toLowerCase();
			key = key.toLowerCase();
		}
		if (modelWord.isSelected()) {
			for (int k=0; k < Utils.WORD_SEPARATORS.length; k++) {
				if (key.indexOf(Utils.WORD_SEPARATORS[k]) >= 0) {
					warning("The text target contains an illegal "+"character \'"+Utils.WORD_SEPARATORS[k]+"\'");
					return -1;
				}
			}
		}
		
		String replacement = "";
		if (doReplace) {
			try {
				replacement = docReplace.getText(0,docReplace.getLength());
			} catch (BadLocationException ex) {}
		}
		
		int xStart = -1;
		int xFinish = -1;
		while (true) {
			if (searchUp)
				xStart = searchData.lastIndexOf(key, pos-1);
			else
				xStart = searchData.indexOf(key, pos-searchIndex);
			if (xStart < 0) {
				if (showWarnings)
					warning("Text not found");
				return 0;
			}
			xFinish = xStart+key.length();
			if (modelWord.isSelected()) {
				boolean s1 = xStart>0;
				boolean b1 = s1 && !Utils.isSeparator(searchData.charAt(xStart-1));
				boolean s2 = xFinish < searchData.length();
				boolean b2 = s2 && !Utils.isSeparator(searchData.charAt(xFinish));
				if (b1 || b2)   {
					if (searchUp && s1)   {
						pos = xStart;
						continue;
					}

					if (!searchUp && s2) {
						pos = xFinish;
						continue;
					}

					if (showWarnings)
						warning("Text not found");
					return 0;
				}
			}

			break;

		}

		if (!searchUp) {
			xStart += searchIndex;
			xFinish += searchIndex;
		}

		if (doReplace) {
			editor.setSelection(xStart, xFinish, searchUp);
			monitor.replaceSelection(replacement);
			editor.setSelection(xStart, xStart+replacement.length(),searchUp);
			searchIndex = -1;
		}
		else
			editor.setSelection(xStart, xFinish, searchUp);
		return 1;
	}

	protected void warning(String message) {
		JOptionPane.showMessageDialog(editor,message, "Warning", JOptionPane.INFORMATION_MESSAGE);
	}

	public class findAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			findNext(false, true);				
		}			  
	}

	public class closeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
		}	
	}

	public class replaceAllAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int counter = 0;
			while (true) {
				int result = findNext(true, false);
				if (result < 0)    // error
					return;
				else if (result == 0)    // no more
					break;
				counter++;
			}
			JOptionPane.showMessageDialog(editor,counter+" replacement(s) have been done", "Info",JOptionPane.INFORMATION_MESSAGE);
		}			  
	}

	public class replaceAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JTextPane monitor = editor.getTextPane();
			int xStart = monitor.getSelectionStart();
			int xFinish = monitor.getSelectionEnd();
			
			String key = "";
			try { 
				key = docFind.getText(0, docFind.getLength()); 
			}catch (BadLocationException ex) {}
			
			String replacement = "";
			try {
				replacement = docReplace.getText(0,docReplace.getLength());
			} catch (BadLocationException ex) {}
			
			String selected = "";
			try {
				selected = editor.getDocument().getText(xStart, xFinish-xStart);
			} catch (BadLocationException e1) {}
			
			if(selected.equals(key)) {
				monitor.replaceSelection(replacement);
				editor.setSelection(xStart, xStart+replacement.length(),searchUp);
				JOptionPane.showMessageDialog(editor,"replacement have been done", "Info",JOptionPane.INFORMATION_MESSAGE);
			}else {
				findNext(false, false);
			}
			
		}			  
	}

}
