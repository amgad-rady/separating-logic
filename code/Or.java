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
 * The disjunction formula.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Or extends Formula {
	private Formula left;
	private Formula right;

	/**
	 * Initializes this formula as the disjunction of the given formulas.
	 *
	 * @param left  the left subformula
	 * @param right the right subformula
	 */
	public Or(Formula left, Formula right) {
		super();
		this.left = left;
		this.right = right;
	}

	/**
	 * Returns the left subformula.
	 *
	 * @return the left subformula
	 */
	public Formula getLeft() {
		return this.left;
	}

	/**
	 * Returns the right subformula.
	 *
	 * @return the right subformula
	 */
	public Formula getRight() {
		return this.right;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Formula simplify() {
		Formula simplifiedLeft = this.left.simplify();
		Formula simplifiedRight = this.right.simplify();

		if (simplifiedLeft.isSmaller(simplifiedRight)) {
			return simplifiedLeft;
		} else if (simplifiedRight.isSmaller(simplifiedLeft)) {
			return simplifiedRight;
		} else {
			return new Or(simplifiedLeft, simplifiedRight);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSmaller(Formula other) {
		if (other instanceof Or) {
			return this.left.isSmaller(((Or) other).left) && this.right.isSmaller(((Or) other).right);
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toLaTeX() {
		return "(" + this.left.toLaTeX() + " \\vee " + this.right.toLaTeX() + ")";
	}

	/**
	 * Tests whether this formula is syntactically equivalent to the given object.
	 * 
	 * @param object an object
	 * @return true if this formula is syntactically equivalent to the given object,
	 * false otherwise.
	 */
	public boolean equals(Object object) {		
		if (object instanceof Or) {
			Or other = (Or) object;
			return this.left.equals(other.left) && this.right.equals(other.right);
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
		return "(" + this.left.toString() + " || " + this.right.toString() + ")";
	}
}
