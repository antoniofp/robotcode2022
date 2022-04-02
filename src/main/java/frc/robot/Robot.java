
package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

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
  DifferentialDrive m_myRobot = new DifferentialDrive(rightMotors, leftMotors);

  private final DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);

  int leftBump2 = 0;
  int aBut = 0;
  int xBut = 0;
  int bBut = 0;
  int yBut = 0;
  int rightBump = 0;
  int leftBump = 0;
  double leftTriggerGas = 0;
  double rightTriggerGas = 0;
  double totalGas;
  
  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();
    doubleSolenoid.set(Value.kReverse);
    leftClimber.follow(rightClimber);
    leftClimber.stopMotor();
    rightClimber.stopMotor();
    m_backleftMotor.setIdleMode(IdleMode.kBrake);
    m_frontleftMotor.setIdleMode(IdleMode.kBrake);
    m_backrightMotor.setIdleMode(IdleMode.kBrake);
    m_frontrightMotor.setIdleMode(IdleMode.kBrake);
    leftClimber.setIdleMode(IdleMode.kBrake);
    rightClimber.setIdleMode(IdleMode.kBrake);
  }
  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
  }
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {

  }
  @Override
  public void teleopPeriodic() {
    double leftTriggerGas = m_driverController.getLeftTriggerAxis()*.5;
    double rightTriggerGas = m_driverController.getRightTriggerAxis()*.5;
    double totalGas = leftTriggerGas + rightTriggerGas;
    m_myRobot.arcadeDrive(m_driverController.getLeftX(), m_driverController.getLeftY()*totalGas);
    
    int leftBump2 = (m_driver2Controller.getLeftBumper()) ? 1 : 0;
    int aBut = (m_driver2Controller.getAButton()) ? 1 : 0;
    int xBut = (m_driver2Controller.getXButton()) ? 1 : 0;
    int bBut = (m_driver2Controller.getBButton()) ? 1 : 0;
    int yBut = (m_driver2Controller.getYButton()) ? 1 : 0;
    int rightBump = (m_driverController.getRightBumper()) ? 1 : 0;
    int leftBump = (m_driverController.getLeftBumper()) ? 1 : 0;
    shooter.set((leftBump2*0.8));
    intake.set(aBut*0.8);
    intake.set(xBut*-0.3);
    feeder.set(bBut*0.7);
    feeder.set(yBut*-0.2);
    rightClimber.set(rightBump*0.4);
    rightClimber.set(leftBump*-0.8);

    if (m_driver2Controller.getPOV() == 0) {
      doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    } else if (m_driver2Controller.getPOV() == 180) {
      doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }
}
