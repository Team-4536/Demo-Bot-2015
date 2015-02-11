
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;

public class DriveTrain {
	Talon leftTalon;
	Talon rightTalon;
	Gyro gyroSensor;
	
	double prevTurnThrottle;
	double angleChangeRate;
		
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
		return gyroSensor.getAngle(); //Returns degrees
	}
	
	public double gyroGetRate() {
		return gyroSensor.getRate(); // Returns units of degrees per second.
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
		rightTalonThrottle = Utilities.limit(rightTalonThrottle);
		leftTalonThrottle = Utilities.limit(leftTalonThrottle);
			
		// These two lines set the values for the Talons based on the two throttle values
		leftTalon.set(leftTalonThrottle);
		rightTalon.set(rightTalonThrottle);
	}
	
	/*
	 * This method can be used to make the drive train drive straight at a certain angle. 
	 * It only works if the angle that is inputed is the current angle of the robot.
	 */
	public void driveStraight(double forwardThrottle, double angle, double fullSpeedTime) {
		
		double angleDiff = angle - gyroSensor.getAngle();
		double turnThrottle = angleDiff * Constants.PROPORTIONALITY_CONSTANT;
		double turnThrottleAccel = Utilities.accelLimit(fullSpeedTime, turnThrottle, prevTurnThrottle);
		
		this.drive(forwardThrottle, turnThrottleAccel);
		prevTurnThrottle = turnThrottleAccel;		
	}

	/*
	 * This method can be used to turn to a specific angle
	 * It takes in the angle that is desired and the time that it would take the motors to get to full speed
	 * This time creates an acceleration limit, which we use to draw less current and save battery life.
	 */
	public void turnTo(double desiredAngle, double fullSpeedTime) {
		double angle;
		double angleDiff;
		double derivative;
		
		angle = gyroSensor.getAngle();
		
		while (angle > desiredAngle + 180) {
			angle -= 360;
		}
			
		while (angle < desiredAngle - 180) {
			angle += 360;
		}

		angleDiff = desiredAngle - angle;
		
		if (angleDiff > 10 || angleDiff < -10) {
			derivative = this.gyroGetRate() * Constants.DERIVATIVE_CONSTANT;
		} else {
			derivative = 0;
		}
		
		
		double turnThrottle = angleDiff * Constants.PROPORTIONALITY_CONSTANT - derivative;
		double turnThrottleAccel = Utilities.accelLimit(fullSpeedTime, turnThrottle, prevTurnThrottle);
		
		this.drive(0, turnThrottleAccel);
		prevTurnThrottle = turnThrottleAccel;	
	}		
	
}