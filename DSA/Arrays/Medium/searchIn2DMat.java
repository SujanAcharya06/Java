
class Solution {

	// Optimal
	// Time complexity -> O(log(n * m))
	// Space Complexity -> O(1) -> using no extra space
	static boolean method(int[][] matrix, int target) {
		int n = matrix.length;
		int m = matrix[0].length;

		int left = 0;
		int right = n * m - 1;
		while (left <= right) {
			int mid = left + (right - left) / 2;
			int row = mid / m;
			int col = mid % m;
			int num = matrix[row][col];
			if (target == num) {
				return true;
			} else if (target < num) {
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		return false;
	}

}

public class searchIn2DMat {
	public static void main(String[] args) {
		int[][] matrix = {
				{ 1, 2, 3, 4 },
				{ 5, 6, 7, 8 },
				{ 9, 10, 11, 12 }
		};

		int target = 11;

		System.out.println(Solution.method(matrix, target));
	}

}
