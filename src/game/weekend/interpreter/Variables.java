package game.weekend.interpreter;

class Variables {

	protected Variables(Text text) {
	}

	protected int getVar(String name) throws InterpreterException {
		int c = (int) name.toUpperCase().charAt(0);
		if (c < 65 || c > 90) {
			throw new InterpreterException("Имя '" + name + "' не является переменной.", 0, 0, 0);
		}
		return var[c - 65];
	}

	protected void setVar(String name, int value) throws InterpreterException {
		int c = (int) name.toUpperCase().charAt(0);
		if (c < 65 || c > 90) {
			throw new InterpreterException("Имя '" + name + "' не является переменной.", 0, 0, 0);
		}
		var[c - 65] = value;
	}

	private int[] var = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
}
