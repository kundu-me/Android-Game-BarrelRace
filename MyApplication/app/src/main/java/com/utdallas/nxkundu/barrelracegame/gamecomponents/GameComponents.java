package com.utdallas.nxkundu.barrelracegame.gamecomponents;

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
 * This class creates of ALL the Components to be used in the Game
 * and put those in a map
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

public class GameComponents {

    private float screenWidth;
    private float screenHeight;
    private float minWidthHeight;

    /**************************************************************************
     * Constructor
     *
     **************************************************************************/
    public GameComponents(float screenWidth, float screenHeight) {

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.minWidthHeight = Math.min(screenWidth, screenHeight);
    }

    /**************************************************************************
     * Method
     * getGameComponents()
     * This method returns the map of components that are used in the game
     **************************************************************************/
    public ConcurrentMap<String, Component> getGameComponents() {

        ConcurrentMap<String, Component> mapComponents = new ConcurrentHashMap<>();

        /**
         * Horse
         */
        Component componentHorse = new Component(Component.COMPONENT_TYPE_HORSE,
                Component.COMPONENT_SHAPE_CIRCLE, Component.COMPONENT_NAME_HORSE_1);

        componentHorse.setX1(minWidthHeight/2);
        componentHorse.setY1(minWidthHeight + 50);
        componentHorse.setXMax(minWidthHeight);
        componentHorse.setYMax(minWidthHeight);
        componentHorse.setRadius(GameSettings.BARREL_RADIUS);

        mapComponents.put(Component.COMPONENT_NAME_HORSE_1, componentHorse);

        /**
         * Barrels
         */
        /**
         * Barrel1
         */
        Component componentBarrel1 = new Component(Component.COMPONENT_TYPE_BARREL,
                Component.COMPONENT_SHAPE_CIRCLE, Component.COMPONENT_NAME_BARREL_1);

        componentBarrel1.setX1(minWidthHeight/2);
        componentBarrel1.setY1(minWidthHeight/1.5f);
        componentBarrel1.setXMax(minWidthHeight);
        componentBarrel1.setYMax(minWidthHeight);
        componentBarrel1.setRadius(GameSettings.BARREL_RADIUS);
        mapComponents.put(Component.COMPONENT_NAME_BARREL_1, componentBarrel1);

        /**
         * Barrel2
         */
        Component componentBarrel2 = new Component(Component.COMPONENT_TYPE_BARREL,
                Component.COMPONENT_SHAPE_CIRCLE, Component.COMPONENT_NAME_BARREL_2);

        componentBarrel2.setX1(screenWidth/3);
        componentBarrel2.setY1(screenHeight/5f);
        componentBarrel2.setXMax(minWidthHeight);
        componentBarrel2.setYMax(minWidthHeight);
        componentBarrel2.setRadius(GameSettings.BARREL_RADIUS);
        mapComponents.put(Component.COMPONENT_NAME_BARREL_2, componentBarrel2);

        /**
         * Barrel3
         */
        Component componentBarrel3 = new Component(Component.COMPONENT_TYPE_BARREL,
                Component.COMPONENT_SHAPE_CIRCLE, Component.COMPONENT_NAME_BARREL_3);

        componentBarrel3.setX1(screenWidth/1.5f);
        componentBarrel3.setY1(screenHeight/5f);
        componentBarrel3.setXMax(minWidthHeight);
        componentBarrel3.setYMax(minWidthHeight);
        componentBarrel3.setRadius(GameSettings.BARREL_RADIUS);
        mapComponents.put(Component.COMPONENT_NAME_BARREL_3, componentBarrel3);

        /**
         * Course
         */
        /**
         * Course Bottom Left
         */
        Component componentCourseBottomLeft = new Component(Component.COMPONENT_TYPE_COURSE,
                Component.COMPONENT_SHAPE_LINE, Component.COMPONENT_NAME_COURSE_BOTTOM_LEFT);

        componentCourseBottomLeft.setX1(20);
        componentCourseBottomLeft.setY1(minWidthHeight);
        componentCourseBottomLeft.setXMax(minWidthHeight);
        componentCourseBottomLeft.setYMax(minWidthHeight);
        componentCourseBottomLeft.setX2(minWidthHeight/2 - 100);
        componentCourseBottomLeft.setY2(minWidthHeight);

        mapComponents.put(Component.COMPONENT_NAME_COURSE_BOTTOM_LEFT, componentCourseBottomLeft);

        /**
         * Course Bottom Right
         */
        Component componentCourseBottomRight = new Component(Component.COMPONENT_TYPE_COURSE,
                Component.COMPONENT_SHAPE_LINE, Component.COMPONENT_NAME_COURSE_BOTTOM_RIGHT);

        componentCourseBottomRight.setX1(screenWidth/2 + 100);
        componentCourseBottomRight.setY1(minWidthHeight);
        componentCourseBottomRight.setXMax(minWidthHeight);
        componentCourseBottomRight.setYMax(minWidthHeight);
        componentCourseBottomRight.setX2(minWidthHeight - 20);
        componentCourseBottomRight.setY2(minWidthHeight);

        mapComponents.put(Component.COMPONENT_NAME_COURSE_BOTTOM_RIGHT, componentCourseBottomRight);

        /**
         * Course Left
         */
        Component componentCourseLeft = new Component(Component.COMPONENT_TYPE_COURSE,
                Component.COMPONENT_SHAPE_LINE, Component.COMPONENT_NAME_COURSE_LEFT);

        componentCourseLeft.setX1(20);
        componentCourseLeft.setY1(20);
        componentCourseLeft.setXMax(minWidthHeight);
        componentCourseLeft.setYMax(minWidthHeight);
        componentCourseLeft.setX2(20);
        componentCourseLeft.setY2(minWidthHeight);

        mapComponents.put(Component.COMPONENT_NAME_COURSE_LEFT, componentCourseLeft);

        /**
         * Course Top
         */
        Component componentCourseTop = new Component(Component.COMPONENT_TYPE_COURSE,
                Component.COMPONENT_SHAPE_LINE, Component.COMPONENT_NAME_COURSE_TOP);

        componentCourseTop.setX1(20);
        componentCourseTop.setY1(20);
        componentCourseTop.setXMax(minWidthHeight);
        componentCourseTop.setYMax(minWidthHeight);
        componentCourseTop.setX2(minWidthHeight - 20);
        componentCourseTop.setY2(20);

        mapComponents.put(Component.COMPONENT_NAME_COURSE_TOP, componentCourseTop);

        /**
         * Course Right
         */
        Component componentCourseRight = new Component(Component.COMPONENT_TYPE_COURSE,
                Component.COMPONENT_SHAPE_LINE, Component.COMPONENT_NAME_COURSE_RIGHT);

        componentCourseRight.setX1(minWidthHeight - 20);
        componentCourseRight.setY1(20);
        componentCourseRight.setXMax(minWidthHeight);
        componentCourseRight.setYMax(minWidthHeight);
        componentCourseRight.setX2(minWidthHeight - 20);
        componentCourseRight.setY2(minWidthHeight);

        mapComponents.put(Component.COMPONENT_NAME_COURSE_RIGHT, componentCourseRight);

        return mapComponents;
    }
}
