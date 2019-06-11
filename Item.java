/**
 * Class Item - an object in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * An "Item" represents one object in the game.
 * 
 * @author  Héctor Robles de Paz
 * @version 2019.06.10
 */
public class Item{
    private String id;
    private String itemDescription;
    private int itemWeight;
    private boolean canBePickedUp;
    
    /**
     * Constructor for objects of class Item
     * @param description  La descripcion del objeto.
     * @param weight  El peso del objeto.
     */
    public Item (String id, String description, int weight, boolean canBePickedUp){
        this.id = id;
        this.itemDescription = description;
        this.itemWeight = weight;
        this.canBePickedUp = canBePickedUp;
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
     * @return Si el objeto puede ser cogido o no.
     */
    public boolean getItemCanBePickedUp()
    {
        return canBePickedUp;
    }
    
    /**
     * @return El id del objeto.
     */
    public String getItemId()
    {
        return id;
    }
}
