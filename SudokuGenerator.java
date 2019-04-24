import java.io.*;
import java.util.*;
import java.lang.Math;

public class SudokuGenerator {

	public static class LatinSquare {
		public ArrayList<ArrayList<Integer>> latinSquareData;

		public LatinSquare(int size) {
			this.latinSquareData = new ArrayList<ArrayList<Integer>>();
			Random rand = new Random();
			ArrayList<Integer> negativeArr = new ArrayList<Integer>();
			ArrayList<ArrayList<Integer>> allPermute = permute(size);
			for (int i = 0; i < size; i++)
				negativeArr.add(-1);
			for (int i = 0; i < size; i++)
				this.latinSquareData.add(negativeArr);
			for (int i = 0; i < size; i++) {
				ArrayList<Integer> tmpRow = allPermute.get(rand.nextInt(allPermute.size()));
				while (!validInput(this.latinSquareData, tmpRow, i)) {
					tmpRow = allPermute.get(rand.nextInt(allPermute.size()));
				}
				this.latinSquareData.set(i, tmpRow);
			}
		}

		public static ArrayList<ArrayList<Integer>> permute(int size) {
			int[] nums = new int[size];
			for (int i = 0; i < size; i++) {
				nums[i] = i;
			}
			ArrayList<ArrayList<Integer>> result = new ArrayList<>();
			permute(0, nums, result);
			return result;
		}

		private static void permute(int start, int[] nums, ArrayList<ArrayList<Integer>> result) {
			if (start == nums.length - 1) {
				ArrayList<Integer> list = new ArrayList<>();
				for (int num : nums) {
					list.add(num);
				}
				result.add(list);
				return;
			}

			for (int i = start; i < nums.length; i++) {
				swap(nums, i, start);
				permute(start + 1, nums, result);
				swap(nums, i, start);
			}
		}

		private static void swap(int[] nums, int i, int j) {
			int temp = nums[i];
			nums[i] = nums[j];
			nums[j] = temp;
		}

		private static boolean validInput(ArrayList<ArrayList<Integer>> latinSquare, ArrayList<Integer> input,
				int row) {
			for (int i = 0; i < latinSquare.size(); i++) {
				if (i == row)
					continue;
				for (int j = 0; j < latinSquare.size(); j++) {
					if (latinSquare.get(i).get(j) == input.get(j))
						return false;
				}
			}
			return true;
		}

		public void print() {
			for (ArrayList<Integer> i : this.latinSquareData) {
				for (Integer j : i) {
					System.out.print(j + " ");
				}
				System.out.println();
			}
		}

		public boolean equals(LatinSquare obj) {
			if (this.latinSquareData.size() != obj.latinSquareData.size())
				return false;
			for (int i = 0; i < this.latinSquareData.size(); i++) {
				for (int j = 0; j < this.latinSquareData.size(); j++) {
					if (this.latinSquareData.get(i).get(j) == obj.latinSquareData.get(i).get(j))
						return false;
				}
			}
			return true;
		}
	}

	public static class SudokuBlock {

		public ArrayList<ArrayList<LatinSquare>> sudokuData;
		public LatinSquare weightLS;

		public SudokuBlock(int size) {
			this.sudokuData = new ArrayList<ArrayList<LatinSquare>>();
			for (int i = 0; i < size; i++) {
				this.sudokuData.add(new ArrayList<LatinSquare>());
				for (int j = 0; j < size; j++) {
					LatinSquare tmpLS = new LatinSquare(size);
					while (!checkUniqueLS(this.sudokuData, tmpLS))
						tmpLS = new LatinSquare(size);
					this.sudokuData.get(i).add(tmpLS);
				}
			}

			weightLS = new LatinSquare(size);
			convertToDemBase();
			findLine();
		}

		public static boolean checkUniqueLS(ArrayList<ArrayList<LatinSquare>> sdk, LatinSquare input) {

			for (int i = 0; i < sdk.size(); i++) {
				for (int j = 0; j < sdk.get(i).size(); j++) {
					if (sdk.get(i).get(j).equals(input))
						return false;
				}
			}
			return true;
		}

		public void convertToDemBase() {
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for (int j = 0; j < this.sudokuData.size(); j++) {
					for (int k = 0; k < this.sudokuData.size(); k++) {
						for (int m = 0; m < this.sudokuData.size(); m++) {
							this.sudokuData.get(i).get(j).latinSquareData.get(k).set(m,
									this.sudokuData.get(i).get(j).latinSquareData.get(k).get(m)
											+ this.weightLS.latinSquareData.get(i).get(j) * this.sudokuData.size() + 1);
						}
					}
				}
			}
		}

