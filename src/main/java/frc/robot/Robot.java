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
    //public DataSender dataSender;
    public LED led;
    public Claw claw;
    public Dashboard dashboard;

    private int i;

    @Override
    public void robotInit() {
        controllers = new Controllers();
        limelight = new Limelight();
        drivetrain = new Drivetrain(controllers, limelight);
        claw = new Claw(controllers);
        arm = new Arm(controllers, claw, drivetrain);
        //dataSender = new DataSender(drivetrain.getPose());
        led = new LED();
        dashboard = new Dashboard();
        auto = new Auto(drivetrain, limelight, dashboard, arm);

        //dataSender.init();
        gyro.reset();
        drivetrain.resetEncoders();
        led.initLED();
        dashboard.dashboardSetup();
        arm.resetEncoders();
        dashboard.cameraInit();
        drivetrain.gearshiftInit();

        i = 0;
    }

    @Override
    public void robotPeriodic() {
        drivetrain.updateOdometry();
        //dataSender.update(drivetrain.getPose());
        led.runLED();
        controllers.update();

        if (controllers.gyroResetButton()) {
            gyro.reset();
        }
    }

    @Override
    public void autonomousInit() {
        gyro.reset();
        i=0;
        arm.resetEncoders();
        dashboard.dashboardAutoInit();
        drivetrain.gearshiftInit();
        auto.autonomousStartup(dashboard.getSelectedAuto());
        drivetrain.resetEncoders();
        // claw.grabCube();
    }

    @Override
    public void autonomousPeriodic() {
        switch (i) {
            case 0:
            arm.highScoreCube();
            if (arm.continueToTrajectory) {
                auto.autonomousStartup(dashboard.getSelectedAuto());
                i++;
            }
            break;
            
            case 1:
            arm.highScoreCube();
            auto.runTrajectory();
            if (auto.finishedTrajectory) {
                i++;
            }
            break;
            
            case 2:
            drivetrain.drive(drivetrain.gyroDrive(), drivetrain.gyroDrive());
            System.out.println(drivetrain.gyroDrive());
            break;
        }

    }

    @Override
    public void teleopInit() {
        Drivetrain.balanced = false;
        //dataSender.init();
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
        Drivetrain.balanced = controllers.rainbowLEDButton();
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

        //arm.temporaryEncoderTesting();
        // temporary if statements, remove us
    
        //System.out.println(arm.axisEncoderGet());

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
