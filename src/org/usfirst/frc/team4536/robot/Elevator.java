package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {
	
	Talon elevatorTalon;
	DigitalInput topLimitSwitch;
	DigitalInput bottomLimitSwitch;
	
	boolean topLimitSwitchValue;
	boolean bottomLimitSwitchValue;
	
	/*
     * This function is the constructor for the Elevator class
     * It takes in three arguments - one talon channel, and the channels of the top and bottom limit switches
     */
	public Elevator(int talonChannel, int topLimitSwitchChannel, int bottomLimitSwitchChannel) {
		elevatorTalon = new Talon(talonChannel);
		topLimitSwitch = new DigitalInput(topLimitSwitchChannel);
		bottomLimitSwitch = new DigitalInput(bottomLimitSwitchChannel);
	}
	
	/*
     * This function is called in order to make the elevator drive
     * It takes in one arguments - the amount of vertical throttle (-1 to 1)
     */
	public void drive(double verticalThrottle) {
		double elevatorTalonThrottle = -verticalThrottle;
		
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
	
	/*
	 * Returns the boolean value of the top limit switch
	 */
	public boolean getTopLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !topLimitSwitch.get();
	}
	
	/*
	 * Returns the boolean value of the bottom limit switch
	 */
	public boolean getBottomLimitSwitchValue() {
		// Boolean value is reversed because the limit switch outputs false when not pressed
		return !bottomLimitSwitch.get();
	}
	
	/*
	 *  Returns the double throttle value of the elevator
	 */
	public double getThrottle() {
		return elevatorTalon.get();
	}
}
