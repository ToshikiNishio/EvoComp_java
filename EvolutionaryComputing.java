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
        System.out.println(ProblemUtil.ProblemID.Sphere.getUpper());

        Individual ind1 = new Individual();
        System.out.println(ind1.positon[0]);
        ind1.positon[0] = 2.5;
        ind1.positon[3] = 5;
        System.out.println(ProblemUtil.ProblemID.Sphere.calc_fit(ind1.positon));

        ProblemUtil.SetProblemID(ProblemUtil.ProblemID.Sphere);
        System.out.println("Lower = " + ProblemUtil.getProg_Lower());
        System.out.println("Upper =" + ProblemUtil.getProg_Upper());
        ProblemUtil.SetProblemID(ProblemUtil.ProblemID.Shwefel_P2_22);
        System.out.println("Lower = " + ProblemUtil.prob_obj.getLower());
        System.out.println("Upper =" + ProblemUtil.prob_obj.getUpper());
        System.out.println("fitness =" + ProblemUtil.prob_obj.calc_fit(ind1.positon));
    }
}
