package game.weekend.interpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import game.weekend.texteditor.Act;
import game.weekend.texteditor.Filer;
import game.weekend.texteditor.Output;

/**
 * Упрвление выполнением интерпретатором текста программы.
 */
public class Runner {

	/**
	 * Создать объект управляющий выполнением интерпретатором текста программы.
	 * 
	 * @param filer  - управление файлом программы.
	 * @param output - реализация интерфейса Output для обработки сообщений
	 *               интерпретатора.
	 */
	public Runner(Filer filer, Output output) {
		this.filer = filer;
		this.output = output;
	}

	/**
	 * Установить объект управляющий действиями.
	 */
	public void setAct(Act act) {
		this.act = act;
	}

	/**
	 * "Выполнить".
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
	 * "Остановить".
	 */
	public void stop() {
		if (starter != null) {
			starter.stopRunning();
		}
	}

	/**
	 * Запуск интерпретатора в собственном потоке.
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
				out.error(e); // Сообщения об ошибках передаются как исключения
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
		 * Остановить выполнение.
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
