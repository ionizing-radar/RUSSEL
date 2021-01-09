package com.example.russel;

import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.InterpolatorRes;

//import android.graphics.AvoidXfermode;

/**
 * Created by Daniel on 7/25/2016.
 * https://github.com/efficientisoceles/JoystickView
 * Changes by ionizing-radar:
 * - class properties radius and theta, and getters for both (allow for polar coodinates for driving)
 * - multiple casts to (float)
 * - removed shading visual effects
 */
public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private float radius;
    private float theta;
    private JoystickListener joystickCallback;

    private void setupDimensions() {
        centerX = (float) getWidth() / 2;
        centerY = (float) getHeight() / 2;
        baseRadius = (float) Math.min(getWidth(), getHeight()) / 4;
        hatRadius = (float) Math.min(getWidth(), getHeight()) / 5;
    }

    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView (Context context, AttributeSet attributes) {
        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    private void drawJoystick(float newX, float newY) {
        if(getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas(); //Stuff to draw
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // Clear the BG

            //Draw the base
            colors.setARGB(255, 100, 100, 100);
            myCanvas.drawCircle(centerX, centerY, baseRadius, colors);

            // Draw the hat
            colors.setARGB(240, 68, 68, 68); //Change the joystick color for shading purposes
            myCanvas.drawCircle(newX, newY, hatRadius, colors); //Draw the shading for the hat

            getHolder().unlockCanvasAndPost(myCanvas); //Write the new drawing to the SurfaceView
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)  {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouch(View v, MotionEvent e) {
        if(v.equals(this)) {
            if(e.getAction() != MotionEvent.ACTION_UP) { // MotionEvent.ACTION_UP is called when touch event ends (finger lifted off screen)
                // find the hypotenuse of the (X,Y) position of the touch event
                float displacement = (float) Math.sqrt((Math.pow(e.getX() - centerX, 2)) + Math.pow(e.getY() - centerY, 2));

                radius = (float) Math.min(displacement/baseRadius, 1);
                // subtracting pi/2 might not be needed in landscape ...
                theta = (float) (Math.atan2(ratioize((e.getY()-centerY)/baseRadius)*-1,ratioize((e.getX()-centerX)/baseRadius))-(Math.PI/2));

                Log.i(JoystickView.class.getName(), "radius: "+radius+"     theta: "+theta);

                if(displacement < baseRadius) { // displacement within constrained distance
                    drawJoystick(e.getX(), e.getY());
                    joystickCallback.onJoystickMoved((e.getX() - centerX)/baseRadius, (e.getY() - centerY)/baseRadius, getId());
                } else { // distance outside constrained distance, need to scale it back
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (e.getX() - centerX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX-centerX)/baseRadius, (constrainedY-centerY)/baseRadius, getId());
                }
            } else // reset joystick to center when released
                drawJoystick(centerX, centerY);
            joystickCallback.onJoystickMoved(0,0,getId());
        }
        return true;
    }

    public float getRadius() { return radius; }
    public float getTheta() { return theta; }

    private float ratioize(float n) {
        if (n > 1) return 1;
        if (n < -1) return -1;
        return n;
    }

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}