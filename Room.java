import java.util.*;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */
public class Room 
{
    private String description;
    private HashMap<String, Room> roomExits;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        roomExits = new HashMap<String, Room>();
    }

    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     */
    public void setExits(Room north, Room east, Room south, Room west, Room northEast, Room southEast, Room southWest, Room northWest) 
    {
        roomExits.put("north", north);
        roomExits.put("east", east);
        roomExits.put("south", south);
        roomExits.put("west", west);
        roomExits.put("northeast", northEast);
        roomExits.put("southeast", southEast);
        roomExits.put("southwest", southWest);
        roomExits.put("northwest", northWest);
    }

    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param The direction we want to go.
     * @return The Room of the exit.
     */
    public Room getExit(String direction){
        return roomExits.get(direction);
    }

    /**
     * Devuelve la información de las salidas existentes
     * Por ejemplo: "Exits: north east west"
     *
     * @return Una descripción de las salidas existentes.
     */
    public String getExitString(){
        String exits = "";
        for(Map.Entry<String, Room> exitTemp : roomExits.entrySet()){
            if(exitTemp.getValue() != null)
            exits += " " + exitTemp.getKey();
        }
        return exits;
    }
}
