package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {
    private final double DRIVE_DEADZONE = 0;
    private final Joystick joyE = new Joystick(2);
    public final Joystick joyL = new Joystick(0);
    private final Joystick joyR = new Joystick(1);
    public final boolean autoBalanceXMode = joyE.getRawButton(1);

    // temporary button for temporary testing temporary temporary!.
    public boolean LLalignButton = joyL.getRawButton(3);

    public double getLeftDrive() {
        return insideDeadzone(joyL.getY(), DRIVE_DEADZONE);
    }

    public double getRightDrive() {
        return insideDeadzone(joyR.getY(), DRIVE_DEADZONE);
    }

    private double drivePrecision(double joystickVal) {
        if (joystickVal >= 0) {
            return Math.pow(joystickVal, 2);
        } else {
            return -Math.pow(joystickVal, 2);
        }
    }

    private double insideDeadzone(double joystickVal, double driveDeadzone) {
        return Math.abs(joystickVal) > driveDeadzone ? joystickVal : 0;
    }

}
