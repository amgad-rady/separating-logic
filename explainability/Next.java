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

package explainability;

/**
 * The next formula.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Next extends Formula {
	private Formula formula;

	/**
	 * Initializes this next formula with the given subformula.
	 *
	 * @param formula the subformula
	 */
	public Next(Formula formula) {
		this(formula, false);
	}

	/**
	 * Initializes this next formula with the given subformula.
	 *
	 * @param formula the subformula
	 * @param simplified whether this formula is simplified
	 */
	public Next(Formula formula, boolean simplified) {
		super(simplified);
		this.formula = formula;
	}

	/**
	 * Returns the subformula of this next formula.
	 *
	 * @return the subformula of this next formula
	 */
	public Formula getFormula() {
		return this.formula;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula simplify() {
		Formula simplified = this.formula.simplified ? this.formula : this.formula.simplify();
		if (simplified instanceof True) {
			return simplified; // Proposition 26(a) and 28(c)
		} else {
			return new Next(simplified, true); // Proposition 26(a)
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toLaTeX() {
		return "(\\bigcirc" + this.formula.toLaTeX() + ")";
	}

	/**
	 * Tests whether this formula is syntactically equivalent to the given object.
	 * 
	 * @param object an object
	 * @return true if this formula is syntactically equivalent to the given object,
	 * false otherwise.
	 */
	public boolean equals(Object object) {
		if (object != null && object instanceof Next) {
			Next next = (Next) object;
			return this.formula.equals(next.formula);
		} else {
			return false;
		}
	}

	/**
	 * Returns the hash code of this formula.
	 * 
	 * @return the hash code of this formula
	 */
	public int hashCode() {
		return this.formula.hashCode() + 1;
	}


	/**
	 * Returns a string representation of this formula.
	 *
	 * @return a string representation of this formula
	 */
	@Override
	public String toString() {
		return "O [" + this.formula.toString() + "]";
	}
}
