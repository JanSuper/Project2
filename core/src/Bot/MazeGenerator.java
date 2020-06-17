package Bot;

import java.util.Collections;
import java.util.Arrays;

public class MazeGenerator {
	private final int x;
	private final int y;
	private final int[][] maze;
 
	public MazeGenerator(int x, int y) {
		this.x = x;
		this.y = y;
		maze = new int[this.x][this.y];
		generateMaze(0, 0);
	}
 
	public void display() {
		InfoObject mazeBlocks = new InfoObject();
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
		
		
		mazeBlocks.getSteps();
		mazeBlocks.getBotSteps();
		mazeBlocks.printSteps();
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
 
	public static void main(String[] args) {
		int x = args.length >= 1 ? (Integer.parseInt(args[0])) : 8;
		int y = args.length == 2 ? (Integer.parseInt(args[1])) : 8;
		MazeGenerator maze = new MazeGenerator(x, y);
		maze.display();

	}
 
}

