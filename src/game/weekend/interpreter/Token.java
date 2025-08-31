package game.weekend.interpreter;

class Token {

	protected Token(int code, int type, String value, int line, int fromPos, int toPos) {
		this.code = code;
		this.type = type;
		this.value = value;
		this.line = line;
		this.fromPos = fromPos;
		this.toPos = toPos;
	}

	public String toString() {
		String s = "Token ";
		if (type == Token.DELIMITER) {
			s = s + "Разделитель: " + value;
		} else if (type == Token.STRING) {
			s = s + "Строка     : " + value;
		} else if (type == Token.NUMBER) {
			s = s + "Число      : " + value;
		} else if (type == Token.VARIABLE) {
			s = s + "Переменная : " + value;
		} else if (type == Token.COMMAND) {
			s = s + "Команда    : " + value;
		} else {
			s = s + "??? : " + value;
		}
		return s;
	}

	// Типы токенов
	protected static final int DELIMITER = 1;
	protected static final int STRING = 2;
	protected static final int NUMBER = 3;
	protected static final int VARIABLE = 4;
	protected static final int COMMAND = 5;

	// Коды токенов
	protected static final int EOF = 1;
	protected static final int EOL = 2;
	protected static final int REM = 101;
	protected static final int PRINT = 102;
	protected static final int PRINTLN = 103;

	protected final int code;
	protected final int type;
	protected final String value;
	protected final int line;
	protected final int fromPos;
	protected final int toPos;
}
