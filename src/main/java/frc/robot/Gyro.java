package frc.robot;

// imports for AHRS Constants.gyro
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.ADIS16448_IMU.CalibrationTime;
//import edu.wpi.first.wpilibj.ADIS16448_IMU.IMUAxis;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.ADIS16448_IMU;

public class Gyro {

    public static final ADIS16448_IMU gyro = new ADIS16448_IMU();

    double xAxisRate;
    double yAxisRate;

    public Gyro(){
        System.out.println("why even");
    }


            // Control drive system automatically, 
            // driving in reverse direction of pitch/roll angle,
            // with a magnitude based upon the angle

        /** @return accumulated gyro pitch in degrees */
        public double getGyroX() {
            /*if ( autoBalanceXMode ) {
                double pitchAngleRadians = Constants.gyro.getGyroAngleX() * (Math.PI / 180.0);
                xAxisRate = Math.sin(pitchAngleRadians) * -1;
            }*/
            return gyro.getGyroAngleX();
        }

        /** @return accumulated gyro roll in degrees */
        public double getGyroY() {
            /*if ( autoBalanceYMode ) {
                double rollAngleRadians = Constants.gyro.getGyroAngleY() * (Math.PI / 180.0);
                yAxisRate = Math.sin(rollAngleRadians) * -1;
            }*/
            return gyro.getGyroAngleY();
        }

        /** @return accumulated gyro yaw in degrees */
        public double getGyroZ() {
            /*if ( autoBalanceYMode ) {
                double rollAngleRadians = Constants.gyro.getGyroAngleY() * (Math.PI / 180.0);
                yAxisRate = Math.sin(rollAngleRadians) * -1;
            }*/
            return gyro.getGyroAngleZ();
        }

        /** @return instantaneous acceleration in X direction (front/back) in m/s/s*/
        public double getAccelX(){
            return gyro.getAccelX();
        }

        /** @return instantaneous acceleration in Y direction (right/left) in m/s/s*/
        public double getAccelY(){
            return gyro.getAccelY();
        }

        /** @return instantaneous acceleration in Z direction (up/down) in m/s/s*/
        public double getAccelZ(){
            return gyro.getAccelZ();
        }
            
        public double gyroAdjust(double gyroY) {
            double gyroDriveAdjustment = 0.0;
            if (gyroY > 5) {
                gyroDriveAdjustment = 0.25;
            } else if (gyroY < -5) {
                gyroDriveAdjustment = -0.25;
            } else {
                gyroDriveAdjustment = 0;
            }
            return gyroDriveAdjustment;
        }


}
