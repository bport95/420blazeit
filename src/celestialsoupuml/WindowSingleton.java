/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
 * @author ben
 */
public class WindowSingleton {

    private static JFrame window;
    private static SelectedTool selectedTool;
    private static JPanel panel = new JPanel();
    private static JPanel sideMenu = new JPanel();
    private static ShapeContainer selectedContainer;
    private static boolean isPressingMouse;
    private static MouseListener containerListener;
    private static MouseMotionListener containerMotionListener;
    public static ArrayList<ShapeContainer> shapeContainers;

    enum SelectedTool {
        LINE,
        RECTANGLE,
        SELECT
    }

    public WindowSingleton() {

        window = new JFrame("");
        window.setTitle("UML - Line");
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        shapeContainers = new ArrayList();

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
                if (selectedContainer != null && selectedContainer.shapeType == ShapeEnum.CLASSBOX) {
                    editClassText();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedContainer != null) {
                    shapeContainers.remove(selectedContainer);
                    panel.remove(selectedContainer);
                    panel.repaint();
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

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");

        menuBar.add(menu);
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem printItem = new JMenuItem("Print");
        JMenuItem quitItem = new JMenuItem("Quit");

        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                panel.repaint();
            }
        });

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                 int rVal = c.showOpenDialog(window);
                 if(rVal == JFileChooser.APPROVE_OPTION){
                    try {
                        LoadFromFile(c.getSelectedFile().toString());
                    } catch (IOException ex) {
                        Logger.getLogger(WindowSingleton.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(WindowSingleton.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 }
            }
        });
        
        
        
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               JFileChooser c = new JFileChooser();
               int rVal = c.showSaveDialog(window);
               if(rVal == JFileChooser.APPROVE_OPTION){
                   try {
                       SaveToFile(c.getSelectedFile().toString());
                   } catch (IOException ex) {
                       Logger.getLogger(WindowSingleton.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
            }
        });
        
        printItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               PrinterJob pj = PrinterJob.getPrinterJob();
                if (pj.printDialog()) {
                    try {pj.print();}
                    catch (PrinterException exc) {
                        System.out.println(exc);
                     }
                 }   
            }
        });
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menu.add(newItem);
        menu.add(openItem);
        menu.add(saveItem);
        menu.add(printItem);
        menu.add(quitItem);
        window.setJMenuBar(menuBar);
        window.setLocationRelativeTo(null);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        panel = new JPanel() {
            Point pointStart = null;
            Point pointEnd = null;
            boolean lineFinished = false;

            {
                //Mouse listener on the main panel 
                //checking for drawing and selecting
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

                            if (selectedContainer != null) {
                                selectedContainer.setIsSelected(false);
                                selectedContainer = null;
                            }

                            ShapeContainer container = new ShapeContainer(ShapeEnum.RELATIONSHIPLINE);
                            container.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);

                            selectedContainer = container;
                            selectedContainer.setIsSelected(true);

                            container.addMouseListener(containerListener);
                            container.addMouseMotionListener(containerMotionListener);
                            shapeContainers.add(container);
                            
                            repaint();

                            panel.add(container);
                            pointStart = null;

                        }

                        if (selectedTool == SelectedTool.RECTANGLE) {

                            int x = Math.min(pointStart.x, e.getPoint().x);
                            int y = Math.min(pointStart.y, e.getPoint().y);
                            int width = Math.max(pointStart.x - e.getPoint().x, e.getPoint().x - pointStart.x);
                            int height = Math.max(pointStart.y - e.getPoint().y, e.getPoint().y - pointStart.y);

                            pointStart = null;
                            repaint();

                            ShapeContainer container = new ShapeContainer(ShapeEnum.CLASSBOX);
                            container.drawBox(x, y, width, height);
                            if (selectedContainer != null) {
                                selectedContainer.setIsSelected(false);
                                selectedContainer = null;
                            }
                            selectedContainer = container;
                            selectedContainer.setIsSelected(true);

                            container.addMouseListener(containerListener);
                            container.addMouseMotionListener(containerMotionListener);
                            shapeContainers.add(container);
                            panel.add(container);

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

        containerListener = new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressingMouse = false;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedTool == SelectedTool.SELECT) {
                    if (selectedContainer != null) {
                        selectedContainer.setIsSelected(false);
                    }

                    selectedContainer = (ShapeContainer) e.getSource();
                    selectedContainer.setIsSelected(true);
                    isPressingMouse = true;

                    if (e.getClickCount() == 2) {
                        if (selectedContainer != null && selectedContainer.shapeType == ShapeEnum.CLASSBOX) {
                             editClassText();
                        }
                    }
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
        };

        containerMotionListener = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (isPressingMouse == true) {
                    selectedContainer.moveBox(e.getX() - (selectedContainer.getWidth() / 2), e.getY() - (selectedContainer.getHeight() / 2));
                }
            }
        };

        window.add(panel);

        window.show();

    }

    /*
    * Displays a text area in a JPanel popup window 
    * for editing the text of the selected class container
    *
     */
    private static void editClassText() {
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setText(selectedContainer.getClassText());
        textArea.setWrapStyleWord(true);
        JPanel popupPanel = new JPanel(new GridLayout(0, 1));
        popupPanel.add(textArea);
        int result = JOptionPane.showConfirmDialog(null, popupPanel, "Text",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            selectedContainer.setClassText(textArea.getText());
            refreshContainer();
        }
    }

    /*
    * Deselects the current selected 
    * container and then reselects it
     */
    private static void refreshContainer() {
        ShapeContainer tempContainer = selectedContainer;
        selectedContainer.setIsSelected(false);
        selectedContainer = null;
        selectedContainer = tempContainer;
        selectedContainer.setIsSelected(true);
    }
    
    private void LoadFromFile(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        System.out.println(filePath);
        
        FileInputStream fileInput = new FileInputStream(filePath);
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        ArrayList<SaveObject> savedObjects = (ArrayList<SaveObject>)objectInput.readObject();
        objectInput.close();
        fileInput.close();
        System.out.println(savedObjects.size());
        for(SaveObject s : savedObjects){
            ShapeContainer newS = new ShapeContainer(s.type);
            newS.startX = s.startX;
            newS.startY = s.startY;
            newS.endX = s.endX;
            newS.endY = s.endY;
            newS.width = s.width;
            newS.height = s.height;
            System.out.println(newS.startX + " " + newS.startY + " " + newS.endX + " " + newS.endY + " " +
                              newS.width + " " + newS.height + "\n");
            shapeContainers.add(newS);
        }
        
    }
    
    private void SaveToFile(String filePath) throws FileNotFoundException, IOException
    {
        System.out.println(filePath);
        ArrayList<SaveObject> savedObjects = new ArrayList<>();
        for(ShapeContainer s : shapeContainers){
            savedObjects.add(new SaveObject(s.startX, s.startY, s.endX, s.endY, s.width, s.height, s.shapeType));
        }
        FileOutputStream fout = new FileOutputStream(filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(savedObjects);
        oos.close();
        fout.close();
        
    }

    public static WindowSingleton getInstance() {
        return WindowSingletonHolder.INSTANCE;
    }

    public static class WindowSingletonHolder {
        private static final WindowSingleton INSTANCE = new WindowSingleton();
    }
}