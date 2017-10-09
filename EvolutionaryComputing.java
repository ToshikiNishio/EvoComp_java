/**
 * Created by toshiki on 2017/06/24.
 */
public class EvolutionaryComputing {
    public static void main(String[] args) throws Exception{
        Utility.getDate();
        Utility.setMAX_FUNC_EVAL(200000);
        Utility.setDIMENSION(30);
        Utility.setRUN_MAX(30);
        Utility.printSettings();
        AlgContext algContext = new AlgContext(new GPSO());

        for (ProblemUtil.ProblemID problem : ProblemUtil.ProblemID.values()) {
            if (problem.ordinal() < 12) {
                ProblemUtil.setProblemID(problem);
                Utility.InitOutputPara();
                for (int run = 0; run < Utility.getRUN_MAX(); run++) {
                    Utility.setRandSeed(1234 + run);
                    algContext.run(run);
                }
                Utility.printOutputPara(problem);
                System.out.println();
            }
        }
    }
}
