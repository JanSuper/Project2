package Bot;

import Model.FrictionRKSolver;
import Model.RKSolver;
import Model.Vector2d;
import com.mygdx.game.PuttingCourse;
import com.mygdx.game.PuttingSimulator;

import java.util.ArrayList;

public class RoughBot {

    FrictionRKSolver solver = new FrictionRKSolver(.3);
    RKSolver actualSolver = new RKSolver();

    public Vector2d solve(){
        WigerToods.getInstance().setSolver(solver);
       Vector2d diff = PuttingCourse.getInstance().get_flag_position().absDifference(PuttingSimulator.getInstance().get_ball_position());
        Vector2d shotVel;
        if(WigerToods.getInstance().maxDistance < PuttingSimulator.getInstance().get_ball_position().absDifference(PuttingCourse.getInstance().get_flag_position()).evaluateVector()){
        //max power shot
        Vector2d dummy = new Vector2d(15,15);
        shotVel = dummy.multiplyBy(diff);

    }else{
           shotVel = WigerToods.getInstance().search();
        }



        return shotVel;
    }



}
