package serialization.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal JSON reader/writer for lab serialization (variant 4 — JSON).
 * Supports objects, arrays, strings, numbers, booleans, and null.
 */
public final class JsonValue {
    private JsonValue() {
    }

    public static String stringify(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return quote((String) value);
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map) {
            return stringifyObject((Map<?, ?>) value);
        }
        if (value instanceof List) {
            return stringifyArray((List<?>) value);
        }
        if (value instanceof int[]) {
            return stringifyIntArray((int[]) value);
        }
        throw new IllegalArgumentException("Unsupported JSON value type: " + value.getClass());
    }

    @SuppressWarnings("unchecked")
    public static Object parse(String json) {
        Parser parser = new Parser(json.trim());
        Object value = parser.readValue();
        parser.skipWhitespace();
        if (!parser.isAtEnd()) {
            throw new IllegalArgumentException("Unexpected trailing JSON content");
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> asObject(Object value) {
        if (!(value instanceof Map)) {
            throw new IllegalArgumentException("Expected JSON object");
        }
        return (Map<String, Object>) value;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> asArray(Object value) {
        if (!(value instanceof List)) {
            throw new IllegalArgumentException("Expected JSON array");
        }
        return (List<Object>) value;
    }

    public static int asInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new IllegalArgumentException("Expected JSON number, got: " + value);
    }

    public static String asString(Object value) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Expected JSON string");
        }
        return (String) value;
    }

    public static int[] asIntArray(Object value) {
        List<Object> list = asArray(value);
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = asInt(list.get(i));
        }
        return result;
    }

    private static String stringifyObject(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(quote(String.valueOf(entry.getKey())));
            sb.append(':');
            sb.append(stringify(entry.getValue()));
        }
        sb.append('}');
        return sb.toString();
    }

    private static String stringifyArray(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(stringify(list.get(i)));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String stringifyIntArray(int[] values) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(values[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    private static String quote(String text) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    private static final class Parser {
        private final String json;
        private int index;

        private Parser(String json) {
            this.json = json;
        }

        private boolean isAtEnd() {
            return index >= json.length();
        }

        private void skipWhitespace() {
            while (!isAtEnd() && Character.isWhitespace(json.charAt(index))) {
                index++;
            }
        }

        private char peek() {
            return json.charAt(index);
        }

        private char next() {
            return json.charAt(index++);
        }

        private Object readValue() {
            skipWhitespace();
            if (isAtEnd()) {
                throw new IllegalArgumentException("Unexpected end of JSON");
            }
            char c = peek();
            if (c == '{') {
                return readObject();
            }
            if (c == '[') {
                return readArray();
            }
            if (c == '"') {
                return readString();
            }
            if (c == 't') {
                expectLiteral("true");
                return Boolean.TRUE;
            }
            if (c == 'f') {
                expectLiteral("false");
                return Boolean.FALSE;
            }
            if (c == 'n') {
                expectLiteral("null");
                return null;
            }
            return readNumber();
        }

        private Map<String, Object> readObject() {
            expect('{');
            Map<String, Object> map = new LinkedHashMap<>();
            skipWhitespace();
            if (peek() == '}') {
                next();
                return map;
            }
            while (true) {
                skipWhitespace();
                String key = readString();
                skipWhitespace();
                expect(':');
                map.put(key, readValue());
                skipWhitespace();
                char c = next();
                if (c == '}') {
                    break;
                }
                if (c != ',') {
                    throw new IllegalArgumentException("Expected ',' or '}' in object");
                }
            }
            return map;
        }

        private List<Object> readArray() {
            expect('[');
            List<Object> list = new ArrayList<>();
            skipWhitespace();
            if (peek() == ']') {
                next();
                return list;
            }
            while (true) {
                list.add(readValue());
                skipWhitespace();
                char c = next();
                if (c == ']') {
                    break;
                }
                if (c != ',') {
                    throw new IllegalArgumentException("Expected ',' or ']' in array");
                }
            }
            return list;
        }

        private String readString() {
            expect('"');
            StringBuilder sb = new StringBuilder();
            while (!isAtEnd()) {
                char c = next();
                if (c == '"') {
                    return sb.toString();
                }
                if (c == '\\') {
                    if (isAtEnd()) {
                        throw new IllegalArgumentException("Unterminated escape sequence");
                    }
                    char esc = next();
                    switch (esc) {
                        case '"':
                        case '\\':
                        case '/':
                            sb.append(esc);
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid escape: \\" + esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            throw new IllegalArgumentException("Unterminated string");
        }

        private Number readNumber() {
            int start = index;
            if (peek() == '-') {
                index++;
            }
            while (!isAtEnd() && Character.isDigit(json.charAt(index))) {
                index++;
            }
            if (!isAtEnd() && json.charAt(index) == '.') {
                index++;
                while (!isAtEnd() && Character.isDigit(json.charAt(index))) {
                    index++;
                }
            }
            String number = json.substring(start, index);
            if (number.contains(".")) {
                return Double.parseDouble(number);
            }
            return Integer.parseInt(number);
        }

        private void expect(char expected) {
            skipWhitespace();
            if (isAtEnd() || next() != expected) {
                throw new IllegalArgumentException("Expected '" + expected + "'");
            }
        }

        private void expectLiteral(String literal) {
            for (int i = 0; i < literal.length(); i++) {
                if (isAtEnd() || json.charAt(index++) != literal.charAt(i)) {
                    throw new IllegalArgumentException("Expected " + literal);
                }
            }
        }
    }
}
