import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by toshiki on 2017/06/24.
 */
public class EvolutionaryComputing extends Application{
    public static void main (String[] args) throws Exception{
        Application.launch();

        //run();
    }

    public static void run() {
        Utility.setMAX_FUNC_EVAL(200000);
        Utility.setDIMENSION(30);
        Utility.setRUN_MAX(1);
        AlgContext algContext = new AlgContext(new APSO());

        for (ProblemUtil.ProblemID problem : ProblemUtil.ProblemID.values()) {
            //if (problem.ordinal() < 12) {
            if (problem.ordinal() == 0) {
                ProblemUtil.setProblemID(problem);
                Utility.InitOutputPara();
                long start_time = System.currentTimeMillis();
                for (int run = 0; run < Utility.getRUN_MAX(); run++) {
                    Utility.setRandSeed(1234 + run);
                    algContext.run(run);
                }
                Utility.calcOutputPara();
                Utility.printOutputPara(problem);
                long end_time = System.currentTimeMillis();
                Utility.printHourMinuteSecond(end_time - start_time);
                System.out.println();
            }
        }
    }

    public void start(Stage stage){
        stage.setTitle("BestFitness");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Fitness");

        LineChart<Number, Number>
                lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("BestFitness");
        XYChart.Series<Number, Number> xyChartSeries = new XYChart.Series<>();
        xyChartSeries.setName("Fitness");

        //xyChartSeries.getData().add(new XYChart.Data<>(15, 22));
        //xyChartSeries.getData().add(new XYChart.Data<>(20, 31));
        run();
        for (int gen = 0; gen < APSO.BestFit.size(); gen++) {
            xyChartSeries.getData().add(new XYChart.Data<>(gen+1, APSO.BestFit.get(gen)));
        }

        lineChart.getData().add(xyChartSeries);

        HBox root = new HBox();
        root.getChildren().add(lineChart);
        Scene scene = new Scene(root, 560, 400);
        stage.setScene(scene);

        stage.show();
    }
}
