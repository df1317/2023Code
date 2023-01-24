package frc.robot;

// imports for AHRS gyro
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Gyro {

    AHRS gyro = new AHRS(SPI.Port.kMXP);
    
    static final double kOffBalanceAngleThresholdDegrees = 10;
    static final double kOonBalanceAngleThresholdDegrees  = 5;
    boolean autoBalanceXMode;
    boolean autoBalanceYMode;
    double xAxisRate;
    double yAxisRate;

    public void updateAutoBalance() {
    if ( !autoBalanceXMode && 
             (Math.abs(gyro.getPitch()) >= 
                  Math.abs(kOffBalanceAngleThresholdDegrees))) {
                autoBalanceXMode = true;
            }
            else if ( autoBalanceXMode && 
                      (Math.abs(gyro.getPitch()) <= 
                       Math.abs(kOonBalanceAngleThresholdDegrees))) {
                autoBalanceXMode = false;
            }
    if ( !autoBalanceYMode && 
                 (Math.abs(gyro.getPitch()) >= 
                  Math.abs(kOffBalanceAngleThresholdDegrees))) {
                autoBalanceYMode = true;
            }
            else if ( autoBalanceYMode && 
                      (Math.abs(gyro.getPitch()) <= 
                       Math.abs(kOonBalanceAngleThresholdDegrees))) {
                autoBalanceYMode = false;
            }
            
        }
            // Control drive system automatically, 
            // driving in reverse direction of pitch/roll angle,
            // with a magnitude based upon the angle
        public double getGyroX() {
            if ( autoBalanceXMode ) {
                double pitchAngleRadians = gyro.getPitch() * (Math.PI / 180.0);
                xAxisRate = Math.sin(pitchAngleRadians) * -1;
            }
            return xAxisRate;
        }
        public double getGyroY() {
            if ( autoBalanceYMode ) {
                double rollAngleRadians = gyro.getRoll() * (Math.PI / 180.0);
                yAxisRate = Math.sin(rollAngleRadians) * -1;
            }
            return yAxisRate;
        }
            
}
