# JAVA BASE32 CONVERTER

<hr/>

Supports encoding and decoding for Crockford's Base32 format.

<hr/>

## Example

```java
class Example {
    public static void main(String[] args) {
        Base32.encode("BAELDUNG");
        Base32.decode("890MAK24AN74E");
    }
}
```

For more examples check the unit tests in Base32Test class.

## Learning Resources

1. Baeldung Crockford's Base32 description https://www.baeldung.com/cs/crockfords-base32-encoding
2. Checksum info https://www.johndcook.com/blog/2018/12/28/check-sums-and-error-detection/
3. Base32 and Base64 encoding John D. Cook https://www.johndcook.com/blog/2018/12/28/base-32-and-base-64-encoding/
