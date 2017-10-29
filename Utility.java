import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

/**
 * Created by toshiki on 2017/06/24.
 */
public class Utility {
    /* Constructor */
    private Utility(){}

    /* Definition of Parameters */
    private static int MAX_FUNC_EVAL;
    private static int DIMENSION;
    private static int RUN_MAX;
    /* Parameter setting for PSO */
    public static double MAX_MAGNITUDE_VELOCITY = 0.2;
    /* Current parameter */
    public static int cur_func_eval;
    public static int cur_generation;
    /* Path */
    public static String SRC_PATH = "/Users/toshiki/IdeaProjects/EvoComp_java/src";
    public static String OUTOUT_PATH = "/Users/toshiki/Output/";
    /* Definition */
    public static Sfmt Rnd;
    public static String date;
    public static void getDate(){
        Date d = new Date();
        SimpleDateFormat d1 = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
        Utility.date = d1.format(d);
        System.out.println(Utility.date);
    }
    public static boolean csvOutputFlg = false;
    public static boolean outputParameterFlg = false;
    public static boolean outputScatterFlg = false;
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
    public static void setCsvOutputFlg(boolean flg){Utility.csvOutputFlg = flg;}
    public static void setOuputParameterFlg(boolean flg){Utility.outputParameterFlg = flg;}
    public static void setOutputScatterFlg(boolean flg){Utility.outputScatterFlg = flg;}

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
    public static String returnStarLine(){
        return "********************************************************************************************";
    }
    public static void printStarLine(){
        System.out.println(returnStarLine());
    }
    public static void printDevideGeneration(int curGen, int curEval ,int lbestInd, double lbestFit){
        System.out.println(String.format("***** Generation = " + "%7d" + " *** Evaluation = " + "%9d" + " *** lbestIndex = " + "%3d" +
                " **** lbestFitness = " + lbestFit + " *******************************************", curGen, curEval, lbestInd, lbestFit));
    }

    /* For output */
    private static double ave;
    private static double std;
    private static double min;
    private static double max;
    public static double[] stack_hist_best_fit;
    private static long start_time;
    private static long end_time;
    private static long run_time;



    public static void InitOutputPara(){
        ave = 0;
        std = 0;
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;
        stack_hist_best_fit = new double[Utility.getRUN_MAX()];
        start_time = System.currentTimeMillis();
    }
    public static void calcOutputPara(){
        /* Calculate run time */
        end_time = System.currentTimeMillis();
        run_time = end_time - start_time;
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
    public static void printResult(ProblemUtil.ProblemID problemID, String algName){
        calcOutputPara();

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
        printHourMinuteSecond(run_time);
        System.out.println();
        printStarLine();
        printResultTxt(problemID, algName);
        printResultCsv(problemID, algName);
    }

    public static void printResultTxt(ProblemUtil.ProblemID problemID, String algName){
        String folderName;
        folderName = OUTOUT_PATH + Utility.date + "/" + algName;
        File newfile = new File(folderName);
        newfile.mkdirs();
        try {
            FileWriter fw = new FileWriter(folderName + "/ExperimentalResult.txt", true); //追記モード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.println();
            pw.print("*******************************************************************");
            pw.println();
            pw.print(problemID.name());
            pw.println();
            pw.print("Average = " + returnShortNum(ave));
            pw.print("  Std = "  + returnShortNum(std));
            pw.print("  Min = " + returnShortNum(min));
            pw.print("  Max = " + returnShortNum(max));
            pw.println();
            pw.print(returnHourMinuteSecond(run_time));
            pw.println();
            pw.print("*******************************************************************");
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printResultCsv(ProblemUtil.ProblemID problemID, String algName){
        String folderName;
        folderName = OUTOUT_PATH + Utility.date + "/" + algName;
        File newfile = new File(folderName);
        newfile.mkdirs();
        try {
            FileWriter fw = new FileWriter(folderName + "/ExperimentalResult.csv", true); //追記モード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            for (int line = 0; line < 4; line++) {
                /* col = 0*/
                if (line == 0){
                    writeValue(pw, problemID.name());
                }else {
                    writeValue(pw, "");
                }
                /* col = 1*/
                switch (line){
                    case 0:
                        writeValue(pw, "Mean");
                        break;
                    case 1:
                        writeValue(pw, "Std");
                        break;
                    case 2:
                        writeValue(pw, "Min");
                        break;
                    case 3:
                        writeValue(pw, "Max");
                        break;
                }
                /* col = 2*/
                switch (line){
                    case 0:
                        writeValue(pw, returnShortNum(ave));
                        break;
                    case 1:
                        writeValue(pw, returnShortNum(std));
                        break;
                    case 2:
                        writeValue(pw, returnShortNum(min));
                        break;
                    case 3:
                        writeValue(pw, returnShortNum(max));
                        break;
                }

                pw.println();
            }
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String returnShortNum(double num){
        double threshhold = 1.0;
        DecimalFormat zero = new DecimalFormat("0");
        DecimalFormat dec = new DecimalFormat("0.00");
        DecimalFormat exp = new DecimalFormat("0.00E0");

        if (Math.abs(num) == 0)
            return zero.format(num);
        else if (Math.abs(num) < threshhold)
            return exp.format(num);
        else
            return dec.format(num);
    }

    public static void printShortNum(double num){
        System.out.print(returnShortNum(num));
    }

    public static String returnHourMinuteSecond(long milliSecond){
        long second = TimeUnit.MILLISECONDS.toSeconds(milliSecond) % 60;
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliSecond) % 60;
        long hour = TimeUnit.MILLISECONDS.toHours(milliSecond);

        return String.format("%02dh : %02dm : %02ds", hour, minute, second);
    }

    public static void printHourMinuteSecond(long milliSecond){
        System.out.printf(returnHourMinuteSecond(milliSecond));
    }
    public static void printFinalBest(int run, Swarm sub){
        Utility.printLine();
        System.out.println("run = " + run);
        sub.printBestParticle();
        System.out.println();
        Utility.printLine();
    }

    public static void printSettings(){
        printLine();
        System.out.println("Maximun Function Evaluations = " + MAX_FUNC_EVAL);
        System.out.println("Dimension = " + DIMENSION);
        System.out.println("RUN = " + RUN_MAX);
        printLine();
        String folderName;
        folderName = OUTOUT_PATH + Utility.date;
        File newfile = new File(folderName);
        newfile.mkdirs();
        /* Make Experimental setting file */
        try {
            FileWriter fw = new FileWriter(folderName + "/ExperimentalSetting.txt", false); //上書きモード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("Maximun Function Evaluations = " + MAX_FUNC_EVAL);
            pw.println();
            pw.print("Dimension = " + DIMENSION);
            pw.println();
            pw.print("RUN = " + RUN_MAX);
            pw.println();
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeValue(PrintWriter pw, int value){
        pw.print(value + ",");
    }

    public static void writeValue(PrintWriter pw, double value){
        pw.print(value + ",");
    }

    public static void writeValue(PrintWriter pw, String value){
        pw.print(value + ",");
    }

    public static void writeValue(PrintWriter pw, int[] array){
        for (int value : array) {
            writeValue(pw, value);
        }
    }

    public static void writeValue(PrintWriter pw, double[] array){
        for (double value : array) {
            writeValue(pw, value);
        }
    }
}

