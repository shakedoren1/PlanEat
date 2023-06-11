package com.example.planeatapp;

import com.google.gson.annotations.SerializedName;
import java.util.List;
public class IngredientList {
    @SerializedName("Appetizers")
    private List<PotluckItem> Appetizers;

    @SerializedName("Mains")
    private List<PotluckItem> Mains;

    @SerializedName("Sides")
    private List<PotluckItem> Sides;

    @SerializedName("Dessert")
    private List<PotluckItem> Dessert;

    @SerializedName("Drinks")
    private List<PotluckItem> Drinks;

    @SerializedName("Other")
    private List<PotluckItem> Other;

    // Getter methods for all fields
    public List<PotluckItem> getAppetizers() {
        return Appetizers;
    }

    public List<PotluckItem> getMains() {
        return Mains;
    }

    public List<PotluckItem> getSides() {
        return Sides;
    }

    public List<PotluckItem> getDessert() {
        return Dessert;
    }

    public List<PotluckItem> getDrinks() {
        return Drinks;
    }

    public List<PotluckItem> getOther() {
        return Other;
    }
}