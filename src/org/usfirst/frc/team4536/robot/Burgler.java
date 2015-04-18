package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

/*
*Suggested improvement: There needs to be comments in this class! Caleb
*/

public class Burgler {
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	Timer extendedTimer;
	Timer retractedTimer;
	
	//An initial value should be given to the solenoids in this constructor. Otherwise calling isExtended() could throw an error. Caleb
	public Burgler(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		extendedTimer = new Timer();
		retractedTimer = new Timer();
		
		extendedTimer.start();
		retractedTimer.start();
		
		// Platform is initial retracted
		this.extend();	
	}	
	
	public boolean isReleasingRecyclingContainer() {
		
		return rightSolenoid.get(); // true = extended, false = retracted. This code works because the values must be opposite of each other and only come in 2 combinations.x
	}
	
	public boolean isGrabbingRecyclingContainer() {
		
		return leftSolenoid.get();
	}
	
	// extending the burgler means retracting the actual piston
	public void extend() {
		
		rightSolenoid.set(true);
		leftSolenoid.set(false);
	}
	
	// retracting the burgler means extending the actual piston
	public void retract() {
		
		rightSolenoid.set(false);
		leftSolenoid.set(true);	
	}
	
	public void grabRecyclingContainer() {
		
		this.retract();
	}
	
	public void releaseRecyclingContainer() {
		
		this.extend();
	}
	
	public void flip() {
		
		rightSolenoid.set(!rightSolenoid.get());
		leftSolenoid.set(!leftSolenoid.get());	
	}

	public double timeExtended() {
		if (this.isReleasingRecyclingContainer() == true) { // is Extended
			return extendedTimer.get();
		} else {
			extendedTimer.reset();
			return 0;
		}
	}
	
	public double timeRetracted() {
		if (this.isGrabbingRecyclingContainer()) { // is Retracted.
			return retractedTimer.get();
		} else {
			retractedTimer.reset();
			return 0;
		}
	}
}
