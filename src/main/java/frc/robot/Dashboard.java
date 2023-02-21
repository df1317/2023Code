package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;

public class Dashboard {
    // default auto can be any auto, set in init
    private static final String defaultAuto = "default auto";
    private static final String firstAuto = "1st auto";
    private static final String secondAuto = "2nd auto";
    private String selectedAuto;
    private final SendableChooser<String> sendableChooser = new SendableChooser<>();
    
    // call this method in robotInit to put options on the dashboard
    public void dashboardSetup() {
        sendableChooser.setDefaultOption("Default Auto", defaultAuto);
        sendableChooser.addOption("First Auto", firstAuto);
        sendableChooser.addOption("Second Auto", secondAuto);
        SmartDashboard.putData("AUTO CHOICES", sendableChooser);
    }

    // call this method in autoInit to act upon selected autonomous
    public void dashboardAutoInit() {
        selectedAuto = sendableChooser.getSelected();
        System.out.println("SELECTED: " + selectedAuto);
    }
}
