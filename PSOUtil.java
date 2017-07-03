/**
 * Created by toshiki on 2017/07/03.
 */
public class PSOUtil {
    /* Constructor */
    private PSOUtil() {}

    /* Member */
    private static double MAX_MAGNITUDE_VELOCITY = 0.2;
    private static double MAX_IW = 0.9;   //Maximum inertia weight
    private static double MIN_IW = 0.4;   //Minimum inertia weight
    private static double C1 = 2.0;       //Acceleration Coefficients 1
    private static double C2 = 2.0;       //Acceleration Coefficients 2

    public static double currentIW;

    /* Getter */
    public static double getMAX_MAGNITUDE_VELOCITY() {return MAX_MAGNITUDE_VELOCITY;}
    public static double getMAX_IW() {return MAX_IW;}
    public static double getMIN_IW() {return MIN_IW;}
    public static double getC1() {return C1;}
    public static double getC2() {return C2;}


}
