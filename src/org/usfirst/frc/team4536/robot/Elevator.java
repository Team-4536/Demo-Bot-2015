package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {
	
	Talon elevatorTalon;
	DigitalInput topLimitSwitch;
	DigitalInput middleLimitSwitch;
	DigitalInput bottomLimitSwitch;
	
	// Why do these need to be declared up here? If they do not need to store values between cycles of code, then you may as well declare them inside of a function to reduce clutter up here. Caleb
	boolean topLimitSwitchValue;
	boolean middleLimitSwitchValue;
	boolean bottomLimitSwitchValue;
	
	/*
     * This function is the constructor for the Elevator class
     * It takes in three arguments - one talon channel, and the channels of the top and bottom limit switches
     * These comments need to be updated. Caleb
     */
	public Elevator(int talonChannel, 
					int topLimitSwitchChannel, 
					int middleLimitSwitchChannel,
					int bottomLimitSwitchChannel) 
	{
		elevatorTalon = new Talon(talonChannel);
		topLimitSwitch = new DigitalInput(topLimitSwitchChannel);
		middleLimitSwitch = new DigitalInput(middleLimitSwitchChannel);
		bottomLimitSwitch = new DigitalInput(bottomLimitSwitchChannel);
	}
	
	/*
     * This function is called in order to make the elevator drive
     * It takes in one arguments - the amount of vertical throttle (-1 to 1)
     * Which way is which? Does verticalThrottle=1 mean drive up or drive down? Caleb
     */
	public void driveFullRange(double verticalThrottle) {
		double elevatorTalonThrottle = verticalThrottle;
		
		// Makes sure the elevator talon throttle is between -1 and 1
		Utilities.limit(elevatorTalonThrottle);
		
		// Boolean values are reversed because limit switches output false when not pressed
		topLimitSwitchValue = !topLimitSwitch.get();
		bottomLimitSwitchValue = !bottomLimitSwitch.get();
		
		if(topLimitSwitchValue == true && elevatorTalonThrottle > 0) {
			// If the top limit switch is engaged, and the elevator is going up, set it as 0
			elevatorTalon.set(0);
		}
		else if(bottomLimitSwitchValue == true && elevatorTalonThrottle < 0) {
			// If the bottom limit switch is engaged, and the elevator motor is going down, set it as 0
			elevatorTalon.set(0);
		}
		else {
			// If neither limit switch is engaged, the elevator motor can go both up and down
			elevatorTalon.set(elevatorTalonThrottle);
		}
	}
	
	public void driveSmallRange(double verticalThrottle) {
		double elevatorTalonThrottle = verticalThrottle;
		
		// Makes sure the elevator talon throttle is between -1 and 1
		Utilities.limit(elevatorTalonThrottle);
		
		// Boolean values are reversed because limit switches output false when not pressed
		topLimitSwitchValue = !topLimitSwitch.get();
		middleLimitSwitchValue = !middleLimitSwitch.get();
		
		if(topLimitSwitchValue == true && elevatorTalonThrottle > 0) {
			// If the top limit switch is engaged, and the elevator is going up, set it as 0
			elevatorTalon.set(0);
		}
		else if(middleLimitSwitchValue == true && elevatorTalonThrottle < 0) {
			// If the bottom limit switch is engaged, and the elevator motor is going down, set it as 0
			elevatorTalon.set(0);
		}
		else {
			// If neither limit switch is engaged, the elevator motor can go both up and down
			elevatorTalon.set(elevatorTalonThrottle);
		}
	}
 	
	/*
	 * Returns the boolean value of the top limit switch
	 */
	public boolean topLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !topLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the middle limit switch
	 */
	public boolean middleLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !middleLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the bottom limit switch
	 */
	public boolean bottomLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !bottomLimitSwitch.get();
	}
	
	/*
	 *  Returns the double throttle value of the elevator
	 *  Which way is which? Does a positive return value mean the elevator is driving up or down? 
	 */
	public double getThrottle() {
		return elevatorTalon.get();
	}
}
