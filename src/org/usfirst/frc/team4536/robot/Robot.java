package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick mainStick;
	Joystick secondaryStick;
	Compressor compressor;
	Platform platform; 
	
	double prevThrottleY;
	double prevThrottleX;
	double finalThrottleY;
	double finalThrottleX;
	int trueCount;
	
	boolean prevJoystickButton3;
	
	public void robotInit() {
		
		//Drive
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL);
    	
    	//Joysticks
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	
    	//Pneumatics
    	compressor = new Compressor();
    	
    	//Starting the driveTrain gyro sensor
    	driveTrain.startGyro();
    	
    	//Previous Values
    	prevThrottleY = 0;
    	prevThrottleX = 0;
    	prevJoystickButton3 = false;
    	
    	//Platform
    	platform = new Platform(1,0);
    	platform.retract();
    	
    	System.out.println("YOU ARE CONNECTED 'MON, IT'S BOBSLED TIME!");
    }
	
	public void autonomousInit(){
		driveTrain.resetGyro(); //Gyro heading is set to zero.
		compressor.start();
	}

	public void autonomousPeriodic() {
		driveTrain.driveStraight(0.5, 0, 5);
		//driveTrain.turnTo(30, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}

	public void teleopInit() {
		trueCount = 0;
		compressor.start();
	}
	public void teleopPeriodic() {
		
		//Gyro Code
		SmartDashboard.putNumber("Gyro_Sensor_Value", driveTrain.gyroSensor.getAngle());
		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(mainStick.getX(), Constants.DEAD_ZONE);
    	
    	//Drive code with acceleration limits and slow mode
    	
    	if (mainStick.getRawButton(4) == true) { //Slow Mode - speed limit and lower acceleration limit
    		//Speed limit
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.SLOW_FORWARD_SPEED_CURVE) * Constants.SLOW_FORWARD_SPEED_LIMIT; //Speed limit (multiply by 0.2)
        	double throttleX = Utilities.speedCurve(mainStickX, Constants.SLOW_TURN_SPEED_CURVE) * Constants.SLOW_TURN_SPEED_LIMIT; //Speed limit (multiply by 0.2)
        	
        	//Acceleration Limit
    		finalThrottleY = Utilities.accelLimit(Constants.SLOW_FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
        	finalThrottleX = Utilities.accelLimit(Constants.SLOW_TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
        	//prev variables are redefined after the code is executed in a cycle
    		prevThrottleY = finalThrottleY;
    		prevThrottleX = finalThrottleX;
    		
    	} else { //Normal Acceleration Drive Mode
    		double throttleY = Utilities.speedCurve(mainStickY, Constants.FOWARD_SPEED_CURVE);
        	double throttleX = Utilities.speedCurve(mainStickX, Constants.TURN_SPEED_CURVE);
        	
        	//Acceleration Limit
    		finalThrottleY = Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, throttleY, prevThrottleY);
        	finalThrottleX = Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, throttleX, prevThrottleX);
    		
        	//prev (previous) variables are redefined after the code is executed in a cycle
        	prevThrottleY = finalThrottleY;
        	prevThrottleX = finalThrottleX;
        	
    	} 
    	
    	//Drive
		if (mainStick.getRawButton(7) == true && finalThrottleX == 0) {
			driveTrain.turnTo(0, Constants.TURN_FULL_SPEED_TIME); //Turns robot to heading of 0.
		} else {
	    	driveTrain.drive(finalThrottleY, finalThrottleX);
		}
		
    	//driveTrain.drive(finalThrottleY, finalThrottleX);
    	
    	if (mainStick.getRawButton(3) == true && prevJoystickButton3 == false) { //Makes it so there is a limit of one flip 
    		
    		platform.flip(); //switches platform state between extended and retracted  
    	}
    	prevJoystickButton3 = mainStick.getRawButton(3); //sets previous value after flip is executed
    	
    	//COOL CODE DO NOT DELETE! FLIPS AFTER THE BUTTON HAS BEEN HELD DOWN AWHIE AND THEN RELEASED
    	/*if (mainStick.getRawButton(3) == true && prevJoystickButton3 == true) {
    		trueCount += 1;
    	}
    		
    	System.out.println(trueCount);
  
    	if (mainStick.getRawButton(3) == false && prevJoystickButton3 == true && trueCount > Utilities.hold(Constants.HOLD_BUTTON_TIME)){
    		platform.flip();
    		trueCount = 0;
    			
    	}*/

    }
	
	public void disabledInit() {
		System.out.println("DISABLED");
	}
	
	public void disabledPeriodic() {
		compressor.stop();
		driveTrain.resetGyro();
	}
	
	public void testInit(){
		
	}

    public void testPeriodic() {

    }
    
}
