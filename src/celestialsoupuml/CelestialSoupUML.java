/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
/**
 *
 * @author bwjablon
 */
public class CelestialSoupUML  {

    enum SelectedTool
    {
        LINE,
        RECTANGLE
    }
    static SelectedTool selectedTool;
    private static List<Line> lines = new ArrayList();
    static JPanel p = new JPanel();
    private static List<Boxes> boxes = new ArrayList();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        selectedTool = SelectedTool.LINE;
        JFrame window = new JFrame("Untitled");
        window.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    System.exit(0); 
            }
        });
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        
        menuBar.add(menu);
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem lineItem = new JMenuItem("Line");
        JMenuItem boxItem = new JMenuItem("Box");
        JMenuItem quitItem = new JMenuItem("Quit");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boxes.clear();
                lines.clear();
                p.repaint();
            }
        });
        lineItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.LINE;
            }
        });
        
        boxItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.RECTANGLE;
            }
        });
        
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        menu.add(newItem);
        menu.add(lineItem);
        menu.add(boxItem);
        menu.add(quitItem);
        window.setJMenuBar(menuBar);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screenSize.getWidth() / 2);
        int height = (int)(screenSize.getHeight() / 2);
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        p = new JPanel() {
        Point pointStart = null;
        Point pointEnd   = null;
        boolean lineFinished = false;
        {
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    pointStart = e.getPoint();
                    
                }

                public void mouseReleased(MouseEvent e) {
                    if(selectedTool == SelectedTool.LINE)
                    {
                        Line newLine = new Line(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                        lines.add(newLine);
                    }
                    
                    if(selectedTool == SelectedTool.RECTANGLE)
                    {
                        int x = Math.min(pointStart.x, e.getPoint().x);
                        int y = Math.min(pointStart.y, e.getPoint().y);
                        int width = Math.max(pointStart.x - e.getPoint().x, e.getPoint().x - pointStart.x);
                        int height = Math.max(pointStart.y - e.getPoint().y, e.getPoint().y - pointStart.y);
                        boxes.add(new Boxes(x, y, width, height));
                    }
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
        public void paintComponent(Graphics g) 
        {
                super.paintComponent(g);

                
                if(lines.size() > 0)
                {
                    for(int i = 0; i < lines.size(); i++)
                    {
                        g.drawLine(lines.get(i).pointStartX, lines.get(i).pointStartY, lines.get(i).pointEndX, lines.get(i).pointEndY);
                    }
                }

                if(boxes.size() > 0)
                {
                    for(int i = 0; i < boxes.size(); i++)
                    {
                        g.drawRect(boxes.get(i).x, boxes.get(i).y, boxes.get(i).width, boxes.get(i).height);  
                    }
                }
                if (pointStart != null) 
                {
                    if(selectedTool == SelectedTool.LINE)
                        g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);

                    if(selectedTool == SelectedTool.RECTANGLE)
                    {
                        int x = Math.min(pointStart.x, pointEnd.x);
                        int y = Math.min(pointStart.y,pointEnd.y);
                        int width = Math.max(pointStart.x - pointEnd.x, pointEnd.x - pointStart.x);
                        int height = Math.max(pointStart.y - pointEnd.y, pointEnd.y - pointStart.y);
                        g.drawRect(x, y, width, height);
                    }
                }
            }
        };
        window.add(p);
        window.show();
    } 
}


