/**
 * Created by toshiki on 2017/06/24.
 */
public class EvolutionaryComputing {
    public static void main(String[] args) throws Exception{
        Utility.setMAX_FUNC_EVAL(1000);
        Utility.setDIMENSION(2);
        Utility.setRUN_MAX(30);
        Utility.setRandSeed(1234);
        ProblemUtil.setProblemID(ProblemUtil.ProblemID.Sphere);

        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;
        SubSwarm sub1 = new SubSwarm(PSOUtil.getSUB_SWARM_SIZE());

        PSOUtil.currentIW = PSOUtil.getMAX_IW();

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()){
            sub1.updateVelocity();
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

            PSOUtil.currentIW = PSOUtil.getMAX_IW() - (PSOUtil.getMAX_IW() - PSOUtil.getMIN_IW())
                                                        * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();
            Utility.cur_generation ++;
        }

//        for (Particle par : sub1.particles) {
//            System.out.println("----------------------------------------------");
//            par.printIndivisual();
//        }


//        Individual ind1 = new Individual();
//        System.out.println(ind1.positon[0]);
//        ind1.positon[0] = 2.5;
//        ind1.positon[3] = 5;
//        System.out.println(ProblemUtil.ProblemID.Sphere.calc_fit(ind1.positon));
//
//        System.out.println("Lower = " + ProblemUtil.getProg_Lower());
//        System.out.println("Upper =" + ProblemUtil.getProg_Upper());
//        ProblemUtil.setProblemID(ProblemUtil.ProblemID.Shwefel_P2_22);
//        System.out.println("Lower = " + ProblemUtil.getProg_Lower());
//        System.out.println("Upper =" + ProblemUtil.getProg_Upper());
//        System.out.println("fitness =" + ProblemUtil.calcFitness(ind1.positon));
//
//
//        Particle ind2 = new Particle();
//        ind2.printIndivisual();
//        ind2.printCurrentFitness();
//        ind2.printHistBestFitness();
//
//        SubSwarm sub1 = new SubSwarm(20);
//        for (Particle par : sub1.particles) {
//            System.out.println("----------------------------------------------");
//            par.printIndivisual();
//        }
//        System.out.println("----------------------------------------------");
//        for (int i = 0; i < sub1.particles.size(); i++) {
//            System.out.print(i + " : ");
//            sub1.particles.get(i).printHistBestFitness();
//        }
//        sub1.printLbest();
    }
}
