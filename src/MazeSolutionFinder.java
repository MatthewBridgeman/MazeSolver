import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MazeSolutionFinder {

	/* 
	 * I decided to go with a fairly simple solution where the program would try to go N, E, S then W, and continue doing this until it reaches a dead end.
	 * Once it reaches a dead end, it will back track and mark the location it just came from, and then attempt to go N, E, S or W again.
	 * The program will keep doing this until it either reaches the end location or it finds it's way back at the starting location with no possible way to go.
	 * It usually solves the mazes in under a second, and it is a pretty simple way of solving a maze.
	 */ 	
	
	public static void main(String[] args) {
		File file = GetFile(); // Gets the file input from the user
		long startTime = System.nanoTime(); // Grab the time we started to solve the maze
		Maze maze = LoadMazeData(file); // Loads the maze data
		SolveMaze(maze); // Solves the maze		
		long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime); // Grabs the time we finished the maze and converts to milliseconds
		
		// Print the solved maze to console
		if (maze.solved) {
		    System.out.println("Maze was solved in a total of " + maze.steps + " steps, in " + elapsedTime + " milliseconds.");
			for (int mazeY = 0; mazeY < maze.maze.size(); mazeY++) {
				String mazeLine = "";
				for (int mazeX = 0; mazeX < maze.maze.get(mazeY).size(); mazeX++) {
					mazeLine += maze.maze.get(mazeY).get(mazeX).toString();
				}
			    System.out.println(mazeLine);
			}
		} 
		else {
		    System.out.println("Maze could not be solved, we attempted " + maze.steps + " steps, in " + elapsedTime + " milliseconds.");
		}
	}

	// Asks the user to provide the maze file and returns a valid file
	public static File GetFile() {		
		Scanner filenameInput = new Scanner(System.in);
		Boolean fileLoaded = false;
		File file = null;
		
		// Keep asking for a filename until a valid one is selected
		while (!fileLoaded) {
			System.out.print("Enter filename to load: "); // Ask the user for the filename
			String filename = filenameInput.nextLine();
			if (!filename.contains(".txt")) {
				filename += ".txt"; // Check if the filename provided had .txt on the end, and add it if not
			}
			
			// Check to see if the file exists
			file = new File("Mazes\\" + filename);
			if (file.isFile()) {
				fileLoaded = true;		
			    System.out.println("File successfully loaded: " + filename + "\n");
			} else {
			    System.out.println("Error: No such file: " + filename + "\n");
			}
		}
		filenameInput.close();
		
		return file;
	}
	
	// Loads the maze data from the file
	public static Maze LoadMazeData(File file) {		
		// Create and initialize the variables for the maze
		ArrayList<ArrayList<Character>> mazeArray = new ArrayList<ArrayList<Character>>();
		Point startLocation = new Point(-1,-1);
		Point endLocation = new Point(-1,-1);
		Point mazeSize = new Point(-1,-1);
		
		// Start reading the maze file
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));

			int linePos = 0;
			String line;
			
			// Loop through every line in the maze file
			while((line = in.readLine()) != null)
			{	
				// Read the first line of the file containing maze size
		    	if (linePos == 0) {
		    		// As we're reading each number one by one, we're going to need to piece the full number together as a string and parse it to an int later.
	    			String mazeSizeXStr = "";
	    			String mazeSizeYStr = "";
	    			boolean spaceFound = false;
	    			
		    		// Loop through every other character in the line
		    		for (int charPos = 0; charPos < line.length(); charPos++) {	    			
		    			if (line.charAt(charPos) != ' ') { // If the character being read is not a space, then it is probably a number
		    				if (!spaceFound) { // If a space hasn't been found yet, we're still piecing together the first number
		    					mazeSizeXStr += line.charAt(charPos); // Add each number to the string 
		    				} 
		    				else {
		    					mazeSizeYStr += line.charAt(charPos);
		    				}
		    			} else {
		    				spaceFound = true; // If a space is found, we set this to true so that we start working on the second number
		    			}
		    		}

		    		// Set the maze size by parsing the strings into integers
		    		mazeSize.setLocation(Integer.parseInt(mazeSizeXStr), Integer.parseInt(mazeSizeYStr));
		    	}
		    	
		    	// Read the second line of the file containing start location
		    	if (linePos == 1) {
		    		// As we're reading each number one by one, we're going to need to piece the full number together as a string and parse it to an int later.
	    			String startLocationXStr = "";
	    			String startLocationYStr = "";
	    			boolean spaceFound = false;
	    			
		    		// Loop through every other character in the line
		    		for (int charPos = 0; charPos < line.length(); charPos++) {	    			
		    			if (line.charAt(charPos) != ' ') { // If the character being read is not a space, then it is probably a number
		    				if (!spaceFound) { // If a space hasn't been found yet, we're still piecing together the first number
		    					startLocationXStr += line.charAt(charPos); // Add each number to the string 
		    				} 
		    				else {
		    					startLocationYStr += line.charAt(charPos);
		    				}
		    			} else {
		    				spaceFound = true; // If a space is found, we set this to true so that we start working on the second number
		    			}
		    		}

		    		// Set the start position by parsing the strings into integers
		    		startLocation.setLocation(Integer.parseInt(startLocationXStr), Integer.parseInt(startLocationYStr));
		    	}
		    	
		    	// Read the third line of the file containing end location
		    	if (linePos == 2) {
		    		// As we're reading each number one by one, we're going to need to piece the full number together as a string and parse it to an int later.
	    			String endLocationXStr = "";
	    			String endLocationYStr = "";
	    			boolean spaceFound = false;
	    			
		    		// Loop through every other character in the line
		    		for (int charPos = 0; charPos < line.length(); charPos++) {		    			
		    			if (line.charAt(charPos) != ' ') { // If the character being read is not a space, then it is probably a number
		    				if (!spaceFound) { // If a space hasn't been found yet, we're still piecing together the first number
		    					endLocationXStr += line.charAt(charPos); // Add each number to the string 
		    				} 
		    				else {
		    					endLocationYStr += line.charAt(charPos);
		    				}
		    			} else {
		    				spaceFound = true; // If a space is found, we set this to true so that we start working on the second number
		    			}
		    		}

		    		// Set the end position by parsing the strings into integers
		    		endLocation.setLocation(Integer.parseInt(endLocationXStr), Integer.parseInt(endLocationYStr));
		    	}
		    	
		    	// Read the forth line onwards of the file containing the maze itself
		    	if (linePos >= 3) {
		    		// Initialize the array for the individual maze line
		    		ArrayList<Character> mazeLine = new ArrayList<Character>();
		    		
		    		// Loop through every other character in the line, skipping the empty spaces.
		    		for (int charPos = 0; charPos < line.length(); charPos += 2) {
		    			
		    			// Read and replace the numbers in the maze with the appropriate characters
		    			if (line.charAt(charPos) == '0') {
				    		mazeLine.add(' ');
		    			}
		    			else if (line.charAt(charPos) == '1') {
				    		mazeLine.add('#');
		    			}
		    		}
		    		// If the new maze line array isn't empty, push to the main maze array.
		    		if (!mazeLine.isEmpty()) {
		    			mazeArray.add(mazeLine);
		    		}
		    	}
			    linePos++;
			}
			
			// Add the start and end locations to the maze array
			mazeArray.get(startLocation.y).set(startLocation.x, 'S');
			mazeArray.get(endLocation.y).set(endLocation.x, 'E');
			
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create the maze object and return it
		Maze maze = new Maze(mazeArray, mazeSize, startLocation, endLocation);
		return maze;
	}
	
	public static void SolveMaze(Maze maze) {
		// Initialize solution array and push starting location
		maze.solution.add(maze.startLocation);
		Point currentPos = maze.solution.get(maze.solution.size() - 1);
		boolean stuck = false;
		
		// While we are not at the end location, or are currently stuck with no where to go, do the following:
		while (!currentPos.equals(maze.endLocation) && !stuck) {
			// Check if we can go up by looking to see if we're not at the edge of the maze and there is a free space to move to (either an empty space ' ' or the end location 'E')
			if ((currentPos.y > 0) && ((maze.maze.get(currentPos.y - 1).get(currentPos.x) == ' ') || (maze.maze.get(currentPos.y - 1).get(currentPos.x) == 'E'))) {
			    maze.solution.add(new Point(currentPos.x, currentPos.y - 1)); // Push the new up position into the solution stack
				currentPos = maze.solution.get(maze.solution.size() - 1); // Set the current position to the latest position in the solution stack
				maze.maze.get(currentPos.y).set(currentPos.x, 'X'); // Set the current position in the maze to an X to indicate our path
				maze.steps++; // Iterate the number of steps we've done
			}
			// Check if we can go right by looking to see if we're not at the edge of the maze and there is a free space to move to (either an empty space ' ' or the end location 'E')
			else if ((currentPos.x < (maze.mazeSize.x - 1)) && ((maze.maze.get(currentPos.y).get(currentPos.x + 1) == ' ') || (maze.maze.get(currentPos.y).get(currentPos.x + 1) == 'E'))) {
			    maze.solution.add(new Point(currentPos.x + 1, currentPos.y)); // Push the new right position into the solution stack
				currentPos = maze.solution.get(maze.solution.size() - 1);
				maze.maze.get(currentPos.y).set(currentPos.x, 'X');
				maze.steps++;
			}
			// Check if we can go down by looking to see if we're not at the edge of the maze and there is a free space to move to (either an empty space ' ' or the end location 'E')
			else if ((currentPos.y < (maze.mazeSize.y - 1)) && ((maze.maze.get(currentPos.y + 1).get(currentPos.x) == ' ') || (maze.maze.get(currentPos.y + 1).get(currentPos.x) == 'E'))) {
			    maze.solution.add(new Point(currentPos.x, currentPos.y + 1)); // Push the new down position into the solution stack
				currentPos = maze.solution.get(maze.solution.size() - 1);
				maze.maze.get(currentPos.y).set(currentPos.x, 'X');
				maze.steps++;
			}
			// Check if we can go left by looking to see if we're not at the edge of the maze and there is a free space to move to (either an empty space ' ' or the end location 'E')
			else if ((currentPos.x > 0) && ((maze.maze.get(currentPos.y).get(currentPos.x - 1) == ' ') || (maze.maze.get(currentPos.y).get(currentPos.x - 1) == 'E'))) {
			    maze.solution.add(new Point(currentPos.x - 1, currentPos.y)); // Push the new left position into the solution stack
				currentPos = maze.solution.get(maze.solution.size() - 1);
				maze.maze.get(currentPos.y).set(currentPos.x, 'X');
				maze.steps++;
			}
			// We could not go in any direction and reached a dead end, so we need to back track
			else if (currentPos != maze.startLocation) {
				maze.maze.get(currentPos.y).set(currentPos.x, '@'); // Set the path we just came from to an @ symbol to indicate it is a path we have already taken 
				maze.solution.remove(maze.solution.size() - 1); // Pop the last positon we we're in from the solution stack
				currentPos = maze.solution.get(maze.solution.size() - 1); // Set the current position to the latest position in the solution stack
				maze.steps++;
			} 
			// We have no where else to go, end the loop as there is no solution
			else {
				stuck = true;
			}
		}
		
		// Clean up the maze by removing the @ symbols and replace them with empty spaces ' '
		if (currentPos.equals(maze.endLocation)) {
			maze.solved = true;
			
			for (int mazeY = 0; mazeY < maze.maze.size(); mazeY++) {
				for (int mazeX = 0; mazeX < maze.maze.get(mazeY).size(); mazeX++) {
					if (maze.maze.get(mazeY).get(mazeX) == '@') {
						maze.maze.get(mazeY).set(mazeX, ' ');
					}
				}
			}
			maze.maze.get(maze.endLocation.y).set(maze.endLocation.x, 'E'); // Reset where the end location is as it would have been replaced with an X
		}
	}
}