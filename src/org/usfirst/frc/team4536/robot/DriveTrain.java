
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;
//import java.lang.Math;

public class DriveTrain {
	Talon leftTalon;
	Talon rightTalon;
	Gyro gyroSensor;
		
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
	
	public void driveStraight(double forwardThrottle, double angle) {
		
		double angleDiff = angle - gyroSensor.getAngle();
		double adjustment = angleDiff * Constants.PROPORTIONALITY_CONSTANT;
		
		this.drive(forwardThrottle, adjustment);		
		
	}
	
	public void turnTo(double desiredAngle) {
		double turnDirectionMagnitude = desiredAngle - gyroSensor.getAngle();
		
		while (desiredAngle > gyroSensor.getAngle() + 180) {
			turnDirectionMagnitude = gyroSensor.getAngle() + 360;
		}
		
		while (desiredAngle < gyroSensor.getAngle() - 180) {
			turnDirectionMagnitude = gyroSensor.getAngle() - 360;
		}
		/*double gyroRawAngle;
		double angleDiff;
		
		//Angle Adjustment
		angle %= 360;
		
		if (angle > 180) {
			angle = angle - 360;
		} 
		else if (angle < -180) {
			angle = angle + 360;
		}
		
		//Gyro adjustment
		if ((gyroSensor.getAngle() > 180)) {
			gyroRawAngle = angle - 360;
		} 
		else if ((gyroSensor.getAngle() < -180)) {
			gyroRawAngle = angle + 360;
		} else {
			gyroRawAngle = gyroSensor.getAngle();
		}
		
		if (Math.abs(angle - gyroRawAngle) > 180) {
			if (angle > 0) {
				angleDiff = gyroRawAngle - (angle - 180);
			} else {
				angleDiff = angle - (gyroRawAngle - 180); b
			}
			
		} else {
			if (angle > gyroRawAngle) {
				angleDiff = angle - gyroRawAngle;
			} else {
				angleDiff = gyroRawAngle - angle;
			}
		}
		
		double gyroCycleDiff = (360 - gyroRawAngle);
		double gyroAngle = gyroRawAngle;
		
		double angleCycleDiff = (360 - angle);*/
		
		//double angleDiff = angle - ((gyroSensor.getAngle() % 360)- 180);
		//double cycleDiff = (360 - ((gyroSensor.getAngle() % 360)) - 180) - (360 - angle);
		double adjustment = turnDirectionMagnitude * Constants.PROPORTIONALITY_CONSTANT;
		
		this.drive(0, adjustment);
		
		}	
}
