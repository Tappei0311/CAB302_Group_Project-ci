package org.example.cab302_project;

public class Ingredient {
    private int id;
    private String Ingredient;
    private int Quantity;
    private int MinQuantity;
    private boolean quick_access;

    public Ingredient(int id,String Ingredient, int Quantity, int MinQuantity, boolean quick_access){
        this.id = id;
        this.Ingredient = Ingredient;
        this.Quantity = Quantity;
        this.MinQuantity = MinQuantity;
        this.quick_access = quick_access;
    }
    public Ingredient(String Ingredient, int Quantity, int MinQuantity, boolean quick_access){
        this.Ingredient = Ingredient;
        this.Quantity = Quantity;
        this.MinQuantity = MinQuantity;
        this.quick_access = quick_access;
    }
   public Ingredient(String Ingredient, int Quantity, int MinQuantity) {
        this.Ingredient = Ingredient;
        this.Quantity = Quantity;
        this.MinQuantity = MinQuantity;
    }
    public int getId() {
        return id;
    }
    public String getIngredient() {
        return Ingredient;
    }
    public int getQuantity() {
        return Quantity;
    }
    public int getMinQuantity() {
        return MinQuantity;
    }
    public boolean isQuick_access() {
        return quick_access;
    }
    public void setName(String Ingredient) {
        this.Ingredient = Ingredient;
    }
    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
    public void setMinQuantity(int MinQuantity) {
        this.MinQuantity = MinQuantity;
    }
    public void setQuick_access(boolean quick_access) {
        this.quick_access = quick_access;
    }

    @Override
    public String toString() {
        return this.Ingredient;
    }
}




