import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimalCouplingSolverTest {
    @Test
    public void test_solver_correctness() {
        int states = 100;
        int labels = 3;

        LabelledMarkovChain LMC = Main.getRandomInstance(states, labels);
        while (LMC.label[0] != LMC.label[1]) {
            LMC = Main.getRandomInstance(states, labels);
        }

        OptimalCouplingComputer solver_mcf = new OptimalCouplingComputer(0, 1, LMC.probability, LMC.distance);
        LinearProgrammingSolver solver_lp = new LinearProgrammingSolver(0, 1, LMC.probability, LMC.distance);

        double distance_mcf = solver_mcf.compute_distance();
        double distance_lp = solver_lp.compute_distance();

        assertEquals(distance_lp, distance_mcf, 1e-10);
    }
}