package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {
    private final double DRIVE_DEADZONE = 0.1;
    private final Joystick joyE = new Joystick(0);
    private final Joystick joyL = new Joystick(1);
    private final Joystick joyR = new Joystick(2);

    public double getLeftDrive() {
        return insideDeadzone(joyL.getY(), DRIVE_DEADZONE);
    }

    public double getRightDrive() {
        return insideDeadzone(joyR.getY(), DRIVE_DEADZONE);
    }

    private double drivePrecision(double joystickVal) {
        if (joystickVal >= 0) {
            return joystickVal * joystickVal;
        } else {
            return joystickVal * joystickVal * -1;
        }
    }

    private double insideDeadzone(double joystickVal, double driveDeadzone) {
        return Math.abs(joystickVal) > driveDeadzone ? joystickVal : 0;
    }

}
