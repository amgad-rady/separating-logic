package explainability;

import java.util.Set;
import java.util.TreeSet;

public class Shifts {
	private static final Set<Double> shifts = new TreeSet<Double>();
	
	public static void find(Formula formula) {
		if (formula instanceof Minus) {
			Minus minus = (Minus) formula;
			shifts.add(minus.getShift());
			find(minus.getFormula());
		} else if (formula instanceof Plus) {
			Plus plus = (Plus) formula;
			shifts.add(plus.getShift());
			find(plus.getFormula());
		} else if (formula instanceof Next) {
			Next next = (Next) formula;
			find(next.getFormula());
		} else if (formula instanceof Or) {
			Or or = (Or) formula;
			for (Formula subformula : or.getSubformulas()) {
				find(subformula);
			}
		}
		else if (formula instanceof And) {
			And and = (And) formula;
			for (Formula subformula : and.getSubformulas()) {
				find(subformula);
			}
		}
	}
	
	public static void print() {
		for (Double shift : shifts) {
			System.out.println(shift);
		}
	}
}
