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
public class Room{
    // Descripcion de la habitacion
    private String description;
    // Salidas de la habitacion
    private HashMap<String, Room> roomExits;
    // Items que hay en la habitacion
    private ArrayList<Item> listItem;

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
        listItem = new ArrayList<Item>();
    }

    /**
     * Añade un item a la habitacion.
     * @param item El item a añadir.
     */
    public void addItem(Item item){
        //añadimos el item a la lista de ellos en la habitacion.
        listItem.add(item);
    }

    /**
     * Elimina un item de la habitacion.
     * @param item El item a eliminar.
     */
    public void deleteItem(Item item){
        //buscamos el item la lista y si lo encontramos lo eliminamos
        int cont = 0;
        boolean eliminado = false;
        while(cont < listItem.size() && !eliminado){
            if(listItem.get(cont).getItemId().equals(item.getItemId())){
                listItem.remove(cont);
                eliminado = true;
            }
            cont++;
        }
    }

    /**
     * Busca un item en la habitacion.
     * @param name El nombre del item a buscar.
     * @return Item Devuelve el item si lo encuentra o null si no lo encuntra
     */
    public Item searchItem(String name){
        //Busca el item en la lista y lo devuelve en caso de encontrarlo o devuelve null si no lo encuentra
        Item itemSelected = null;
        for(Item itemTemp : listItem){
            if(itemTemp.getItemId().equals(name)){
                itemSelected = itemTemp;
            }
        }
        return itemSelected;
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
        // Recorre el hashMaps de salidas de la habitacion y devuelve el conjunto de las que no son null
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
     * @return Una descripcion de la habitacion incluyendo sus salidas e items
     */
    public String getLongDescription(){
        String longDescription = "\nEstas en " + getDescription() + "\n";
        if(listItem.size() > 0){
            longDescription += "     Contiene :\n"; 
            for(Item itemTemp : listItem){
                longDescription += "     *  " + itemTemp.getItemDescription() + " cuyo peso es de " + itemTemp.getItemWeight() + "\n";
            }
        }else{
            longDescription += "No hay objetos en la habitacion.\n";
        }
        longDescription += "Salidas:" + getExitString() + "\n";
        return longDescription;
    }

    /**
     * Devuelve el numero de items que hay en la habitacion
     * @return El tamaño del array donde estan guardados los items de la habitacion
     */
    public int getListItemSize(){
        return listItem.size();
    }
}
