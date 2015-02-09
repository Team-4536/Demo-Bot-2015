package org.usfirst.frc.team4536.robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Solenoid;

public class Platform {
	
	Solenoid rightSolenoid;
	Solenoid leftSolenoid;
	Timer platformExtendedTimer;
	
	public Platform(int rightSolenoidChannel, int leftSolenoidChannel) {	
		rightSolenoid = new Solenoid(rightSolenoidChannel);
		leftSolenoid = new Solenoid(leftSolenoidChannel);
		platformExtendedTimer = new Timer();
		
		platformExtendedTimer.start();
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
	
	public double platformTimeSinceExtended() {
		if (this.isExtended() == true) {
			return platformExtendedTimer.get();
		} else {
			platformExtendedTimer.reset();
			return 0;
		}
	}

}
