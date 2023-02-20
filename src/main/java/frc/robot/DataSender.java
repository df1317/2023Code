package frc.robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Filesystem;

public class DataSender {

    private Properties vals;
    private File propFile;
    private static final double k_inchesPerMeter = 39.3700787402;

    public DataSender(Pose2d pose) {
        vals = new Properties();
        init();
    }

    public void init() {
        propFile = new File(Filesystem.getDeployDirectory() + "/KinematicsVals.txt");
        System.out.println(propFile.setWritable(true, false));
    }

    public void update(Pose2d pose) {
        vals.setProperty("x", "" + pose.getX() * k_inchesPerMeter * 2);
        vals.setProperty("y", "" + pose.getY() * k_inchesPerMeter * 2);
        vals.setProperty("theta", "" + pose.getRotation().getDegrees());

        try {
            vals.store(new FileWriter(propFile), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}