import java.util.Arrays;

class Soultion {

	// brute force
	// Time Complexity -> O(N * log N)
	// -> O(Log N) -> sortign
	// -> O(N) -> traverse array
	public static int method1(int[] arr) {

		int n = arr.length;

		// sort array so that duplicates will be adjacent
		Arrays.sort(arr);

		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] == arr[i + 1]) {
				return arr[i];
			}
		}

		return -1;
	}

	// Time Complexity -> O(N)
	// Space Complexity -> O(N)
	public static int method2(int[] arr) {
		int n = arr.length;

		// freq array
		int[] freq = new int[n + 1];

		for (int i = 0; i < n; i++) {
			if (freq[arr[i]] != 0) {
				return arr[i];
			} else {
				freq[arr[i]]++;
			}
		}
		return -1;
	}

	// Time Complexity -> O(N) -> traverse array atmost once to find duplicate
	public static int method3(int[] arr) {
		int slow = arr[0];
		int fast = arr[0];

		do {
			slow = arr[slow];
			fast = arr[arr[fast]];
		} while (slow != fast);

		fast = arr[0];
		while (slow != fast) {
			slow = arr[slow];
			fast = arr[fast];
		}
		return slow;
	}

}

public class findDuplicate {
	public static void main(String[] args) {
		int arr[] = new int[] { 1, 3, 4, 2, 2 };
		System.out.println(Soultion.method3(arr));

	}

}
