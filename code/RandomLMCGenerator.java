import java.util.Random;

public class RandomLMCGenerator {
    final int[] labels;
    final double[][] probabilities;
    final double[][] distances;
    final private int states;

    public RandomLMCGenerator(int states, int label_reduction_factor, double p) {
        assert p < 1;
        assert p > 0;

        Random r = new Random();
        this.states = states;

        int number_labels = 1 + r.nextInt(states / label_reduction_factor);

        int[] labels = new int[states];
        for (int i = 0; i < states; i++) {
            labels[i] = r.nextInt(number_labels);
        }
        this.labels = labels;

        double[][] probabilities = new double[states][states];
        for (int i = 0; i < states; i++) {
            for (int j = 0; j < states; j++) {
                if (r.nextDouble() <= p) {
                    probabilities[i][j] = 1 + r.nextInt();
                }
            }
        }
        for (int i = 0; i < states; i++) {
            double sum = 0;
            for (int j = 0; j < states; j++) {
                sum += probabilities[i][j];
            }
            while (sum == 0) {
                for (int j = 0; j < states; j++) {
                    if (r.nextDouble() <= p) {
                        probabilities[i][j] = 1 + r.nextInt();
                        sum += probabilities[i][j];
                    }
                }
            }
            for (int j = 0; j < states; j++) {
                if (probabilities[i][j] != 0) {
                    probabilities[i][j] = probabilities[i][j] / sum;
                }
            }
        }

        this.probabilities = probabilities;

        double[][] distances = new double[states][states];
        for (int s = 0; s < states; s++) {
            for (int t = 0; t < states; t++) {
                if (labels[s] != labels[t]) {
                    distances[s][t] = 1.0;
                }
            }
        }
        this.distances = distances;
    }
}
