package adventure.loader;

import adventure.model.Room;
import java.util.List;

public interface GameLoader {
    List<Room> loadRooms(String source);
}
