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
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
    private boolean isMoving;
    private JLabel label;
    
    
    
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
        this.box.text = "";
        label = new JLabel();
        label.setText(this.box.text);
        label.setLocation(10, 10);
        label.setSize(width,height);
        label.setVerticalAlignment(SwingConstants.TOP);
        this.add(label);
        
        this.setBackground(Color.white);
        repaint();
        
        
    }
    
    public void drawLine(int startX, int startY, int endX, int endY){

        this.width = Math.abs(endX-startX);
        this.height = Math.abs(endY-startY);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.setSize(width,height);
        this.setLocation(startX,startY);
        this.setOpaque(false);
        invalidate();
        
    }
    
    public void moveBox(int newX, int newY){
        isMoving = true;
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
        System.out.println("Draw Line");

        if(!isMoving){
            if (startX < endX && startY < endY) {
                startX = 0;
                startY = 0;
                endX = (int) (this.getLocation().getX() + this.getSize().getWidth());
                endY = (int) (this.getLocation().getY() + this.getSize().height);
            } else if (startX < endX && startY > endY) {
                startX = 0;
                startY = (int) this.getSize().getHeight();
                endX = (int) (this.getLocation().getX() + this.getSize().getWidth());
                endY = (int) (this.getSize().height - this.getLocation().getY());
            } else if (startX > endX && startY > endY) {
                endX = 0;
                endY = 0;
                startX = (int) (this.getLocation().getX() + this.getSize().getWidth());
                startY = (int) (this.getLocation().getY() + this.getSize().height);
            } else if (startX > endX && startY < endY) {
                endX = 0;
                endY = (int) this.getSize().getHeight();
                startX = (int) (this.getLocation().getX() + this.getSize().getWidth());
                startY = (int) (this.getSize().height - this.getLocation().getY());
            } else {
                System.out.println("Unknown Type");
            }
        }else{
            isMoving = false;
        }    
               
        g.drawLine(startX, startY, endX, endY);
      }
    }
    
    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
        repaint();
    }
    
    public void setClassText(String newText){
        this.remove(label);
        this.box.text = newText;
        //this.label.setText(this.box.text);
        label.setText("<html>" + this.box.text.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        System.out.println("Text: " + label.getText());
        System.out.println("Line Index: " + label.getText().indexOf("--"));
        label.setText(label.getText().replace("--", "------------------------------------------------------------------------------"));
        this.add(label);
    }
    
    public String getClassText(){
        return box.text;
    }
    
}
