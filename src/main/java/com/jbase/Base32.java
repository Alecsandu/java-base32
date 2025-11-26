package com.jbase;

public class Base32 {

    private static final Base32Converter CROCK_FORD_BASE_32_CONVERTER = new CrockfordBase32Converter();

    public static String encode(String input, Boolean withChecksum) {
        return CROCK_FORD_BASE_32_CONVERTER.encode(input, withChecksum);
    }

    public static String decode(String input, Boolean withChecksum) {
        return CROCK_FORD_BASE_32_CONVERTER.decode(input, withChecksum);
    }
}
