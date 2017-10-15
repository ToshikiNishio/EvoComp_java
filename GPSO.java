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
    public GPSO(){
        printParameters();
    }

    /* Parameter Settings */
    double MAX_IW = 0.9;   //Maximum inertia weight
    double MIN_IW = 0.4;   //Minimum inertia weight
    double C1 = 2.0;       //Acceleration Coefficients 1
    double C2 = 2.0;       //Acceleration Coefficients 2
    int    SWARM_SIZE = 20;
    /* Variable definition */
    String folderName;
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
        writeFiles();
    }

    public void printParameters() {
        Utility.printLine();
        System.out.println("Maximum inertia weight = " + MAX_IW);
        System.out.println("Minimum inertia weight = " + MIN_IW);
        System.out.println("C1 = " + C1);
        System.out.println("C2 = " + C2);
        System.out.println("Swarm size = " + SWARM_SIZE);
        Utility.printLine();
        String className = new Object(){}.getClass().getEnclosingClass().getName();
        folderName = "/Users/toshiki/Output/" + Utility.date + "/" + className;
        File newfile = new File(folderName);
        newfile.mkdirs();
        /* Make Parameter setting file */
        try {
            FileWriter fw = new FileWriter(folderName + "/ParameterSetting.txt", false); //上書きモード
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
        makeParameterFiles(run);
        makeScatterFiles(run);
    }

    private void writeFiles(){
        if (!Utility.csvOutputFlg) {
            return;
        }
        writeParameterFiles();
        writeScatterFiles();

        clearRecord();
    }

    private void makeParameterFiles(int run){
        /* Make Folder */
        String className = new Object(){}.getClass().getEnclosingClass().getName();
        folderName = "/Users/toshiki/Output/" + Utility.date + "/" + className +
                                    "/" + ProblemUtil.getProb_obj() + "/RUN" + run;
        File newfile = new File(folderName);
        newfile.mkdirs();
        /* Make csv file */
        try {
            //Make csv file
            FileWriter fw = new FileWriter(folderName + "/output.csv", false); //上書きモード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("Generation");
            pw.print(",");
            pw.print("eval_times");
            pw.print(",");
            pw.print("Best_fitness");
            pw.print(",");
            pw.print("inertia_weight");
            pw.print(",");
            pw.print("c1");
            pw.print(",");
            pw.print("c2");
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
        folderName = "/Users/toshiki/Output/" + Utility.date + "/" + className +
                "/" + ProblemUtil.getProb_obj() + "/RUN" + run;
        File newfile = new File(folderName);
        newfile.mkdirs();
        /* Make csv file */
        try {
            //Make csv file
            FileWriter fw = new FileWriter(folderName + "/scatter.csv", false); //上書きモード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print("Generation");
            pw.print(",");
            pw.print("eval_times");
            pw.print(",");
            pw.print("G_index");
            pw.print(",");
            pw.print("individual_index");
            pw.print(",");
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                pw.print("X" + dim);
                pw.print(",");
            }
            for (int dim = 0; dim < Utility.getDIMENSION(); dim++) {
                pw.print("pBest" + dim);
                pw.print(",");
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
            FileWriter fw = new FileWriter(folderName + "/output.csv", true); //追記モード
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            int size = recordGeneration.size();
            for (int ind =0; ind < size; ind++) {
                pw.print(recordGeneration.get(ind));
                pw.print(",");
                pw.print(recordEvalTimes.get(ind));
                pw.print(",");
                pw.print(recordBestFitness.get(ind));
                pw.print(",");
                pw.print(recordInertiaWeight.get(ind));
                pw.print(",");
                pw.print(C1);
                pw.print(",");
                pw.print(C2);
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
            FileWriter fw = new FileWriter(folderName + "/scatter.csv", true); //追記モード
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            int generation = recordPosition.size();
            for (int gen =0; gen < generation; gen++) {
                int par_ind = 0;
                for (Particle par : recordPosition.get(gen).particles) {
                    pw.print(recordGeneration.get(gen));
                    pw.print(",");
                    pw.print(recordEvalTimes.get(gen));
                    pw.print(",");
                    pw.print(recordGInd.get(gen));
                    pw.print(",");
                    pw.print(par_ind++);
                    pw.print(",");
                    for (int dim = 0; dim < Utility.getDIMENSION(); dim++){
                        pw.print(par.positon[dim]);
                        pw.print(",");
                    }
                    for (int dim = 0; dim < Utility.getDIMENSION(); dim++){
                        pw.print(par.hist_best_pos[dim]);
                        pw.print(",");
                    }
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
