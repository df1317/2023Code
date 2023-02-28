package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {

    public Controllers controllers;
    public Gyro gyro = new Gyro();
    public Limelight limelight;
    public Drivetrain drivetrain;
    public Auto auto;
    public Arm arm;
    public DataSender dataSender;
    public LED led;
    public Claw claw;
    public Dashboard dashboard;

    @Override
    public void robotInit() {
        controllers = new Controllers();
        limelight = new Limelight();
        drivetrain = new Drivetrain(controllers, limelight);
        auto = new Auto(drivetrain, limelight, dashboard);
        arm = new Arm(controllers);
        dataSender = new DataSender(drivetrain.getPose());
        led = new LED();
        claw = new Claw(controllers);
        dashboard = new Dashboard();

       dataSender.init();
       gyro.reset();
       drivetrain.resetEncoders();
       led.initLED();
       dashboard.dashboardSetup();
       arm.resetEncoders();
    }

    @Override
    public void robotPeriodic() {
        drivetrain.updateOdometry();
        dataSender.update(drivetrain.getPose());
        led.runLED();
        controllers.update();
    }

    @Override
    public void autonomousInit() {
        dashboard.dashboardAutoInit();
        auto.autonomousStartup();
    }

    @Override
    public void autonomousPeriodic() {
        auto.runStraightAutonomous();
    }

    @Override
    public void teleopInit() {
        dataSender.init();
        drivetrain.resetEncoders();
        drivetrain.gearshiftInit();
        // TODO: set all motors to 0 here, 3 second wait period between auto and teleop
        // for checking balance
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.drive(controllers.getLeftDrive(), controllers.getRightDrive(), 0.5);
        SmartDashboard.putNumber("X Pos", drivetrain.getPose().getX());
        SmartDashboard.putNumber("Y Pos", drivetrain.getPose().getY());
        SmartDashboard.putNumber("Gyro Z", gyro.getGyroYaw());
        SmartDashboard.putNumber("Gyro X", gyro.getGyroPitch());
        SmartDashboard.putNumber("Gyro Y", gyro.getGyroRoll());

        // Limelight testing
        // limelight.updateLimelightVariables();
        // SmartDashboard.putNumber("LL distance", limelight.calculateLimelightDistance());
        // testing only

        // TODO: run limelight alignment commands in teleop

        drivetrain.driveTeleop();

        drivetrain.gearshift();

        gyro.resetButton(controllers.gyroResetButton());
        
        arm.runArmCommands();

        claw.runClawCommands();

        arm.temporaryEncoderTesting();

        // System.out.println("L " + drivetrain.getLeftRate());
        // System.out.println("R " + drivetrain.getRightRate());
    
    //    System.out.println("L " + drivetrain.getLeftEncoder());
    //    System.out.println("R " + drivetrain.getRightEncoder());

    System.out.println(drivetrain.getLeftEncoder());
   if (drivetrain.getLeftEncoder() < 1) {
            drivetrain.autoDrive(-0.5, 0);
        } else {
            drivetrain.drive(0, 0);
        }
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
