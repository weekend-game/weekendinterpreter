package game.weekend.interpreter;

import java.io.BufferedReader;

public class Interpreter {

	public Interpreter(BufferedReader inp, IOutput out) throws InterpreterException {
		this.out = out;
		text = new Text(inp);
		variables = new Variables(text);
		tokenReader = new TokenReader(text);
		expressions = new Expressions(tokenReader, variables);
		commands = new Commands(this, tokenReader, expressions);
	}

	public void execute() throws InterpreterException {
		out.clear();

		Token t;
		do {
			// Последовательно читаем лексемы.
			t = tokenReader.getToken();
			if (t.type == Token.VARIABLE) {
				// Переменная. Запомним её имя.
				String varName = t.value;

				// Раз это переменная, то далее ожидаем оператор
				// присваивания
				t = tokenReader.getToken();
				if (!t.value.equals("=")) {
					throw new InterpreterException("Ожидается оператор присваивания.", t.line, t.fromPos, t.toPos);
				}

				// и выражение. Вычисляем его значение
				int result = expressions.getExp();
				// и присваиваем переменной.
				variables.setVar(varName, result);

				// Если не переменная, то ожидаем команду.
			} else if (t.type == Token.COMMAND) {
				// Для каждой команды вызываем соответстыующий метод объекта
				// команд.
				if (t.code == Token.PRINT) {
					commands.print_();
				} else if (t.code == Token.PRINTLN) {
					commands.println_();
				} else if (t.code == Token.REM) {
					commands.rem_();
				}
			}
		} while (t.code != Token.EOF && !stop);

		if (stop) {
			out.println("Выполнение прервано.");
		} else {
			out.println("Выполнение завершено.");
		}
	}

	public void stop() {
		stop = true;
	}

	protected IOutput out;

	private Text text;
	private Variables variables;
	private TokenReader tokenReader;
	private Expressions expressions;
	private Commands commands;
	private boolean stop = false;
}
