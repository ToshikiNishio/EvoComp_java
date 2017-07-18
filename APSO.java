/**
 * Created by toshiki on 2017/07/18.
 */
public class APSO implements Algorithm {
    enum EvolutionaryState{
        Exploration     ( 1.0, -1.0),
        Exploitation    ( 0.5, -0.5),
        Convergence     ( 0.5,  0.5),
        JumpingOut      (-1.0,  1.0);

        double addC1;
        double addC2;
        EvolutionaryState(double addC1, double addC2) {
            this.addC1 = addC1;
            this.addC2 = addC2;
        }
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
        /* Acceleration Coefficients.
         * C[0] : c1, C[1] : c2 */
        double C[] = new double[2];
        C[0] = INI_C1;
        C[1] = INI_C2;
        double evoFactor;
        EvolutionaryState state;

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            /* Evolutionary state estimation */
            evoFactor = calcEvoFactor(sub1);
            state = getEvoState(evoFactor);
            /* Adaptive Parameter Control */
            currentIW = 1.0 / (1.0 + 1.5 * Math.exp(-2.6 * evoFactor));
            adaptiveAccelerationCoefficients(state, C);
//            System.out.println(state);
//            System.out.println("c1 =" + C[0] + " c2 =" + C[1]);
            /* Update particles as well as GPSO */
            sub1.updateVelocity(currentIW, C[0], C[1]);
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

//            currentIW = MAX_IW - (MAX_IW -MIN_IW)
//                    * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();
            Utility.cur_generation++;
        }
        /* Input best particle for Output */
        Utility.stack_hist_best_fit[run] = sub1.getLbest_fitness();
        //Utility.printFinalBest(run, sub1);
    }

    private double calcEvoFactor(SubSwarm sub){
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
        return evoFactor;
    }

    EvolutionaryState getEvoState(double evoFactor){
        /* Step3 Classify Evolutionary factor into one of four sets */
        if (evoFactor <= (3.5 / 15.0))
            return EvolutionaryState.Convergence;
        if (evoFactor <= 0.5)
            return EvolutionaryState.Exploitation;
        if (evoFactor <= (11.5 / 15.0))
            return EvolutionaryState.Exploration;

        return EvolutionaryState.JumpingOut;
    }

    /* Add delta to acceleration coefficients */
    void adaptiveAccelerationCoefficients(EvolutionaryState state, double[] ac){
        /* Adjustment on acceleration coefficients bounded by delta */
        double delta;

        delta = 0.05 + Utility.rand() * 0.05;
        delta = Utility.rand() * delta;
        ac[0] += delta * state.addC1;
        
        delta = 0.05 + Utility.rand() * 0.05;
        delta = Utility.rand() * delta;
        ac[1] += delta * state.addC2;
        /* Clamp acceleration coefficients */
        for (int i = 0; i < 2 ; i++) {
            if (ac[i] <= 1.5)
                ac[i] = 1.5;
            if (ac[i] >= 2.5)
                ac[i] = 2.5;
        }
        /* If sum is larger than 4.0, normalized */
        double sum = ac[0] + ac[1];
        if (sum > 4.0){
            ac[0] = ac[0] / sum * 4.0;
            ac[1] = ac[1] / sum * 4.0;
        }
    }
}