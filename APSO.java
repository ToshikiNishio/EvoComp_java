/**
 * Created by toshiki on 2017/07/18.
 */
public class APSO implements Algorithm {
    enum EvolutionaryState{
        Exploration,
        Exploitation,
        Convergence,
        JumpingOut
    }

    public void run(int run){
        double MAX_IW = 0.9;    //Maximum inertia weight
        double MIN_IW = 0.4;    //Minimum inertia weight
        double INI_C1 = 2.0;    //Acceleration Coefficients 1
        double INI_C2 = 2.0;    //Acceleration Coefficients 2
        int    SUB_SWARM_SIZE = 20;

        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;

        SubSwarm sub1 = new SubSwarm(SUB_SWARM_SIZE);
        double currentIW = MAX_IW;
        double currentC1 = INI_C1;
        double currentC2 = INI_C2;
        EvolutionaryState state;

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            state = EvolutionaryStateEstimation(sub1);
            System.out.println(state);

            sub1.updateVelocity(currentIW, currentC1, currentC2);
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

            currentIW = MAX_IW - (MAX_IW -MIN_IW)
                    * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();

            Utility.cur_generation++;
        }
        /* Input best particle for Output */
        Utility.stack_hist_best_fit[run] = sub1.getLbest_fitness();
        //Utility.printFinalBest(run, sub1);
    }

    private EvolutionaryState EvolutionaryStateEstimation(SubSwarm sub){
        /* Step1 Calculate the mean distance of each particle "ind" to
        *  all the other particles. */
        int N = sub.particles.size();
        double dis[][] = new double[N][N];
        double meanDis[] = new double[N];

        for (int ind1 = 0; ind1 < N; ind1++) {
            for (int ind2 = 0; ind2 < ind1; ind2++) {
                dis[ind2][ind1] = dis[ind1][ind2] =
                        Utility.distance(sub.particles.get(ind1),
                                            sub.particles.get(ind2));
            }
            dis[ind1][ind1] = 0;
        }

        for (int ind1 = 0; ind1 < N; ind1++) {
            double dsum = 0;
            for (int ind2 = 0; ind2 < N; ind2++) {
                dsum += dis[ind1][ind2];
            }
            meanDis[ind1] = dsum / (N - 1.0);
        }
        /* Step2 Compute an "evolutionary factor"*/
        double evoFactor;
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;

        for (double mdis : meanDis) {
            if (min > mdis)
                min = mdis;
            if (max < mdis)
                max = mdis;
        }

        if (max == min){
            evoFactor = 0;
            System.out.println("evoF = 0 !");
        } else {
            evoFactor = (meanDis[sub.getLbest_index()] - min)
                    / (max - min);
        }
        /* Step3 Classify Evolutionary factor into one of four sets */
        if (evoFactor <= (3.5 / 15.0))
            return EvolutionaryState.Convergence;
        if (evoFactor <= 0.5)
            return EvolutionaryState.Exploitation;
        if (evoFactor <= (11.5 / 15.0))
            return EvolutionaryState.Exploration;

        return EvolutionaryState.JumpingOut;
    }
}
