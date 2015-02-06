package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Solenoid;

/*
*Suggested improvement: There needs to be comments in this class! Caleb
*/

public class Platform {
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	
	//An initial value should be given to the solenoids in this constructor. Otherwise calling isExtended() could throw an error. Caleb
	public Platform(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		
		// Platform is initial retracted
		// Suggestion: Use this.retract() instead. Saves you a line of code and is easier to read! Caleb
		rightSolenoid.set(false);
		leftSolenoid.set(true);	
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

}
