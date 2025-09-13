package game.weekend.interpreter;

import java.io.BufferedReader;

import game.weekend.texteditor.Loc;

public class Interpreter {

	public Interpreter(BufferedReader inp, IOutput out) throws InterpreterException {
		this.out = out;
		text = new Text(inp);
		variables = new Variables();
		tokenReader = new TokenReader(text);
		labels = new Labels(tokenReader);
		expressions = new Expressions(tokenReader, variables);
		commands = new Commands(this, tokenReader, variables, expressions, labels);
	}

	public void execute() throws InterpreterException {
		out.clear();

		Token t;
		do {
			// Sequential reading of lexemes
			t = tokenReader.getToken();
			if (t.type == Token.Type.VARIABLE) {
				// Variable. Let's remember its name.
				String varName = t.value;

				// Since this is a variable, we expect an assignment operator
				t = tokenReader.getToken();
				if (!t.value.equals("=")) {
					throw new InterpreterException(Loc.get("an_assignment_operator_is_expected") + ".", t.line,
							t.fromPos, t.toPos);
				}

				// and an expression. We calculate its value
				int result = expressions.getExp();
				// and assign it to the variable.
				variables.setVar(varName, result);

				// If it is not a variable, then we expect a command
			} else if (t.type == Token.Type.COMMAND) {
				// For each command, we call the corresponding method of the commands object
				if (t.code == Token.Code.PRINT) {
					commands.print_();
				} else if (t.code == Token.Code.PRINTLN) {
					commands.println_();
				} else if (t.code == Token.Code.INPUT) {
					commands.input_();
				} else if (t.code == Token.Code.GOTO) {
					commands.goto_(t);
				} else if (t.code == Token.Code.IF) {
					commands.if_();
				} else if (t.code == Token.Code.FOR) {
					commands.for_();
				} else if (t.code == Token.Code.NEXT) {
					commands.next_();
				} else if (t.code == Token.Code.GOSUB) {
					commands.gosub_();
				} else if (t.code == Token.Code.RETURN) {
					commands.return_();
				} else if (t.code == Token.Code.END) {
					break;
				} else if (t.code == Token.Code.REM) {
					commands.rem_();
				}
			}
		} while (t.code != Token.Code.EOF && !stop);

		if (stop) {
			out.println(Loc.get("execution_interrupted") + ".");
		} else {
			out.println(Loc.get("execution_completed") + ".");
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
	private Labels labels;
	private boolean stop = false;
}
