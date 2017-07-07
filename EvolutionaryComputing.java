/**
 * Created by toshiki on 2017/06/24.
 */
public class EvolutionaryComputing {
    public static void main(String[] args) throws Exception{
        Utility.setMAX_FUNC_EVAL(200000);
        Utility.setDIMENSION(30);
        Utility.setRUN_MAX(30);
        Utility.setRandSeed(1234);
        ProblemUtil.setProblemID(ProblemUtil.ProblemID.Sphere);

        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;
        SubSwarm sub1 = new SubSwarm(PSOUtil.getSUB_SWARM_SIZE());

        PSOUtil.currentIW = PSOUtil.getMAX_IW();

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()){
            //sub1.printSubSwarm();
            sub1.updateVelocity();
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

            PSOUtil.currentIW = PSOUtil.getMAX_IW() - (PSOUtil.getMAX_IW() - PSOUtil.getMIN_IW())
                                                        * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();
            Utility.cur_generation ++;
            System.out.println("Gen =" + Utility.cur_generation + "Lbest = " + sub1.getLbest_index() + " Hist_fit = " + sub1.getLbest_fitness());
        }
        sub1.printSubSwarm();

    }
}
