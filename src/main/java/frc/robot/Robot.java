
package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

public class Robot extends TimedRobot {
  private final XboxController m_driverController = new XboxController(0);
  private final XboxController m_driver2Controller = new XboxController(1);
  private CANSparkMax m_frontleftMotor = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax m_backleftMotor = new CANSparkMax(2, MotorType.kBrushless);
  private CANSparkMax m_frontrightMotor = new CANSparkMax(3, MotorType.kBrushless);
  private CANSparkMax m_backrightMotor = new CANSparkMax(4, MotorType.kBrushless);
  private CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushless);
  private CANSparkMax intake = new CANSparkMax (6, MotorType.kBrushless);
  private CANSparkMax feeder = new CANSparkMax (7, MotorType.kBrushless);
  private CANSparkMax rightClimber = new CANSparkMax (8, MotorType.kBrushless);
  private CANSparkMax leftClimber = new CANSparkMax (9, MotorType.kBrushless);
  private final MotorControllerGroup leftMotors = new MotorControllerGroup(m_frontleftMotor, m_backleftMotor);
  private final MotorControllerGroup rightMotors = new MotorControllerGroup(m_frontrightMotor, m_backrightMotor);
  // Create object for drive train
  DifferentialDrive m_myRobot = new DifferentialDrive(rightMotors, leftMotors);
  // Create encoder objects 
  RelativeEncoder leftEncoder = m_frontleftMotor.getEncoder();
  RelativeEncoder rightEncoder = m_frontrightMotor.getEncoder();
  // Create solenoid object
  private final DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

  // Variables for the state of buttons. By default is 0, when pressed it will change to 1
  int leftBump2 = 0;
  int aBut = 0;
  int xBut = 0;
  int bBut = 0;
  int yBut = 0;
  int rightBump = 0;
  int leftBump = 0;
  // Variables for speed control
  double leftTriggerGas = 0;
  double rightTriggerGas = 0;
  double totalGas;

  
  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();
    //keep intake up by default
    doubleSolenoid.set(Value.kReverse);
    leftClimber.follow(rightClimber);
    leftClimber.stopMotor();
    rightClimber.stopMotor(); 
  }
  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    // Set encoder positions to zero
    leftEncoder.setPosition(0);
  }
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    m_backleftMotor.setIdleMode(IdleMode.kBrake);
    m_frontleftMotor.setIdleMode(IdleMode.kBrake);
    m_backrightMotor.setIdleMode(IdleMode.kBrake);
    m_frontrightMotor.setIdleMode(IdleMode.kBrake);
    leftClimber.setIdleMode(IdleMode.kBrake);
    rightClimber.setIdleMode(IdleMode.kBrake);
  }
  @Override
  public void teleopPeriodic() {
    // Get values from triggers and add both to set multiplier for drivetrain speed
    double leftTriggerGas = m_driverController.getLeftTriggerAxis()*.5;
    double rightTriggerGas = m_driverController.getRightTriggerAxis()*.5;
    double totalGas = leftTriggerGas + rightTriggerGas;
    // Take value of x and y coordinates from left joystick to control speed and rotation of drivetrain
    m_myRobot.arcadeDrive(m_driverController.getLeftX()*totalGas, m_driverController.getLeftY()*totalGas);
    // Compact if statement to determine if a button is being pressed
    int leftBump2 = (m_driver2Controller.getLeftBumper()) ? 1 : 0;
    int aBut = (m_driver2Controller.getAButton()) ? 1 : 0;
    int xBut = (m_driver2Controller.getXButton()) ? 1 : 0;
    int bBut = (m_driver2Controller.getBButton()) ? 1 : 0;
    int yBut = (m_driver2Controller.getYButton()) ? 1 : 0;
    int rightBump = (m_driverController.getRightBumper()) ? 1 : 0;
    int leftBump = (m_driverController.getLeftBumper()) ? 1 : 0;
    // Speeds for motors are set multiplying a predetermined value by the state of the button
    shooter.set((leftBump2*0.8));
    intake.set(aBut*0.8);
    intake.set(xBut*-0.3);
    feeder.set(bBut*0.7);
    feeder.set(yBut*-0.2);
    rightClimber.set(rightBump*0.4);
    rightClimber.set(leftBump*-0.8);
    // Intake is lowered or elevated with D-Pad
    if (m_driver2Controller.getPOV() == 0) {
      doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    } else if (m_driver2Controller.getPOV() == 180) {
      doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }
}
