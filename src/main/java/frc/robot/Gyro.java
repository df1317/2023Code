package frc.robot;

// imports for AHRS Constants.gyro
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.ADIS16470_IMU.CalibrationTime;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class Gyro {

    static final double kOffBalanceAngleThresholdDegrees = 10;
    static final double kOonBalanceAngleThresholdDegrees  = 5;
    boolean autoBalanceXMode = true;
    boolean autoBalanceYMode = true;
    double xAxisRate;
    double yAxisRate;

    public Gyro(){
        System.out.println("why even");
    }

    public void updateAutoBalance() {
    if ( !autoBalanceXMode && 
             (Math.abs(Constants.gyro.getAngle()) >= 
                  Math.abs(kOffBalanceAngleThresholdDegrees))) {
                autoBalanceXMode = true;
            }
            else if ( autoBalanceXMode && 
                      (Math.abs(Constants.gyro.getAngle()) <= 
                       Math.abs(kOonBalanceAngleThresholdDegrees))) {
                autoBalanceXMode = false;
            }
    if ( !autoBalanceYMode && 
                 (Math.abs(Constants.gyro.getAngle()) >= 
                  Math.abs(kOffBalanceAngleThresholdDegrees))) {
                autoBalanceYMode = true;
            }
            else if ( autoBalanceYMode && 
                      (Math.abs(Constants.gyro.getAngle()) <= 
                       Math.abs(kOonBalanceAngleThresholdDegrees))) {
                autoBalanceYMode = false;
            }
            
        }
            // Control drive system automatically, 
            // driving in reverse direction of pitch/roll angle,
            // with a magnitude based upon the angle
        public double getGyroX() {
            /*if ( autoBalanceXMode ) {
                double pitchAngleRadians = Constants.gyro.getGyroAngleX() * (Math.PI / 180.0);
                xAxisRate = Math.sin(pitchAngleRadians) * -1;
            }*/
            return Constants.gyro.getGyroAngleX();
        }
        public double getGyroY() {
            /*if ( autoBalanceYMode ) {
                double rollAngleRadians = Constants.gyro.getGyroAngleY() * (Math.PI / 180.0);
                yAxisRate = Math.sin(rollAngleRadians) * -1;
            }*/
            return Constants.gyro.getGyroAngleY();
        }
            
}
