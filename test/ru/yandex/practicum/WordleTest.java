package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.TechnicalException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionary;
import ru.yandex.practicum.util.Logger;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    static Logger logger;
    static WordleDictionaryLoader wordleDictionaryLoader;
    static WordleDictionary wordleDictionary;
    static WordleGame wordleGame;

    @BeforeAll
    static void init() throws IOException {
        logger = new Logger("testLog.txt");
        wordleDictionaryLoader = new WordleDictionaryLoader(logger);
    }


    @Test
    void normalizeWordShouldConvertToLowerCase() {
        String result = WordleDictionaryLoader.normalizeWord("ПАРАД");
        assertEquals("парад", result);
    }

    @Test
    void normalizeWordShouldReplaceYoWithE() {
        String result = WordleDictionaryLoader.normalizeWord("полёт");
        assertEquals("полет", result);
    }

    @Test
    void normalizeWordShouldTrimSpaces() {
        String result = WordleDictionaryLoader.normalizeWord("  порог  ");
        assertEquals("порог", result);
    }

// ==================== ТЕСТЫ ДЛЯ WordleDictionary ====================

    @BeforeEach
    void setUp() throws TechnicalException, IOException {
        wordleDictionary = new WordleDictionary(wordleDictionaryLoader, logger);
    }

    @Test
    void isTrueWordShouldReturnTrueForExistingWord() throws IOException {
        boolean result = false;
        result = wordleDictionary.isTrueWord("герой");
        assertTrue(result);
    }

    @Test
    void isTrueNormalizedWordsShouldReturnTrue() throws IOException {
        boolean result = false;
        result = wordleDictionary.isTrueWord("котёл");
        assertTrue(result);
    }


    @Test
    void getRandomWordShouldReturnWordFromDictionary() throws IOException {
        String word = wordleDictionary.getRandomWord();
        assertEquals(5, word.length());
    }


    @Test
    void getHintString_shouldReturnFiveCharacters() {
        String hint = wordleDictionary.getHintString("гость", "герой");
        assertEquals(5, hint.length());
    }


    @Test
    void getHintStringShouldHandleYoNormalization() {
        String hint = wordleDictionary.getHintString("котёл", "котел");
        assertEquals("+++++", hint);
    }

    @Test
    void getHintStringShouldReturnCorrectHint() {
        String hint = wordleDictionary.getHintString("короб", "кубок");
        assertEquals("+^-+^", hint);
    }

    // ==================== ТЕСТЫ ДЛЯ WordleGameTest ====================

    @BeforeEach
    void start() throws IOException, TechnicalException {
        wordleDictionaryLoader = new WordleDictionaryLoader(logger);
        wordleDictionary = new WordleDictionary(wordleDictionaryLoader, logger);
        wordleGame = new WordleGame(wordleDictionary, logger);
    }
    @Test
    void getAnswerWithCorrectWordShouldReturnFivePluses() throws IOException, WordNotFoundInDictionary {
        String secret = wordleGame.getAnswer();
        String result = wordleGame.getAnswer(secret);
        assertEquals("+++++", result);
    }
@Test
  void  getAnswerWithMultipleValidWordsShouldIncreaseStepsCorrectly () throws IOException, WordNotFoundInDictionary {
        assertEquals(0, wordleGame.getSteps());
        wordleGame.getAnswer("город");
        wordleGame.getAnswer("город");
        wordleGame.getAnswer("город");
        wordleGame.getAnswer("город");
        assertEquals(4, wordleGame.getSteps());
}
    @Test
    void getAnswerWithoutParamsShouldReturnSameWordOnMultipleCalls() {
        String firstCall = wordleGame.getAnswer();
        String secondCall = wordleGame.getAnswer();
        assertEquals(firstCall, secondCall);
    }

    @Test
    void getHintShouldReturnCorrectHint() throws IOException {
        String hint = wordleGame.getHint();
        assertEquals(5, hint.length());
    }
}