package Bot;

import Model.FrictionRKSolver;
import Model.RKSolver;
import Model.Solver;
import Model.Vector2d;
import com.mygdx.game.PuttingCourse;
import com.mygdx.game.PuttingSimulator;
import java.util.Random;

public class RoughBot implements Bot {

	double startingFric = new Random().nextDouble()*2;
    Solver solver = new FrictionRKSolver(startingFric);
    Solver actualSolver = new RKSolver();

    private static RoughBot  singleton = null;

    private RoughBot(){

    }
    public static RoughBot getInstance(){
        if(singleton == null){
            singleton = new RoughBot();
        }
        return singleton;
    }

    public Vector2d search(){
        WigerToods.getInstance().setSolver(solver);
        Vector2d diff = PuttingCourse.getInstance().get_flag_position().absDifference(PuttingSimulator.getInstance().get_ball_position());
        Vector2d shotVel;
        if(WigerToods.getInstance().maxDistance < PuttingSimulator.getInstance().get_ball_position().absDifference(PuttingCourse.getInstance().get_flag_position()).evaluateVector()){
        //max power shot
        Vector2d dummy = new Vector2d(15,15);
        shotVel = dummy.multiplyBy(diff);
        Vector2d firstShot = actualSolver.takeShot(PuttingSimulator.getInstance().get_ball_position(), shotVel);
        Vector2d testShot = solver.takeShot(PuttingSimulator.getInstance().get_ball_position(), shotVel);
        startingFric = startingFric/(firstShot.evaluateVector() / testShot.evaluateVector());
        solver = new FrictionRKSolver(startingFric);
        System.out.println(startingFric);
    }else{
           shotVel = WigerToods.getInstance().search();
           Vector2d firstShot = actualSolver.takeShot(PuttingSimulator.getInstance().get_ball_position(), shotVel);
           Vector2d testShot = solver.takeShot(PuttingSimulator.getInstance().get_ball_position(), shotVel);
           startingFric = startingFric/(firstShot.evaluateVector() / testShot.evaluateVector());
           solver = new FrictionRKSolver(startingFric);
           System.out.println(startingFric);
        }

    ///estimate friction.

        return shotVel;
    }

    @Override
    public void setSolver(Solver x) {
        this.actualSolver=x;
    }


}
