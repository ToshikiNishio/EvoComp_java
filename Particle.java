/**
 * Created by toshiki on 2017/06/28.
 */
public class Particle extends Individual{
    double[] velocity = new double[Utility.getDIMENSION()];
    double hist_best_fit;
    double[] hist_best_pos = new double[Utility.getDIMENSION()];
}
