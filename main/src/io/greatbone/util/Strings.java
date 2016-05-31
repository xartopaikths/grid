package io.greatbone.util;

/**
 * Some often-used character string operations such as english langauge-relevant conversions.
 *
 * @author Michael Huang
 */
public class Strings {

    private static Roll<String, String> SS = new Roll<>(128); // irregular singulars

    static {
        SS.put("ache", "aches");
        SS.put("alumna", "alumnae");
        SS.put("alumnus", "alumni");
        SS.put("axis", "axes");
        SS.put("bison", "bison");
        SS.put("calf", "calves");
        SS.put("caribou", "caribou");
        SS.put("child", "children");
        SS.put("datum", "data");
        SS.put("deer", "deer");
        SS.put("elf", "elves");
        SS.put("elk", "elk");
        SS.put("fish", "fish");
        SS.put("foot", "feet");
        SS.put("gentleman", "gentlemen");
        SS.put("gentlewoman", "gentlewomen");
        SS.put("go", "goes");
        SS.put("goose", "geese");
        SS.put("grouse", "grouse");
        SS.put("half", "halves");
        SS.put("hoof", "hooves");
        SS.put("knife", "knives");
        SS.put("leaf", "leaves");
        SS.put("life", "lives");
        SS.put("louse", "lice");
        SS.put("man", "men");
        SS.put("money", "monies");
        SS.put("moose", "moose");
        SS.put("mouse", "mice");
        SS.put("ox", "oxen");
        SS.put("plus", "pluses");
        SS.put("quail", "quail");
        SS.put("reindeer", "reindeer");
        SS.put("scarf", "scarves");
        SS.put("self", "selves");
        SS.put("sheaf", "sheaves");
        SS.put("sheep", "sheep");
        SS.put("shelf", "shelves");
        SS.put("squid", "squid");
        SS.put("thief", "thieves");
        SS.put("tooth", "teeth");
        SS.put("wharf", "wharves");
        SS.put("wife", "wives");
        SS.put("wolf", "wolves");
        SS.put("woman", "women");
    }

    private static Roll<String, String> PS = new Roll<>(128); // irregular plurals

    static {
        PS.put("aches", "ache");
        PS.put("alumnae", "alumna");
        PS.put("alumni", "alumnus");
        PS.put("axes", "axes");
        PS.put("bison", "bison");
        PS.put("buses", "bus");
        PS.put("busses", "bus");
        PS.put("brethren", "brother");
        PS.put("caches", "cache");
        PS.put("calves", "calf");
        PS.put("cargoes", "cargo");
        PS.put("caribou", "caribou");
        PS.put("children", "child");
        PS.put("data", "datum");
        PS.put("deer", "deer");
        PS.put("dice", "die");
        PS.put("dies", "die");
        PS.put("dominoes", "domino");
        PS.put("echoes", "echo");
        PS.put("elves", "elf");
        PS.put("elk", "elk");
        PS.put("embargoes", "embargo");
        PS.put("fish", "fish");
        PS.put("feet", "foot");
        PS.put("gentlemen", "gentleman");
        PS.put("gentlewomen", "gentlewoman");
        PS.put("geese", "goose");
        PS.put("goes", "go");
        PS.put("grottoes", "grotto");
        PS.put("grouse", "grouse");
        PS.put("halves", "half");
        PS.put("hooves", "hoof");
        PS.put("knives", "knife");
        PS.put("leaves", "leaf");
        PS.put("lives", "life");
        PS.put("lice", "louse");
        PS.put("men", "man");
        PS.put("monies", "money");
        PS.put("moose", "moose");
        PS.put("mottoes", "motto");
        PS.put("mice", "mouse");
        PS.put("octopi", "octopus");
        PS.put("octopodes", "octopus");
        PS.put("octopuses", "octopus");
        PS.put("oxen", "ox");
        PS.put("pies", "pie");
        PS.put("pluses", "plus");
        PS.put("posses", "posse");
        PS.put("potatoes", "potato");
        PS.put("quail", "quail");
        PS.put("reindeer", "reindeer");
        PS.put("scarves", "scarf");
        PS.put("sheaves", "sheaf");
        PS.put("sheep", "sheep");
        PS.put("shelves", "shelf");
        PS.put("squid", "squid");
        PS.put("teeth", "tooth");
        PS.put("thieves", "thief");
        PS.put("ties", "tie");
        PS.put("tomatoes", "tomato");
        PS.put("wharves", "wharf");
        PS.put("wives", "wife");
        PS.put("wolves", "wolf");
        PS.put("women", "woman");
    }

