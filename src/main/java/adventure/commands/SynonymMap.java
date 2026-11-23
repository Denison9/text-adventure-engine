package adventure.commands;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Φορτώνει συνώνυμα, προθέσεις και άρθρα από synonyms.json.
// π.χ. "grab" -> "take", "with"/"to"/"the" -> ignored.
public class SynonymMap {
    private final Map<String, String> synonyms = new HashMap<>();
    //HashSet για ταχύτατο έλεγχο ύπαρξης και αυτόματη απόρριψη διπλότυπων.
    private final Set<String> ignoredWords = new HashSet<>();

    public SynonymMap(String filePath) {
        loadSynonyms(filePath);
    }

    private void loadSynonyms(String filePath) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            SynonymData data = gson.fromJson(reader, SynonymData.class);
            if (data != null) {
                // Φόρτωση ρημάτων (π.χ. "grab" -> "take")
                if (data.verbs != null) {
                    for (Map.Entry<String, String> entry : data.verbs.entrySet()) {
                        //Αποθήκευση σε πεζά (lowercase) για case-insensitive αναζήτηση
                        this.synonyms.put(entry.getKey().toLowerCase(), entry.getValue().toLowerCase());
                    }
                }
                // Προθέσεις (with, to, on...) και άρθρα (the, a, an)
                // μπαίνουν στο ίδιο set ignoredWords.
                if (data.prepositions != null) {
                    for (String p : data.prepositions) {
                        if (p != null) this.ignoredWords.add(p.toLowerCase());
                    }
                }
                if (data.articles != null) {
                    for (String a : data.articles) {
                        if (a != null) this.ignoredWords.add(a.toLowerCase());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Σφάλμα φόρτωσης JSON: " + e.getMessage());
        }
    }

    // Επιστρέφει το κανονικό verb. Αν δεν βρεθεί, επιστρέφει το ίδιο το input.
    public String getBaseCommand(String input) {
        return synonyms.getOrDefault(input, input);
    }

    // Λέξεις που πρέπει να αγνοεί ο parser (άρθρα + προθέσεις).
    public Set<String> getIgnoredWords() {
        return Collections.unmodifiableSet(ignoredWords);
    }

    // Αντιστοιχεί στο schema του synonyms.json.
    private static class SynonymData {
        Map<String, String> verbs;
        String[] prepositions;
        String[] articles;
    }
}
