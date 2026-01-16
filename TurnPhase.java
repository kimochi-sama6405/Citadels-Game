package citadels;
import java.util.*;

public class TurnPhase {
    private final Map<CharacterCards, Integer> character_owners; //Maps each character card to the player who owns it for this round.
    private final Map<Integer, Player> players; //Maps each player number to their corresponding Player object.
    private final Set<CharacterCards> killed_CharacterCards; //store the killed characters in a set (doesn't allow duplicates unlike list + fast look up)
    private CharacterCards thief_target; //the charcter thief is trying to steal from
    private CharacterCards assassin_target; //the character assassin kills
    private int crowned_player;

    public TurnPhase(Map<CharacterCards, Integer> character_owners, int num_players, int crowned_player){
        this.character_owners = character_owners;
        this.players = new HashMap<>();
        this.killed_CharacterCards = new HashSet<>();
        this.thief_target = null;
        this.assassin_target = null;
        this.crowned_player = crowned_player;

        for(int i = 1; i <= num_players; i++){ //so its not out of bound
            players.put(i, new Player(i)); //Creates a new Player object with their ID  (number) as i. Store the corresponding ID and PLayer(ID) to a map called "players" => e.g (1, Player 1)
        }
    }

    public void run(Scanner scanner){ 
        for(CharacterCards character : CharacterCards.values()){ //loop over each character in the enum
            System.out.println((character.ordinal() + 1) + ": " + character); //ordinal() gives the position of the enum constant => goes from 0,1,2,3...

            Integer player_number = character_owners.get(character); //get() in hashmap gives the value that corresponds to the key that we pass in => checks which player has the character and if no values (player_number) correspond to the key (character card) then hteres no player who chose that character card
            if(player_number == null){
                System.out.println("No one is the " + character);
                continue; //if no one chose that character card, skip the rest of the code and loop back to the next character card
            } 

            Player player = players.get(player_number); //assign the object "player" with the value (player (ID)) that corresponds to the player_number (ID) in the hashmap "players"; 
            System.out.println("Player " + player_number + " is the " + character);

            
            if(killed_CharacterCards.contains(character)){ //if the character card we are looping over appears in the set of killed characters
                System.out.println("Player " + player_number + " loses their turn because they were assassinated.");
                continue; //so immediately skip the character and to prevent a killed character from still getting to run their special ability
            }
            if(player_number == 1){ //human player
                System.out.println("Your turn");
                humanPlayer(scanner, player, character);
            }else{
                computerPlayer(player, character);
            }
        }
    }
    private void humanPlayer(Scanner scanner, Player player, CharacterCards character){
        if(character == CharacterCards.ASSASSIN){
            System.out.print("Who do you want to kill? Choose a character from 2-8:\n> ");
            int choice = Integer.parseInt(scanner.nextLine());
            assassin_target = CharacterCards.values()[choice - 1]; //bcz enum constant positions start from 0
            killed_CharacterCards.add(assassin_target);
            System.out.println("You chose to kill the " + assassin_target);
        }else if(character == CharacterCards.THIEF){
            System.out.print("Who do you want to steal from? Choose a character from 3-8:\n> ");
            int choice = Integer.parseInt(scanner.nextLine());
            thief_target = CharacterCards.values()[choice - 1];
            System.out.println("You chose to steal from " + thief_target);
        }else if(character == CharacterCards.MAGICIAN){
            System.out.println("Magician: You may swap hands with another player or discard and redraw.");
            System.out.print("Enter 'swap player <player_number> or 'redraw <number>'\n> ");
            String choice = scanner.nextLine().trim();
            if(choice.startsWith("swap player")){
                String[] parts = choice.split("\\s+"); //split the entire input into an array e.g:["swap","player",'1']
                int targetID = Integer.parseInt(parts[2]); //take the number
                Player target = players.get(targetID); 
                player.swapHands(target);
                System.out.println("You swapped hands with Player " + targetID);
            }else if(choice.startsWith("redraw")){
                int count = Integer.parseInt(choice.split(" ")[1]);
                player.redrawCards(count);
                System.out.println("You have redrawn " + count + " cards.");
            }else{
                System.out.print("Invalid input"); //*testcase
            }
        }else if(character == CharacterCards.KING){
            crowned_player = 1;
            System.out.println("You are now the crowned player.");
            System.out.println("You gain 1 gold for each yellow district.");
            player.addGold(player.countDistrictsByColor("yellow"));
        }else if(character == CharacterCards.BISHOP){
            System.out.println("Bishop: You gain 1 gold for each blue district. Your buildings are immune to the Warlord.");
            player.addGold(player.countDistrictsByColor("blue"));
        }else if(character == CharacterCards.MERCHANT){
            System.out.println("Merchant: You gain 1 gold for each green district plus 1 extra gold.");
            player.addGold(player.countDistrictsByColor("green") + 1);
        }else if(character == CharacterCards.ARCHITECT){
            System.out.println("Architect: You draw 2 extra cards and can build up to 3 districts.");
            player.drawextraCards();
        }else if(character == CharacterCards.WARLORD){
            System.out.println("Warlord: You gain 1 gold for each red district. You can destroy a district.");
            player.addGold(player.countDistrictsByColor("red"));

        }


        System.out.println("Collect 2 gold or draw two cards and pick one [gold/cards]:");
        while(true){
            System.out.print("> ");
            String player_choice = scanner.nextLine().trim();

            if(player_choice.equalsIgnoreCase("gold")){
                player.addGold(2);
                System.out.println("PLayer 1 received 2 gold.");
                break; //break out of the while(true) loop once the player picks either gold or card
            }else if(player_choice.equalsIgnoreCase("cards")){
                System.out.println("Player 1 chose cards.");
                List<String> drawn_card = player.drawnDistrictCards(scanner);
                System.out.println("You chose card " + drawn_card.get(0));
                break;
            }else {
                System.out.println("Invalid input. Please type 'gold' or 'cards'."); //*testcase 
            }
        }

         boolean ended = false; //keeps the player in the loop until they enter the end command.
         while(!ended){
            System.out.print("> ");
            String player_input = scanner.nextLine().trim();

            if(player_input.equalsIgnoreCase("hand")){
                player.showHand();
            }else if(player_input.equalsIgnoreCase("citadel") || player_input.equalsIgnoreCase("list") || player_input.equalsIgnoreCase("city")){
                player.getCity();
            }else if(player_input.equalsIgnoreCase("gold")){
                System.out.println("You have " + player.getGold() + " gold.");
            }else if(player_input.startsWith("build")){ //build 4
                try {
                    int idx = Integer.parseInt(player_input.split(" ")[1]) - 1;
                    player.buildFromHand(idx);
                } catch (Exception e) {
                    System.out.println("Usage: build <hand position>");
                }
            }else if(player_input.equalsIgnoreCase("all")){
                player.showStatus();
            }else if(player_input.equalsIgnoreCase("action")){
                if(character == CharacterCards.ASSASSIN){
                    System.out.println("Use: action kill <character_number> to assassinate a character.");
                }
                if(character == CharacterCards.THIEF){
                    System.out.println("Use: action steal <character_number> to rob a character.");
                }
                if(character == CharacterCards.MAGICIAN){
                    System.out.println("Use: action swap <player_number> or action redraw <number> to exchange or redraw cards.");
                }
                if(character == CharacterCards.KING){
                    System.out.println("King has no special actions. Becomes the crowned player");
                }
                if(character == CharacterCards.BISHOP){
                    System.out.println("Bishop has no special actions.");
                }
                if(character == CharacterCards.MERCHANT){
                    System.out.println("Merchant has no special actions.");
                }
                if(character == CharacterCards.ARCHITECT){
                    System.out.println("Architect may build up to 3 districts this turn. Use build repeatedly.");
                }
                if(character == CharacterCards.WARLORD){
                    System.out.println("Use: action destroy <player_umber> <card_number> to destroy a district.");
                }
            }else if(player_input.startsWith("info")){
                String name = player_input.substring(5).trim().toUpperCase(); //starts capitalising all the characters after info because what comes after is <name> => gotta match the syntax of enum
                try {
                    CharacterCards c = CharacterCards.valueOf(name);
                    System.out.println(c.getDescription());
                } catch(IllegalArgumentException e){
                    System.out.println("Unknown character: " + name);
                }
            }else if(player_input.equalsIgnoreCase("help")){
                System.out.println("Available commands:");
                System.out.println("  info <H|name>      : show information about a building (<hand-index>) or a character");
                System.out.println("  t                  : processes turns");
                System.out.println("  all                : shows all current game info");
                System.out.println("  citadel/list/city [p] : shows districts built by player p (default you)");
                System.out.println("  hand               : shows cards in your hand");
                System.out.println("  gold [p]           : shows gold of player p (default you)");
                System.out.println("  build <place in hand> : builds that card into your city");
                System.out.println("  action             : gives info about your special action and how to perform it");
                System.out.println("  end                : ends your turn");
                System.out.println("  save <file>        : saves the game state to JSON file");
                System.out.println("  load <file>        : loads the game state from JSON file");
                System.out.println("  debug              : toggles debug mode (show AI hands)");
    
            }else if(player_input.equalsIgnoreCase("end")){
                System.out.println("You ended your turn.");
                ended = true;
            }else{
                System.out.println("Invalid command");
            }    
        }
    }

