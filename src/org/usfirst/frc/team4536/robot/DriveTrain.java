
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;
//import java.lang.Math;

public class DriveTrain {
	Talon leftTalon;
	Talon rightTalon;
	Gyro gyroSensor;
	
	double prevTurnThrottle;
		
	/*
     * This function is the constructor for the DriveTrain class
     * It takes in two arguments - t
     * .he two PWM channels for the Talons.
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
		Utilities.limit(leftTalonThrottle);
		Utilities.limit(rightTalonThrottle);
			
		// These two lines set the values for the Talons based on the two throttle values
		leftTalon.set(leftTalonThrottle);
		rightTalon.set(rightTalonThrottle);
	}
	
	public void driveStraight(double forwardThrottle, double angle, double fullSpeedTime) {
		
		double angleDiff = angle - gyroSensor.getAngle();
		double turnThrottle = angleDiff * Constants.PROPORTIONALITY_CONSTANT;
		double turnThrottleAccel = Utilities.accelLimit(fullSpeedTime, turnThrottle, prevTurnThrottle);
		
		this.drive(forwardThrottle, turnThrottleAccel);
		prevTurnThrottle = turnThrottleAccel;
		
	}
	
	public void turnTo(double desiredAngle, double fullSpeedTime) {
		double angle;
		double angleDiff;
		
		angle = gyroSensor.getAngle();
		
		while (angle > desiredAngle + 180) {
			angle -= 360;
		}
			
		while (angle < desiredAngle - 180) {
			angle += 360;
		}

		angleDiff = desiredAngle - angle;
		
		double turnThrottle = angleDiff * Constants.PROPORTIONALITY_CONSTANT;
		double turnThrottleAccel = Utilities.accelLimit(fullSpeedTime, turnThrottle, prevTurnThrottle);
		
		this.drive(0, turnThrottleAccel);
		prevTurnThrottle = turnThrottleAccel;
		
		
		}	
}
