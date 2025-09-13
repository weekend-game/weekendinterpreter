package game.weekend.interpreter;

class Token {

	public enum Type {
		DELIMITER, STRING, NUMBER, VARIABLE, COMMAND
	}

	public enum Code {
		UNDEFINED, EOF, EOL, REM, PRINT, PRINTLN, INPUT, GOTO, IF, FOR, NEXT, GOSUB, RETURN, END
	}

	protected Token(Code code, Type type, String value, int line, int fromPos, int toPos) {
		this.code = code;
		this.type = type;
		this.value = value;
		this.line = line;
		this.fromPos = fromPos;
		this.toPos = toPos;
	}

	public String toString() {
		return "Token " + type + " " + value;
	}

	protected final Code code;
	protected final Type type;
	protected final String value;
	protected final int line;
	protected final int fromPos;
	protected final int toPos;
}
