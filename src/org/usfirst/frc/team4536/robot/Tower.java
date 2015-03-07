package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;

public class Tower {
	
	Talon towerTalon;

	public Tower(int towerChannel) { //Constructor for the tower
		
		towerTalon = new Talon(towerChannel);
		
	}
	
	public void setSpeed(double towerMotorSpeed) { //Sets the towers speed
		
		towerTalon.set(towerMotorSpeed);
		
	}

}
