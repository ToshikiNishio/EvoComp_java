/**
 * Created by toshiki on 2017/07/17.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;


public class GPSO implements Algorithm {

    /* Parameter Settings */
    double MAX_IW = 0.9;   //Maximum inertia weight
    double MIN_IW = 0.4;   //Minimum inertia weight
    double C1 = 2.0;       //Acceleration Coefficients 1
    double C2 = 2.0;       //Acceleration Coefficients 2
    int    SWARM_SIZE = 20;
    /* Variable definition */
    String folderPassClass;
    String folderPassRun;
    String folderPassProb;
    double currentIW;
    /* Record variables */
    public ArrayList<Integer> recordGeneration =new ArrayList<Integer>();
    public ArrayList<Integer> recordEvalTimes =new ArrayList<Integer>();
    public ArrayList<Double> recordInertiaWeight =new ArrayList<Double>();
    public ArrayList<Double> recordBestFitness =new ArrayList<Double>();
    public ArrayList<Swarm> recordPosition = new ArrayList<>();
    public ArrayList<Integer> recordGInd = new ArrayList<>();

    public void run(int run){
        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;
        currentIW = MAX_IW;

        makeFiles(run);

        Swarm swarm = new Swarm(SWARM_SIZE);
        recordVariables(swarm);
        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            swarm.updateVelocity(currentIW, C1, C2);
            swarm.updatePosition();
            swarm.evaluateSubSwarm();

            currentIW = MAX_IW - (MAX_IW -MIN_IW)
                        * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();

            Utility.cur_generation++;

            recordVariables(swarm);
        }
        /* Input best particle for Output */
        Utility.stack_hist_best_fit[run] = swarm.getLbest_fitness();
        //Utility.printFinalBest(run, swarm);
        writeFiles(swarm, run);
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
        makeResultFile();
    }

    private void writeFiles(Swarm swarm, int run){
        if (!Utility.csvOutputFlg) {
            return;
        }
        if (Utility.outputParameterFlg) {
            writeParameterFiles();
        }
        if (Utility.outputScatterFlg) {
            writeScatterFiles();
        }
        writeResultFile(swarm, run);

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

    private void makeResultFile(){
        /* Make Folder */
        String className = new Object(){}.getClass().getEnclosingClass().getName();
        folderPassProb = Utility.OUTPUT_PATH + Utility.date + "/" + className +
                "/" + ProblemUtil.getProbID_Name();
        File newfile = new File(folderPassProb);
        newfile.mkdirs();
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
                Utility.writeValue(pw, C1);
                Utility.writeValue(pw, C2);

                pw.println();
            }
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void writeResultFile(Swarm swarm, int run){
        try {
            FileWriter fw = new FileWriter(folderPassProb + "/result.txt", true); //追記モード
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            pw.println(Utility.returnLine());

            pw.println("Run" + run);
            pw.println("Fitness =" + swarm.getLbest_fitness() );

            Particle bestPar = swarm.particles.get(swarm.getLbest_index());
            pw.print("Hist_best_position = ");
            for (double dim_pos : bestPar.hist_best_pos) {
                pw.print(Utility.returnShortNum(dim_pos) + " ");
            }
            pw.println();

            pw.println(Utility.returnLine());
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
