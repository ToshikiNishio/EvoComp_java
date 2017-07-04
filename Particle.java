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
        for (int i = 0; i<Utility.getDIMENSION(); i++){
            System.out.println("velocity[" + i + "] = " + velocity[i]);
        }
    }
    public void printHistBestPosition() {
        for (int i = 0; i<Utility.getDIMENSION(); i++) {
            System.out.println("hist_best_position[" + i + "] = " + hist_best_pos[i]);
        }
    }
    public void printHistBestFitness() {
        System.out.println("his_best_fitness = " + hist_best_fit);
    }
}
