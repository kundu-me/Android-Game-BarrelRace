package com.utdallas.nxkundu.barrelracegame.gamehandler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.utdallas.nxkundu.barrelracegame.gamecomponents.Component;
import com.utdallas.nxkundu.barrelracegame.gamesettings.GameSettings;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by nxkundu on 4/21/18.
 */

public class GameHandler {

    private ConcurrentMap<String, Component> mapGameComponents;

    private ConcurrentMap<String, Long> mapBarrelBoundaryCrossed;

    private long timeStampCourseTouched = -1;

    public static final String DIRECTION_EAST = "EAST";
    public static final String DIRECTION_WEST = "WEST";
    public static final String DIRECTION_NORTH = "NORTH";
    public static final String DIRECTION_SOUTH = "SOUTH";

    private boolean isGameCompleted = false;
    private boolean isBarrelTouched = false;
    private boolean isCourseTouched = false;

    public GameHandler(ConcurrentMap<String, Component> mapGameComponents) {

        super();
        this.mapGameComponents = mapGameComponents;
        this.mapBarrelBoundaryCrossed = new ConcurrentHashMap<>();
    }

    public void drawComponents(Canvas canvas) {

        if(mapGameComponents == null || mapGameComponents.size() == 0) {
            return;
        }

        for(String componentKey : mapGameComponents.keySet()) {

            Component component = mapGameComponents.get(componentKey);

            if(component != null) {
                component.drawComponent(canvas);
            }
        }

    }

