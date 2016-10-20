/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import java.awt.Graphics;
import javax.swing.JPanel;



/**
 *
 * @author Brandan
 */
public class Boxes extends JPanel {
    
    public int x;
    public int y;
    public int width;
    public int height;
    public String text;
    
    Boxes(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
     class RectArea extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
           
             g.drawRect(x, y, width, height);  
            
        }
    }
}
