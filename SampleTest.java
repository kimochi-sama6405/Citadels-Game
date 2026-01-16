package citadels;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SampleTest {
    private Player player;
    private Player other;

    @BeforeEach
    void setUp() {
        player = new Player(1);
        other = new Player(2);
    }

    @Test
    void testInitialState() {
        // Player should start with ID, 2 gold, empty hand and city
        assertEquals(1, player.getId());
        assertEquals(2, player.getGold());
        assertTrue(player.getHand().isEmpty(), "Hand should be empty initially");
        assertTrue(player.getCity().isEmpty(), "City should be empty initially");
    }

    @Test
    void testAddAndSpendGold() {
        // Adding and spending gold should adjust balance correctly
        player.addGold(5);
        assertEquals(7, player.getGold());
        assertTrue(player.spendGold(3), "Should be able to spend 3 gold");
        assertEquals(4, player.getGold());
        assertFalse(player.spendGold(10), "Should not be able to overspend");
        assertEquals(4, player.getGold());
    }

    @Test
    void testSwapHands() {
        // Swapping hands should exchange the card lists
        player.addCardToHand("A");
        other.addCardToHand("B");
        player.swapHands(other);
        assertEquals(Collections.singletonList("B"), player.getHand());
        assertEquals(Collections.singletonList("A"), other.getHand());
    }

    @Test
    void testCountDistrictsByColor() {
        // Count occurrences of specific color tags in city
        player.getCity().addAll(Arrays.asList(
            "Tavern [green], cost: 1",
            "Market [green], cost: 2",
            "Cathedral [blue], cost: 5",
            "Church [blue], cost: 2"
        ));
        assertEquals(2, player.countDistrictsByColor("green"));
        assertEquals(2, player.countDistrictsByColor("blue"));
        assertEquals(0, player.countDistrictsByColor("red"));
    }

    @Test
    void testBuildFromHand_validAndDuplicate() {
        // Valid build removes from hand, adds to city, and spends gold
        String card = "Tavern [green], cost: 1";
        player.addCardToHand(card);
        player.addGold(5);
        player.buildFromHand(0);
        assertTrue(player.getCity().contains(card));
        assertFalse(player.getHand().contains(card));
        assertEquals(6, player.getGold(), "Gold should decrease by 1 after building");

        // Duplicate build should be rejected: city already has the card
        player.addCardToHand(card);
        int beforeGold = player.getGold();
        player.buildFromHand(player.getHand().indexOf(card));
        assertTrue(player.getHand().contains(card), "Duplicate card should remain in hand");
        assertEquals(beforeGold, player.getGold(), "Gold should not change on duplicate build");
    }

    @Test
    void testRedrawCards_listOverload() {
        // Redrawing specific indices should maintain hand size
        player.addCardToHand("X");
        player.addCardToHand("Y");
        player.addCardToHand("Z");
        int originalHandSize = player.getHand().size();
        player.redrawCards(Arrays.asList(0, 2));
        assertEquals(originalHandSize, player.getHand().size(), "Hand size should remain constant after redraw");
    }

    @Test
    void testRedrawCards_countOverload() {
        // Redraw overload should also maintain hand size when count <= hand size
        player.addCardToHand("A");
        player.addCardToHand("B");
        player.addCardToHand("C");
        int size = player.getHand().size();
        player.redrawCards(2);
        assertEquals(size, player.getHand().size(), "Hand size should stay same after redraw(count)");
    }
}
