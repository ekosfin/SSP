package com.example.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    //source: https://howtodoinjava.com/regex/how-to-build-regex-based-password-validator-in-java/
    private static Validator INSTANCE = new Validator();
    private static String pattern = null;

    private Validator() {}

    public static Validator buildValidator( boolean forceSpecialChar,
                                            boolean forceCapitalLetter,
                                            boolean forceNumber,
                                            int minLength,
                                            int maxLength)
    {
        StringBuilder patternBuilder = new StringBuilder("((?=.*[a-z])");

        if (forceSpecialChar) {
            patternBuilder.append("(?=.*[@#$%])");
        }

        if (forceCapitalLetter) {
            patternBuilder.append("(?=.*[A-Z])");
        }

        if (forceNumber) {
            patternBuilder.append("(?=.*[0-9])");
        }

        patternBuilder.append(".{" + minLength + "," + maxLength + "})");
        pattern = patternBuilder.toString();

        return INSTANCE;
    }

    public static boolean validatePassword(final String password)
    {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }

}
