package com.jbase;

import java.util.HashMap;
import java.util.Map;

public class Base32 {

    private static final String B32_ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";
    private static final int MASK_8BIT = 0xFF;
    private static final int MASK_5BIT = 0x1F;
    private static final int NUM_OF_BITS_IN_BASE32 = 5;
    private static final Map<String, Integer> B32_ALPHABET_INDEXES = new HashMap<>();
    private static final int CHECKSUM_MODULUS = 37;
    private static final int BASE_32 = 32;

    static {
        int index = 0;
        for (char ch : B32_ALPHABET.toCharArray()) {
            B32_ALPHABET_INDEXES.put(String.valueOf(ch), index);
            index++;
        }
    }

    public static String encode(String input) {
        int buffer = 0;
        int numOfBitsRead = 0;
        StringBuilder result = new StringBuilder();

        for (byte b : input.getBytes()) {
            buffer = (buffer << Byte.SIZE) | (b & MASK_8BIT);
            numOfBitsRead += Byte.SIZE;

            while (numOfBitsRead >= NUM_OF_BITS_IN_BASE32) {
                int chunk = (buffer >> (numOfBitsRead - NUM_OF_BITS_IN_BASE32)) & MASK_5BIT;
                result.append(B32_ALPHABET.charAt(chunk));
                numOfBitsRead -= NUM_OF_BITS_IN_BASE32;
            }
        }

        if (numOfBitsRead > 0) {
            int padding = NUM_OF_BITS_IN_BASE32 - numOfBitsRead;
            int chunk = (buffer << padding) & MASK_5BIT;
            result.append(B32_ALPHABET.charAt(chunk));
        }

        return result.toString();
    }

    public static String decode(String input) {
        int buffer = 0;
        int bitsRead = 0;
        StringBuilder result = new StringBuilder();

        for (byte b : input.getBytes()) {
            int chValue = getNumericValue(b);
            buffer = (buffer << NUM_OF_BITS_IN_BASE32) | chValue;
            bitsRead += 5;

            while (bitsRead >= Byte.SIZE) {
                int val = (buffer >> (bitsRead - Byte.SIZE)) & MASK_8BIT;
                result.append(Character.toString(val));
                bitsRead -= Byte.SIZE;
            }
        }

        return result.toString();
    }

    public static String encodeWithChecksum(String input) {
        String encodedText = encode(input);
        int checksum = calculateChecksum(encodedText);
        return encodedText + B32_ALPHABET.charAt(checksum);
    }

    private static int calculateChecksum(String encodedText) {
        int checksum = 0;
        for (char ch : encodedText.toCharArray()) {
            checksum = (checksum * BASE_32 + B32_ALPHABET_INDEXES.get(String.valueOf(ch))) % CHECKSUM_MODULUS;
        }
        return checksum;
    }

    private static int getNumericValue(byte b) {
        String codePointStringRepresentation = Character.toString(b);
        int index = B32_ALPHABET_INDEXES.getOrDefault(Character.toString(b), -1);
        if (index == -1) {
            throw new IllegalStateException("Invalid Crockford Base32 character: " + codePointStringRepresentation);
        }
        return index;
    }

}
