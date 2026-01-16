package citadels;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

public class App {
	
	private File cardsFile;

	public App() {
		try {
            cardsFile = new File(URLDecoder.decode(this.getClass().getResource("cards.tsv").getPath(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}

    public static void main(String[] args) {
        App app = new App();
        Scanner scan = new Scanner(System.in);
        int num_players = 0;

        //intial game setup
        while(true){
            System.out.print("Enter the number of players [4-7]:\n> ");
            String input = scan.nextLine(); //do scan.nextLine + integer.parseInt combo instead of scan.nextInt() so its easier to catch errors/relatively safe for all types of inputs

            try{
                num_players = Integer.parseInt(input);
                if(num_players >= 4 && num_players <= 7){
                    break;
                }else{
                    System.out.println("Please enter a number from 4 to 7.");//if user input is out of range 4-7 then ask for input again  
                }
            } catch (NumberFormatException e){ //*testcase
                System.out.println("Invalid input. Please enter a number!");
            }
        }
        System.out.println("Shuffling deck...");
        System.out.println("Adding characters...");
        System.out.println("Dealing cards...");
        System.out.println("Starting Citadels with " + num_players + " players...");
        System.out.println("You are player 1");

        Random random = new Random();
        int crowned_player = random.nextInt(num_players)+1;
        System.out.println("Player " + crowned_player + " is the crowned player and goes first.");
        System.out.println("Press t to process turns");
        System.out.println("================================");
        System.out.println("SELECTION PHASE");
        System.out.println("================================");

        Map<CharacterCards, Integer> character_owners = new HashMap<>();

        while(true){
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String trigger = scanner.nextLine().trim(); //trim() to remove whitespace characters from the beginning and end of a string

            if(trigger.equalsIgnoreCase("t")){ //compares two strings, ignoring lower case and upper case differences. 
                List<CharacterCards> full_deck = new ArrayList<>(List.of(CharacterCards.values())); //creates a full deck of cards with 8 characters by adding all the characters (from enum class) into a list
                CharacterSelectionPhase selection_phase = new CharacterSelectionPhase(num_players, full_deck); //creates a new object called "selection_phase" to represent the phase round, passing the parameters of no.of players + full deck of card
                selection_phase.prepare();
                

                List<CharacterCards> remainingCards = new ArrayList<>(selection_phase.getRemainingCards()); //creating a new list to hold all the remaining characters

                List<Integer> selection_order = new ArrayList<>();
                for (int i = 0; i < num_players; i++){
                    selection_order.add((crowned_player + i - 1) % num_players + 1); //e.g: crown = 4, no.of players = 5 => order is [4,5,1,2,3] => all the order is stored in the selection_order list
                }

                Iterator<Integer> it = selection_order.iterator(); //loop over each number in the list selection_order, each "it" is an element of the thing we are looping through
                while(it.hasNext()){ //loop over and over as long as there are still numbers in the selection_order list
                    int player = it.next(); //the player who starts choosing first is always the first number in the list (i.e: crowmed player)=> then as the code loops over, the current player will move to the next number in the list 
                    System.out.print("> ");
                    String next = scanner.nextLine().trim();

                    if(!next.equalsIgnoreCase("t")){//if the next input isnt "t"
                        System.out.println("It is not your turn. Press t to continue with other player turns.");
                        it = selection_order.iterator(); //restarts the iterator (to start from the beginning of the loop)
                        continue; //skips the rest -> goes back to top of loop
                    }

                    if(player == 1){ //human player
                        System.out.println("Choose your character. Available characters:");
                        System.out.println(String.join(", ", remainingCards.stream().map(CharacterCards::toString).toList()));
                        //System.out.println(remainingCards);

                        CharacterCards selected_character = null;
                        while(true){
                            System.out.print("> ");
                            String user_input = scanner.nextLine().trim();
                            for (CharacterCards card : remainingCards){ //check each element of the remainingCards list
                                if(card.toString().equalsIgnoreCase(user_input)){ //if the user input is one of the elements in the list (meaning the character they wanted to select is available)
                                    selected_character = card;
                                    break; //break out of the for-loop once theres a character card in the list that matches the user input
                                }
                            }
                            if(selected_character != null){
                                break; //break out of the while loop 
                            }else { //if there's no character card in the list that's similar to the user input => the character was simply removed already
                                System.out.println("Invalid input. Please enter again."); //*testcase
                            }
                        }
                        remainingCards.remove(selected_character);
                        character_owners.put(selected_character, 1); 
                        System.out.println("Player 1 chose a character.");

                    }else { //if not player 1, the rest are computer players
                        Random randomised = new Random();
                        int index = randomised.nextInt(remainingCards.size()); //random number from the list
                        CharacterCards computer_pick = remainingCards.remove(index); //removes a character card from the remaining card list (imagining the computer player picked that character)
                        character_owners.put(computer_pick, player);
                        System.out.println("Player " + player + " chose a character.");
                    }
                }
            
            System.out.println("Character choosing is over, action round will now begin.");
            System.out.println("================================");
            System.out.println("TURN PHASE");
            System.out.println("================================");
            break; ////break from the while(true) loop at the start to end the selection phase
            }else{
                System.out.println("It is not your turn. Press t to continue with other player turns.");
            }
        }
         TurnPhase turn_phase = new TurnPhase(character_owners, num_players, crowned_player);
         turn_phase.run(scan);
    

} 
}
