package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	
	// Talon ports
	public static final int LEFT_TALON_CHANNEL = 0;
	public static final int RIGHT_TALON_CHANNEL = 1;
	
	//Sensor channels
	public static final int HALL_EFFECT_SENSOR_CHANNEL = 0;
	public static final int LIMIT_SWITCH_1_CHANNEL = 3;
	
	// Dead zone constant. Currently, a 12% dead zone on the joysticks.
	public static final double DEAD_ZONE = 0.12;
	
	// This constant controls the exponent for speed curve used on the joysticks.
	public static final double SPEED_CURVE = 2;
}
