import org.junit.jupiter.api.Test;

class LabelledMarkovChainTest {

	@Test
	public void test() {
		int states = 5;
		double[][] probability = new double[states][states];
		probability[0][0] = 0.33;
		probability[0][2] = 0.33;
		probability[0][3] = 0.33;
		probability[1][1] = 0.5;
		probability[1][3] = 0.25;
		probability[1][4] = 0.25;
		probability[2][2] = 1.0;
		probability[3][3] = 1.0;
		probability[4][4] = 1.0;
		int[] label = new int[states];
		label[2] = 1;
		label[3] = 2;
		label[4] = 3;
		double[][] distance = { { 0.0, 7.0 / 8.0, 1.0, 1.0, 1.0 },
					{ 7.0 / 8.0, 0.0, 1.0, 1.0, 1.0 },
					{ 1.0, 1.0, 0.0, 1.0, 1.0 },
					{ 1.0, 1.0, 1.0, 0.0, 1.0 },
					{ 1.0, 1.0, 1.0, 1.0, 0.0 } };	

		LabelledMarkovChain chain = new LabelledMarkovChain(probability, label, distance);
		double[] solution = chain.getOptimalFunction(0, 1);
		for (int s = 0; s < states; s++) {
		    System.out.printf("f[%d] = %.3f%n", s, solution[s]);
		}
	}

}
