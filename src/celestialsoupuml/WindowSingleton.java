/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author ben
 */
public class WindowSingleton {

    private static JFrame window;
    JMenuBar menuBar = new JMenuBar();
    JComboBox comboBox = new JComboBox();
    private static SelectedTool selectedTool;
    private static JPanel panel = new JPanel();
    private static JPanel sideMenu = new JPanel();
    private static ShapeContainer selectedContainer;
    private static boolean isPressingMouse;
    private static MouseListener containerListener;
    private static MouseMotionListener containerMotionListener;
    private static MouseListener textContainerListener;
    private static MouseMotionListener textContainerMotionListener;
    public static ArrayList<ShapeContainer> shapeContainers;
    private static boolean unsavedChanges;

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
                menuBar.getComponent(2).setVisible(false);
                menuBar.getComponent(3).setVisible(false);
                window.setTitle("UML - Box");
                showEditItems();
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.SELECT;
                menuBar.getComponent(2).setVisible(false);
                menuBar.getComponent(3).setVisible(false);
                window.setTitle("UML - Select");
                redrawShapes();
            }
        });

        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.LINE;
                menuBar.getComponent(2).setVisible(true);
                menuBar.getComponent(3).setVisible(true);
                window.setTitle("UML - Line");
                redrawShapes();
                hideEditItems();
            }
        });

        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapeContainer t = new ShapeContainer();
                t.drawTextBox(10, 10, 200, 200);
                t.setClassText("Freeform text");
                t.addMouseListener(containerListener);
                t.addMouseMotionListener(containerMotionListener);
                panel.add(t);
                shapeContainers.add(t);
                panel.repaint();

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

        JMenu menu = new JMenu("File");
        JMenu editmenu = new JMenu("Edit");

        comboBox.addItem("Association");
        comboBox.addItem("Directed Association");
        comboBox.addItem("Generalization");
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    if ((selectedContainer.shapeType == ShapeEnum.RELATIONSHIPLINE)
                            && (comboBox.getSelectedItem().toString() == "Association")) {
                        for (ShapeContainer s : shapeContainers) {
                            if (s.getSelected() == true) {
                                s.relationshipType = RelationshipStatusEnum.ASSOCIATION;
                                s.redrawShape();
                            }
                        }
                        System.out.println("Change to association");
                    } else if ((selectedContainer.shapeType == ShapeEnum.RELATIONSHIPLINE)
                            && (comboBox.getSelectedItem().toString() == "Directed Association")) {
                        System.out.println("Change to Directed Assocation");
                        for (ShapeContainer s : shapeContainers) {
                            if (s.getSelected() == true) {
                                s.relationshipType = RelationshipStatusEnum.DIRECTED_ASSOCIATION;
                                s.redrawShape();
                            }
                        };
                    } else if ((selectedContainer.shapeType == ShapeEnum.RELATIONSHIPLINE)
                            && (comboBox.getSelectedItem().toString() == "Generalization")) {
                        System.out.println("Change to generalization");
                        for (ShapeContainer s : shapeContainers) {
                            if (s.getSelected() == true) {
                                s.relationshipType = RelationshipStatusEnum.GENERALIZATION;
                                s.redrawShape();
                            }
                        };
                    } else {
                        System.out.println("Uncaught dropdown");
                    }

                }

            }
        });
        JLabel relationshipLabel = new JLabel("Line Relationship:");
        menuBar.add(menu);
        menuBar.add(editmenu);
        menuBar.add(relationshipLabel);
        menuBar.add(comboBox);
        System.out.println(menuBar.getComponentIndex(comboBox));
        menuBar.getComponent(2).setVisible(false);
        menuBar.getComponent(3).setVisible(false);
        
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exportItem = new JMenuItem("Export");
        JMenuItem quitItem = new JMenuItem("Quit");

        JMenuItem editTextItem = new JMenuItem("Edit Text");


        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (unsavedChanges == true) {
                    unsavedChangesPrompt("new");
                } else {
                    removeAnnotations();
                }
            }
        });

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                c.setAcceptAllFileFilterUsed(true);
                int rVal = c.showOpenDialog(window);
                if (rVal == JFileChooser.APPROVE_OPTION) {
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
                savePrompt();
            }
        });

        exportItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("Save as...");

                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG File", ".png"));
                int rVal = fileChooser.showSaveDialog(window);
                if (rVal == JFileChooser.APPROVE_OPTION) {

                    BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = image.createGraphics();
                    System.out.println(panel.getWidth() + " , " + panel.getHeight());
                    panel.paint(g);

                    try {
                        ImageIO.write(image, "png", new File(fileChooser.getSelectedFile() + ".png"));
                    } catch (IOException ex) {
                        Logger.getLogger(WindowSingleton.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        });
        quitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unsavedChangesPrompt("quit");
            }
        });

        editTextItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedContainer.shapeType == ShapeEnum.CLASSBOX || selectedContainer.shapeType == ShapeEnum.FREEFORMTEXT) {
                    editClassText();
                }
            }
        });


        menu.add(newItem);
        menu.add(openItem);
        menu.add(saveItem);
        menu.add(exportItem);
        menu.add(quitItem);
        editmenu.add(editTextItem);
        window.setJMenuBar(menuBar);
        window.setLocationRelativeTo(null);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        hideEditItems();

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
                                hideEditItems();
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
                            if (comboBox.getSelectedItem().toString() == "Generalization") {
                                container.setRelationshipType(RelationshipStatusEnum.GENERALIZATION);
                            }
                            selectedContainer = container;

                            selectedContainer.setIsSelected(true);

                            container.addMouseListener(containerListener);
                            container.addMouseMotionListener(containerMotionListener);
                            shapeContainers.add(container);

                            repaint();

                            panel.add(container);
                            pointStart = null;

                            unsavedChanges = true;

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

                    for (ShapeContainer s : shapeContainers) {
                        if (s.getSelected() == true) {
                            if (s.shapeType == ShapeEnum.CLASSBOX || s.shapeType == ShapeEnum.FREEFORMTEXT) {
                                showEditItems();
                            } else {
                                comboBox.setSelectedIndex(getRStat(s));
                                hideEditItems();
                            }
                        }
                    }

                    if (e.getClickCount() == 2) {
                        if (selectedContainer != null && (selectedContainer.shapeType == ShapeEnum.CLASSBOX
                                || selectedContainer.shapeType == ShapeEnum.FREEFORMTEXT)) {
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
                    for (ShapeContainer s : shapeContainers) {
                        if (s.getSelected() == true) {
                            s.moveBox(e.getX() - (s.getWidth() / 2), e.getY() - (s.getHeight() / 2));
                        }
                    }
                    
                }
            }
        };

        window.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("Comp Resized");
                redrawShapes();
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

        menuBar.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("MB Resized");
                redrawShapes();
            }

            @Override
            public void componentMoved(ComponentEvent e) {

                System.out.println("MB Moved");
            }

            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("MB Shown");
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                System.out.println("MB Hidden");
            }
        });

        window.add(panel);

        window.show();

    }

    /*
    * Displays a text area in a JPanel popup window 
    * for editing the text of the selected class container
    *
     */
    private static void editClassText() {

        for (ShapeContainer s : shapeContainers) {
            if (s.getSelected()) {
                JTextArea textArea = new JTextArea(20, 50);
                textArea.setText(s.getClassText());
                textArea.setWrapStyleWord(true);
                JPanel popupPanel = new JPanel(new GridLayout(0, 1));
                popupPanel.add(textArea);
                int result = JOptionPane.showConfirmDialog(null, popupPanel, "Text",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    s.setClassText(textArea.getText());
                    refreshContainer();
                }
            }
        }
    }

    private static void resizeBox() {
        for (ShapeContainer s : shapeContainers) {
            if (s.getSelected()) {

            }
        }
    }

    
    private void showEditItems() {
        JMenu edit = (JMenu) menuBar.getComponent(1);
        edit.getItem(0).setEnabled(true);
    }
    
    private void hideEditItems() {
        JMenu edit = (JMenu) menuBar.getComponent(1);
        edit.getItem(0).setEnabled(false);
        
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

    private void LoadFromFile(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException {
        System.out.println(filePath);

        shapeContainers.clear();
        panel.removeAll();
        panel.repaint();

        FileInputStream fileInput = new FileInputStream(filePath);
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        ArrayList<SaveObject> savedObjects = (ArrayList<SaveObject>) objectInput.readObject();
        objectInput.close();
        fileInput.close();
        System.out.println(savedObjects.size());
        for (SaveObject s : savedObjects) {
            ShapeContainer newS = new ShapeContainer(s.type);
            newS.startX = s.startX;
            newS.startY = s.startY;
            newS.endX = s.endX;
            newS.endY = s.endY;
            newS.width = s.width;
            newS.height = s.height;
            newS.relationshipType = s.relStat;
            newS.shapeType = s.type;
            newS.classText = s.classText;
            System.out.println(newS.startX + " " + newS.startY + " " + newS.endX + " " + newS.endY + " "
                    + newS.width + " " + newS.height + newS.shapeType + newS.relationshipType + "\n");
            shapeContainers.add(newS);

            if (newS.shapeType == ShapeEnum.CLASSBOX) {
                System.out.println("Is Class Box");
                newS.drawBox(newS.startX, newS.startY, newS.width, newS.height);
            } else if (newS.shapeType == ShapeEnum.RELATIONSHIPLINE) {
                newS.drawLine(newS.startX, newS.startY, newS.endX, newS.endY);
            } else {
                System.out.println("Unknown shape type");
            }

            newS.addMouseListener(containerListener);
            newS.addMouseMotionListener(containerMotionListener);

            panel.add(newS);

            newS.redrawShape();

        }

    }

    private void SaveToFile(String filePath) throws FileNotFoundException, IOException {
        System.out.println(filePath);
        ArrayList<SaveObject> savedObjects = new ArrayList<>();
        for (ShapeContainer s : shapeContainers) {
            if (s.shapeType == ShapeEnum.RELATIONSHIPLINE) {
                savedObjects.add(new SaveObject(s.startX, s.startY, s.endX, s.endY, s.width, s.height, s.shapeType, s.relationshipType, null));
            } else {
                savedObjects.add(new SaveObject(s.startX, s.startY, s.endX, s.endY, s.width, s.height, s.shapeType, null, s.getClassText()));
            }

        }

        unsavedChanges = false;

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

    private static void redrawShapes() {
        System.out.println("Redraw Shapes");
        for (ShapeContainer s : shapeContainers) {
            System.out.println("S Start X:" + s.startX);
            if (s.shapeType == ShapeEnum.RELATIONSHIPLINE) {
                s.drawLine(s.startX, s.startY, s.endX, s.endY);
            } else if (s.shapeType == ShapeEnum.CLASSBOX) {
                s.drawBox(s.startX, s.startY, s.width, s.height);
            } else {
                s.drawTextBox(s.startX, s.startY, s.width, s.height);
            }
        }
    }

    private static int getRStat(ShapeContainer s) {
        if (s.relationshipType == RelationshipStatusEnum.DIRECTED_ASSOCIATION) {
            return 1;
        } else if (s.relationshipType == RelationshipStatusEnum.GENERALIZATION) {
            return 2;
        }

        return 0;

    }

    private static void removeAnnotations() {
        shapeContainers.clear();
        panel.removeAll();
        panel.repaint();
    }

    private void savePrompt() {
        JFileChooser c = new JFileChooser();
        c.setFileFilter(new FileNameExtensionFilter("Celestial Soup UML", ".csu"));
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showSaveDialog(window);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            try {
                SaveToFile(c.getSelectedFile().toString() + ".csu");
            } catch (IOException ex) {
                Logger.getLogger(WindowSingleton.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void unsavedChangesPrompt(String action) {
        JLabel label = new JLabel();
        label.setText("You have unsaved changes, would you like to save them first?");
        JPanel popupPanel = new JPanel(new GridLayout(0, 1));
        popupPanel.add(label);
        int result = JOptionPane.showConfirmDialog(null, popupPanel, "Text",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            savePrompt();
        } else if (action.equals("quit")) {
            System.exit(0);
        } else {
            removeAnnotations();
        }
    }
}
