package adventure.loader;

import adventure.model.Condition;
import adventure.model.Door;
import adventure.model.Exit;
import adventure.model.HasItemCondition;
import adventure.model.Item;
import adventure.model.Room;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Διαβάζει JSON αρχείο και φτιάχνει τα δωμάτια του παιχνιδιού.
//Υπεύθυνος για το Data Deserialization.

public class JsonGameLoader implements GameLoader {

    private String startingRoomId = null;

    public String getStartingRoomId() {
        return startingRoomId;
    }

    @Override
    public List<Room> loadRooms(String filePath) {
        Gson gson = new Gson();
        List<Room> finalRooms = new ArrayList<>();
        Map<String, Room> tempMap = new HashMap<>();

        try (Reader reader = new FileReader(filePath)) {
            // Διαβάζει όλο το JSON σε αντικείμενα GameData/RoomData/ItemData/OpenableData.
            GameData data = gson.fromJson(reader, GameData.class);

            if (data != null && data.rooms != null) {
                this.startingRoomId = data.startingRoom;

                // 1ο πέρασμα: δημιουργεί όλα τα Room χωρίς exits ακόμα.
                // Χρειάζεται γιατί τα exits δείχνουν σε άλλα δωμάτια.
                for (RoomData rd : data.rooms) {
                    Room room = new Room(rd.id, rd.name, rd.description);
                    room.setWinningRoom(rd.isWinningRoom);
                    room.setRequiredItemToWin(rd.requiredItemToWin);
                    finalRooms.add(room);
                    tempMap.put(rd.id, room);
                }

                // 2ο πέρασμα: συνδέει exits, προσθέτει items και πόρτες.
                for (RoomData rd : data.rooms) {
                    Room currentRoom = tempMap.get(rd.id);

                    // Exits: για κάθε direction βρίσκει το target Room και φτιάχνει Exit.
                    if (rd.exits != null) {
                        for (Map.Entry<String, String> entry : rd.exits.entrySet()) {
                            String direction    = entry.getKey();
                            String targetRoomId = entry.getValue();
                            Room targetRoom     = tempMap.get(targetRoomId);
                            if (targetRoom != null) {
                                boolean locked = rd.lockedExits != null && rd.lockedExits.contains(direction);
                                currentRoom.setExit(direction, targetRoom, locked);
                            }
                        }
                    }

                    // Items: προστίθενται στο δωμάτιο.
                    if (rd.items != null) {
                        for (ItemData itemData : rd.items) {
                            currentRoom.addItem(new Item(itemData.name, itemData.description, itemData.readableText));
                        }
                    }

                    // Openables (πόρτες): όταν χρειάζονται κάποιο item, μπαίνει
                    // HasItemCondition. Όταν συνδέονται με κατεύθυνση, ξεκλειδώνουν
                    // αυτόματα το αντίστοιχο Exit μόλις ανοίξουν.
                    if (rd.openables != null) {
                        for (OpenableData od : rd.openables) {
                            Condition condition = null;
                            if (od.requiresItem != null && !od.requiresItem.isEmpty()) {
                                condition = new HasItemCondition(od.requiresItem);
                            }
                            Door door = new Door(od.name, od.initiallyOpen, condition);
                            if (od.linkedDirection != null) {
                                Exit linked = currentRoom.getExit(od.linkedDirection);
                                if (linked != null) {
                                    door.setLinkedExit(linked);
                                }
                            }
                            currentRoom.addOpenable(door);
                        }
                    }
                }

                // Τοποθετεί το αρχικό δωμάτιο πρώτο στη λίστα.
                if (startingRoomId != null && tempMap.containsKey(startingRoomId)) {
                    Room start = tempMap.get(startingRoomId);
                    finalRooms.remove(start);
                    finalRooms.add(0, start);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading game data: " + e.getMessage());
        }
        return finalRooms;
    }

    // Inner classes (Data Transfer Objects) που αντιστοιχούν στο JSON schema (τα διαβάζει η Gson).

    private static class GameData {
        String startingRoom;
        List<RoomData> rooms;
    }

    private static class RoomData {
        String id;
        String name;
        String description;
        Map<String, String> exits;
        List<String> lockedExits;
        List<ItemData> items;
        List<OpenableData> openables;
        boolean isWinningRoom;
        String requiredItemToWin;
    }

    private static class ItemData {
        String name;
        String description;
        String readableText; // προαιρετικό — μόνο για αναγνώσιμα αντικείμενα
    }

    private static class OpenableData {
        String name;
        boolean initiallyOpen;
        String requiresItem;
        String linkedDirection;
    }
}
