/*
 * Copyright (C)  2020  Zainab Fatmi
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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the probabilistic bisimilarity decision algorithm from the paper "Optimal
 * State-Space Lumping in Markov Chains" by Salem Derisavi, Holger Hermanns, and William Sanders.
 * 
 * @author Zainab Fatmi
 */
public class ProbabilisticBisimilarity {
	private LinkedList<Block> partition;
	private int numberOfStates;
	private int numberOfBlocks;
	private double epsilon; // to compare probabilities

	/**
	 * Initializes the decision procedure of decide probabilistic bisimilarity for the given labelled Markov chain.
	 * 
	 * @param transition the transition matrix of the labelled Markov chain
	 * @param label the labelling function of the labelled Markov chain
	 * @param precision the precision of the probabilities in the transition matrix
	 */
	public ProbabilisticBisimilarity(double[][] transition, int[] label, int precision) {
		this.partition = new LinkedList<Block>();
		this.numberOfStates = transition.length;
		this.numberOfBlocks = 0;
		this.epsilon = Math.pow(10.0, -precision) / 2.0;
		
		// determine the number of different labels
		Set<Integer> labels = new HashSet<Integer>();
		for (int l : label) {
			labels.add(l);
		}
		int numberOfLabels = labels.size();
		
		// create an empty block for each label
		for (int i = 0; i < numberOfLabels; i++) {
			new Block();
		}
		
		// add the states to the blocks corresponding to the label of the state 
		State[] idToState = new State[this.numberOfStates]; // map id to State
		for (int id = 0; id < this.numberOfStates; id++) {
			State state = new State(id);
			idToState[id] = state;
			Block block = partition.get(label[id]);
			block.elements.add(state);
			state.block = block;
		}
		for (int source = 0; source < this.numberOfStates; source++) {
			for (int target = 0; target < this.numberOfStates; target++) {
				if (transition[source][target] != 0.0) {
					idToState[target].predecessors.put(idToState[source], transition[source][target]);
				}
			}
		}
	}

	/**
	 * A class to represent the nodes of a splay tree.  Each node of the tree stores a block and its probability
	 * of transitioning to the current splitter.
	 */
	private class Node {
		private Block block;
		private double probability;
		private Node parent;
		private Node left; // left child
		private Node right; // right child

		/**
		 * Initializes this node with an empty black, the given probability and the given parent node.
		 * 
		 * @param probability the probability of the states in the block of this node transitioning to the 
		 * current splitter
		 * @param parent the parent node
		 */
		public Node(double probability, Node parent) {
			this.block = new Block();
			this.probability = probability;
			this.parent = parent;
			this.left = null;
			this.right = null;
		}
	}

	/**
	 * A splay tree.  Each node of the tree stores a block and its probability of transitioning to the current splitter.
	 */
	private class SplayTree {
		private Node root;

		/**
		 * Initializes this splay tree as empty.
		 */
		public SplayTree() {
			this.root = null;
		}

		/**
		 * Inserts the state in its appropriate position in this splay tree. If the probability
		 * exists in the splay tree, adds the state to the block associated to the probability,
		 * otherwise creates a new node in this splay tree.
		 * 
		 * @param probability the probability of the state transitioning to the current splitter
		 * @param state a state
		 */
		public void insert(double probability, State state) {
			Node x = this.root;
			Node y = null; // the parent of x
			while (x != null && (Math.abs(probability - x.probability) >= epsilon)) {
				y = x;
				if (probability < x.probability) {
					x = x.left;
				} else {
					x = x.right;
				}
			}
			if (x == null) {
				Node node = new Node(probability, y);
				if (y == null) {
					root = node;
				} else if (probability < y.probability) {
					y.left = node;
				} else {
					y.right = node;
				}
				node.block.elements.add(state);
				state.block = node.block;
				splay(node);
			} else {
				x.block.elements.add(state);
				state.block = x.block;
				splay(x);
			}
		}

		/**
		 * Moves the given node to the root of the splay tree.
		 * 
		 * @param x a node
		 */
		private void splay(Node x) {
			while (x.parent != null) {
				if (x.parent.parent == null) {
					if (x == x.parent.left) {
						// zig rotation
						this.rotateRight(x.parent);
					} else {
						// zag rotation
						this.rotateLeft(x.parent);
					}
				} else if (x == x.parent.left && x.parent == x.parent.parent.left) {
					// zig-zig rotation
					this.rotateRight(x.parent.parent);
					this.rotateRight(x.parent);
				} else if (x == x.parent.right && x.parent == x.parent.parent.right) {
					// zag-zag rotation
					this.rotateLeft(x.parent.parent);
					this.rotateLeft(x.parent);
				} else if (x == x.parent.right && x.parent == x.parent.parent.left) {
					// zig-zag rotation
					this.rotateLeft(x.parent);
					this.rotateRight(x.parent);
				} else {
					// zag-zig rotation
					this.rotateRight(x.parent);
					this.rotateLeft(x.parent);
				}
			}
		}

