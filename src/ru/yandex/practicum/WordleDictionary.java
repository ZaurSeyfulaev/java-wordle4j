package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.InputException;
import ru.yandex.practicum.exceptions.TechnicalException;
import ru.yandex.practicum.util.Logger;

import java.io.IOException;
import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private static final int MAX_WORD_LENGTH = 5;
    private final List<String> words; // Список всех слов
    private final Set<String> wordSet; // Для быстрого поиска
    Logger logger;
    Random rand = new Random();//Для хранения слов и подсказок к ним

    public WordleDictionary(WordleDictionaryLoader wordleDictionaryLoader, Logger logger) throws TechnicalException, IOException {
        this.words = wordleDictionaryLoader.load();
        this.wordSet = new HashSet<>(words);
        this.logger = logger;
    }

    public boolean isTrueWord(String word) throws IOException {
        try {
            if (word.length() > MAX_WORD_LENGTH) {
                throw new InputException("Слово слишком длинное");
            }
            String normalizedWord = WordleDictionaryLoader.normalizeWord(word);
            return wordSet.contains(normalizedWord) && word.length() == MAX_WORD_LENGTH;
        } catch (InputException e) {
            logger.printLog(e.getMessage());
        }
        return false;
    }

    public String getRandomWord() throws IOException {
        int wordIndex = rand.nextInt(words.size());
        logger.printLog("Компьютер загадал слово " + words.get(wordIndex));
        return words.get(wordIndex);
    }

    public List<String> getAllWords() {
        return new ArrayList<>(words);
    }

    public String getHintString(String userWord, String trueWord) {
        String normalizeUserWord = WordleDictionaryLoader.normalizeWord(userWord);
        String normalizeTrueWord = WordleDictionaryLoader.normalizeWord(trueWord);
        StringBuilder builder = new StringBuilder();  //нужен для того, чтобы собрать символы-подсказки в строку

        for (int i = 0; i < normalizeUserWord.length(); i++) {
            if (normalizeUserWord.charAt(i) == normalizeTrueWord.charAt(i)) {
                builder.append("+");
            } else if (normalizeTrueWord.indexOf(normalizeUserWord.charAt(i)) != -1) {
                builder.append("^");
            } else {
                builder.append("-");
            }
        }
        return builder.toString();
    }
}
