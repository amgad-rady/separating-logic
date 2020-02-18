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
	super();
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
     * Returns a LaTeX representation of this formula.
     *
     * @return a LaTeX representation of this formula
     */	
    @Override
    public String toLaTeX() {
	return "\\bigcirc " + this.formula.toLaTeX();
    }

    /**
     * Returns a string representation of this formula.
     *
     * @return a string representation of this formula
     */	
    @Override
    public String toString() {
	return "O " + this.formula.toString();
    }
}
