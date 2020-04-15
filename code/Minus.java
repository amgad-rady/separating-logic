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
 * The formula shifted by some negative amount.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Minus extends Formula {
  private Formula formula;
  private double shift;

  /**
   * Initializes this formula consisting of the given formula shifted by
   * the given amount negatively (subtracting).
   *
   * @param formula a formula
   * @param shift   the shift amount
   * @pre. shift in [0, 1]
   */
  public Minus(Formula formula, double shift) {
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
   * Returns a simplification of this formula that is semantically equivalent to this formula.
   *
   * @return a simplification of this formula
   */
  @Override
  public Formula simplify() {
    Formula simpliedFormula = this.formula.simplify();

    if (simpliedFormula instanceof True) {
      return simpliedFormula;
    } else if (this.shift == 0) {
      return simpliedFormula;
    } else if (this.shift == 1) {
      return new True();
    } else {
      return new Minus(simpliedFormula, this.shift);
    }
  }

  /**
   * Returns a LaTeX representation of this formula.
   *
   * @return a LaTeX representation of this formula
   */
  @Override
  public String toLaTeX() {
    return "\\left\\{" + this.formula.toLaTeX() + " \\ominus " + this.shift + "\\right\\}";
  }

  /**
   * Returns a string representation of this formula.
   *
   * @return a string representation of this formula
   */
  @Override
  public String toString() {
    return "{" + this.formula.toString() + " - " + this.shift + "}";
  }
}
