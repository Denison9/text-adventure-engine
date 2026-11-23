package adventure.model;

public class Exit {
    private String direction;
    private boolean isLocked;
    private Room targetRoom;

    public Exit(String direction, Room targetRoom, boolean isLocked) {
        this.direction = direction;
        this.targetRoom = targetRoom;
        this.isLocked = isLocked;
    }

    public Room getTargetRoom() { return targetRoom; }
    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }
}
