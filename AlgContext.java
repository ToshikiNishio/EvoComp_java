/**
 * Created by toshiki on 2017/07/17.
 */
public class AlgContext {
    private Algorithm algorithm;

    public AlgContext(Algorithm algorithm){
        setAlgorithm(algorithm);
    }

    public  void setAlgorithm(Algorithm algorithm){
        this.algorithm = algorithm;
    }

    public void run(int run){
        algorithm.run(run);
    }
}
