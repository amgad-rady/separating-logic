import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		// read the transitions
		Scanner input = new Scanner(new File("sample.tra"));
		int states = input.nextInt();
		int transitions = input.nextInt();
		double[][] probability = new double[states][states];
		for (int t = 0; t < transitions; t++) {
			int source = input.nextInt();
			int target = input.nextInt();
			probability[source][target] = input.nextDouble();
		}
		input.close();

		// read the labels
		input = new Scanner(new File("sample.lab"));
		int[] label = new int[states];
		for (int s = 0; s < states; s++) {
			label[s] = input.nextInt();
		}
		input.close();

		// read the transitions
		input = new Scanner(new File("sample.dis"));
		double[][] distance = new double[states][states];
		for (int s = 0; s < states; s++) {
			for (int t = 0; t < states; t++) {
				if (s == t) {
					distance[s][t] = 0.0;
				} else if (s < t) {
					distance[s][t] = input.nextDouble();
				} else {
					distance[s][t] = distance[t][s];
				}
			}
		}
		input.close();

		//LabelledMarkovChain chain = new LabelledMarkovChain(label, probability, distance);
		//System.out.println(chain);

		OptimalCouplingComputer calculator = new OptimalCouplingComputer(0, 1, probability, distance);
		System.out.println("The cycle cancelling algorithm says Delta(d) is:");
		System.out.println(calculator.compute_distance());

		LinearProgrammingSolver test_calculator = new LinearProgrammingSolver(0, 1, probability, distance);
		System.out.println("The MCF LP solver says Delta(d) is:");
		System.out.println(test_calculator.compute_distance());
	}

}
