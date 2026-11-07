package adventure.model;

public interface ItemInterface {
    String getName();
    String getDescription();
    void execute(Player player);
}
