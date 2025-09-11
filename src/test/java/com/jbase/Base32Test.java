package com.jbase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Base32Test {

    @Test
    void base32Test_basicWordEncoding() {
        String data = "BAELDUNG";

        String encodedData = Base32.encode(data);

        assertNotNull(encodedData);
        assertEquals("890MAK24AN74E", encodedData);
    }

    @Test
    void base32Test_wordEncodingWithChecksum() {
        String data = "BAELDUNG";

        String encodedData = Base32.encodeWithChecksum(data);

        assertNotNull(encodedData);
        assertEquals("890MAK24AN74EY", encodedData);
    }

    @Test
    void base32Test_basicNumber() {
        String data = "123454";

        String encodedData = Base32.encode(data);

        assertNotNull(encodedData);
        assertEquals("64S36D1N6G", encodedData);
    }

    @Test
    void base32Test_decodeWord() {
        String encodedWord = "890MAK24AN74EY";

        String decodedWord = Base32.decode(encodedWord);

        assertNotNull(decodedWord);
        assertEquals("BAELDUNG", decodedWord);
    }

    @Test
    void base32Test_encodeSentence() {
        String sentence = "The quick brown fox jumps over the lazy dog.";

        String encodedSentence = Base32.encode(sentence);
        String encodedSentenceWithChecksum = Base32.encodeWithChecksum(sentence);

        assertEquals("AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG", encodedSentence);
        assertEquals("AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBGM", encodedSentenceWithChecksum);
    }

    @Test
    void base32Test_decodeIntoSentence() {
        String encodedSentence = "AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG";
        String encodedSentenceWithChecksum = "AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBGM";

        String sentence = Base32.decode(encodedSentence);
        String decodedChecksumSentence = Base32.decodeWithChecksum(encodedSentenceWithChecksum);

        assertEquals("The quick brown fox jumps over the lazy dog.", sentence);
        assertEquals("The quick brown fox jumps over the lazy dog.", decodedChecksumSentence);
    }

    @Test
    void base32Test_decodeInvalidWordThrowsException() {
        String invalidInput = "I890MAK24AN74E";

        Exception ex = assertThrows(IllegalStateException.class, () -> Base32.decode(invalidInput));

        assertEquals("Invalid Crockford Base32 character: I", ex.getMessage());
    }

}
