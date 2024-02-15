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
	
	public Editor f_owner;
	public JTabbedPane f_tb;
	public JTextField f_txtFind1;
	public JTextField f_txtFind2;
	public Document f_docFind;
	public Document f_docReplace;
	public ButtonModel f_modelWord;
	public ButtonModel f_modelCase;
	public ButtonModel f_modelUp;
	public ButtonModel f_modelDown;
	public int f_searchIndex = -1;
	public boolean f_searchUp = false;
	public String  f_searchData;
	
	 public FindDialog(Editor owner, int index) {
		    super();
		    f_owner = owner;
		    f_tb = new JTabbedPane();
		    // "Find" panel
		    JPanel p1 = new JPanel(new BorderLayout());
		    JPanel pc1 = new JPanel(new BorderLayout());
		    JPanel pf = new JPanel();
		    pf.setLayout(new DialogLayout(20, 5));
		    pf.setBorder(new EmptyBorder(8, 5, 8, 0));
		    pf.add(new JLabel("Find what:"));
		    f_txtFind1 = new JTextField();
		    f_docFind = f_txtFind1.getDocument();
		    pf.add(f_txtFind1);
		    pc1.add(pf, BorderLayout.CENTER);
		    JPanel po = new JPanel(new GridLayout(2, 2, 8, 2));
		    po.setBorder(new TitledBorder(new EtchedBorder(),
		      "Options"));
		    JCheckBox chkWord = new JCheckBox("Whole words only");
		    chkWord.setMnemonic('w');
		    f_modelWord = chkWord.getModel();
		    po.add(chkWord);
		    ButtonGroup bg = new ButtonGroup();
		    JRadioButton rdUp = new JRadioButton("Search up");
		    rdUp.setMnemonic('u');
		    f_modelUp = rdUp.getModel();
		    bg.add(rdUp);
		    po.add(rdUp);
		    JCheckBox chkCase = new JCheckBox("Match case");
		    chkCase.setMnemonic('c');
		    f_modelCase = chkCase.getModel();
		    po.add(chkCase);
		    JRadioButton rdDown = new JRadioButton("Search down", true);
		    rdDown.setMnemonic('d');
		    f_modelDown = rdDown.getModel();
		    bg.add(rdDown);
		    po.add(rdDown);
		    pc1.add(po, BorderLayout.SOUTH);
		    p1.add(pc1, BorderLayout.CENTER);
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
		    p1.add(p01, BorderLayout.EAST);
		    f_tb.addTab("Find", p1);
		    // "Replace" panel
		    JPanel p2 = new JPanel(new BorderLayout());
		    JPanel pc2 = new JPanel(new BorderLayout());
		    JPanel pc = new JPanel();
		    pc.setLayout(new DialogLayout(20, 5));
		    pc.setBorder(new EmptyBorder(8, 5, 8, 0));
		    pc.add(new JLabel("Find what:"));
		    f_txtFind2 = new JTextField();
		    f_txtFind2.setDocument(f_docFind);
		    pc.add(f_txtFind2);
		    pc.add(new JLabel("Replace:"));
		    JTextField txtReplace = new JTextField();
		    f_docReplace = txtReplace.getDocument();
		    pc.add(txtReplace);
		    pc2.add(pc, BorderLayout.CENTER);
		    po = new JPanel(new GridLayout(2, 2, 8, 2));
		    po.setBorder(new TitledBorder(new EtchedBorder(),
		      "Options"));
		    chkWord = new JCheckBox("Whole words only");
		    chkWord.setMnemonic('w');
		    chkWord.setModel(f_modelWord);
		    po.add(chkWord);
		    bg = new ButtonGroup();
		    rdUp = new JRadioButton("Search up");
		    rdUp.setMnemonic('u');
		    rdUp.setModel(f_modelUp);
		    bg.add(rdUp);
		    po.add(rdUp);
		    chkCase = new JCheckBox("Match case");
		    chkCase.setMnemonic('c');
		    chkCase.setModel(f_modelCase);
		    po.add(chkCase);
		    rdDown = new JRadioButton("Search down", true);
		    rdDown.setMnemonic('d');
		    rdDown.setModel(f_modelDown);
		    bg.add(rdDown);
		    po.add(rdDown);
		    pc2.add(po, BorderLayout.SOUTH);
		    p2.add(pc2, BorderLayout.CENTER);
		    JPanel p02 = new JPanel(new FlowLayout());
		    p = new JPanel(new GridLayout(3, 1, 2, 8));
		    JButton btReplace = new JButton("Replace");
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
		    p2.add(p02, BorderLayout.EAST);
		    // Make button columns the same size
		    p01.setPreferredSize(p02.getPreferredSize());
		    f_tb.addTab("Replace", p2);
		    f_tb.setSelectedIndex(index);
		    getContentPane().add(f_tb, BorderLayout.CENTER);
		    
		    WindowListener flst = new WindowAdapter() {
		      @Override
			public void windowActivated(WindowEvent e) {
		        f_searchIndex = -1;
		        if (f_tb.getSelectedIndex()==0)
		          f_txtFind1.grabFocus();
		        else
		          f_txtFind2.grabFocus();
		      }

		      @Override
			public void windowDeactivated(WindowEvent e) {
		        f_searchData = null;
		      }
		    };
		    addWindowListener(flst);
		    pack();
		    setResizable(false);
		  }

		  public void setSelectedIndex(int index) {
			/**
			 * method void setSelectedIndex
			 * @param int index
			 */
		    f_tb.setSelectedIndex(index);
		    setVisible(true);
		    f_searchIndex = -1;
		  }

		  public int findNext(boolean doReplace, boolean showWarnings) {
			/**
			 * method int findNext in class FindDialog find next text
			 * @param boolean doReplace
			 * @param boolean showWarnings 
			 */
		    JTextPane monitor = f_owner.getTextPane();
		    int pos = monitor.getCaretPosition();
		    if (f_modelUp.isSelected() != f_searchUp) {
		      f_searchUp = f_modelUp.isSelected();
		      f_searchIndex = -1;
		    }
		    if (f_searchIndex == -1) {
		      try {
		        Document doc = f_owner.getDocument();
		        if (f_searchUp)
		          f_searchData = doc.getText(0, pos);
		        else
		          f_searchData = doc.getText(pos, doc.getLength()-pos);
		        f_searchIndex = pos;
		      } catch (BadLocationException ex) {
		        warning(ex.toString());
		        return -1;
		      }
		    }
		    
		    String key = "";
		    try { key = f_docFind.getText(0, f_docFind.getLength()); }
			    catch (BadLocationException ex) {}
			    if (key.length()==0) {
			      warning("Please enter the target to search");
			      return -1;
			    }

		    if (!f_modelCase.isSelected()) {
		      f_searchData = f_searchData.toLowerCase();
		      key = key.toLowerCase();
		    }
		    if (f_modelWord.isSelected()) {
		      for (int k=0; k < Utils.WORD_SEPARATORS.length; k++) {
		        if (key.indexOf(Utils.WORD_SEPARATORS[k]) >= 0) {
		          warning("The text target contains an illegal "+
		            "character \'"+Utils.WORD_SEPARATORS[k]+"\'");
		          return -1;
		        }
		      }
		    }
		    String replacement = "";
		    if (doReplace) {
		      try {
		        replacement = f_docReplace.getText(0,
		          f_docReplace.getLength());
		      } catch (BadLocationException ex) {}

		    }
		    int xStart = -1;
		    int xFinish = -1;
		    while (true) {
		      if (f_searchUp)
		        xStart = f_searchData.lastIndexOf(key, pos-1);
		      else
		        xStart = f_searchData.indexOf(key, pos-f_searchIndex);
		      if (xStart < 0) {
		        if (showWarnings)
		          warning("Text not found");
		        return 0;
		      }
		      xFinish = xStart+key.length();
		      if (f_modelWord.isSelected()) {
		        boolean s1 = xStart>0;
		        boolean b1 = s1 && !Utils.isSeparator(f_searchData.charAt(
		          xStart-1));
		        boolean s2 = xFinish < f_searchData.length();
		        boolean b2 = s2 && !Utils.isSeparator(f_searchData.charAt(
		          xFinish));
		        if (b1 || b2)   {
		          if (f_searchUp && s1)   {
		            pos = xStart;
		            continue;
		          }

		          if (!f_searchUp && s2) {
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

		    if (!f_searchUp) {
		      xStart += f_searchIndex;
		      xFinish += f_searchIndex;
		    }

		    if (doReplace) {
		      f_owner.setSelection(xStart, xFinish, f_searchUp);
		      monitor.replaceSelection(replacement);
		      f_owner.setSelection(xStart, xStart+replacement.length(),
		        f_searchUp);
		      f_searchIndex = -1;
		    }
		    else
		      f_owner.setSelection(xStart, xFinish, f_searchUp);
		    return 1;
		  }

		  protected void warning(String message) {
			/**
			 * method void warning custom showMessageDialog in class FindDialog
			 * @param String message
			 */
		    JOptionPane.showMessageDialog(f_owner,
		      message, "Warning", JOptionPane.INFORMATION_MESSAGE);
		  }
		  
		  public class findAction implements ActionListener {
			/**
			 * class findAction ActionListener in class FindDialog
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				findNext(false, true);				
			}			  
		  }
		  
		  public class closeAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				/**
				 * class closeAction ActionListener in class FindDialog
				 */
				// TODO Auto-generated method stub
				setVisible(false);				
			}			  
		  }
		  
		  public class replaceAllAction implements ActionListener {
			/**
			 * class replaceAllAction ActionListener in class FindDialog: replace all text
			 */
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
		        JOptionPane.showMessageDialog(f_owner,
		          counter+" replacement(s) have been done", "Info",
		          JOptionPane.INFORMATION_MESSAGE);
		      	}			  
		  }

}
