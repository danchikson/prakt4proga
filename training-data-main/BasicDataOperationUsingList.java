import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Date;

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
        this.shortList = new ArrayList<>();
        for (short value : shortArray) {
            shortList.add(value);
        }
    }

    public void performOperations() {
        // Вивести поточну дату і час перед кожною операцією
        printCurrentDateTime(); 

        System.out.println("Операції з масивом:");

        measureTime("пошук у масиві", this::searchInArray);
        printCurrentDateTime();  // Печать текущего времени перед следующей операцией

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
        int index = Utils.binarySearch(shortArray, valueToSearch);
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

        short min = shortArray[0];
        short max = shortArray[0];
        for (short value : shortArray) {
            if (value < min) min = value;
            if (value > max) max = value;
        }

        System.out.println("Мінімальне значення в масиві: " + min);
        System.out.println("Максимальне значення в масиві: " + max);
    }

    private void sortArray() {
        // вимiрюємо час, витрачений на сортування масиву
        long startTime = System.nanoTime();
        Arrays.sort(shortArray);
        long endTime = System.nanoTime();
        Utils.printOperationDuration(startTime, "сортування масиву", endTime);
    }

    private void searchInList() {
        int index = Collections.binarySearch(shortList, valueToSearch);
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

        short min = Collections.min(shortList);
        short max = Collections.max(shortList);

        System.out.println("Мінімальне значення у списку: " + min);
        System.out.println("Максимальне значення у списку: " + max);
    }

    private void sortList() {
        Collections.sort(shortList);
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
        List<Short> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                values.add(Short.parseShort(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Помилка читання файлу: " + e.getMessage(), e);
        }

        short[] result = new short[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
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

    public static void sort(short[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    short temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    public static int binarySearch(short[] array, short value) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] == value) {
                return mid;
            } else if (array[mid] < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Метод для отримання поточної дати і часу.
     */
    public static String getCurrentDateTime() {
        Date now = new Date();
        return now.toString();
    }

    /**
     * Метод для виведення часу операції.
     */
    public static void printOperationDuration(long startTime, String operationName, long endTime) {
        long duration = endTime - startTime;
        System.out.printf(">>>>>>>>> Час виконання операції '%s': %d наносекунд%n", operationName, duration);
    }
}
