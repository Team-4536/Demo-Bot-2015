package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Solenoid;

/*
*Suggested improvement: There needs to be comments in this class! Caleb
*/

public class Platform {
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	
	
	public Platform(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
	}	
	/*
	*Suggested improvement: change method name from "get" to something more intuitive, such as "isExtended" or "isRetracted." Caleb
	*/
	public boolean get() {
		
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
