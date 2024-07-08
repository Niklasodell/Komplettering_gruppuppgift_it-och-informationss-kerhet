package com.example.login.util;

/*
* Denna kod innehåller en hjälpklass MaskingUtils som erbjuder en metod för att anonymisera e-postadresser.
* Metoden anonymize tar en e-postadress som input och ersätter mitten av användarnamnet (allt före @) med stjärnor,
* men behåller det första och sista tecknet i användarnamnet samt hela domändelen (efter @).
*
* Om e-postadressen är tom, null, eller inte korrekt formaterad returneras den oförändrad eller som en tom sträng.
* */

public class MaskingUtils {

    public static String anonymize(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }
        String username = parts[0];
        if (username.length() <= 1) {
            return email;
        }
        return username.charAt(0) + "**********" + username.charAt(username.length() - 1) + "@" + parts[1];
    }
}
