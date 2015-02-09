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
	
	//Proportionality Constant for PID loop
	public static final double PROPORTIONALITY_CONSTANT = 0.005; 
	
	// Dead zone constant. Currently, a 12% dead zone on the joysticks.
	public static final double DEAD_ZONE = 0.05;
	
	// This constant controls the exponent for speed curve used on the joysticks.
	public static final double FOWARD_SPEED_CURVE = 2;
	public static final double TURN_SPEED_CURVE = 1;
	
	//Accel Limit Constants 
		//(Teleop)
		public static final double FORWARD_FULL_SPEED_TIME = 0.5; //Time it woud take to reach full speed. Put in Utilities.accelLimit()
		public static final double TURN_FULL_SPEED_TIME = 0.5; 
		//(Auto)
		public static final double AUTO_FORWARD_FULL_SPEED_TIME = 10; //Used in auto with turnTo() method
		public static final double AUTO_TURN_FULL_SPEED_TIME = 30; //Used in auto with driveStraight() method
		//(Teleop - Slow)
		public static final double SLOW_FORWARD_FULL_SPEED_TIME = 40; //Time it woud take to reach full speed. Put in Utilities.accelLimit()
		public static final double SLOW_TURN_FULL_SPEED_TIME = 20;
	
	//Hold Button Method() Constants
	public static final double HOLD_BUTTON_TIME = 1;
	
	//Slow Mode Constants (teleop)
	public static final double SLOW_FORWARD_SPEED_LIMIT = 0.5;
	public static final double SLOW_TURN_SPEED_LIMIT = 0.5;
	public static final double SLOW_FORWARD_SPEED_CURVE = 1;
	public static final double SLOW_TURN_SPEED_CURVE = 1;
	
}
