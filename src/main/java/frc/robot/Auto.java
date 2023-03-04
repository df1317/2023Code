package frc.robot;

import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory.State;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;

public class Auto {
    private Drivetrain drivetrain;
    private Limelight limelight;
    private static Gyro gyro = new Gyro();
    private Dashboard dashboard;
    private Arm arm;

    public Auto(Drivetrain drivetrain, Limelight limelight, Dashboard dashboard) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.dashboard = dashboard;
        this.arm = arm;
    }

    // trajectory setup
    PathPlannerTrajectory Blue_ACubeLeaveCommunity = PathPlanner.loadPath("ACubeLeaveCommunity", new PathConstraints(1.0, 0.5));
    PathPlannerTrajectory Blue_ACubeBalance = PathPlanner.loadPath("ACubeBalance", new PathConstraints(1.0, 0.5));
    PathPlannerTrajectory Blue_BCubeBalance = PathPlanner.loadPath("BCubeBalance", new PathConstraints(1.0, 0.5));
    PathPlannerTrajectory Blue_CCubeBalance = PathPlanner.loadPath("CCubeBalance", new PathConstraints(1.0, 0.5));
    PathPlannerTrajectory Blue_CCubeLeaveCommunity = PathPlanner.loadPath("CCubeLeaveCommunity", new PathConstraints(1.0, 0.5));
    
    PathPlannerTrajectory Red_ACubeLeaveCommunity = PathPlannerTrajectory.transformTrajectoryForAlliance(Blue_ACubeLeaveCommunity, DriverStation.Alliance.valueOf("Red"));
    PathPlannerTrajectory Red_ACubeBalance = PathPlannerTrajectory.transformTrajectoryForAlliance(Blue_ACubeBalance, DriverStation.Alliance.valueOf("Red"));
    PathPlannerTrajectory Red_BCubeBalance = PathPlannerTrajectory.transformTrajectoryForAlliance(Blue_BCubeBalance, DriverStation.Alliance.valueOf("Red"));
    PathPlannerTrajectory Red_CCubeBalance = PathPlannerTrajectory.transformTrajectoryForAlliance(Blue_CCubeBalance, DriverStation.Alliance.valueOf("Red"));
    PathPlannerTrajectory Red_CCubeLeaveCommunity = PathPlannerTrajectory.transformTrajectoryForAlliance(Blue_CCubeLeaveCommunity, DriverStation.Alliance.valueOf("Red"));

    List<PathPlannerTrajectory> groupedPath = PathPlanner.loadPathGroup("GroupedPath", new PathConstraints(1.0, 1));
    PathPlannerTrajectory straight = PathPlanner.loadPath("Straight", new PathConstraints(1.0, 0.5));

    private final RamseteController m_ramseteController = new RamseteController();

    private Timer timer;

    public void autonomousStartup() {
        gyro.reset();
        drivetrain.resetEncoders();

        timer = new Timer();
        timer.start();

        // TODO: getInitialPose() of selected autonomous
        // drivetrain.resetOdometry(simpleCurve6.getInitialPose());
        drivetrain.resetOdometry(straight.getInitialPose());
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

    public void runStraightAutonomous() {
        if (timer.get() < straight.getTotalTimeSeconds()) {
            State desiredPose = straight.sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
            System.out.println(straight.getTotalTimeSeconds()-timer.get());

        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED");
        }
    }

    public void runDefaultAutonomous() {
        if (drivetrain.getRightEncoder() < 3) {
            drivetrain.drive(0.5, 0.5);
        } else {
            drivetrain.drive(0, 0);
        }
    }

    public void runBlueACubeLeaveCommunity() {
        if (timer.get() < Blue_ACubeLeaveCommunity.getTotalTimeSeconds()) {
            State desiredPose = Blue_ACubeLeaveCommunity.sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
            System.out.println(Blue_ACubeLeaveCommunity.getTotalTimeSeconds()-timer.get());

        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED");
        }
    }

    public void runBlueBlueACubeBalance() {
        if (timer.get() < Blue_ACubeBalance.getTotalTimeSeconds()) {
            State desiredPose = Blue_ACubeBalance.sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
            System.out.println(Blue_ACubeBalance.getTotalTimeSeconds()-timer.get());

        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED");
        }
    }

    public void runBlueBCubeBalance() {
        if (timer.get() < Blue_BCubeBalance.getTotalTimeSeconds()) {
            State desiredPose = Blue_BCubeBalance.sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
            System.out.println(Blue_BCubeBalance.getTotalTimeSeconds()-timer.get());

        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED");
        }
    }

    public void runBlueCCubeLeaveCommunity() {
        if (timer.get() < Blue_CCubeLeaveCommunity.getTotalTimeSeconds()) {
            State desiredPose = Blue_CCubeLeaveCommunity.sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
            System.out.println(Blue_CCubeLeaveCommunity.getTotalTimeSeconds()-timer.get());

        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED");
        }
    }

    public void runBlueCCubeBalance() {
        if (timer.get() < Blue_CCubeBalance.getTotalTimeSeconds()) {
            State desiredPose = Blue_CCubeBalance.sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
            System.out.println(Blue_CCubeBalance.getTotalTimeSeconds()-timer.get());

        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED");
        }
    }

   public void runAutonomous() {
    switch (dashboard.getSelectedAuto()) {
        case Dashboard.A_Cube_Score_LeaveComm:
          arm.highScoreCube();
          if (arm.continueToTrajectory) {
            runDefaultAutonomous();
          }
            break;

        case Dashboard.secondAuto:
            runStraightAutonomous();
            break;

        case Dashboard.defaultAuto:
        default:
          runDefaultAutonomous();
          break;
      }
   }
}