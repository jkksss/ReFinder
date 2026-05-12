/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package linaer_algo.refinder;

import linaer_algo.refinder.algorithm.RabinKarp;
import linaer_algo.refinder.database.DatabaseConnection;
import linaer_algo.refinder.database.DataSeeder;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Joko1
 */
public class ReFinder {

    public static void main(String[] args) {
        
        // 1. (Optional) Setup your modern UI Theme before the app opens!
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // 2. Launch the Main GUI Window!
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // This creates your UI and makes it visible on the screen
                new linaer_algo.refinder.ui.MainFrame().setVisible(true);
            }
        });
    }
}
     

