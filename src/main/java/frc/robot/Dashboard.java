package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.cameraserver.CameraServer;

public class Dashboard {
    // default auto can be any auto, set in init
    public static final String defaultAuto = "default auto";
    public static final String firstAuto = "1st auto";
    public static final String secondAuto = "2nd auto";
    public static final String cube_Score = "cube score";
    public static final String A_Cube_Score_LeaveComm = "A_Cube_Score_LeaveComm";
    public static final String A_Cube_Score_Balance = "A_Cube_Score_Balance";
    public static final String A_Balance = "A_Balance";
    private String selectedAuto;
    private final SendableChooser<String> sendableChooser = new SendableChooser<>();
    
    // call this method in robotInit to put options on the dashboard
    public void dashboardSetup() {
        sendableChooser.setDefaultOption("Default Auto", defaultAuto);
        sendableChooser.addOption("First Auto", firstAuto);
        sendableChooser.addOption("Second Auto", secondAuto);
        SmartDashboard.putData("AUTO CHOICES", sendableChooser);
    }

    public String getSelectedAuto() {
        selectedAuto = sendableChooser.getSelected();
        return selectedAuto;
    }

    // call this method in autoInit to act upon selected autonomous
    public void dashboardAutoInit() {
        getSelectedAuto();
        System.out.println("SELECTED: " + selectedAuto);
    }

    public void cameraInit() {
        CameraServer.startAutomaticCapture();
        CameraServer.startAutomaticCapture();
    }
}