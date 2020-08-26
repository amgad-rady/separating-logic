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

public class Compare {
	/**
	 * Tests whether smaller is smaller than bigger.  Both smaller and bigger are assumed to be simplified and different from null.
	 * If the method returns true, then smaller is smaller than bigger.  Even if the method returns false, then smaller may be smaller 
	 * than bigger.
	 * 
	 * @param smaller a simplified formula
	 * @param bigger a simplified formula
	 * @return approximation of whether smaller is smaller than bigger
	 */
	public static boolean isSmaller(Formula smaller, Formula bigger) {
		if (smaller instanceof True) {
			return true; 
		} 
		if (bigger instanceof False) {
			return true; 
		} 
		if (smaller instanceof Next && bigger instanceof Next) {
			Formula smallerSub = ((Next) smaller).getFormula();
			Formula biggerSub = ((Next) bigger).getFormula();
			if (isSmaller(smallerSub, biggerSub)) {
				return true; 
			}
		} 
		if (smaller instanceof Minus) {
			double smallerShift = ((Minus) smaller).getShift();
			Formula smallerSub = ((Minus) smaller).getFormula();
			if (bigger instanceof Minus) {
				double biggerShift = ((Minus) bigger).getShift();
				Formula biggerSub = ((Minus) bigger).getFormula();
				if (isSmaller(smallerSub, biggerSub) && smallerShift >= biggerShift) {
					return true; 
				}
			} 
			if (bigger instanceof Plus) {
				double biggerShift = ((Plus) bigger).getShift();
				Formula biggerSub = ((Plus) bigger).getFormula();
				if (biggerSub instanceof True && smallerShift >= 1 - biggerShift) {
					return true; 
				}
			}
			if (isSmaller(smallerSub, bigger) || smallerSub.equals(bigger)) {
				return true; 
			}
		} 
		if (bigger instanceof Plus) {
			double plusShift = ((Plus) bigger).getShift();
			Formula biggerSub = ((Plus) bigger).getFormula();
			if (smaller instanceof Plus) {
				double smallerShift = ((Plus) smaller).getShift();
				Formula smallerSub = ((Plus) smaller).getFormula();
				if (isSmaller(smallerSub, biggerSub) && smallerShift <= plusShift) {
					return true; 
				}
			}
			if (biggerSub instanceof Minus) {
				double minusShift = ((Minus) biggerSub).getShift();
				Formula biggerSubSub = ((Minus) biggerSub).getFormula();
				if (isSmaller(smaller, biggerSubSub) && minusShift <= plusShift) {
					return true; 
				}
			}
			if (isSmaller(smaller, biggerSub)) {
				return true; 
			}
		}
		if (smaller instanceof And && bigger instanceof And) {
			boolean forall = true; // for all subformulas smallerSub of smaller exists a subformula biggerSub of bigger such that smallerSub is smaller than or equal to biggerSub
			for (Formula smallerSub : ((And) smaller).getSubformulas()) {
				boolean exists = false; // exists a subformula biggerSub of bigger such that smallerSub is smaller than or equal to biggerSub
				for (Formula biggerSub : ((And) bigger).getSubformulas()) {
					exists = exists || isSmaller(smallerSub, biggerSub);
				}
				forall = forall && exists;
			}
			if (forall) {
				return true; 
			}
		}
		if (smaller instanceof Or && bigger instanceof Or) {
			boolean exists = false; // there exists a subformula smallerSub of smaller for all subformulas biggerSub of bigger such that smallerSub is smaller than or equal to biggerSub
			for (Formula smallerSub : ((Or) smaller).getSubformulas()) {
				boolean forall = true; // for all subformulas biggerSub of bigger such that smallerSub is smaller than or equal to biggerSub
				for (Formula biggerSub : ((Or) bigger).getSubformulas()) {
					forall = forall && isSmaller(smallerSub, biggerSub);
				}
				exists = exists || forall;
			}
			if (exists) {
				return true; 
			}
		}
		return false;
	}
}
