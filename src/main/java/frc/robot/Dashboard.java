package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.cameraserver.CameraServer;

public class Dashboard {
    // default auto can be any auto, set in init
    public static final String defaultAuto = "straight";
    public static final String A_LeaveComm = "A_LeaveComm";
    public static final String A_Balance = "A_Balance";
    public static final String B_Balance = "B_Balance";
    public static final String C_LeaveComm = "C_LeaveComm";
    public static final String C_Balance = "C_Balance";
    

    private String selectedAuto;
    private final SendableChooser<String> sendableChooser = new SendableChooser<>();
    
    // call this method in robotInit to put options on the dashboard
    public void dashboardSetup() {
        sendableChooser.setDefaultOption("Straight", defaultAuto);
        sendableChooser.addOption("A_LeaveComm", A_LeaveComm);
        sendableChooser.addOption("A_Balance", A_Balance);
        sendableChooser.addOption("B_Balance", B_Balance);
        sendableChooser.addOption("C_LeaveComm", C_LeaveComm);
        sendableChooser.addOption("C_Balance", C_Balance);
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