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
	
	// Previous values used for toggles for the platform and tipper 
	boolean prevPlatformControllingButton;
	boolean prevTipperControllingButton;
	
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
    	
    	toteLimitSwitch = new DigitalInput(Constants.TOTE_LIMIT_SWITCH_CHANNEL);

    	autoTimer = new Timer();
    	auto = new Auto(driveTrain);
    	//This gets the stuff on the SmartDashboard, it's like a dummy variable. Ask and I shall explain more
		autoNumber = (int) auto.autoNumber();
    }
	
	public void autonomousInit() {
		compressor.start();

		autoTimer.reset();
		autoTimer.start();
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
			case 2: auto.driveForwardWithRecyclingContainer(autoTime);
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
    	
    	driveTrain.drive(finalThrottleY, finalThrottleX); */
    	
    	
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
        else elevator.goToHeight();
        
        prevElevatorThrottle = elevatorThrottle;
              
        elevator.update();
        
        if(secondaryStick.getRawButton(1)) {
        	if (!toteLimitSwitch.get()
        		&& (elevator.getHeight() < Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION - 0.5
        		|| elevator.getHeight() > Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION + 0.5)){
        		elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
        	}
        	else if (!toteLimitSwitch.get()){        		
        			elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION);
        	}
        }
        
        //Automation of setting tote stack then backing up
        /*
        if (mainStick.getRawButton(11) == true) {
        
        }     */
        
        /*if (teleopTimer.get() > 133){
        	tipper.extend();
        	elevator.setHeight(0);
        }*/
        
        
        else if(secondaryStick.getRawButton(3)) {
        	elevator.setDesiredHeight(elevator.getHeight() + Constants.ELEVATOR_HEIGHT_FOR_ONE_TOTE);
        }
        else if(secondaryStick.getRawButton(2)) {
        	elevator.setDesiredHeight(elevator.getHeight() - Constants.ELEVATOR_HEIGHT_FOR_ONE_TOTE);
        }
        else if(secondaryStick.getRawButton(4)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_SCORING_PLATFORM);
        }
        else if(secondaryStick.getRawButton(5)) {
         	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_STEP);
        }
        else if(secondaryStick.getRawButton(11)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_A_TOTE_ABOVE_FEEDER_STATION);
        }
        else if(secondaryStick.getRawButton(10)) {
        	elevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
        }
        else if(secondaryStick.getRawButton(7)) {
        	
        }
        else if(secondaryStick.getRawButton(9)) {
        	
        }
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
