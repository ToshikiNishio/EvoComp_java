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
    public static ProblemID getProb_obj() { return prob_obj; }

    /* Set Bounds from problemID and we can use method calcFitness() */
    public static void setProblemID(ProblemID id){
        setProb_obj(id);
        setProg_Lower(id.lower);
        setProg_Upper(id.upper);
    }
    public static double calcFitness(double pos[]){return prob_obj.calc_fit(pos);}

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
        Schwefel_P2_22           (-10.00,  10.00){
            double calc_fit(double pos[]) {
                double fit = 0;
                double tmp1 = 0;
                double tmp2 = 1;
                for (double dim_p : pos) {
                    tmp1 += Math.abs(dim_p);
                    tmp2 *= Math.abs(dim_p);
                }
                fit += tmp1 + tmp2;
                return fit;
            }
        },
        Quadric                 (-100.00, 100.00){
            double calc_fit(double pos[]) {
                double fit = 0;
                double tmp;
                for (int i = 0; i < Utility.getDIMENSION(); i++) {
                    tmp = 0;
                    for (int j = 0; j < i ; j++) {
                        tmp += pos[j];
                    }
                    fit += Utility.square(tmp);
                }
                return fit;
            }
        },
        Rosenbrock              (-10.00,  10.00){
            double calc_fit(double pos[]){
                double fit = 0;
                for (int dim = 0; dim < Utility.getDIMENSION() - 1; dim++) {
                    fit += 100 * Utility.square(pos[dim+1] - Utility.square(pos[dim])) + Utility.square(pos[dim] - 1);
                }
                return  fit;
            }
        },
        Step                    (-100.00, 100.00){
            double calc_fit(double pos[]){
                double fit = 0;
                for (double dim_p : pos) {
                    fit += Utility.square(Math.floor(dim_p + 0.5));
                }
                return fit;
            }
        },
        Quadric_Noise           (-1.28,   1.28){
            double calc_fit(double pos[]){
                double fit = 0;
                for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                    fit += (dim + 1) * Math.pow(pos[dim],4);
                }
                fit += Utility.rand();
                return fit;
            }
        },
        Schwefel                (-500.00, 500.00){
            double calc_fit(double pos[]){
                double fit = 0;
                for (double dim_p : pos) {
                    fit -= dim_p * Math.sin(Math.sqrt(Math.abs(dim_p)));
                }
                return fit;
            }
        },
        Rastrigin               (-5.12,   5.12){
            double calc_fit(double pos[]){
                double fit = 10 * Utility.getDIMENSION();
                for (double dim_p : pos) {
                    fit += Utility.square(dim_p) - 10 * Math.cos(2 * Math.PI * dim_p);
                }
                return fit;
            }
        },
        Noncontinuous_Rastrigin (-5.12,   5.12){
            double calc_fit(double pos[]){
                double y = 0;
                double fit = 10 * Utility.getDIMENSION();
                for (double dim_p : pos) {
                    if (Math.abs(dim_p) < 0.5)
                        y = dim_p;
                    if (Math.abs(dim_p) >= 0.5)
                        y = Math.round(2 * dim_p) / 2.0;
                    fit += Utility.square(y) - 10 * Math.cos(2 * Math.PI * y);
                }
                return fit;
            }
        },
        Ackley                  (-32.00,  32.00){
            double calc_fit(double pos[]){
                double fit = 0;
                double tmp1 = 0;
                double tmp2 = 0;
                double D = Utility.getDIMENSION();

                for (double dim_p : pos) {
                    tmp1 += Utility.square(dim_p);
                    tmp2 += Math.cos(2 * Math.PI * dim_p);
                }
                fit += -20 * Math.exp(-0.2 * Math.sqrt(tmp1 / D))
                        - Math.exp(tmp2 / D)
                        + 20 + Math.exp(1);
                return fit;
            }
        },
        Griewank                (-600.00, 600.00){
            double calc_fit(double pos[]){
                double fit = 0;
                double tmp1 = 0;
                double tmp2 = 1;

                for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                    tmp1 += Utility.square(pos[dim]);
                    tmp2 *= Math.cos(pos[dim] / Math.sqrt(dim + 1));
                }
                fit += (1.0 / 4000) * tmp1 - tmp2 + 1;
                return fit;
            }
        },
        Generalized_Penalized   (-50.00,  50.00){
            double calc_fit(double pos[]){
                double fit = 0;
                double tmp1 = 0;
                double tmp2 = 0;
                double[] y = new double[Utility.getDIMENSION()];

                for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                    y[dim] = 1 + (1.0 / 4) * (pos[dim] + 1);
                }
                for (int dim = 0; dim < Utility.getDIMENSION() - 1; dim++) {
                    tmp1 += Utility.square(y[dim] - 1) * (1 + 10 * Utility.square(Math.sin(Math.PI * y[dim+1])));
                }
                double a = 10;
                double k = 100;
                double m = 4;
                for (double dim_p : pos) {
                    if (dim_p > a)
                        tmp2 += k * Math.pow(k * (dim_p - a), m);
                    if (Math.abs(dim_p) < a)
                        tmp2 += 0;
                    if (dim_p < -a)
                        tmp2 += k * Math.pow(-dim_p - a, m);
                }
                int D = Utility.getDIMENSION();
                fit += Math.PI / D
                        * (10 * Utility.square(Math.sin(Math.PI * y[0]))
                            + tmp1 + Utility.square(y[D-1] - 1))
                        + tmp2;
                return fit;
            }
        },
        CEC2015_Rotated_High_Conditioned_Elliptic   (- 100.00, 100.00){
            double calc_fit(double pos[]){
                testfunc tf = new testfunc();
                double fit[] = new double[1];
                try {
                    tf.test_func(pos, fit, Utility.getDIMENSION(), 1, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return fit[0];
            }

        };
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
