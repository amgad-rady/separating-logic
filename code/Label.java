
public class Label extends Formula {
	private int label;

	/**
	 * @param label
	 */
	public Label(int label) {
		super();
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public int getLabel() {
		return this.label;
	}

	@Override
	public String toLaTeX() {
		return "label" + this.label;
	}
	
	@Override
	public String toString() {
		return "label" + this.label;
	}
}
