import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LabelledMarkovChainTest {
    private LabelledMarkovChainTest() {}

    private static int states; // number of states of the labelled Markov chain
    private static double[][] probability; // transition probabilities of the labelled Markov chain
    private static int[] label; // state labelling of the labelled Markov chain
    private static double[][] distance; // probabilistic bisimilarity distances of states (only distance[s][t] with s < t are used)

    public static void main(String[] args) {
	try {
	    // read the transition probabilities
	    readProbabilities(args[0]);

	    // read the state labelling
	    readLabels(args[1]);

	    // read the distances
	    readDistances(args[2]);

	    //  
	    LabelledMarkovChain chain = new LabelledMarkovChain(probability, label, distance);
	    double[] solution = chain.getOptimalFunction(0, 1);
	    for (int s = 0; s < states; s++) {
		System.out.printf("f[%d] = %.3f%n", s, solution[s]);
	    }
	} catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Command line argument(s) missing.");
	} catch (FileNotFoundException e) {
	    System.out.println(e.getMessage());
	} catch (InputMismatchException e) {
	    System.out.println(e.getMessage());
	} catch (NoSuchElementException e) {
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Reads the transition probabilities from the given file.
     *
     * @param file the name of the file
     * @throws FileNotFoundException if the given file cannot be read
     * @throws InputMismatchException if the given file contains not the right type of data
     * @throws NoSuchElementException if the given file contains not enough data
     */
    private static void readProbabilities(String file) throws FileNotFoundException, InputMismatchException, NoSuchElementException {
	try {
	    Scanner input = new Scanner(new File(file));

	    states = input.nextInt(); // number of states
	    int transitions = input.nextInt(); // number of transitions
	    
	    probability = new double[states][states]; // transition probability
	    for (int t = 0; t < transitions; t++) {
		int source = input.nextInt();
		int target = input.nextInt();
		probability[source][target] = input.nextDouble();
	    }

	    input.close();

	} catch (FileNotFoundException e) {
	    throw new FileNotFoundException("File " + file + " is not found.");
	} catch (InputMismatchException e) {
	    throw new InputMismatchException("File " + file + " contains not the right type of data.");
	} catch (NoSuchElementException e) {
	    throw new NoSuchElementException("File " + file + " contains not enough data.");
	}
    }

    /**
     * Reads the labelling from the given file.
     *
     * @param file the name of the file
     * @throws FileNotFoundException if the given file cannot be read
     * @throws InputMismatchException if the given file contains not the right type of data
     * @throws NoSuchElementException if the given file contains not enough data
     */
    private static void readLabels(String file) throws FileNotFoundException, InputMismatchException, NoSuchElementException {
	try {
	    Scanner input = new Scanner(new File(file));

	    label = new int[states]; // state labelling
	    for (int s = 0; s < states; s++) {
		label[s] = input.nextInt();
	    }

	    input.close();

	} catch (FileNotFoundException e) {
	    throw new FileNotFoundException("File " + file + " is not found.");
	} catch (InputMismatchException e) {
	    throw new InputMismatchException("File " + file + " contains not the right type of data.");
	} catch (NoSuchElementException e) {
	    throw new NoSuchElementException("File " + file + " contains not enough data.");
	}
    }

    /**
     * Reads the distances from the given file.
     *
     * @param file the name of the file
     * @throws FileNotFoundException if the given file cannot be read
     * @throws InputMismatchException if the given file contains not the right type of data
     * @throws NoSuchElementException if the given file contains not enough data
     */
    private static void readDistances(String file) throws FileNotFoundException, InputMismatchException, NoSuchElementException {
	try {
	    Scanner input = new Scanner(new File(file));

	    distance = new double[states][states]; // distances
	    for (int s = 0; s < states; s++) {
		for (int t = s + 1; t < states; t++) {
		    distance[s][t] = input.nextDouble();
		    distance[t][s] = distance[s][t];
		}
	    }

	    input.close();

	} catch (FileNotFoundException e) {
	    throw new FileNotFoundException("File " + file + " is not found.");
	} catch (InputMismatchException e) {
	    throw new InputMismatchException("File " + file + " contains not the right type of data.");
	} catch (NoSuchElementException e) {
	    throw new NoSuchElementException("File " + file + " contains not enough data.");
	}
    }
}
