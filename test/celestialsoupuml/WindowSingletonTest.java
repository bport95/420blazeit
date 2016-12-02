/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import celestialsoupuml.WindowSingleton.SelectedTool;
import javax.swing.JMenu;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ben
 */
public class WindowSingletonTest {
    
    public WindowSingletonTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class WindowSingleton.
     */
    @Test
    public void testGetInstance() {
        //test whether or not the combo box appears
        WindowSingleton w = WindowSingleton.getInstance();
        assert(w.comboBox.isVisible() == false);
        
        w.selectButton.doClick();
        assert(w.comboBox.isVisible() == false);
        assert(w.selectButton.isEnabled() == false);
        assert(w.lineButton.isEnabled() == true);
        assert(w.boxButton.isEnabled() == true);
        assert(w.textButton.isEnabled() == true);
        assert(w.deleteButton.isEnabled() == true);
        
        w.lineButton.doClick();
        assert(w.comboBox.isVisible() == true);
        assert(w.selectButton.isEnabled() == true);
        assert(w.lineButton.isEnabled() == false);
        assert(w.boxButton.isEnabled() == true);
        assert(w.textButton.isEnabled() == true);
        assert(w.deleteButton.isEnabled() == true);
        assert(((JMenu) w.menuBar.getComponent(1)).getItem(0).isEnabled() == false);
        
        w.boxButton.doClick();
        assert(w.comboBox.isVisible() == false);
        assert(w.selectButton.isEnabled() == true);
        assert(w.lineButton.isEnabled() == true);
        assert(w.boxButton.isEnabled() == false);
        assert(w.textButton.isEnabled() == true);
        assert(w.deleteButton.isEnabled() == true);
        
        w.deleteButton.doClick();
        assert(w.comboBox.isVisible() == false);
        assert(w.selectButton.isEnabled() == true);
        assert(w.lineButton.isEnabled() == true);
        assert(w.boxButton.isEnabled() == true);
        assert(w.textButton.isEnabled() == true);
        assert(w.deleteButton.isEnabled() == true);

        w.textButton.doClick();
        assert(w.comboBox.isVisible() == false);
        assert(w.selectButton.isEnabled() == true);
        assert(w.lineButton.isEnabled() == true);
        assert(w.boxButton.isEnabled() == true);
        assert(w.textButton.isEnabled() == true);
        assert(w.deleteButton.isEnabled() == true);
        assert(((JMenu) w.menuBar.getComponent(1)).getItem(0).isEnabled() == true);

        
    }
    
}
