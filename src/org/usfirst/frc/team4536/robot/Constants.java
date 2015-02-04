package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	
	// Talon ports
	public static final int LEFT_TALON_CHANNEL = 0;
	public static final int RIGHT_TALON_CHANNEL = 1; 
	
	// Limit Switch channels
	public static final int TOP_LIMIT_SWITCH_CHANNEL = 3;
	public static final int MIDDLE_LIMIT_SWITCH_CHANNEL = 5;
	public static final int BOTTOM_LIMIT_SWITCH_CHANNEL = 4;
	
	public static final int HALL_EFFECT_SENSOR_CHANNEL = 0;
	
	// Platform solenoid channels
	public static final int RIGHT_PLATFORM_SOLENOID_CHANNEL = 1;
	public static final int LEFT_PLATFORM_SOLENOID_CHANNEL = 0;
	
	// Elevator motor channel
	// Suggestion: put this constant up by the Talons. Caleb
	public static final int ELEVATOR_MOTOR_CHANNEL = 2;
	
	// Dead zone constant. Currently, a 12% dead zone on the joysticks. Usually at 0.12
	public static final double DEAD_ZONE = 0.12;
	
	// This constant controls the exponent for speed curve used on the joysticks. 
	public static final double SPEED_CURVE = 2;
	
	// Full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double ELEVATOR_FULL_SPEED_TIME = 0.5;
}
