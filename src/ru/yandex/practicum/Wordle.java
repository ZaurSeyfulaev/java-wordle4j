package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.TechnicalException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;
import ru.yandex.practicum.util.Logger;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final int MAX_STEPS = 6;
    public static void main(String[] args) throws IOException, TechnicalException {

        Logger logger = new Logger("log.txt");
        WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
        WordleDictionary wordleDictionary = new WordleDictionary(loader, logger);
        WordleGame wordleGame = new WordleGame(wordleDictionary, logger);
        String userWord = "";
        System.out.println("=== Игра Wordle ===");
        System.out.println("Угадайте слово из 5 букв. Попыток: 6");
        System.out.println("+ : буква на месте");
        System.out.println("^ : буква есть в слове");
        System.out.println("- : буквы нет");
        System.out.println("Enter - подсказка,  тратит попытку)\n");
        logger.printLog("Игра началась");

        try (Scanner scanner = new Scanner(System.in)) {
            while (wordleGame.getSteps() < MAX_STEPS) {
                System.out.println("Осталось в запасе попыток " + (MAX_STEPS - wordleGame.getSteps()));
                System.out.println("Введите слово:");
                userWord = scanner.nextLine();
                logger.printLog("Пользователь ввел слово ==> " + userWord);
                if (wordleDictionary.isTrueWord(userWord)) {
                    System.out.println(wordleGame.getAnswer(userWord));
                } else if (userWord.isEmpty()) {
                    userWord = wordleGame.getHint();
                    System.out.println(userWord);
                    System.out.println(wordleGame.getAnswer(userWord));
                } else {
                    System.out.println("Введенное слово больше 5 символов или его нет в словаре");
                }
                if (userWord.equals(wordleGame.getAnswer())) {
                    System.out.println("Поздравляю! Слово угадано!");
                    logger.close();
                    break;
                } else if (wordleGame.getSteps() == MAX_STEPS) {
                    System.out.println("Вы проиграли");

                }
            }
        } catch (IOException | WordNotFoundInDictionary e) {
            logger.printLog(e.getMessage());
        } finally {
            logger.close();
        }
    }
}
