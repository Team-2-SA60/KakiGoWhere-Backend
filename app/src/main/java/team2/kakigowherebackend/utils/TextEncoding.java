package team2.kakigowherebackend.utils;

public class TextEncoding {
    public static String fixEncoding(String badString) {
        return badString
                .replace("â€™", "’")
                .replace("â€“", "–")
                .replace("â€œ", "“")
                .replace("â€�", "”")
                .replace("â€˜", "‘")
                .replace("â€¢", "•")
                .replace("&amp;", "&");
    }
}
