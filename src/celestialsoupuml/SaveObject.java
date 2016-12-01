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
public class SaveObject implements java.io.Serializable {

    public ShapeEnum type;
    public RelationshipStatusEnum relStat;
    public int startX, startY, endX, endY, locationX, locationY, width, height;
    public String classText;

    /**
     *
     * @param startX starting position of x
     * @param startY starting position of y
     * @param endX ending position of x
     * @param endY ending position of y
     * @param locX coordinate location of x
     * @param locY coordinate location of y
     * @param width width of the shape
     * @param height height of the shape
     * @param shapeType type of shape
     * @param relationshipStatus relationship type of line
     * @param classText class text of class box
     */
    public SaveObject(int startX, int startY, int endX, int endY, int locX, int locY, int width, int height, ShapeEnum shapeType, RelationshipStatusEnum relationshipStatus, String classText) {
        if (shapeType == ShapeEnum.RELATIONSHIPLINE) {
            this.startX = startX;
            this.startY = startY;
            this.locationX = locX;
            this.locationY = locY;
            this.endX = endX;
            this.endY = endY;
            this.type = shapeType;
            this.relStat = relationshipStatus;
            this.classText = null;
        } else {

            this.startX = startX;
            this.startY = startY;
            this.width = width;
            this.height = height;
            this.locationX = locX;
            this.locationY = locY;
            this.type = shapeType;
            this.relStat = null;
            this.classText = classText;
        }
    }
}
