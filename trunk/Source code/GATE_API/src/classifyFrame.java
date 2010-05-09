import gate.util.Out;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.util.List;

public class classifyFrame extends java.awt.Frame {

	private static final String STRING_DOCS = "What state has the most Indians";
	private static final String GATE_API_DEMO = "GATE API demo";
	private static final long serialVersionUID = 1L;
	// Variables declaration
	private java.awt.Button okButton;
	private java.awt.List kindOfTokenList;
	private java.awt.TextField questionText;
	private java.awt.TextField patternText;
	// End of variables declaration
	private static final String OK_BUTTON = "okButton";
	private static final String RUN = "Run";

	public classifyFrame() {
		super(GATE_API_DEMO);
		initComponents();
	}

	private void initComponents() {

		Classifier.loadPattern();
		kindOfTokenList = new java.awt.List();
		questionText = new java.awt.TextField();
		patternText  = new TextField();
		okButton = new java.awt.Button();

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});
		setLayout(null);
		add(kindOfTokenList);
		kindOfTokenList.setBounds(19, 80, 450, 240);

		add(questionText);
		questionText.setBounds(19, 40, 370, 30);
		questionText.setText(STRING_DOCS);
		
		add(patternText);
		patternText.setBounds(19, 325, 370, 30);

		okButton.setLabel(RUN);
		add(okButton);
		okButton.setBounds(410, 40, 57, 24);
		okButton.getAccessibleContext().setAccessibleName(OK_BUTTON);
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				button1ActionPerformed(evt);
			}
		});
		pack();
	}

	@SuppressWarnings("unchecked")
	protected void button1ActionPerformed(ActionEvent evt) {
		List list = null;
		if (questionText.getText() != null) {
			list = GateUtil.getToken(questionText.getText());
		}
		kindOfTokenList.removeAll();
		patternText.setText("");
		String temp = "";
		for (int i = list.size() - 1; i >= 0; i--) {
			Token token = (Token) list.get(i);
			kindOfTokenList.add(token.toString());
			temp += token.getKind() + " ";
		}				
		kindOfTokenList.add(temp);
		patternText.setText(Classifier.checkPatternQuestion(questionText.getText()));
	}

	private void exitForm(java.awt.event.WindowEvent evt) {
		System.exit(0);
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				GateUtil.RunGate();
				classifyFrame frame = new classifyFrame();
				frame.setSize(600, 400);
				frame.setVisible(true);
			}
		});
	}
}
