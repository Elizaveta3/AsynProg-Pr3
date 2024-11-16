import java.io.File;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Task2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Зчитуємо директорію і формат файлів з консолі
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть шлях до директорії: ");
        String directoryPath = scanner.nextLine();
        System.out.print("Введіть розширення файлів (наприклад, .pdf): ");
        String extension = scanner.nextLine();

        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Вказаний шлях не є директорією!");
            return;
        }

        // Створюємо атомарну змінну для підрахунку файлів
        AtomicInteger fileCount = new AtomicInteger();

        // Використовуємо Fork/Join Framework для рекурсивної обробки директорій
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);


        // Викликаємо ForkJoinTask для пошуку файлів
        FileSearchTask task = new FileSearchTask(dir, extension, fileCount);
        forkJoinPool.submit(task);

        // Чекаємо завершення виконання
        task.get();

        // Виводимо результат
        System.out.println("Кількість файлів з розширенням " + extension + ": " + fileCount.get());

        // Закриваємо ForkJoinPool
        forkJoinPool.shutdown();
    }

    // Завдання для пошуку файлів у директорії
    static class FileSearchTask extends RecursiveTask<Void> {
        private final File directory;
        private final String extension;
        private final AtomicInteger fileCount;

        public FileSearchTask(File directory, String extension, AtomicInteger fileCount) {
            this.directory = directory;
            this.extension = extension;
            this.fileCount = fileCount;
        }

        @Override
        protected Void compute() {
            File[] files = directory.listFiles();
            if (files != null) {
                // Створюємо підзадачі для кожного файлу чи директорії
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Якщо це директорія, рекурсивно створюємо нову підзадачу
                        FileSearchTask task = new FileSearchTask(file, extension, fileCount);
                        task.fork();  // Запускаємо підзадачу асинхронно
                    } else if (file.getName().endsWith(extension)) {
                        // Якщо це файл з потрібним розширенням, збільшуємо лічильник
                        fileCount.incrementAndGet();
                    }
                }
            }
            return null;
        }
    }
}
