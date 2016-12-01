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
    public int locationX;
    public int locationY;
    private JLabel label;
    public String classText;
    public boolean isMoving;

    public ShapeContainer() {
        //super();

    }

    public ShapeContainer(ShapeEnum objectType) {
        //super();

        relationshipType = RelationshipStatusEnum.ASSOCIATION;
        shapeType = objectType;

    }

    /**
     *
     * @param x the x position of the box
     * @param y the y position of the box
     * @param width the width of the box
     * @param height the height of the box
     */
    public void drawBox(int x, int y, int width, int height) {

        this.removeAll();

        this.locationX = x;
        this.locationY = y;
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

    /**
     *
     * @param x the x position of the text box
     * @param y the y position of the text box
     * @param width the width of the text box
     * @param height the height of the text box
     */
    public void drawTextBox(int x, int y, int width, int height) {

        this.removeAll();

        this.locationX = x;
        this.locationY = y;
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

        this.shapeType = ShapeEnum.FREEFORMTEXT;

        this.setBackground(Color.white);
        repaint();

    }

    /**
     *
     * @param sX the starting x position of the line
     * @param sY the starting y position of the line
     * @param eX the ending x position of the line
     * @param eY the ending y position of the line
     */
    public void drawLine(int sX, int sY, int eX, int eY) {

        this.startX = sX;
        this.startY = sY;

        this.locationX = sX;
        this.locationY = sY;

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

    /**
     *
     * @param newX the new position of x
     * @param newY the new position of y
     */
    public void moveBox(int newX, int newY) {

        this.locationX = this.getLocation().x + newX;
        this.locationY = this.getLocation().y + newY;

        this.setLocation(this.locationX, this.locationY);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (relationshipType == RelationshipStatusEnum.ASSOCIATION) {
            g2.setStroke(new BasicStroke(2));
        } else if (relationshipType == RelationshipStatusEnum.GENERALIZATION) {
            float[] dash1 = {10.0f};
            BasicStroke dashed
                    = new BasicStroke(1.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
            g2.setStroke(dashed);
        }

        if (isSelected) {
            g2.setColor(Color.red);
        } else {
            g2.setColor(Color.black);
        }

        if (shapeType == ShapeEnum.CLASSBOX || shapeType == ShapeEnum.FREEFORMTEXT) {

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

    /**
     *
     * @param isSelected whether or not the shape container is selected
     */
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        repaint();
    }

    /**
     *
     * @param status the new relationship type status
     */
    public void setRelationshipType(RelationshipStatusEnum status) {
        relationshipType = status;
        repaint();
    }

    /**
     *
     * @param newText the new class text
     */
    public void setClassText(String newText) {
        this.remove(label);
        this.classText = newText;
        label.setText("<html>" + this.classText.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        label.setText(label.getText().replace("--", "------------------------------------------------------------------------------"));
        this.add(label);
    }

    /**
     *
     * @return the class text of the shape container
     */
    public String getClassText() {
        return this.classText;
    }

    /**
     *
     * @return whether or not the shape is selected
     */
    public boolean getSelected() {
        return this.isSelected;
    }

    public void redrawShape() {
        this.moveBox(-1, -1);
        this.moveBox(1, 1);
    }

}
