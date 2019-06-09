import java.util.*;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    // Habitacion actual
    private Room currentRoom;
    // Pila donde guardamos las habitaciones por las que hemos ido pasando
    private Stack<Room> previousRoom;
    // ArryList donde guardamos los objetos que transportamos
    private ArrayList<Item> itemBackpack;
    // Peso maximo que podemos transportar
    private static final int MAX_WEIGHT = 30;
    //distancia entre el nombre de item y su peso
    private static final int DISTANCE = 12;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = new Stack<Room>();
        itemBackpack = new ArrayList<Item>();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room calabozo, torreon, patio, establos, granSalonReal, capilla, cocina, pozo, herreria, puertaDeEntrada;

        // create the rooms
        calabozo = new Room("el calabozo");
        torreon = new Room("el torreon");
        patio = new Room("el patio");
        establos = new Room("los establos");
        granSalonReal = new Room("el gran salon real");
        capilla = new Room("la capilla");
        cocina = new Room("la cocina");
        pozo = new Room("el pozo");
        herreria = new Room("la herreria");
        puertaDeEntrada = new Room("la puerta de entrada");

        // inicializamos los objetos de las salas
        calabozo.addItem(new Item ("cadenas", 10));
        calabozo.addItem(new Item ("grilletes", 5));
        patio.addItem(new Item ("banco", 30));
        establos.addItem(new Item ("montura", 5));
        granSalonReal.addItem(new Item ("trono", 50));
        capilla.addItem(new Item ("cruz", 25));
        capilla.addItem(new Item ("caliz", 5));
        capilla.addItem(new Item ("bancos", 60));
        cocina.addItem(new Item ("cazuela", 10));
        cocina.addItem(new Item ("mesa", 20));
        cocina.addItem(new Item ("sarten", 5));
        herreria.addItem(new Item ("maza", 15));
        herreria.addItem(new Item ("espada", 10));
        herreria.addItem(new Item ("escudo", 10));

        // initialise room exits
        calabozo.setExit("north", torreon);
        torreon.setExit("north", pozo);
        torreon.setExit("east", patio);
        torreon.setExit("south", calabozo);
        patio.setExit("north", granSalonReal);
        patio.setExit("east", herreria);
        patio.setExit("south", puertaDeEntrada);
        patio.setExit("west", torreon);
        patio.setExit("northwest", pozo);
        establos.setExit("northwest", patio);
        granSalonReal.setExit("east", cocina);
        granSalonReal.setExit("south", patio);
        granSalonReal.setExit("west", capilla);
        capilla.setExit("east", granSalonReal);
        cocina.setExit("west", granSalonReal);
        pozo.setExit("south", torreon);
        herreria.setExit("west", patio);
        herreria.setExit("southwest", establos);
        puertaDeEntrada.setExit("north", patio);

        currentRoom = calabozo;  // start game calabozo
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Gracias por jugar. Hasta luego.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("¡¡¡Bienvenido al mundo de Zuul!!!");
        System.out.println("En esta ocasión en un castillo medieval, recien liberado de tu cautiverio en el calabozo.");
        System.out.println("Escribe 'help' si necesitas ayuda.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("No se lo que quieres decir...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("look")) {  
            look();
        }
        else if (commandWord.equals("eat")) {   
            eat();
        }
        else if (commandWord.equals("back")) {  
            back();
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("items")) {  
            items();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Estas perdido. Estas solo. Estas deambulando dentro del castillo");
        System.out.println();
        System.out.println("Tus comandos son:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("¿A donde quieres ir?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("¡Ahi no hay salida!");
        }
        else {
            previousRoom.push(currentRoom);
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("¿Que quieres quitar?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /** 
     * Print information of the location
     */
    private void printLocationInfo(){
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * Print information of the location
     */
    private void look() {   
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * Imprime un texto sobre la accion de comer
     */
    private void eat(){
        System.out.println("Has comido ahora y ya no tienes hambre.");
    }

    /** 
     * Vuelve a la habitacion anterior
     */
    private void back(){
        //comprueba si la pila previousRoom esta vacia y si no volvemos a la ultima posicion
        if(previousRoom.isEmpty()){
            System.out.println("Estas en el principio.");
        }else{
            currentRoom = previousRoom.pop();
            printLocationInfo();
        }
    }

    /**
     * Metodo de acciones para cuando ejecutamos el comando take.
     * @param command El comando ha ser procesado.
     */
    public void take(Command command){
        //Comprobamos que la habitacion tiene objetos
        if(currentRoom.getListItemSize() > 0){
            if(command.hasSecondWord()){
                String itemName = command.getSecondWord();
                // buscamos el objeto en la habitacion para cogerlo
                Item itemSelected = currentRoom.searchItem(itemName);
                if(itemSelected == null) {
                    System.out.println("El objeto no esta en la habitacion.");
                }else{
                    //comprobamos si el peso no excederia el maximo que podamos llevar
                    if(canTake(itemSelected)){
                        itemBackpack.add(itemSelected);
                        currentRoom.deleteItem(itemSelected);
                        System.out.println("Objeto " + itemSelected.getItemDescription() + " cogido");
                    }else if(itemSelected.getItemWeight() > MAX_WEIGHT){
                        System.out.println("El objeto es muy pesado para llevarlo.");
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
     * @param command El comando ha ser procesado.
     */
    public void drop(Command command){
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
            if(itemTemp.getItemDescription().equals(name)){
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
            if(itemBackpack.get(cont).getItemDescription().equals(item.getItemDescription())){
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
        //SI hay objetos los mostramos junto con sus caracteristicas y el peso total que haya
        if(itemBackpack.size() > 0){
            System.out.println("Tienes en la mochila:");
            for(Item itemTemp : itemBackpack){
                System.out.println("Objeto - " + itemTemp.getItemDescription() + getBlankSpaces(itemTemp.getItemDescription() ) + "| Peso - " + itemTemp.getItemWeight());
            }
            System.out.println("                 Peso total - " + loadedWeight());
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
        if(loadedWeight() + item.getItemWeight() <= MAX_WEIGHT){
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
}
