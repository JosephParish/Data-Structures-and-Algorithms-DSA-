public class ExponentialSearch {

    public static int exponentialSearch(int[] arr, int target) {
        int n = arr.length;

        if (arr[0] == target) {
            return 0;
        }

        int i = 1;
        while (i < n && arr[i] <= target) {
            i *= 2;
        }

        return binarySearch(arr, i / 2, Math.min(i, n - 1), target);
    }
}