
package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder.Type;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

public class Robot extends TimedRobot {
  final XboxController m_driverController = new XboxController(0);
  final XboxController m_driver2Controller = new XboxController(1);
  //Creating object for chassis
  Drivetrain drivetrain = new Drivetrain();
  //Creating objects for mechanims motors
  CANSparkMax shooter = new CANSparkMax(5, MotorType.kBrushless);
  CANSparkMax intake = new CANSparkMax (6, MotorType.kBrushless);
  CANSparkMax feeder = new CANSparkMax (7, MotorType.kBrushless);
  CANSparkMax leftClimber = new CANSparkMax (8, MotorType.kBrushless);
  CANSparkMax rightClimber = new CANSparkMax (9, MotorType.kBrushless);
  // Encoders
  RelativeEncoder rightClimbEncoder = rightClimber.getEncoder(Type.kHallSensor, 42);
  RelativeEncoder leftClimbEncoder = leftClimber.getEncoder(Type.kHallSensor, 42);
  // Solenoid valves
  final DoubleSolenoid doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
  final Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

  //PATHWEAVER
  String trajectoryJSON = "paths/StraightBack.wpilib.json";
  AHRS gyro = new AHRS(SPI.Port.kMXP);
  Trajectory trajectory = new Trajectory();

  @Override
  public void robotInit() { 
    
    CameraServer.startAutomaticCapture();
    doubleSolenoid.set(Value.kForward); //keep intake up
    rightClimber.follow(leftClimber);

    leftClimber.stopMotor();
    rightClimber.stopMotor(); 
    leftClimbEncoder.setPosition(0);
  }

  @Override
  public void autonomousInit() { 
    drivetrain.brakeChassis();
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() { 
    drivetrain.brakeChassis();
    leftClimber.setIdleMode(IdleMode.kBrake);
    rightClimber.setIdleMode(IdleMode.kBrake);
    leftClimbEncoder.setPosition(0);
  }

  @Override
  public void teleopPeriodic() { 
    double leftTriggerGas = m_driverController.getLeftTriggerAxis() *.5;
    double rightTriggerGas = m_driverController.getRightTriggerAxis() *.5;
    double totalGas = leftTriggerGas + rightTriggerGas;

// Take value of x and y coordinates from left joystick to control speed and rotation of drivetrain
    drivetrain.drive(m_driverController.getLeftX(), m_driverController.getLeftY(), totalGas);

// Compact if statement to asign speed values to each button
    double shooterSpeed = (m_driver2Controller.getLeftBumper()) ? 0.8 : 0;
    double intakeSpeed = (m_driver2Controller.getAButton()) ? 0.8 : 0;
    double reverseIntakeSpeed = (m_driver2Controller.getXButton()) ? -0.3 : 0;
    double feederSpeed = (m_driver2Controller.getBButton()) ? 0.7 : 0;
    double reverseFeederSpeed = (m_driver2Controller.getYButton()) ? -0.2 : 0;
    double climbUpSpeed = (m_driverController.getRightBumper()) ? 0.4 : 0;
    double climbDownSpeed = (m_driverController.getLeftBumper()) ? -0.8 : 0;

// Enable lock mechanism when climber reaches 34 revolutions
    boolean lockEnabled = false;
    if (leftClimbEncoder.getPosition() >= 34) {
      lockEnabled = true;
    }
// Speeds for motors are set using the previous variables
    shooter.set(shooterSpeed);
    intake.set(intakeSpeed + reverseIntakeSpeed);
    feeder.set(feederSpeed + reverseFeederSpeed);
    // if climber is within that range, it can move up or down
    if (leftClimbEncoder.getPosition() >= 1 & leftClimbEncoder.getPosition() <=34) {
      leftClimber.set(climbUpSpeed + climbDownSpeed);
    } // if the climber is below 1, and lock is not enabled, it can only go up
      else if (leftClimbEncoder.getPosition()<1 & !lockEnabled) {
        leftClimber.set(climbUpSpeed);
      // if climber lock gets enabled and it is below 34, it can move up or down, including below 0
      } else if (lockEnabled & leftClimbEncoder.getPosition() < 34) {
      leftClimber.set(climbDownSpeed + climbUpSpeed);
      } else { // if the climber is not in the previous cases, it means it is above 34, so it must only go down
      leftClimber.set(climbDownSpeed);
    }


    // Intake is lowered or elevated with D-Pad on controller
    if (m_driver2Controller.getPOV() == 0) {
    doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    if (m_driver2Controller.getPOV() == 180) {
      doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    if (m_driver2Controller.getRightBumperPressed()){
      compressor.disable();
    }
  }

  @Override
  public void disabledInit(){
    drivetrain.coastChassis();
  }
}