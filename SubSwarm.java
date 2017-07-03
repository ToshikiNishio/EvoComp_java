import java.util.ArrayList;

/**
 * Created by toshiki on 2017/07/02.
 */
public class SubSwarm {
    public ArrayList<Particle> particles =new ArrayList<Particle>();
    /* Lbest is Gbest in global-version PSO */
    private int Lbest_index;
    private double Lbest_fitness;

    /* Initialize particles */
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
    public void printLbest(){System.out.println("Lbest= " + Lbest_index);}
    /* Update particles */
    public void updateVelocity(){
        double c1 = PSOUtil.getC1();
        double c2 = PSOUtil.getC2();
        double w  = PSOUtil.currentIW;
        int    G  = Lbest_index;

        for (Particle par : particles) {
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                par.velocity[dim] = w * par.velocity[dim]
                                    + c1 * Utility.rand() * (par.hist_best_pos[dim] - par.positon[dim])
                                    + c2 * Utility.rand() * (particles.get(G).hist_best_pos[dim] - par.positon[dim]);
            }
        }
    }
}
