import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimalCouplingSolverTest {
  @Test
  public void test_solver_correctness() throws IOException {
    int states = 2500;
    int labels = 50;

    LabelledMarkovChain LMC = Main.getRandomInstance(states, labels);
    while (LMC.label[0] != LMC.label[1]) {
      LMC = Main.getRandomInstance(states, labels);
    }

/*        FileWriter writer = new FileWriter("diagnostic_file.txt");
        writer.write(LMC.toString());
        writer.close();*/


    OptimalCouplingComputer solver_mcf = new OptimalCouplingComputer(0, 1, LMC.probability, LMC.distance);
    LinearProgrammingSolver solver_lp = new LinearProgrammingSolver(0, 1, LMC.probability, LMC.distance);

    double distance_mcf = solver_mcf.compute_distance();
    double distance_lp = solver_lp.compute_distance();

    assertEquals(distance_lp, distance_mcf, 1e-10);
  }

  @Test
  public void test_solver_running_time() {
    final int NumIterations = 200;
    final int NumStates = 2500;
    final int NumLabels = 50;

    DescriptiveStatistics trial_mcf = new DescriptiveStatistics();
    DescriptiveStatistics trial_lp = new DescriptiveStatistics();

    for (int i = 0; i < NumIterations; i++) {
      LabelledMarkovChain test_chain = Main.getRandomInstance(NumStates, NumLabels);

      DescriptiveStatistics instance_mcf = new DescriptiveStatistics();
      DescriptiveStatistics instance_lp = new DescriptiveStatistics();

      for (int j = 0; j < 10; j++) {
        System.gc();
        double start_time_mcf = System.nanoTime();
        OptimalCouplingComputer mcf_solver = new OptimalCouplingComputer(0, 1, test_chain.probability,
          test_chain.distance);
        mcf_solver.compute_distance();
        double end_time_mcf = System.nanoTime();

        System.gc();
        double start_time_lp = System.nanoTime();
        LinearProgrammingSolver lp_solver = new LinearProgrammingSolver(0, 1, test_chain.probability,
          test_chain.distance);
        lp_solver.compute_distance();
        double end_time_lp = System.nanoTime();

        if (j > 2) {
          instance_mcf.addValue(end_time_mcf - start_time_mcf);
          instance_lp.addValue(end_time_lp - start_time_lp);
        }
      }
      trial_mcf.addValue(instance_mcf.getMean());
      trial_lp.addValue(instance_lp.getMean());
    }
    System.out.println("The MCF mean is: " + trial_mcf.getMean());
    System.out.println("The MCF std is: " + trial_mcf.getStandardDeviation());
    System.out.println("The LP mean is: " + trial_lp.getMean());
    System.out.println("The LP std is: " + trial_lp.getStandardDeviation());
  }
}