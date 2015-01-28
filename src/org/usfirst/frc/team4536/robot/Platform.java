package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Solenoid;

public class Platform {
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	
	
	public Platform(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
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
