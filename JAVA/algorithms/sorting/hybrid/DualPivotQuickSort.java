public class DualPivotQuickSort {

    public static void dualPivotQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int[] pivots = partition(arr, low, high);

            dualPivotQuickSort(arr, low, pivots[0] - 1);
            dualPivotQuickSort(arr, pivots[0] + 1, pivots[1] - 1);
            dualPivotQuickSort(arr, pivots[1] + 1, high);
        }
    }

    private static int[] partition(int[] arr, int low, int high) {

        if (arr[low] > arr[high]) {
            swap(arr, low, high);
        }

        int leftPivot = arr[low];
        int rightPivot = arr[high];

        int lt = low + 1;
        int gt = high - 1;
        int i = low + 1;

        while (i <= gt) {

            if (arr[i] < leftPivot) {
                swap(arr, i, lt);
                lt++;
            } else if (arr[i] > rightPivot) {
                while (arr[gt] > rightPivot && i < gt) {
                    gt--;
                }

                swap(arr, i, gt);
                gt--;

                if (arr[i] < leftPivot) {
                    swap(arr, i, lt);
                    lt++;
                }
            }

            i++;
        }

        lt--;
        gt++;

        swap(arr, low, lt);
        swap(arr, high, gt);

        return new int[]{lt, gt};
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}