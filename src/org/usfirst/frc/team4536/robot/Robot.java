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
	DigitalInput toteLimitSwitch;
	
	Compressor compressor;
	
	// Joysticks
	Joystick mainStick;
	Joystick secondaryStick;
	
	// Previous values used for toggles for the platform, tipper, and automatic stack setting functionality in that order. 
	boolean prevPlatformControllingButton;
	boolean prevTipperControllingButton;
	boolean prevAutoSet;
	
	// This value is necessary for our acceleration limit on the elevator
	double prevElevatorThrottle;
	
	double prevThrottleY = 0;
	double prevThrottleX = 0;
	double finalThrottleY = 0;
	double finalThrottleX = 0;
	double elevatorSpeedLimit = 1;
	
	public void robotInit() {
		// Robot Systems
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL,
    					    		Constants.GYRO_SENSOR_CHANNEL,
    					    		Constants.TOTE_LIMIT_SWITCH_CHANNEL);
    	driveTrain.startGyro();
    	platform = new Platform(Constants.RIGHT_PLATFORM_SOLENOID_CHANNEL, Constants.LEFT_PLATFORM_SOLENOID_CHANNEL); // Also retracts Platform as part of initialization.
    	tipper = new Tipper(Constants.RIGHT_TIPPER_SOLENOID_CHANNEL, Constants.LEFT_TIPPER_SOLENOID_CHANNEL); // Also retracts Tipper as part of initialization.	
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
    	
    	toteLimitSwitch = new DigitalInput(Constants.TOTE_LIMIT_SWITCH_CHANNEL);

    	autoTimer = new Timer();
    	auto = new Auto(driveTrain, elevator);
    	//This gets the stuff on the SmartDashboard, it's like a dummy variable. Ask and I shall explain more
		autoNumber = (int) auto.autoNumber();
    }
	
	public void autonomousInit() {
		compressor.start();
		autoTimer.reset();
		autoTimer.start();
		driveTrain.resetGyro();
	}

	public void autonomousPeriodic() {
		//driveTrain.turnTo(90, Constants.AUTO_TURN_FULL_SPEED_TIME);
		/*driveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		System.out.println(driveTrain.gyroGetAngle());*/		

		double autoTime = autoTimer.get();
		
		//Can't have autoNumber since that would be the value when the code's deployed
		switch ((int) auto.autoNumber()){
			case 1: auto.driveForward(autoTime);
					break;
			case 2: auto.driveBackwardWithRecyclingContainer(autoTime);
					break;
			case 3: auto.driveBackwardWithTote(autoTime);
					break;
			case 4: auto.twoTote(autoTime);
					break;
			case 5: auto.twoRecyclingContainers(autoTime);
					break;
			case 6: auto.doNothing();
					break;
			case 7: auto.threeToteStack(autoTime);
					break;
			case 8: auto.toteAndContainer(autoTime);
					break;
			case 9: auto.extendPlatform();
					break;
			default: auto.doNothing();
					 break;
		}
		elevator.goToDesiredHeight(1);
    }
	
	public void teleopInit() {
		compressor.start();
		teleopTimer.reset();
	}
	
	public void teleopPeriodic() {
		
		//Retracted Timer
		double platformRetractedTime = platform.timeRetracted();
		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(-mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(-mainStick.getX(), Constants.DEAD_ZONE);
    	
    	//driveTrain.driveStraight(-0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
    	//System.out.println(driveTrain.gyroGetAngle());
    	
    	/*
    	 * If the tipper (back piston) is extended, we don't want the driver to have full driving ability
    	 */
    	//Code for automation of recycling container pickup. Tips forward then drives the elevator up to catch on the recycling container.
    	if(mainStick.getRawButton(Constants.RECYCLING_CONTAINER_PICK_UP)) {	
    		if(elevator.getHeight() < Constants.TOP_LIMIT_SWITCH_HEIGHT - 0.5
        	|| elevator.getHeight() > Constants.TOP_LIMIT_SWITCH_HEIGHT + 0.5) {
    			tipper.extend();
    			if(tipper.timeExtended() > 1) {
    				elevator.setDesiredHeight(Constants.TOP_LIMIT_SWITCH_HEIGHT);
    			}
    		}    	
    		else {
    			tipper.retract();
    		}
    	}
    	
    	//Automation of setting tote stack then backing up
        if (mainStick.getRawButton(Constants.AUTOMATIC_STACK_SET_DOWN_AND_DRIVE_BACK) == true){
        	
        	if (mainStick.getRawButton(Constants.AUTOMATIC_STACK_SET_DOWN_AND_DRIVE_BACK) == true && prevAutoSet == false) {
        		driveTrain.resetGyro();
        	}
        	
        	platform.retract();
        	
        	if (platformRetractedTime > 6) {
        		elevator.setDesiredHeight(-30);
  
        		if (elevator.bottomLimitSwitchValue() == true || (elevator.getHeight() < 0 && elevator.getHeight() > -1)) {
        			driveTrain.driveStraight(-0.3, 0, Constants.SLOW_TURN_FULL_SPEED_TIME);
        		} 
        	}
        }//if button 7 is pressed, SUPER SLOW MODE is ENABLED
        else if (mainStick.getRawButton(Constants.SUPER_SLOW_MODE) == true) {
    		// Multiplying by the speed limit puts a speed limit on the forward and turn throttles
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.SUPER_SLOW_FORWARD_SPEED_CURVE) * Constants.SUPER_SLOW_FORWARD_SPEED_LIMIT;
    		double throttleX = Utilities.speedCurve(-mainStickX, Constants.SUPER_SLOW_TURN_SPEED_CURVE) * Constants.SUPER_SLOW_TURN_SPEED_LIMIT;
    		
    		finalThrottleY = Utilities.accelLimit(Constants.SUPER_SLOW_FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
    		finalThrottleX = Utilities.accelLimit(Constants.SUPER_SLOW_TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
    		driveTrain.drive(finalThrottleY, finalThrottleX);
    		
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    	}	//if button 6 is pressed, SLOW MODE is ENABLED
        else if (mainStick.getRawButton(Constants.SLOW_MODE) == true) {
    		// Multiplying by the speed limit puts a speed limit on the forward and turn throttles
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.SLOW_FORWARD_SPEED_CURVE) * Constants.SLOW_FORAWRD_SPEED_LIMIT;
    		double throttleX = Utilities.speedCurve(-mainStickX, Constants.SLOW_TURN_SPEED_CURVE) * Constants.SLOW_TURN_SPEED_LIMIT;
    		
    		finalThrottleY = Utilities.accelLimit(Constants.SLOW_FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
    		finalThrottleX = Utilities.accelLimit(Constants.SLOW_TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
    		driveTrain.drive(finalThrottleY, finalThrottleX);
    		
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    	}
        else if (mainStick.getRawButton(Constants.TURN_TO_FEEDER_STATION)) {
        	driveTrain.turnTo(Constants.FEEDER_STATION_ANGLE, Constants.TURN_FULL_SPEED_TIME);
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
    	
        prevAutoSet = mainStick.getRawButton(Constants.AUTOMATIC_STACK_SET_DOWN_AND_DRIVE_BACK);
    	
    	
    	
    	// Uses button 2 on the main stick as a toggle for the tipper
    	if(mainStick.getRawButton(Constants.TIPPER_TOGGLE) == true && prevTipperControllingButton == false) {
    		tipper.flip();
    	}
    	prevTipperControllingButton = mainStick.getRawButton(Constants.TIPPER_TOGGLE);
    	
    	/*
    	Adjusts input from secondary stick.
    	Puts on a dead zone, speed curve, and acceleration limit.
    	*/
    	// Gets Y value from secondaryStick and puts a dead zone on it
    	double secondaryStickY = Utilities.deadZone(secondaryStick.getY(), Constants.DEAD_ZONE);
    	
    	// Puts a speed curve on the Y value from the secondaryStick
    	secondaryStickY = Utilities.speedCurve(secondaryStickY, Constants.ELEVATOR_SPEED_CURVE);
    	
    	// Sets the elevator throttle as the secondary stick Y value (with dead zone and speed curve)
        double elevatorThrottle = secondaryStickY;
        
        elevatorThrottle = Utilities.accelLimit(Constants.ELEVATOR_FULL_SPEED_TIME, elevatorThrottle, prevElevatorThrottle);
      
        
        /*
        * When the trigger is held the robot automatically stacks the totes as they slide in and 
        * hit the limit switch
        */
        if(secondaryStick.getRawButton(Constants.AUTOMATED_TOTE_STACKING) && !toteLimitSwitch.get()) { // If automated stacking button is pressed and a tote is properly loaded on the platform pressing the limit switch.
        	 if ((elevator.getHeight() < Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION - 0.5
        		  || elevator.getHeight() > Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION + 4)
        		  && elevator.getDesiredHeight() != Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION){
        		         	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
        	 }
        	 else if (elevator.getHeight() >= Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION - 0.5
        			  && elevator.getHeight() <= Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION + 0.5
        			  && elevator.getDesiredHeight() != Constants.ELEVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION){    		
        		      		elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION);       		  
        	 }
        }
        // Moves elevator to predetermined elevator heights. They are exclusive so two values may not be sent at once.
        else if(secondaryStick.getRawButton(Constants.LOWER_ONE_TOTE_LEVEL)) {
            elevator.setDesiredHeight(elevator.getHeight() - Constants.ELEVATOR_HEIGHT_FOR_ONE_TOTE);
        }
        else if(secondaryStick.getRawButton(Constants.RAISE_ONE_TOTE_LEVEL)) {
        	elevator.setDesiredHeight(elevator.getHeight() + Constants.ELEVATOR_HEIGHT_FOR_ONE_TOTE);
        }
        else if(secondaryStick.getRawButton(Constants.SCORING_PLATFORM_HEIGHT)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_SCORING_PLATFORM);
        }
        else if(secondaryStick.getRawButton(Constants.RECYCLING_CONTAINER_GROUND_PICKUP_HEIGHT)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_RECYCLING_CONTAINER_PICKING_OFF_THE_GROUND);
        }
        else if(secondaryStick.getRawButton(Constants.STEP_HEIGHT)) {
         	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_STEP);
        }
        else if(secondaryStick.getRawButton(Constants.FEEDER_STATION_BOTTOM_HEIGHT)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
        }
        else if(secondaryStick.getRawButton(Constants.TOTE_ABOVE_FEEDER_STATION_HEIGHT)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION);
        }
        	
        // Uses button 3 on the main stick as a toggle for the platform 
       	if(secondaryStick.getRawButton(Constants.PLATFORM_TOGGLE) == true && prevPlatformControllingButton == false) {
       		platform.flip();
       	}
       	prevPlatformControllingButton = secondaryStick.getRawButton(Constants.PLATFORM_TOGGLE);
       	
       	//Gyro Calibration Code
        driveTrain.updateAngle(secondaryStick.getRawButton(Constants.GYRO_CALIBRATION));
    
        //Cuts the speed of the elevator in half while button 9 is held.
        if (secondaryStick.getRawButton(Constants.ELEVATOR_SPEED)){
        	elevatorSpeedLimit = .5; // half speed
        }
        else if (secondaryStick.getRawButton(Constants.ELEVATOR_SPEED) == false){
        	elevatorSpeedLimit = 1; // full speed
        }
        
        	
        /*
         * This is code for the override button. When button 6 is pressed it allows the secondary 
         * driver to manually drive the elevator with the joystick. In this case you need to set the 
         * desired height as the current height so that it doesn't recoil to the previous desired height
         * when the driver lets off of the button.
         */
        
        if (secondaryStick.getRawButton(Constants.ELEVATOR_MANUAL_OVERRIDE)){
        	elevator.drive(elevatorThrottle * elevatorSpeedLimit);
        	elevator.setDesiredHeight(elevator.getHeight());
        }
        
        else elevator.goToDesiredHeight(elevatorSpeedLimit);
        
        prevElevatorThrottle = elevatorThrottle;
           
        elevator.update();
        System.out.println(elevator.getHeight());
    }
	
	public void disabledInit() {
		System.out.println("DISABLED");
		compressor.stop();
		teleopTimer.stop();
	}
	
	public void disabledPeriodic() {
		driveTrain.resetGyro(); // Constantly resets while disabled so the robot starts the match at a gyro heading of zero.
	}
    
    public void testPeriodic() {
    		
    }
    
}