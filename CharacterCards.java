package citadels;
import java.util.*;

public enum CharacterCards{
    ASSASSIN("Assassin - Select another character whom you wish to kill. The killed character loses their turn."),
    THIEF("Thief - Select another character whom you wish to rob. When a player reveals that character to take his turn, you immediately take all of his gold. You cannot rob the Assassin or the killed character."),
    MAGICIAN("Magician - Can either exchange their hand with another player's, or discard any number of district cards face down to the bottom of the deck and draw an equal number of cards from the district deck (can only do this once per turn)."),
    KING("King - Gains one gold for each yellow (noble) district in their city. They receive the crown token and will be the first to choose characters on the next round."),
    BISHOP("Bishop - Gains one gold for each blue (religious) district in their city. Their buildings cannot be destroyed by the Warlord, unless they are killed by the Assassin."),
    MERCHANT("Merchant - Gains one gold for each green (trade) district in their city. Gains one extra gold."),
    ARCHITECT("Architect - Gains two extra district cards. Can build up to 3 districts per turn."),
    WARLORD("Warlord - Gains one gold for each red (military) district in their city. You can destroy one district of your choice by paying one fewer gold than its building cost. You cannot destroy a district in a city with 8 or more districts.");

     @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();//keep Capital letter at index 0 but the rest of the characters are lowercase (starting from index 1 by using substring(1))
    }
    private final String description; CharacterCards(String desc){
        this.description = desc;
    }
    public String getDescription(){
        return description;
    }

}
