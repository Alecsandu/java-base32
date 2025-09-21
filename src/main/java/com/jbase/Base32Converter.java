package com.jbase;

public interface Base32Converter {

    String encode(String input, Boolean doChecksum);

    String decode(String input, Boolean withChecksum);

}
