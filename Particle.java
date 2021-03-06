/**
 * Created by toshiki on 2017/06/28.
 */
public class Particle extends Individual implements Cloneable{
    double hist_best_fit;
    double[] hist_best_pos = new double[Utility.getDIMENSION()];
    double[] velocity = new double[Utility.getDIMENSION()];

    /* Generate particle */
    public Particle(){
        hist_best_fit = super.current_fitness;
        hist_best_pos = super.positon.clone();

        double MAX_V = (ProblemUtil.getProg_Upper() - ProblemUtil.getProg_Lower()) * Utility.MAX_MAGNITUDE_VELOCITY;
        for (int dim = 0 ; dim < Utility.getDIMENSION() ; dim++) {
            //velocity[dim] = 0;
            velocity[dim] = -MAX_V + 2 * MAX_V * Utility.rand();
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
    public void printHistBest(){
        printHistBestPosition();
        printHistBestFitness();
    }
    public void printVelocity(){
        System.out.print("Velocity = ");
        for (int i = 0; i<Utility.getDIMENSION(); i++){
            System.out.print(" ");
            Utility.printShortNum(velocity[i]);
        }
        System.out.println();
    }
    public void printHistBestPosition() {
        System.out.print("Hist_best_position = ");
        for (int i = 0; i<Utility.getDIMENSION(); i++) {
            System.out.print(" ");
            Utility.printShortNum(hist_best_pos[i]);
        }
        System.out.println();
    }
    public void printHistBestFitness() {
        System.out.print("His_best_fitness = ");
        Utility.printShortNum(hist_best_fit);
    }

    @Override
    public Particle clone(){
        Particle ret = null;

        try{
            ret = (Particle)super.clone();
            ret.hist_best_pos = this.hist_best_pos.clone();
            ret.velocity = this.velocity.clone();
        }catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }
}
