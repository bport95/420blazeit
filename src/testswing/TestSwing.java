/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testswing;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import testswing.Line;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author bwjablon
 */
public class TestSwing  {

    private static List<Line> lines = new ArrayList();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame window = new JFrame("Window");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.getWidth() / 2);
        int height = (int)(screenSize.getHeight() / 2);
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        JPanel p = new JPanel() {
        Point pointStart = null;
        Point pointEnd   = null;
        boolean lineFinished = false;
        {
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    pointStart = e.getPoint();
                    
                }

                public void mouseReleased(MouseEvent e) {
                    Line newLine = new Line(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                    lines.add(newLine);
                    pointStart = null;
                    
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    pointEnd = e.getPoint();
                }

                public void mouseDragged(MouseEvent e) {
                    pointEnd = e.getPoint();
                    repaint();
                }
            });
        }
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if(lines.size() > 0)
            {
                for(int i = 0; i < lines.size(); i++)
                {
                    g.drawLine(lines.get(i).pointStartX, lines.get(i).pointStartY, lines.get(i).pointEndX, lines.get(i).pointEndY);
                }
            }
            if (pointStart != null) {
                
                g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                
              }
            
            
             
            }
        };
        window.add(p);
        window.show();
    }
    
    

    
 
 
    
}


