package game.weekend.texteditor;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game.weekend.interpreter.IOutput;
import game.weekend.interpreter.InterpreterException;

/**
 * Implementation of the IOutput interface to handle interpreter messages.
 */
public class Output implements IOutput {

	/**
	 * Create an implementation of the IOutput interface to handle interpreter
	 * messages.
	 * 
	 * @param editor text editor. Used to position the cursor to the location of the
	 *               detected error.
	 */
	public Output(Editor editor) {
		this.editor = editor;

		pane = new JTextArea();
		pane.setTabSize(4);
		pane.setEditable(false);

		spane = new JScrollPane(pane);
	}

	@Override
	public void clear() {
		pane.setText("");
	}

	@Override
	public void print(String s) {
		pane.append(s);
		pane.setCaretPosition(pane.getCaretPosition() + s.length());
	}

	@Override
	public void println(String s) {
		pane.append(s + "\n");
		pane.setCaretPosition(pane.getCaretPosition() + s.length() + 1);
	}

	/**
	 * Display a message about the detected error and indicate its location in the
	 * program text.
	 * 
	 * @param e an exception generated when an error is detected in the program
	 *          text.
	 * 
	 */
	public void error(InterpreterException e) {
		println(Loc.get("error") + ". " + Loc.get("line") + ": " + e.line + ". " + Loc.get("position") + ": "
				+ e.fromPos + ". " + e.getMessage());
		editor.setCurrentPos(e.line, e.fromPos, e.toPos);
	}

	/**
	 * Get the interpreter's results display component.
	 * 
	 * @return interpreter results display component.
	 */
	public JTextArea getJTextArea() {
		return pane;
	}

	/**
	 * Get JScrollPane.
	 * 
	 * @return JScrollPane.
	 */
	public JScrollPane getScrollPane() {
		return spane;
	}

	private Editor editor;
	private JTextArea pane;
	private JScrollPane spane;
}
