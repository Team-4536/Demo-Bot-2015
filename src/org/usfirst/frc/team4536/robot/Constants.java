package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	
	// Talon ports
	public static final int LEFT_TALON_CHANNEL = 0;
	public static final int RIGHT_TALON_CHANNEL = 1;
	
	// Dead zone constant. Currently, a 12% dead zone on the joysticks.
	public static final double DEAD_ZONE = 0.05;
	
	// This constant controls the exponent for speed curve used on the joysticks.
	public static final double SPEED_CURVE = 2;
	
	//Teleop Constants 
	
	public static final double FORWARD_FULL_SPEED_TIME = 10;
	public static final double TURN_FULL_SPEED_TIME = 1;
}
