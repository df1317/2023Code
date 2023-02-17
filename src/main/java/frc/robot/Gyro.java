package frc.robot;

import edu.wpi.first.wpilibj.ADIS16448_IMU;

public class Gyro {

    private static final ADIS16448_IMU gyro = new ADIS16448_IMU();

    // Control drive system automatically,
    // driving in reverse direction of pitch/roll angle,
    // with a magnitude based upon the angle

    /** @return accumulated gyro pitch in degrees */
    public double getGyroX() {
        return gyro.getGyroAngleX();
    }

    /** @return accumulated gyro roll in degrees */
    public double getGyroY() {
        return gyro.getGyroAngleY();
    }

    /** @return accumulated gyro yaw in degrees */
    public double getGyroZ() {
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

    public double gyroAdjust(double gyroY) {
        double gyroDriveAdjustment = 0.0;
        if (gyroY > 5) {
            gyroDriveAdjustment = 1;
        } else if (gyroY < -5) {
            gyroDriveAdjustment = -1;
        } else {
            gyroDriveAdjustment = 0;
        }
        return gyroDriveAdjustment;
    }

    public void reset() {
        gyro.reset();
    }

}
