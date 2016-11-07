/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celestialsoupuml;

/**
 *
 * @author Brandan
 */

/*
   This class exists because ShapeContainer cannot be serialized properly
   due to there being a super class in it.
*/
public class SaveObject implements java.io.Serializable{
    
    public ShapeEnum type;
    public int startX, startY, endX, endY, width, height;
    public SaveObject(int startX, int startY, int endX, int endY, int width, int height, ShapeEnum shapeType)
    {
        if(shapeType == ShapeEnum.RELATIONSHIPLINE)
        {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.type = shapeType;
        }
        else
        {
            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
            this.type = shapeType;
        }
    }
}
