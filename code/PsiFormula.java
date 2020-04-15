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
 * @author Amgad Rady
 */
public class PsiFormula {
  /**
   * Return \psi_{stuv}^n
   *
   * @param u         first factor
   * @param v         second factor
   * @param n         index
   * @param labels    state labels of the LMC
   * @param KR_dual   Kantorovich-Rubinstein dual function g_{st}^n
   * @param distances Distance function d^n(x, y)
   * @return The formula \psi_{stuv}^n
   */
  public static Formula generate_formula(int u, int v, int n,
                                         int[] labels, double[] KR_dual, double[][] distances) throws IllegalArgumentException {
    if (n < 0) {
      throw new IllegalArgumentException("n cannot be negative");
    }

    int first_index;
    int second_index;

    if (KR_dual[u] <= KR_dual[v]) {
      first_index = u;
      second_index = v;
    } else {
      first_index = v;
      second_index = u;
    }

    return (new Plus(
      new Minus(
        formula_constructor(first_index, second_index, n, labels),
        distances[u][v] - Math.abs(KR_dual[u] - KR_dual[v])),
      Math.min(KR_dual[u], KR_dual[v]))).simplify();
  }

  public static Formula formula_constructor(int first_index, int second_index, int iteration, int[] labels) {
    if (labels[first_index] != labels[second_index]) {
      return new Label(labels[first_index]);
    } else if ((iteration == 0) || (first_index == second_index)) {
      return new True();
    } else {
      return new Phi(first_index, second_index, iteration);
    }
  }
}