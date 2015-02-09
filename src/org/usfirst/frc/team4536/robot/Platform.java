package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/*
*Suggested improvement: There needs to be comments in this class! Caleb
*/

public class Platform {
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	Timer extendedTimer;
	
	//An initial value should be given to the solenoids in this constructor. Otherwise calling isExtended() could throw an error. Caleb
	public Platform(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		extendedTimer = new Timer();
		
		extendedTimer.start();
		
		// Platform is initial retracted
		this.retract();	
	}	
	
	public boolean isExtended() {
		
		return rightSolenoid.get(); // true = extended, false = retracted. This code works because the values must be opposite of each other and only come in 2 combinations.x
	}
	
	public void extend() {
		
		rightSolenoid.set(true);
		leftSolenoid.set(false);
	}
	
	public void retract() {
		
		rightSolenoid.set(false);
		leftSolenoid.set(true);	
	}
	
	public void flip() {
		
		rightSolenoid.set(!rightSolenoid.get());
		leftSolenoid.set(!leftSolenoid.get());	
	}

	public double timeSinceExtended() {
		if (this.isExtended() == true) {
			return extendedTimer.get();
		} else {
			extendedTimer.reset();
			return 0;
		}
	}
}
