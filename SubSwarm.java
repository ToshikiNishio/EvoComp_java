import java.util.ArrayList;

/**
 * Created by toshiki on 2017/07/02.
 */
public class SubSwarm {
    public ArrayList<Particle> particles =new ArrayList<Particle>();

    /* Lbest is Gbest in global-version PSO */
    private int Lbest_index;
    private double Lbest_fitness;

    public SubSwarm(int createParticleNum) {
        Lbest_fitness = Double.MAX_VALUE;
        for (int par_i = 0; par_i < createParticleNum; par_i++) {
            Particle tmp = new Particle();
            judgeParticleLbest(tmp, par_i);
            particles.add(tmp);
        }
    }

    public int getLbest_index() {return Lbest_index;}
    /* If argument is better than Lbest, we set argument Lbest.  */
    /* Consider better method name */
    public void judgeParticleLbest(Particle par, int par_i) {
        if (par.hist_best_fit < Lbest_fitness) {
            Lbest_index = par_i;
            Lbest_fitness = par.hist_best_fit;
        }
    }

    public void printLbest(){
        System.out.println("Lbest= " + Lbest_index);
    }

}
