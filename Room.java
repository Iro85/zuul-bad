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
    private Room northExit;
    private Room southExit;
    private Room eastExit;
    private Room westExit;
    private Room northEastExit;
    private Room southEastExit;
    private Room southWestExit;
    private Room northWestExit;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
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
        if(north != null)
            northExit = north;
        if(east != null)
            eastExit = east;
        if(south != null)
            southExit = south;
        if(west != null)
            westExit = west;
        if(northEast != null)
            northEastExit = northEast;
        if(southEast != null)
            southEastExit = southEast;
        if(southWest != null)
            southWestExit = southWest;
        if(northWest != null)
            northWestExit = northWest;
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
        Room exit = null;
        switch(direction){
            case "north" : exit = northExit; break;
            case "east" : exit = eastExit; break;
            case "south" : exit = southExit; break;
            case "west" : exit = westExit; break;
            case "northeast" : exit = northEastExit; break;
            case "southeast" : exit = southEastExit; break;
            case "southwest" : exit = southWestExit; break;
            case "northwest" : exit = northWestExit; break;
            default : exit = null; break;
        }
        return exit;
    }

    /**
     * Devuelve la información de las salidas existentes
     * Por ejemplo: "Exits: north east west"
     *
     * @return Una descripción de las salidas existentes.
     */
    public String getExitString(){
        String exits = "";
        if(northExit != null)
            exits += " north";
        if(eastExit != null)
            exits += " east";
        if(southExit != null)
            exits += " south";
        if(westExit != null)
            exits += " west";
        if(northEastExit != null)
            exits += " northEast";
        if(southEastExit != null)
            exits += " southEast";
        if(southWestExit != null)
            exits += " southWest";
        if(northWestExit != null)
            exits += " northWest";
        return exits;
    }
}
