package adventure.model;

public class Item implements ItemInterface {
    private String name;
    private String description;
    private String readableText; // null αν το αντικείμενο δεν μπορεί να διαβαστεί

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        this.readableText = null;
    }

    public Item(String name, String description, String readableText) {
        this.name = name;
        this.description = description;
        this.readableText = readableText;
    }

    @Override public String getName() { return this.name; }
    @Override public String getDescription() { return this.description; }
    public String getReadableText() { return this.readableText; }
    public boolean isReadable() { return this.readableText != null && !this.readableText.isEmpty(); }

    @Override
    public void execute(Player player) {
        System.out.println("You examine: " + this.name);
        System.out.println("Description: " + this.description);
    }
}
