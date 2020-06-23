package Bot;

import Model.RKSolver;
import Model.Solver;
import Model.Vector2d;
import com.mygdx.game.Main;

public class Node {
    private static Solver solver=null;
    private static int counter =1;
    private int id;
    private Vector2d coordBeforeShot;//before the shot
    private int costG;//number of shot taken
    private double costh;//distance to the flag
    private Node parent;
    private Vector2d coordAfterShot;
    private Vector2d lastShot;


    /**
     * first constructor
     * @param start position of the ball at the start
     * @param shot hole position
     */
    public Node(Vector2d start, Vector2d shot, Node parent){
        if(solver==null) solver = (Solver)new RKSolver();
        this.coordBeforeShot = start;
        this.parent=parent;
        if(parent==null) this.costG=1;
        else this.costG=parent.getCostG()+1;
        this.lastShot=shot;
        this.coordAfterShot = solver.takeShot(start, shot);
        this.id=counter++;
    }


    /**
     * accessor for coordinates
     * @return x and y position
     */
    public Vector2d getCoordBeforeShot() {
        return coordBeforeShot;
    }

    /**
     * accessor for cost function
     * @return cost of node represent the distance to the flag
     */
    public int getCostG() {
        return costG;
    }

    /**
     * accessor for cost function
     * @return cost of node represent how close it is to the flag
     */
    public double getCosth() {
        return costh;
    }

    /**
     * setter method
     * @param costh the score that represent how close it is to the flag
     */
    public void setCosth(double costh) {
        this.costh = costh;
    }

    /**
     * accessor for last shot
     * @return the vector of the shot
     */
    public Vector2d getLastShot() {
        return lastShot;
    }

    /**
     * accessor for last shot
     * @return the position after the shot
     */
    public Vector2d getCoordAfterShot(){
        return this.coordAfterShot;
    }

    public int getId() {
        return id;
    }

    public String toString(){
        if(parent != null)
            return "id: "+this.id+" - coming from: "+parent.getId()+" - end up at coord: "+this.coordAfterShot;
        else
            return "id: "+this.id+" - Starting node - end up at coord: "+this.coordAfterShot;
    }


}
