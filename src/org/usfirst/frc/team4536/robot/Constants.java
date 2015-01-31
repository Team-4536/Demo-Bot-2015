package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	
	// Talon ports
	public static final int LEFT_TALON_CHANNEL = 0;
	public static final int RIGHT_TALON_CHANNEL = 5; //supposed to be 1 this is a test
	
	// Limit Switch channels
	public static final int TOP_LIMIT_SWITCH_CHANNEL = 3;
	public static final int BOTTOM_LIMIT_SWITCH_CHANNEL = 4;
	
	public static final int HALL_EFFECT_SENSOR_CHANNEL = 0;
	
	// Platform solenoid channels
	public static final int RIGHT_PLATFORM_SOLENOID_CHANNEL = 1;
	public static final int LEFT_PLATFORM_SOLENOID_CHANNEL = 0;
	
	// Elevator motor channel
	public static final int ELEVATOR_MOTOR_CHANNEL = 1; //supposed to be 2 this is a test
	
	// Dead zone constant. Currently, a 12% dead zone on the joysticks.
	public static final double DEAD_ZONE = 0.12;
	
	// This constant controls the exponent for speed curve used on the joysticks.
	public static final double SPEED_CURVE = 2;
}
