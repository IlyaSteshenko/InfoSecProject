package com.informationsecurity.PasteBinService.services;

import org.springframework.stereotype.Service;

@Service
public class TextFormatService {

    public String format(String description) {
        final int STR_MAX_LEN = 80;
        final int STR_COUNT = 5;
        if (description == null || description.isEmpty()) {
            return "";
        }

        StringBuilder preview = new StringBuilder();
        int lineCount = 0;
        int index = 0;
        int length = description.length();

        while (lineCount < STR_COUNT && index < length) {
            int nextNewline = description.indexOf('\n', index);
            int endOfLine;


            if (nextNewline == -1 || index+STR_MAX_LEN<nextNewline) {
                // Если нет символа новой строки, берем следующую подстроку длиной 80 символов
                endOfLine = Math.min(index + STR_MAX_LEN, length);
            } else {
                // Иначе берем до символа новой строки
                endOfLine = Math.min(nextNewline + 1, length);
            }

            // Добавляем строку в результат
            preview.append(description, index, endOfLine);
            lineCount++;
            index = endOfLine;

            // Если строка была обрезана до 80 символов, добавляем символ новой строки

        }

        return preview.toString();
    }


}
