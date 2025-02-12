import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

class CaesarCipher {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            int choice = getUserChoice(scanner);

            if (choice == 0) {
                System.out.println("Выход из программы.");
                break;
            }

            String inputFile = getFilePath(scanner, "Введите путь к исходному файлу:");
            if (!Files.exists(Paths.get(inputFile))) {
                System.out.println("Ошибка: файл не найден!");
                continue;
            }

            String outputFile = getFilePath(scanner, "Введите путь для сохранения результата:");
            int key = getShiftKey(scanner);

            processFile(inputFile, outputFile, key, choice == 1);
        }

        scanner.close();
    }

    private static int getUserChoice(Scanner scanner) {
        while (true) {
            System.out.println("\nВыберите режим:");
            System.out.println("1 - Шифрование");
            System.out.println("2 - Расшифрование");
            System.out.println("0 - Выход");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice >= 0 && choice <= 2) {
                return choice;
            }
            System.out.println("Ошибка: некорректный выбор. Повторите попытку.");
        }
    }

    private static String getFilePath(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    private static int getShiftKey(Scanner scanner) {
        while (true) {
            System.out.println("Введите ключ (сдвиг):");
            int key = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера
            if (key >= 0) {
                return key;
            }
            System.out.println("Ошибка: ключ не может быть отрицательным!");
        }
    }

    private static void processFile(String inputFile, String outputFile, int key, boolean encrypt) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputFile));
            StringBuilder result = new StringBuilder();

            for (String line : lines) {
                result.append(transformText(line, key, encrypt)).append("\n");
            }

            Files.write(Paths.get(outputFile), result.toString().getBytes());
            System.out.println("Файл успешно обработан и сохранен: " + outputFile);
        } catch (IOException e) {
            System.out.println("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    private static String transformText(String text, int key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        int shift = encrypt ? key : -key;

        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(c);
            if (index != -1) {
                int newIndex = (index + shift + ALPHABET.length()) % ALPHABET.length();
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
