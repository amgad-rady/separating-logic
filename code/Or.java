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
   * Returns a LaTeX representation of this formula.
   *
   * @return a LaTeX representation of this formula
   */
  @Override
  public String toLaTeX() {
    if ((this.left instanceof Identity) && (this.right instanceof Identity)) {
      return "";
    } else if ((this.left instanceof Identity) && !(this.right instanceof Identity)) {
      return this.right.toLaTeX();
    } else if ((this.right instanceof Identity) && !(this.left instanceof Identity)) {
      return this.left.toLaTeX();
    } else {
      return "\\left(" + this.left.toLaTeX() + " \\vee " + this.right.toLaTeX() + "\\right)";
    }
  }

  /**
   * Returns a string representation of this formula.
   *
   * @return a string representation of this formula
   */
  @Override
  public String toString() {
    if ((this.left instanceof Identity) && (this.right instanceof Identity)) {
      return "";
    } else if ((this.left instanceof Identity) && !(this.right instanceof Identity)) {
      return this.right.toString();
    } else if ((this.right instanceof Identity) && !(this.left instanceof Identity)) {
      return this.left.toString();
    } else {
      return "(" + this.left.toString() + " || " + this.right.toString() + ")";
    }
  }
}
