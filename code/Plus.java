/*
 * Copyright (C)  2020  Amgad Rady and Franck van Breugel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * The formula shifted by some positive amount.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Plus extends Formula {
	private Formula formula;
	private double shift;

	/**
	 * Initializes this formula consisting of the given formula shifted by
	 * the given amount positively (adding).
	 *
	 * @param formula a formula
	 * @param shift   the shift amount
	 * @pre. shift in [0, 1]
	 */
	public Plus(Formula formula, double shift) {
		super();
		this.formula = formula;
		this.shift = shift;
	}

	/**
	 * Returns the subformula of this formula.
	 *
	 * @return the subformula of this formula
	 */
	public Formula getFormula() {
		return this.formula;
	}

	/**
	 * Returns the shift amount.
	 *
	 * @return the shift amount
	 */
	public double getShift() {
		return this.shift;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula simplify() {
		Formula simpliedFormula = this.formula.simplify();

		if (simpliedFormula instanceof False) {
			return simpliedFormula;
		} else if (this.shift == 0) {
			return simpliedFormula;
		} else if (this.shift == 1) {
			return new False();
		} else {
			return new Plus(simpliedFormula, this.shift);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSmaller(Formula other) {
		if (other instanceof Plus) {
			if (this.formula.isSmaller(((Plus) other).formula)) {
				return this.shift <= ((Plus) other).shift;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toLaTeX() {
		return "(" + this.formula.toLaTeX() + " \\oplus " + this.shift + ")";
	}

	/**
	 * Tests whether this formula is syntactically equivalent to the given object.
	 * 
	 * @param object an object
	 * @return true if this formula is syntactically equivalent to the given object,
	 * false otherwise.
	 */
	public boolean equals(Object object) {
		final double EPSILON = 0.0000000001;

		if (object instanceof Plus) {
			Plus other = (Plus) object;
			return this.formula.equals(other.formula) && Math.abs(this.shift - other.shift) < EPSILON;
		} else {
			return false;
		}
	}

	/**
	 * Returns a string representation of this formula.
	 *
	 * @return a string representation of this formula
	 */
	@Override
	public String toString() {
		return "(" + this.formula.toString() + " + " + this.shift + ")";
	}
}
