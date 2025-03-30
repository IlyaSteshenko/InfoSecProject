package com.informationsecurity.PasteBinService.services;

import com.informationsecurity.PasteBinService.models.Code;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CodeStorage {

    private final ConcurrentHashMap<String, Code> codes = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Code> get() {
        return codes;
    }

    public void add(String email, Code code) {
        codes.put(email, code);
    }

    public boolean isCodeExists(Code code) {
        return codes.contains(code);
    }

    public void removeCode(String email) {
        codes.remove(email);
    }

    public boolean checkCode(String email, Code code) {
        if (
                email != null &&
                codes.get(email) != null &&
                !isTimeExpired(code.getExpirationTime())
        ) {
            return codes.get(email).equals(code);
        }
        return false;
    }

    private boolean isTimeExpired(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        return
                timeToNum(now) > timeToNum(time) &&
                now.getYear() == time.getYear() &&
                now.getMonthValue() == time.getMonthValue() &&
                now.getDayOfMonth() == time.getDayOfMonth();
    }

    // Конвертация времени в число (точность до секунд)
    private long timeToNum(LocalDateTime time) {
        return
                time.getHour() * 60 * 60 +
                time.getMinute() * 60 +
                time.getSecond();
    }

}
