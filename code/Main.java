import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static LabelledMarkovChain getRandomInstance(int states, int number_of_labels) {
        double p = 2 * Math.log(states) / states; //Erdos-Renyi threshold
        Random r = new Random();

        //Distribution generation parameters
        int max_int = 10;
        int min_int = 1;

        int[] labels = new int[states];
        for (int i = 0; i < states; i++) {
            labels[i] = r.nextInt(number_of_labels);
        }

        double[][] probabilities = new double[states][states];
        for (int i = 0; i < states; i++) {
            double sum = 0;
            for (int j = 0; j < states; j++) {
                if (r.nextDouble() <= p) {
                    probabilities[i][j] += r.nextInt((max_int - min_int) + 1) + min_int;
                    sum += probabilities[i][j];
                }
            }
            for (int j = 0; j < states; j++) {
                if (probabilities[i][j] > 0) {
                    probabilities[i][j] = probabilities[i][j] / sum;
                }
            }
        }

        double[][] distances = new double[states][states];
        for (int i = 0; i < states; i++) {
            for (int j = 0; j < states; j++) {
                if (labels[i] != labels[j]) {
                    distances[i][j] = 1.0;
                }
            }
        }

        return new LabelledMarkovChain(labels, probabilities, distances);
    }

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

        OptimalCouplingComputer solver_mcf = new OptimalCouplingComputer(0, 1, probability, distance);
        LinearProgrammingSolver solver_lp = new LinearProgrammingSolver(0, 1, probability, distance);

        double distance_mcf = solver_mcf.compute_distance();
        double distance_lp = solver_lp.compute_distance();

        System.out.println("The MCF solver says the distance is: " + distance_mcf);
        System.out.println("The LP solver says the distance is: " + distance_lp);
    }

}
