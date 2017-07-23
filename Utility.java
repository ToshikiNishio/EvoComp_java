import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by toshiki on 2017/06/24.
 */
public class Utility {
    /* Constructor */
    private Utility(){}

    /* Parameter setting */
    private static int MAX_FUNC_EVAL;
    private static int DIMENSION;
    private static int RUN_MAX;
    public static Sfmt Rnd;
    /* Parameter setting for PSO */
    public static double MAX_MAGNITUDE_VELOCITY = 0.2;
    /* Current parameter */
    public static int cur_func_eval;
    public static int cur_generation;

    public static String SRC_PATH = "/Users/toshiki/IdeaProjects/CEC2015/src";

    /* Set Parameter */
    public static void setMAX_FUNC_EVAL(int MAX_FUNC_EVAL){
        Utility.MAX_FUNC_EVAL = MAX_FUNC_EVAL;
    }
    public static void setDIMENSION(int DIMENSION) {
        Utility.DIMENSION = DIMENSION;
    }
    public static void setRUN_MAX(int RUN_MAX) {
        Utility.RUN_MAX = RUN_MAX;
    }

    /*Get Parameter*/
    public static int getDIMENSION() {return DIMENSION;}
    public static int getMAX_FUNC_EVAL(){
        return MAX_FUNC_EVAL;
    }
    public static int getRUN_MAX() {
        return RUN_MAX;
    }

    /* SIMD-oriented Fast Mersenne Twister */
    public static void setRandSeed(int seed){Utility.Rnd = new Sfmt(seed);}
    public static double rand(){return Rnd.NextUnif();}

    /* Utility method */
    public static double square(double x){return x * x;}
    public static double distance(Individual ind1, Individual ind2){
        double sum = 0;

        for (int dim = 0; dim < getDIMENSION(); dim++) {
            sum += square(ind1.positon[dim] - ind2.positon[dim]);
        }
        return Math.sqrt(sum);
    }

    public static void printLine(){
        System.out.println("--------------------------------------------------------------------------------------------");
    }
    public static void printindLine(int num){
        System.out.println(String.format("----- ind = " + "%05d" + " ---------------------------------------------------", num));
    }
    public static void printStarLine(){
        System.out.println("********************************************************************************************");
    }
    public static void printDevideGeneration(int curGen, int curEval ,int lbestInd, double lbestFit){
        System.out.println(String.format("***** Generation = " + "%7d" + " *** Evaluation = " + "%9d" + " *** lbestIndex = " + "%3d" +
                " **** lbestFitness = " + lbestFit + " *******************************************", curGen, curEval, lbestInd, lbestFit));
    }

    /* For output */
    public static double ave;
    public static double std;
    public static double min;
    public static double max;
    public static double meanGeneration = 0;
    public static double[] stack_hist_best_fit;

    public static void InitOutputPara(){
        ave = 0;
        std = 0;
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;
        stack_hist_best_fit = new double[Utility.getRUN_MAX()];
    }
    public static void calcOutputPara(){
        /* Calculate average */
        for (double fit : stack_hist_best_fit) {
            ave += fit;
        }
        ave /= RUN_MAX;
        /* Calculate Standerd deviation  */
        for (double fit : stack_hist_best_fit) {
            std += square(fit - ave);
        }
        std = Math.sqrt(std / RUN_MAX);
        /* Calculate Min and Max */
        for (double fit : stack_hist_best_fit) {
            if (fit < min)
                min = fit;
            if (fit > max)
                max = fit;
        }
    }
    public static void printOutputPara(ProblemUtil.ProblemID problemID){
        printStarLine();
        System.out.println(problemID.name());
        System.out.print("Average = ");
        printShortNum(ave);
        System.out.print("  Std = ");
        printShortNum(std);
        System.out.print("  Min = ");
        printShortNum(min);
        System.out.print("  Max = ");
        printShortNum(max);
        System.out.println();
        printStarLine();
    }
    public static void printShortNum(double num){
        double threshhold = 1.0;
        DecimalFormat zero = new DecimalFormat("0");
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat exp = new DecimalFormat("0.00E0");

        if (Math.abs(num) == 0)
            System.out.print(zero.format(num));
        else if (Math.abs(num) < threshhold)
            System.out.print(exp.format(num));
        else
            System.out.print(dec.format(num));
    }
    public static void printHourMinuteSecond(long milliSecond){
        long second = TimeUnit.MILLISECONDS.toSeconds(milliSecond);
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliSecond);
        long hour = TimeUnit.MILLISECONDS.toHours(milliSecond);

        System.out.printf("%02dh : %02dm : %02ds", hour, minute, second);
    }
    public static void printFinalBest(int run, SubSwarm sub){
        Utility.printLine();
        System.out.println("run = " + run);
        sub.printBestParticle();
        System.out.println();
        Utility.printLine();
    }
}

