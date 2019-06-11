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

public class Game{
    private Parser parser;
    //Jugador
    private Player zuulPlayer;
    //peso maximo transportable para un jugador base sin caracteristicas especiales
    private static final int MAX_WEIGHT = 30;

    /**
     * Create the game and initialise its internal map.
     */
    public Game(){
        parser = new Parser();
        zuulPlayer = new Player(createRooms(), MAX_WEIGHT);
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private Room createRooms(){
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
        calabozo.addItem(new Item ("cadenas", "cadenas de metal", 10, true));
        calabozo.addItem(new Item ("grilletes", "grilletes oxidados", 5, true));
        patio.addItem(new Item ("banco", "banco de madera", 30, false));
        establos.addItem(new Item ("montura", "montura de cuero", 5, true));
        granSalonReal.addItem(new Item ("trono", "trono del rey", 50, false));
        capilla.addItem(new Item ("cruz", "cruz cristiana", 25, false));
        capilla.addItem(new Item ("caliz", "caliz sagrado", 5, true));
        capilla.addItem(new Item ("virgen", "estatua de la virgen maria", 35, false));
        capilla.addItem(new Item ("bancos", "bancos antiguos", 60, false));
        cocina.addItem(new Item ("cazuela", "cazuela mediana", 10, true));
        cocina.addItem(new Item ("mesa", "mesa alargada", 20, false));
        cocina.addItem(new Item ("sarten", "sarten grande", 5, true));
        cocina.addItem(new Item ("barril", "barril de cerveza", 40, false));
        herreria.addItem(new Item ("maza", "maza oscura", 15, true));
        herreria.addItem(new Item ("espada", "espada grande a 2 manos", 10, true));
        herreria.addItem(new Item ("escudo", "escudo reforzado", 10, true));
        herreria.addItem(new Item ("horno", "horno incandescente", 80, false));
        
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

        return calabozo;  // start game calabozo
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
        zuulPlayer.look();
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
            zuulPlayer.goRoom(command);
        }
        else if (commandWord.equals("look")) {  
            zuulPlayer.look();
        }
        else if (commandWord.equals("eat")) {   
            zuulPlayer.eat();
        }
        else if (commandWord.equals("back")) {  
            zuulPlayer.back();
        }
        else if (commandWord.equals("take")) {
            zuulPlayer.pickUpItem(command);
        }
        else if (commandWord.equals("drop")) {
            zuulPlayer.dropItem(command);
        }
        else if (commandWord.equals("items")) {  
            zuulPlayer.items();
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
}