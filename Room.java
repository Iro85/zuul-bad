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
    private String itemDescription;
    private int itemWeight;
    
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
     * Define una salida para la habitacion.
     * @param direction El nombre de la direccion de la salida
     * @param neighbor La habitacion a la que se llega usando esa salida
     */
    public void setExit(String direction, Room neighbor){
        roomExits.put(direction, neighbor);
    }
    
    /**
     * Define un objeto para la habitacion.
     * @param description  La descripcion del objeto.
     * @param weight  El peso del objeto.
     */
    public void setItem (String description, int weight){
        this.itemDescription = description;
        this.itemWeight = weight;
    }
    
    /**
     * @return La descripcion del objeto.
     */
    public String getItemDescription()
    {
        return itemDescription;
    }
    
    /**
     * @return El peso del objeto.
     */
    public int getItemWeight()
    {
        return itemWeight;
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

    /**
     * Devuelve un texto con la descripcion larga de la habitacion del tipo:
     *     You are in the 'name of room'
     *     Exits: north west southwest
     * @return Una descripcion de la habitacion incluyendo sus salidas
     */
    public String getLongDescription(){
        String longDescription = "\nEstas en " + getDescription() + "\n";
        if(this.itemWeight > 0){
            longDescription += "Contiene " + this.itemDescription + " cuyo peso es de " + this.itemWeight + "\n";
        }
        longDescription += "Salidas:" + getExitString() + "\n";
        return longDescription;
    }
}
