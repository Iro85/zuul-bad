import java.util.*;
/**
 * Class Player - a character in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Player" represents one character in the game.
 * 
 * @author  Héctor Robles de Paz
 * @version 2019.06.10
 */
public class Player{
    // Habitacion actual
    private Room currentRoom;
    // Pila donde guardamos las habitaciones por las que hemos ido pasando
    private Stack<Room> previousRoom;
    // ArryList donde guardamos los objetos que transportamos
    private ArrayList<Item> itemBackpack;
    // Peso maximo que podemos transportar
    private int maxWeight;
    //distancia entre el nombre de item y su peso
    private static final int DISTANCE = 23;

    /**
     * Constructor for objects of class Player
     */
    public Player(Room currentRoom, int maxWeight){
        this.currentRoom = currentRoom;
        itemBackpack = new ArrayList<Item>();
        previousRoom = new Stack<Room>();
        this.maxWeight = maxWeight;
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    public void goRoom(Command command){
        if(!command.hasSecondWord()){
            // if there is no second word, we don't know where to go...
            System.out.println("¿A donde quieres ir?");
            return;
        }
        String direction = command.getSecondWord();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null){
            System.out.println("¡Ahi no hay salida!");
        }
        else{
            previousRoom.push(currentRoom);
            currentRoom = nextRoom;
            look();
            if(currentRoom.getDescription().equals("una caverna oscura")){
                cavernaFound();
            }
        }
    }

    /** 
     * Vuelve a la habitacion anterior
     */
    public void back(){
        //comprueba si la pila previousRoom esta vacia y si no volvemos a la ultima posicion
        if(previousRoom.isEmpty()){
            System.out.println("Estas en el principio.");
        }else{
            currentRoom = previousRoom.pop();
            look();
        }
    }

