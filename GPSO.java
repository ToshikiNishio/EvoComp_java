/**
 * Created by toshiki on 2017/07/17.
 */
public class GPSO implements Algorithm {
    public void run(int run){
        double MAX_IW = 0.9;   //Maximum inertia weight
        double MIN_IW = 0.4;   //Minimum inertia weight
        double C1 = 2.0;       //Acceleration Coefficients 1
        double C2 = 2.0;       //Acceleration Coefficients 2
        int    SUB_SWARM_SIZE = 20;

        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;

        SubSwarm sub1 = new SubSwarm(SUB_SWARM_SIZE);
        double currentIW = MAX_IW;

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            sub1.updateVelocity(currentIW, C1, C2);
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
}
