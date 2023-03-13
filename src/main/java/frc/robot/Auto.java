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

    public Auto(Drivetrain drivetrain, Limelight limelight, Dashboard dashboard, Arm arm) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.dashboard = dashboard;
        this.arm = arm;
    }

    // trajectory setup
    PathPlannerTrajectory A_LeaveCommunity = PathPlanner.loadPath("A_LeaveCommunity", new PathConstraints(3, 2));
    PathPlannerTrajectory A_Balance = PathPlanner.loadPath("A_Balance", new PathConstraints(1.0, 0.5));
    PathPlannerTrajectory B_Balance = PathPlanner.loadPath("B_Balance", new PathConstraints(4, 0.5));
    PathPlannerTrajectory C_Balance = PathPlanner.loadPath("C_Balance", new PathConstraints(1.0, 0.5));
    PathPlannerTrajectory C_LeaveCommunity = PathPlanner.loadPath("C_LeaveCommunity", new PathConstraints(1.0, 0.5));
 
    PathPlannerTrajectory straight = PathPlanner.loadPath("Straight", new PathConstraints(1.0, 0.5));

    private PathPlannerTrajectory selectedTrajectory;

    private final RamseteController m_ramseteController = new RamseteController();

    private Timer timer;

    public boolean finishedTrajectory;

    public void autonomousStartup(String trajectory) {
        drivetrain.resetEncoders();

        timer = new Timer();
        timer.start();
        finishedTrajectory = false;
        selectedTrajectory = PathPlannerTrajectory.transformTrajectoryForAlliance(chooseAutonomous(trajectory), (DriverStation.getAlliance() == DriverStation.Alliance.valueOf("Red") ) ? DriverStation.Alliance.valueOf("Blue") : DriverStation.Alliance.valueOf("Red"));

        drivetrain.resetOdometry(selectedTrajectory.getInitialPose());
    }

    public void runTrajectory() {
        if (timer.get() < selectedTrajectory.getTotalTimeSeconds()) {
            State desiredPose = selectedTrajectory.sample(timer.get());
            ChassisSpeeds refChassisSpeeds = m_ramseteController.calculate(drivetrain.getPose(), desiredPose);

            drivetrain.autoDrive(refChassisSpeeds.vxMetersPerSecond, refChassisSpeeds.omegaRadiansPerSecond);
            System.out.println(selectedTrajectory.getTotalTimeSeconds()-timer.get());

        } else {
            drivetrain.autoDrive(0, 0);
            System.out.println("FINISHED");
            finishedTrajectory = true;
        }
    }

   private PathPlannerTrajectory chooseAutonomous(String input) {
    switch (input){
        case Dashboard.A_LeaveComm:
            return A_LeaveCommunity;

        case Dashboard.A_Balance:
            return A_Balance;
        
        case Dashboard.B_Balance:
            return B_Balance;
        
        case Dashboard.C_LeaveComm:
            return C_LeaveCommunity;
    
        case Dashboard.C_Balance:
            return C_Balance;

        case Dashboard.defaultAuto:
        default:
          return straight;
      }
   }
}