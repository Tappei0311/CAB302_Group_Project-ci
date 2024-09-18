package org.example.cab302_project;

public class Ingredient {
    private int id;
    private String ingredientName;
    private int ingredientCount;
    private int minThreshold;
    private boolean quickAccess;

    public Ingredient(String ingredientName, int ingredientCount, int minThreshold) {
        this.ingredientName = ingredientName;
        this.ingredientCount = ingredientCount;
        this.minThreshold = minThreshold;
    }

    public int getId() {
        return id;
    }
    public  void setId(int id) {
        this.id = id;
    }
    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    public int getIngredientCount() {
        return ingredientCount;
    }
    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }
    public int getMinThreshold() {
        return minThreshold;
    }

    public void setMinThreshold(int minThreshold) {
        this.minThreshold = minThreshold;
    }

    public boolean isQuickAccess() {
        return quickAccess;
    }
    public void setQuickAccess(boolean quickAccess) {
        this.quickAccess = quickAccess;
    }
}
