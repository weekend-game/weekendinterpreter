package game.weekend.interpreter;

import javax.swing.JOptionPane;

import game.weekend.texteditor.Loc;

class Commands {

	protected Commands(Interpreter i, TokenReader tr, Variables vr, Expressions ex, Labels lb) {
		inter = i;
		tokenReader = tr;
		variables = vr;
		labels = lb;
		expressions = ex;
	}

	/**
	 * Command PRINT
	 * 
	 * @throws InterpreterException
	 */
	protected void print_() throws InterpreterException {
		Token t;
		do {
			t = tokenReader.getToken();
			if (t.code == Token.Code.EOL || t.code == Token.Code.EOF)
				return;

			String result = "";
			if (t.type == Token.Type.STRING) {
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
	 * Command PRINTLN
	 * 
	 * @throws InterpreterException
	 */
	protected void println_() throws InterpreterException {
		print_();
		inter.out.println("");
	}

	/**
	 * Command INPUT
	 * 
	 * @throws InterpreterException
	 */
	protected void input_() throws InterpreterException {
		Token t = tokenReader.getToken();
		String mes = "";
		if (t.type == Token.Type.STRING) {
			mes = t.value;
			t = tokenReader.getToken();
			if (t.type != Token.Type.DELIMITER && !t.value.equals(",")) {
				throw new InterpreterException(
						Loc.get("syntax_error_in_input_statement") + ". " + Loc.get("delimiter_not_found") + ".",
						t.line, t.fromPos, t.toPos);
			}
			t = tokenReader.getToken();
		}

		if (t.type != Token.Type.VARIABLE) {
			throw new InterpreterException(
					Loc.get("syntax_error_in_input_statement") + ". " + Loc.get("variable_expected") + ".", t.line,
					t.fromPos, t.toPos);
		}

		String value = JOptionPane.showInputDialog(mes);
		int i = 0;
		if (value != null) {
			try {
				i = Integer.parseInt(value);
			} catch (NumberFormatException ignored) {
			}
		}
		variables.setVar(t.value, i);

		inter.out.println(mes + " " + i);
	}

	/**
	 * Command GOTO
	 * 
	 * @throws InterpreterException
	 */
	protected void goto_(Token token) throws InterpreterException {
		int number = expressions.getExp();

		Labels.Label l = labels.getLabel(number);

		if (l == null)
			throw new InterpreterException(Loc.get("label") + " " + number + " " + Loc.get("not_found_") + ".",
					token.line, token.fromPos, token.toPos);

		labels.goToLabel(l);
	}

	/**
	 * Command IF
	 * 
	 * @throws InterpreterException
	 */
	protected void if_() throws InterpreterException {
		// Left expression
		int left = expressions.getExp();

		// Comparison operation
		Token t = tokenReader.getToken();
		if (!t.value.equals("<") && !t.value.equals(">") && !t.value.equals("=") && !t.value.equals("#")) {
			throw new InterpreterException(Loc.get("comparison_operator_expected") + ".", t.line, t.fromPos, t.toPos);
		}

		// Right expression
		int right = expressions.getExp();

		// Result of comparison operation
		boolean cond = false;
		if (t.value.equals("<")) {
			if (left < right) {
				cond = true;
			}
		} else if (t.value.equals(">")) {
			if (left > right) {
				cond = true;
			}
		} else if (t.value.equals("=")) {
			if (left == right) {
				cond = true;
			}
		} else if (t.value.equals("#")) {
			if (left != right) {
				cond = true;
			}
		}

		// Transitions
		if (cond) {
			Token th = tokenReader.getToken();
			if (!th.value.equalsIgnoreCase("THEN")) {
				throw new InterpreterException(Loc.get("a_then_statement_is_required") + ".", t.line, t.fromPos,
						t.toPos);
			}
		} else {
			tokenReader.nextLine();
		}
	}

	/**
	 * Command FOR
	 * 
	 * @throws InterpreterException
	 */
	protected void for_() throws InterpreterException {

		// Loop variable
		Token t = tokenReader.getToken();
		if (t.type != Token.Type.VARIABLE) {
			throw new InterpreterException(Loc.get("not_variable") + ".", t.line, t.fromPos, t.toPos);
		}
		String varName = t.value;

		// Assignment operator expected
		t = tokenReader.getToken();
		if (!t.value.equals("=")) {
			throw new InterpreterException(Loc.get("an_assignment_operator_is_required") + ".", t.line, t.fromPos,
					t.toPos);
		}

		// Initial value of the loop variable
		int init = expressions.getExp();
		variables.setVar(varName, init);

		// TO operator expected
		t = tokenReader.getToken();
		if (!t.value.equalsIgnoreCase("TO")) {
			throw new InterpreterException(Loc.get("a_to_operator_is_required") + ".", t.line, t.fromPos, t.toPos);
		}

		// The final value of the loop variable
		int target = expressions.getExp();

		if (init <= target) {
			ForItem fi = new ForItem(varName, target, tokenReader.getLine());
			forStack.push(fi);
		} else {
			while (!t.value.equalsIgnoreCase("NEXT")) {
				t = tokenReader.getToken();
			}
		}
	}

	/**
	 * Command NEXT
	 * 
	 * @throws InterpreterException
	 */
	protected void next_() throws InterpreterException {
		ForItem fi = forStack.pop();
		int init = variables.getVar(fi.var) + 1;
		if (init <= fi.target) {
			variables.setVar(fi.var, init);
			forStack.push(fi);
			tokenReader.setLine(fi.line);
		}
	}

	/**
	 * Command GOSUB
	 * 
	 * @throws InterpreterException
	 */
	protected void gosub_() throws InterpreterException {
		int label = expressions.getExp();
		if (labels.getLabel(label) == null) {
			throw new InterpreterException(Loc.get("undefined_label") + ".", 0, 0, 0);
		}
		int line = tokenReader.getLine();
		subStack.push(line);
		labels.goToLabel(label);
	}

	/** Command RETURN */
	protected void return_() {
		int line = subStack.pop();
		tokenReader.setLine(line);
	}

	/** Command REM */
	protected void rem_() throws InterpreterException {
		tokenReader.nextLine();
	}

	private Interpreter inter;
	private TokenReader tokenReader;
	private Variables variables;
	private Labels labels;
	private Expressions expressions;

	private Stack<ForItem> forStack = new Stack<ForItem>();
	private Stack<Integer> subStack = new Stack<Integer>();

	private class ForItem {
		public final String var;
		public final int target;
		public final int line;

		protected ForItem(String var, int target, int line) {
			this.var = var;
			this.target = target;
			this.line = line;
		}
	}
}
