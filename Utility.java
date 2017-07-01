/**
 * Created by toshiki on 2017/06/24.
 */
public class Utility {
    /* Constructor */
    private Utility(){}

    /* Member */
    private static int MAX_FUNC_EVAL;
    private static int DIMENSION;
    private static int RUN_MAX;
    public static Sfmt Rnd;

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
    public static void setRnd(int seed){Utility.Rnd = new Sfmt(seed);}
    public static double rand(){return Rnd.NextUnif();}

    /* Utility method */
    public static double square(double x){return x * x;}

}

