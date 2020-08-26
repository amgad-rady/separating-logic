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

import java.util.HashSet;
import java.util.Set;

/**
 * The disjunction formula.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Or extends Formula {
	private Set<Formula> subformulas;

	/**
	 * Initializes this formula as an empty disjunction.
	 */
	public Or() {
		this(false);
	}

	/**
	 * Initializes this formula as an empty disjunction.
	 * 
	 * @param simplified whether this formula is simplified
	 */
	public Or(boolean simplified) {
		super(simplified);
		this.subformulas = new HashSet<Formula>();
	}

	/**
	 * Returns the subformulas.
	 *
	 * @return the subformulas
	 */
	public Set<Formula> getSubformulas() {
		return this.subformulas;
	}

	/**
	 * Add the given formula to this conjunction.
	 * 
	 * @param formula a formula
	 */
	public void add(Formula formula) {
		this.subformulas.add(formula);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula simplify() {
		// simplify each subformula, exclude False, simplify a disjunction to True if it contains True, simplify an empty disjunction to False
		Or simplification = new Or();
		boolean isEmpty = true; // disjunction is empty
		for (Formula subformula : this.getSubformulas()) {
			Formula simplified = subformula.simplified ? subformula : subformula.simplify();
			if (simplified instanceof True) {
				return simplified;
			} else if (!(simplified instanceof False)) {
				simplification.add(simplified);
				isEmpty = false;
			}
		}
		if (isEmpty) {
			return new False();
		}

		// exclude each subformula for which there is another subformula that is smaller or equal
		Or furtherSimplification = new Or(true);
		for (Formula one : simplification.getSubformulas()) {
			boolean found = false; // another formula that is smaller than or equal to one has been found
			for (Formula other : simplification.getSubformulas()) {
				found = found || (one != other && Compare.isSmaller(other, one));
			}
			if (!found) {
				furtherSimplification.add(one);
			}
		}
		return furtherSimplification;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toLaTeX() {
		if (this.subformulas.isEmpty()) {
			return "\\mathrm{false}";
		} else {
			Formula[] part = this.subformulas.toArray(new Formula[0]);
			if (this.subformulas.size() == 1) {
				return part[0].toLaTeX();
			} else {
				StringBuffer representation = new StringBuffer("(");
				for (int i = 0; i < this.subformulas.size() - 1; i++) {
					representation.append(part[i].toLaTeX());
					representation.append(" \\vee ");
				}
				representation.append(part[this.subformulas.size() - 1].toLaTeX());
				representation.append(")");
				return representation.toString();
			}
		}
	}

	/**
	 * Tests whether this formula is syntactically equivalent to the given object.
	 * 
	 * @param object an object
	 * @return true if this formula is syntactically equivalent to the given object,
	 * false otherwise.
	 */
	public boolean equals(Object object) {		
		if (object != null && object instanceof Or) {
			Or other = (Or) object;
			return this.subformulas.equals(other.subformulas);
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
		int hash = 17;
		for (Formula subformula : this.subformulas) {
			hash = hash * 31 + subformula.hashCode();
		}
		return hash;
	}

	/**
	 * Returns a string representation of this formula.
	 *
	 * @return a string representation of this formula
	 */
	@Override
	public String toString() {
		if (this.subformulas.isEmpty()) {
			return "false";
		} else {
			Formula[] part = this.subformulas.toArray(new Formula[0]);
			if (this.subformulas.size() == 1) {
				return part[0].toString();
			} else {
				StringBuffer representation = new StringBuffer("(");
				for (int i = 0; i < this.subformulas.size() - 1; i++) {
					representation.append(part[i].toString());
					representation.append(" || ");
				}
				representation.append(part[this.subformulas.size() - 1].toString());
				representation.append(")");
				return representation.toString();
			}
		}
	}
}
