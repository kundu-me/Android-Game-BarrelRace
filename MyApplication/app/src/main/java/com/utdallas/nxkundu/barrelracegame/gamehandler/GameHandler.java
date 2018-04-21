package com.utdallas.nxkundu.barrelracegame.gamehandler;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.utdallas.nxkundu.barrelracegame.gamecomponents.Component;
import com.utdallas.nxkundu.barrelracegame.gamesettings.GameSettings;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by nxkundu on 4/21/18.
 */

public class GameHandler {

    private ConcurrentMap<String, Component> mapGameComponents;

    private long timeStampCourseTouched = -1;

    public GameHandler(ConcurrentMap<String, Component> mapGameComponents) {

        super();
        this.mapGameComponents = mapGameComponents;
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

            boolean isBarrelTouched = isBarrelTouched();

            if(isBarrelTouched) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>> isBarrelTouched = " + isBarrelTouched);
            }

            boolean isCourseTouched = isCourseTouched();

            if(isCourseTouched) {

                timeStampCourseTouched = System.currentTimeMillis();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>> isCourseTouched = " + isCourseTouched);
            }
            else {

                timeStampCourseTouched = -1;
            }
        }
    }

    private boolean isBarrelTouched() {


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

    private boolean isCourseTouched() {

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

    private boolean isCircleLineIntersect() {

        return false;
    }

    public ConcurrentMap<String, Component> getMapGameComponents() {
        return mapGameComponents;
    }

    public void setMapGameComponents(ConcurrentMap<String, Component> mapGameComponents) {
        this.mapGameComponents = mapGameComponents;
    }
}
