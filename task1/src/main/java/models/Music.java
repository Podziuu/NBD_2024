package models;

public class Music extends Item {
    private boolean vinyl;

    public Music(String itemId, int basePrice, String itemName, String genre, boolean vinyl) {
        super(itemId, basePrice, itemName);
        this.vinyl = vinyl;
    }

    @Override
    public String getItemInfo() {
        return super.getItemInfo() + ", Vinyl: " + (vinyl ? "Yes" : "No");
    }

    public boolean isVinyl() {
        return vinyl;
    }
}
