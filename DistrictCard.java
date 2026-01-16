package citadels;

public class DistrictCard {
    private final String name;
    private final String color;
    private final int cost;
    private final String description;

    public DistrictCard(String name, String color, int cost, String description) {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        String base = name + " [" + color + "" + cost + "]"; //so it has a format of 
        return description.isEmpty() ? base : base + ": " + description;
    }
} 
