
public class Plus extends Formula {
	private Formula formula;
	private double shift;
	
	/**
	 * @param formula
	 * @param shift
	 */
	public Plus(Formula formula, double shift) {
		super();
		this.formula = formula;
		this.shift = shift;
	}

	/**
	 * @return the formula
	 */
	public Formula getFormula() {
		return this.formula;
	}

	/**
	 * @return the shift
	 */
	public double getShift() {
		return this.shift;
	}

	@Override
	public String toLaTeX() {
		return this.formula.toLaTeX() + " \\oplus " + this.shift;
	}
	
	@Override
	public String toString() {
		return this.formula.toString() + " + " + this.shift;
	}
}
