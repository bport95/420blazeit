/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author ben
 */

public class ShapeContainer extends javax.swing.JPanel{
    
    private ShapeEnum shapeType;
    private int width;
    private int height;
    private boolean isSelected;
    private Boxes box;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    
    
    public ShapeContainer(ShapeEnum objectType){
        super();
        
        shapeType = objectType;
        
    }
    
    public void drawBox(int x, int y, int width, int height){
        
        this.setSize(width,height);
        this.setLocation(x, y);
        this.box = new Boxes(x,y,width,height);
        this.width = width;
        this.height = height;
        //this.setBackground(Color.green);
        repaint();
    }
    
    public void drawLine(int startX, int startY, int endX, int endY){
        
        this.width = Math.abs(endX-startX);
        this.height = Math.abs(endY-startY);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.setSize(500,500);
        this.setLocation(startX,startY);

        this.setBackground(Color.green);
        
        System.out.println("X: " + startX);
        System.out.println("Y: " + startY);
        System.out.println("Width: " + this.width);
        System.out.println("Height: " + this.height);
        
       // repaint();
    }
    
    public void moveBox(int newX, int newY){
        this.setLocation(this.getLocation().x + newX,this.getLocation().y + newY);
    }
    
    public void paintComponent(Graphics g) 
    {
      super.paintComponent(g);
      
      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(2));
      if(isSelected){
          g2.setColor(Color.red);
      }else{
          g2.setColor(Color.black);
      }

      if(shapeType == ShapeEnum.BOX){
        g2.drawRect(0, 0, width, height);
      }else{
        g.drawLine(0, 0, this.endX, this.endY);
      }
    }
    
    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
        repaint();
    }
    
}
