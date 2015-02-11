package org.usfirst.frc.team4536.robot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Tipper {
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	Timer extendedTimer;
	
	public Tipper(int rightSolenoidChannel, int leftSolenoidChannel) {
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		extendedTimer = new Timer();
		
		extendedTimer.start();
	}
	
public boolean isExtended() {
		
		return rightSolenoid.get(); // true = extended, false = retracted. This code works because the values must be opposite of each other and only come in 2 combinations.
	}
	
	public void extend() {
		
		rightSolenoid.set(true);
		leftSolenoid.set(false);
	}
	
	public void retract() {
		
		rightSolenoid.set(false);
		leftSolenoid.set(true);	
	}
	
	public void flip() { //Switches state between extend and retract
		
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