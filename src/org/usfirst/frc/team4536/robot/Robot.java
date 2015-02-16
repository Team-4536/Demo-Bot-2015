package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
	// Robot Systems
	DriveTrain driveTrain;
	Platform platform;
	Tipper tipper;
	Elevator elevator;
	Timer teleopTimer;
	Timer autoTimer;
	Auto auto;
	int autoNumber;
	Compressor compressor;
	
	// Joysticks
	Joystick mainStick;
	Joystick secondaryStick;
	
	// Previous values used for toggles for the platform and tipper 
	boolean prevPlatformControllingButton;
	boolean prevTipperControllingButton;
	boolean prevAutoSet;
	
	// This value is necessary for our acceleration limit on the elevator
	double prevElevatorThrottle;
	
	double prevThrottleY = 0;
	double prevThrottleX = 0;
	double finalThrottleY = 0;
	double finalThrottleX = 0;
	
	public void robotInit() {
		// Robot Systems
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL);
    	driveTrain.startGyro();
    	platform = new Platform(Constants.RIGHT_PLATFORM_SOLENOID_CHANNEL, Constants.LEFT_PLATFORM_SOLENOID_CHANNEL);
    	platform.retract();
    	tipper = new Tipper(Constants.RIGHT_TIPPER_SOLENOID_CHANNEL, Constants.LEFT_TIPPER_SOLENOID_CHANNEL);
    	tipper.retract();    	
    	elevator = new Elevator(Constants.ELEVATOR_MOTOR_CHANNEL, 
    							Constants.ENCODER_SENSOR_A_CHANNEL,
    							Constants.ENCODER_SENSOR_B_CHANNEL,
    							Constants.TOP_LIMIT_SWITCH_CHANNEL, 
    							Constants.MIDDLE_LIMIT_SWITCH_CHANNEL,
    							Constants.BOTTOM_LIMIT_SWITCH_CHANNEL);
    	elevator.setActualHeight(0);
    	teleopTimer = new Timer();
    	teleopTimer.start();
    	
    	// Joysticks
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	
    	compressor = new Compressor();
    	
    	// Previous values used for toggles for the platform and tipper  
    	prevPlatformControllingButton = false;
    	prevTipperControllingButton = false;
    	
    	// This value is necessary for our acceleration limit on the elevator
    	prevElevatorThrottle = 0;
    	
    	autoTimer = new Timer();
    	auto = new Auto(driveTrain, elevator);
    	//This gets the stuff on the SmartDashboard, it's like a dummy variable. Ask and I shall explain more
		autoNumber = (int) auto.autoNumber();
		
		autoTimer.start();
    }
	
	public void autonomousInit() {
		compressor.start();
		autoTimer.reset();
	}

	public void autonomousPeriodic() {
		//driveTrain.turnTo(90, Constants.AUTO_TURN_FULL_SPEED_TIME);
		driveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		//System.out.println(driveTrain.gyroGetAngle());
		
		double autoTime = autoTimer.get();
		
		//Can't have autoNumber since that would be the value when the code's deployed
		switch ((int) auto.autoNumber()){
			case 1: auto.driveForward(autoTime);
					break;
			case 2: auto.driveBackwardWithRecyclingContainer(autoTime);
					break;
			case 3: auto.driveForwardPushingTote(autoTime);
					break;
			case 4: auto.twoTote(autoTime);
					break;
			case 5: auto.twoRecyclingContainers(autoTime);
					break;
			case 6: auto.doNothing();
					break;
			default: auto.doNothing();
					 break;
		
		}
    }
	
	public void teleopInit() {
		compressor.start();
		teleopTimer.reset();
	}
	
	public void teleopPeriodic() {
		
		// Retracted Timer
		double retractedTime = platform.timeRetracted();
		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(-mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(-mainStick.getX(), Constants.DEAD_ZONE);
    	
    	//driveTrain.driveStraight(-0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
    	//System.out.println(driveTrain.gyroGetAngle());
    	
    	// If button 4 on the main stick is pressed slow mode is enabled
    	/*if(mainStick.getRawButton(4) == true) {
    		// Multiplying by the speed limit puts a speed limit on the forward and turn throttles
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.SLOW_FORWARD_SPEED_CURVE) * Constants.SLOW_FORAWRD_SPEED_LIMIT;
    		double throttleX = Utilities.speedCurve(-mainStickX, Constants.SLOW_TURN_SPEED_CURVE) * Constants.SLOW_TURN_SPEED_LIMIT;
    		
    		finalThrottleY = Utilities.accelLimit(Constants.SLOW_FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
    		finalThrottleX = Utilities.accelLimit(Constants.SLOW_TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    	}
    	else {
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.FORWARD_SPEED_CURVE) * Constants.FORWARD_SPEED_LIMIT;
    		double throttleX = Utilities.speedCurve(-mainStickX, Constants.TURN_SPEED_CURVE) * Constants.TURN_FULL_SPEED_TIME;
    		
    		finalThrottleY = Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
    		finalThrottleX = Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    	}
    	
    	driveTrain.drive(finalThrottleY, finalThrottleX);*/
    	
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
        
        //if(platform.isExtended() != true) {
        //	elevator.driveFullRange(elevatorThrottle);
        //}
        //else {
        //	elevator.driveSmallRange(elevatorThrottle);
        //}
        
        elevator.update();
        
        //Automation of setting tote stack then backing up
        if (mainStick.getRawButton(Constants.AUTOMATED_STACK_SET) == true){
        	
        	if (mainStick.getRawButton(Constants.AUTOMATED_STACK_SET) == true && prevAutoSet == false) {
        		driveTrain.resetGyro();
        	}
        	
        	platform.retract();
        	
        	if (retractedTime > 6) {
        		elevator.setDesiredHeight(-30);
  
        		if (elevator.bottomLimitSwitchValue() == true || (elevator.getHeight() < 0 && elevator.getHeight() > -1)) {
        			driveTrain.driveStraight(-0.3, 0, Constants.SLOW_TURN_FULL_SPEED_TIME);
        		} 
        	}
        }	//if button 4 is pressed, slow mode is enabled
        else if (mainStick.getRawButton(4) == true) {
    		// Multiplying by the speed limit puts a speed limit on the forward and turn throttles
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.SLOW_FORWARD_SPEED_CURVE) * Constants.SLOW_FORAWRD_SPEED_LIMIT;
    		double throttleX = Utilities.speedCurve(-mainStickX, Constants.SLOW_TURN_SPEED_CURVE) * Constants.SLOW_TURN_SPEED_LIMIT;
    		
    		finalThrottleY = Utilities.accelLimit(Constants.SLOW_FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
    		finalThrottleX = Utilities.accelLimit(Constants.SLOW_TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
    		driveTrain.drive(finalThrottleY, finalThrottleX);
    		
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    	}
    	else {
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.FORWARD_SPEED_CURVE) * Constants.FORWARD_SPEED_LIMIT;
    		double throttleX = Utilities.speedCurve(-mainStickX, Constants.TURN_SPEED_CURVE) * Constants.TURN_SPEED_LIMIT;
    		
    		finalThrottleY = Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
    		finalThrottleX = Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
    		driveTrain.drive(finalThrottleY, finalThrottleX);
    		
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    		
    	}
    	
        prevAutoSet = mainStick.getRawButton(Constants.AUTOMATED_STACK_SET);
        	
        	System.out.println("Elevator Height:" + elevator.getHeight());
        
               if (teleopTimer.get() > 133){
        	//tipper.extend();
        	//platform.retract();
        	//elevator.setHeight(0);
               }
            if (secondaryStick.getRawButton(6) == true){
            	elevator.drive(elevatorThrottle);
            	elevator.setDesiredHeight(elevator.getHeight());
            } else {
            	elevator.goToDesiredHeight(1); // (+) up, (-) down.	
            }
	}
	
	public void disabledInit() {
		System.out.println("DISABLED");
		compressor.stop();
		teleopTimer.reset();
	}
	
	public void disabledPeriodic() {
		driveTrain.gyroSensor.reset();
	}
    
    public void testPeriodic() {
    		
    }
    
}