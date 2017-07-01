import java.util.ArrayList;

/**
 * Created by toshiki on 2017/07/02.
 */
public class SubSwarm {
    public ArrayList<Particle> particles =new ArrayList<Particle>();

    public SubSwarm(int createParticleNum) {
        for (int par_i = 0; par_i < createParticleNum; par_i++) {
            Particle tmp = new Particle();
            particles.add(tmp);
        }
    }

}
