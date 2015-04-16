package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

/*
 * The Can Burgler is an attached robot to the minutebots 2015 robot
 * Zenith. The Can Burgler picks up cans quickly off of the step in the 
 * FRC 2015 game "Recylce Rush" and is an attempt to be picked by top
 * teams in eliminations os that
 */

public class Burgler {
	
	Talon burglerTalon;
	Timer deployedTimer;
	Timer retractedTimer;
	
	private static int talonCount = 0;
	private static int nextTalon = 0;
	
	/*  This is a default constructor which allows you to make
	 *  a burglerTalon right after a previous one. Default value
	 *  if called initially is 0. May be used after the other Burgler
	 *  constructor and it will make a burglerTalon one pwm channel
	 *  higher than the previously constructed Burgler.
	 */
	public Burgler() {
		
		burglerTalon = new Talon(nextTalon);
		
		burglerTalon.set(0.0);
		
		talonCount++;
		nextTalon++;
	}
	
	/*
	 * This is a constructor which takes in integer parameters
	 * to determine the pwm channel of the talon which will
	 * operate the Can Burgler.
	 */
	public Burgler(int talonChannel) {
		
		burglerTalon = new Talon(talonChannel);
		
		burglerTalon.set(0.0);
		
		talonCount++;
		nextTalon = talonChannel + 1;
	}
	
	public void deploy() {
		
		burglerTalon.set(Constants.DEPLOYMENT_SPEED); // @ param double
	}
	
	public void deploy(double speed){
		
		burglerTalon.set(speed);
	}
	
	public void retract() {
		
		burglerTalon.set(-Constants.RETRACTING_SPEED); // @ param double
	}
	
	public void retract(double speed) {
		
		burglerTalon.set(-speed);
	}
	
	public String toString() {
		
		return ("" + burglerTalon.get());
	}
	
	public double getSpeed() {
		
		return burglerTalon.get();
	}
	
	public String nextTalon() {
		
		return ("" + nextTalon);
	}
	
	public static int numBurglers() {
		
		return talonCount;
	}
}
