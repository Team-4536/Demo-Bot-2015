package org.usfirst.frc.team4536.robot;

public class Utilities {
	
	/*
	 * Takes in values and makes sure they are both above deadZone and below -deadZone
	 * We use it to create a small region on our joysticks that outputs 0
	 * Once the joystick gets outside of the dead zone, it outputs the actual values of the joystick
	 */
	public static double deadZone (double input, double deadZone) {
        double output;
		
		if ((input < deadZone) && (input > -deadZone))      //if the input is within the dead zone, return 0
            output = 0;
        else
            output = input;
		
		return output;
    }
	
	
}
