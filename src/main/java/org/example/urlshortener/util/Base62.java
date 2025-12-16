// Uses the unique id from the database to generate a Base62 encoded short code for URLs.
package org.example.urlshortener.util;

public class Base62 {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    private Base62() {}

    public static String encode(long num) {
            if (num == 0) return "0";
            StringBuilder sb = new StringBuilder();
            while (num > 0) {
                int rem = (int) (num % BASE);
                sb.append(ALPHABET.charAt(rem));
                num /= BASE;
            }
            return sb.reverse().toString();
        }

    public static long decode(String str) {
        long result = 0;
        for (char c : str.toCharArray()) {
            result = result * BASE + ALPHABET.indexOf(c);
        }
        return result;
    }
    
}
