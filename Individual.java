/**
 * Created by toshiki on 2017/06/24.
 */
public class Individual {
    double[] positon = new double[Utility.getDIMENSION()];
    double current_fitness;


    /* Generate individual */
    public Individual(){
        for (int dim = 0 ; dim < Utility.getDIMENSION() ; dim++) {
            double lower = ProblemUtil.getProg_Lower();
            double upper = ProblemUtil.getProg_Upper();
            positon[dim] = lower + (upper - lower) * Utility.rand();
        }
        current_fitness = ProblemUtil.calcFitness(positon);
    }

    /* Print information of individual */
    public void printIndivisual(){
        printCurrentPosition();
        printCurrentFitness();
    }
    public void printCurrentPosition() {
        for (int i = 0; i<Utility.getDIMENSION(); i++) {
            System.out.println("position[" + i + "] = " + positon[i]);
        }
    }
    public void printCurrentFitness() {
        System.out.println("current_fitness = " + current_fitness);
    }

}
