package org.usfirst.frc.team4536.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
	// Robot Systems
	DriveTrain driveTrain;
	Platform platform;
	Tipper tipper;
	Tower tower;
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
	Joystick towerStick;
	
	
	// Previous values used for toggles for the platform and tipper 
	boolean prevPlatformControllingButton;
	boolean prevTipperControllingButton;
	
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
    					    		Constants.RIGHT_TALON_CHANNEL);
    	driveTrain.startGyro();
    	platform = new Platform(Constants.RIGHT_PLATFORM_SOLENOID_CHANNEL, Constants.LEFT_PLATFORM_SOLENOID_CHANNEL);
    	platform.retract();
    	tipper = new Tipper(Constants.RIGHT_TIPPER_SOLENOID_CHANNEL, Constants.LEFT_TIPPER_SOLENOID_CHANNEL);
    	tipper.retract();    	
    	tower = new Tower(Constants.TOWER_MOTOR_CHANNEL);
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
    	towerStick = new Joystick(Constants.TOWER_STICK_PORT);
    	
    	compressor = new Compressor();
    	
    	// Previous values used for toggles for the platform and tipper  
    	prevPlatformControllingButton = false;
    	prevTipperControllingButton = false;
    	
    	// This value is necessary for our acceleration limit on the elevator
    	prevElevatorThrottle = 0;
    	
    	toteLimitSwitch = new DigitalInput(Constants.TOTE_LIMIT_SWITCH_CHANNEL);

    	autoTimer = new Timer();
    	auto = new Auto(driveTrain, elevator, tower, platform);
    	//This gets the stuff on the SmartDashboard, it's like a dummy variable. Ask and I shall explain more
		autoNumber = (int) auto.autoNumber();
    }
	
	public void autonomousInit() {
		autoNumber = (int) auto.autoNumber();
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
			case 4: auto.toteAndContainer(autoTime);
					break;
			case 5: auto.twoTote(autoTime);
					break;
			case 6: auto.twoRecyclingContainers(autoTime);
					break;
			case 7: auto.tower(autoTime);
					break;
			case 8: auto.doNothing();
					break;
			default: auto.doNothing();
					 break;
		
		}
		elevator.goToDesiredHeight(0.25);
		elevator.update(); 
    }
	
	public void teleopInit() {
		compressor.start();
		teleopTimer.reset();
	}
	
	public void teleopPeriodic() {		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(-mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(-mainStick.getX(), Constants.DEAD_ZONE);
    	
    	//driveTrain.driveStraight(-0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
    	//System.out.println(driveTrain.gyroGetAngle());
    	
    	/*
    	 * If the tipper (back piston) is extended, we don't want the driver to have full driving ability
    	 */
    	if(mainStick.getRawButton(7)) {	
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
    	else {
    		// If button 4 on the main stick is pressed slow mode is enabled
    		if(mainStick.getRawButton(4) == true) {
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
        		double throttleX = Utilities.speedCurve(-mainStickX, Constants.TURN_SPEED_CURVE) * Constants.TURN_SPEED_LIMIT;
        		
        		finalThrottleY = Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
        		finalThrottleX = Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
        		
        		prevThrottleY = finalThrottleY;
        		prevThrottleX = finalThrottleX;
        	}
        	
        	driveTrain.drive(finalThrottleY, finalThrottleX); 
    	}
    	
    	
    	
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
      
        //Tower Code
        tower.setSpeed(Constants.TOWER_FULL_SPEED_TIME_TELEOP, towerStick.getY());
        
        /*
        * When the trigger is held the robot automatically stacks the totes as they slide in and 
        * hit the limit switch
        */
        if(secondaryStick.getRawButton(1) && !toteLimitSwitch.get()) {
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
        	
        else if(secondaryStick.getRawButton(2)) {
            elevator.setDesiredHeight(elevator.getHeight() - Constants.ELEVATOR_HEIGHT_FOR_ONE_TOTE);
        }
        else if(secondaryStick.getRawButton(3)) {
        	elevator.setDesiredHeight(elevator.getHeight() + Constants.ELEVATOR_HEIGHT_FOR_ONE_TOTE);
        }
        else if(secondaryStick.getRawButton(4)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_SCORING_PLATFORM);
        }
        else if(secondaryStick.getRawButton(7)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_RECYCLING_CONTAINER_PICKING_OFF_THE_GROUND);
        }
        else if(secondaryStick.getRawButton(8)) {
         	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_STEP);
        }
        else if(secondaryStick.getRawButton(10)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
        }
        else if(secondaryStick.getRawButton(11)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION);
        }
        	
     // Uses button 3 on the main stick as a toggle for the platform 
       	if(mainStick.getRawButton(5) == true && prevPlatformControllingButton == false) {
       		platform.flip();
       	}
       	prevPlatformControllingButton = mainStick.getRawButton(5);
    
        //Cuts the speed of the elevator in half while button 9 is held
        if (secondaryStick.getRawButton(9)){
        	elevatorSpeedLimit = .5;
        }
        else if (secondaryStick.getRawButton(9) == false){
        	elevatorSpeedLimit = 1;
        }
        
        	
        /*
         * This is code for the override button. When button 6 is pressed it allows the secondary 
         * driver to manually drive the elevator with the joystick. In this case you need to set the 
         * desired height as the current height so that it doesn't recoil to the previous desired height
         * when the driver lets off of the button.
         */
        
        if (secondaryStick.getRawButton(6)){
        	elevator.drive(elevatorThrottle);
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
		driveTrain.resetGyro();
	}
    
    public void testPeriodic() {
    		
    }
    
}
