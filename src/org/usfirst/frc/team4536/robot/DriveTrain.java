
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;

public class DriveTrain {
	Talon leftTalon;
	Talon rightTalon;
	Gyro gyroSensor;
		
	/*
     * This function is the constructor for the DriveTrain class
     * It takes in two arguments - the two PWM channels for the Talons.
     */
	public DriveTrain(int leftTalonChannel, int rightTalonChannel) {
		leftTalon = new Talon(leftTalonChannel);
		rightTalon = new Talon(rightTalonChannel);
		gyroSensor = new Gyro(Constants.GYRO_SENSOR_CHANNEL);
	}
	
	public void startGyro() {
		gyroSensor.initGyro();
	}
	
	public void resetGyro() {
		gyroSensor.reset();
	}
	
	public double gyroGetAngle() {
		double angle = gyroSensor.getAngle();
		return angle;
	}
	
	/*
     * This function is called in order to make the robot drive
     * It takes in two arguments - the amount of forward throttle (-1 to 1)
     * and the amount of turn throttle (-1 to 1). 
     */
	public void drive(double forwardThrottle, double turnThrottle) {
		double leftTalonThrottle = forwardThrottle + turnThrottle;
		double rightTalonThrottle = -forwardThrottle + turnThrottle;
				
	    // Makes sure that the two Talon throttles are between -1 and 1
		rightTalonThrottle = Utilities.limit(leftTalonThrottle);
		leftTalonThrottle = Utilities.limit(rightTalonThrottle);
			
		// These two lines set the values for the Talons based on the two throttle values
		leftTalon.set(leftTalonThrottle);
		rightTalon.set(rightTalonThrottle);
	}
	
	/*
	 * This method can be used to make the drive train drive straight at a certain angle. 
	 * It only works if the angle that is inputed is the current angle of the robot.
	 */
	public void driveStraight(double forwardThrottle, double angle) {
		double angleDiff = angle - gyroSensor.getAngle();
		double adjustment = angleDiff * Constants.PROPORTIONALITY_CONSTANT;
		
		this.drive(forwardThrottle, adjustment);				
	}
	
}
