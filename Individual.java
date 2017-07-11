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
        evaluateIndividual();
    }

    public void evaluateIndividual(){
        if (Utility.cur_func_eval == Utility.getMAX_FUNC_EVAL())
            return;

        current_fitness = ProblemUtil.calcFitness(positon);
        Utility.cur_func_eval++;
    }

    /* Print information of individual */
    public void printIndivisual(){
        printCurrentPosition();
        printCurrentFitness();
    }
    public void printCurrentPosition() {
        System.out.print("Position =");
        for (int dim = 0; dim<Utility.getDIMENSION(); dim++) {
            System.out.print(" ");
            Utility.printShortNum(positon[dim]);
        }
        System.out.println();
    }
    public void printCurrentFitness() {
        System.out.print("Current_fitness = ");
        Utility.printShortNum(current_fitness);
        System.out.println();
    }

}
