/**
 * Created by toshiki on 2017/07/18.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;


public class APSO implements Algorithm {

    /* Parameter Settings */
    double MAX_IW = 0.9;   //Maximum inertia weight
    double MIN_IW = 0.4;   //Minimum inertia weight
    double C1 = 2.0;       //Acceleration Coefficients 1
    double C2 = 2.0;       //Acceleration Coefficients 2
    int    SWARM_SIZE = 20;

    enum EvolutionaryState{
        Exploration     ( 1.0, -1.0),
        Exploitation    ( 0.5, -0.5),
        Convergence     ( 0.5,  0.5),
        JumpingOut      (-1.0,  1.0);

        double addC1;
        double addC2;
        EvolutionaryState(double addC1, double addC2) {
            this.addC1 = addC1;
            this.addC2 = addC2;
        }
    }
    /* Variable definition */
    String folderPassClass;
    String folderPassRun;
    double currentIW;
    /* Record variables */
    public ArrayList<Integer> recordGeneration =new ArrayList<Integer>();
    public ArrayList<Integer> recordEvalTimes =new ArrayList<Integer>();
    public ArrayList<Double> recordInertiaWeight =new ArrayList<Double>();
    public ArrayList<Double> recordBestFitness =new ArrayList<Double>();
    public ArrayList<Swarm> recordPosition = new ArrayList<>();
    public ArrayList<Integer> recordGInd = new ArrayList<>();
    public ArrayList<Double> recordC1 = new ArrayList<>();
    public ArrayList<Double> recordC2 = new ArrayList<>();

    public void run(int run){
        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;
        currentIW = MAX_IW;

        makeFiles(run);

        Swarm swarm = new Swarm(SWARM_SIZE);
        recordVariables(swarm);
        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            adaptiveParam(swarm);

            swarm.updateVelocity(currentIW, C1, C2);
            swarm.updatePosition();
            swarm.evaluateSubSwarm();

            Utility.cur_generation++;

            recordVariables(swarm);
        }
        /* Input best particle for Output */
        Utility.stack_hist_best_fit[run] = swarm.getLbest_fitness();
        //Utility.printFinalBest(run, swarm);
        writeFiles();
    }

    private void adaptiveParam(Swarm swarm){
        double[] meanDis = calcMeanDis(swarm);
        double evoFactor = calcEvoFac(meanDis, swarm.getLbest_index());
        EvolutionaryState evoState = calcEvoState(evoFactor);
        updateAC(evoState);
        currentIW = 1.0 / (1.0 + 1.5 * Math.exp(-2.6 * evoFactor));
    }

    private double[] calcMeanDis(Swarm swarm){
        /* Step1 Calculate the mean distance of each particle "ind" to
        *  all the other particles. */
        int N = swarm.particles.size();
        double dis[][] = new double[N][N];
        double meanDis[] = new double[N];

        for (int ind1 = 0; ind1 < N; ind1++) {
            for (int ind2 = 0; ind2 < ind1; ind2++) {
                dis[ind2][ind1] = dis[ind1][ind2] =
                        Utility.distance(swarm.particles.get(ind1),
                                swarm.particles.get(ind2));
            }
            dis[ind1][ind1] = 0;
        }

        for (int ind1 = 0; ind1 < N; ind1++) {
            double dsum = 0;
            for (int ind2 = 0; ind2 < N; ind2++) {
                dsum += dis[ind1][ind2];
            }
            meanDis[ind1] = dsum / (N - 1.0);
        }

        return meanDis;
    }

    private double calcEvoFac(double[] meanDis, int BestInd){
        /* Step2 Compute an "evolutionary factor"*/
        double min = Utility.getMinFromArray(meanDis);
        double max = Utility.getMaxFromArray(meanDis);
        double evoFactor;
        if (min == max){
            evoFactor = 0;
            System.out.println("Evolutionary Factor = 0 !!");
        } else {
            evoFactor = (meanDis[BestInd] - min) / (max - min);
        }

        return evoFactor;
    }

    private EvolutionaryState calcEvoState(double evoFactor){
        /* Step3 Classify Evolutionary factor into one of four sets */
        if (! (0 <= evoFactor && evoFactor <= 1) )
            System.out.println("Error!! Invalid Evolutionary Factor: evoFactor = " + evoFactor);

        if (evoFactor <= (3.5 / 15.0))
            return EvolutionaryState.Convergence;
        if (evoFactor <= 0.5)
            return EvolutionaryState.Exploitation;
        if (evoFactor <= 11.5 / 15.0)
            return EvolutionaryState.Exploration;

        return EvolutionaryState.JumpingOut;
    }

    /* Add delta to acceleration coefficients */
    private void updateAC(EvolutionaryState evoState){
        /* Adjustment on acceleration coefficients bounded by delta */
        double delta1;
        delta1 = 0.05 + Utility.rand() * 0.05;
        delta1 = Utility.rand() * delta1;
        C1 += delta1 * evoState.addC1;

        double delta2;
        delta2 = 0.05 + Utility.rand() * 0.05;
        delta2 = Utility.rand() * delta2;
        C2 += delta2 * evoState.addC2;

        /* Clamp acceleration coefficients */
        if (C1 <= 1.5)
            C1 = 1.5;
        if (C1 >= 2.5)
            C1 = 2.5;
        if (C2 <= 1.5)
            C2 = 1.5;
        if (C2 >= 2.5)
            C2 = 2.5;

        /* If sum is larger than 4.0, normalized */
        double sum = C1 + C2;
        if (sum > 4.0){
            C1 = C1 / sum * 4.0;
            C2 = C2 / sum * 4.0;
        }
    }
    /**************************************************************************************************************
     *   Create Output File
     * *************************************************************************************************************/

    public void printParameters() {
        Utility.printLine();
        System.out.println("Maximum inertia weight = " + MAX_IW);
        System.out.println("Minimum inertia weight = " + MIN_IW);
        System.out.println("C1 = " + C1);
        System.out.println("C2 = " + C2);
        System.out.println("Swarm size = " + SWARM_SIZE);
        Utility.printLine();
        String className = new Object(){}.getClass().getEnclosingClass().getName();
        folderPassClass = Utility.OUTPUT_PATH + Utility.date + "/" + className;
        File newfile = new File(folderPassClass);
        newfile.mkdirs();
        /* Make Parameter setting file */
        try {
            FileWriter fw = new FileWriter(folderPassClass + "/ParameterSetting.txt", false); //上書きモード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("Maximum inertia weight = " + MAX_IW);
            pw.println();
            pw.print("Minimum inertia weight = " + MIN_IW);
            pw.println();
            pw.print("C1 = " + C1);
            pw.println();
            pw.print("C2 = " + C2);
            pw.println();
            pw.print("Swarm size = " + SWARM_SIZE);
            pw.println();

            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    /*
    * Follow method are to make csv files.
    * */
    private void makeFiles(int run){
        if (!Utility.csvOutputFlg) {
            return;
        }
        if (Utility.outputParameterFlg) {
            makeParameterFiles(run);
        }
        if (Utility.outputScatterFlg) {
            makeScatterFiles(run);
        }
    }

    private void writeFiles(){
        if (!Utility.csvOutputFlg) {
            return;
        }
        if (Utility.outputParameterFlg) {
            writeParameterFiles();
        }
        if (Utility.outputScatterFlg) {
            writeScatterFiles();
        }

        clearRecord();
    }

    private void makeParameterFiles(int run){
        /* Make Folder */
        String className = new Object(){}.getClass().getEnclosingClass().getName();
        folderPassRun = Utility.OUTPUT_PATH + Utility.date + "/" + className +
                "/" + ProblemUtil.getProbID_Name() + "/RUN" + run;
        File newfile = new File(folderPassRun);
        newfile.mkdirs();
        /* Make csv file */
        try {
            //Make csv file
            FileWriter fw = new FileWriter(folderPassRun + "/output.csv", false); //上書きモード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            Utility.writeValue(pw, "Generation");
            Utility.writeValue(pw, "eval_times");
            Utility.writeValue(pw, "Best_fitness");
            Utility.writeValue(pw, "inertia_weight");
            Utility.writeValue(pw, "c1");
            Utility.writeValue(pw, "c2");

            pw.println();
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void makeScatterFiles(int run){
        /* Make Folder */
        String className = new Object(){}.getClass().getEnclosingClass().getName();
        folderPassRun = Utility.OUTPUT_PATH + Utility.date + "/" + className +
                "/" + ProblemUtil.getProbID_Name() + "/RUN" + run;
        File newfile = new File(folderPassRun);
        newfile.mkdirs();
        /* Make csv file */
        try {
            //Make csv file
            FileWriter fw = new FileWriter(folderPassRun + "/scatter.csv", false); //上書きモード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            Utility.writeValue(pw, "Generation");
            Utility.writeValue(pw, "eval_times");
            Utility.writeValue(pw, "G_index");
            Utility.writeValue(pw, "individual_index");
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                Utility.writeValue(pw, "X" + dim);
            }
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                Utility.writeValue(pw, "pBest" + dim);
            }

            pw.println();
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void recordVariables(Swarm swarm){
        if (!Utility.csvOutputFlg) {
            return;
        }

        recordGeneration.add(Utility.cur_generation);
        recordEvalTimes.add(Utility.cur_func_eval);
        recordInertiaWeight.add(currentIW);
        recordBestFitness.add(swarm.getLbest_fitness());
        recordGInd.add(swarm.getLbest_index());
        recordC1.add(C1);
        recordC2.add(C2);
        /* Record swarm to record positions */
        Swarm tmp_swrm = new Swarm(0);
        for (int ind = 0; ind < swarm.particles.size(); ind++) {
            tmp_swrm.particles.add(swarm.particles.get(ind).clone());
        }
        recordPosition.add(tmp_swrm);
    }

    private void writeParameterFiles(){
        try {
            FileWriter fw = new FileWriter(folderPassRun + "/output.csv", true); //追記モード
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            int size = recordGeneration.size();
            for (int ind =0; ind < size; ind++) {
                Utility.writeValue(pw, recordGeneration.get(ind));
                Utility.writeValue(pw, recordEvalTimes.get(ind));
                Utility.writeValue(pw, recordBestFitness.get(ind));
                Utility.writeValue(pw, recordInertiaWeight.get(ind));
                Utility.writeValue(pw, recordC1.get(ind));
                Utility.writeValue(pw, recordC2.get(ind));

                pw.println();
            }
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeScatterFiles(){
        try {
            FileWriter fw = new FileWriter(folderPassRun + "/scatter.csv", true); //追記モード
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            int generation = recordPosition.size();
            for (int gen =0; gen < generation; gen++) {
                int par_ind = 0;
                for (Particle par : recordPosition.get(gen).particles) {
                    Utility.writeValue(pw, recordGeneration.get(gen));
                    Utility.writeValue(pw, recordEvalTimes.get(gen));
                    Utility.writeValue(pw, recordGInd.get(gen));
                    Utility.writeValue(pw, par_ind++);
                    Utility.writeValue(pw, par.positon);
                    Utility.writeValue(pw, par.hist_best_pos);

                    pw.println();
                }
            }
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* To initialize next run record variables, we clear it. */
    private void clearRecord(){
        recordPosition.clear();
        recordGeneration.clear();
        recordEvalTimes.clear();
        recordBestFitness.clear();
        recordInertiaWeight.clear();
    }
}
