package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {
    private final double DRIVE_DEADZONE = 0.05;
    private final Controller joyE = new Controller(2);
    private final Controller joyL = new Controller(0);
    private final Controller joyR = new Controller(1);
    public final boolean autoBalanceXMode = joyE.getButtonOutput(1);

    // temporary button for temporary testing temporary temporary!.
    public boolean LLalignButton = joyL.getButtonOutput(3);

    public double getLeftDrive() {
        return insideDeadzone(joyL.getY(), DRIVE_DEADZONE);
    }

    public double getRightDrive() {
        return insideDeadzone(joyR.getY(), DRIVE_DEADZONE);
    }

    private double insideDeadzone(double joystickVal, double driveDeadzone) {
        return Math.abs(joystickVal) > driveDeadzone ? joystickVal : 0;
    }

    public boolean getLimelightAutoAlign() {
        return joyL.getButtonOutput(3);
    }

    public boolean getAutoBalance() {
        return joyL.getButtonOutput(4);
    }

    public boolean gyroResetButton() {
        return joyL.getButtonOutput(5);
    }

    public double getTurretRotation() {
        return joyE.getZ();
    }

    public double getAxisRotation() {
        return joyE.getY();
    }

    public boolean extendButton() {
        return joyE.getButtonOutput(6);
    }

    public boolean retractButton() {
        return joyE.getButtonOutput(4);
    }

    public boolean grabCubeButton() {
        return joyE.getButtonOutput(5);
    }

    public boolean grabConeButton() {
        return joyE.getButtonOutput(3);
    }

    public boolean realeaseClawButton() {
        return joyE.getButtonOutput(2);
    }

    public boolean crazyLEDLights() {
        return joyE.getButtonOutput(1);
    }
}
