package game.weekend.interpreter;

import java.util.ArrayList;

import game.weekend.texteditor.Loc;

class Labels {
	public Labels(TokenReader tokenReader) throws InterpreterException {
		this.tokenReader = tokenReader;

		Token token;
		int line = 1;
		do {
			tokenReader.setLine(line);
			token = tokenReader.getToken();
			if (token.type == Token.Type.NUMBER) {
				int number = Integer.parseInt(token.value);
				if (getLabel(number) != null) {
					throw new InterpreterException(Loc.get("duplicate_labels") + ".", token.line, token.fromPos,
							token.toPos);
				}
				labels.add(new Label(number, line));
			}
			++line;
		} while (token.code != Token.Code.EOF);

		tokenReader.reset();
	}

	public void goToLabel(int number) {
		Label l = getLabel(number);
		if (l != null)
			tokenReader.setLine(l.line);
	}

	public void goToLabel(Label label) {
		tokenReader.setLine(label.line);
	}

	public Label getLabel(int number) {
		for (Label l : labels) {
			if (l.number == number) {
				return l;
			}
		}
		return null;
	}

	private TokenReader tokenReader;
	private ArrayList<Label> labels = new ArrayList<Label>();

	public static class Label {
		public Label(int number, int line) {
			this.number = number;
			this.line = line;
		}

		private final int number;
		private final int line;
	}
}
