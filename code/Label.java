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
	 * {@inheritDoc}
	 */
	@Override
	public Formula simplify() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSmaller(Formula other) {
		if (other instanceof Label) {
			return this.label == ((Label) other).label;
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toLaTeX() {
		return "\\ell_" + this.label;
	}

	/**
	 * Tests whether this formula is syntactically equivalent to the given object.
	 * 
	 * @param object an object
	 * @return true if this formula is syntactically equivalent to the given object,
	 * false otherwise.
	 */
	public boolean equals(Object object) {
		return object instanceof Label && this.label == ((Label) object).label;
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
