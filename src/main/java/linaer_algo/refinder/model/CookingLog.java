/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linaer_algo.refinder.model;

/**
 *
 * @author Joko1
 */
public class CookingLog {
    private int id;
    private int recipeId;
    private String recipeName;
    private String dateCooked;
    private int servings;
   
    //Constructor to the already created cooking log
    public CookingLog(int id, int recipeId, String recipeName, String dateCooked, int servings){
        this.id = id;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.dateCooked = dateCooked;
        this.servings = servings;
    }
    
    // Constructor for the new cooking log
    public CookingLog(int recipeId, String recipeName, String dateCooked, int servings){
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.dateCooked = dateCooked;
        this.servings = servings;
    }
    
    //Getters for the private field datas
    public int getId(){
        return id;
    }
    
    public int getRecipeId(){
        return recipeId;
    }
    
    public String getRecipeName(){
        return recipeName;
    }
    
    public String getDateCooked(){
        return dateCooked;
    }
    
    public int getServings(){
        return servings;
    }
    
    //Setters for any updated data
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setRecipeId(int recipeId){
        this.recipeId = recipeId;
    }
    
    public void setRecipeName(String recipeName){
        this.recipeName = recipeName;
    }
    
    public void setDateCooked(String dateCooked){
        this.dateCooked = dateCooked;
    }
    
    public void setServings(int servings){
        this.servings = servings;
    }
    
    @Override
    public String toString(){
        return dateCooked + " - " + recipeName + "("+ servings +" servings)";
    }
}
