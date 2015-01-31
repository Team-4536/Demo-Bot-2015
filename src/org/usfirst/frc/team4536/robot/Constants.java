package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	
	// Talon ports
	public static final int LEFT_TALON_CHANNEL = 0;
	public static final int RIGHT_TALON_CHANNEL = 1;
	
	// Sensor Ports
	public static final int HALL_EFFECT_SENSOR_CHANNEL = 0;
	public static final int GYRO_SENSOR_CHANNEL = 0;
	
	//Proportionality Constant
	public static final double PROPORTIONALITY_CONSTANT = 0.013;
	
	// Dead zone constant. Currently, a 12% dead zone on the joysticks.
	public static final double DEAD_ZONE = 0.05;
	
	// This constant controls the exponent for speed curve used on the joysticks.
	public static final double SPEED_CURVE = 2;
	
	//Teleop Constants 
	
	public static final double FORWARD_FULL_SPEED_TIME = 10;
	public static final double TURN_FULL_SPEED_TIME = 5;
	public static final double HOLD_BUTTON_TIME = 1;
	
	//Slow Mode Constants
	
	public static final double SLOW_FORWARD_FULL_SPEED_TIME = 40;
	public static final double SLOW_TURN_FULL_SPEED_TIME = 20;
	public static final double SLOW_SPEED_LIMIT = 0.5;
	public static final double SLOW_SPEED_CURVE = 1;
	
}
