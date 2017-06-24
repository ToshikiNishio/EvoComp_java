/**
 * Created by toshiki on 2017/06/24.
 */
public class ProblemUtil {
    /* Constructor */
    private ProblemUtil() {}

    public enum Basic_ProblemID {
        Sphere                  (-100.00, 100.00),
        Shwefel_P2_22           (-10.00,  10.00),
        Quadric                 (-100.00, 100.00),
        Rosenbrock              (-10.00,  10.00),
        Step                    (-100.00, 100.00),
        Quadric_Noise           (-1.28,   1.28),
        Schwefel                (-500.00, 500.00),
        Rastrigin               (-5.12,   5.12),
        Noncontinuous_Rastrigin (-5.12,   5.12),
        Ackley                  (-32.00,  32.00),
        Griewank                (-600.00, 600.00),
        Generalized_Penalized   (-50.00,  50.00);

        private final double lower;
        private final double upper;

        Basic_ProblemID(double lower, double upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public double getLower(){
            return  lower;
        }
        public double getUpper(){
            return upper;
        }
    }
}
