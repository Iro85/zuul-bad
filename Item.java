
/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    private String itemDescription;
    private int itemWeight;
    
    /**
     * Constructor for objects of class Item
     * @param description  La descripcion del objeto.
     * @param weight  El peso del objeto.
     */
    public Item (String description, int weight){
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
}
