package game.weekend.interpreter;

class Stack<T> {

	static class Node<T> {
		public final T item;
		public final Node<T> next;

		public Node() {
			item = null;
			next = null;
		}

		public Node(T item, Node<T> next) {
			this.item = item;
			this.next = next;
		}

		private boolean end() {
			return (item == null && next == null);
		}
	}

	public void push(T item) {
		top = new Node<T>(item, top);
	}

	public T pop() {
		T i = top.item;
		if (!top.end()) {
			top = top.next;
		}
		return i;
	}

	private Node<T> top = new Node<T>();
}
