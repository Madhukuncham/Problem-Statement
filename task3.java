import java.util.*;

class Product {
    private String id;
    private String name;
    private String category;
    private String description;
    private Map<String, String> details;

    public Product(String id, String name, String category, String description, Map<String, String> details) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", details=" + details +
                '}';
    }
}

class ProductConfiguration {
    private Map<String, Product> products;
    private Map<String, Map<String, String>> categoryIndex;
    private Trie textualSearchIndex;

    public ProductConfiguration() {
        this.products = new HashMap<>();
        this.categoryIndex = new HashMap<>();
        this.textualSearchIndex = new Trie();
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        categoryIndex.computeIfAbsent(product.getCategory(), k -> new HashMap<>()).put(product.getId(), product.getName());
        updateTextualSearchIndex(product);
    }

    private void updateTextualSearchIndex(Product product) {
        textualSearchIndex.insert(product.getName(), product.getId());
        textualSearchIndex.insert(product.getDescription(), product.getId());
        for (Map.Entry<String, String> entry : product.getDetails().entrySet()) {
            textualSearchIndex.insert(entry.getValue(), product.getId());
        }
    }

    public Product getProductById(String productId) {
        return products.get(productId);
    }

    public Map<String, String> searchByCategory(String category) {
        return categoryIndex.getOrDefault(category, new HashMap<>());
    }

    public Map<String, String> searchText(String query) {
        return textualSearchIndex.search(query);
    }
}

class TrieNode {
    Map<Character, TrieNode> children;
    Map<String, String> products;

    public TrieNode() {
        this.children = new HashMap<>();
        this.products = new HashMap<>();
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word, String productId) {
        TrieNode node = root;
        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }
        node.products.put(productId, productId);
    }

    public Map<String, String> search(String query) {
        TrieNode node = root;
        for (char ch : query.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return new HashMap<>();
            }
            node = node.children.get(ch);
        }
        return node.products;
    }
}

class Task3 {
    public static void main(String[] args) {
        ProductConfiguration config = new ProductConfiguration();
        Product laptop = new Product("1", "Laptop", "Electronics", "Powerful laptop with high-resolution display",
                Map.of("Processor", "Intel i7", "RAM", "16GB", "Storage", "512GB SSD"));
        Product book = new Product("2", "Java Programming", "Books", "Comprehensive guide to Java programming",
                Map.of("Author", "John Doe", "Pages", "500", "ISBN", "123456789"));
        config.addProduct(laptop);
        config.addProduct(book);
		Scanner sc=new Scanner (System.in);
		System.out.println("enter the id number");
		String id_no=sc.next();

        Product retrievedProduct = config.getProductById(id_no);
        System.out.println("Retrieved Product: " + retrievedProduct);
        Map<String, String> electronicsProducts = config.searchByCategory("Electronics");
        System.out.println("Electronics Products: " + electronicsProducts);
        Map<String, String> searchResults = config.searchText("Java Programming");
        System.out.println("Search Results: " + searchResults);
    }
}
