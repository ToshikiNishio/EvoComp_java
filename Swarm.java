import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Created by toshiki on 2017/07/02.
 */
public class Swarm {
    public ArrayList<Particle> particles =new ArrayList<Particle>();
    /* Lbest is Gbest in global-version PSO */
    private int Lbest_index;
    private double Lbest_fitness;

    /* Initialize particles */
    public Swarm(int createParticleNum) {
        Lbest_fitness = Double.MAX_VALUE;
        for (int par_i = 0; par_i < createParticleNum; par_i++) {
            Particle tmp = new Particle();
            judgeParticleLbest(tmp, par_i);
            particles.add(tmp);
        }
    }

    public int getLbest_index() {return Lbest_index;}
    public double getLbest_fitness() {return Lbest_fitness;}
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
    public void updateVelocity(double w, double c1, double c2){
        //double w  = PSOUtil.currentIW;
        int    G  = Lbest_index;
        double MAX_V = (ProblemUtil.getProg_Upper() - ProblemUtil.getProg_Lower()) * Utility.MAX_MAGNITUDE_VELOCITY;

        double x;
        double hist_x;
        double G_hist_x;

        for (Particle par : particles) {
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                x = par.positon[dim];
                hist_x = par.hist_best_pos[dim];
                G_hist_x = particles.get(G).hist_best_pos[dim];

                par.velocity[dim] = w * par.velocity[dim]
                                    + c1 * Utility.rand() * (hist_x - x)
                                    + c2 * Utility.rand() * (G_hist_x - x);
                if (par.velocity[dim] < -MAX_V) {
                    par.velocity[dim] = -MAX_V;
                }
                else if (par.velocity[dim] > MAX_V) {
                    par.velocity[dim] = MAX_V;
                }
            }
        }

    }
    public void updatePosition(){
        double Lower = ProblemUtil.getProg_Lower();
        double Upper = ProblemUtil.getProg_Upper();

        for (Particle par : particles) {
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                par.positon[dim] += par.velocity[dim];
                
                if (par.positon[dim] < Lower){
                    par.positon[dim] = Math.min(Upper, 2 * Lower - par.positon[dim]);
                }else if (par.positon[dim] > Upper){
                    par.positon[dim] = Math.max(Lower, 2 * Upper - par.positon[dim]);
                }
            }
        }
    }
    public void evaluateSubSwarm(){

//        particles.stream().parallel().forEachOrdered(par -> {
//            //synchronized (par) {
//                par.evaluateParticle();
//                judgeParticleLbest(par, particles.indexOf(par));
//            //}
//        });

        for (int par_i = 0; par_i < particles.size(); par_i++) {
            Particle par = particles.get(par_i);
            par.evaluateParticle();
            judgeParticleLbest(par, par_i);
        }
    }

    /* Print information of Swarm */
    public void printSubSwarm(){
        Utility.printDevideGeneration(Utility.cur_generation, Utility.cur_func_eval, Lbest_index, Lbest_fitness);
        for (int par_i = 0; par_i < particles.size() ; par_i++) {
            Utility.printindLine(par_i);
            particles.get(par_i).printIndivisual();
        }
        System.out.println();
    }
    public void printBestParticle(){
        particles.get(Lbest_index).printHistBest();
    }
}
