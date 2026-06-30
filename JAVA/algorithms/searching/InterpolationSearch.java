public class InterpolationSearch {

    public static int interpolationSearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high && target >= arr[low] && target <= arr[high]) {

            // If there is only one element left
            if (low == high) {
                if (arr[low] == target) {
                    return low;
                }
                return -1;
            }

            // Estimate the position
            int pos = low + ((target - arr[low]) * (high - low))
                    / (arr[high] - arr[low]);

            if (arr[pos] == target) {
                return pos;
            }

            if (arr[pos] < target) {
                low = pos + 1;
            } else {
                high = pos - 1;
            }
        }

        return -1;
    }
}