		/**
		 * Rotates left at the given node.
		 * 
		 * @param x a node
		 */
		private void rotateLeft(Node x) {
			Node y = x.right;
			x.right = y.left;
			if (x.right != null) {
				x.right.parent = x;
			}
			y.parent = x.parent;
			if (x.parent == null) {
				this.root = y;
			} else if (x == x.parent.left) {
				x.parent.left = y;
			} else {
				x.parent.right = y;
			}
			y.left = x;
			x.parent = y;
		}

		/**
		 * Rotates right at the given node.
		 * 
		 * @param x a node
		 */
		private void rotateRight(Node x) {
			Node y = x.left;
			x.left = y.right;
			if (x.left != null) {
				x.left.parent = x;
			}
			y.parent = x.parent;
			if (x.parent == null) {
				this.root = y;
			} else if (x == x.parent.right) {
				x.parent.right = y;
			} else {
				x.parent.left = y;
			}
			y.right = x;
			x.parent = y;
		}
	}

	/**
	 * A class to represent the blocks of the partition.
	 */
	private class Block {
		private int id; // for easier hashCode and equals methods
		private LinkedList<State> elements;
		private SplayTree tree;

		/**
		 * Initializes this block as empty and adds it to the partition.
		 */
		public Block() {
			this.id = numberOfBlocks++;
			this.elements = new LinkedList<State>();
			this.tree = new SplayTree();
			partition.add(this);
		}

		@Override
		public int hashCode() {
			return this.id;
		}

		@Override
		public boolean equals(Object object) {
			if (this != null && this.getClass() == object.getClass()) {
				Block other = (Block) object;
				return this.id == other.id;
			} else {
				return false;
			}
		}
	}

	/**
	 * A class to represent the states of the labelled Markov chain.
	 */
	private class State {
		private int id;
		private Block block; // needed by the splay tree
		private double sum;
		private LinkedHashMap<State, Double> predecessors; // no need for successors

		/**
		 * Initializes this state with the given index.
		 * 
		 * @param id the non-negative ID of the state
		 */
		public State(int id) {
			this.id = id;
			this.sum = 0;
			this.predecessors = new LinkedHashMap<State, Double>();
		}

		@Override
		public int hashCode() {
			return this.id;
		}

		@Override
		public boolean equals(Object object) {
			if (this != null && this.getClass() == object.getClass()) {
				Block other = (Block) object;
				return this.id == other.id;
			} else {
				return false;
			}
		}
	}

	/**
	 * Decides probabilistic bisimilarity for the labelled Markov chain.
	 * 
	 * @return the probabilistic bisimilarity partition of the states of the labelled Markov chain
	 */
	public Set<Set<Integer>> decide() {
		LinkedList<Block> potentialSplitters = new LinkedList<Block>(this.partition); // potential splitters
		Set<State> predecessors = new HashSet<State>(); // states that have a transition to the current splitter
		LinkedList<Block> partitioned = new LinkedList<Block>(); // blocks which will be partitioned

		while (!potentialSplitters.isEmpty()) {
			Block splitter = potentialSplitters.pop();

			predecessors.clear();
			for (State state : splitter.elements) {
				for (State predecessor : state.predecessors.keySet()) {
					predecessor.sum = 0;
				}
			}
			for (State state : splitter.elements) {
				for (Map.Entry<State, Double> entry : state.predecessors.entrySet()) {
					State predecessor = entry.getKey();
					predecessor.sum += entry.getValue();
					predecessors.add(predecessor);
				}
			}
			
			partitioned.clear();
			for (State state : predecessors) {
				Block block = state.block;
				block.elements.remove(state);
				block.tree.insert(state.sum, state);
				if (!partitioned.contains(block)) {
					partitioned.add(block);
				}
			}
			
			for (Block block : partitioned) {
				// traverse the subblock tree, adding subblocks and keeping track of the maximum
				Block max = block;
				LinkedList<Node> queue = new LinkedList<Node>();
				queue.add(block.tree.root);
				while (!queue.isEmpty()) {
					Node node = queue.removeFirst();
					if (node.left != null) {
						queue.add(node.left);
					}
					if (node.right != null) {
						queue.add(node.right);
					}
					if (node.block.elements.size() > max.elements.size()) {
						max = node.block;
					}
					potentialSplitters.add(node.block);
				}
				
				if (!potentialSplitters.contains(block) && !(max == block)) {
					potentialSplitters.add(block);
					potentialSplitters.remove(max);
				}

				if (block.elements.isEmpty()) {
					this.partition.remove(block);
					potentialSplitters.remove(block);
				} else {
					block.tree.root = null; // reset the splay tree
				}
			}
		}
		
		Set<Set<Integer>> result = new HashSet<Set<Integer>>();
		for (Block block : this.partition) {
			Set<Integer> set = new HashSet<Integer>();
			for (State state : block.elements) {
				set.add(state.id);
			}
			result.add(set);
		}
		return result;
	}
}