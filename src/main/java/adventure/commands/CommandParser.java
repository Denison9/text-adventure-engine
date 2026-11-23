package adventure.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

// Παίρνει input από τον χρήστη (π.χ. "pick up the key") και βγάζει
// ρήμα + όρισμα (π.χ. ρήμα="take", όρισμα=["key"]).
public class CommandParser {
    private static final Set<String> EMPTY_IGNORED = Collections.emptySet();
    private final SynonymMap synonymMap;

    public CommandParser(SynonymMap synonymMap) {
        this.synonymMap = synonymMap;
    }

    // Αν ο χρήστης πατήσει Enter χωρίς να γράψει τίποτα, επιστρέφει null
    public ParsedData parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String lowerInput = input.toLowerCase().trim();

        String verb = null;
        int skipTokens = 1;

        // Ελέγχει για 2-λεκτο verb πρώτα (π.χ. "pick up", "look at").
        // Πρέπει να γίνει πριν φιλτραριστούν οι προθέσεις, αλλιώς το look at
        // θα έχανε το at και δεν θα ταίριαζε.
        if (synonymMap != null) {
            String[] rawForVerb = lowerInput.split("\\s+");
            if (rawForVerb.length >= 2) {
                String twoWord = rawForVerb[0] + " " + rawForVerb[1];
                String mapped = synonymMap.getBaseCommand(twoWord);
                // Αν βρέθηκε αντιστοιχία 2 λέξεων στο λεξικό
                if (!mapped.equals(twoWord)) {
                    verb = mapped;
                    skipTokens = 2;
                }
            }
            // Αν δεν ήταν phrasal verb, ελέγχουμε την 1η λέξη
            if (verb == null) {
                verb = synonymMap.getBaseCommand(lowerInput.split("\\s+")[0]);
            }
        } else {
            // Fallback αν δεν υπάρχει λεξικό
            verb = lowerInput.split("\\s+")[0];
        }

        // Αγνοεί άρθρα/προθέσεις από τα arguments
        Set<String> ignored = synonymMap != null ? synonymMap.getIgnoredWords() : EMPTY_IGNORED;

        // Δημιουργία της λίστας Ορισμάτων (Arguments)
        String[] rawWords = lowerInput.split("\\s+");
        List<String> arguments = new ArrayList<>();
        // Σαρώνει το υπόλοιπο της πρότασης, παραλείποντας τα άρθρα/προθέσεις
        for (int i = skipTokens; i < rawWords.length; i++) {
            if (!ignored.contains(rawWords[i])) {
                arguments.add(rawWords[i]);
            }
        }

        return new ParsedData(verb, arguments.toArray(new String[0]));
    }

    // DTO (Data Transfer Object)
    // Απλό αντικείμενο που μεταφέρει το αποτέλεσμα στον CommandRegistry.
    public static class ParsedData {
        private String verb;
        private String[] arguments;

        public ParsedData(String verb, String[] arguments) {
            this.verb = verb;
            this.arguments = arguments;
        }

        public String getVerb() { return verb; }
        public String[] getArguments() { return arguments; }
    }
}
