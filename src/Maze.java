import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

public class Maze {

	ArrayList<ArrayList<Character>> maze;
	ArrayList<Point> solution;
	Point mazeSize;
	Point startLocation;
	Point endLocation;
	int steps;
	boolean solved;
	
	public Maze(ArrayList<ArrayList<Character>> maze, Point mazeSize, Point startLocation, Point endLocation) {
		this.maze = maze;
		solution = new ArrayList<Point>();
		this.mazeSize = mazeSize;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		steps = 0;
		solved = false;
	}
}