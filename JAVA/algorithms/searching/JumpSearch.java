public class JumpSearch {

    public static int jumpSearch(int[] arr, int target) {
        int n = arr.length;

        // Find optimal jump size
        int step = (int) Math.sqrt(n);
        int prev = 0;

        // Jump through the array
        while (prev < n && arr[Math.min(step, n) - 1] < target) {
            prev = step;
            step += (int) Math.sqrt(n);

            if (prev >= n) {
                return -1;
            }
        }

        // Linear search in the identified block
        while (prev < Math.min(step, n)) {
            if (arr[prev] == target) {
                return prev;
            }
            prev++;
        }

        return -1;
    }
}