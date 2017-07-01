/**
 * Created by toshiki on 2017/06/28.
 */
public class Particle extends Individual{
    double[] velocity = new double[Utility.getDIMENSION()];
    double hist_best_fit;
    double[] hist_best_pos = new double[Utility.getDIMENSION()];

    /* Generate particle */
    public Particle(){
        hist_best_fit = super.current_fitness;
        hist_best_pos = super.positon;
        for (int dim = 0 ; dim < Utility.getDIMENSION() ; dim++) {
            velocity[dim] = 0;
        }
    }
}
