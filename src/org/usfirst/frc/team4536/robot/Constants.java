package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	
	// Drive Talon ports
	public static final int LEFT_TALON_CHANNEL = 1;
	public static final int RIGHT_TALON_CHANNEL = 0; 
	
	// Elevator Talon ports
	public static final int ELEVATOR_MOTOR_CHANNEL = 2;
	
	// Platform solenoid channels
	public static final int RIGHT_PLATFORM_SOLENOID_CHANNEL = 2;
	public static final int LEFT_PLATFORM_SOLENOID_CHANNEL = 3;
	public static final int RIGHT_TIPPER_SOLENOID_CHANNEL = 1;
	public static final int LEFT_TIPPER_SOLENOID_CHANNEL = 0;
	
	// Limit Switch channels
	public static final int TOP_LIMIT_SWITCH_CHANNEL = 3;
	public static final int MIDDLE_LIMIT_SWITCH_CHANNEL = 5;
	public static final int BOTTOM_LIMIT_SWITCH_CHANNEL = 4;
	
	// Gyro Sensor channel
	public static final int GYRO_SENSOR_CHANNEL = 0;
	
	//Proportionality Constant for PID loop
	public static final double PROPORTIONALITY_CONSTANT = 0.005; 
	
	// Dead zone constant. Currently, a 10% dead zone on the joysticks. 
	public static final double DEAD_ZONE = 0.10;
	
	// This constant controls the exponent for speed curve used on the joysticks. 
	public static final double FORWARD_SPEED_CURVE = 1.5;
	public static final double TURN_SPEED_CURVE = 1.5;
	public static final double ELEVATOR_SPEED_CURVE = 2;
	
	// Full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double ELEVATOR_FULL_SPEED_TIME = 0.5;
	public static final double FORWARD_FULL_SPEED_TIME = 0.25;
	public static final double TURN_FULL_SPEED_TIME = 0.25;
	
	// Slow mode speed limit
	public static final double SLOW_SPEED_LIMIT = 0.5;
	
	// These constants control the exponent for speed curves used on the joysticks. 
	public static final double SLOW_FORWARD_SPEED_CURVE = 2;
	public static final double SLOW_TURN_SPEED_CURVE = 2;
	
	// Slow mode full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double SLOW_FORWARD_FULL_SPEED_TIME = 0.5;
	public static final double SLOW_TURN_FULL_SPEED_TIME = 0.5;
	
	// Auto full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double AUTO_FORWARD_FULL_SPEED_TIME = 0.5;
	public static final double AUTO_TURN_FULL_SPEED_TIME = 0.5;
	
}
