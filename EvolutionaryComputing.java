/**
 * Created by toshiki on 2017/06/24.
 */
public class EvolutionaryComputing {
    public static void main(String[] args) throws Exception{
        Utility.setMAX_FUNC_EVAL(200000);
        System.out.println("FEs=" + Utility.getMAX_FUNC_EVAL());
        Utility.setDIMENSION(30);
        System.out.println("DIM=" + Utility.getDIMENSION());
        Utility.setRUN_MAX(30);
        System.out.println("RUN_MAX=" + Utility.getRUN_MAX());
        System.out.println(ProblemUtil.Basic_ProblemID.Quadric.getLower());
    }
}
