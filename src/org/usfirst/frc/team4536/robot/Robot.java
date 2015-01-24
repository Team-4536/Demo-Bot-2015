package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick mainStick;
	Joystick secondaryStick;
	Compressor compressor;
	
	double prevForwardThrottle;
	double prevTurnThrottle;

	public void robotInit() {
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    		Constants.RIGHT_TALON_CHANNEL);
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	compressor = new Compressor();
    	prevForwardThrottle = 0;
    	prevTurnThrottle = 0;
    	
    	System.out.println("YOU ARE CONNECTED 'MON, IT'S BOBSLED TIME!");
    }

	public void autonomousPeriodic() {
		compressor.start();
		}

	public void teleopInit() {
		
	}
	public void teleopPeriodic() {
		compressor.start();
		
		if (secondaryStick.getRawButton(3)) {
			compressor.stop();
		}
		
    	// Gets X and Y values from mainStick and puts a dead zone on them
      	double mainStickY = Utilities.deadZone(mainStick.getY(), Constants.DEAD_ZONE);
    	double mainStickX = Utilities.deadZone(mainStick.getX(), Constants.DEAD_ZONE);
    	
    	// Puts a speed curve on the X and Y values from the mainStick 
    	mainStickY = Utilities.speedCurve(mainStickY, Constants.SPEED_CURVE);
    	mainStickX = Utilities.speedCurve(mainStickX, Constants.SPEED_CURVE);
    	
    	//Acceleration Limit
/*
    	double finalForwardThrottle;
    	double finalTurnThrottle;
    	
    	double throttleForwardDiff = mainStickY - prevForwardThrottle;
    	double throttleTurnDiff = mainStickX - prevTurnThrottle;
    	double forwardAccelerationLimit = 0.02 / Constants.FORWARD_FULL_SPEED_TIME; //Sets forwardAccelerationLimit to the proper double to make the robot reach its top speed in the given FORWARD_FULL_SPEED_TIME
    	double turnAccelerationLimit = 0.02 / Constants.TURN_FULL_SPEED_TIME; //Sets turnAccelerationLimit to the proper double to make the robot reach its top speed in the given TURN_FULL_SPEED_TIME
    	*/
    	//forward throttle of robot can increase only by + or - forwardAccelerationLimit per cycle (20ms)
    	/*if (throttleForwardDiff > forwardAccelerationLimit) {
    		finalForwardThrottle = prevForwardThrottle + forwardAccelerationLimit;
    	} else if (throttleForwardDiff < -forwardAccelerationLimit) {
    		finalForwardThrottle = prevForwardThrottle - forwardAccelerationLimit;
    	} else {
    		finalForwardThrottle = mainStickY;
    	}*/
    	
    	//turning throttle of robot can increase only by + or - turnAccelerationLimit per cycle (20ms)
    	/*if (throttleTurnDiff > turnAccelerationLimit) {
    		finalTurnThrottle = prevTurnThrottle + turnAccelerationLimit;
    	} else if (throttleTurnDiff < -turnAccelerationLimit) {
    		finalTurnThrottle = prevTurnThrottle - turnAccelerationLimit;
    	} else {
    		finalTurnThrottle = mainStickX;
    	}*/
    	
    	driveTrain.drive(Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, mainStickY, prevForwardThrottle), Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, mainStickX, prevTurnThrottle));
    	
    	//prev variables are redefined after the code is executed in a cycle
    	prevForwardThrottle = Utilities.accelLimit(Constants.FORWARD_FULL_SPEED_TIME, mainStickY, prevForwardThrottle);
    	prevTurnThrottle = Utilities.accelLimit(Constants.TURN_FULL_SPEED_TIME, mainStickX, prevTurnThrottle);
    	    	
    	
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
