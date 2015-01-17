// I have made a comment and can use github
package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	DriveTrain driveTrain;
	Joystick mainStick;
	Joystick secondaryStick;
	
    public void robotInit() {
    	driveTrain = new DriveTrain(Constants.LEFT_TALON_CHANNEL, Constants.RIGHT_TALON_CHANNEL);
        mainStick = new Joystick(Constants.LEFT_STICK_PORT);
    	secondaryStick = new Joystick(Constants.RIGHT_STICK_PORT);
    }

    public void autonomousPeriodic() {
  
    }


    public void teleopPeriodic() {
    	double forwardThrottle = mainStick.getY();
    	double turnThrottle = mainStick.getX();
    	
    	driveTrain.drive(Utilities.deadZone(forwardThrottle, Constants.DEAD_ZONE), Utilities.deadZone(turnThrottle, Constants.DEAD_ZONE));
        
    	SmartDashboard.putNumber("Y joystick Value + 1", mainStick.getY() + 1);
    	SmartDashboard.putNumber("Forward Throttle with dead zone", Utilities.deadZone(forwardThrottle, Constants.DEAD_ZONE));
    }
    
    
    
    public void testPeriodic() {
    
    }
    
}
