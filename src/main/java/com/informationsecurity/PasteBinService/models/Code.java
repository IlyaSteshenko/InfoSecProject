package com.informationsecurity.PasteBinService.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    private String code;
    private LocalDateTime expirationTime;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Code code1 = (Code) o;
        return Objects.equals(code, code1.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }
}
