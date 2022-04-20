
package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
  RelativeEncoder leftEncoder =  m_frontleftMotor.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder rightEncoder = m_frontrightMotor.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder rightClimbEncoder = rightClimber.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder shootEncoder = shooter.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder feederEncoder = feeder.getEncoder(Type.kHallSensor, 42);
  // Create solenoid object
  private final DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  
  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture();
    //keep intake up by default
    doubleSolenoid.set(Value.kForward);
    leftClimber.follow(rightClimber);
    leftClimber.stopMotor();
    rightClimber.stopMotor(); 
  }
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Wheels Pos", leftEncoder.getPosition());
    SmartDashboard.putNumber("Right Wheels Pos", rightEncoder.getPosition());
    SmartDashboard.putNumber("Shooter Temp", shooter.getMotorTemperature());
    SmartDashboard.putNumber("Climber Pos", rightClimbEncoder.getPosition());
    SmartDashboard.putNumber("Intake Temp", intake.getMotorTemperature());
  }

  @Override
  public void autonomousInit() {
    // Set encoder positions to zero
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    shootEncoder.setPosition(0);
    feederEncoder.setPosition(0);
    m_backleftMotor.setIdleMode(IdleMode.kBrake);
    m_frontleftMotor.setIdleMode(IdleMode.kBrake);
    m_backrightMotor.setIdleMode(IdleMode.kBrake);
    m_frontrightMotor.setIdleMode(IdleMode.kBrake);
  }
  @Override
  public void autonomousPeriodic() {
    // Calculate average distance of both drivetrain sides
    double distance = (leftEncoder.getPosition() + rightEncoder.getPosition())/2;
    if (distance<10) {
      m_myRobot.tankDrive(-.7, -.7);
    }
    else {
      // accelerate shooter for 10 rotations to reach full speed
      while (shootEncoder.getPosition()<10 ) {
        shooter.set(0.8);
      }
      // keep shooter spinning and activate feeder for 5 rotations
      while (feederEncoder.getPosition()<5) {
      shooter.set(0.8);
      feeder.set(.7);
      } 
    }
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    shootEncoder.setPosition(0);
    // once robot has shoot, move out of tarmac doing 5 rotations
    if (distance<5){
      m_myRobot.tankDrive(-.7, -.7);
    }
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
    // Compact if statement to asign speed values to each button
    double shooterSpeed = (m_driver2Controller.getLeftBumper()) ? 0.8 : 0;
    double intakeSpeed = (m_driver2Controller.getAButton()) ? 0.8 : 0;
    double reverseIntakeSpeed = (m_driver2Controller.getXButton()) ? -0.3 : 0;
    double feederSpeed = (m_driver2Controller.getBButton()) ? 0.7 : 0;
    double reverseFeederSpeed = (m_driver2Controller.getYButton()) ? -0.2 : 0;
    double climbUpSpeed = (m_driverController.getRightBumper()) ? 0.4 : 0;
    double climbDownSpeed = (m_driverController.getLeftBumper()) ? -0.8 : 0;
    // Speeds for motors are set using the previous variables
    shooter.set(shooterSpeed);
    intake.set(intakeSpeed + reverseIntakeSpeed);
    feeder.set(feederSpeed + reverseFeederSpeed);
    rightClimber.set(climbUpSpeed + climbDownSpeed);
    // Intake is lowered or elevated with D-Pad on controller
    if (m_driver2Controller.getPOV() == 0) {
    doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    } else if (m_driver2Controller.getPOV() == 180) {
      doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
  }
}
