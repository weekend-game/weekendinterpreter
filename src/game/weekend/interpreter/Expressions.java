package game.weekend.interpreter;

import game.weekend.texteditor.Loc;

class Expressions {

	protected Expressions(TokenReader tr, Variables vr) {
		tokenReader = tr;
		variables = vr;
	}

	protected int getExp() throws InterpreterException {
		token = tokenReader.getToken();
		int res = level2();
		tokenReader.backToken(token);
		return res;
	}

	// + -
	private int level2() throws InterpreterException {
		int res1 = level3();
		String oper = token.value;
		while (oper.equals("+") || oper.equals("-")) {

			token = tokenReader.getToken();
			int res2 = level3();
			res1 = arith(res1, oper, res2);

			oper = token.value;
		}
		return res1;
	}

	// * / %
	private int level3() throws InterpreterException {

		int res1 = level4();
		String oper = token.value;
		while (oper.equals("*") || oper.equals("/") || oper.equals("%")) {

			token = tokenReader.getToken();
			int res2 = level4();
			res1 = arith(res1, oper, res2);

			oper = token.value;
		}
		return res1;
	}

	// ^1
	private int level4() throws InterpreterException {

		int res1 = level5();
		String oper = token.value;
		if (oper.equals("^")) {

			token = tokenReader.getToken();
			int res2 = level5();
			res1 = arith(res1, oper, res2);

			oper = token.value;
		}
		return res1;
	}

	// Unary + и -
	private int level5() throws InterpreterException {

		String oper = token.value;
		if (oper.equals("+") || oper.equals("-")) {
			token = tokenReader.getToken();
		} else {
			oper = null;
		}

		int res1 = level6();

		if (oper != null && oper.equals("-")) {
			res1 = -res1;
		}
		return res1;
	}

	// Parentheses
	private int level6() throws InterpreterException {

		int res1 = 0;
		if (token.value.equals("(")) {
			token = tokenReader.getToken();
			res1 = level2();
			if (!token.value.equals(")")) {
				throw new InterpreterException(Loc.get("unpaired_parentheses") + ".", token.line, token.fromPos,
						token.toPos);
			}
			token = tokenReader.getToken();
		} else {
			res1 = level7();
		}
		return res1;
	}

	// Number
	private int level7() throws InterpreterException {
		int result = 0;

		if (token.type == Token.Type.NUMBER) {
			result = Integer.parseInt(token.value);
			token = tokenReader.getToken();
		} else if (token.type == Token.Type.VARIABLE) {
			result = variables.getVar(token.value);
			token = tokenReader.getToken();
		} else {
			throw new InterpreterException(Loc.get("syntax_error") + ".", token.line, token.fromPos, token.toPos);
		}
		return result;
	}

	private int arith(int arg1, String op, int arg2) {
		int result = 0;

		if (op.equals("-")) {
			return arg1 - arg2;
		}

		if (op.equals("+")) {
			return arg1 + arg2;
		}

		if (op.equals("*")) {
			return arg1 * arg2;
		}

		if (op.equals("/")) {
			return arg1 / arg2;
		}

		if (op.equals("%")) {
			return arg1 % arg2;
		}

		if (op.equals("^")) {
			return (int) Math.pow(arg1, arg2);
		}

		return result;
	}

	private Token token;
	private TokenReader tokenReader;
	private Variables variables;
}
