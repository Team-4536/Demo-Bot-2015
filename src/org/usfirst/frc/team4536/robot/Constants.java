package org.usfirst.frc.team4536.robot;

public class Constants {
	
	// Joystick ports
	public static final int LEFT_STICK_PORT = 0;
	public static final int RIGHT_STICK_PORT = 1;
	public static final int TOWER_STICK_PORT = 2;
	
	// PWM ports
	public static final int LEFT_TALON_CHANNEL = 1;
	public static final int RIGHT_TALON_CHANNEL = 0; 
	public static final int ELEVATOR_MOTOR_CHANNEL = 2;
	public static final int TOWER_MOTOR_CHANNEL = 3;
	
	// Platform solenoid channels
	public static final int RIGHT_PLATFORM_SOLENOID_CHANNEL = 2;
	public static final int LEFT_PLATFORM_SOLENOID_CHANNEL = 3;
	public static final int RIGHT_TIPPER_SOLENOID_CHANNEL = 1;
	public static final int LEFT_TIPPER_SOLENOID_CHANNEL = 0;
	
	// Digital Switch channels
	public static final int TOP_LIMIT_SWITCH_CHANNEL = 3;
	public static final int MIDDLE_LIMIT_SWITCH_CHANNEL = 1;
	public static final int BOTTOM_LIMIT_SWITCH_CHANNEL = 4;
	public static final int TOTE_LIMIT_SWITCH_CHANNEL = 2;
	public static final int ENCODER_SENSOR_A_CHANNEL = 5;
	public static final int ENCODER_SENSOR_B_CHANNEL = 6;
	
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
	public static final double SLOW_ELEVATOR_FULL_SPEED_TIME = 1;
	public static final double TOWER_FULL_SPEED_TIME_SPEEDING_UP = 3;
	public static final double TOWER_FULL_SPEED_TIME_STOPPING = 2;
	public static final double TOWER_FULL_SPEED_TIME_TELEOP = 2;
	
	//Tower constants
	public static final double TOWER_AUTO_SPEED = 1;
	public static final double TOWER_AUTO_TIME = 3;
	
	// Normal Speed Limit
	public static final double FORWARD_SPEED_LIMIT = 1;
	public static final double TURN_SPEED_LIMIT = 1;
	
	// Slow mode speed limit
	public static final double SLOW_FORAWRD_SPEED_LIMIT = 1;
	public static final double SLOW_TURN_SPEED_LIMIT = 0.6;
	
	// Super Slow Mode Speed Limit
	public static final double SUPER_SLOW_FORWARD_SPEED_LIMIT = 0.5;
	public static final double SUPER_SLOW_TURN_SPEED_LIMIT = 0.3;
	
	// These constants control the exponent for speed curves used on the joysticks. 
	public static final double SLOW_FORWARD_SPEED_CURVE = 1.5;
	public static final double SLOW_TURN_SPEED_CURVE = 1.5;
	
	// Super Slow Mode Speed Curve
	public static final double SUPER_SLOW_FORWARD_SPEED_CURVE = 1.5;
	public static final double SUPER_SLOW_TURN_SPEED_CURVE = 1.5;
	
	// Slow mode full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double SLOW_FORWARD_FULL_SPEED_TIME = 1;
	public static final double SLOW_TURN_FULL_SPEED_TIME = 1.7;
	
	//Super Slow Mode
	public static final double SUPER_SLOW_FORWARD_FULL_SPEED_TIME = 2;
	public static final double SUPER_SLOW_TURN_FULL_SPEED_TIME = 3.4;
	
	// Auto full speed times (time it takes for something get to full speed during an acceleration limit)
	// Measured in seconds
	public static final double AUTO_FORWARD_FULL_SPEED_TIME = 3;
	public static final double AUTO_TURN_FULL_SPEED_TIME = 0.2;
	
	// Elevator Height Constants
	public static final double ELEVATOR_HEIGHT_FOR_STEP = 20; //Inches
	public static final double ELEVATOR_HEIGHT_FOR_SCORING_PLATFORM = 5; //Inches
	public static final double ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION = 10; //Inches
	public static final double ELEVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION = 32; //Inches
	public static final double ELEVATOR_HEIGHT_FOR_PICKING_OFF_THE_GROUND = 0; //Inches
	public static final double ELEVATOR_HEIGHT_FOR_ONE_TOTE = 12; //Inches
	public static final double ELEVATOR_HEIGHT_FOR_RECYCLING_CONTAINER_PICKING_OFF_THE_GROUND = 14.5; //Inches
	public static final double BOTTOM_LIMIT_SWITCH_HEIGHT = -0.5; //Inches
	public static final double MIDDLE_LIMIT_SWITCH_HEIGHT = 8; //Inches
	public static final double TOP_LIMIT_SWITCH_HEIGHT = 48; //Inches
	
	//Buttons for Joysticks
		//mainStick
			//Slow Modes
			public static final int SLOW_MODE = 6; //Robot enters slow mode with larger acceleration limits and lower speed limits.
			public static final int SUPER_SLOW_MODE = 7; //Robot enters an even 
			
			//Automation Buttons
			public static final int AUTOMATIC_STACK_SET_DOWN_AND_DRIVE_BACK = 1; // Button for automatically setting a stack and backing up.
			public static final int RECYCLING_CONTAINER_PICK_UP = 3; //Button for picking up recycling conatiner
			
			//Toggles
			public static final int TIPPER_TOGGLE = 2; //Flips solenoids of tipper, extending or retracting.
		
		//secondaryStick
			//Platform Toggle
			public static final int PLATFORM_TOGGLE = 5; //Flips the solenoids of the platform to operate it on a toggle.
		
			//Elevator Speed
			public static final int ELEVATOR_SPEED = 9; // When pressed it halves the speed of the elevator.
			
			//Elevator Manual Override
			public static final int ELEVATOR_MANUAL_OVERRIDE = 6; // When pressed, allows manual override by secondary drive of elevator.w3
		
			//Elevator Heights	
			public static final int SCORING_PLATFORM_HEIGHT = 4; //moves elevator to the height required for the scoring platform.
			public static final int RECYCLING_CONTAINER_GROUND_PICKUP_HEIGHT = 7; //moves the elevator to the height required for recycling container ground pickup.
			public static final int STEP_HEIGHT = 8; //moves the elevator to the height required for the step.
			public static final int FEEDER_STATION_BOTTOM_HEIGHT = 10; //moves the elevator to the height of the bottom of the feeder station.
			public static final int TOTE_ABOVE_FEEDER_STATION_HEIGHT = 11; //moves the elevator to the height it would need to suspend a tote above so another tote may be loaded from the feeder station.
			public static final int AUTOMATED_TOTE_STACKING = 1; // Automatically stacks totes based on whether the limit switch is pressed while button 1 is held down.
			public static final int LOWER_ONE_TOTE_LEVEL = 2; // Automatically lower the elevator one tote level.
			public static final int RAISE_ONE_TOTE_LEVEL = 3; // Automatically raise the elevator one tote level.
	
}
