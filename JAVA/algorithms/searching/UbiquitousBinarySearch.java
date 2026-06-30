public class UbiquitousBinarySearch {

    public static int ubiquitousBinarySearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;
            
            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        if (low < arr.length && arr[low] == target) {
            return low;
        }

        return -1;
    }
}