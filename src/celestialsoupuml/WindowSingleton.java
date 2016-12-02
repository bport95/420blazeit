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
    public SelectedTool selectedTool;
    private static JPanel panel = new JPanel();
    public static JPanel sideMenu = new JPanel();
    private static ShapeContainer selectedContainer;
    private static boolean isPressingMouse;
    private static MouseListener containerListener;
    private static MouseMotionListener containerMotionListener;
    private static MouseListener textContainerListener;
    private static MouseMotionListener textContainerMotionListener;
    public static ArrayList<ShapeContainer> shapeContainers;
    private static boolean unsavedChanges;
    public JButton lineButton;
    public JButton boxButton;
    public JButton selectButton;
    public JButton textButton;
    public JButton deleteButton;

    public enum SelectedTool {
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

        selectButton = new JButton();
        selectButton.setText("Select");

        lineButton = new JButton();
        lineButton.setText("Line");

        boxButton = new JButton();
        boxButton.setText("Box");

        textButton = new JButton();
        textButton.setText("Text");

        deleteButton = new JButton();
        deleteButton.setText("Delete");

        boxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.RECTANGLE;
                hideLineMenu();
                window.setTitle("UML - Box");
                showEditItems();
                resetSidebarButtons(boxButton);
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.SELECT;
                if (selectedContainer != null) {
                    if (selectedContainer.shapeType == ShapeEnum.RELATIONSHIPLINE) {
                        showLineMenu();
                    } else {
                        hideLineMenu();
                    }
                }

                window.setTitle("UML - Select");
                redrawShapes();
                resetSidebarButtons(selectButton);
            }
        });

        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTool = SelectedTool.LINE;
                showLineMenu();
                window.setTitle("UML - Line");
                redrawShapes();
                hideEditItems();
                resetSidebarButtons(lineButton);
            }
        });

        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int sx = 0;
                int sy = 0;

                int coord = checkTextBoxLocation(sx,sy);
                hideLineMenu();
                ShapeContainer t = new ShapeContainer();
                t.drawTextBox(coord,coord, 200, 200);
                t.setClassText("Freeform text");
                t.addMouseListener(containerListener);
                t.addMouseMotionListener(containerMotionListener);
                panel.add(t);
                shapeContainers.add(t);
                panel.repaint();
                resetSidebarButtons(textButton);

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
                    hideLineMenu();
                    resetSidebarButtons(deleteButton);
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
                    } else if ((selectedContainer.shapeType == ShapeEnum.RELATIONSHIPLINE)
                            && (comboBox.getSelectedItem().toString() == "Generalization")) {
                        for (ShapeContainer s : shapeContainers) {
                            if (s.getSelected() == true) {
                                s.relationshipType = RelationshipStatusEnum.GENERALIZATION;
                                s.redrawShape();
                            }
                        };
                    }

                }

            }
        });
        JLabel relationshipLabel = new JLabel("Line Relationship:");
        menuBar.add(menu);
        menuBar.add(editmenu);
        menuBar.add(relationshipLabel);
        menuBar.add(comboBox);
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

                            }

                            hideEditItems();
                            hideLineMenu();

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
                            container.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y, false);
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
                                hideLineMenu();
                            } else {
                                comboBox.setSelectedIndex(getRStat(s));
                                hideEditItems();
                                showLineMenu();
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
                redrawShapes();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        menuBar.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                redrawShapes();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        window.add(panel);

        window.show();

    }

    /**
     * Displays a text area in a JPanel popup window for editing the text of the
     * selected class container
     *
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

    /**
     *
     * @param filePath file path of the saved file
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void LoadFromFile(String filePath) throws FileNotFoundException, IOException, ClassNotFoundException {

        shapeContainers.clear();
        panel.removeAll();
        panel.repaint();

        FileInputStream fileInput = new FileInputStream(filePath);
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        ArrayList<SaveObject> savedObjects = (ArrayList<SaveObject>) objectInput.readObject();
        objectInput.close();
        fileInput.close();
        for (SaveObject s : savedObjects) {
            ShapeContainer newS = new ShapeContainer(s.type);
            newS.startX = s.startX;
            newS.startY = s.startY;
            newS.endX = s.endX;
            newS.endY = s.endY;
            newS.locationX = s.locationX;
            newS.locationY = s.locationY;
            newS.width = s.width;
            newS.height = s.height;
            newS.relationshipType = s.relStat;
            newS.shapeType = s.type;
            newS.classText = s.classText;
            shapeContainers.add(newS);

            if (newS.shapeType == ShapeEnum.CLASSBOX) {
                newS.drawBox(newS.locationX, newS.locationY, newS.width, newS.height);
            } else if (newS.shapeType == ShapeEnum.RELATIONSHIPLINE) {
                newS.drawLine(newS.startX, newS.startY, newS.endX, newS.endY, false);
            } else {
                newS.drawTextBox(newS.locationX, newS.locationY, newS.width, newS.height);
            }

            newS.addMouseListener(containerListener);
            newS.addMouseMotionListener(containerMotionListener);

            panel.add(newS);

            newS.redrawShape();

        }

    }

    /**
     *
     * @param filePath file path of where to save the file
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void SaveToFile(String filePath) throws FileNotFoundException, IOException {
        ArrayList<SaveObject> savedObjects = new ArrayList<>();
        for (ShapeContainer s : shapeContainers) {
            if (s.shapeType == ShapeEnum.RELATIONSHIPLINE) {
                savedObjects.add(new SaveObject(s.startX, s.startY, s.endX, s.endY, s.locationX, s.locationY, s.width, s.height, s.shapeType, s.relationshipType, null));
            } else if (s.shapeType == ShapeEnum.FREEFORMTEXT) {
                savedObjects.add(new SaveObject(s.startX, s.startY, s.endX, s.endY, s.locationX, s.locationY, s.width, s.height, s.shapeType, null, s.getClassText()));
            } else {
                savedObjects.add(new SaveObject(s.startX, s.startY, s.endX, s.endY, s.locationX, s.locationY, s.width, s.height, s.shapeType, null, s.getClassText()));
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
        for (ShapeContainer s : shapeContainers) {
            if (s.shapeType == ShapeEnum.RELATIONSHIPLINE) {
                s.drawLine(s.startX, s.startY, s.endX, s.endY, true);
                s.setLocation(s.locationX, s.locationY);
            } else if (s.shapeType == ShapeEnum.CLASSBOX) {
                s.drawBox(s.locationX, s.locationY, s.width, s.height);
            } else {
                s.drawTextBox(s.locationX, s.locationY, s.width, s.height);
            }
        }
    }

    /**
     *
     * @param s shape container
     * @return
     */
    private static int getRStat(ShapeContainer s) {
        if (s.relationshipType == RelationshipStatusEnum.GENERALIZATION) {
            return 1;
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

    /**
     * Prompts when unsaved changes are about to be trashed
     */
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

    public void hideLineMenu() {
        menuBar.getComponent(2).setVisible(false);
        menuBar.getComponent(3).setVisible(false);
    }

    public void showLineMenu() {
        menuBar.getComponent(2).setVisible(true);
        menuBar.getComponent(3).setVisible(true);
    }

    public void resetSidebarButtons(JButton b) {
        boxButton.setEnabled(true);
        selectButton.setEnabled(true);
        lineButton.setEnabled(true);
        textButton.setEnabled(true);
        deleteButton.setEnabled(true);

        if (b != deleteButton & b != textButton) {
            b.setEnabled(false);
        }
    }
    
    public int checkTextBoxLocation(int x, int y){
        for (ShapeContainer s : shapeContainers) {
            if (s.locationX == x && s.locationY ==y) {
                if(x<window.getSize().width-200 && y<window.getSize().height-200){
                    x += 50;
                    y += 50;
                   checkTextBoxLocation(x,y); 
                }else{
                    return x;
                } 
            }
        }
        return x;
    }
}
