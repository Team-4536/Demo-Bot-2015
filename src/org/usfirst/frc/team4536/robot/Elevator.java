package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {
	
	Talon elevatorTalon;
	DigitalInput topLimitSwitch;
	DigitalInput bottomLimitSwitch;
	
	boolean topLimitSwitchValue;
	boolean bottomLimitSwitchValue;
	
	public Elevator(int talonChannel, int topLimitSwitchChannel, int bottomLimitSwitchChannel) {
		elevatorTalon = new Talon(talonChannel);
		topLimitSwitch = new DigitalInput(topLimitSwitchChannel);
		bottomLimitSwitch = new DigitalInput(bottomLimitSwitchChannel);
	}
	
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
}
