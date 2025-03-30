package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Code;
import com.informationsecurity.PasteBinService.models.UniqueCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UniqueCodeGeneratorService implements UniqueCodeGenerator {

    @Autowired
    private CodeStorage codeStorage;

    @Override
    public Code generateCode() {
        return generateCode(90);
    }

    @Override
    public Code generateCode(long expirationTimeSeconds) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(0, 10));
        }

        LocalDateTime now = LocalDateTime.now();
        Code code = new Code(
                builder.toString(),
                LocalDateTime.of(
                        now.getYear(),
                        now.getMonth(),
                        now.getDayOfMonth(),
                        now.getHour(),
                        now.getMinute(),
                        now.getSecond()
                ).plusSeconds(expirationTimeSeconds)
        );

        if (codeStorage.isCodeExists(code)) {
            return generateCode(expirationTimeSeconds);
        }
        return code;
    }
}
