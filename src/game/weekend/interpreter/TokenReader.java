package game.weekend.interpreter;

import java.util.HashMap;

import game.weekend.texteditor.Loc;

/**
 * Reader of lexemes from Text.
 */
class TokenReader {

	/**
	 * Create a lexeme reader from Text.
	 */
	public TokenReader(Text t) {
		text = t;

		command = new HashMap<String, Token.Code>();
		command.put("PRINT", Token.Code.PRINT);
		command.put("PRINTLN", Token.Code.PRINTLN);
		command.put("INPUT", Token.Code.INPUT);
		command.put("GOTO", Token.Code.GOTO);
		command.put("IF", Token.Code.IF);
		command.put("FOR", Token.Code.FOR);
		command.put("NEXT", Token.Code.NEXT);
		command.put("GOSUB", Token.Code.GOSUB);
		command.put("RETURN", Token.Code.RETURN);
		command.put("END", Token.Code.END);
		command.put("REM", Token.Code.REM);
	}

	public Token getToken() throws InterpreterException {

		// If the lexeme was returned, then return the returned lexeme
		if (bufferedToken != null) {
			Token t = bufferedToken;
			bufferedToken = null;
			return t;
		}

		int line = text.getLine();
		int pos = text.getPos();

		int c;
		c = text.read();

		// End of file
		if (c == -1) {
			Token t = new Token(Token.Code.EOF, Token.Type.DELIMITER, "EOF", line, pos, text.getPos());
			text.backChar(c);
			return t;
		}

		while (isWhite((char) c)) {
			c = text.read();
		}

		// Line feed
		if (c == '\n') {
			Token t = new Token(Token.Code.EOL, Token.Type.DELIMITER, "EOL", line, pos, text.getPos());
			return t;
		}

		// Other delimiters
		if (isDelim((char) c)) {
			Token t = new Token(Token.Code.UNDEFINED, Token.Type.DELIMITER, "" + (char) c, line, pos, text.getPos());
			return t;
		}

		// String
		if (c == '"') {
			StringBuffer sb = new StringBuffer();
			c = text.read();
			while (c != '"' && c != -1 && c != '\n') {
				sb.append((char) c);
				c = text.read();
			}

			if (c == -1 || c == '\n') {
				throw new InterpreterException(Loc.get("no_closing_quotes") + ".", line, pos, pos + 1);
			}

			Token t = new Token(Token.Code.UNDEFINED, Token.Type.STRING, sb.toString(), line, pos, text.getPos());
			return t;
		}

		// Number
		if (Character.isDigit((char) c)) {
			StringBuffer sb = new StringBuffer();
			while (Character.isDigit((char) c)) {
				sb.append((char) c);
				c = text.read();
			}
			text.backChar(c);
			Token t = new Token(Token.Code.UNDEFINED, Token.Type.NUMBER, sb.toString(), line, pos, text.getPos());
			return t;
		}

		// Variable or command
		if (Character.isLetter((char) c)) {
			StringBuffer sb = new StringBuffer();
			do {
				sb.append((char) c);
				c = text.read();
			} while (!isDelim((char) c));
			text.backChar(c);

			String value = sb.toString();
			Token.Code tc = command.get(value);
			Token.Code code = null;
			if (tc != null) {
				code = tc;
			}

			Token.Type type = (code == null) ? Token.Type.VARIABLE : Token.Type.COMMAND;

			Token t = new Token(code, type, value, line, pos, text.getPos());
			return t;
		}

		throw new InterpreterException(Loc.get("indefinite_lexeme") + ".", line, pos, text.getPos());
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

	public void reset() {
		text.reset();
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

	private HashMap<String, Token.Code> command;
}
