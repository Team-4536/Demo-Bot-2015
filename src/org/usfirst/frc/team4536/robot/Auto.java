package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto { 
	DriveTrain autoDriveTrain;
	Elevator autoElevator;
	Tipper tipper;
	Platform platform;
	
	
	public Auto(DriveTrain autoConstructerDriveTrain, Elevator elevator){		
		autoDriveTrain = autoConstructerDriveTrain;
		autoElevator = elevator;
		tipper = new Tipper(Constants.RIGHT_TIPPER_SOLENOID_CHANNEL, Constants.LEFT_TIPPER_SOLENOID_CHANNEL);
		platform = new Platform(Constants.RIGHT_PLATFORM_SOLENOID_CHANNEL, Constants.LEFT_PLATFORM_SOLENOID_CHANNEL); // Also retracts Platform as part of initialization.
	}
	
	public static double autoNumber(){

		SendableChooser autoChooser;
		autoChooser = new SendableChooser();
		autoChooser.addObject("Drive Forward", 1);
		autoChooser.addObject("Drive Backward with Recycling Container", 2);
		autoChooser.addObject("Drive Backward with Tote", 3);
		autoChooser.addObject("Two Totes", 4); //Two tote auto
		autoChooser.addObject("Two Recycling Containers", 5);
		autoChooser.addDefault("Do Nothing", 6);
		autoChooser.addObject("Three Tote Stack", 7);
		autoChooser.addObject("Tote and Container", 8);
		autoChooser.addObject("Extend Platform", 9);
		SmartDashboard.putData("Auto_Chooser" , autoChooser); 
		
		//Returns the number of the auto selected auto on the SmartDashboard
		return autoChooser.getSelected().hashCode();
	}
	
	public void doNothing(){
		autoDriveTrain.drive(0, 0);
	}
	
	public void extendPlatform() {
		platform.extend();
	}

	public void driveForward(double autoTime){
		
		if (autoTime < 1.8){
			autoDriveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	public void driveBackwardWithRecyclingContainer(double autoTime){
		if( !tipper.isExtended() && (tipper.timeExtended() < 1) 
				&& autoElevator.getDesiredHeight() != Constants.TOP_LIMIT_SWITCH_HEIGHT) {
	    		tipper.extend();
	    }
	    else if (tipper.timeExtended() > 1){
	    		autoElevator.setDesiredHeight(Constants.TOP_LIMIT_SWITCH_HEIGHT);
	    					
		}	    	
	    if (autoTime > 3 && tipper.isExtended()){
	    		tipper.retract();			
	    }
		
	    else if (autoTime > 4 && autoTime < 8 && !tipper.isExtended()){
			autoDriveTrain.driveStraight(-0.3, 0, Constants.AUTO_FORWARD_FULL_SPEED_TIME);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	public void driveBackwardWithTote (double autoTime){
	    if (autoTime < 1){
	    		autoElevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
		}
	    else if (autoTime > 1 && autoTime < 5){
	    	autoDriveTrain.turnTo(-90, Constants.AUTO_TURN_FULL_SPEED_TIME);
	    }
	    else if (autoTime > 5 && autoTime < 7 
	    		&& autoDriveTrain.gyroGetAngle() > -95 && autoDriveTrain.gyroGetAngle() < -85){
			autoDriveTrain.driveStraight(-0.3, -90, 1);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	public void toteAndContainer(double autoTime){
	}	
	
	public void twoTote (double autoTime){
	
	}
	public void twoRecyclingContainers(double autoTime){
		
	}
	
	public void threeToteStack(double autoTime){
		
	}
	
}