		public void findLine() {
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for (int j = this.sudokuData.size(); j > i + 1; j--) {
					int src = j + (this.sudokuData.size() * i);
					int des = (j - 1) * (this.sudokuData.size()) + 1 + i;
					swapLine(src - 1, des - 1);
				}
			}
		}

		public void swapLine(int l1, int l2) {
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for (int j = 0; j < this.sudokuData.size(); j++) {
					int tmp = this.sudokuData.get((int) (l1 / this.sudokuData.size())).get(i).latinSquareData
							.get((l1 + 1) % this.sudokuData.size()).get(j);
					this.sudokuData.get((int) (l1 / this.sudokuData.size())).get(i).latinSquareData
							.get((l1 + 1) % this.sudokuData.size())
							.set(j, this.sudokuData.get((int) (l2 / this.sudokuData.size())).get(i).latinSquareData
									.get((l2 + 1) % this.sudokuData.size()).get(j));
					this.sudokuData.get((int) (l2 / this.sudokuData.size())).get(i).latinSquareData
							.get((l2 + 1) % this.sudokuData.size()).set(j, tmp);

				}
			}
		}

		public void printLine(int line) {
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for (int j = 0; j < this.sudokuData.size(); j++) {
					System.out.print(this.sudokuData.get((int) (line / this.sudokuData.size())).get(i).latinSquareData
							.get((line + 1) % this.sudokuData.size()).get(j) + " ");
				}
			}
			System.out.println();
		}

		public void print() {
			String horizontalLine = "-------";
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for (int j = 0; j < this.sudokuData.size(); j++) {
					for (int k = 0; k < this.sudokuData.size(); k++) {
						for (int m = 0; m < this.sudokuData.size(); m++) {
							System.out.print(this.sudokuData.get(i).get(k).latinSquareData.get(j).get(m) + " ");
						}
						System.out.print("| ");
					}
					System.out.println();
				}
				for (int m = 0; m < this.sudokuData.size(); m++) {
					System.out.print(horizontalLine);
				}
				System.out.println("----");
			}
		}

		public void writeToFile(String path) throws Exception {
			FileWriter out = new FileWriter(new File(path));
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for (int j = 0; j < this.sudokuData.size(); j++) {
					for (int k = 0; k < this.sudokuData.size(); k++) {
						for (int m = 0; m < this.sudokuData.size(); m++) {
							out.write(this.sudokuData.get(i).get(k).latinSquareData.get(j).get(m) + " ");
						}
					}
					out.write("\r\n");
				}
			}
			out.close();

		}

	}

	public static void digingHole(String inPath, String outPath, int numHole) throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(inPath));
		BufferedWriter out = new BufferedWriter(new FileWriter(outPath));

		String[] lines = in.readLine().split(" ");
		int[][] sudoku = new int[lines.length][lines.length];
		for (int i = 0; i < lines.length; i++) {
			for (int j = 0; j < lines.length; j++) {
				sudoku[i][j] = Integer.parseInt(lines[j]);
			}
			if (i == lines.length - 1)
				break;
			lines = in.readLine().split(" ");
		}

		Random rand = new Random();
		for (int i = 0; i < numHole; i++) {
			int row = rand.nextInt(sudoku.length);
			int col = rand.nextInt(sudoku.length);
			if (sudoku[row][col] != 0) {
				sudoku[row][col] = 0;
			} else {
				i--;
			}
		}
		for (int[] i : sudoku) {
			for (int j : i) {
				out.write(j + " ");
			}
			out.write("\r\n");
		}
		// System.out.println("\n=========================");
		// for (int i = 0; i < sudoku.length; i++) {
		// 	System.out.print("| ");
		// 	for (int j = 0; j < sudoku.length; j++) {
		// 		System.out.print(sudoku[i][j] + " ");
		// 		if((j+1)%3==0)
		// 			System.out.print("| ");
		// 	}
		// 	if((i+1)%3==0)
		// 		System.out.println("\n=========================");
		// 	else
		// 		System.out.println();
		// }

		// sudoku = solverSudoku(sudoku);


		// System.out.println("\n=========================");
		// for (int i = 0; i < sudoku.length; i++) {
		// 	System.out.print("| ");
		// 	for (int j = 0; j < sudoku.length; j++) {
		// 		System.out.print(sudoku[i][j] + " ");
		// 		if((j+1)%3==0)
		// 			System.out.print("| ");
		// 	}
		// 	if((i+1)%3==0)
		// 		System.out.println("\n=========================");
		// 	else
		// 		System.out.println();
		// }

		out.close();
		
	}

	// public static boolean checkValidSudoku(int[][] sudoku, int i, int j) {

	// 	for (int m = 0; m < sudoku.length; m++){
	// 		if(sudoku[i][j] == sudoku[i][m] && m != j && sudoku[i][m] != 0){
	// 			return false;
	// 		}
	// 		if(sudoku[i][j] == sudoku[m][j] && m != i && sudoku[m][j] != 0){
	// 			return false;
	// 		}
	// 	}				
	// 	int sizeOfBlock = (int)Math.sqrt(sudoku.length);
	// 	for (int m = (int)(i/sizeOfBlock); m < ((int)(i/sizeOfBlock) + sizeOfBlock); m++){
	// 		for(int n = (int)(i/sizeOfBlock); n < ((int)(i/sizeOfBlock) + sizeOfBlock); n++){
	// 			if(sudoku[m][n] == sudoku[i][j] && m != i && n != j){
	// 				return false;
	// 			}
	// 		}
	// 	}
	// 	return true;
	// }

	// public static int[][] solverSudoku(int[][] sudoku) {
	// 	int[][] ret = sudoku;
	// 	int[] pre = new int[sudoku.length * sudoku.length];
	// 	for (int i = 0; i < sudoku.length; i++) {
	// 		for (int j = 0; j < sudoku.length; j++) {
	// 			if(sudoku[i][j] == 0){
	// 				sudoku[i][j]++;
	// 				while(!checkValidSudoku(sudoku, i, j)){
	// 					sudoku[i][j]++;
	// 				}
	// 			}
	// 		}
	// 	}

	// 	return ret;
	// }

	public static void main(String args[]) throws Exception {
		SudokuBlock a = new SudokuBlock(3);
		a.writeToFile(args[0]);

		digingHole(args[0], args[1], Integer.parseInt(args[2]));
	}
}