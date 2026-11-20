//Аллазкани
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import java.nio.file.Path;
import java.util.*;

public class Words {
    private static final int WORD_COUNT = 10;
    private record Puzzle(char[][] grid, List<String> words) {}
    private static final String LETTERS = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final Set<String> SIZES = Set.of("10x10", "11x10", "10x11", "12x10", "10x12");

    private static List<String> readWords(Path file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        var document = factory.newDocumentBuilder().parse(file.toFile());
        var arrays = document.getElementsByTagName("string-array");
        Set<String> words = new LinkedHashSet<>();
        for (int i = 0; i < arrays.getLength(); i++) {
            Element array = (Element) arrays.item(i);
            if (!"words".equals(array.getAttribute("name"))) continue;
            var items = array.getElementsByTagName("item");
            for (int j = 0; j < items.getLength(); j++) {
                String word = items.item(j).getTextContent().strip().toUpperCase(Locale.ROOT);
                if (!word.matches("[А-ЯЁ]+")) {
                    throw new IllegalArgumentException("Недопустимое слово в XML: " + word);
                }
                words.add(word);
            }
        }
        if (words.size() < WORD_COUNT) {
            throw new IllegalArgumentException("В массиве words должно быть хотя бы 10 разных русских слов.");
        }
        return List.copyOf(words);
    }


