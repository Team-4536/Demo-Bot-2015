
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;

public class DriveTrain {
	Talon leftTalon;
	Talon rightTalon;
		
	/**
     * This function is the constructor for the DriveTrain class
     * It takes in two arguments - the two PWM channels for the Talons.
     */
	public DriveTrain(int leftTalonChannel, int rightTalonChannel) {
		leftTalon = new Talon(leftTalonChannel);
		rightTalon = new Talon(rightTalonChannel);
	}
	
	
	/**
     * This function is called in order to make the robot drive
     * It takes in two arguments - the amount of forward throttle (-1 to 1)
     * and the amount of turn throttle (-1 to 1). 
     */
	public void drive(double forwardThrottle, double turnThrottle) {
		double leftTalonThrottle = forwardThrottle + turnThrottle;
		double rightTalonThrottle = -forwardThrottle + turnThrottle;
		
		
		/**
	     * Set of if statements to ensure values going to the Talons are between -1 and 1.
	     */
		if(leftTalonThrottle > 1) {
			leftTalonThrottle = 1;
		}
		if(leftTalonThrottle < -1) {
			leftTalonThrottle = -1;
		}
		if(rightTalonThrottle > 1) {
			rightTalonThrottle = 1;
		}
		if(rightTalonThrottle < -1) {
			rightTalonThrottle = 1;
		}
		
		leftTalon.set(leftTalonThrottle);
		rightTalon.set(rightTalonThrottle);
	}
}
