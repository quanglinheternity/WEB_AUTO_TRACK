package com.transport.util;

import java.util.Random;

public final class CodeGenerator {

    private static final Random RANDOM = new Random();

    private CodeGenerator() {}

    
    public static String generateCode(String prefix) {
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8); // phần cuối của timestamp
        int random = RANDOM.nextInt(1000); // 3 số ngẫu nhiên
        return String.format("%s%s%03d", prefix.toUpperCase(), timestamp, random);
    }
}
