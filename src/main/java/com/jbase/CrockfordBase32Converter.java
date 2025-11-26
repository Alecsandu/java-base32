package com.jbase;

import java.util.HashMap;
import java.util.Map;

class CrockfordBase32Converter implements Base32Converter {

    private static final String BASE32_ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";
    private static final String BASE32_ALPHABET_PLUS_CHECKSUM_SYMBOLS = "0123456789ABCDEFGHJKMNPQRSTVWXYZ*~$=U";

    private static final int MASK_8BIT = 0xFF;

    private static final int MASK_5BIT = 0x1F;

    private static final int NUM_OF_BITS_IN_BASE32 = 5;

    private static final Map<String, Integer> B32_ALPHABET_INDEXES = new HashMap<>();

    private static final int CHECKSUM_MODULUS = 37;

    static {
        int index = 0;
        for (char ch : BASE32_ALPHABET.toCharArray()) {
            B32_ALPHABET_INDEXES.put(String.valueOf(ch), index);
            index++;
        }
    }

    private static String encode(String input) {
        int buffer = 0;
        int numOfBitsRead = 0;
        StringBuilder result = new StringBuilder();

        for (byte b : input.getBytes()) {
            buffer = (buffer << Byte.SIZE) | (b & MASK_8BIT);
            numOfBitsRead += Byte.SIZE;

            while (numOfBitsRead >= NUM_OF_BITS_IN_BASE32) {
                int chunk = (buffer >> (numOfBitsRead - NUM_OF_BITS_IN_BASE32)) & MASK_5BIT;
                result.append(getBase32Character(chunk));
                numOfBitsRead -= NUM_OF_BITS_IN_BASE32;
            }
        }

        if (numOfBitsRead > 0) {
            int padding = NUM_OF_BITS_IN_BASE32 - numOfBitsRead;
            int chunk = (buffer << padding) & MASK_5BIT;
            result.append(getBase32Character(chunk));
        }

        return result.toString();
    }

    private static String decode(String input) {
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

    private static String encodeWithChecksum(String input) {
        String encodedText = encode(input);
        int checksum = calculateChecksum(encodedText);
        return encodedText + getBase32CharacterChecksum(checksum);
    }

    private static String decodeWithChecksum(String input) {
        char receivedChecksumSymbol = input.charAt(input.length() - 1);

        boolean valid = false;
        for (var ch : BASE32_ALPHABET_PLUS_CHECKSUM_SYMBOLS.toCharArray()) {
            if (ch == receivedChecksumSymbol) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            throw new IllegalStateException("Invalid checksum symbol!");
        }

        String decodedString = decode(input.substring(0, input.length() - 1));

        int currentChecksum = calculateChecksum(input.substring(0, input.length() - 1));
        if (!(receivedChecksumSymbol == getBase32CharacterChecksum(currentChecksum))) {
            throw new IllegalStateException("Invalid checksum value!");
        }

        return decodedString;
    }

    private static int calculateChecksum(String encodedText) {
        int checksum = 0;
        for (char ch : encodedText.toCharArray()) {
            checksum = (checksum * 32 + B32_ALPHABET_INDEXES.get(String.valueOf(ch))) % CHECKSUM_MODULUS;
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

    private static char getBase32Character(int index) {
        return BASE32_ALPHABET.charAt(index);
    }

    private static char getBase32CharacterChecksum(int index) {
        return BASE32_ALPHABET_PLUS_CHECKSUM_SYMBOLS.charAt(index);
    }

    private static String getNormalizedInput(String input) {
        return input.toUpperCase();
    }

    @Override
    public String encode(String input, Boolean withChecksum) {
        if (withChecksum) {
            return encodeWithChecksum(input);
        }
        return encode(input);
    }

    @Override
    public String decode(String input, Boolean withChecksum) {
        String normalized = getNormalizedInput(input);
        if (withChecksum) {
            return decodeWithChecksum(normalized);
        }
        return decode(normalized);
    }
}
