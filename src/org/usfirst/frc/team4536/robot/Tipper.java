package org.usfirst.frc.team4536.robot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Tipper { // This is a counter-weight for our robots stacks and also allows our robot to tip for picking up recycling containers.
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	Timer extendedTimer; // A timer for measuring the time that the Tipper has been extended.
	Timer retractedTimer; // A timer for measuring the time that the Tipper has been retracted.
	
	public Tipper(int rightSolenoidChannel, int leftSolenoidChannel) {
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		extendedTimer = new Timer();
		retractedTimer = new Timer();
		
		extendedTimer.start();
		retractedTimer.start();
		
		// Tipper is initialized as retracted
				this.retract();	//rightSolenoid is initialized as true, leftSolenoid is initialized as false.
	}
	
	public boolean isExtended() {
		
		return rightSolenoid.get(); // true = extended, false = retracted. This code works because the values must be opposite of each other and only come in 2 combinations.
	}
	
	public boolean isRetracted() {
		
		return leftSolenoid.get(); // true = retracted, false = extended. This code works because the values must be opposite of each other and only come in 2 combinations.
	}
	
	public void extend() { // Sets the solenoid states to extend the Tipper.
		
		rightSolenoid.set(true);
		leftSolenoid.set(false);
	}
	
	public void retract() { // Sets the solenoid states to retract the Tipper.
		
		rightSolenoid.set(false);
		leftSolenoid.set(true);	
	}
	
	public void flip() { // Flips the solenoid states of the Tipper. Example: goes from retracted to extended (or) extended to retracted.
		
		rightSolenoid.set(!rightSolenoid.get());
		leftSolenoid.set(!leftSolenoid.get());	
	}
	
	public double timeExtended() { // Returns the time the Tipper has been extended.
		if (this.isExtended()) { // If Tipper is extended.
			return extendedTimer.get(); // Returns the time the Tipper has been extended.
		} else {
			extendedTimer.reset(); // Constantly resets the extendedTimer to zero each cycle of code when not extended. This is so the timer counts up from zero once it becomes extended.
			return 0; // If not extended it returns a time of zero.
		}
	}
	
	public double timeRetracted() {
		if (this.isRetracted()) { // If Tipper is retracted.
			return retractedTimer.get();
		} else {
			retractedTimer.reset(); // Constantly resets the retractedTimer to zero each cycle of code when not retracted. This is so the timer counts up from zero once it becomes retracted.
			return 0; //If not retracted it returns a time of zero.
		}
	}
	

}