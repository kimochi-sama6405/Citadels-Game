package citadels;

import java.util.*;
import java.util.stream.Collectors;

public class Player {
    private final int id;
    private int gold;
    private final List<String> hand; //all stored district cards
    private final List<String> city;
    private final List<String> districtDeck; //full deck of district cards

    public Player(int id) {
        this.id = id;
        this.gold = 2;
        this.hand = new ArrayList<>();
        this.city = new ArrayList<>();
        this.districtDeck = new ArrayList<>(List.of(
            "Watchtower [red], cost: 1",
            "Prison [red], cost: 2",
            "Battlefield [red], cost: 3",
            "Fortress [red], cost: 5",
            "Manor [yellow], cost: 3",
            "Castle [yellow], cost: 4",
            "Palace [yellow], cost: 5",
            "Tavern [green], cost: 1",
            "Market [green], cost: 2",
            "Trading Post [green], cost: 2",
            "Docks [green], cost: 3",
            "Harbor [green], cost: 4",
            "Town Hall [green], cost: 5",
            "Temple [blue], cost: 1",
            "Church [blue], cost: 2",
            "Monastary [blue], cost: 3",
            "Cathedral [blue], cost: 5",
            "Haunted City [purple], cost: 2",
            "Keep [purple], cost: 3",
            "Laboratory [purple], cost: 5",
            "Smithy [purple], cost: 5",
            "Observatory [purple], cost: 5",
            "Graveyard [purple], cost: 5",
            "Dragon Gate [purple], cost: 6",
            "University [purple], cost: 6",
            "Library [purple], cost: 6",
            "Great Wall [purple], cost: 6",
            "School Of Magic [purple], cost: 6",
            "Lighthouse [purple], cost: 3",
            "Armory [purple], cost: 3",
            "Museum [purple], cost: 4",
            "Imperial Treasury [purple], cost: 4",
            "Map Room [purple], cost: 5",
            "Wishing Well [purple], cost: 5",
            "Quarry [purple], cost: 5",
            "Poor House [purple], cost: 5",
            "Bell Tower [purple], cost: 5",
            "Factory [purple], cost: 6",
            "Park [purple], cost: 6",
            "Hospital [purple], cost: 6",
            "Throne Room [purple], cost: 6"
        ));
        Collections.shuffle(this.districtDeck);
    }

    public int getId() {
        return id;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    public void addCardToHand(String card) {
        hand.add(card);
    }

    public List<String> getHand() {
        return hand;
    }

    public List<String> getCity() {
        return city;
    }

    public void buildDistrict(String card, int cost) {
        if (spendGold(cost)) {
            hand.remove(card);
            city.add(card);
            System.out.println("Built " + card);
        } else {
            System.out.println("You cannot afford to build this building.");
        }
    }

    public void showHand() {
        System.out.println("You have " + gold + " gold. Cards in hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i));// iterate through and show how many district cards we have
        }
    }
    
    public void showStatus(boolean isHuman) {
        String label = "Player " + id + (isHuman ? " (you)" : "");
        String cityList = city.isEmpty() ? "" : String.join(", ", city);
        System.out.println(label + ": cards=" + hand.size()
            + " gold=" + gold
            + " city=" + cityList);
    }

    public void showStatus() {
        showStatus(this.id == 1);
    }


    public int countDistrictsByColor(String color){
        int count = 0;
        for (String district : city) {
            if (district.toLowerCase().contains("[" + color.toLowerCase())){
                count++;
            }
        } return count;
    }
    public void redrawCards(List<Integer> indices) {
        Collections.sort(indices, Collections.reverseOrder());
        List<String> discarded = new ArrayList<>();
        for (int idx : indices) {
            if (idx >= 0 && idx < hand.size()) {
                discarded.add(hand.remove(idx));
            }
        }
        for (String d : discarded) {
            if (!districtDeck.isEmpty()) {
                hand.add(districtDeck.remove(0));
            }
        }
        districtDeck.addAll(discarded);
        Collections.shuffle(districtDeck);
    }

    // Overload to simplify Magician redraw command
    public void redrawCards(int count) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < count && i < hand.size(); i++) indices.add(i);
        redrawCards(indices);
    }


    public void swapHands(Player other) {
        List<String> temp = new ArrayList<>(this.hand);
        this.hand.clear();
        this.hand.addAll(other.hand);
        other.hand.clear();
        other.hand.addAll(temp);
    }
    public void drawextraCards() {
    System.out.println("Architect: drawing 2 extra district cards.");
    for (int i = 0; i < 2; i++) {
        if (districtDeck.isEmpty()) {
            System.out.println("No more cards to draw.");
            break;
        }
        String c = districtDeck.remove(0);
        hand.add(c);
        System.out.println("Drew: " + c);
    }
}
   
     public void buildFromHand(int index) {
        if (index < 0 || index >= hand.size()) {
            System.out.println("Invalid build index");
            return;
        }
        String card = hand.get(index);
        if (city.contains(card)) {
            System.out.println("You already have " + card + " in your city.");
            return;
        }
        // extract cost after 'cost: '
        int cost;
        try {
            String num = card.substring(card.lastIndexOf("cost:") + 5).trim();
            cost = Integer.parseInt(num);
        } catch (Exception e) {
            System.out.println("Error parsing cost");
            return;
        }
        if (!spendGold(cost)) {
            System.out.println("You cannot afford to build this building.");
            return;
        }
        hand.remove(index);
        city.add(card);
        System.out.println("Built " + card);
    }

    public List<String> drawnDistrictCards(Scanner scanner) {
        List<String> draw = new ArrayList<>(); //make a new list to store drawn out district cards
        if (districtDeck.size() < 2) { //if the deck of district cards has less than two (not enough to pick from)
            System.out.println("Not enough cards in the deck to draw.");
            return draw;
        }

        Collections.shuffle(districtDeck); //shuffle up the deck so the pick is random
        draw.add(districtDeck.remove(0));
        draw.add(districtDeck.remove(0));

        System.out.println("Pick one of the following cards: 'collect card <option>'.");
        for (int i = 0; i < draw.size(); i++) {
            System.out.println((i + 1) + ". " + draw.get(i));
        }

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if(input.startsWith("collect card")){
                try{
                    int card_choice = Integer.parseInt(input.split(" ")[2]) - 1; //to get the number the user is inputting
                    if (card_choice >= 0 && card_choice < draw.size()) {
                        hand.add(draw.get(card_choice)); //add the picked district card (out of the 2 drawn out district cards) into the current hand
                        districtDeck.add(draw.get(1 - card_choice)); // discard to bottom of deck
                        return List.of(draw.get(card_choice));
                    } else {
                        System.out.println("Invalid selection.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid command format. Try again.");
                }
            } else {
                System.out.println("Please enter a valid command: 'collect card 1 or 2'");
            }
        }
    }
}
