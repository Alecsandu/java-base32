package com.jbase;

public interface Base32Converter {

    String encode(String input, Boolean withChecksum);

    String decode(String input, Boolean withChecksum);

}
