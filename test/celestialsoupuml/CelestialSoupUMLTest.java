/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

import com.sun.glass.events.MouseEvent;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Brandan
 */
public class CelestialSoupUMLTest {
    public CelestialSoupUMLTest() {
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
     * Test of main method, of class CelestialSoupUML.
     */
    @Test
    public void testMain() throws AWTException, InterruptedException {
        System.out.println("main");
        
        String[] args = null;
        CelestialSoupUML.main(args);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() / 2);
        int height = (int) (screenSize.getHeight() / 2);
        Robot r = new Robot();
        ShapeContainer sc = new ShapeContainer(ShapeEnum.RELATIONSHIPLINE);
        sc.drawLine(width, height, width + 300, height + 300);
        r.mouseMove(87, 127);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(8000);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mouseMove(width, height);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseMove( width + 100 , height + 100);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.delay(8000);
        assert(WindowSingleton.shapeContainers.size() > 0 );
        r.mouseMove(87, 100);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(8000);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.mouseMove(width - 100, height - 100);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.mouseMove(width - 300, height - 300);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.delay(8000);
        assert(WindowSingleton.shapeContainers.size() > 1);
        
        
        
        
        
        
        
       
        
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