    private void computerPlayer(Player player, CharacterCards character){
        if(character == CharacterCards.ASSASSIN){
            CharacterCards target = CharacterCards.values()[new Random().nextInt(7) + 1];
            assassin_target = target;
            killed_CharacterCards.add(target);
            System.out.println("Player " + player.getId() + " chose to kill the " + target);
        }else if(character == CharacterCards.THIEF){
            List<CharacterCards> possible_targets = new ArrayList<>();
            for (CharacterCards c : CharacterCards.values()){ //c is each enum constant
                if(c.ordinal() >= 2){ //skips assassin (ordinal 0) and thief (ordinal 1)
                    Integer owner = character_owners.get(c);
                    if(owner != null && !killed_CharacterCards.contains(c)){ //if owner (ID) is not null (meaning someone did pick the role) and its not iin the killed character list
                        possible_targets.add(c); //add the character card in the list of possible targets
                    }
                }
            } 
            if(!possible_targets.isEmpty()){
                CharacterCards target = possible_targets.get(new Random().nextInt(possible_targets.size())); 
                thief_target = target;
                System.out.println("Player " + player.getId() + " chose to steal from the " + target);
            }else{
                System.out.println("The Thief has no valid targets to steal from.");
            }
        }else if(character == CharacterCards.MAGICIAN){
            if (players.size() > 1) {
                int targetId = new Random().nextInt(players.size() - 1) + 2;
                Player target = players.get(targetId);
                player.swapHands(target);
                System.out.println("Player " + player.getId() + " swapped hands with player " + targetId);
            }
        }else if(character == CharacterCards.KING){
            crowned_player = player.getId();
            System.out.println("Player " + player.getId() + " is now the crowned player.");
            player.addGold(player.countDistrictsByColor("yellow"));
        } else if(character == CharacterCards.BISHOP){
            player.addGold(player.countDistrictsByColor("blue"));
        } else if(character == CharacterCards.MERCHANT){
            player.addGold(player.countDistrictsByColor("green") + 1);
        } else if(character == CharacterCards.ARCHITECT){
            player.drawextraCards();
        } else if(character == CharacterCards.WARLORD){
            player.addGold(player.countDistrictsByColor("red"));
        }

        Random randomised = new Random();
        if(randomised.nextBoolean()){
            player.addGold(2);
            System.out.println("Player " + player.getId() + " collected 2 gold.");
        }else{
            //player.drawnDistrictCards();
            System.out.println("Player " + player.getId() + " chose cards.");
        }

        
    }
}









    

