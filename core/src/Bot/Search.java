package Bot;

import Model.PhysicsEngine;
import Model.Vector2d;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.game.Main;
import com.mygdx.game.Menu;
import com.mygdx.game.PuttingCourse;
import java.util.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static Bot.MazeGenerator.BLOCK_SIZE;

public class Search {
    public PuttingCourse course;
    private PhysicsEngine engine;
    private double radius;
    private int[][] pathScore;

    /**
     * Constructor
     * @param course terrain on which we need to do the research
     * @param engine phyisics engine to simulate shots
     */
    public Search(PuttingCourse course, PhysicsEngine engine) {
        this.course = course;
        this.engine = engine;
    }
    /**
     * Constructor
     * @param course terrain on which we need to do the research
     * @param engine phyisics engine to simulate shots
     * @param steps making sure that the steps are generated with getSteps method
     */
    public Search(PuttingCourse course, PhysicsEngine engine, ArrayList<BlockInfo> steps) {
        this.course = course;
        this.engine = engine;
        pathScore = new int[MazeGenerator.getInstance().mazeBlocks.maze.length][MazeGenerator.getInstance().mazeBlocks.maze[0].length];
        int score = steps.size();
        for (int i = 0; i < pathScore.length; i++) {
            for (int j = 0; j < pathScore[0].length; j++) {
                pathScore[i][j]=score;
            }
        }
        for (BlockInfo bi : steps){
            pathScore = setScore(pathScore, bi.i,bi.j,--score);
        }
    }

    /**
     * set the score for the block of size BLOCK_SIZE with coordinates i and j and adjacent blocks that are NOT walls
     * @param pathScore
     * @param i
     * @param j
     * @param score
     * @return
     */
    private int[][] setScore(int[][] pathScore, int i, int j, int score){
        pathScore[i][j]=score;
        if(!MazeGenerator.getInstance().mazeBlocks.maze[i-1][j].wall) pathScore[i-1][j] =Math.min(score+2,pathScore[i-1][j]);
        if(!MazeGenerator.getInstance().mazeBlocks.maze[i+1][j].wall) pathScore[i+1][j] =Math.min(score+2, pathScore[i+1][j]);
        if(!MazeGenerator.getInstance().mazeBlocks.maze[i][j-1].wall) pathScore[i][j-1] =Math.min(score+2, pathScore[i][j-1]);
        if(!MazeGenerator.getInstance().mazeBlocks.maze[i][j+1].wall) pathScore[i][j+1] =Math.min(score+2,pathScore[i][j+1]);
        return pathScore;
    }

    private int[][] getPathScore() {
        return pathScore;
    }

    /**
     * return the score of the current position
     * @param currentPosition
     * @return score attributed to that coordinate
     */
    private int getScore(Vector2d currentPosition){
        return pathScore[(int)currentPosition.getX()/BLOCK_SIZE][(int)currentPosition.getY()/BLOCK_SIZE];
    }

    public LinkedList<Node> searchMaze(){
        Node start = new Node(course.get_start_position(), new Vector2d(15,0) ,null);
        start.setCosth(getScore(start.getCoordAfterShot()));
    //    double standard = distanceToNext(start.getCoordAfterShot());
        for (int i = 2; i < 360; i+=2) {
            Vector2d firstShot = new Vector2d(15*Math.cos(i/180*Math.PI),15*Math.sin(i/180*Math.PI));
            Node tmp = new Node(course.get_start_position(), firstShot, null);
            tmp.setCosth(getScore(tmp.getCoordAfterShot()));
            if(tmp.getCosth()<start.getCosth()) start=tmp;
     //       else if(tmp.getCosth()==start.getCosth()&&distanceToNext(tmp.getCoordAfterShot())<standard) start = tmp;

        }
        LinkedList<Node> choosen = new LinkedList<>();
        choosen.add(start);
        Node current = null;
        do{
            if (current != null) choosen.add(current);
            current = new Node(choosen.getLast().getCoordAfterShot(), new Vector2d(15,0) ,choosen.getLast());
            current.setCosth(getScore(current.getCoordAfterShot()));
            for (int i = 2; i < 360; i+=2) {
                Vector2d nextShot = new Vector2d(15*Math.cos(i/180*Math.PI),15*Math.sin(i/180*Math.PI));
                Node tmp = new Node(choosen.getLast().getCoordAfterShot(), nextShot, choosen.getLast());
                tmp.setCosth(getScore(tmp.getCoordAfterShot()));
                if(tmp.getCosth()<current.getCosth()) current=tmp;
            }

        }while(current.getCosth()<choosen.getLast().getCosth());

        /**
         * Needs last couple of shots because velocity may be too high
         */

        return choosen;
    }

