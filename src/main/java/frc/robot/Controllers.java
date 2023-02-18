package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {
    private final double DRIVE_DEADZONE = 0.05;
    private final Joystick joyE = new Joystick(2);
    private final Joystick joyL = new Joystick(0);
    private final Joystick joyR = new Joystick(1);

    // temporary button for temporary testing temporary temporary!.
    public boolean LLalignButton = joyL.getRawButton(3);

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
        return joyL.getRawButton(3);
    }

    public boolean getAutoBalance() {
        return joyL.getRawButton(4);
    }

    public boolean gyroResetButton() {
        return joyL.getRawButton(5);
    }

}
