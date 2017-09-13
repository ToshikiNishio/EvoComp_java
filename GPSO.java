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

    public void run(int run){
        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;

        SubSwarm sub1 = new SubSwarm(SUB_SWARM_SIZE);
        currentIW = MAX_IW;

        makeOutputFiles(run);

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            sub1.updateVelocity(currentIW, C1, C2);
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

            currentIW = MAX_IW - (MAX_IW -MIN_IW)
                        * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();

            Utility.cur_generation++;

            recordVariables(sub1);
            //writeFile();
        }
        /* Input best particle for Output */
        Utility.stack_hist_best_fit[run] = sub1.getLbest_fitness();
        //Utility.printFinalBest(run, sub1);
        writeOutputFiles();
    }

    private void makeOutputFiles(int run){
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

    private void writeOutputFiles(){
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
        recordGeneration.clear();
        recordEvalTimes.clear();
        recordBestFitness.clear();
        recordInertiaWeight.clear();
    }

    private void recordVariables(SubSwarm swarm){
        recordGeneration.add(Utility.cur_generation);
        recordEvalTimes.add(Utility.cur_func_eval);
        recordInertiaWeight.add(currentIW);
        recordBestFitness.add(swarm.getLbest_fitness());
    }
}
