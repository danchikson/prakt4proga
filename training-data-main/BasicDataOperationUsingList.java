import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Клас BasicDataOperationUsingList надає методи для виконання основних операцій з даними типу short.
 */
public class BasicDataOperationUsingList {
    static final String PATH_TO_DATA_FILE = "list/short.data";

    short valueToSearch;
    short[] shortArray;
    List<Short> shortList;

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Введіть значення для пошуку у вигляді аргументу командного рядка.");
        }

        BasicDataOperationUsingList operation = new BasicDataOperationUsingList(Short.parseShort(args[0]));
        operation.performOperations();
    }

    public BasicDataOperationUsingList(short valueToSearch) {
        this.valueToSearch = valueToSearch;
        this.shortArray = Utils.readArrayFromFile(PATH_TO_DATA_FILE);
        this.shortList = Arrays.stream(shortArray)  // преобразуем массив в список
                                .boxed()
                                .collect(Collectors.toList());
    }

    public void performOperations() {
        // Вивести поточну дату і час перед кожною операцією
        printCurrentDateTime(); 

        System.out.println("Операції з масивом:");

        measureTime("пошук у масиві", this::searchInArray);
        printCurrentDateTime();  // Печать текущего времени перед следующей операцією

        measureTime("пошук мінімального і максимального значення у масиві", this::findMinAndMaxInArray);
        printCurrentDateTime(); 

        measureTime("сортування масиву", this::sortArray);
        printCurrentDateTime(); 

        measureTime("пошук у масиві після сортування", this::searchInArray);
        printCurrentDateTime(); 

        measureTime("пошук мінімального і максимального значення у масиві після сортування", this::findMinAndMaxInArray);
        printCurrentDateTime(); 

        System.out.println("\nОперації з списком:");

        measureTime("пошук у списку", this::searchInList);
        printCurrentDateTime(); 

        measureTime("пошук мінімального і максимального значення у списку", this::findMinAndMaxInList);
        printCurrentDateTime(); 

        measureTime("сортування списку", this::sortList);
        printCurrentDateTime(); 

        measureTime("пошук у списку після сортування", this::searchInList);
        printCurrentDateTime(); 

        measureTime("пошук мінімального і максимального значення у списку після сортування", this::findMinAndMaxInList);
        printCurrentDateTime(); 

        Utils.writeArrayToFile(shortArray, PATH_TO_DATA_FILE + ".sorted");
    }

    private void measureTime(String operationName, Runnable operation) {
        long startTime = System.nanoTime();
        operation.run();
        long endTime = System.nanoTime();
        Utils.printOperationDuration(startTime, operationName, endTime);
    }

    private void searchInArray() {
        int index = Arrays.binarySearch(shortArray, valueToSearch);
        if (index >= 0) {
            System.out.println("Значення '" + valueToSearch + "' знайдено в масиві за індексом: " + index);
        } else {
            System.out.println("Значення '" + valueToSearch + "' у масиві не знайдено.");
        }
    }

    private void findMinAndMaxInArray() {
        if (shortArray.length == 0) {
            System.out.println("Масив порожній.");
            return;
        }

        short min = Arrays.stream(shortArray).min().orElse((short) 0);
        short max = Arrays.stream(shortArray).max().orElse((short) 0);

        System.out.println("Мінімальне значення в масиві: " + min);
        System.out.println("Максимальне значення в масиві: " + max);
    }

    private void sortArray() {
        Arrays.sort(shortArray);
    }

    private void searchInList() {
        int index = shortList.indexOf(valueToSearch);
        if (index >= 0) {
            System.out.println("Значення '" + valueToSearch + "' знайдено в списку за індексом: " + index);
        } else {
            System.out.println("Значення '" + valueToSearch + "' у списку не знайдено.");
        }
    }

    private void findMinAndMaxInList() {
        if (shortList.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }

        short min = shortList.stream().min(Short::compareTo).orElse((short) 0);
        short max = shortList.stream().max(Short::compareTo).orElse((short) 0);

        System.out.println("Мінімальне значення у списку: " + min);
        System.out.println("Максимальне значення у списку: " + max);
    }

    private void sortList() {
        shortList = shortList.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Метод для друку поточної дати і часу.
     */
    private void printCurrentDateTime() {
        String currentDateTime = Utils.getCurrentDateTime();
        System.out.println("Поточна дата і час: " + currentDateTime);
    }
}

/**
 * Клас Utils містить допоміжні методи для роботи з масивами і файлами.
 */
class Utils {
    public static short[] readArrayFromFile(String pathToFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            return reader.lines()
                    .mapToShort(Short::parseShort)
                    .toArray();
        } catch (IOException e) {
            throw new RuntimeException("Помилка читання файлу: " + e.getMessage(), e);
        }
    }

    public static void writeArrayToFile(short[] array, String pathToFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile))) {
            for (short value : array) {
                writer.write(String.valueOf(value));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Помилка запису у файл: " + e.getMessage(), e);
        }
    }

    /**
     * Метод для отримання поточної дати і часу.
     */
    public static String getCurrentDateTime() {
        return new Date().toString();
    }

    /**
     * Метод для виведення часу операції.
     */
    public static void printOperationDuration(long startTime, String operationName, long endTime) {
        long duration = endTime - startTime;
        System.out.printf(">>>>>>>>> Час виконання операції '%s': %d наносекунд%n", operationName, duration);
    }
}
