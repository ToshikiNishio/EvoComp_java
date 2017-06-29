/**
 * Created by toshiki on 2017/06/24.
 */
public class Individual {
    double[] positon = new double[Utility.getDIMENSION()];
    double current_fitness;


    /* Constructor */
    public Individual(){
        for (int dim = 0 ; dim < Utility.getDIMENSION() ; dim++) {
            double lower = ProblemUtil.getProg_Lower();
            double upper = ProblemUtil.getProg_Upper();
            positon[dim] = lower + (upper - lower) * Utility.rand();
        }
        current_fitness = ProblemUtil.getFitness(positon);
    }
}
