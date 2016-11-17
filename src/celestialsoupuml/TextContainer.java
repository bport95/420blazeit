/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author ben
 */
public class TextContainer extends javax.swing.JPanel {

    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private String text;
    private JLabel label;
    private boolean isSelected;

    public TextContainer(int x, int y, int w, int h) {

        this.xPos = x;
        this.yPos = y;
        this.width = w;
        this.height = h;

        this.text = "text goes here";

        this.setBackground(Color.gray);

        setupContainer();

    }

    public void setupContainer() {
        this.setLocation(this.xPos, this.yPos);
        this.setSize(this.width, this.height);
        label = new JLabel();
        label.setText(this.text);
        label.setLocation(10, 10);
        label.setSize(width, height);
        label.setVerticalAlignment(SwingConstants.TOP);
        this.add(label);

        invalidate();
    }

    public void moveContainer(int newX, int newY) {

        this.xPos = this.getLocation().x + newX;
        this.yPos = this.getLocation().y + newY;

        this.setLocation(xPos, yPos);
    }

    public void setText(String newText) {
        this.remove(label);
        this.text = newText;
        label.setText("<html>" + this.text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        label.setText(label.getText().replace("--", "------------------------------------------------------------------------------"));
        this.add(label);
    }

    public String getText() {
        return this.text;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        repaint();
    }
}