    public void drawGameComponents(SurfaceHolder surfaceHolder) {

        Canvas canvas = null;

        try {

            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(GameSettings.BACKGROUND_COLOR);

            if (canvas != null) {

                drawComponents(canvas);

            }
        }
        finally {
            if (canvas != null) {

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void handleHorseMovement(SurfaceView surfaceViewPlayArea,
                                    long eventTimestamp, float accelerationX, float accelerationY, float accelerationZ) {

        if(isGameCompleted || isBarrelTouched) {
            return;
        }
        /**
         * Wait for WAIT_TIME_ON_COURSE_TOUCH if the course is touched
         */
        if(timeStampCourseTouched != -1
                && System.currentTimeMillis() - timeStampCourseTouched <= GameSettings.WAIT_ON_COURSE_TOUCH_LONG_TIME) {

            return;
        }


        Component componentHorse = mapGameComponents.get(Component.COMPONENT_NAME_HORSE_1);

        long diffTime = (eventTimestamp - componentHorse.getTimeLastUpdated());

        if (diffTime > 100) {

            componentHorse.updateComponent(eventTimestamp, GameSettings.ACCELEROMETER_RATE,
                    accelerationX, accelerationY, accelerationZ);

            mapGameComponents.put(Component.COMPONENT_NAME_HORSE_1, componentHorse);

            drawGameComponents(surfaceViewPlayArea.getHolder());

            isBarrelTouched = isAnyBarrelTouched();

            if(isBarrelTouched) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>> isBarrelTouched = " + isBarrelTouched);
                return;
            }

            isCourseTouched = isAnyCourseTouched();

            if(isCourseTouched) {

                timeStampCourseTouched = System.currentTimeMillis();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>> isCourseTouched = " + isCourseTouched);
            }
            else {

                timeStampCourseTouched = -1;
            }

            if(mapBarrelBoundaryCrossed.size() == 12) {

                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>COMPLETED 3 Barrel circle ****** " + mapBarrelBoundaryCrossed.keySet());

                if((componentHorse.getY1() + componentHorse.getRadius() >= componentHorse.getYMax())
                    && (componentHorse.getX1() >= componentHorse.getXMax()/2 - 50 )
                    && (componentHorse.getX1() <= componentHorse.getXMax()/2 + 50 )) {

                    isGameCompleted = true;
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>GAME COMPLETED <<<<<<<<<<<<<<<");
                }

            }
            else {

                updateMapBarrelBoundaryCrossed();
                System.out.println(mapBarrelBoundaryCrossed.keySet());
            }
        }
    }

    private boolean isAnyBarrelTouched() {


        Component componentHorse = mapGameComponents.get(Component.COMPONENT_NAME_HORSE_1);
        float x1Horse = componentHorse.getX1();
        float y1Horse = componentHorse.getY1();

        Component barrel = mapGameComponents.get(Component.COMPONENT_NAME_BARREL_1);
        float x1Barrel = barrel.getX1();
        float y1Barrel = barrel.getY1();
        if(isCirclesIntersect(x1Horse, y1Horse, x1Barrel, y1Barrel,
                GameSettings.HORSE_RADIUS, GameSettings.BARREL_RADIUS)) {
            return true;
        }

        barrel = mapGameComponents.get(Component.COMPONENT_NAME_BARREL_2);
        x1Barrel = barrel.getX1();
        y1Barrel = barrel.getY1();
        if(isCirclesIntersect(x1Horse, y1Horse, x1Barrel, y1Barrel,
                GameSettings.HORSE_RADIUS, GameSettings.BARREL_RADIUS)) {
            return true;
        }

        barrel = mapGameComponents.get(Component.COMPONENT_NAME_BARREL_3);
        x1Barrel = barrel.getX1();
        y1Barrel = barrel.getY1();
        if(isCirclesIntersect(x1Horse, y1Horse, x1Barrel, y1Barrel,
                GameSettings.HORSE_RADIUS, GameSettings.BARREL_RADIUS)) {
            return true;
        }

        return false;
    }

    private void updateMapBarrelBoundaryCrossed() {

        checkBarrelBoundaryCrossed(Component.COMPONENT_NAME_BARREL_1);
        checkBarrelBoundaryCrossed(Component.COMPONENT_NAME_BARREL_2);
        checkBarrelBoundaryCrossed(Component.COMPONENT_NAME_BARREL_3);
    }

    private void checkBarrelBoundaryCrossed(String componentNameBarrel) {

        Component componentHorse = mapGameComponents.get(Component.COMPONENT_NAME_HORSE_1);
        float x1Horse = componentHorse.getX1();
        float y1Horse = componentHorse.getY1();

        Component barrel = mapGameComponents.get(componentNameBarrel);
        float x1Barrel = barrel.getX1();
        float y1Barrel = barrel.getY1();

        boolean isAnyBarrelBoundaryCrossed = false;

        /**
         * EAST
         */
        if((!mapBarrelBoundaryCrossed.containsKey(componentNameBarrel + DIRECTION_EAST))
                && (Math.abs(y1Horse - y1Barrel) <= GameSettings.BARREL_BOUNDARY_MIN_DISTANCE)
                && (Math.abs(x1Horse - x1Barrel) <= GameSettings.BARREL_BOUNDARY_RADIUS)
                && (x1Horse < x1Barrel)) {

            mapBarrelBoundaryCrossed.put(componentNameBarrel + DIRECTION_EAST, System.currentTimeMillis());

            isAnyBarrelBoundaryCrossed = true;
        }

        /**
         * WEST
         */
        if((!mapBarrelBoundaryCrossed.containsKey(componentNameBarrel + DIRECTION_WEST))
                && (Math.abs(y1Horse - y1Barrel) <= GameSettings.BARREL_BOUNDARY_MIN_DISTANCE)
                && (Math.abs(x1Horse - x1Barrel) <= GameSettings.BARREL_BOUNDARY_RADIUS)
                && (x1Horse > x1Barrel)) {

            mapBarrelBoundaryCrossed.put(componentNameBarrel + DIRECTION_WEST, System.currentTimeMillis());

            isAnyBarrelBoundaryCrossed = true;
        }
        /**
         * NORTH
         */
        if((!mapBarrelBoundaryCrossed.containsKey(componentNameBarrel + DIRECTION_NORTH))
                && (Math.abs(x1Horse - x1Barrel) <= GameSettings.BARREL_BOUNDARY_MIN_DISTANCE)
                && (Math.abs(y1Horse - y1Barrel) <= GameSettings.BARREL_BOUNDARY_RADIUS)
                && (y1Horse < y1Barrel)) {

            mapBarrelBoundaryCrossed.put(componentNameBarrel + DIRECTION_NORTH, System.currentTimeMillis());

            isAnyBarrelBoundaryCrossed = true;
        }

        /**
         * SOUTH
         */
        if((!mapBarrelBoundaryCrossed.containsKey(componentNameBarrel + DIRECTION_SOUTH))
                && (Math.abs(x1Horse - x1Barrel) <= GameSettings.BARREL_BOUNDARY_MIN_DISTANCE)
                && (Math.abs(y1Horse - y1Barrel) <= GameSettings.BARREL_BOUNDARY_RADIUS)
                && (y1Horse > y1Barrel)) {

            mapBarrelBoundaryCrossed.put(componentNameBarrel + DIRECTION_SOUTH, System.currentTimeMillis());

            isAnyBarrelBoundaryCrossed = true;
        }

        /**
         * UPDATE COUNT NUMBER OF POSITION CROSSED
         */
        if(isAnyBarrelBoundaryCrossed) {

            updateCountBarrelCompleted(barrel);
        }

    }

    private void updateCountBarrelCompleted(Component barrel) {

        int countBarrelCompleted = 0;
        if(barrel.getMapComponentSettings() != null
                && barrel.getMapComponentSettings().containsKey(Component.COMPONENT_SETTINGS_BARREL_ROUND_COMPLETED)) {
            countBarrelCompleted = barrel.getMapComponentSettings().get(Component.COMPONENT_SETTINGS_BARREL_ROUND_COMPLETED);
        }

        barrel.getMapComponentSettings().put(Component.COMPONENT_SETTINGS_BARREL_ROUND_COMPLETED, ++countBarrelCompleted);
    }

    private boolean isCirclesIntersect(float x1, float y1, float x2, float y2,
                                      float radius1, float radius2) {

        float distanceSquare = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        float radiusSumSquare = (radius1 + radius2) * (radius1 + radius2);

        if (distanceSquare < radiusSumSquare) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isAnyCourseTouched() {

        Component componentHorse = mapGameComponents.get(Component.COMPONENT_NAME_HORSE_1);
        float x1Horse = componentHorse.getX1();
        float y1Horse = componentHorse.getY1();

        /**
         * TOP
         */
        Component course = mapGameComponents.get(Component.COMPONENT_NAME_COURSE_TOP);
        if(y1Horse - GameSettings.HORSE_RADIUS <= course.getY1() + GameSettings.COURSE_LINE_WIDTH) {

            return true;
        }

        /**
         * LEFT
         */
        course = mapGameComponents.get(Component.COMPONENT_NAME_COURSE_LEFT);
        if(x1Horse - GameSettings.HORSE_RADIUS <= course.getX1() + GameSettings.COURSE_LINE_WIDTH) {

            return true;
        }

        /**
         * RIGHT
         */
        course = mapGameComponents.get(Component.COMPONENT_NAME_COURSE_RIGHT);
        if(x1Horse + GameSettings.HORSE_RADIUS >= course.getX1() - GameSettings.COURSE_LINE_WIDTH) {

            return true;
        }

        /**
         * BOTTOM LEFT
         */
        course = mapGameComponents.get(Component.COMPONENT_NAME_COURSE_BOTTOM_LEFT);
        if(y1Horse + GameSettings.HORSE_RADIUS >= course.getY1() - GameSettings.COURSE_LINE_WIDTH
                && x1Horse >= course.getX1() && x1Horse <= course.getX2())  {

            return true;
        }

        /**
         * BOTTOM RIGHT
         */
        course = mapGameComponents.get(Component.COMPONENT_NAME_COURSE_BOTTOM_RIGHT);
        if(y1Horse + GameSettings.HORSE_RADIUS >= course.getY1() - GameSettings.COURSE_LINE_WIDTH
                && x1Horse >= course.getX1() && x1Horse <= course.getX2())  {

            return true;
        }


        return false;
    }

    public boolean isGameCompleted() {
        return isGameCompleted;
    }

    public boolean isBarrelTouched() {
        return isBarrelTouched;
    }

    public boolean isCourseTouched() {
        return isCourseTouched;
    }

    public long getTimeStampCourseTouched() {
        return timeStampCourseTouched;
    }

    public ConcurrentMap<String, Component> getMapGameComponents() {
        return mapGameComponents;
    }

    public void setMapGameComponents(ConcurrentMap<String, Component> mapGameComponents) {
        this.mapGameComponents = mapGameComponents;
    }
}
