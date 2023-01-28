package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;

public class Robot extends TimedRobot {

    public Drivetrain drivetrain = new Drivetrain();
    public Controllers controllers = new Controllers();
    public Gyro gyro = new Gyro();
    public Limelight limelight = new Limelight();
    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    @Override
    public void robotInit() {
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        SmartDashboard.putNumber("Gyro X", gyro.getGyroX());
        //System.out.println(gyro.gyro.getAngle());
        SmartDashboard.putNumber("Gyro Y", gyro.getGyroY());
        SmartDashboard.putBoolean("Auto Balance", controllers.autoBalanceXMode);
        /*if (controllers.autoBalanceXMode) {
            drivetrain.rotateDegrees();
        } else {*/

        //drivetrain.drive(controllers.getLeftDrive(), controllers.getRightDrive(), 1);
        
        // Limelight testing
        limelight.updateLimelightVariables();
        SmartDashboard.putNumber("LL distance", limelight.calculateLimelightDistance());

        // testing only 
        double leftDrive;
        double rightDrive;

        if (controllers.joyL.getRawButton(3)) {
            leftDrive = -limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());
            rightDrive = limelight.limelightSteeringAlign(limelight.calculateLimelightAngle());
            System.out.println(limelight.calculateLimelightAngle());
        } else {
            leftDrive = controllers.getLeftDrive();
            rightDrive = controllers.getRightDrive();
        }

        drivetrain.drive(leftDrive, rightDrive);

    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void simulationInit() {
    }

    @Override
    public void simulationPeriodic() {
    }
}