    public static boolean isPlural(String word) {
        word = word.toLowerCase();
        if (PS.get(word) != null) {
            return true;
        }
        if (word.length() <= 1) {
            return false;
        }
        // If it is not an irregular plural, it must end in -s,
        // but it must not be an irregular singular (like "bus")
        // nor end in -ss (like "boss").
        if (word.charAt(word.length() - 1) != 's') {
            return false;
        }
        if (SS.get(word) != null) {
            return false;
        }
        if (word.length() >= 2 && word.charAt(word.length() - 2) == 's') {
            return false;
        }
        return true;
    }

    public static boolean isSingular(String word) {
        word = word.toLowerCase();
        if (SS.get(word) != null) {
            return true;
        }
        // If it is not an irregular singular, it must not be an
        // irregular plural (like "children"), and it must not end
        // in -s unless it ends in -ss (like "boss")).
        if (PS.get(word) != null) {
            return false;
        }
        if (word.length() <= 0) {
            return false;
        }
        if (word.charAt(word.length() - 1) != 's') {
            return true;
        }
        if (word.length() >= 2 && word.charAt(word.length() - 2) == 's') {
            return true; // word ends in -ss
        }
        return false; // word is not irregular, and ends in -s but not -ss
    }

    /**
     * This macro returns the plural form of a given english word.
     *
     * @param word the english word for which the plural form is returned
     * @return the plural form of a given english word
     */
    public static String plural(String word) {
        word = word.toLowerCase();
        if (isPlural(word)) {
            return word;
        }

        String lookup = SS.get(word);
        if (lookup != null) {
            return lookup;
        }

        int length = word.length();
        if (length <= 1) {
            return word + "'s";
        }
        char lastLetter = word.charAt(length - 1);
        char secondLast = word.charAt(length - 2);
        if ("sxzo".indexOf(lastLetter) >= 0 ||
                (lastLetter == 'h' && (secondLast == 's' || secondLast == 'c'))) {
            return word + "es";
        }

        if (lastLetter == 'y') {
            if ("aeiou".indexOf(secondLast) >= 0) {
                return word + "s";
            } else {
                return word.substring(0, length - 1) + "ies";
            }
        }
        return word + "s";
    }

    /**
     * This macro returns the singular form of a given english word.
     *
     * @param word the english word for which the singular form is returned
     * @return the singular form of a given english word
     */
    public static String singular(String word) {
        word = word.toLowerCase();
        if (isSingular(word)) {
            return word;
        }
        String lookup = PS.get(word);
        if (lookup != null) {
            return lookup;
        }
        int length = word.length();
        if (length <= 1) {
            return word;
        }
        char lastLetter = word.charAt(length - 1);
        if (lastLetter != 's') {
            return word; // no final -s
        }
        char secondLast = word.charAt(length - 2);
        if (secondLast == '\'') {
            return word.substring(0, length - 2);
        }
        // remove -'s
        if (word.equalsIgnoreCase("gas")) {
            return word;
        }
        if (secondLast != 'e' || length <= 3) {
            return word.substring(0, length - 1); // remove final -s
        }
        // Word ends in -es and has length >= 4:
        char thirdLast = word.charAt(length - 3);
        if (thirdLast == 'i') { // -ies => -y
            return word.substring(0, length - 3) + "y";
        }
        if (thirdLast == 'x') { // -xes => -x
            return word.substring(0, length - 2);
        }
        if (length <= 4) { // e.g. uses => use
            return word.substring(0, length - 1);
        }
        char fourthLast = word.charAt(length - 4);
        if (thirdLast == 'h' && (fourthLast == 'c' || fourthLast == 's')) { // -ches or -shes => -ch or -sh
            return word.substring(0, length - 2);
        }
        if (thirdLast == 's' && fourthLast == 's') { // -sses => -ss
            return word.substring(0, length - 2);
        }
        return word.substring(0, length - 1); // keep the final e.
    }

    /**
     * Determines if a given string is in upper case.
     *
     * @param str the given string
     * @return true if upper case
     */
    public static boolean isUpperCase(String str) {
        if (str == null || str.isEmpty())
            return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isUpperCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether a given token is a valid java identifier.
     *
     * @param token the string to be recognized as a java identifier
     * @return true if
     */
    public static boolean isJavaIdentifier(String token) {
        if (token == null || token.isEmpty() || !Character.isJavaIdentifierStart(token.charAt(0))) {
            return false;
        } else {
            for (int i = 1; i < token.length(); i++) {
                if (!Character.isJavaIdentifierPart(token.charAt(i))) return false;
            }
            return true;
        }
    }

}
