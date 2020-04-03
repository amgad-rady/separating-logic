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

    Formula phi;
    if (labels[u] != labels[v]) {
      phi = new Label(u);
    } else if (n == 0 && labels[u] == labels[v]) {
      phi = new True();
    } else {
      phi = new Phi(u, v, n);
    }

    double first_factor = distances[u][v] - Math.abs(KR_dual[u] - KR_dual[v]);
    double second_factor = Math.min(KR_dual[u], KR_dual[v]);

    if (first_factor == 0 && second_factor == 0) {
      return phi;
    } else if (first_factor != 0 && second_factor == 0) {
      return new Minus(phi, first_factor);
    } else if (first_factor == 0 && second_factor != 0) {
      return new Plus(phi, second_factor);
    } else {
      return new Plus(new Minus(phi, first_factor), second_factor);
    }
  }
}
