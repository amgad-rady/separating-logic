/**
 * A class that contains formula constructors that utilize semantic simplification.
 *
 * @author Amgad Rady
 */
public class Constructor {
  public static Formula formula_constructor(String param, Formula left, Formula right) {
    switch (param) {
      case "And":
        if ((left instanceof False) || (right instanceof False)) {
          return new False();
        } else if ((left instanceof True) && (right instanceof True)) {
          return new True();
        } else if (left instanceof True) {
          return right;
        } else if (right instanceof True) {
          return left;
        } else {
          return new And(left, right);
        }
      case "Or":
        if ((left instanceof True) || (right instanceof True)) {
          return new True();
        } else if ((left instanceof False) && (right instanceof False)) {
          return new False();
        } else if ((left instanceof False)) {
          return right;
        } else if ((right instanceof False)) {
          return left;
        } else {
          return new Or(left, right);
        }
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Formula formula_constructor(String param, Formula formula, double offset) {
    switch (param) {
      case "Minus":
        if (formula instanceof True) {
          return formula;
        } else if (offset == 0) {
          return formula;
        } else if (offset == 1) {
          return new True();
        } else {
          return new Minus(formula, offset);
        }
      case "Plus":
        if (formula instanceof False) {
          return formula;
        } else if (offset == 0) {
          return formula;
        } else if (offset == 1) {
          return new False();
        } else {
          return new Plus(formula, offset);
        }
      default:
        throw new IllegalArgumentException();
    }
  }

  public static Formula formula_constructor(int first_index, int second_index, int iteration, int[] labels) {
    if ((iteration == 0) || (first_index == second_index)) {
      return new True();
    } else if (labels[first_index] != labels[second_index]) {
      return new Label(labels[first_index]);
    } else {
      return new Phi(first_index, second_index, iteration);
    }
  }
}
