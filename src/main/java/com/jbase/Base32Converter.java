package com.jbase;

interface Base32Converter {

    String encode(String input, Boolean withChecksum);

    String decode(String input, Boolean withChecksum);

}
