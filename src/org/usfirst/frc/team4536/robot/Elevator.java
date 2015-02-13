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
	}

	
	/*
     * This function is called in order to make the elevator drive
     * It takes in one arguments - the amount of vertical throttle (1 to -1)
     * To go up the value would be 1. To go down the value would be -1
     */
	public void drive(double verticalThrottle) {
		double elevatorTalonThrottle = -verticalThrottle;
		
		elevatorTalon.set(Utilities.limit(elevatorTalonThrottle));
		
		if(!topLimitSwitch.get() == true && verticalThrottle > 0) {
			elevatorTalon.set(0);
		}
		else if((!bottomLimitSwitch.get() == true || !middleLimitSwitch.get() == true) && verticalThrottle < 0) {
			elevatorTalon.set(0);
		}
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
	 *  Returns the double throttle value of the elevator
	 *  Positive return value means the elevator is going up
	 */
	public double getThrottle() {
		return elevatorTalon.get();
	}
	
	/*public void goToHeight(double desiredHeight){
		double elevatorThrottle;
		elevatorThrottle = Utilities.limit((desiredHeight - currentHeight)*Constants.ELEVATOR_PROPORTIONALITY_CONSTANT);
		elevatorThrottle = Utilities.accelLimit(Constants.ELEVATOR_FULL_SPEED_TIME, elevatorThrottle, prevElevatorThrottle);
		
		this.drive(elevatorThrottle);
		
		prevElevatorThrottle = elevatorThrottle;
	}*/
	public void goToHeight(){
		double elevatorThrottle;
		elevatorThrottle = Utilities.limit((desiredHeight - currentHeight)*Constants.ELEVATOR_PROPORTIONALITY_CONSTANT);
		elevatorThrottle = Utilities.accelLimit(Constants.ELEVATOR_FULL_SPEED_TIME, elevatorThrottle, prevElevatorThrottle);
		
		this.drive(elevatorThrottle);
		
		prevElevatorThrottle = elevatorThrottle;
	}
	
	public double setDesiredHeight (double teleopDesiredHeight){
		desiredHeight = teleopDesiredHeight;
		return desiredHeight;
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
		System.out.println(currentHeight);
	}
	
	public void resetEncoder(){
		elevatorEncoder.reset();
	}
	
	public void printEncoderValue() {
		//System.out.println("Raw " + elevatorEncoder.getRaw());
		//System.out.println("Rate " + elevatorEncoder.getRate());
		System.out.println(elevatorEncoder.getDistance());
	}
}
