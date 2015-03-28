package org.usfirst.frc.team4536.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Auto { 
	DriveTrain autoDriveTrain;
	Elevator autoElevator;
	Tipper tipper;
	
	
	public Auto(DriveTrain autoConstructerDriveTrain, Elevator elevator){		
		autoDriveTrain = autoConstructerDriveTrain;
		autoElevator = elevator;
		tipper = new Tipper(Constants.RIGHT_TIPPER_SOLENOID_CHANNEL, Constants.LEFT_TIPPER_SOLENOID_CHANNEL);
	}
	
	
	//Creates each auto as an option on the SmartDashboard. The default is doing nothing.
	public static double autoNumber(){
		SendableChooser autoChooser;
		autoChooser = new SendableChooser();
		autoChooser.addObject("Drive Forward", 1);
		autoChooser.addObject("Drive Backward with Container", 2);
		autoChooser.addObject("Drive Backward with Tote", 3);
		autoChooser.addObject("DriveBackward with Recycling Container and Tote", 4);
		autoChooser.addObject("Two Tote Auto", 5);
		autoChooser.addObject("Three Tote Stack", 6);
		autoChooser.addObject("Two Recycling Container Auto", 7);
		autoChooser.addObject("Drive With Container to Right Feeder Station", 8);
		autoChooser.addObject("Drive With Container to Left Feeder Station", 9);
		autoChooser.addDefault("Do Nothing", 10);
		SmartDashboard.putData("Auto_Chooser_Thing" , autoChooser); 
		
		//Returns the number of the auto selected auto on the SmartDashboard
		return autoChooser.getSelected().hashCode();
	}
	
	public void doNothing(){
		autoDriveTrain.drive(0, 0);
	}

	//It takes in autoTime to have the robot do different things at specific times during auto
	public void driveForward(double autoTime){
		
		if (autoTime < 1.8){
			autoDriveTrain.driveStraight(0.5, 0, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
			autoDriveTrain.drive(0 , 0);
	}
	
	//It takes in autoTime to have the robot do different things at specific times during auto
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
	
	//It takes in autoTime to have the robot do different things at specific times during auto
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
	//It takes in autoTime to have the robot do different things at specific times during auto
	public void toteAndContainer(double autoTime){
		
	}	
	//It takes in autoTime to have the robot do different things at specific times during auto
	public void twoTote (double autoTime){
	
	}
	//It takes in autoTime to have the robot do different things at specific times during auto
	public void threeToteStack (double autoTime){
			
	}
	//It takes in autoTime to have the robot do different things at specific times during auto
	public void twoRecyclingContainers(double autoTime){
		
	}
	//It takes in autoTime to have the robot do different things at specific times during auto
	public void driveWithRecyclingContainerToRightFeederStation(double autoTime){
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
						
		else if (autoTime > 4 && autoTime < 12 && !tipper.isExtended()){
				autoDriveTrain.turnTo(-135, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
				autoDriveTrain.drive(0 , 0);			
	}
		
	public void driveWithRecyclingContainerToLeftFeederStation(double autoTime){
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
						
		else if (autoTime > 4 && autoTime < 12 && !tipper.isExtended()){
				autoDriveTrain.turnTo(135, Constants.AUTO_TURN_FULL_SPEED_TIME);
		}
		else
				autoDriveTrain.drive(0 , 0);
	}

	
}
