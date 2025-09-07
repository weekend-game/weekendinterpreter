package game.weekend.texteditor;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game.weekend.interpreter.IOutput;
import game.weekend.interpreter.InterpreterException;

/**
 * Реализация интерфейса IOutput для обработки сообщений интерпретатора.
 */
public class Output implements IOutput {

	/**
	 * Создать реализацию интерфейса IOutput для обработки сообщений интерпретатора.
	 * 
	 * @param editor редактор текста. Используется для позиционирования курсора на
	 *               место обнаруженной ошибки.
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
	 * Отобразить сообщение об обнаруженной ошибке и указать её место в тексте
	 * программы.
	 * 
	 * @param e исключение сгенерированное при обнаружении ошибки в тексте
	 *          программы.
	 */
	public void error(InterpreterException e) {
		println(Loc.get("error") + ". " + Loc.get("line") + ": " + e.line + ". " + Loc.get("position") + ": "
				+ e.fromPos + ". " + e.getMessage());
		editor.setCurrentPos(e.line, e.fromPos, e.toPos);
	}

	/**
	 * Получть компонент отображения результатов интерпретатора.
	 * 
	 * @return компонент отображения результатов интерпретатора.
	 */
	public JTextArea getJTextArea() {
		return pane;
	}

	/**
	 * Получть JScrollPane.
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
