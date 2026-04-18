package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;
import ru.yandex.practicum.util.Logger;

import java.io.IOException;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private final HashMap<Integer, Character> exactMatchPositions = new HashMap<>();// Запоминаю индекс верной буквы слово
    private final List<Character> garbageLetters = new ArrayList<>(); // Для мусорных символов которых нет в слове
    private final List<Character> presentLetters = new ArrayList<>();// для всех символов которые есть в слове
    private final LinkedHashMap<String, String> set = new LinkedHashMap<>();
    private final Random random = new Random();
    private final WordleDictionary dictionary;
    private final String answer;
    private int steps = 0;
    private List<String> list;
    private Logger logger;

    public WordleGame(WordleDictionary dictionary, Logger logger) throws IOException {
        this.dictionary = dictionary;
        this.steps = 0;
        this.answer = dictionary.getRandomWord();
        this.list = new ArrayList<>();
        list = dictionary.getAllWords();
        this.logger = logger;
    }

    public void getHintWord(String userWord, char[] answer) {
        for (int i = 0; i < answer.length; i++) {
            if (answer[i] == '-') {
                garbageLetters.add(userWord.charAt(i));
            } else if (answer[i] == '+') {
                presentLetters.add(userWord.charAt(i));
                exactMatchPositions.put(i, userWord.charAt(i));
            } else {
                presentLetters.add(userWord.charAt(i));
            }
        }
    }

    public int getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAnswer(String userWord) throws IOException, WordNotFoundInDictionary {
        String resAnswer = dictionary.getHintString(userWord, answer);
        if (resAnswer.isEmpty()) {
            logger.printLog("Ошибка. Словарь пуст");
            throw new WordNotFoundInDictionary("Словарь пуст");
        }
        getHintWord(userWord, resAnswer.toCharArray());
        steps++;
        return resAnswer;
    }

    //Чистит список от слов, в которых нет ненужных букв
    private List<String> getGoodWords() throws IOException {
        boolean check;
        List<String> goodWords = new ArrayList<>();
        for (String word : list) {
            check = true;
            for (Character garbageLetter : garbageLetters) {
                if (word.indexOf(garbageLetter) != -1) {
                    check = false;
                    break;
                }
            }
            if (check) {
                goodWords.add(word);
            }
        }

        list = goodWords;
        logger.printLog("В списке getGoodWords осталось " + list.size() + " слов");
        return list;
    }

    // Метод возвращает список слов, в которых есть угаданные пользователем буквы
    private List<String> getWordsWithAllChars() throws IOException {
        List<String> result = new ArrayList<>();
        List<String> goodWords = getGoodWords();
        boolean check;
        for (String word : goodWords) {
            check = true;
            for (Character presentLetter : presentLetters) {
                if (word.indexOf(presentLetter) == -1) {
                    check = false;
                    break;
                }
            }
            if (check) {
                result.add(word);
            }
        }
        list = result;
        logger.printLog("В списке getWordsWithAllChars осталось " + list.size() + " слов");
        return list;
    }

    // Возвращаем список слов в которых есть буквы? которые угадал пользователь на угаданных индексах
    private List<String> getWordsWithExactPositions() throws IOException {
        List<String> result = new ArrayList<>();
        List<String> words = getWordsWithAllChars();
        boolean check;
        for (String word : words) {
            check = true;
            for (Map.Entry<Integer, Character> entry : exactMatchPositions.entrySet()) {
                if (word.charAt(entry.getKey()) != entry.getValue()) {
                    check = false;
                    break;
                }
            }
            if (check) {
                result.add(word);
            }
        }
        list = result;
        logger.printLog("В списке getWordsWithExactPositions осталось " + list.size() + " слов");
        return list;
    }

    public String getHint() throws IOException {
        List<String> hintList = getWordsWithExactPositions();
        if (hintList.isEmpty()) {
            return "";
        }
        return hintList.get(random.nextInt(hintList.size()));
    }
}