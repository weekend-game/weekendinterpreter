 package game.weekend.interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import game.weekend.texteditor.Act;
import game.weekend.texteditor.Filer;
import game.weekend.texteditor.Output;

/**
 * Control of execution by the interpreter.
 */
public class Runner {

	/**
	 * Create an object that controls the execution of the interpreter.
	 * 
	 * @param filer  - file handling object.
	 * @param output - implementation of the Output interface for processing
	 *               interpreter messages.
	 */
	public Runner(Filer filer, Output output) {
		this.filer = filer;
		this.output = output;
	}

	/**
	 * Set the Act object.
	 */
	public void setAct(Act act) {
		this.act = act;
	}

	/**
	 * "Run".
	 */
	public void run() {
		File file = filer.getFile();
		if (file != null) {

			BufferedReader inp = null;
			try {
				inp = new BufferedReader(new FileReader(file, Filer.CHARSET));
			} catch (IOException e) {
				e.printStackTrace();
			}

			starter = new Starter(inp, output);
			starter.start();
		}
	}

	/**
	 * "Stop".
	 */
	public void stop() {
		if (starter != null) {
			starter.stopRunning();
		}
	}

	/**
	 * Run the interpreter in its own thread.
	 */
	private class Starter extends Thread {

		public Starter(BufferedReader inp, Output out) {
			this.inp = inp;
			this.out = out;
		}

		@Override
		public void run() {
			if (act != null) {
				act.setRunMode(true);
			}

			try {
				inter = new Interpreter(inp, out);
				inter.execute();
				inter = null;
			} catch (InterpreterException e) {
				out.error(e); // Error messages are passed as exceptions
			} finally {
				if (act != null) {
					act.setRunMode(false);
				}

				if (inp != null) {
					try {
						inp.close();
					} catch (IOException ignored) {
					}
				}
			}
		}

		/**
		 * Stop the program execution.
		 */
		public void stopRunning() {
			inter.stop();
		}

		private final BufferedReader inp;
		private final Output out;
		private Interpreter inter;
	}

	private Filer filer;
	private Act act;
	private Output output;
	private Starter starter;
}
