package org.example.cab302_project.test;

import org.example.cab302_project.Ingredient;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientTest {


    @Test
    public void testGetIngredientInfo(){
        Ingredient ingredient = new Ingredient("test1", 5, 1, false);
        assertEquals("test1", ingredient.getIngredient());
        assertEquals(5, ingredient.getQuantity());
        assertEquals(1, ingredient.getMinQuantity());
        assertFalse(ingredient.isQuick_access());
    }

}
