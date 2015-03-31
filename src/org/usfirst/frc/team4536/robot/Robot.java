package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CameraServer; 

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

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
	int fieldSide;
	DigitalInput toteLimitSwitch;
	CameraServer camera = CameraServer.getInstance();
	
	Compressor compressor;
	
	// Joysticks
	Joystick mainStick;
	Joystick secondaryStick;
	Joystick towerStick;
	
		int session;
	    Image frame;
	
	// Previous values used for toggles for the platform, tipper, and automatic stack setting functionality in that order. 
	boolean prevPlatformControllingButton;
	boolean prevTipperControllingButton;
	boolean prevAutoSet;
	boolean prevToteLimitSwitchValue;
	
	// This value is necessary for our acceleration limit on the elevator
	double prevElevatorThrottle;
	
	double prevThrottleY = 0;
	double prevThrottleX = 0;
	double finalThrottleY = 0;
	double finalThrottleX = 0;
	double elevatorSpeedLimit = 1;
	
	int numberOfTotes = 0;
	
	public void robotInit() {
		// Robot Systems
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL,
    					    		Constants.GYRO_SENSOR_CHANNEL);
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
    							Constants.BOTTOM_LIMIT_SWITCH_CHANNEL,
    							Constants.TOTE_LIMIT_SWITCH_CHANNEL);
    	elevator.setActualHeight(0);
    	teleopTimer = new Timer();
    	teleopTimer.start();
    	tower = new Tower(Constants.TOWER_MOTOR_CHANNEL); 
    	
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

    	autoTimer = new Timer();
    	auto = new Auto(driveTrain, elevator, tower);
    	//This gets the stuff on the SmartDashboard, it's like a dummy variable. Ask and I shall explain more
		autoNumber = (int) auto.autoNumber();
		fieldSide = driveTrain.fieldSide();
		
    	frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // the camera name (ex "cam1") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam1",
        NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);

    }
	
	public void autonomousInit() {
		compressor.start();
		autoTimer.reset();
		autoTimer.start();
		driveTrain.resetGyro();
	}

	public void autonomousPeriodic() {	

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
		case 6: auto.threeToteStack(autoTime);
				break;
		case 7: auto.twoRecyclingContainers(autoTime);
				break;
		case 8: auto.driveWithRecyclingContainerToRightFeederStation(autoTime);
				break;
		case 9: auto.driveWithRecyclingContainerToLeftFeederStation(autoTime);
				break;
		case 10: auto.tower(autoTime);
				break;
		case 11: auto.doNothing();
				break;
		default: auto.doNothing();
				 break;
		}
		// Makes sure the elevator works in auto 
		elevator.goToDesiredHeight(1);
		elevator.update();
    }
	
	public void teleopInit() {
		compressor.start();
		teleopTimer.reset();
	}
	
	public void teleopPeriodic() {
		
		NIVision.IMAQdxStartAcquisition(session);
	     /*
	      * grab an image, draw the circle, and provide it for the camera server
	      * which will in turn send it to the dashboard.
  		  */
	    NIVision.IMAQdxGrab(session, frame, 1);
	    CameraServer.getInstance().setImage(frame);
	        
		NIVision.IMAQdxStopAcquisition(session);
		
		//Retracted Timer
		double retractedTime = platform.timeRetracted();
		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(-mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(-mainStick.getX(), Constants.DEAD_ZONE);
    	
    	/*
    	 * If the tipper (back piston) is extended, we don't want the driver to have full driving ability
    	 */
    	//Code for automation of recycling container pickup. Tips forward then drives the elevator up to catch on the recycling container.
    	if(mainStick.getRawButton(Constants.RECYCLING_CONTAINER_PICK_UP)) {	
    		if(elevator.getDesiredHeight() != Constants.TOP_LIMIT_SWITCH_HEIGHT) {
    			tipper.extend();
    			if(tipper.timeExtended() > 1) {
    				elevator.setDesiredHeight(Constants.TOP_LIMIT_SWITCH_HEIGHT);
    			}
    		}    	
    		else if (elevator.getHeight() > (Constants.ELEVATOR_HEIGHT_FOR_RECYCLING_CONTAINER_PICKING_OFF_THE_GROUND + 12)){
    			tipper.retract();
    		}
    	}
    	
    	//Automation of setting tote stack then backing up
        if (mainStick.getRawButton(Constants.AUTOMATIC_STACK_SET_DOWN_AND_DRIVE_BACK) == true){
        	
        	if (mainStick.getRawButton(Constants.AUTOMATIC_STACK_SET_DOWN_AND_DRIVE_BACK) == true && prevAutoSet == false) {
        		driveTrain.resetGyro();
        	}
        	
        	platform.retract();
        	
        	if (retractedTime > 3) {
        		elevator.setDesiredHeight(-30);
  
        		if (elevator.bottomLimitSwitchValue() == true || (elevator.getHeight() < 0 && elevator.getHeight() > -1)) {
        			driveTrain.driveStraight(-0.3, 0, Constants.SLOW_TURN_FULL_SPEED_TIME);
        		} 
        	}
        }
        // If the turn from feeder station button is pressed the robot turns from the feeder station toward the scoring platforms.
        else if (mainStick.getRawButton(Constants.TURN_FROM_FEEDER_STATION)) {
        	driveTrain.slowTurnTo(0, Constants.SUPER_SLOW_TURN_FULL_SPEED_TIME, Constants.SUPER_SLOW_TURN_SPEED_LIMIT);
        }
        //if button 7 is pressed, SUPER SLOW MODE is ENABLED
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
    	else {
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.FORWARD_SPEED_CURVE) * Constants.FORWARD_SPEED_LIMIT;
    		double throttleX = Utilities.speedCurve(-mainStickX, Constants.TURN_SPEED_CURVE) * Constants.TURN_SPEED_LIMIT;
    		
    		finalThrottleY = Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
    		finalThrottleX = Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
    		driveTrain.drive(finalThrottleY, finalThrottleX);
    		
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    		
    	}
    	
        prevAutoSet = mainStick.getRawButton(Constants.AUTOMATIC_STACK_SET_DOWN_AND_DRIVE_BACK); // Defines the previous automated set button value
    	
    	
    	
     // Uses button tipper toggle on the main stick as a toggle for the tipper
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
      
        //Code for the tower
        tower.setSpeed(Constants.TOWER_FULL_SPEED_TIME_TELEOP, towerStick.getY());
        
        /*
        * When the trigger is held the robot automatically stacks the totes as they slide in and 
        * hit the limit switch
        */
        if(secondaryStick.getRawButton(Constants.AUTOMATED_TOTE_STACKING) && elevator.toteLimitSwitchValue()) {
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
       		numberOfTotes = 0;
       	}
       	prevPlatformControllingButton = secondaryStick.getRawButton(Constants.PLATFORM_TOGGLE);
    
       	//Gyro Calibration Code
        driveTrain.updateAngle(mainStick.getRawButton(Constants.GYRO_CALIBRATION), elevator.toteLimitSwitchValue());
        System.out.println("Gyro Angle: " + driveTrain.gyroGetAngle());
        
       	/*Sets the speed of the elevator proportional to the number of totes
       	 * The more totes, the slower the speed
       	 * If there are 3 or fewer totes, the elevator runs at full speed
       	 */
       	 if (numberOfTotes <= 3){
       		 	elevatorSpeedLimit = 1;
       	 }
       	         
       	 else if (numberOfTotes > 3){
       	        elevatorSpeedLimit = (1/(Math.pow(numberOfTotes, .5)));
       	 }
        
        	
        /*
         * This is code for the override button. When button 6 is pressed it allows the secondary 
         * driver to manually drive the elevator with the joystick. In this case you need to set the 
         * desired height as the current height so that it doesn't recoil to the previous desired height
         * when the driver lets off of the button.
         */
        
        if (secondaryStick.getRawButton(Constants.ELEVATOR_MANUAL_OVERRIDE)){
        	elevator.drive(elevatorThrottle);
        	elevator.setDesiredHeight(elevator.getHeight());
        }
        
        if (elevator.toteLimitSwitchValue() && !prevToteLimitSwitchValue){
        	numberOfTotes = (numberOfTotes + 1);
        }
        prevToteLimitSwitchValue =elevator.toteLimitSwitchValue();
        prevElevatorThrottle = elevatorThrottle;
        
        elevator.goToDesiredHeight(elevatorSpeedLimit);    
        elevator.update();
        
        
    }
	
	public void disabledInit() {
		System.out.println("DISABLED");
		compressor.stop();
		teleopTimer.stop();
	}
	
	public void disabledPeriodic() {
		driveTrain.resetGyro(); // Constantly resets while disabled so the robot starts the match at a gyro heading of zero.
		driveTrain.setActualAngle(0);
	}
    
    public void testPeriodic() {
    		
    }
    
}
