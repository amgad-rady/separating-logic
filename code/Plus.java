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
     * the given positive amount. 
     *
     * @param formula a formula
     * @param shift the positive shift amount  
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

     * @return the shift amount
     */
    public double getShift() {
	return this.shift;
    }

    /**
     * Returns a LaTeX representation of this formula.
     *
     * @return a LaTeX representation of this formula
     */	
    @Override
    public String toLaTeX() {
	return this.formula.toLaTeX() + " \\oplus " + this.shift;
    }
    
    /**
     * Returns a string representation of this formula.
     *
     * @return a string representation of this formula
     */
    @Override
    public String toString() {
	return this.formula.toString() + " + " + this.shift;
    }
}
