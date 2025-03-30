package com.informationsecurity.PasteBinService.models;

public interface UniqueCodeGenerator {
    Code generateCode();
    Code generateCode(long expirationTime);
}
