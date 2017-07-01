/**
 * Created by toshiki on 2017/06/24.
 */
public class EvolutionaryComputing {
    public static void main(String[] args) throws Exception{
        Utility.setMAX_FUNC_EVAL(200000);
        Utility.setDIMENSION(30);
        Utility.setRUN_MAX(30);
        Utility.setRnd(1234);
        ProblemUtil.setProblemID(ProblemUtil.ProblemID.Sphere);


        Individual ind1 = new Individual();
        System.out.println(ind1.positon[0]);
        ind1.positon[0] = 2.5;
        ind1.positon[3] = 5;
        System.out.println(ProblemUtil.ProblemID.Sphere.calc_fit(ind1.positon));

        System.out.println("Lower = " + ProblemUtil.getProg_Lower());
        System.out.println("Upper =" + ProblemUtil.getProg_Upper());
        ProblemUtil.setProblemID(ProblemUtil.ProblemID.Shwefel_P2_22);
        System.out.println("Lower = " + ProblemUtil.getProg_Lower());
        System.out.println("Upper =" + ProblemUtil.getProg_Upper());
        System.out.println("fitness =" + ProblemUtil.calcFitness(ind1.positon));


        Individual ind2 = new Individual();
        ind2.printIndivisual();
    }
}