    private double distanceToNext(Vector2d position){
        int i= (int)position.getX()/BLOCK_SIZE, j=(int)position.getY()/BLOCK_SIZE;
        double score = getScore(position);
        Vector2d centralPosition = new Vector2d((i+0.5)*BLOCK_SIZE, (j+0.5)*BLOCK_SIZE);//normalize position
        Vector2d tmp =centralPosition.cloneAndAdd(BLOCK_SIZE,0);
        if(getScore(tmp)<score) return position.difference(tmp);
        tmp =centralPosition.cloneAndAdd(0,BLOCK_SIZE);
        if(getScore(tmp)<score) return position.difference(tmp);
        tmp =centralPosition.cloneAndAdd(-BLOCK_SIZE,0);
        if(getScore(tmp)<score) return position.difference(tmp);
        tmp =centralPosition.cloneAndAdd(0,-BLOCK_SIZE);
        if(getScore(tmp)<score) return position.difference(tmp);
        return 1000;
    }

    public LinkedList<Node> search(){
        Node start = new Node(course.get_start_position(), course.get_flag_position() ,null);
        start.setCosth(start.getCoordAfterShot().difference(course.get_flag_position()));
        for (int i = 2; i < 360; i++) {
            Vector2d firstShot = new Vector2d(15*Math.cos(i/180*Math.PI),15*Math.sin(i/180*Math.PI));
            Node tmp = new Node(course.get_start_position(), firstShot, null);
            tmp.setCosth(tmp.getCoordAfterShot().difference(course.get_flag_position()));
            if(tmp.getCosth()<start.getCosth()) start=tmp;
        }
        LinkedList<Node> choosen = new LinkedList<>();
        choosen.add(start);
        Node current = null;
        do{
            if (current != null) choosen.add(current);
            current = new Node(choosen.getLast().getCoordAfterShot(), course.get_flag_position() ,choosen.getLast());
            current.setCosth(current.getCoordAfterShot().difference(course.get_flag_position()));
            for (int i = 2; i < 360; i+=2) {
                Vector2d nextShot = new Vector2d(15*Math.cos(i/180*Math.PI),15*Math.sin(i/180*Math.PI));
                Node tmp = new Node(choosen.getLast().getCoordAfterShot(), nextShot, choosen.getLast());
                tmp.setCosth(tmp.getCoordAfterShot().difference(course.get_flag_position()));
                if(tmp.getCosth()<current.getCosth()) current=tmp;
            }

        }while(current.getCosth()<choosen.getLast().getCosth());

        /**
         * Needs last couple of shots because velocity may be too high
         */

        return choosen;
    }

    private double distance(Vector2d n, Vector2d p){
        double x = (n.getX()-p.getX()), y = (n.getY()-p.getY());
        return Math.sqrt(x*x+y*y);
    }



    public static void main(String[] args){
        ModelBuilder mb = new ModelBuilder();
        PuttingCourse.getInstance().get_height().setFunction("0", true);
        //new MazeGenerator(8,8);
        MazeGenerator.createMaze(mb);
        MazeGenerator.getInstance().display();
        Vector2d start = new Vector2d( BLOCK_SIZE + 1,  BLOCK_SIZE + 1);
        Vector2d finish = new Vector2d(15 * BLOCK_SIZE + 1, 15*BLOCK_SIZE + 1);
        ArrayList<BlockInfo> steps= MazeGenerator.getInstance().mazeBlocks.getSteps(start, finish);

        Search search = new Search(PuttingCourse.getInstance(), Main.getInstance().getSolver(), steps);
        PuttingCourse.getInstance().set_flag_position(new Vector2d(15.5*BLOCK_SIZE, 15.5*BLOCK_SIZE));
        PuttingCourse.getInstance().set_start_position(new Vector2d(1.5*BLOCK_SIZE, 1.5*BLOCK_SIZE));
        int[][] pathScore = search.getPathScore();
//        System.out.println(search.course.obstacles.size()+"  grgrgr");


        /**
           * Here in the test, i find the coordinates of the score which would be 12 with a bit of randomness
           */
        Random r= new Random();
        Vector2d currentPosition=new Vector2d(0,0);
        for (int i = 0; i < pathScore.length; i++) {
            for (int j = 0; j < pathScore[0].length; j++) {
                if(pathScore[i][j]==12) currentPosition=new Vector2d(i*BLOCK_SIZE+1.5+(r.nextDouble()-0.5)*BLOCK_SIZE, j*BLOCK_SIZE+1.5+(r.nextDouble()-0.5)*BLOCK_SIZE);
            }
        }

        System.out.println(currentPosition);
        //return
       System.out.println("answer:"+pathScore[(int)currentPosition.getX()/BLOCK_SIZE][(int)currentPosition.getY()/BLOCK_SIZE]);

        for(int[] arr : pathScore) System.out.println(Arrays.toString(arr));

        LinkedList<Node> firstShots = search.searchMaze();
        for(Node n: firstShots) System.out.println(n);

    }
}

