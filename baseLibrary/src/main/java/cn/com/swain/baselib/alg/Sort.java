package cn.com.swain.baselib.alg;

/**
 * author Guoqiang_Sun
 * date 2019/8/15
 * desc
 */
public class Sort {

    /**
     * 插入排序
     */
    public static void insertSort(int[] a) {
        int i, j, v;
        int n = a.length;
        for (i = 1; i < n; i++) {
            v = a[i];
            j = i - 1;
            while (j >= 0 && a[j] > v) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = v;
        }
    }

    /**
     * 希尔排序
     */
    public static void xlSort(int[] a) {
        int h = 1;
        int n = a.length;
        while (h < n / 3) //1
            h = 3 * h + 1;
        while (h >= 1) {
            //增量为h的插入排序
            for (int i = h; i < n; i++) {
                int v = a[i];
                int j = i - h;
                while (j >= 0 && a[j] > v) {
                    a[j + h] = a[j];
                    j -= h;
                }
                a[j + h] = v;
            }
            h = h / 3;
        }
    }

    /**
     * 选择排序
     */
    public static void selectionSort(int[] a) {
        int i, j, min, v;
        int n = a.length;
        for (i = 0; i < n; i++) {
            //每次将未排序部分的首元素下标赋值给下标min
            min = i;
            //得到未排序部分的最小值的下标并赋值给min
            for (j = i + 1; j < n; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            v = a[i];
            a[i] = a[min];
            a[min] = v;
        }
    }

    /**
     * 冒泡排序
     */
    public static void bubbleSort(int[] a) {
        int i, j, v;
        int n = a.length;
        for (i = 0; i < n - 1; i++) {
            for (j = n - 1; j > i; j--) {
                if (a[j] < a[j - 1]) {
                    v = a[j];
                    a[j] = a[j - 1];
                    a[j - 1] = v;
                }
            }
        }
    }

    ////////////////////////////////////////////

    public static void main(String[] args) {
        int[] b = {4, 8, 9, 1, 10, 6, 2, 5};
        int[] a = new int[b.length];
        long start;

        System.arraycopy(b, 0, a, 0, b.length);
        printArray("insertSort", a);
        start = System.nanoTime();
        insertSort(a);
        System.out.println("insertSort : " + (System.nanoTime() - start));
        printArray("insertSort", a);

        System.arraycopy(b, 0, a, 0, b.length);
        printArray("xlSort", a);
        start = System.nanoTime();
        xlSort(a);
        System.out.println("xlSort : " + (System.nanoTime() - start));
        printArray("xlSort", a);

        System.arraycopy(b, 0, a, 0, b.length);
        printArray("selectionSort", a);
        start = System.nanoTime();
        selectionSort(a);
        System.out.println("selectionSort : " + (System.nanoTime() - start));
        printArray("selectionSort", a);

        System.arraycopy(b, 0, a, 0, b.length);
        printArray("bubbleSort", a);
        start = System.nanoTime();
        bubbleSort(a);
        System.out.println("bubbleSort : " + (System.nanoTime() - start));
        printArray("bubbleSort", a);


    }

    private static void printArray(String head, int[] array) {
        System.out.print(head + "{");
        int len = array.length;
        for (int i = 0; i < len; i++) {
            System.out.print(array[i]);
            if (i < len - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
}
