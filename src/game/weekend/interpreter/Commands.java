package game.weekend.interpreter;

import javax.swing.JOptionPane;

class Commands {

	protected Commands(Interpreter i, TokenReader tr, Variables vr, Expressions ex, Labels lb) {
		inter = i;
		tokenReader = tr;
		variables = vr;
		labels = lb;
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

	/**
	 * Команда INPUT
	 * 
	 * @throws InterpreterException
	 */
	protected void input_() throws InterpreterException {
		Token t = tokenReader.getToken();
		String mes = "";
		if (t.type == Token.STRING) {
			mes = t.value;
			t = tokenReader.getToken();
			if (t.type != Token.DELIMITER && !t.value.equals(",")) {
				throw new InterpreterException("Синтаксическая ошибка в операторе INPUT. Нет разделителя.", t.line,
						t.fromPos, t.toPos);
			}
			t = tokenReader.getToken();
		}

		if (t.type != Token.VARIABLE) {
			throw new InterpreterException("Синтаксическая ошибка в операторе INPUT. Ожидается переменная.", t.line,
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
		
		inter.out.println(mes + " : " + i);
	}

	/**
	 * Команда GOTO
	 * 
	 * @throws InterpreterException
	 */
	protected void goto_() throws InterpreterException {
		int l = expressions.getExp();
		labels.goToLabel(l);
	}

	/**
	 * Команда IF
	 * 
	 * @throws InterpreterException
	 */
	protected void if_() throws InterpreterException {
		// Левое вырожение
		int left = expressions.getExp();

		// Операция сравнения
		Token t = tokenReader.getToken();
		if (!t.value.equals("<") && !t.value.equals(">") && !t.value.equals("=") && !t.value.equals("#")) {
			throw new InterpreterException("Ожидается оператор сравнения.", t.line, t.fromPos, t.toPos);
		}

		// Правое выражение
		int right = expressions.getExp();

		// Результат операции сравнения
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

		// Переходы
		if (cond) {
			Token th = tokenReader.getToken();
			if (!th.value.equalsIgnoreCase("THEN")) {
				throw new InterpreterException("Необходим оператор THEN.", t.line, t.fromPos, t.toPos);
			}
		} else {
			tokenReader.nextLine();
		}
	}

	/**
	 * Команда FOR
	 * 
	 * @throws InterpreterException
	 */
	protected void for_() throws InterpreterException {

		// Переменная цикла
		Token t = tokenReader.getToken();
		if (t.type != Token.VARIABLE) {
			throw new InterpreterException("Не переменная.", t.line, t.fromPos, t.toPos);
		}
		String varName = t.value;

		// Ожидаем оператор присваивания
		t = tokenReader.getToken();
		if (!t.value.equals("=")) {
			throw new InterpreterException("Необходим оператор присваивания.", t.line, t.fromPos, t.toPos);
		}

		// Начальное значение переменной цикла
		int init = expressions.getExp();
		variables.setVar(varName, init);

		// Ожидаем оператор TO
		t = tokenReader.getToken();
		if (!t.value.equalsIgnoreCase("TO")) {
			throw new InterpreterException("Необходим оператор TO.", t.line, t.fromPos, t.toPos);
		}

		// Конечное значение переменной цикла
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
	 * Команда NEXT
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
	 * Команда GOSUB
	 * 
	 * @throws InterpreterException
	 */
	protected void gosub_() throws InterpreterException {
		int label = expressions.getExp();
		if (labels.findLabel(label) == null) {
			throw new InterpreterException("Неопределённая метка.", 0, 0, 0);
		}
		int line = tokenReader.getLine();
		subStack.push(line);
		labels.goToLabel(label);
	}

	/** Команда RETURN */
	protected void return_() {
		int line = subStack.pop();
		tokenReader.setLine(line);
	}

	/** Команда END */
	protected void end_() {
	}

	/** Команда REM */
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
