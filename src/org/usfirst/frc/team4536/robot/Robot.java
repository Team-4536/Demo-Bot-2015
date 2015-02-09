package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;

public class Robot extends IterativeRobot {
	// Robot Systems
	DriveTrain driveTrain;
	Platform platform;
	Tipper tipper;
	Elevator elevator;
	
	Compressor compressor;
	
	// Joysticks
	Joystick mainStick;
	Joystick secondaryStick;
	
	// Previous values used for toggles for the platform and tipper 
	boolean prevPlatformControllingButton;
	boolean prevTipperControllingButton;
	
	// This value is necessary for our acceleration limit on the elevator
	double prevElevatorThrottle;
	
	public void robotInit() {
		// Robot Systems
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL);
    	platform = new Platform(Constants.RIGHT_PLATFORM_SOLENOID_CHANNEL, Constants.LEFT_PLATFORM_SOLENOID_CHANNEL);
    	platform.retract();
    	tipper = new Tipper(Constants.RIGHT_TIPPER_SOLENOID_CHANNEL, Constants.LEFT_TIPPER_SOLENOID_CHANNEL);
    	tipper.retract();    	
    	elevator = new Elevator(Constants.ELEVATOR_MOTOR_CHANNEL, 
    							Constants.TOP_LIMIT_SWITCH_CHANNEL, 
    							Constants.MIDDLE_LIMIT_SWITCH_CHANNEL,
    							Constants.BOTTOM_LIMIT_SWITCH_CHANNEL);
    	
    	// Joysticks
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	
    	compressor = new Compressor();
    	
    	// Previous values used for toggles for the platform and tipper  
    	prevPlatformControllingButton = false;
    	prevTipperControllingButton = false;
    	
    	// This value is necessary for our acceleration limit on the elevator
    	prevElevatorThrottle = 0;
    }
	
	public void autonomousInit() {
		compressor.start();
	}

	public void autonomousPeriodic() {
		
    }
	
	public void teleopInit() {
		compressor.start();
	}
	
	public void teleopPeriodic() {
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(-mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(-mainStick.getX(), Constants.DEAD_ZONE);
    	
    	// Puts a speed curve on the X and Y values from the mainStick 
    	mainStickY = Utilities.speedCurve(mainStickY, Constants.FORWARD_SPEED_CURVE);
    	mainStickX = Utilities.speedCurve(mainStickX, Constants.TURN_SPEED_CURVE);
    	
    	// Sets throttle values based on mainStick X and Y values (with dead zone and speed curve).
    	double forwardThrottle = mainStickY;
    	double turnThrottle = mainStickX;
    	
    	System.out.println("Forward Throttle: " + forwardThrottle);
    	System.out.println("Turn Throttle: " + turnThrottle);
    	
    	driveTrain.drive(forwardThrottle, turnThrottle);
    	
    	// Uses button 3 on the main stick as a toggle for the platform 
    	if(mainStick.getRawButton(3) == true && prevPlatformControllingButton == false) {
    		platform.flip();
    	}
    	prevPlatformControllingButton = mainStick.getRawButton(3);
    	
    	// Uses button 2 on the main stick as a toggle for the tipper
    	if(mainStick.getRawButton(2) == true && prevTipperControllingButton == false) {
    		tipper.flip();
    	}
    	prevTipperControllingButton = mainStick.getRawButton(2);
    	    	
    	// Gets Y value from secondaryStick and puts a dead zone on it
    	double secondaryStickY = Utilities.deadZone(secondaryStick.getY(), Constants.DEAD_ZONE);
    	
    	// Puts a speed curve on the Y value from the secondaryStick
    	secondaryStickY = Utilities.speedCurve(secondaryStickY, Constants.ELEVATOR_SPEED_CURVE);
    	
    	// Sets the elevator throttle as the secondary stick Y value (with dead zone and speed curve)
        double elevatorThrottle = secondaryStickY;
        
        elevatorThrottle = Utilities.accelLimit(Constants.ELEVATOR_FULL_SPEED_TIME, elevatorThrottle, prevElevatorThrottle);
        prevElevatorThrottle = elevatorThrottle; 
        
        /*
         * If the platform is extended, use the driveSmallRange method, which forgets about the 
         * bottom limit switch and begins sensing the middle limit switch.
         * We don't want our elevator going down too far when the platform is out.
         * Suggestion: switch around driveFullRange and driveSmallRange so that you don't have to deal with false values. Caleb
         */
        if(platform.isExtended() != true) {
        	elevator.driveFullRange(elevatorThrottle);
        }
        else {
        	elevator.driveSmallRange(elevatorThrottle);
        }
    }
	
	public void disabledInit() {
		System.out.println("DISABLED");
	}
	
	public void disabledPeriodic() {
		compressor.stop();
	}
    
    public void testPeriodic() {
    		
    }
    
}
