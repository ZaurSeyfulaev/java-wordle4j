package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.TechnicalException;
import ru.yandex.practicum.util.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    private static final int MAX_WORD_LENGTH = 5;
    private static final String wordsDictionary = "words_ru.txt";
    Logger logger;

    public WordleDictionaryLoader(Logger logger) {
        this.logger = logger;
    }

    public static String normalizeWord(String word) {
        if (word.isEmpty()) return "";
        String normalized = word.trim().toLowerCase();
        normalized = normalized.replace('ё', 'е');
        return normalized;
    }

    public List<String> load() throws TechnicalException, IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(wordsDictionary, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = normalizeWord(br.readLine());
                if (line.length() == MAX_WORD_LENGTH) {
                    words.add(line);
                }
            }
            if (words.isEmpty()) {
                logger.printLog("В словаре нет слов длинной 5 символов");
                throw new TechnicalException("В словаре нет слов длинной 5 символов");
            }
        } catch (FileNotFoundException e) {
            logger.printLog("Ошибка: " + e.getMessage());
            throw new TechnicalException("Файл не найден " + wordsDictionary);
        } catch (IOException e) {
            logger.printLog("Ошибка " + e.getMessage());
            throw new TechnicalException("Ошибка чтения файла" + e.getMessage());
        }
        return words;
    }
}


