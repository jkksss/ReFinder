/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linaer_algo.refinder.model;

/**
 *
 * @author Joko1
 */
public class Ingredient {
    
        private int id;
        private String name;
        private double quantity;
        private String unit;
    
    public Ingredient(int id, String name, double quantity, String unit){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
    
    public Ingredient(String name, double quantity, String unit){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        
    }
    
    //Getters, basically gets the information of the ingredients freom the private fields
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public double getQuantity(){
        return quantity;
    }
    
    public String getUnit(){
        return unit;
    }
    
    //Setters, basically sets the updated ingredients
    public void setId(int id){
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
     
    public void setQuantity(double quantity){
        this.quantity = quantity;
    }
    
    public void setUnit(String unit){
        this.unit = unit;
    }
    
    @Override
    public String toString(){
        return name + "-" + quantity + " " + unit;
    }
}
