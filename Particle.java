/**
 * Created by toshiki on 2017/06/28.
 */
public class Particle extends Individual{
    double hist_best_fit;
    double[] hist_best_pos = new double[Utility.getDIMENSION()];
    double[] velocity = new double[Utility.getDIMENSION()];

    /* Generate particle */
    public Particle(){
        hist_best_fit = super.current_fitness;
        hist_best_pos = super.positon;
        for (int dim = 0 ; dim < Utility.getDIMENSION() ; dim++) {
            velocity[dim] = 0;
        }
    }

    public void evaluateParticle(){
        super.evaluateIndividual();
        /* Compare current fitness & position with historical fitness & position */
        if (current_fitness < hist_best_fit){
            hist_best_fit = current_fitness;
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                hist_best_pos[dim] = positon[dim];
            }
        }
    }

    /* Print information of individual */
    public void printIndivisual(){
        printCurrentPosition();
        printCurrentFitness();
        printVelocity();
        printHistBestPosition();
        printHistBestFitness();
    }
    public void printVelocity(){
        System.out.print("Velocity = ");
        for (int i = 0; i<Utility.getDIMENSION(); i++){
            System.out.printf(" %f", velocity[i]);
        }
        System.out.println();
    }
    public void printHistBestPosition() {
        System.out.print("Hist_best_position = ");
        for (int i = 0; i<Utility.getDIMENSION(); i++) {
            System.out.printf(" %f", hist_best_pos[i]);
        }
        System.out.println();
    }
    public void printHistBestFitness() {
        System.out.println("His_best_fitness = " + hist_best_fit);
    }
}
