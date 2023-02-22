package frc.robot;

import edu.wpi.first.wpilibj.ADIS16448_IMU;

public class Gyro {

    private static final ADIS16448_IMU gyro = new ADIS16448_IMU();

    public double gyroDriveAdjustment = 0;
    private final double gyroDeadzoneDegrees = 3;

    // Control drive system automatically,
    // driving in reverse direction of pitch/roll angle,
    // with a magnitude based upon the angle

    /** @return accumulated gyro pitch in degrees */
    public double getGyroPitch() {
        return gyro.getGyroAngleY();
    }

    /** @return accumulated gyro roll in degrees */
    public double getGyroRoll() {
        return gyro.getGyroAngleX();
    }

    /** @return accumulated gyro yaw in degrees */
    public double getGyroYaw() {
        return gyro.getGyroAngleZ();
    }

    /** @return instantaneous acceleration in X direction (front/back) in m/s/s */
    public double getAccelX() {
        return gyro.getAccelX();
    }

    /** @return instantaneous acceleration in Y direction (right/left) in m/s/s */
    public double getAccelY() {
        return gyro.getAccelY();
    }

    /** @return instantaneous acceleration in Z direction (up/down) in m/s/s */
    public double getAccelZ() {
        return gyro.getAccelZ();
    }

    public double gyroAdjust() {
        gyroDriveAdjustment = getGyroPitch() / 10.0;

        if (Math.abs(getGyroPitch()) > gyroDeadzoneDegrees) {
            return gyroDriveAdjustment;
        } else {
            return 0;
        }
    }

    public void reset() {
        gyro.reset();
        gyroDriveAdjustment = 0.0;
    }

    public void resetButton(boolean resetButton) {
        if (resetButton) {
            reset();
            // System.out.println("!! GYRO RESET !!");
        }
    }
}
