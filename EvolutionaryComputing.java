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

        Utility.setCsvOutputFlg(true);
        Utility.setOuputParameterFlg(true);
        Utility.setOutputScatterFlg(false);

        AlgContext algContext = new AlgContext(new GPSO());
        algContext.printParameters();

        for (ProblemUtil.ProblemID problem : ProblemUtil.ProblemID.values()) {
            if (problem.ordinal() < 12) {
                ProblemUtil.setProblemID(problem);
                Utility.InitOutputPara();
                for (int run = 0; run < Utility.getRUN_MAX(); run++) {
                    Utility.setRandSeed(1234 + run);
                    algContext.run(run);
                }
                String algName = String.valueOf(algContext.getAlgorithm());
                Utility.printResult(problem, algName.substring(0, algName.indexOf("@")));
                System.out.println();
            }
        }
    }
}
