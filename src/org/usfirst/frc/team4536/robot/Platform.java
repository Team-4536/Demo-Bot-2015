package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Platform { //This a piston controlled platform to rest stacks on.
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	Timer extendedTimer;
	Timer retractedTimer;
	
	public Platform(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		extendedTimer = new Timer();
		retractedTimer = new Timer();
		
		extendedTimer.start();
		retractedTimer.start();
		
		// Platform is initially retracted
		this.retract();	//rightSolenoid is set true, leftSolenoid is set false.
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
	
	public void flip() { //Flips the solenoid states. Example: goes from retracted to extended (or) extended to retracted.
		
		rightSolenoid.set(!rightSolenoid.get());
		leftSolenoid.set(!leftSolenoid.get());	
	}

	public double timeExtended() {
		if (this.isExtended() == true) { // if is Extended
			return extendedTimer.get();
		} else {
			extendedTimer.reset();
			return 0;
		}
	}
	
	public double timeRetracted() {
		if (this.isExtended() == false) { // if is Retracted.
			return retractedTimer.get();
		} else {
			retractedTimer.reset();
			return 0;
		}
	}
}