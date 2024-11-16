import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Task1 {

    // Метод для генерації масиву
    public static int[] generateArray(int size, int min, int max) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }
        return array;
    }

    // Метод для виведення масиву
    public static void printArray(int[] array) {
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    // Метод для обчислення суми через ThreadPool (Work Dealing)
    public static long pairSumWithThreadPool(int[] array, int numThreads) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Future<Long>[] results = new Future[numThreads];

        int chunkSize = (array.length - 1 + numThreads - 1) / numThreads; // Рахуємо пару між сусідніми елементами

        // Розподіляємо завдання між потоками
        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize + 1, array.length);

            results[i] = executor.submit(() -> {
                long sum = 0;
                for (int j = start; j < end - 1; j++) {
                    sum += array[j] + array[j + 1];
                }
                return sum;
            });
        }

        long totalSum = 0;
        // Обчислюємо результат з усіх потоків
        for (Future<Long> result : results) {
            totalSum += result.get();
        }

        executor.shutdown();
        return totalSum;
    }







    // Реалізація через ForkJoinPool (Work Stealing)
    static class PairSumTask extends RecursiveTask<Long> {
        private final int[] array;
        private final int start;
        private final int end;

        public PairSumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long sum = 0;
            // Попарно додаємо кожен елемент з його наступним
            for (int i = start; i < end - 1; i++) {
                sum += array[i] + array[i + 1];
            }
            return sum;
        }
    }

    public static long pairSumWithForkJoin(int[] array) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        PairSumTask task = new PairSumTask(array, 0, array.length);
        return forkJoinPool.invoke(task);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);

        // Введення даних від користувача
        System.out.print("Введіть кількість елементів масиву: ");
        int size = scanner.nextInt();
        System.out.print("Введіть початкове значення елементів: ");
        int min = scanner.nextInt();
        System.out.print("Введіть кінцеве значення елементів: ");
        int max = scanner.nextInt();

        // Генерація масиву
        int[] array = generateArray(size, min, max);

        // Виведення згенерованого масиву
        System.out.println("Згенерований масив:");
        printArray(array);

        // Виконання через ThreadPool
        int numThreads = Runtime.getRuntime().availableProcessors();
        long startTime = System.currentTimeMillis();
        long resultThreadPool = pairSumWithThreadPool(array, numThreads);
        long endTime = System.currentTimeMillis();
        System.out.println("ThreadPool (Work Dealing):");
        System.out.println("Сума попарних елементів: " + resultThreadPool);
        System.out.println("Час виконання: " + (endTime - startTime) + " мс");

        // Виконання через ForkJoinPool
        startTime = System.currentTimeMillis();
        long resultForkJoin = pairSumWithForkJoin(array);
        endTime = System.currentTimeMillis();
        System.out.println("ForkJoinPool (Work Stealing):");
        System.out.println("Сума попарних елементів: " + resultForkJoin);
        System.out.println("Час виконання: " + (endTime - startTime) + " мс");
    }
}
