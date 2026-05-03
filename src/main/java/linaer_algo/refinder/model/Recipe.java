/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linaer_algo.refinder.model;
import java.util.List;
/**
 *
 * @author Joko1
 */
public class Recipe {
    
    private int id;
    private String name;
    private String cuisine;
    private int cookTime;
    private String instructions;
    private String photoPath;
    private List<Ingredient> ingredients;
    private double matchPercentage;
    
    //Recipes already in the database
    public Recipe(int id, String name, String cuisine, int cookTime, String instructions, String photoPath){
    
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.cookTime = cookTime;
        this.instructions = instructions;
        this.photoPath = photoPath;
    }
    
    //Recipes that will be entered yet in the database 
    public Recipe(String name, String cuisine, int cookTime, String instructions, String photoPath){
        
        this.name = name;
        this.cuisine = cuisine;
        this.cookTime = cookTime;
        this.instructions = instructions;
        this.photoPath = photoPath;
}
    
     //Getters, gets the information from the private fields
     public int getId(){
         return id;
     }
     
     public String getName(){
         return name;
     }
     
     public String getCuisine(){
         return cuisine;
     }
     
     public int getCookTime(){
         return cookTime;
     }
     
     public String getInstructions(){
        return instructions;
     }
     
     public String getPhotoPath(){
         return photoPath;
     }
     
     public List<Ingredient> getIngredients(){
         return ingredients;
     }
     
     public double getMatchPercentage(){
        return matchPercentage;
    }
     
     //Setters, sets the updated data of ingredients changed
     public void setId(int id){
         this.id = id;
     }
     
     public void setName(String name){
         this.name = name;
     }
     
     public void setCuisine(String cuisine){
         this.cuisine = cuisine;
     }
     
     public void setCookTime(int cookTime){
         this.cookTime = cookTime;
     }
     
     public void setInstructions(String instructions){
         this.instructions = instructions;
     }
     
     public void setPhotoPath(String photoPath){
         this.photoPath = photoPath;
     }
     
     public void setIngredients(List<Ingredient> ingredients){
         this.ingredients = ingredients;
     }
     
     public void setMatchPercentage(double matchPercentage){
         this.matchPercentage = matchPercentage;
     }
     
     @Override
     public String toString(){
         return name + " ("+ cuisine +") - " +cookTime + " mins";
     }
}
