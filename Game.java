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
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private boolean backMaked;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        backMaked = false;
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
        patio.addItem(new Item ("banco de madera", 20));
        establos.addItem(new Item ("montura", 5));
        granSalonReal.addItem(new Item ("trono", 50));
        capilla.addItem(new Item ("cruz", 25));
        cocina.addItem(new Item ("cazuela", 10));
        herreria.addItem(new Item ("maza", 15));
        
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
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
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
            System.out.println("I don't know what you mean...");
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
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
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
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom;
            currentRoom = nextRoom;
            backMaked = false;
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
            System.out.println("Quit what?");
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
        System.out.println("You have eaten now and you are not hungry any more");
    }
    
    /** 
     * Vuelve a la habitacion anterior
     */
    private void back(){
        if(!backMaked){
            currentRoom = previousRoom;
            backMaked = true;
            printLocationInfo();
        }
    }
}
