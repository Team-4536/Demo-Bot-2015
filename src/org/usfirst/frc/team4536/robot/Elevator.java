package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;

public class Elevator {
	
	Talon elevatorTalon;
	
	public Elevator(int talonChannel) {
		elevatorTalon = new Talon(talonChannel);
	}
	
	public void drive(double verticalThrottle) {
		double elevatorTalonThrottle = verticalThrottle;
		
		//Makes sure the elevator talon throttle is between -1 and 1
		Utilities.limit(elevatorTalonThrottle);
		
		//Sets the elevator talon throttle
		elevatorTalon.set(elevatorTalonThrottle);
	}
}
