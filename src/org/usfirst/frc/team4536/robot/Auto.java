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
		autoChooser.addObject("Drive With Container to Feeder Station", 8);
		autoChooser.addDefault("Do Nothing", 9);
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
	  /* For if we can pick up off the ground 
	   * if (autoTime < 1){
	    	autoElevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
		}
	    else if (autoTime >= 1 && autoTime < 3){
	    	autoDriveTrain.driveStraight(1, 0, Constants.AUTO_FORWARD_FULL_SPEED_TIME);
	    }
	    else if (autoTime >= 3 && autoTime < 4){
	    	autoElevator.setDesiredHeight(Constants.BOTTOM_LIMIT_SWITCH_HEIGHT);
	    }
	    else if (autoTime >= 4 && autoTime < 5){
	    	autoElevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
	    }
	    else if (autoTime >= 5 && autoTime < 7){
	    	autoDriveTrain.driveStraight(1, 0, Constants.AUTO_FORWARD_FULL_SPEED_TIME);
	    }
	    else if (autoTime >= 7 && autoTime < 8){
	    	autoElevator.setDesiredHeight(Constants.BOTTOM_LIMIT_SWITCH_HEIGHT);
	    }
	    else if (autoTime >= 8 && autoTime < 9){
	    	autoElevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
	    }
	    else if (autoTime >= 9 && autoTime < 13){
	    	autoDriveTrain.turnTo(-90, Constants.AUTO_TURN_FULL_SPEED_TIME);
	    }
	    else if (autoTime >= 13 && autoTime < 15 
	    		&& autoDriveTrain.gyroGetAngle() > -95 && autoDriveTrain.gyroGetAngle() < -85){
			autoDriveTrain.driveStraight(-0.3, -90, 1);
		}
		If we can't pick up off the ground
		*/
		if (autoTime < 1){
	    	autoElevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
		}
	    else if (autoTime >= 1 && autoTime < 3){
	    	autoDriveTrain.driveStraight(1, 0, Constants.AUTO_FORWARD_FULL_SPEED_TIME);
	    }
	    else if (autoTime >= 3 && autoTime < 3.5 && !tipper.isExtended()){
	    	autoElevator.setDesiredHeight(Constants.BOTTOM_LIMIT_SWITCH_HEIGHT);
	    }
	    else if (autoTime >= 3.5 && autoTime < 4 && tipper.isExtended()){
	    	tipper.extend();
	    }
	    else if (autoTime >= 4 && autoTime < 4.5 && tipper.isExtended()){
	    	autoElevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
	    }	    	
	    else if (autoTime >= 4.5 && autoTime < 5 && tipper.isExtended()){
	    	tipper.retract();	
	    }
	    else if (autoTime >= 5 && autoTime < 7){
	    	autoDriveTrain.driveStraight(1, 0, Constants.AUTO_FORWARD_FULL_SPEED_TIME);
	    }
	    else if (autoTime >= 7 && autoTime < 7.5 && !tipper.isExtended()){
	    	autoElevator.setDesiredHeight(Constants.BOTTOM_LIMIT_SWITCH_HEIGHT);
	    }
	    else if (autoTime >= 7.5 && autoTime < 8 && tipper.isExtended()){
	    	tipper.extend();
	    }
	    else if (autoTime >= 8 && autoTime < 8.5 && tipper.isExtended()){
	    	autoElevator.setDesiredHeight(Constants.ELEVATOR_HEIGHT_FOR_BOTTOM_OF_FEEDER_STATION);
	    }	    	
	    else if (autoTime >= 8.5 && autoTime < 9 && tipper.isExtended()){
	    	tipper.retract();	
	    }
	    else if (autoTime >= 9 && autoTime < 13){
	    	autoDriveTrain.turnTo(-90, Constants.AUTO_TURN_FULL_SPEED_TIME);
	    }
	    else if (autoTime >= 13 && autoTime < 15 
	    		&& autoDriveTrain.gyroGetAngle() > -95 && autoDriveTrain.gyroGetAngle() < -85){
			autoDriveTrain.driveStraight(-0.3, -90, 1);
		}
		
	    else
		autoDriveTrain.drive(0 , 0);
		
	    
	    
	}
	//It takes in autoTime to have the robot do different things at specific times during auto
	public void twoRecyclingContainers(double autoTime){
		
	}
	//It takes in autoTime to have the robot do different things at specific times during auto
	public void driveWithRecyclingContainerToFeederStation(double autoTime){
		
	}
	
}
