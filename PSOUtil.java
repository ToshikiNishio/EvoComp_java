/**
 * Created by toshiki on 2017/07/03.
 */
public class PSOUtil {
    /* Constructor */
    private PSOUtil() {}

    /* Member */
    private static double MAX_MAGNITUDE_VELOCITY;
    private static double MAX_IW;   //Maximum inertia weight
    private static double MIN_IW;   //Minimum inertia weight

    /* Getter */
    public static double getMAX_MAGNITUDE_VELOCITY() {return MAX_MAGNITUDE_VELOCITY;}
    public static double getMAX_IW() {return MAX_IW;}
    public static double getMIN_IW() {return MIN_IW;}


}
