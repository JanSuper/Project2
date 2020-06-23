package Bot;

import Model.PhysicsEngine;
import Model.Vector2d;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.mygdx.game.Main;
import com.mygdx.game.Menu;
import com.mygdx.game.PuttingCourse;
import com.mygdx.game.PuttingSimulator;
import Model.RKSolver;
import Model.Solver;
import java.util.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static Bot.MazeGenerator.BLOCK_SIZE;

public class Search implements Bot {
    public PuttingCourse course;
    private PhysicsEngine engine;
    private double radius;
    private int[][] pathScore;
    private static Search singleton = null;
    private LinkedList<Node> choosen = new LinkedList<>();
    private int[] lastshot;


    private Search(PuttingCourse course, PhysicsEngine engine) {
        this.course = course;
        this.engine = engine;
        Vector2d start = new Vector2d( BLOCK_SIZE + 1,  BLOCK_SIZE + 1);
        Vector2d finish = new Vector2d(15 * BLOCK_SIZE + 1, 15*BLOCK_SIZE + 1);
        ArrayList<BlockInfo> steps= MazeGenerator.getInstance().mazeBlocks.steps;
        pathScore = new int[MazeGenerator.getInstance().mazeBlocks.maze.length][MazeGenerator.getInstance().mazeBlocks.maze[0].length];
        boolean[][] set = new boolean[pathScore.length][pathScore[0].length];
        int maxScore = steps.size()*2;
        for (int i = 0; i < pathScore.length; i++) {
            for (int j = 0; j < pathScore[0].length; j++) {
                pathScore[i][j]=maxScore;
            }
        }
        setScore(pathScore,15, 15,0, MazeGenerator.mazeBlocks.maze, set);

         lastshot = MazeGenerator.mazeBlocks.getBotSteps().get(MazeGenerator.mazeBlocks.getBotSteps().size()-1);
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

    private void setScore(int[][] pathScore, int i, int j, int score, BlockInfo[][] maze ,boolean[][] set){
        if(i>0&&j>0&&i<maze.length&&j<maze[0].length&&!set[i][j]&&!maze[i][j].wall) {
            pathScore[i][j] = score++;
            set[i][j]=true;
            setScore(pathScore, i + 1, j, score, maze,set);
            setScore(pathScore, i - 1, j, score, maze,set);
            setScore(pathScore, i, j + 1, score, maze,set);
            setScore(pathScore, i, j - 1, score, maze,set);
        }
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
        if(currentPosition.getY()<0||currentPosition.getX()<0||
                currentPosition.getX()>MazeGenerator.mazeBlocks.maze.length*BLOCK_SIZE||
                currentPosition.getY()>MazeGenerator.mazeBlocks.maze.length*BLOCK_SIZE) return 999;
        return pathScore[(int)currentPosition.getX()/BLOCK_SIZE][(int)currentPosition.getY()/BLOCK_SIZE];
    }

    public Vector2d search(){
    	if(lastShot()) {
        	return  WigerToods.getInstance().search();
        }
            PuttingCourse.getInstance().botUse=true;
            Node start = new Node(PuttingSimulator.getInstance().get_ball_position(), new Vector2d(15, 0), null);
            start.setCosth(getScore(start.getCoordAfterShot()));
        PuttingCourse.getInstance().reset();

        //    double standard = distanceToNext(start.getCoordAfterShot());
            for (int i = 2; i < 360; i += 2) {
                Vector2d firstShot = new Vector2d(15 * Math.cos(i * 180 / Math.PI), 15 * Math.sin(i * 180 / Math.PI));
                Node tmp = new Node(PuttingSimulator.getInstance().get_ball_position(), firstShot, null);
                tmp.setCosth(getScore(tmp.getCoordAfterShot()));
                PuttingCourse.getInstance().reset();

                if (tmp.getCosth() < start.getCosth()) start = tmp;
                //       else if(tmp.getCosth()==start.getCosth()&&distanceToNext(tmp.getCoordAfterShot())<standard) start = tmp;

            }

            PuttingCourse.getInstance().botUse=false;
            choosen.add(start);
            PuttingCourse.getInstance().reset();
            PuttingCourse.getInstance().lasthit = null;
            return start.getLastShot();


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


    @Override
    public void setSolver(Solver x) {
        this.engine =x;
    }

    private double distance(Vector2d n, Vector2d p){
        double x = (n.getX()-p.getX()), y = (n.getY()-p.getY());
        return Math.sqrt(x*x+y*y);
    }

    public static Search getInstance(){
        if(singleton==null) singleton = new Search(PuttingCourse.getInstance(),new RKSolver());
        return singleton;
    }

    public Vector2d nextShot(){
        if( choosen.size()>0) return choosen.pop().getLastShot();
        return null;
    }

    public static void main(String[] args){
        new MazeGenerator(8,8);
        MazeGenerator.getInstance().display();
        Vector2d start = new Vector2d( BLOCK_SIZE + 1,  BLOCK_SIZE + 1);
        Vector2d finish = new Vector2d(15 * BLOCK_SIZE + 1, 15*BLOCK_SIZE + 1);

        Search search = new Search(PuttingCourse.getInstance(), Main.getInstance().getSolver());
        int[][] pathScore = search.getPathScore();

    }
    
    public boolean lastShot() {
    	Vector2d curPos = PuttingSimulator.getInstance().get_ball_position();
    	Vector2d flag = PuttingCourse.getInstance().get_flag_position();
    	Vector2d from = new Vector2d(lastshot[0]*BLOCK_SIZE + 1 + 0.5f, lastshot[1]*BLOCK_SIZE + 1 + 0.5f);
    	Vector2d to = new Vector2d(lastshot[2]*BLOCK_SIZE + 1 + 0.5f, lastshot[3]*BLOCK_SIZE + 1 + 0.5f);
    	return	(	
    				(from.getX() - 1f <= curPos.getX()) && (curPos.getX() <= to.getX() + 1f )&&
    				(from.getY() - 1f <= curPos.getY()) && (curPos.getY() <= to.getY() + 1f )
    			) && curPos.absDifference(flag).evaluateVector() < WigerToods.getInstance().maxDistance;
    }
}

