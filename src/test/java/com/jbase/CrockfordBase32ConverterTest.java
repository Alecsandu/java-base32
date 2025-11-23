package com.jbase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrockfordBase32ConverterTest {

    private final Base32Converter crockfordBase32Converter = new CrockfordBase32Converter();

    @Test
    void base32Test_basicWordEncoding() {
        String data = "BAELDUNG";

        String encodedData = crockfordBase32Converter.encode(data, false);

        assertNotNull(encodedData);
        assertEquals("890MAK24AN74E", encodedData);
    }

    @Test
    void base32Test_wordEncodingWithChecksum() {
        String data = "BAELDUNG";

        String encodedData = crockfordBase32Converter.encode(data, true);

        assertNotNull(encodedData);
        assertEquals("890MAK24AN74EY", encodedData);
    }

    @Test
    void base32Test_basicNumber() {
        String data = "123454";

        String encodedData = crockfordBase32Converter.encode(data, false);

        assertNotNull(encodedData);
        assertEquals("64S36D1N6G", encodedData);
    }

    @Test
    void base32Test_decodeWord() {
        String encodedWord = "890MAK24AN74EY";

        String decodedWord = crockfordBase32Converter.decode(encodedWord, false);

        assertNotNull(decodedWord);
        assertEquals("BAELDUNG", decodedWord);
    }

    @Test
    void base32Test_encodeSentenceWithChecksumSymbolFromDefaultAlphabet() {
        String sentence = "The quick brown fox jumps over the lazy dog.";

        String encodedSentence = crockfordBase32Converter.encode(sentence, false);
        String encodedSentenceWithChecksum = crockfordBase32Converter.encode(sentence, true);

        assertEquals("AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG", encodedSentence);
        assertEquals("AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBGM", encodedSentenceWithChecksum);
    }

    @Test
    void base32Test_encodeSentenceWithChecksumSymbolFromExtendedAlphabet() {
        String sentence = "the quick brown fox jumps over the lazy dog.";

        String encodedSentence = crockfordBase32Converter.encode(sentence, false);
        String encodedSentenceWithChecksum = crockfordBase32Converter.encode(sentence, true);

        assertEquals("EHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG", encodedSentence);
        assertEquals("EHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG*", encodedSentenceWithChecksum);
    }

    @Test
    void base32Test_decodeIntoSentenceWithChecksumSymbolFromDefaultAlphabet() {
        String encodedSentence = "AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG";
        String encodedSentenceWithValidChecksum = "AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBGM";

        String sentence = crockfordBase32Converter.decode(encodedSentence, false);
        String decodedChecksumSentence = crockfordBase32Converter.decode(encodedSentenceWithValidChecksum, true);

        assertEquals("The quick brown fox jumps over the lazy dog.", sentence);
        assertEquals("The quick brown fox jumps over the lazy dog.", decodedChecksumSentence);
    }

    @Test
    void base32Test_decodeIntoSentenceWithChecksumSymbolFromExtendedAlphabet() {
        String encodedSentence = "EHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG";
        String encodedSentenceWithValidChecksum = "EHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBG*";

        String sentence = crockfordBase32Converter.decode(encodedSentence, false);
        String decodedChecksumSentence = crockfordBase32Converter.decode(encodedSentenceWithValidChecksum, true);

        assertEquals("the quick brown fox jumps over the lazy dog.", sentence);
        assertEquals("the quick brown fox jumps over the lazy dog.", decodedChecksumSentence);
    }

    @Test
    void base32Test_decodeWithInvalidChecksumValue() {
        String encodedSentenceWithInvalidChecksum = "AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBGA";

        Exception ex = assertThrows(IllegalStateException.class, () -> crockfordBase32Converter.decode(encodedSentenceWithInvalidChecksum, true));

        assertEquals("Invalid checksum value!", ex.getMessage());
    }

    @Test
    void base32Test_decodeWithInvalidChecksumSymbol() {
        String encodedSentenceWithInvalidChecksum = "AHM6A83HENMP6TS0C9S6YXVE41K6YY10D9TPTW3K41QQCSBJ41T6GS90DHGQMY90CHQPEBGI";

        Exception ex = assertThrows(IllegalStateException.class, () -> crockfordBase32Converter.decode(encodedSentenceWithInvalidChecksum, true));

        assertEquals("Invalid checksum symbol!", ex.getMessage());
    }

    @Test
    void base32Test_decodeInvalidWordThrowsException() {
        String invalidInput = "I890MAK24AN74E";

        Exception ex = assertThrows(IllegalStateException.class, () -> crockfordBase32Converter.decode(invalidInput, false));

        assertEquals("Invalid Crockford Base32 character: I", ex.getMessage());
    }
}
