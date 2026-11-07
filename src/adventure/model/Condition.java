package adventure.model;

public interface Condition {
    boolean isMet(Player player);
    String getFailMessage();
}
