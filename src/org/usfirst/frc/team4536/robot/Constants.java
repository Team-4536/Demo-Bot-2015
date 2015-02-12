package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	
	// PWM ports
	public static final int LEFT_TALON_CHANNEL = 1;
	public static final int RIGHT_TALON_CHANNEL = 0; 
	public static final int ELEVATOR_MOTOR_CHANNEL = 2;
	
	// Platform solenoid channels
	public static final int RIGHT_PLATFORM_SOLENOID_CHANNEL = 2;
	public static final int LEFT_PLATFORM_SOLENOID_CHANNEL = 3;
	public static final int RIGHT_TIPPER_SOLENOID_CHANNEL = 1;
	public static final int LEFT_TIPPER_SOLENOID_CHANNEL = 0;
	
	// Digital Switch channels
	public static final int TOP_LIMIT_SWITCH_CHANNEL = 3;
	public static final int MIDDLE_LIMIT_SWITCH_CHANNEL = 1;
	public static final int BOTTOM_LIMIT_SWITCH_CHANNEL = 4;
	public static final int ENCODER_SENSOR_A_CHANNEL = 6;
	public static final int ENCODER_SENSOR_B_CHANNEL = 5;
	
	// Analog Sensor channel
	public static final int GYRO_SENSOR_CHANNEL = 0;
	
	// This is a conversion between the encoder ticks and inches
	public static final double TICKS_PER_INCHES = 247.7;
	
	// Proportionality Constant for PID loop
	public static final double PROPORTIONALITY_CONSTANT = 0.05; 
	public static final double ELEVATOR_PROPORTIONALITY_CONSTANT = .5; //Units of throttle/inch
	public static final double DERIVATIVE_CONSTANT = 0.015;
	
	// Dead zone constant. Currently, a 13% dead zone on the joysticks. 
	public static final double DEAD_ZONE = 0.13;
	
	// This constant controls the exponent for speed curve used on the joysticks. 
	public static final double FORWARD_SPEED_CURVE = 1.5;
	public static final double TURN_SPEED_CURVE = 1.5;
	public static final double ELEVATOR_SPEED_CURVE = 2;
	
	// Full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double ELEVATOR_FULL_SPEED_TIME = 0.25;
	public static final double FORWARD_FULL_SPEED_TIME = 0.25;
	public static final double TURN_FULL_SPEED_TIME = 0.25;
	
	// Normal Speed Limit
	public static final double FORWARD_SPEED_LIMIT = 1;
	public static final double TURN_SPEED_LIMIT = 1;
	
	// Slow mode speed limit
	public static final double SLOW_FORAWRD_SPEED_LIMIT = 1;
	public static final double SLOW_TURN_SPEED_LIMIT = 0.6;
	
	// These constants control the exponent for speed curves used on the joysticks. 
	public static final double SLOW_FORWARD_SPEED_CURVE = 1.5;
	public static final double SLOW_TURN_SPEED_CURVE = 1.5;
	
	// Slow mode full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double SLOW_FORWARD_FULL_SPEED_TIME = 0.5;
	public static final double SLOW_TURN_FULL_SPEED_TIME = 1.7;
	
	// Auto full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double AUTO_FORWARD_FULL_SPEED_TIME = 3;
	public static final double AUTO_TURN_FULL_SPEED_TIME = 0.2;
	
	// Elevator Height Constants
	public static final double 	ELVATOR_HEIGHT_FOR_STEP = 20;
	public static final double 	ELVATOR_HEIGHT_FOR_SCORING_PLATFORM = 5;
	public static final double 	ELVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION = 10;
	public static final double 	ELVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION = 22;
	public static final double 	ELVATOR_HEIGHT_FOR_PICKING_OFF_THE_GROUND = 0;
	public static final double 	ELVATOR_HEIGHT_FOR_ONE_TOTE = 12;
	public static final double  BOTTOM_LIMIT_SWITCH_HEIGHT = -1;
	public static final double  MIDDLE_LIMIT_SWITCH_HEIGHT = 8;
	public static final double  TOP_LIMIT_SWITCH_HEIGHT = 50;
	
}
