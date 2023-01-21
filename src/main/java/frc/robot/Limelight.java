package frc.robot;

import edu.wpi.first.networktables.*;

public class Limelight {

    private double limelightTV;
    private double limelightTA;
    private double limelightTX;
    private double limelightTY;

    public Limelight() {
        updateLimelightVariables();

        // SmartDashboard.putNumber("Valid Target?", limelightTV);
        // SmartDashboard.putNumber("Limelight tx", limelightTX);
        // SmartDashboard.putNumber("Limelight ty", limelightTY);
        // SmartDashboard.putNumber("Limelight ta", limelightTA);
    }

    private void updateLimelightVariables () {
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

    public void configLimelight() {
        // Forces led on
        table.getEntry("ledMode").setNumber(3);
        // Sets limelight's current pipeline to 0
        table.getEntry("pipeline").setNumber(0);
        // Sets the mode of the camera to vision processor mode
        table.getEntry("camMode").setNumber(0);
        // Defaults Limelight's snapshotting feature to off
        table.getEntry("snapshot").setNumber(0);
    }
}


