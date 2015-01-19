package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick mainStick;
	Joystick secondaryStick;
	
	public void robotInit() {
    		driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, 
    					    Constants.RIGHT_TALON_CHANNEL);
        	mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    		secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    	}

	public void autonomousPeriodic() {
		
    	}

	public void teleopPeriodic() {
    		// Gets X and Y values from mainStick and puts a dead zone on them
      		double mainStickY = Utilities.deadZone(mainStick.getY(), Constants.DEAD_ZONE);
    		double mainStickX = Utilities.deadZone(mainStick.getX(), Constants.DEAD_ZONE);
    	
    		// Sets throttle values based on mainStick X and Y values (with dead zone).
    		double forwardThrottle = mainStickX;
    		double turnThrottle = mainStickY;
    	
    		driveTrain.drive(Utilities.deadZone(forwardThrottle, Constants.DEAD_ZONE), 
    				 Utilities.deadZone(turnThrottle, Constants.DEAD_ZONE));
    	}
    
    	public void testPeriodic() {
    		
    	}
    
}
