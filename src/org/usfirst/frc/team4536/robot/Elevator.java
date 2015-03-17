package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {
	
	Talon elevatorTalon;
	DigitalInput topLimitSwitch;
	DigitalInput middleLimitSwitch;
	DigitalInput bottomLimitSwitch;
	Encoder elevatorEncoder;
	
	double currentHeight;
	double correction;
	double prevElevatorThrottle;
	double desiredHeight;
	double elevatorSpeedLimited;
	
	/*
     * This function is the constructor for the Elevator class
     * It takes in four arguments - one talon channel, and the channels of the top, bottom, and middle limit switches
     */
	public Elevator(int talonChannel, 
					int encoderAChannel,
					int encoderBChannel,
					int topLimitSwitchChannel, 
					int middleLimitSwitchChannel,
					int bottomLimitSwitchChannel) 
	{
		elevatorTalon = new Talon(talonChannel);
		elevatorEncoder = new Encoder(encoderAChannel, encoderBChannel);
		topLimitSwitch = new DigitalInput(topLimitSwitchChannel);
		middleLimitSwitch = new DigitalInput(middleLimitSwitchChannel);
		bottomLimitSwitch = new DigitalInput(bottomLimitSwitchChannel);
		
		currentHeight = 0;
		correction = 0;
		prevElevatorThrottle = 0;
		desiredHeight = 0;
		elevatorSpeedLimited = 1;
	}

	
	/*
     * This function is called in order to make the elevator drive
     * It takes in one arguments - the amount of vertical throttle (1 to -1)
     * To go up the value would be 1. To go down the value would be -1
     */
	public void drive(double verticalThrottle) {
		double elevatorTalonThrottle = -verticalThrottle;
		
		if(topLimitSwitchValue() == true && verticalThrottle > 0) {
			elevatorTalon.set(0);
		}
		else if((bottomLimitSwitchValue() == true || !middleLimitSwitch.get() == true) && verticalThrottle < 0) {
			elevatorTalon.set(0);
		}
		else 
			elevatorTalon.set(Utilities.limit(elevatorTalonThrottle));
	}
 	
 	
	/*
	 * Returns the boolean value of the top limit switch
	 * A returned value of true indicates that the limit switch is pressed
	 */
	public boolean topLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !topLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the middle limit switch
	 * A returned value of true indicates that the limit switch is pressed
	 */
	public boolean middleLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !middleLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the bottom limit switch
	 * A returned value of true indicates that the limit switch is pressed
	 */
	public boolean bottomLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !bottomLimitSwitch.get();
	}
	
	/*
	 *  Returns the double throttle value of the elevator
	 *  Positive return value means the elevator is going up
	 */
	public double getThrottle() {
		return elevatorTalon.get();
	}
	
	public void goToDesiredHeight(double teleopElevatorSpeedLimit){ // this method goes to the height set by desiredHeight
		double elevatorThrottle;
		
		elevatorThrottle = Utilities.limit((desiredHeight - currentHeight)*Constants.ELEVATOR_PROPORTIONALITY_CONSTANT);
		elevatorThrottle = Utilities.speedLimit(elevatorThrottle, teleopElevatorSpeedLimit);
		elevatorThrottle = Utilities.accelLimit(Constants.ELEVATOR_FULL_SPEED_TIME, elevatorThrottle, prevElevatorThrottle);
		
		this.drive(elevatorThrottle);
		
		prevElevatorThrottle = elevatorThrottle;
	}
	
	public void setDesiredHeight (double teleopDesiredHeight){ //Sets the height that the goToHeight() method goes to
		desiredHeight = teleopDesiredHeight;
	}
	
	
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
	
	public void update(){
		currentHeight = correction + elevatorEncoder.get()/Constants.TICKS_PER_INCHES;
		//System.out.println("Current Elevator Height: " + currentHeight);
		
		if (this.bottomLimitSwitchValue()) {
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
		System.out.println("Encoder Value: " + elevatorEncoder.getDistance());
	}
	
	public double getDesiredHeight(){
		return desiredHeight;
	}
}
