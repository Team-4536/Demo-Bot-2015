package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Gyro;

public class DriveTrain {
	Talon leftTalon;
	Talon rightTalon;
	Gyro gyroSensor;
	DigitalInput toteLimitSwitch;
	
	double prevTurnThrottle;
	double prevForwardThrottle;
	double angleChangeRate;
	double currentAngle;
	double angleCorrection;
	
	boolean prevToteLimitSwitchValue;
		
	/*
     * This function is the constructor for the DriveTrain class
     * It takes in four arguments - the two PWM channels for the Talons, the gyro analog channel and the digital input of the tote limit switch.
     */
	public DriveTrain(int leftTalonChannel, int rightTalonChannel, int gyroChannel, int toteLimitSwitchChannel) {		
		leftTalon = new Talon(leftTalonChannel);
		rightTalon = new Talon(rightTalonChannel);
		gyroSensor = new Gyro(gyroChannel);
		toteLimitSwitch = new DigitalInput(toteLimitSwitchChannel);
	}
	
	/*
	 * starts the gyro and sets the heading of the gyro to zero.
	 */
	public void startGyro() {
		gyroSensor.initGyro();
	}
	
	/*
	 * sets the current gyro heading as 0 degrees.
	 */
	public void resetGyro() {
		gyroSensor.reset();
	}
	
	/*
	 * Returns as a double the angle of the gyro.
	 * Note: It has no set bounds so it may return 
	 * angles greater than 360 and angles less than -360.
	 */
	public double gyroGetAngle() {
		return currentAngle;
	}
	
	/*
	 * Returns the rate of change of the angle
	 * (derivative!) as a double.
	 */
	public double gyroGetRate() {
		return gyroSensor.getRate();
	}
	
	/*
	 * Returns the boolean value of the tote limit switch
	 * A returned value of true indicates that the limit switch is pressed
	 */
	public boolean toteLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !toteLimitSwitch.get();
	}
	
	/*
	 * Defines the current angle of the gyro taking in the
	 * correction defined by the setActualAngle() method.
	 * Takes in the argument of the boolean value of a
	 * calibration button so the current angle can be 
	 * adjusted by using the setActualAngle() method when
	 * the calibration button is pressed.
	 */
	public void updateAngle (boolean calibrationButton) {
		
		currentAngle = angleCorrection + this.gyroGetAngle();
		
		if (this.toteLimitSwitchValue() && !prevToteLimitSwitchValue) {
			setActualAngle(Constants.FEEDER_STATION_ANGLE); // Angle that is required to line up with the feeder station.
		} else if (calibrationButton) {
			this.setActualAngle(Constants.FORWARD_HEADING); // angle where robot is perpendicular to the alliance wall, perpendicular to the step or parallel to the side railings.
		}
		
		prevToteLimitSwitchValue = this.toteLimitSwitchValue();
		
	}
	
	/*
	 * When you know the angle of the robot when something
	 * happens you can set the angle of the robot to calibrate
	 * the gyro. It just takes the argument of the actual angle
	 * of the robot.
	 */
	public void setActualAngle (double actualAngle) {
		
		angleCorrection = actualAngle - this.gyroGetAngle();
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
	 * It specializes in mainting angle more than reaching an angle but is otherwise the same
	 * as the turnTo() method.
	 */
	public void driveStraight(double forwardThrottle, double desiredAngle, double fullSpeedTime) {
		
		double angleDiff = desiredAngle - currentAngle; // Finds the difference between the angle its going to and its current angle.
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
		
		angle = currentAngle;
		
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
		prevTurnThrottle = turnThrottleAccel; // defines the previous throttle value.	
	}		
	
}