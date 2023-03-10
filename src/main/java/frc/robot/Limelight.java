package frc.robot;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Limelight {

    private double limelightTV;
    private double limelightTA;
    private double limelightTX;
    private double limelightTY;

    private double limelightKP = -0.05;
    public double limelightMinCommand = 0.1;

    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    public double limelightInAlignment = 0;

    public Limelight() {
        updateLimelightVariables();

        SmartDashboard.putNumber("Valid Target?", limelightTV);
        SmartDashboard.putNumber("Limelight tx", limelightTX);
        SmartDashboard.putNumber("Limelight ty", limelightTY);
        SmartDashboard.putNumber("Limelight ta", limelightTA);
    }

    public boolean validLimelightTarget() {
        if (limelightTV == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void updateLimelightVariables() {
        limelightTV = table.getEntry("tv").getDouble(0);
        limelightTA = table.getEntry("ta").getDouble(0);
        limelightTX = table.getEntry("tx").getDouble(0);
        limelightTY = table.getEntry("ty").getDouble(0);
    }

    public double calculateLimelightDistance() {
        updateLimelightVariables();
        final double limelightMountingHeight = 0.0; // inches
        final double limelightMountingAngle = 23; // degrees rotated back from vertical apparently
        final double limelightTargetHeight = 8; // inches

        double limelightTotalAngle = Math.toRadians(limelightMountingAngle + limelightTY);

        // Limelight distance from target in inches
        double limelightDistance = (limelightTargetHeight - limelightMountingHeight) / Math.atan(limelightTotalAngle);
        return limelightDistance;
    }

    public double calculateLimelightAngle() {
        updateLimelightVariables();
        return limelightTX;
    }

    public double limelightSteeringAlign() {
        double limelightHeadingError = -limelightTX;
        double limelightAlignmentAdjust = 0.0;

        if (limelightTX > 1.0) {
            limelightAlignmentAdjust = (limelightKP * limelightHeadingError) - limelightMinCommand;
        } else if (limelightTX < -1.0) {
            limelightAlignmentAdjust = (limelightKP * limelightHeadingError) + limelightMinCommand;
        }

        return limelightAlignmentAdjust;
    }

    public boolean limelightInAlignment() {
        return (Math.abs(limelightSteeringAlign()) < limelightMinCommand);
    }

    public void configLimelight() {
        // Forces led on
        table.getEntry("ledMode").setNumber(3);
        // Sets limelight's current pipeline to 8
        table.getEntry("pipeline").setNumber(8);
        // Sets the mode of the camera to vision processor mode
        table.getEntry("camMode").setNumber(0);
        // Defaults Limelight's snapshotting feature to off
        table.getEntry("snapshot").setNumber(0);
    }
}