package adventure.model;

public class Door implements Openable {
    private final String name;
    private boolean open;
    private Condition unlockCondition;
    private Exit linkedExit;

    public Door(String name, boolean initiallyOpen, Condition unlockCondition) {
        this.name = name;
        this.open = initiallyOpen;
        this.unlockCondition = unlockCondition;
    }

    @Override public String getName() { return name; }
    @Override public boolean isOpen() { return open; }

    @Override
    public void setOpen(boolean open) {
        this.open = open;
        if (linkedExit != null) {
            linkedExit.setLocked(!open);
        }
    }

    @Override
    public Condition getUnlockCondition() {
        return unlockCondition;
    }

    public void setLinkedExit(Exit exit) {
        this.linkedExit = exit;
        if (exit != null && open) exit.setLocked(false);
    }

    public Exit getLinkedExit() {
        return linkedExit;
    }
}
