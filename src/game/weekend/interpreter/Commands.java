package game.weekend.interpreter;

class Commands {

	protected Commands(Interpreter i, TokenReader tr, Expressions ex) {
		inter = i;
		tokenReader = tr;
		expressions = ex;
	}

	/**
	 * Команда PRINT
	 * 
	 * @throws InterpreterException
	 */
	protected void print_() throws InterpreterException {
		Token t;
		do {
			t = tokenReader.getToken();
			if (t.code == Token.EOL || t.code == Token.EOF)
				return;

			String result = "";
			if (t.type == Token.STRING) {
				result = t.value;
			} else {
				tokenReader.backToken(t);
				result = "" + expressions.getExp();
			}
			inter.out.print(result);

			t = tokenReader.getToken();
		} while (t.value.equals(";") || t.value.equals(","));
	}

	/**
	 * Команда PRINTLN
	 * 
	 * @throws InterpreterException
	 */
	protected void println_() throws InterpreterException {
		print_();
		inter.out.println("");
	}

	/** Команда REM */
	protected void rem_() throws InterpreterException {
		tokenReader.nextLine();
	}

	private Interpreter inter;
	private TokenReader tokenReader;
	private Expressions expressions;
}
