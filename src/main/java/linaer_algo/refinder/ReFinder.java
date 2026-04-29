/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package linaer_algo.refinder;
import linaer_algo.refinder.database.DatabaseConnection;
/**
 *
 * @author Joko1
 */
public class ReFinder {

     public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        System.out.println("ReFinder started!");
    }
}
