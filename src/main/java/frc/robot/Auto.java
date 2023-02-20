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

    public Auto(Drivetrain drivetrain, Limelight limelight) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
    }

    // trajectory setup
    PathPlannerTrajectory simpleCurve6 = PathPlanner.loadPath("SimpleCurve6", new PathConstraints(1.5, 1));
    PathPlannerTrajectory secondCurve1 = PathPlanner.loadPath("SecondCurve1", new PathConstraints(1.0, 1));
    List<PathPlannerTrajectory> groupedPath = PathPlanner.loadPathGroup("GroupedPath", new PathConstraints(1.0, 1));

    private final RamseteController m_ramseteController = new RamseteController();

    public boolean finishedFirstTrajectory;
    public boolean finishedAligning;

    private Timer timer;
    public double subtractTime;

    public void autonomousStartup() {
        gyro.reset();
        drivetrain.resetEncoders();

        // timer for first trajectory
        timer = new Timer();
        timer.start();

        finishedFirstTrajectory = false;
        finishedAligning = false;

        // drivetrain.resetOdometry(simpleCurve6.getInitialPose());
        drivetrain.resetOdometry(groupedPath.get(0).getInitialPose());
    }

    public void runAutonomous() {

        drivetrain.updateOdometry();
        limelight.updateLimelightVariables();

        if (timer.get() < groupedPath.get(0).getTotalTimeSeconds()) {
            State desiredPose = groupedPath.get(0).sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);

            /*
             * } else if (!finishedAligning && !limelight.limelightInAlignment() &&
             * limelight.validLimelightTarget()) {
             * 
             * // System.out.println("align " + limelight.limelightInAlignment());
             * // System.out.println("valid " + limelight.validLimelightTarget());
             * 
             * drivetrain.driveAutoLimelight();
             */
        } else if (timer.get() < (2.5 + groupedPath.get(0).getTotalTimeSeconds())) {
            finishedAligning = true;
            // drivetrain.setScoringMotor(0.5);

        } else if ((2.5 + groupedPath.get(0).getTotalTimeSeconds()) <= timer.get()
                && timer.get() < (4 + groupedPath.get(0).getTotalTimeSeconds())) {
            finishedAligning = true;

            // drivetrain.setScoringMotor(0);

            drivetrain.resetOdometry(groupedPath.get(1).getInitialPose());
        } else if (timer
                .get() <= (groupedPath.get(1).getTotalTimeSeconds() + 4 + groupedPath.get(0).getTotalTimeSeconds())) {
            // drivetrain.setScoringMotor(0);

            State desiredPose = groupedPath.get(1).sample(timer.get() - (groupedPath.get(0).getTotalTimeSeconds() + 4));
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED AUTO");
        }
    }

    // TODO: use me in the real auto code
    public void balance() {
        drivetrain.drive(gyro.gyroAdjust(), gyro.gyroAdjust(), 0.5);
    }
}