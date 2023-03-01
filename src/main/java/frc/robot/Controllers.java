package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {
    private final double DRIVE_DEADZONE = 0.05;
    private final double TURRET_DEADZONE = 0.1;
    private final double AXIS_DEADZONE = 0.1;

    private final Controller joyE = new Controller(0);
    private final Controller joyL = new Controller(1);
    private final Controller joyR = new Controller(2);
    public final boolean autoBalanceXMode = joyE.getButtonOutput(1);

    // temporary button for temporary testing temporary temporary!.
    public boolean LLalignButton = joyL.getButtonOutput(3);

    public void update(){
        joyE.update();
        joyL.update();
        joyR.update();
    }

    private double insideDeadzone(double joystickVal, double driveDeadzone) {
        return Math.abs(joystickVal) > driveDeadzone ? joystickVal : 0;
    }

    public double getLeftDrive() {
        double direction = (joyL.getY() > 0) ? 1 : -1;
        return insideDeadzone(joyL.getY(), DRIVE_DEADZONE);
    }

    public double getRightDrive() {
        double direction = (joyR.getY() > 0) ? 1 : -1;
        return insideDeadzone(joyR.getY(), DRIVE_DEADZONE);
    }

    public boolean getLimelightAutoAlign() {
        return joyL.onButtonPress(9);
    }

    public boolean getAutoBalanceLeft() {
        return joyL.getButtonOutput(2);
    }

    public boolean getAutoBalanceRight() {
        return joyR.getButtonOutput(2);
    }

    public boolean gyroResetButton() {
        return joyL.onButtonPress(5);
    }

    public double getTurretRotation() {
        double direction = (joyE.getZ() > 0) ? 1 : -1;
        return insideDeadzone(joyE.getZ(), TURRET_DEADZONE);
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
        return joyE.onButtonPress(5);
    }

    public boolean grabConeButton() {
        return joyE.onButtonPress(3);
    }

    public boolean releaseButton() {
        return joyE.onButtonPress(2);
    }

    public boolean gearshiftButtonLeft() {
        return joyL.onButtonPress(6);
    }

    public boolean gearshiftButtonRight() {
        return joyR.onButtonPress(6);
    }

    public boolean downshiftButtonLeft() {
        return joyL.onButtonPress(4);
    }

    public boolean downshiftButtonRight() {
        return joyR.onButtonPress(4);
    }

    public boolean turretTrigger() {
        return joyE.getRawButton(1);
    }
}
