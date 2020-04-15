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
 * The formula consisting of a label.
 *
 * @author Amgad Rady
 * @author Franck van Breugel
 */
public class Label extends Formula {
  private int label; // the index of the label

  /**
   * Initializes this formula representing a label with the given index.
   *
   * @param label the index of the label
   */
  public Label(int label) {
    super();
    this.label = label;
  }

  /**
   * Returns the index of the label.
   *
   * @return the index of the label
   */
  public int getLabel() {
    return this.label;
  }

  /**
   * Returns a simplification of this formula that is semantically equivalent to this formula.
   *
   * @return a simplification of this formula
   */
  @Override
  public Formula simplify() {
    return this;
  }

  /**
   * Returns a LaTeX representation of this formula.
   *
   * @return a LaTeX representation of this formula
   */
  @Override
  public String toLaTeX() {
    return "\\mathrm{label}\\_" + this.label;
  }

  /**
   * Returns a string representation of this formula.
   *
   * @return a string representation of this formula
   */
  @Override
  public String toString() {
    return "label_" + this.label;
  }
}
