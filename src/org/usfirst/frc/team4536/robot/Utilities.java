package org.usfirst.frc.team4536.robot;

import java.lang.Math;

public class Utilities {
	
	/*
	 * Takes in values and makes them between -1 and 1
	 */
	public static double limit (double input) {;		
		if(input > 1) {
			return 1;
		}
		else if(input < -1) {
			return -1;
		}
		else {
			return input;
		}
	}
		
	/*
	 * Takes in values and makes sure they are both above deadZone and below -deadZone
	 * We use it to create a small region on our joysticks that outputs 0
	 * Once the joystick gets outside of the dead zone, it outputs their actual values
	 */
	public static double deadZone (double input, double deadZone) {		
		if ((input < deadZone) && (input > -deadZone))      //if the input is within the dead zone, return 0
            return 0;
        else
            return input;
     	}
	
	
	/*
	 * Takes in two values - the input and curve. Input is taken to the power of curve 
	 * We use this method on our joysticks. It allows our driver to have more control over 
	 * the robot using the joysick values closer to 0. 
	 */
	public static double speedCurve(double input, double curve) {
        double output;
        double adjustedCurve = curve;
        
        //We don't want to have bases smaller than 0.1 because of weird math
        if(curve <= 0.1) {         
            adjustedCurve = 0.1;
        }
        
        //If the input is negative then pretend that it's positive 
        if(input < 0) {            
            output = -(Math.pow(-input, adjustedCurve));  //and reflect it over the X axis
        }
        //If it's positive then simply take it to the power of the adjustedCurve
        else {                     
            output = Math.pow(input, adjustedCurve);
        }
        
        //output has to be between -1 and 1
        limit(output);
        
        return output;
    }
	
	/*
	 * This is used to limit the increase of throttleTakes in three values - fullSpeedTime, throttle, prevValue. The fullSpeedTime defines 
	 * the amount of time in seconds it would take for the robot to be driving at full speed.
	 * The prevValue must be defined after the throttle is input into the drive() method so 
	 * it records the previous throttle.
	 */
	public static double accelLimit(double fullSpeedTime, double throttle, double prevValue) { // Limits acceleration of throttle	
		double finalThrottle;
    	
    	double throttleDiff = throttle - prevValue;
    	double accelerationLimit = 0.02 / fullSpeedTime; //Sets accelerationLimit to the proper double to make the robot reach its top speed in the given full speed time.    	
    
    	if (throttleDiff > accelerationLimit) {
    		finalThrottle = prevValue + accelerationLimit; // throttle can only increase by a maximum of the previous throttle value plus the acceleration limit.
    	} else if (throttleDiff < -accelerationLimit) {
    		finalThrottle = prevValue - accelerationLimit; // throttle can only decrease by a minimum of the previous throttle value plus the acceleration limit.
    	} else {
    		finalThrottle = throttle;
    	}
    	
    	return finalThrottle;
	}
	
	/*
	 * Puts a speed limit on throttle.
	 * This could incorporate the limit method later but we don't want to make any drastic changes right now.
	 */
	public static double speedLimit (double throttle, double speedLimitFactor) { // Puts a speed limit on the throttle entered.

		if (throttle > Math.abs(speedLimitFactor)) {
			
			throttle = Math.abs(speedLimitFactor);
		} else if (throttle < -Math.abs(speedLimitFactor)) {
			
			throttle = -Math.abs(speedLimitFactor);
		}
		
		return throttle;
	}
}