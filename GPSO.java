/**
 * Created by toshiki on 2017/07/17.
 */
public class GPSO implements Algorithm {
    public void run(int run){
        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;

        SubSwarm sub1 = new SubSwarm(PSOUtil.getSUB_SWARM_SIZE());
        PSOUtil.currentIW = PSOUtil.getMAX_IW();

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            sub1.updateVelocity();
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

            PSOUtil.currentIW = PSOUtil.getMAX_IW() - (PSOUtil.getMAX_IW() - PSOUtil.getMIN_IW())
                    * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();
            Utility.cur_generation++;
        }
        Utility.stack_hist_best_fit[run] = sub1.getLbest_fitness();
        Utility.printFinalBest(run, sub1);
    }
}
