import java.util.Scanner;
import java.io.*;

class Oblig5 {
	
	public static void main(String[] args) {
		//new SudokuGUI(6, 2, 3);
		new SodokuContainer();
	}


}

class SodokuContainer {
	

	public SodokuContainer() {
		parseBoard();
	}

	public void parseBoard() {

		File file = new File("data.txt");

		try {
			Scanner scanner = new Scanner(file);
			Integer size = Integer.parseInt(scanner.nextLine());
			Integer rows = Integer.parseInt(scanner.nextLine());
			Integer columns = Integer.parseInt(scanner.nextLine());

			// Initialize all the squares based on column and row
			Square[][] squares = new Square[size][size];
			Integer[] boxCoords = {rows, columns};

			System.out.println("Size: "+size+" Rows: "+rows);

			int rowCount = 0;
			while (scanner.hasNextLine()) {
				String[] row = scanner.nextLine().split("");
				for (int i = 1; i < row.length; i++) {
					System.out.println(row[i]);
					squares[rowCount][i-1] = new Square(row[i]);
				}
				rowCount++;

			}

			System.out.println("Size: "+size+" Rows: "+rows+" Columns: "+squares);
			
			Board b = new Board(size, boxCoords, squares);

			b.solve();

		}
		catch(FileNotFoundException e) {

		}

	}

	public void insert() {

	}
	public void get() {

	}
	public void getSolutionCount() {

	}


}

class Board {
	Square squares[][];
	Integer[] boxCoords = Integer[2];

	Board(size, boxCoords, squares) {
		this.boxCoord = boxCoords;
		this.size = size;
		this.squares = squares;
	}

	public void solve() {

	} 

}

class Square {

	boolean filled = false;
	public String value;

	public Square(String val) {
		if(!val.equals(".")) {
			filled = true;
			value = val;
		}
	}
}




