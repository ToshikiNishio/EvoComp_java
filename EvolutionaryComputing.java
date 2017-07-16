/**
 * Created by toshiki on 2017/06/24.
 */
public class EvolutionaryComputing {
    public static void main(String[] args) throws Exception{
        Utility.setMAX_FUNC_EVAL(200000);
        Utility.setDIMENSION(30);
        Utility.setRUN_MAX(30);
        AlgContext algContext = new AlgContext(new GPSO());

        for (ProblemUtil.ProblemID problem : ProblemUtil.ProblemID.values()) {
            if (problem.ordinal() < 12) {
                Utility.setRandSeed(1234);
                ProblemUtil.setProblemID(problem);
                Utility.InitOutputPara();
                long start_time = System.currentTimeMillis();
                for (int run = 0; run < Utility.getRUN_MAX(); run++) {
                    algContext.run(run);
                }
                Utility.calcOutputPara();
                Utility.printOutputPara(problem);
                long end_time = System.currentTimeMillis();
                Utility.printHourMinuteSecond(end_time - start_time);
                System.out.println();
            }
        }
    }
}
