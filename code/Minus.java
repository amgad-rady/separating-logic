
public class Minus extends Formula {
	private Formula formula;
	private double shift;
	
	/**
	 * @param formula
	 * @param shift
	 */
	public Minus(Formula formula, double shift) {
		super();
		this.formula = formula;
		this.shift = shift;
	}

	/**
	 * @return the formula
	 */
	public Formula getFormula() {
		return formula;
	}

	/**
	 * @return the shift
	 */
	public double getShift() {
		return shift;
	}

	@Override
	public String toLaTeX() {
		return this.formula.toLaTeX() + " \\ominus " + this.shift;
	}
	
	@Override
	public String toString() {
		return this.formula.toString() + " - " + this.shift;
	}
}
