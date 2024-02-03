import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

class WordCountApplication {
    public static void main(String[] args) throws IOException {
        final int CHUNK_SIZE = 10 * 1024 * 1024; // 10 MB
        final String FILE_PATH = "large_text_file.txt";

       
        generateLargeTextFile(FILE_PATH);

       
        Map<String, Integer> wordCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            char[] buffer = new char[CHUNK_SIZE];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                line = new String(buffer, 0, bytesRead);
                
                
                processChunk(line, wordCounts);
            }
        }

       
        List<Map.Entry<String, Integer>> sortedWordCounts = new ArrayList<>(wordCounts.entrySet());
        sortedWordCounts.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        
        Trie trie = new Trie();
        for (String word : wordCounts.keySet()) {
            trie.insert(word);
        }

        
        List<String> fuzzySearchResults = trie.search("appl");

        
        for (Map.Entry<String, Integer> entry : sortedWordCounts) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("Fuzzy search results: " + fuzzySearchResults);
    }

   
    private static void processChunk(String chunk, Map<String, Integer> wordCounts) {
       
        String[] words = chunk.split("\\s+");

       
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }
    }

    
    private static void generateLargeTextFile(String filePath) {
        final long FILE_SIZE_GB = 1; // 1 GB

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            long fileSizeBytes = 0;
            String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ";
            
            while (fileSizeBytes < FILE_SIZE_GB * 1024 * 1024 * 1024) {
                writer.write(text);
                fileSizeBytes += text.getBytes().length;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;

    TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
    }
}

class Trie {
    private TrieNode root;

    Trie() {
        root = new TrieNode();
    }

    void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEndOfWord = true;
    }

    List<String> search(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return results; 
            }
            node = node.children.get(c);
        }
        
        searchWordsWithPrefix(node, prefix, results);
        return results;
    }

    private void searchWordsWithPrefix(TrieNode node, String prefix, List<String> results) {
        if (node.isEndOfWord) {
            results.add(prefix);
        }
        for (char c : node.children.keySet()) {
            searchWordsWithPrefix(node.children.get(c), prefix + c, results);
        }
    }
}
