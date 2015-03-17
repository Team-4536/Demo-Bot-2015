package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;

public class Tower {
	
	Talon towerTalon;
	double accelThrottle;
	double prevThrottle;

	public Tower(int towerChannel) { //Constructor for the tower
		
		towerTalon = new Talon(towerChannel);
		
	}
	
	public void setSpeed(double fullSpeedTime, double throttle) { //Sets the towers speed
		
		accelThrottle = throttle;
		accelThrottle = Utilities.accelLimit(fullSpeedTime, accelThrottle, prevThrottle);
		towerTalon.set(accelThrottle);
		
		prevThrottle = accelThrottle;
	}

}
