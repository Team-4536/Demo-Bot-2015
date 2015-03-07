package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Platform { // This a piston controlled platform to rest stacks on.
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	Timer extendedTimer; // A timer for measuring the time that the platform has been extended.
	Timer retractedTimer; // A timer for measuring the time that the platform has been retracted.
	
	public Platform(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		extendedTimer = new Timer();
		retractedTimer = new Timer();
		
		extendedTimer.start();
		retractedTimer.start();
		
		// Platform is initialized as retracted
		this.retract();	//rightSolenoid is initialized as true, leftSolenoid is initialized as false.
	}	
	
	/*
	 * Returns a boolean based on whether the platform is extended.
	 * true = extended, false = retracted. This code works because the
	 * values must be opposite of each other and only come in 2 combinations.
	 */
	public boolean isExtended() {
		
		return rightSolenoid.get();
	}
	
	/*
	 *  Returns a boolean value base on whether the Platform is retracted.
	 *  true = retracted, false = extended. This code works because the
	 *  values must be opposite of each other and only come in 2 combinations.
	 */
	public boolean isRetracted() {
		
		return leftSolenoid.get();
	}
	
	/*
	 * Sets the solenoid states to extend the platform.
	 */
	public void extend() {
		
		rightSolenoid.set(true);
		leftSolenoid.set(false);
	}
	
	/*
	 * Sets the solenoid states to retract the platform.
	 */
	public void retract() {
		
		rightSolenoid.set(false);
		leftSolenoid.set(true);	
	}
	
	/*
	 * Flips the solenoid states.
	 * Example: goes from retracted to extended (or)
	 * extended to retracted.
	 */
	public void flip() {
		
		rightSolenoid.set(!rightSolenoid.get());
		leftSolenoid.set(!leftSolenoid.get());	
	}

	/*
	 * Returns the time the Platform has been extended since
	 * the most recent time the Platform entered the extended state.
	 */
	public double timeExtended() {
		if (this.isExtended()) {
			return extendedTimer.get();
		} else {
			/*
			 * Constantly resets the extendedTimer to zero each cycle
			 * of code when not extended. This is so the timer counts
			 * up from zero once it becomes extended. Returns a time
			 * of zero because it is not extended.
			 */
			extendedTimer.reset();
			return 0;
		}
	}
	
	/*
	 * Returns the time the Platform has been retracted since
	 * the most recent time the Platform entered the retracted state.
	 */
	public double timeRetracted() {
		if (this.isRetracted()) {
			return retractedTimer.get();
		} else {
			/*
			 * Constantly resets the retractedTimer to zero each cycle
			 * of code when not retracted. This is so the timer counts
			 * up from zero once it becomes retracted. Returns a time
			 * of zero because it is not retracted.
			 */
			retractedTimer.reset();
			return 0;
		}
	}
}