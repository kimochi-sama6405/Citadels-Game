package citadels;
import java.util.*;

public class CharacterSelectionPhase {
    private List<CharacterCards> characters; //declaring a private field named "characters" that holds a list of CharacterCards (full deck of 8 characters).
    private List<CharacterCards> faceupcards;//declaring aa private field names "faceupcards" that holds a list of CharacterCards that are faced up (no. of faceupcards depending on the no. of players + cant be King)
    private CharacterCards facedowncard;//declaring a private field named "facedowncards", which holds a single CharacterCard that is chosen to facedown in the game
    private final int num_players;
    private static final Random random = new Random();

    public CharacterSelectionPhase(int num_players, List<CharacterCards> full_deck){
        this.num_players = num_players;
        this.characters = new ArrayList<>(full_deck); //creates a copy of the full deck of 8 characters list and stores it in the characters field => now we got a modifiable list to work with. So that the og list outside aint being modified outside this class
        this.faceupcards = new ArrayList<>(); //store all the faceupcards in a new array list
    }

    public void prepare() { //this method prepares the game by shuffling the cards + randomly discard faceup/facedown cards
        while(true){
            Collections.shuffle(characters); //randomly shuffle the order of the character list thats holding the full deck of 8 characters
            facedowncard = characters.remove(0); //remove the first character from the shuffled deck and store that removed card in the facedowncard 
            faceupcards.clear(); //clear any previous faceupcards in case of reshuffling

            System.out.println("A mystery character was removed."); //referring to the facedowncard (that is being removed)

            int num_faceupcards = 0;
            if(num_players == 4){
                num_faceupcards = 2;
            }else if(num_players == 5){
                num_faceupcards = 1;
            }else if(num_players == 6 || num_players == 7){
                num_faceupcards = 0;
            }else {
                System.out.println("Invalid input"); //*testcase
            }


        boolean King_faceupcard = false;
        for (int i = 0; i < num_faceupcards; i++){ //loop over the each of the faceupcards that are removed
            CharacterCards c = characters.remove(0); //again, remove the first character (which is random cuz the deck is shuffled) and store that removed card in "c" 
            if(c == CharacterCards.KING){
                System.out.println("King was removed.");
                System.out.println("The King cannot be visibly removed, trying again..");
                King_faceupcard = true;
            }
            faceupcards.add(c); //add the removed card in the faceupcards list
        }
        if(!King_faceupcard){ //if King_faceupcard stays "false", then !King_faceupcard gives "true" => the if(true) code will run and break out of the loop
            break; //break the while(true) loop to exit if the King card is NOT amongst the faceupcards. If King is in the faceupcards, then loop again to reshuffle everything
        }
        //if King_faceupcard value turns into "true", then the previous if(!King_faceupcard) becomes if(false) and wont break out of the loop => reshuffle everything!
        characters.add(facedowncard); //re-add all the removed cards again back into the deck of cards
        characters.addAll(faceupcards); //addAll() to add from a list to an arraylist
        }
        for (CharacterCards f : faceupcards){
            System.out.println(f + " was removed.");
        }
    }

    public List<CharacterCards> getRemainingCards(){
        return new ArrayList<>(characters); //return a copy of the list of remaining characters (without the faceupcard/facedowncards cuz those were removed from the deck)
    }


}
