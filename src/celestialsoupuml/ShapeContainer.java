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
    public int endX;
    public int endY;
    private boolean isMoving;
    private JLabel label;
    public String classText;

    public ShapeContainer(ShapeEnum objectType) {
        //super();

        relationshipType = RelationshipStatusEnum.ASSOCIATION;
        shapeType = objectType;

    }

    public void drawBox(int x, int y, int width, int height) {

        this.removeAll();
        
        this.startX = x;
        this.startY = y;
        this.setSize(width, height);
        this.setLocation(x, y);
        this.width = width;
        this.height = height;
        label = new JLabel();
        label.setText(this.classText);
        label.setLocation(10, 10);
        label.setSize(width, height);
        label.setVerticalAlignment(SwingConstants.TOP);
        this.add(label);

        this.setBackground(Color.white);
        repaint();

    }

    public void drawLine(int sX, int sY, int eX, int eY) {
        System.out.println("sX: " + sX);
        System.out.println("sY: " + sY);

        this.startX = sX;
        this.startY = sY;

        System.out.println("startX: " + this.startX);
        System.out.println("startY: " + this.startY);

        this.endX = eX;
        this.endY = eY;
        this.width = Math.abs(endX - startX);
        this.height = Math.abs(endY - startY);
        this.setSize(width, height);
        this.setLocation(startX, startY);

        if (startX == endX) {
            this.setSize(width + 10, height);
        }

        if (startY == endY) {
            this.setSize(width, 10);
        }

        if (startX < endX && startY > endY) {
            this.setLocation(startX, endY);
        } else if (startX > endX && startY > endY) {
            this.setLocation(endX, endY);
        } else if (startX > endX && startY < endY) {
            this.setLocation(endX, this.getLocation().y);
        } //vertical line
        else if (startX == endX && startY != endY) {
            this.setLocation(endX, startY);
            if (endY < endX) {
                this.setLocation(endX, endY);
            }
        } //horizontal line
        else if (startX != endX && startY == endY) {
            this.setLocation(endX, this.getLocation().y);
            if (endY < endX) {
                this.setLocation(startX, this.getLocation().y);
            }
        }

        this.setOpaque(false);
        invalidate();

    }

    public void moveBox(int newX, int newY) {
        isMoving = true;
        this.setLocation(this.getLocation().x + newX, this.getLocation().y + newY);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (relationshipType == RelationshipStatusEnum.ASSOCIATION || relationshipType == RelationshipStatusEnum.DIRECTED_ASSOCIATION) {
            g2.setStroke(new BasicStroke(2));
        }else if (relationshipType == RelationshipStatusEnum.GENERALIZATION) {
            float[] dash1 = {10.0f};
            BasicStroke dashed
                    = new BasicStroke(1.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
            g2.setStroke(dashed);
        } else {
            //logic for arrow line here...  
        }
        if (isSelected) {
            g2.setColor(Color.red);
        } else {
            g2.setColor(Color.black);
        }

        if (shapeType == ShapeEnum.CLASSBOX) {

            g2.drawRect(0, 0, width, height);

        } else {

            int startXDraw = this.startX;
            int startYDraw = this.startY;
            int endXDraw = this.endX;
            int endYDraw = this.endY;

                if (startXDraw < endXDraw && startYDraw < endYDraw) {
                    startXDraw = 0;
                    startYDraw = 0;
                    endXDraw = (int) (this.getSize().getWidth());
                    endYDraw = (int) (this.getSize().height);
                } else if (startXDraw < endXDraw && startYDraw > endYDraw) {
                    startXDraw = 0;
                    startYDraw = (int) this.getSize().getHeight();
                    endXDraw = (int) (this.getSize().getWidth());
                    endYDraw = 0;
                } else if (startXDraw > endXDraw && startYDraw > endYDraw) {
                    endXDraw = 0;
                    endYDraw = 0;
                    startXDraw = (int) (this.getSize().getWidth());
                    startYDraw = (int) (this.getSize().height);
                } else if (startXDraw > endXDraw && startYDraw < endYDraw) {
                    endXDraw = 0;
                    endYDraw = (int) this.getSize().getHeight();
                    startXDraw = (int) (this.getSize().getWidth());
                    startYDraw = 0;
                } else if (startXDraw == endXDraw) {
                    startXDraw = (int) (this.getSize().getWidth() / 2);
                    startYDraw = 0;
                    endXDraw = (int) (this.getSize().getWidth() / 2);
                    endYDraw = (int) this.getSize().getHeight();
                } else if (startYDraw == endYDraw) {
                    startXDraw = 0;
                    startYDraw = (int) this.getSize().getHeight() / 2;
                    endXDraw = (int) (this.getSize().getWidth());
                    endYDraw = (int) this.getSize().getHeight() / 2;
                }

            g.drawLine(startXDraw, startYDraw, endXDraw, endYDraw);
        }
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        repaint();
    }

    public void setRelationshipType(RelationshipStatusEnum status) {
        relationshipType = status;
        repaint();
    }

    public void setClassText(String newText) {
        this.remove(label);
        this.classText = newText;
        label.setText("<html>" + this.classText.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        label.setText(label.getText().replace("--", "------------------------------------------------------------------------------"));
        this.add(label);
    }

    public String getClassText() {
        return this.classText;
    }

    public boolean getSelected() {
        return this.isSelected;
    }
    
    public void redrawShape(){
        this.moveBox(-1, -1);
        this.moveBox(1, 1);
    }
}
