import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimalCouplingSolverTest {
    @Test
    public void test_solver_correctness() throws IOException {
        int states = 8;
        int labels = 3;

        LabelledMarkovChain LMC = Main.getRandomInstance(states, labels);
        while (LMC.label[0] != LMC.label[1]) {
            LMC = Main.getRandomInstance(states, labels);
        }

        FileWriter writer = new FileWriter("diagnostic_file.txt");
        writer.write(LMC.toString());
        writer.close();


        OptimalCouplingComputer solver_mcf = new OptimalCouplingComputer(0, 1, LMC.probability, LMC.distance);
        LinearProgrammingSolver solver_lp = new LinearProgrammingSolver(0, 1, LMC.probability, LMC.distance);

        double distance_mcf = solver_mcf.compute_distance();
        double distance_lp = solver_lp.compute_distance();

        assertEquals(distance_lp, distance_mcf, 1e-10);
    }
}