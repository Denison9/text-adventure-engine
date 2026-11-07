package adventure.model;

public class HasItemCondition implements Condition {
    private String requiredItemId;

    public HasItemCondition(String requiredItemId) {
        this.requiredItemId = requiredItemId;
    }

    @Override
    public boolean isMet(Player player) {
        return player.hasItem(requiredItemId);
    }

    @Override
    public String getFailMessage() {
        return "You can't do that. You need the item: " + requiredItemId;
    }
}
