package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Encoder;

public class Robot extends TimedRobot {

    public Drivetrain drivetrain = new Drivetrain();
    public Controllers controllers = new Controllers();
    public Gyro gyro = new Gyro();
    public Limelight limelight = new Limelight();
    public Auto auto = new Auto();
    public DataSender dataSender;

   
    @Override
    public void robotInit() {
        gyro.gyro.reset();
        dataSender = new DataSender(drivetrain.getPose());
        dataSender.init();

        drivetrain.m_leftEncoder.reset();
        drivetrain.m_rightEncoder.reset();
        drivetrain.m_leftEncoder.setDistancePerPulse(2 * Math.PI * drivetrain.kWheelRadius / drivetrain.kEncoderResolution);
        drivetrain.m_rightEncoder.setDistancePerPulse(2 * Math.PI * drivetrain.kWheelRadius / drivetrain.kEncoderResolution);

        auto.trajectoryInit();
    }

    @Override
    public void robotPeriodic() {
        dataSender.update(drivetrain.getPose());
    }

    @Override
    public void autonomousInit() {
        gyro.gyro.reset();
        auto.autonomousStartup(); 
        drivetrain.m_leftEncoder.reset();
        drivetrain.m_rightEncoder.reset();
        
    }

    @Override
    public void autonomousPeriodic() {
        limelight.updateLimelightVariables();
        // System.out.println(drivetrain.m_leftEncoder.getDistance() + " " + drivetrain.m_rightEncoder.getDistance());
        System.out.println(limelight.limelightTV);
        if (!auto.finishedFirstTrajectory) {
            auto.runAutonomous();
            System.out.println("Running auto trajectory!!");
        } else if (!limelight.limelightInAlignment() && limelight.validLimelightTarget()) {
            drivetrain.driveAutoLimelight();
            System.out.println("Limelight auto aligning!!.");
        } else {
            drivetrain.drive(0, 0);
            System.out.println("FINISHED AUTO");
        }
        
    }

    @Override
    public void teleopInit() {
        dataSender.init();
    }

    @Override
    public void teleopPeriodic() {
        SmartDashboard.putNumber("X Pos", drivetrain.getPose().getX());
        SmartDashboard.putNumber("Y Pos", drivetrain.getPose().getY());
        SmartDashboard.putNumber("Gyro Z", gyro.getGyroZ());
        SmartDashboard.putNumber("Gyro X", gyro.getGyroX());
        SmartDashboard.putNumber("Gyro Y", gyro.getGyroY());
        SmartDashboard.putBoolean("Auto Balance", controllers.autoBalanceXMode);
        /*if (controllers.autoBalanceXMode) {
            drivetrain.rotateDegrees();
        } else {*/
        
        // Limelight testing
        limelight.updateLimelightVariables();
        SmartDashboard.putNumber("LL distance", limelight.calculateLimelightDistance());
        // testing only
        drivetrain.driveTeleop();
 
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
