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
    int    SUB_SWARM_SIZE = 20;
    /* Variable definition */
    String folderName;
    double currentIW;
    /* Record variables */
    public ArrayList<Integer> recordGeneration =new ArrayList<Integer>();
    public ArrayList<Integer> recordEvalTimes =new ArrayList<Integer>();
    public ArrayList<Double> recordInertiaWeight =new ArrayList<Double>();
    public ArrayList<Double> recordBestFitness =new ArrayList<Double>();
    public ArrayList<SubSwarm> recordPosition = new ArrayList<>();
    public ArrayList<Integer> recordGInd = new ArrayList<>();

    public void run(int run){
        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;
        currentIW = MAX_IW;

        makeFiles(run);

        SubSwarm sub1 = new SubSwarm(SUB_SWARM_SIZE);
        recordVariables(sub1);
        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            sub1.updateVelocity(currentIW, C1, C2);
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

            currentIW = MAX_IW - (MAX_IW -MIN_IW)
                        * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();

            Utility.cur_generation++;

            recordVariables(sub1);
        }
        /* Input best particle for Output */
        Utility.stack_hist_best_fit[run] = sub1.getLbest_fitness();
        //Utility.printFinalBest(run, sub1);
        writeFiles();
    }

    /*
    * Follow method are to make csv files.
    * */
    private void makeFiles(int run){
        makeParameterFiles(run);
        makeScatterFiles(run);
    }

    private void writeFiles(){
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

    private void recordVariables(SubSwarm swarm){
        recordGeneration.add(Utility.cur_generation);
        recordEvalTimes.add(Utility.cur_func_eval);
        recordInertiaWeight.add(currentIW);
        recordBestFitness.add(swarm.getLbest_fitness());
        recordGInd.add(swarm.getLbest_index());
        /* Record swarm to record positions */
        SubSwarm tmp_swrm = new SubSwarm(0);
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
            int size = recordPosition.size();
            for (int ind =0; ind < size; ind++) {
                int par_ind = 0;
                for (Particle par : recordPosition.get(ind).particles) {
                    pw.print(recordGeneration.get(ind));
                    pw.print(",");
                    pw.print(recordEvalTimes.get(ind));
                    pw.print(",");
                    pw.print(recordGInd.get(ind));
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
