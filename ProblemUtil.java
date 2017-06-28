/**
 * Created by toshiki on 2017/06/24.
 */
public class ProblemUtil {
    /* Constructor */
    private ProblemUtil() {}

    /* Member */
    private static double Prog_Lower;
    private static double Prog_Upper;
    private static ProblemID prob_obj;

    /* Setter and Getter*/
    private static void setProg_Lower(double prog_Lower) {ProblemUtil.Prog_Lower = prog_Lower;}
    public static double getProg_Lower() {return Prog_Lower;}
    private static void setProg_Upper(double prog_Upper) {ProblemUtil.Prog_Upper = prog_Upper;}
    public static double getProg_Upper() {return Prog_Upper;}

    private static void setProb_obj(ProblemID id){ProblemUtil.prob_obj = id;}
    public static double getFitness(double pos[]){return prob_obj.calc_fit(pos);}
    /* Set Bounds from problemID and we can use method getFitness() */
    public static void SetProblemID(ProblemID id){
        setProb_obj(id);
        setProg_Lower(id.lower);
        setProg_Upper(id.upper);
    }

    /* Definition of Problems*/
    public enum ProblemID {
        Sphere                  (-100.00, 100.00) {
            double calc_fit(double pos[]){
                double fit = 0;
                for (double dim_p: pos){
                    fit += Utility.square(dim_p);
                }
                return fit;
            }
        },
        Shwefel_P2_22           (-10.00,  10.00){
            double calc_fit(double pos[]) {
                double fit = 0;
                double tmp = 1;
                for (double dim_p : pos) {
                    fit += Math.abs(dim_p);
                    tmp *= Math.abs(dim_p);
                }
                fit += tmp;
                return fit;
            }
        };
//        Quadric                 (-100.00, 100.00),
//        Rosenbrock              (-10.00,  10.00),
//        Step                    (-100.00, 100.00),
//        Quadric_Noise           (-1.28,   1.28),
//        Schwefel                (-500.00, 500.00),
//        Rastrigin               (-5.12,   5.12),
//        Noncontinuous_Rastrigin (-5.12,   5.12),
//        Ackley                  (-32.00,  32.00),
//        Griewank                (-600.00, 600.00),
//        Generalized_Penalized   (-50.00,  50.00);
        /* CEC2015 */

        private final double lower;
        private final double upper;

        ProblemID(double lower, double upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public double getLower(){
            return  lower;
        }
        public double getUpper(){
            return upper;
        }
        abstract double calc_fit(double pos[]);


    }
}
