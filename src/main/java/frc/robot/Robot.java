package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Encoder;
import java.util.ArrayList;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathConstraints;
import java.nio.file.Path;
import java.nio.file.*;

public class Robot extends TimedRobot {

    public Drivetrain drivetrain = new Drivetrain();
    public Controllers controllers = new Controllers();
    public Gyro gyro = new Gyro();
    public Limelight limelight = new Limelight();
    public Auto auto = new Auto();

   
    @Override
    public void robotInit() {
        gyro.gyro.reset();
        // System.out.println(gyro.getGyroY());


        // TODO: make a method that resets encoders and sets distance per pulse (in drivetrain)
        drivetrain.m_leftEncoder.reset();
        drivetrain.m_rightEncoder.reset();
        drivetrain.m_leftEncoder.setDistancePerPulse(2 * Math.PI * drivetrain.kWheelRadius / drivetrain.kEncoderResolution);
        drivetrain.m_rightEncoder.setDistancePerPulse(2 * Math.PI * drivetrain.kWheelRadius / drivetrain.kEncoderResolution);

        auto.trajectoryInit();
    }

    @Override
    public void robotPeriodic() {
    }

    @Override
    public void autonomousInit() {
        auto.autonomousStartup(); 
    }

    @Override
    public void autonomousPeriodic() {
        auto.runAutonomous();
        
    }

    @Override
    public void teleopInit() {
        // TODO: set all motors to 0 here, 3 second wait period between auto and teleop for checking balance
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
        
        // Limelight testing
        limelight.updateLimelightVariables();
        SmartDashboard.putNumber("LL distance", limelight.calculateLimelightDistance());

        drivetrain.driveTeleop();

        //System.out.println("gyro " + gyro.gyroAdjust(gyro.getGyroY()));
        //System.out.println(leftDrive + " " + rightDrive);
 
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
