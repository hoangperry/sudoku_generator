import java.io.*;
import java.util.*;

public class SudokuGenerator {

	public static class LatinSquare {
		public ArrayList<ArrayList<Integer>> latinSquareData;

		public LatinSquare(int size) {
			this.latinSquareData = new ArrayList<ArrayList<Integer>>();
			Random rand = new Random();
			ArrayList<Integer> negativeArr = new ArrayList<Integer>();
			ArrayList<ArrayList<Integer>> allPermute = permute(size);
			for(int i = 0; i < size; i++)
				negativeArr.add(-1);
			for(int i = 0; i < size; i++)
				this.latinSquareData.add(negativeArr);
			for(int i = 0; i < size; i++){
				ArrayList<Integer> tmpRow = allPermute.get(rand.nextInt(allPermute.size()));
				while(!validInput(this.latinSquareData, tmpRow, i)){
					tmpRow = allPermute.get(rand.nextInt(allPermute.size()));			
				}
				this.latinSquareData.set(i, tmpRow);
			}
		}

		public static ArrayList<ArrayList<Integer>> permute(int size) {
			int[] nums = new int[size];
			for(int i = 0; i < size; i++){
				nums[i] = i;
			}
			ArrayList<ArrayList<Integer>> result = new ArrayList<>();
			permute(0, nums, result);
			return result;
		}
		 
		private static void permute(int start, int[] nums, ArrayList<ArrayList<Integer>> result){
			if(start==nums.length-1){
				ArrayList<Integer> list = new ArrayList<>();
				for(int num: nums){
					list.add(num);
				}
				result.add(list);
				return;
			}
		 
			for(int i=start; i<nums.length; i++){
				swap(nums, i, start);
				permute(start+1, nums, result);
				swap(nums, i, start);
			}
		}
		 
		private static void swap(int[] nums, int i, int j){
			int temp = nums[i];
			nums[i] = nums[j];
			nums[j] = temp;
		}

		private static boolean validInput(ArrayList<ArrayList<Integer>> latinSquare, ArrayList<Integer> input, int row) {
			for(int i = 0; i < latinSquare.size(); i++){
				if(i == row)
					continue;
				for(int j = 0; j < latinSquare.size(); j++){
					if(latinSquare.get(i).get(j) == input.get(j))
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

		public boolean equals(LatinSquare obj){
			if(this.latinSquareData.size() != obj.latinSquareData.size())
				return false;
			for(int i = 0; i < this.latinSquareData.size(); i++){
				for(int j = 0; j < this.latinSquareData.size(); j++){
					if(this.latinSquareData.get(i).get(j) == obj.latinSquareData.get(i).get(j))
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
			for(int i = 0; i < size; i++){
				this.sudokuData.add(new ArrayList<LatinSquare>());
				for (int j = 0; j < size; j++) {
					LatinSquare tmpLS = new LatinSquare(size);
					while(!checkUniqueLS(this.sudokuData, tmpLS))
						tmpLS = new LatinSquare(size);
					this.sudokuData.get(i).add(tmpLS);
				}
			}
			
			weightLS = new LatinSquare(size);
			convertToDemBase();
		}

		public static boolean checkUniqueLS(ArrayList<ArrayList<LatinSquare>> sdk, LatinSquare input){
			
			for (int i = 0; i < sdk.size(); i++) {
				for (int j = 0; j < sdk.get(i).size(); j++) {
					if(sdk.get(i).get(j).equals(input))
						return false;
				}
			}
			return true;
		}

		public void convertToDemBase(){
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for (int j = 0; j < this.sudokuData.size(); j++) {
					for(int k = 0; k < this.sudokuData.size(); k++){
						for(int m = 0; m < this.sudokuData.size(); m++){
							this.sudokuData.get(i).get(j).latinSquareData.get(k).set(m, this.sudokuData.get(i).get(j).latinSquareData.get(k).get(m) + this.weightLS.latinSquareData.get(i).get(j) * this.sudokuData.size() + 1); 
						}
					}
				}
			}
		}

		public void swapLine(){

		}

		public void print(){
			String horizontalLine = "-------";
			for (int i = 0; i < this.sudokuData.size(); i++) {
				for(int j = 0; j < this.sudokuData.size(); j++){
					for(int k = 0; k < this.sudokuData.size(); k++){
						for(int m = 0; m < this.sudokuData.size(); m++){
							System.out.print(this.sudokuData.get(i).get(k).latinSquareData.get(m).get(j) + " ");
						}
						System.out.print("| ");
					}
					System.out.println();
				}
				for(int m = 0; m < this.sudokuData.size(); m++){
					System.out.print(horizontalLine);
				}
				System.out.println("----");
			}
		}
	}

	public static void main(String args[]) {
		SudokuBlock a = new SudokuBlock(5);
		a.print();
	}
}