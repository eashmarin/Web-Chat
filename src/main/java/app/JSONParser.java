package app;

public class JSONParser {
    public static String getParameter(String input, String key) {
        String[] lines = input.substring(1, input.length() - 1).split(",");
        for (String line: lines) {
            String[] pair = line.split(":");

            if (key.equals(pair[0].substring(1, pair[0].length() - 1)))
                return pair[1].substring(1, pair[1].length() - 1);
        }
        throw new RuntimeException("JSON doesn't have key = " + key);
    }
}
