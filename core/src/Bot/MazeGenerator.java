package Bot;

import Model.ObstacleBuilder;
import Model.Vector2d;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.*;

import javax.sound.sampled.Line;
import java.util.Collections;
import java.util.Arrays;
import java.util.LinkedList;

import static com.mygdx.game.PuttingCourse.*;
import static com.mygdx.game.PuttingCourse.*;

public class MazeGenerator {
	private static MazeGenerator singleton;
	private final int x;
	private final int y;
	private final int[][] maze;
	public static InfoObject mazeBlocks;
	public static final int BLOCK_SIZE = 3;
	
	static Array<ModelInstance> createMaze = null;
 
	public MazeGenerator(int x, int y) {
		this.x = x;
		this.y = y;
		maze = new int[this.x][this.y];
		generateMaze(0, 0);
		this.singleton = this;
	}
	
	public static MazeGenerator getInstance() {
		return singleton;
	}
 
	public void display() {
		mazeBlocks = new InfoObject();
		for (int i = 0; i < y; i++) {
			// draw the north edge
			for (int j = 0; j < x; j++) {
				System.out.print((maze[j][i] & 1) == 0 ? "+---" : "+   ");
				if ((maze[j][i] / 1) % 2 == 0) {
					mazeBlocks.addWall();
					mazeBlocks.addWall();
				}
				else {
					mazeBlocks.addWall();
					mazeBlocks.addCorridor();
				}
			}
			mazeBlocks.addWall();
			System.out.println("+");
			// draw the west edge
			for (int j = 0; j < x; j++) {
				System.out.print((maze[j][i] & 8) == 0 ? "|   " : "    ");
				if((maze[j][i] / 8) % 2 == 0) {
					mazeBlocks.addWall();
					mazeBlocks.addCorridor();
				}
				else {
					mazeBlocks.addCorridor();
					mazeBlocks.addCorridor();
				}
			}
			mazeBlocks.addWall();
			System.out.println("|");
		}
		// draw the bottom line
		for (int j = 0; j < x; j++) {
			System.out.print("+---");
			mazeBlocks.addWall();
			mazeBlocks.addWall();
		}
		mazeBlocks.addWall();
		System.out.println("+");
		
//		for(int i = 0; i<=16; i++) {
//			for (int j = 0; j <= 15; j++) {
//				System.out.print(mazeBlocks.maze[i][j].wall);
//			}
//			System.out.println(mazeBlocks.maze[i][16].wall);
//		}

//		mazeBlocks.getSteps();
//		mazeBlocks.getBotSteps();
//		mazeBlocks.printSteps();
	}
 
	private void generateMaze(int cx, int cy) {
		
		DIR[] dirs = DIR.values();
		Collections.shuffle(Arrays.asList(dirs));
		for (DIR dir : dirs) {
			int nx = cx + dir.dx;
			int ny = cy + dir.dy;
			if (between(nx, x) && between(ny, y)
					&& (maze[nx][ny] == 0)) {
				maze[cx][cy] |= dir.bit;
				maze[nx][ny] |= dir.opposite.bit;
				generateMaze(nx, ny);
			}
		}
	}
 
	private static boolean between(int v, int upper) {
		return (v >= 0) && (v < upper);
	}
 
	private enum DIR {
		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
		private final int bit;
		private final int dx;
		private final int dy;
		private DIR opposite;
 
		// use the static initializer to resolve forward references
		static {
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}
 
		private DIR(int bit, int dx, int dy) {
			this.bit = bit;
			this.dx = dx;
			this.dy = dy;
		}
	};

	public static Array<ModelInstance> createMaze(ModelBuilder modelBuilder){
		if(createMaze != null) {
			return createMaze;
		}
		Array<ModelInstance> result = new Array<ModelInstance>();
		PuttingCourse.getInstance().obstacles=new LinkedList<>();
		MazeGenerator maze = new MazeGenerator(8, 8);
		maze.display();
		for(int i = 0; i<InfoObject.maze.length; i++){
			for(int j=0; j< InfoObject.maze[0].length; j++){
				if(InfoObject.maze[i][j].wall){
					result.add(ObstacleBuilder.makeBox(new Vector2(i*BLOCK_SIZE,j*BLOCK_SIZE),BLOCK_SIZE,BLOCK_SIZE, modelBuilder));
					PuttingCourse.getInstance().obstacles.getLast().i=i;
					PuttingCourse.getInstance().obstacles.getLast().j=j;
				}
			}
		}
		PuttingCourse.getInstance().set_start_position(new Vector2d(BLOCK_SIZE+BLOCK_SIZE/2,BLOCK_SIZE+BLOCK_SIZE/2));
		PuttingCourse.getInstance().set_flag_position(new Vector2d(InfoObject.maze.length*BLOCK_SIZE-1.5*BLOCK_SIZE,InfoObject.maze[0].length*BLOCK_SIZE-1.5*BLOCK_SIZE));
		createMaze = result;
		return result;
	}
 
	public static void main(String[] args) {
		int x = args.length >= 1 ? (Integer.parseInt(args[0])) : 8;
		int y = args.length == 2 ? (Integer.parseInt(args[1])) : 8;

		MazeGenerator maze = new MazeGenerator(8,8);
		maze.display();

	}
 
}

