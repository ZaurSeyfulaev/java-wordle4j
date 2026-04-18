package ru.yandex.practicum.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class Logger {
    private final PrintWriter out;

    public Logger(String filePath) throws IOException {
        out = new PrintWriter(new FileWriter(filePath, StandardCharsets.UTF_8, true));
    }

    public void printLog(String log) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String logTime = now.getYear() + "-"
                + now.getMonthValue() + "-"
                + now.getDayOfMonth() + " "
                + now.getHour() + ":"
                + now.getMinute() + ":"
                + now.getSecond() + "   ";
        out.println(logTime + log);
        out.flush();
    }

    public void close() {
        out.close();
    }
}
