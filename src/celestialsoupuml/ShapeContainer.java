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
import java.awt.Stroke;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * @author ben
 */

public class ShapeContainer extends javax.swing.JPanel {
    
    public RelationshipStatusEnum relationshipType;
    public ShapeEnum shapeType;
    public int width;
    public int height;
    private boolean isSelected;
    public int startX;
    public int startY;
    public  int endX;
    public int endY;
    private boolean isMoving;
    private JLabel label;
    public String classText;
    
    
    
    public ShapeContainer(ShapeEnum objectType){
        //super();
        
        shapeType = objectType;
        
    }
    
    
    
    public void drawBox(int x, int y, int width, int height){
        
        this.setSize(width,height);
        this.setLocation(x, y);
        this.width = width;
        this.height = height;
        label = new JLabel();
        label.setText(this.classText);
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
        this.setLocation(startX, startY);
        
        if(startX == endX){
            this.setSize(width+10, height);
        }
        
        if(startY == endY){
            this.setSize(width, 10);
        }
        
        if (startX < endX && startY > endY) {
            this.setLocation(startX, endY);
        } else if (startX > endX && startY > endY) {
            this.setLocation(endX, endY);
        } else if (startX > endX && startY < endY) {
            this.setLocation(endX, this.getLocation().y);
        }
        //vertical line
        else if (startX == endX && startY != endY) {
            this.setLocation(endX, startY);
            if(endY<endX){
              this.setLocation(endX, endY);  
            }
        }
        //horizontal line
        else if (startX != endX && startY == endY) {
            this.setLocation(endX, this.getLocation().y);
            if(endY<endX){
              this.setLocation(startX, this.getLocation().y);  
            }
        }
        
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
      if(relationshipType == RelationshipStatusEnum.ASSOCIATION){
        g2.setStroke(new BasicStroke(2));
      }else if (relationshipType == RelationshipStatusEnum.GENERALIZATION){
        Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1,2}, 0);
        g2.setStroke(dotted);
      }else{
        //logic for arrow line here...  
      }
      if(isSelected){
          g2.setColor(Color.red);
      }else{
          g2.setColor(Color.black);
      }

      if(shapeType == ShapeEnum.CLASSBOX){
          
        g2.drawRect(0, 0, width, height);
        
      }else{
        if(!isMoving){
            if (startX < endX && startY < endY) {
                startX = 0;
                startY = 0;
                endX = (int) ( this.getSize().getWidth());
                endY = (int) ( this.getSize().height);
            } else if (startX < endX && startY > endY) {
                startX = 0;
                startY = (int) this.getSize().getHeight();
                endX = (int) ( this.getSize().getWidth());
                endY = 0;
            } else if (startX > endX && startY > endY) {
                endX = 0;
                endY = 0;
                startX = (int) ( this.getSize().getWidth());
                startY = (int) ( this.getSize().height);
            } else if (startX > endX && startY < endY) {
                endX = 0;
                endY = (int) this.getSize().getHeight();
                startX = (int) (this.getSize().getWidth());
                startY = 0;
            }else if (startX == endX) {
                startX = (int) (this.getSize().getWidth()/2);
                startY = 0;
                endX = (int) (this.getSize().getWidth()/2);
                endY = (int) this.getSize().getHeight();
            }else if (startY == endY) {
                startX = 0;
                startY = (int) this.getSize().getHeight()/2;
                endX = (int) (this.getSize().getWidth());
                endY = (int) this.getSize().getHeight()/2;
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
    public void setRelationshipType(RelationshipStatusEnum status) {
        relationshipType = status;
        repaint();
    }
    public void setClassText(String newText){
        this.remove(label);
        this.classText = newText;
        label.setText("<html>" + this.classText.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        label.setText(label.getText().replace("--", "------------------------------------------------------------------------------"));
        this.add(label);
    }
    
    public String getClassText(){
        return this.classText;
    }
    
}
