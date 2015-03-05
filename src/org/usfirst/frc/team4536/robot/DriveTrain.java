package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;

public class DriveTrain {
	Talon leftTalon;
	Talon rightTalon;
	Gyro gyroSensor;
	
	double prevTurnThrottle;
	double prevForwardThrottle;
	double angleChangeRate;
		
	/*
     * This function is the constructor for the DriveTrain class
     * It takes in two arguments - the two PWM channels for the Talons.
     */
	public DriveTrain(int leftTalonChannel, int rightTalonChannel, int gyroChannel) {
		leftTalon = new Talon(leftTalonChannel);
		rightTalon = new Talon(rightTalonChannel);
		gyroSensor = new Gyro(gyroChannel);
	}
	
	public void startGyro() {
		gyroSensor.initGyro(); // starts the gyro and sets the heading of the gyro to zero.
	}
	
	public void resetGyro() {
		gyroSensor.reset(); // sets the current gyro heading as 0 degrees.
	}
	
	public double gyroGetAngle() {
		return gyroSensor.getAngle(); // Returns as a double the angle of the gyro. Note: It has no set bounds so it may return angles greater than 360 and angles less than -360.
	}
	
	public double gyroGetRate() {
		return gyroSensor.getRate(); // Returns the rate of change of the angle (derivative!) as a double.
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
	public void driveStraight(double forwardThrottle, double desiredAngle, double fullSpeedTime) {
		
		double angleDiff = desiredAngle - gyroSensor.getAngle(); // Finds the difference between the angle its going to and its current angle.
		double turnThrottle = angleDiff * Constants.PROPORTIONALITY_CONSTANT; // based on how far off the desired angle the robot is
		double turnThrottleAccel = Utilities.accelLimit(fullSpeedTime, turnThrottle, prevTurnThrottle); //Puts an acceleration limit on the turn throttle.
		
		double forwardThrottleAccel = Utilities.accelLimit(fullSpeedTime, forwardThrottle, prevForwardThrottle); //Puts an acceleration limit on the forward movement.
		
		this.drive(forwardThrottleAccel, turnThrottleAccel); // Drives
		prevTurnThrottle = turnThrottleAccel; // Defines the previous value that controlled the turning of the drive train.
		prevForwardThrottle = forwardThrottleAccel; // Defines the previous value that controlled the forward driving of the drive train.
	}
	
	/*
	 * This method can be used to turn to a specific angle while having the robot not move forward.
	 * It takes in the angle that is desired and the time that it would take the motors to get to full speed.
	 * It has an acceleration limit, which we use to draw less current and save battery life.
	 */
	public void turnTo(double desiredAngle, double fullSpeedTime) {
		double angle;
		double angleDiff;
		double derivative; // rate of change of angle
		
		angle = gyroSensor.getAngle();
		
		while (angle > desiredAngle + 180) { // Makes the angle within terms of being within 180 degrees of our desired value so the drive train will turn in the most efficient way.
			angle -= 360;
		}
			
		while (angle < desiredAngle - 180) { // Makes the angle within terms of being within 180 degrees of our desired value so the drive train will turn in the most efficient way.
			angle += 360;
		}

		angleDiff = desiredAngle - angle;
		
		/* 
		 * If the driveTrain is off by more than 10 degrees of its desired angle
		 * it uses the derivative constant and proportionality constant to correct.
		 * If it is within 10 degrees it uses only the proportionality constant.
		 */
		if (angleDiff > 10 || angleDiff < -10) { // if the drive train is off by more than 10 degrees of its desired angle 
			derivative = this.gyroGetRate() * Constants.DERIVATIVE_CONSTANT;
		} else {
			derivative = 0;
		}
		
		/*
		 * adjusts the difference in angle by a constant so it can become
		 * a reasonable turn throttle value to reach the desired angle. Uses
		 * the rate of change of the angle as well to slow down adjustment
		 */
		double turnThrottle = angleDiff * Constants.PROPORTIONALITY_CONSTANT - derivative;
		double turnThrottleAccel = Utilities.accelLimit(fullSpeedTime, turnThrottle, prevTurnThrottle);
		
		this.drive(0, turnThrottleAccel);
		prevTurnThrottle = turnThrottleAccel;	
	}		
	
}