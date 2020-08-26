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
 * A formula.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public abstract class Formula {
	protected boolean simplified;
	
	/**
	 * Initializes this formula.
	 */
	public Formula() {
		this.simplified = false;
	}
	
	/**
	 * Initializes this formula.
	 * 
	 * @param simplified whether this formula is simplified
	 */
	public Formula(boolean simplified) {
		this.simplified = simplified;
	}
	
	/**
	 * Returns a simplification of this formula that is semantically equivalent to this formula.
	 *
	 * @return a simplification of this formula
	 */
	public abstract Formula simplify();

	/**
	 * Returns a LaTeX representation of this formula.
	 *
	 * @return a LaTeX representation of this formula
	 */
	public abstract String toLaTeX();
}
