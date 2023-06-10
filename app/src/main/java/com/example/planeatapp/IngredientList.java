package com.example.planeatapp;

import java.util.List;

public class IngredientList {
    private List<PotluckItem> PotluckItems;

    public List<PotluckItem> getPotluckItems() {
        return PotluckItems;
    }

    public void setPotluckItems(List<PotluckItem> potluckItems) {
        PotluckItems = potluckItems;
    }
}