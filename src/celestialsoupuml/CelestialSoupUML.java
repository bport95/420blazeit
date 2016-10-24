/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

/**
 *
 * @author bwjablon
 */
public class CelestialSoupUML {

    enum SelectedTool {
        LINE,
        RECTANGLE,
        SELECT
    }
    static SelectedTool selectedTool;
    static Boxes box;
    private static List<Line> lines = new ArrayList();
    static JPanel p = new JPanel();
    static JPanel sideMenu = new JPanel();
    private static List<Boxes> boxes = new ArrayList();
    private static ShapeContainer selectedContainer;
    private static JMenuBar menuBar;
    private static boolean isPressingMouse;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        selectedTool = SelectedTool.LINE;
        JFrame window = new JFrame("Untitled");
        window.setTitle("UML - Line");
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");

        menuBar.add(menu);
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem quitItem = new JMenuItem("Quit");

        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boxes.clear();
                lines.clear();
                p.removeAll();
                p.repaint();
            }
        });

        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
        toolbar.setBackground(Color.gray);
        toolbar.setFloatable(false);
        
        JButton selectButton = new JButton();
        selectButton.setText("Select");

        JButton lineButton = new JButton();
        lineButton.setText("Line");

        JButton boxButton = new JButton();
        boxButton.setText("Box");
        
        JButton textButton = new JButton();
        textButton.setText("Text");
        
        JButton deleteButton = new JButton();
        deleteButton.setText("Delete");

        boxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.RECTANGLE;
                window.setTitle("UML - Box");
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.SELECT;
                window.setTitle("UML - Select");
            }
        });

        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.LINE;
                window.setTitle("UML - Line");
            }
        });
        
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedContainer != null){
                    editClassText();
                }
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedContainer != null){
                    p.remove(selectedContainer);
                    p.repaint();
                    selectedContainer = null;
                }
            }
        });

        toolbar.add(selectButton);
        toolbar.addSeparator();
        toolbar.add(lineButton);
        toolbar.addSeparator();
        toolbar.add(boxButton);
        toolbar.addSeparator();
        toolbar.add(textButton);
        toolbar.addSeparator();
        toolbar.add(deleteButton);
        window.add(toolbar, BorderLayout.WEST);

        menu.add(newItem);
        menu.add(quitItem);
        window.setJMenuBar(menuBar);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() / 2);
        int height = (int) (screenSize.getHeight() / 2);
        window.setSize(width, height);
        window.setLocationRelativeTo(null);
        p = new JPanel() {
            Point pointStart = null;
            Point pointEnd = null;
            boolean lineFinished = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (selectedTool == SelectedTool.SELECT) {
                            if (selectedContainer != null) {
                                selectedContainer.setIsSelected(false);

                            }
                            selectedContainer = null;
                            return;
                        }
                        pointStart = e.getPoint();

                    }

                    public void mouseReleased(MouseEvent e) {
                        if (selectedTool == SelectedTool.LINE) {
                            Line newLine = new Line(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                            //lines.add(newLine);

                            if (selectedContainer != null) {
                                selectedContainer.setIsSelected(false);
                                selectedContainer = null;
                            }
                            
                            ShapeContainer container = new ShapeContainer(ShapeEnum.LINE);
                            container.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                            
                            selectedContainer = container;
                            selectedContainer.setIsSelected(true);
                            
                            container.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseReleased(MouseEvent e) {
                                    isPressingMouse = false;
                                    
                                    System.out.println("released");
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    if (selectedTool == SelectedTool.SELECT) {
                                        if(selectedContainer != null){
                                            selectedContainer.setIsSelected(false);
                                        }
                                        
                                        selectedContainer = (ShapeContainer) e.getSource();
                                        selectedContainer.setIsSelected(true);
                                        isPressingMouse = true;

                                    }
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                }

                                @Override
                                public void mouseClicked(MouseEvent e) {
                                }
                            });
                            
                             container.addMouseMotionListener(new MouseMotionAdapter() {
                                public void mouseMoved(MouseEvent e) {

                                }

                                public void mouseDragged(MouseEvent e) {

                                    System.out.println("is pressing : " + isPressingMouse);
                                    System.out.println("selected container : " + selectedContainer);
                                    if (isPressingMouse == true) {
                                        System.out.println("Moved");
                                        selectedContainer.moveBox(e.getX() - (selectedContainer.getWidth() / 2), e.getY() - (selectedContainer.getHeight() / 2));
                                    }

                                }
                            });
                            
                            
                            repaint();
                            p.add(container);
                            pointStart = null;
                             
                        }

                        if (selectedTool == SelectedTool.RECTANGLE) {

                            int x = Math.min(pointStart.x, e.getPoint().x);
                            int y = Math.min(pointStart.y, e.getPoint().y);
                            int width = Math.max(pointStart.x - e.getPoint().x, e.getPoint().x - pointStart.x);
                            int height = Math.max(pointStart.y - e.getPoint().y, e.getPoint().y - pointStart.y);
                            boxes.add(new Boxes(x, y, width, height));

                            pointStart = null;
                            repaint();

                            ShapeContainer container = new ShapeContainer(ShapeEnum.BOX);
                            container.drawBox(x, y, width, height);
                            if (selectedContainer != null) {
                                selectedContainer.setIsSelected(false);
                                selectedContainer = null;
                            }
                            selectedContainer = container;
                            selectedContainer.setIsSelected(true);

                            container.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseReleased(MouseEvent e) {
                                    isPressingMouse = false;
                                    System.out.println("released");
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {
                                    if (selectedTool == SelectedTool.SELECT) {
                                        if (selectedContainer != null) {
                                            selectedContainer.setIsSelected(false);
                                            selectedContainer = null;
                                        }

                                        selectedContainer = (ShapeContainer) e.getSource();
                                        selectedContainer.setIsSelected(true);
                                        isPressingMouse = true;

                                    }
                                }

                                @Override
                                public void mouseExited(MouseEvent e) {
                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                }

                                @Override
                                public void mouseClicked(MouseEvent e) {
                                }
                            });

                            container.addMouseMotionListener(new MouseMotionAdapter() {
                                public void mouseMoved(MouseEvent e) {

                                }

                                public void mouseDragged(MouseEvent e) {

                                    System.out.println("is pressing : " + isPressingMouse);
                                    System.out.println("selected container : " + selectedContainer);
                                    if (isPressingMouse == true) {
                                        System.out.println("Moved");
                                        selectedContainer.moveBox(e.getX() - (selectedContainer.getWidth() / 2), e.getY() - (selectedContainer.getHeight() / 2));
                                    }

                                }
                            });

                            p.add(container);

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

            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (lines.size() > 0) {
                    for (int i = 0; i < lines.size(); i++) {
                        g.drawLine(lines.get(i).pointStartX, lines.get(i).pointStartY, lines.get(i).pointEndX, lines.get(i).pointEndY);
                    }
                }

                if (pointStart != null) {
                    if (selectedTool == SelectedTool.LINE) {
                        g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                    }

                    if (selectedTool == SelectedTool.RECTANGLE) {
                        int x = Math.min(pointStart.x, pointEnd.x);
                        int y = Math.min(pointStart.y, pointEnd.y);
                        int width = Math.max(pointStart.x - pointEnd.x, pointEnd.x - pointStart.x);
                        int height = Math.max(pointStart.y - pointEnd.y, pointEnd.y - pointStart.y);
                        g.drawRect(x, y, width, height);
                    }
                }
            }
        };

        sideMenu = new JPanel() {

        };

        window.add(p);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.show();
        
        window.addComponentListener(new ComponentListener(){
            @Override
            public void componentResized(ComponentEvent e) {
                //Get size of frame and do cool stuff with it   
                System.out.println("Comp Resized");
                if(selectedContainer != null){
                    selectedContainer.invalidate();
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                
                System.out.println("Comp Moved");
            }

            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("Comp Shown");
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                System.out.println("Comp Hidden");
            }
        });


    }
    
    public static void editClassText(){
        JTextArea field1 = new JTextArea(20,50);
        field1.setText(selectedContainer.getClassText());
        field1.setWrapStyleWord(true);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(field1);
        int result = JOptionPane.showConfirmDialog(null, panel, "Text",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            selectedContainer.setClassText(field1.getText());
            System.out.println("Entered Text: " + selectedContainer.getClassText());
            refreshContainer();
        } else {
            System.out.println("Cancelled_");
        }
    }
    
    public static void refreshContainer(){
        ShapeContainer tempContainer = selectedContainer;
        selectedContainer.setIsSelected(false);
        selectedContainer = null;
        selectedContainer = tempContainer;
        selectedContainer.setIsSelected(true);
    }
}
