/**
 * Created by toshiki on 2017/07/17.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

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
    public void run(int run){
        Utility.cur_func_eval = 0;
        Utility.cur_generation = 0;

        SubSwarm sub1 = new SubSwarm(SUB_SWARM_SIZE);
        currentIW = MAX_IW;

        makeFiles(run);

        while (Utility.cur_func_eval < Utility.getMAX_FUNC_EVAL()) {
            sub1.updateVelocity(currentIW, C1, C2);
            sub1.updatePosition();
            sub1.evaluateSubSwarm();

            currentIW = MAX_IW - (MAX_IW -MIN_IW)
                        * Utility.cur_func_eval / Utility.getMAX_FUNC_EVAL();

            Utility.cur_generation++;
            writeFile(sub1);
        }
        /* Input best particle for Output */
        Utility.stack_hist_best_fit[run] = sub1.getLbest_fitness();
        //Utility.printFinalBest(run, sub1);
    }

    private void makeFiles(int run){
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
            pw.print("cur_fitness");
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

    private void writeFile(SubSwarm swarm){
        try {
            FileWriter fw = new FileWriter(folderName + "/output.csv", true); //追記モード
            //Write header
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.print(Utility.cur_generation);
            pw.print(",");
            pw.print(Utility.cur_func_eval);
            pw.print(",");
            pw.print(swarm.getLbest_fitness());
            pw.print(",");
            pw.print(currentIW);
            pw.print(",");
            pw.print(C1);
            pw.print(",");
            pw.print(C2);
            pw.println();
            //Close file
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