    /** 
     * Print information of the location
     */
    public void look() {   
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * Imprime un texto sobre la accion de comer
     */
    public void eat(){
        System.out.println("Has comido ahora y ya no tienes hambre.");
    }

    /**
     * Metodo de acciones para cuando ejecutamos el comando take.
     * @param command El comando ha ser procesado.
     */
    public void pickUpItem(Command command){
        //Comprobamos que la habitacion tiene objetos
        if(currentRoom.getListItemSize() > 0){
            if(command.hasSecondWord()){
                String itemName = command.getSecondWord();
                // buscamos el objeto en la habitacion para cogerlo
                Item itemSelected = currentRoom.searchItem(itemName);
                checkSpecialItems(itemSelected);
                if(itemSelected == null) {
                    System.out.println("El objeto no esta en la habitacion.");
                }else{
                    //comprobamos si el objeto puede ser cogido y si el peso no excederia el maximo que podamos llevar
                    if(!itemSelected.getItemCanBePickedUp()){
                        System.out.println("El objeto es muy pesado para llevarlo.");
                    }else if(canTake(itemSelected)){
                        itemBackpack.add(itemSelected);
                        currentRoom.deleteItem(itemSelected);
                        System.out.println("Objeto " + itemSelected.getItemDescription() + " cogido");
                    }else{
                        System.out.println("El objeto supera nuestra capacidad."); 
                    }
                }
            }else{
                // si no tiene segunda palabra no sabemos que coger...
                System.out.println("¿Que quieres coger?");
                return;
            }
        }else{
            // Si no hay posibles items para coger mostramos un mensaje
            System.out.println("No hay objetos para coger en la habitación");
        }
    }

    /**
     * Metodo de acciones para cuando ejecutamos el comando drop.
     * Metodo para soltar objetos.
     * @param command El comando ha ser procesado.
     */
    public void dropItem(Command command){
        if(command.hasSecondWord()){
            String itemName = command.getSecondWord();
            // buscamos el objeto en la mochila para dejarlo
            Item itemSelected = searchItem(itemName);
            if (itemSelected == null) {
                System.out.println("El objeto no esta en la mochila.");
            }
            else {
                currentRoom.addItem(itemSelected);
                deleteItem(itemSelected);
                System.out.println("Objeto " + itemSelected.getItemDescription() + " dejado");
            }
        }else{
            // si no tiene segunda palabra no sabemos que coger...
            System.out.println("¿Que quieres dejar?");
            return;
        }
    }

    /**
     * Metodo para buscar un item en la mochila.
     * @param name El nombre del item que buscamos.
     */
    public Item searchItem(String name){
        //buscamos el item en la mochila y los devolvemos si lo encontramos o null en caso de que no este
        Item itemSelected = null;
        for(Item itemTemp : itemBackpack){
            if(itemTemp.getItemId().equals(name)){
                itemSelected = itemTemp;
            }
        }
        return itemSelected;
    }

    /**
     * Metodo que elimina un item de la mochila.
     * @param command El nombre del item que buscamos.
     */
    public void deleteItem(Item item){
        //buscamos el item en la mochila y lo eliminamos si lo encontramos
        int cont = 0;
        boolean eliminado = false;
        while(cont < itemBackpack.size() && !eliminado){
            if(itemBackpack.get(cont).getItemId().equals(item.getItemId())){
                itemBackpack.remove(cont);
                eliminado = true;
            }
            cont++;
        }
    }

    /**
     * Metodo que nos muestra los items de la mochila en el caso de que los haya.
     */
    public void items(){
        //Si hay objetos los mostramos junto con sus caracteristicas y el peso total que haya
        if(itemBackpack.size() > 0){
            System.out.println("Contenido de la mochila:");
            for(Item itemTemp : itemBackpack){
                System.out.println("   " + itemTemp.getItemDescription() + getBlankSpaces(itemTemp.getItemDescription() ) + "| Peso - " + itemTemp.getItemWeight());
            }
            System.out.println("                      Peso total - " + loadedWeight());
        }else{
            System.out.println("No tienes objetos en la mochila.");
        }
    }

    /**
     * Metodo que nos indica si un item excederia el peso y si podria ser cogido.
     * @param command El item del que queremos evaluar el peso.
     * @return true Si al coger el item no excedemos el peso que podemos llevar en la mochila.
     */
    public boolean canTake(Item item){
        // Comprobamos si el peso de la mochila sumado al del objeto no excede del maximo transportable y devolvemos true si no lo excede 
        // o false en caso contrario
        boolean acceptedWeight = false;
        if(loadedWeight() + item.getItemWeight() <= maxWeight){
            acceptedWeight = true;
        }
        return acceptedWeight;
    }

    /**
     * Metodo que nos devuelve el peso que llevamos en la mochila en ese momento.
     * @return weight El peso total de los items que llevamos en ese momento.
     */
    public int loadedWeight(){
        // Suma los pesos de todos los objetos de la mochila y devuelve la suma. Si no hay objetos devuelve 0.
        int weight = 0;
        if(itemBackpack.size() > 0){
            for(Item itemTemp : itemBackpack){
                weight += itemTemp.getItemWeight();
            }
        }
        return weight;
    }

    /**
     * Metodo que nos devuelve los espacios en blanco necesarios para que quede colocada la lista de items.
     * @param name El nombre del item que se va a poner.
     * @return String Los espacios en blanco necesarios para que quede colocado el item.
     */
    public String getBlankSpaces(String name){
        String blankSpaces = "";
        for(int cont = 0; cont < DISTANCE - name.length(); cont++){
            blankSpaces += " ";
        }
        return blankSpaces;
    }

    /**
     * Metodo de acciones para cuando ejecutamos el comando drink.
     * Bebemos el caliz y sube la capacidad de llevar mas peso.
     * @param command El comando ha ser procesado.
     */
    public void drink(Command command){
        if(command.hasSecondWord()){
            String itemName = command.getSecondWord();
            // buscamos el objeto en la mochila para beberlo
            Item itemSelected = searchItem(itemName);
            if (itemSelected == null){ 
                System.out.println("El objeto no esta en la mochila.");
            }else if (!itemSelected.getItemId().equals("caliz")) {
                System.out.println("El objeto " + itemSelected.getItemDescription() + " no se puede beber");
            }else{
                deleteItem(itemSelected);
                maxWeight *= 2;
                System.out.println("Te has bebido el " + itemSelected.getItemDescription() + " y ahora puedes llevar el doble de peso.");
            }
        }else{
            // si no tiene segunda palabra no sabemos que coger...
            System.out.println("¿Que quieres beber?");
            return;
        }
    }

    /**
     * Metodo de acciones para cuando encontramosun Item especial.
     * @param Item El item a analizar.
     */
    public void checkSpecialItems(Item itemTemp){
        if(itemTemp != null && itemTemp.getItemId().equals("caliz")){
            System.out.println("Tiene un extraño vino en su interior......tal vez deberia beberlo a ver que pasa....");
        }else if(itemTemp != null && itemTemp.getItemId().equals("cruz")){
            System.out.println("No la puedes transportar pero al intentar levantarla has encontrado una llave...puede que ayude a liberarte....");
            Item key = new Item ("llave", "llave maestra de plata", 5, true);
            if(canTake(key)){
                itemBackpack.add(key);
            }else{
                currentRoom.addItem(key);
            }
        }
    }

    /**
     * Metodo de acciones para cuando ejecutamos el comando use.
     * Comprobamos si estamos en la entrada y si usamos la llave para poder salir y finalizar el juego.
     * @param command El comando ha ser procesado.
     */
    public void use(Command command){
        if(command.hasSecondWord()){
            String itemName = command.getSecondWord();
            // buscamos el objeto en la mochila para beberlo
            Item itemSelected = searchItem(itemName);
            if (itemSelected == null){ 
                System.out.println("El objeto no esta en la mochila.");
            }else if (!itemSelected.getItemId().equals("llave")) {
                System.out.println("El objeto " + itemSelected.getItemDescription() + " no se puede usar");
            }else if (!currentRoom.getDescription().equals("la puerta de entrada")){
                System.out.println("La llave no se puede usar en esta habitacion.");
            }else{
                System.out.println("¡¡¡Has encontrado la salida!!!");
                System.out.println("¡¡¡Conseguiste salir con vida del castillo!!!");
                System.out.println(".......................................");
                System.out.println("Se escucha un pequeño temblor....y se abre una pequeña abertura a tu lado");
                System.out.println("Ahora esta en tus manos salir y escapar (quit) o entrar y arriesgarte......");
                Room caverna = new Room("una caverna oscura");
                caverna.addItem(new Item ("piedras", "piedras y mas piedras", 100, false));
                currentRoom.setExit("west", caverna);
            }
        }else{
            // si no tiene segunda palabra no sabemos que coger...
            System.out.println("¿Que quieres usar?");
            return;
        }
    }

    /*
     * Mensajes de la caverna
     */
    private void cavernaFound(){
        System.out.println("Al entrar fuerzas el pequeño agujero y se derrumba tras de ti dejandote atrapado.");
        System.out.println("Consigues agarrar una pequeña antorcha pero solo ves piedras haya donde mires.....");
        System.out.println("Te das cuenta de que te has quedado atrapado y de que ya no tienes salida.");
        System.out.println("Pudistes salir hacia la libertad pero el ansia te pudo y ahora estas atrapado para toda la eternidad.....");
    }
}