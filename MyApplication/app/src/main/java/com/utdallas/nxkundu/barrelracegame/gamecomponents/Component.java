package com.utdallas.nxkundu.barrelracegame.gamecomponents;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.utdallas.nxkundu.barrelracegame.gamesettings.GameSettings;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by nxkundu on 4/20/18.
 */
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This class contains the basic Component Object which is common
 * for all the Componenets to be used in the Game
 *
 * 1. Horse (1)
 * 2. Barrel (3)
 * 3. Course Top
 * 4. Course Left
 * 5. Course Right
 * 5. Course Bottom Left
 * 6. Course Bottom Right
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

public class Component {

    public  static final String COMPONENT_SHAPE_LINE = "LINE";
    public  static final String COMPONENT_SHAPE_CIRCLE = "CIRCLE";

    public static final String COMPONENT_TYPE_HORSE = "HORSE";
    public static final String COMPONENT_TYPE_BARREL = "BARREL";
    public static final String COMPONENT_TYPE_COURSE = "COURSE";

    public static final String COMPONENT_SETTINGS_BARREL_ROUND_COMPLETED = "BARREL_ROUND_COMPLETED";

    public static final String COMPONENT_NAME_HORSE_1 = "HORSE_1";

    public static final String COMPONENT_NAME_BARREL_1 = "BARREL_1";
    public static final String COMPONENT_NAME_BARREL_2 = "BARREL_2";
    public static final String COMPONENT_NAME_BARREL_3 = "BARREL_3";

    public static final String COMPONENT_NAME_COURSE_BOTTOM_LEFT = "COURSE_BOTTOM_LEFT";
    public static final String COMPONENT_NAME_COURSE_BOTTOM_RIGHT = "COURSE_BOTTOM_RIGHT";
    public static final String COMPONENT_NAME_COURSE_LEFT = "COURSE_LEFT";
    public static final String COMPONENT_NAME_COURSE_RIGHT = "COURSE_RIGHT";
    public static final String COMPONENT_NAME_COURSE_TOP = "COURSE_TOP";

    /***********************************************************
     * This map Contains the Component specific settings
     ***********************************************************/
    private ConcurrentMap<String, Integer> mapComponentSettings;

    private String componentType;
    private String componentShape;
    private String componentName;

    private Paint paint;

    private float x1;
    private float y1;

    private float x2;
    private float y2;

    private float xMax;
    private float yMax;

    private float height;
    private float width;
    private float radius;

    private float accelerationX;
    private float accelerationY;
    private float accelerationZ;

    private long timeLastUpdated;

    /**************************************************************************
     * Constructor
     *
     **************************************************************************/
    public Component(String componentType, String componentShape, String componentName) {

        this.componentType = componentType;
        this.componentShape = componentShape;
        this.componentName = componentName;

        this.mapComponentSettings = new ConcurrentHashMap<>();

        this.timeLastUpdated = System.currentTimeMillis();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**************************************************************************
     * Method
     * drawComponent() -
     * Draws the Component based on the shape of the component
     * and the position values passed
     **************************************************************************/
    public void drawComponent(Canvas canvas) {

        switch (this.componentType) {

            case COMPONENT_TYPE_BARREL:

                if (mapComponentSettings.containsKey(COMPONENT_SETTINGS_BARREL_ROUND_COMPLETED)
                        && mapComponentSettings.get(COMPONENT_SETTINGS_BARREL_ROUND_COMPLETED) == 4) {

                    paint.setColor(GameSettings.BARREL_COLOR_COMPLETED);
                }
                else{
                    paint.setColor(GameSettings.BARREL_COLOR);
                }

                break;

            case COMPONENT_TYPE_HORSE:
                paint.setColor(GameSettings.HORSE_COLOR);
                break;

            case COMPONENT_TYPE_COURSE:
                paint.setColor(GameSettings.COURSE_COLOR);
                break;
        }


        switch (this.componentShape) {

            case COMPONENT_SHAPE_LINE:

                paint.setStrokeWidth(GameSettings.COURSE_LINE_WIDTH);
                canvas.drawLine(x1, y1, x2, y2, paint);
                break;

            case COMPONENT_SHAPE_CIRCLE:

                canvas.drawCircle(x1, y1, radius, paint);
                break;
        }
    }

    /**************************************************************************
     * Method
     * updateComponent() -
     * This method updates the position variables of any component
     **************************************************************************/
    public void updateComponent(long eventTimestamp, float accRate, float accX, float accY, float accZ) {

        timeLastUpdated = eventTimestamp;
        accelerationX = accX * accRate;
        accelerationY = accY * accRate * 1.5f;
        accelerationZ = accZ;

        x1 += accelerationX;
        y1 += accelerationY;

        x2 += accelerationX;
        y2 += accelerationY;

        x1 = checkMinMax(x1, 0, xMax);
        y1 = checkMinMax(y1, 0, yMax);

        x2 = checkMinMax(x2, 0, xMax);
        y2 = checkMinMax(y2, 0, yMax);

        //System.out.println("(" + x1 + ", " + y1 + ")");
    }

    /**************************************************************************
     * Method
     * checkMinMax()
     * This method is used to make the screen a square shaped
     * takig the minimum of the screen width and height
     **************************************************************************/
    public float checkMinMax(float point, float pointMin, float pointMax) {

        point = point < pointMin ? pointMin : point;
        point = point > pointMax ? pointMax : point;
        return point;
    }

    /**************************************************************************
     * Method
     * Getters and Setters
     **************************************************************************/
    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentShape() {
        return componentShape;
    }

    public void setComponentShape(String componentShape) {
        this.componentShape = componentShape;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public long getTimeLastUpdated() {
        return timeLastUpdated;
    }

    public void setTimeLastUpdated(long timeLastUpdated) {
        this.timeLastUpdated = timeLastUpdated;
    }

    public float getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public float getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public float getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(float accelerationZ) {
        this.accelerationZ = accelerationZ;
    }

    public float getXMax() {
        return xMax;
    }

    public void setXMax(float xMax) {
        this.xMax = xMax;
    }

    public float getYMax() {
        return yMax;
    }

    public void setYMax(float yMax) {
        this.yMax = yMax;
    }

    public ConcurrentMap<String, Integer> getMapComponentSettings() {
        return mapComponentSettings;
    }

    public void setMapComponentSettings(ConcurrentMap<String, Integer> mapComponentSettings) {
        this.mapComponentSettings = mapComponentSettings;
    }

    public float getxMax() {
        return xMax;
    }

    public void setxMax(float xMax) {
        this.xMax = xMax;
    }

    public float getyMax() {
        return yMax;
    }

    public void setyMax(float yMax) {
        this.yMax = yMax;
    }
}