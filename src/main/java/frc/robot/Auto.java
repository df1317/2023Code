package frc.robot;

import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory.State;
import edu.wpi.first.wpilibj.Timer;

public class Auto {
    private Drivetrain drivetrain;
    private Limelight limelight;
    private static Gyro gyro = new Gyro();
    private Dashboard dashboard;

    public Auto(Drivetrain drivetrain, Limelight limelight, Dashboard dashboard) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.dashboard = dashboard;
    }

    // trajectory setup
    PathPlannerTrajectory simpleCurve6 = PathPlanner.loadPath("SimpleCurve6", new PathConstraints(1.5, 1));
    PathPlannerTrajectory secondCurve1 = PathPlanner.loadPath("SecondCurve1", new PathConstraints(1.0, 1));
    List<PathPlannerTrajectory> groupedPath = PathPlanner.loadPathGroup("GroupedPath", new PathConstraints(1.0, 1));

    private final RamseteController m_ramseteController = new RamseteController();

    private Timer timer;

    public void autonomousStartup() {
        gyro.reset();
        drivetrain.resetEncoders();

        timer = new Timer();
        timer.start();

        // drivetrain.resetOdometry(simpleCurve6.getInitialPose());
        drivetrain.resetOdometry(groupedPath.get(0).getInitialPose());
    }

    // default autonomous method, create new methods for each separate auto routine
    public void runFirstAutonomous() {

        drivetrain.updateOdometry();
        limelight.updateLimelightVariables();

        if (timer.get() < groupedPath.get(0).getTotalTimeSeconds()) {
            State desiredPose = groupedPath.get(0).sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);

            
        } else if (timer.get() < (3 + groupedPath.get(0).getTotalTimeSeconds()) && !limelight.limelightInAlignment() &&
            limelight.validLimelightTarget()) {
             
            System.out.println("align " + limelight.limelightInAlignment());
            System.out.println("valid " + limelight.validLimelightTarget());
             
            drivetrain.driveAutoLimelight();
            
        } else if (timer.get() < (4 + groupedPath.get(0).getTotalTimeSeconds())) {
            // drivetrain.setScoringMotor(0.5);

        } else if ((3 + groupedPath.get(0).getTotalTimeSeconds()) <= timer.get()
                && timer.get() < (6 + groupedPath.get(0).getTotalTimeSeconds())) {

            // drivetrain.setScoringMotor(0);

            drivetrain.resetOdometry(groupedPath.get(1).getInitialPose());
        } else if (timer.get() <= (groupedPath.get(1).getTotalTimeSeconds() + 6 + groupedPath.get(0).getTotalTimeSeconds())) {
            // drivetrain.setScoringMotor(0);

            State desiredPose = groupedPath.get(1).sample(timer.get() - (groupedPath.get(0).getTotalTimeSeconds() + 6));
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
        } else {
            // drivetrain.drive(drivetrain.gyroDrive(), drivetrain.gyroDrive());
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED AUTO");
        }
    }

    public void runDefaultAutonomous() {
        if (timer.get() < 2) {
            drivetrain.drive(0.5, 0.5);
        } else {
            drivetrain.drive(0, 0);
        }
    }

   public void runAutonomous() {
    switch (dashboard.getSelectedAuto()) {
        case Dashboard.firstAuto:
          runFirstAutonomous();
            break;

        case Dashboard.secondAuto:
            break;

        case Dashboard.defaultAuto:
        default:
          runDefaultAutonomous();
          break;
      }
   }
}