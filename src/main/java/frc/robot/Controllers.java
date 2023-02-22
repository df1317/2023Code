package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controllers {
    private final double DRIVE_DEADZONE = 0.05;
    private final Controller joyE = new Controller(2);
    private final Controller joyL = new Controller(0);
    private final Controller joyR = new Controller(1);
    public final boolean autoBalanceXMode = joyE.getRawButton(1);

    // temporary button for temporary testing temporary temporary!.
    public boolean LLalignButton = joyL.getRawButton(3);

    public void update(){
        joyE.update();
        joyL.update();
        joyR.update();
    }

    public double getLeftDrive() {
        return -insideDeadzone(joyL.getY(), DRIVE_DEADZONE);
    }

    public double getRightDrive() {
        return -insideDeadzone(joyR.getY(), DRIVE_DEADZONE);
    }

    private double insideDeadzone(double joystickVal, double driveDeadzone) {
        return Math.abs(joystickVal) > driveDeadzone ? joystickVal : 0;
    }

    public boolean getLimelightAutoAlign() {
        return joyL.onButtonPress(9);
    }

    public boolean getAutoBalance() {
        return joyL.onButtonPress(4);
    }

    public boolean gyroResetButton() {
        return joyL.onButtonPress(5);
    }

    public double getTurretRotation() {
        return -joyE.getZ();
    }

    public double getAxisRotation() {
        System.out.println(joyE.getY());
        return joyE.getY();
    }

    public boolean extendButton() {
        System.out.println("6" + "" + joyE.getRawButton(6));
        return joyE.getRawButton(6);
    }

    public boolean retractButton() {
        System.out.println("4" + "" + joyE.getRawButton(4));
        return joyE.getRawButton(4);
    }

    public boolean grabCubeButton() {
        return joyE.onButtonPress(5);
    }

    public boolean grabConeButton() {
        return joyE.onButtonPress(3);
    }

    public boolean releaseButton() {
        return joyE.getRawButton(2);
    }

    public boolean gearshiftButtonLeft() {
        return joyL.getRawButton(6);
    }

    public boolean gearshiftButtonRight() {
        return joyR.getRawButton(6);
    }

    public boolean downshiftButtonLeft() {
        return joyL.getRawButton(4);
    }

    public boolean downshiftButtonRight() {
        return joyR.getRawButton(4);
    }

    public boolean crazyLEDLights() {
        return joyE.onButtonPress(1);
    }
}
