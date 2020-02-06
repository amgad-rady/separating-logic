
public class Or extends Formula {
	private Formula left;
	private Formula right;
	
	/**
	 * @param left
	 * @param right
	 */
	public Or(Formula left, Formula right) {
		super();
		this.left = left;
		this.right = right;
	}

	/**
	 * @return the left
	 */
	public Formula getLeft() {
		return this.left;
	}

	/**
	 * @return the right
	 */
	public Formula getRight() {
		return this.right;
	}

	@Override
	public String toLaTeX() {
		return this.left.toLaTeX() + " \\vee " + this.right.toLaTeX();
	}
	
	@Override
	public String toString() {
		return this.left.toString() + " || " + this.right.toString();
	}
}
