package game.weekend.interpreter;

@SuppressWarnings("serial")
public class InterpreterException extends Exception {
	public InterpreterException(String mes, int line, int fromPos, int toPos) {
		super(mes);
		this.line = line;
		this.fromPos = fromPos;
		this.toPos = toPos;
	}

	public final int line;
	public final int fromPos;
	public final int toPos;
}
