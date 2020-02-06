
public class Next extends Formula {
	private Formula formula;

	/**
	 * @param formula
	 */
	public Next(Formula formula) {
		super();
		this.formula = formula;
	}

	/**
	 * @return the formula
	 */
	public Formula getFormula() {
		return formula;
	}

	@Override
	public String toLaTeX() {
		return "\\bigcirc " + this.formula.toLaTeX();
	}
	
	@Override
	public String toString() {
		return "O " + this.formula.toString();
	}
}
