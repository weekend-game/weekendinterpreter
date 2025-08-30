package game.weekend.interpreter;

import java.io.BufferedReader;

import game.weekend.texteditor.Output;

public class Interpreter {

	public Interpreter(BufferedReader inp, Output out) {
		this.inp = inp;
		this.out = out;
	}

	public void execute() throws InterpreterException {
		out.println("Запущен интерпретатор.");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {
		}
		out.println("Интерпретатор остановлен.");
	}

	public void stop() {
		out.println("Дана команда остановить интерпретатор.");
	}

	private BufferedReader inp;
	private Output out;
}
