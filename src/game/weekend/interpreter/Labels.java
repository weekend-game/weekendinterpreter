package game.weekend.interpreter;

import java.util.ArrayList;

import game.weekend.texteditor.Loc;

class Labels {
	public Labels(TokenReader tr, Text tx) throws InterpreterException {
		tokenReader = tr;
		text = tx;

		Token t;
		int line = 1;
		do {
			tokenReader.setLine(line);
			t = tokenReader.getToken();
			if (t.type == Token.NUMBER) {
				if (findLabel(t.code) != null) {
					throw new InterpreterException(Loc.get("duplicate_labels") + ".", t.line, t.fromPos, t.toPos);
				}
				int n = Integer.parseInt(t.value);
				labels.add(new Label(n, line));
			}
			++line;
		} while (t.code != Token.EOF);

		text.reset();
	}

	public void goToLabel(int no) {
		Label l = findLabel(no);
		if (l != null) {
			tokenReader.setLine(l.position);
		}
	}

	public Label findLabel(int name) {
		for (Label l : labels) {
			if (l.name == name) {
				return l;
			}
		}
		return null;
	}

	private TokenReader tokenReader;
	private Text text;
	private ArrayList<Label> labels = new ArrayList<Label>();

	private class Label {
		public Label(int name, int position) {
			this.name = name;
			this.position = position;
		}

		private final int name;
		private final int position;
	}
}
