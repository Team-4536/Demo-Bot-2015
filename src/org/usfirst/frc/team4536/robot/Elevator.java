package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {
	
	Talon elevatorTalon;
	DigitalInput topLimitSwitch;
	DigitalInput middleLimitSwitch;
	DigitalInput bottomLimitSwitch;
	DigitalInput toteLimitSwitch;
	Encoder elevatorEncoder;
	
	double currentHeight;
	double correction;
	double prevElevatorThrottle;
	double desiredHeight;
	double elevatorSpeedLimit;
	
	/*
     * This function is the constructor for the Elevator class
     * It takes in four arguments - one talon channel, and the channels of the top, bottom, and middle limit switches
     */
	public Elevator(int talonChannel, 
					int encoderAChannel,
					int encoderBChannel,
					int topLimitSwitchChannel, 
					int middleLimitSwitchChannel,
					int bottomLimitSwitchChannel,
					int toteLimitSwitchChannel) 
	{
		elevatorTalon = new Talon(talonChannel);
		elevatorEncoder = new Encoder(encoderAChannel, encoderBChannel);
		topLimitSwitch = new DigitalInput(topLimitSwitchChannel);
		middleLimitSwitch = new DigitalInput(middleLimitSwitchChannel);
		bottomLimitSwitch = new DigitalInput(bottomLimitSwitchChannel);
		toteLimitSwitch = new DigitalInput(toteLimitSwitchChannel);
		
		currentHeight = 0;
		correction = 0;
		prevElevatorThrottle = 0;
		desiredHeight = 0;
		elevatorSpeedLimit = 1;
	}

	
	/*
     * This function is called in order to make the elevator drive
     * It takes in one arguments - the amount of vertical throttle (1 to -1)
     * To go up the value would be 1. To go down the value would be -1
     */
	public void drive(double verticalThrottle) {
		double elevatorTalonThrottle = -verticalThrottle;
		
		if(!topLimitSwitch.get() == true && verticalThrottle > 0) {
			elevatorTalon.set(0);
		}
		else if((!bottomLimitSwitch.get() == true || !middleLimitSwitch.get() == true) && verticalThrottle < 0) {
			elevatorTalon.set(0);
		}
		else 
			elevatorTalon.set(Utilities.limit(elevatorTalonThrottle));
	}
 	
 	
	/*
	 * Returns the boolean value of the top limit switch
	 * A return value of true indicates that the limit switch is pressed
	 */
	public boolean topLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !topLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the middle limit switch
	 * A return value of true indicates that the limit switch is pressed
	 */
	public boolean middleLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !middleLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the bottom limit switch
	 * A return value of true indicates that the limit switch is pressed
	 */
	public boolean bottomLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !bottomLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the tote limit switch
	 * A return value of true indicates that the limit switch is pressed
	 */
	public boolean toteLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !toteLimitSwitch.get();
	}
	
	/*
	 * Returns the double throttle value of the elevator
	 * Positive return value means the elevator is going up
	 */
	public double getThrottle() {
		return elevatorTalon.get();
	}
	
	/*
	 * This method takes in the speed you want the Elevator to go 
	 * The speed limit should be between 0 and 1. 0 is not moving and 1 is full speed.
	 * It makes the elevator go to the height most recently set by the setElevatorHeight method
	 */
	public void goToDesiredHeight(double teleopElevatorSpeedLimit){
		double elevatorThrottle;
		elevatorSpeedLimit = teleopElevatorSpeedLimit;
		elevatorThrottle = Utilities.limit((desiredHeight - currentHeight)*Constants.ELEVATOR_PROPORTIONALITY_CONSTANT);
		elevatorThrottle = Utilities.accelLimit(Constants.ELEVATOR_FULL_SPEED_TIME, elevatorThrottle, prevElevatorThrottle);
		
		this.drive(elevatorThrottle*elevatorSpeedLimit);
		
		prevElevatorThrottle = elevatorThrottle;
	}
	
	/*
	 * This method takes the height you want the Elevator to go to 
	 * The height is based off of the height from the bottom of the tote to the ground
	 * 0 is when the tote is on the ground and -0.5 is the lowest our elevator can 
	 * go because of the limit switch
	 * It sends this value to the goToDesiredHeight method to actually drive to that height
	 * It's done this way so when you push a button it goes to the height instead of having to 
	 * hold the button to get to said height
	 */
	public void setDesiredHeight (double teleopDesiredHeight){
		desiredHeight = teleopDesiredHeight;
	}
	
	/*
	 * The currentHeight is not the encoder value. It should be the height in real life in inches
	 * It's affected by ticks to inches conversion and the correction from the dynamic calibration
	 */
	public double getHeight(){
		return currentHeight;
	}
	
	/*
	 * This method exists because the elevator won't always start at the very bottom position
	 * however the encoder will always begin counting from 0.
	 * This is the difference between the height of the elevator and the value from the encoder.
	 */
	public void setActualHeight(double actualHeight){
		correction = actualHeight - elevatorEncoder.get()/Constants.TICKS_PER_INCHES;
	}
	
	/*
	 * This method is the dynamic calibration of the elevatorHeight
	 * It sets the currentHeight to the actual height when it hits one of the limit switches
	 */
	public void update(){
		currentHeight = correction + elevatorEncoder.get()/Constants.TICKS_PER_INCHES;
		
		if (this.bottomLimitSwitchValue() == true) {
			setActualHeight(Constants.BOTTOM_LIMIT_SWITCH_HEIGHT);
		}
		
		if (this.middleLimitSwitchValue()) {
			setActualHeight(Constants.MIDDLE_LIMIT_SWITCH_HEIGHT);
		}
			
		if (this.topLimitSwitchValue()) {
			setActualHeight(Constants.TOP_LIMIT_SWITCH_HEIGHT);
		}
	}
	
	public void resetEncoder(){
		elevatorEncoder.reset();
	}
	
	public void printEncoderValue() {
		//System.out.println("Raw " + elevatorEncoder.getRaw());
		//System.out.println("Rate " + elevatorEncoder.getRate());
		System.out.println(elevatorEncoder.getDistance());
	}
	public double getDesiredHeight(){
		return desiredHeight;
	}
}
