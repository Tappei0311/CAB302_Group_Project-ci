package org.example.cab302_project.test;

import org.example.cab302_project.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngredientsTest2 {
    private Ingredient ingredient;
    @BeforeEach
    public void setUp(){
        ingredient = new Ingredient(1,"Potato",7 ,6, false);
    }
    @Test
    public void TestGetID(){
        assertEquals(1 ,ingredient.getId());
    }
    @Test
    public void TestGetIngredient(){
        assertEquals("Potato" ,ingredient.getIngredient());
    }
    @Test
    public void TestGetQuantity(){
        assertEquals(7 ,ingredient.getQuantity());
    }
    @Test
    public void TestGetMinQuantity(){
        assertEquals(6 ,ingredient.getMinQuantity());
    }
    @Test
    public void TestIsQuickaccess(){
        assertEquals(false ,ingredient.isQuick_access());
    }
    @Test
    public void TestSetIngredient(){
        ingredient.setName("Lemon");
        assertEquals("Lemon" ,ingredient.getIngredient());
    }
    @Test
    public void TestSetQuantity(){
        ingredient.setQuantity(5);
        assertEquals(5 ,ingredient.getQuantity());
    }
    @Test
    public void TestSetMinQuantity(){
        ingredient.setMinQuantity(2);
        assertEquals(2 ,ingredient.getMinQuantity());
    }
    @Test
    public void TestSetQuickaccess(){
        ingredient.setQuick_access(true);
        assertEquals(true ,ingredient.isQuick_access());
    }
}
