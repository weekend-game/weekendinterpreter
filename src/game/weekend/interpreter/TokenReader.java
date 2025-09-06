package game.weekend.interpreter;

import java.util.HashMap;

/**
 * Читатель лексем из Text.
 */
class TokenReader {

	/**
	 * Создать читатель лексем из Text.
	 */
	public TokenReader(Text t) {
		text = t;

		command = new HashMap<String, Integer>();
		command.put("PRINT", Token.PRINT);
		command.put("PRINTLN", Token.PRINTLN);
		command.put("INPUT", Token.INPUT);
		command.put("GOTO", Token.GOTO);
		command.put("IF", Token.IF);
		command.put("FOR", Token.FOR);
		command.put("NEXT", Token.NEXT);
		command.put("GOSUB", Token.GOSUB);
		command.put("RETURN", Token.RETURN);
		command.put("END", Token.END);
		command.put("REM", Token.REM);
	}

	public Token getToken() throws InterpreterException {

		// Если лексему возвращали, то вернуть возвращённую лексему
		if (bufferedToken != null) {
			Token t = bufferedToken;
			bufferedToken = null;
			return t;
		}

		int line = text.getLine();
		int pos = text.getPos();

		int c;
		c = text.read();

		// Конец файла
		if (c == -1) {
			Token t = new Token(Token.EOF, Token.DELIMITER, "EOF", line, pos, text.getPos());
			text.backChar(c);
			return t;
		}

		while (isWhite((char) c)) {
			c = text.read();
		}

		// Перевод строки (Вообще-то следует запросить признак конца строки)
		if (c == '\n') {
			Token t = new Token(Token.EOL, Token.DELIMITER, "EOL", line, pos, text.getPos());
			return t;
		}

		// Прочие разделители.
		if (isDelim((char) c)) {
			Token t = new Token(0, Token.DELIMITER, "" + (char) c, line, pos, text.getPos());
			return t;
		}

		// Строка
		if (c == '"') {
			StringBuffer sb = new StringBuffer();
			c = text.read();
			while (c != '"' && c != -1 && c != '\n') {
				sb.append((char) c);
				c = text.read();
			}

			if (c == -1 || c == '\n') {
				throw new InterpreterException("Нет закрывающих кавычек.", line, pos, pos + 1);
			}

			Token t = new Token(0, Token.STRING, sb.toString(), line, pos, text.getPos());
			return t;
		}

		// Число
		if (Character.isDigit((char) c)) {
			StringBuffer sb = new StringBuffer();
			while (Character.isDigit((char) c)) {
				sb.append((char) c);
				c = text.read();
			}
			text.backChar(c);
			Token t = new Token(0, Token.NUMBER, sb.toString(), line, pos, text.getPos());
			return t;
		}

		// Переменная или команда
		if (Character.isLetter((char) c)) {
			StringBuffer sb = new StringBuffer();
			do {
				sb.append((char) c);
				c = text.read();
			} while (!isDelim((char) c));
			text.backChar(c);

			String value = sb.toString();
			Integer cc = command.get(value);
			int code = 0;
			if (cc != null) {
				code = cc;
			}
			int type = (code == 0) ? Token.VARIABLE : Token.COMMAND;

			Token t = new Token(code, type, value, line, pos, text.getPos());
			return t;
		}

		throw new InterpreterException("Неопределённая лексема.", line, pos, text.getPos());
	}

	public void backToken(Token t) {
		bufferedToken = t;
	}

	public int getLine() {
		return text.getLine();
	}

	public void nextLine() {
		bufferedToken = null;
		text.nextLine();
	}

	public void setLine(int number) {
		bufferedToken = null;
		text.setLine(number);
	}

	public int getPos() {
		return text.getPos();
	}

	private boolean isDelim(char c) {
		for (char del : delimiter) {
			if (c == del)
				return true;
		}
		return false;
	}

	private boolean isWhite(char c) {
		return (c == ' ' || c == '\t');
	}

	private Text text;
	private Token bufferedToken;

	private final char[] delimiter = { ' ', ';', ',', '+', '-', '<', '>', '/', '*', '%', '^', '=', '(', ')', '\t', '\n',
			'\0' };

	private HashMap<String, Integer> command;
}
