package game.weekend.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

class Text {

	protected Text(BufferedReader file) {
		String s = "";

		while (s != null) {
			try {
				s = file.readLine();
				if (s != null) {
					text.add(s.trim());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected int read() {
		int c = bufferedChar;

		if (bufferedChar != -1) {
			bufferedChar = -1;
			return c;
		}

		if (line <= text.size()) {
			String s = text.get(line - 1);
			if (pos <= s.length()) {
				c = s.charAt(pos - 1);
				++pos;
			} else {
				c = '\n';
				pos = 1;
				++line;
			}
		}
		return c;
	}

	protected void backChar(int c) {
		bufferedChar = c;
	}

	protected int getLine() {
		return line;
	}

	protected void nextLine() {
		bufferedChar = -1;
		pos = 1;
		++line;
	}

	protected void setLine(int number) {
		bufferedChar = -1;
		pos = 1;
		line = number;
	}

	protected int getPos() {
		return pos;
	}

	protected void reset() {
		bufferedChar = -1;
		pos = 1;
		line = 1;
	}

	private ArrayList<String> text = new ArrayList<String>();
	private int bufferedChar = -1;
	private int pos = 1;
	private int line = 1;
}
