package Bot;

import Model.*;

public class WigerToods {
    String function;
    Vector2d startPos;
    Solver solver;


    public Vector2d search(){
        Vector2d testFin = startPos.clone();
        Vector2d testVelocity =
        while(true) {//difference within a tolerance from the goalPosition

            testFin= solver.takeShot(velocity);
        }
    }
    public Vector2d calibrateVel(Vector2d prevVel, Vector2d prevFin){
        return prevVel+(prevVel*((solver.goalPosition)))
    }

    public static void main (String [] arg){

    }
}
