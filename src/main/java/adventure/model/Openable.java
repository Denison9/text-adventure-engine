package adventure.model;

public interface Openable {
    String getName();
    boolean isOpen();
    void setOpen(boolean open);
    Condition getUnlockCondition();
}
