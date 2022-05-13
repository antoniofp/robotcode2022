package frc.robot.Subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drivetrain extends SubsystemBase {

  private static final double kGearRatio = 7.29;
  private static final double kWheelRadiusInches = 3.0;

  CANSparkMax leftMaster = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
  CANSparkMax rightMaster = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);

  CANSparkMax leftSlave = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
  CANSparkMax rightSlave = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);

  private final MotorControllerGroup leftMotors = new MotorControllerGroup(leftMaster, leftSlave);
  private final MotorControllerGroup rightMotors = new MotorControllerGroup(rightMaster, rightSlave);
  // Create object for drive train
  DifferentialDrive chassis = new DifferentialDrive(rightMotors, leftMotors);

  AHRS gyro = new AHRS(SPI.Port.kMXP);

  DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(28));
  DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(getHeading());

  SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.3, 1.96, 0.06);

  PIDController leftPIDController = new PIDController(2.95, 0, 0);
  PIDController rightPIDController = new PIDController(2.95, 0, 0);

  Pose2d pose = new Pose2d();

  public Drivetrain() {
    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    leftMaster.setInverted(false);
    rightMaster.setInverted(true);

    gyro.reset();
  }

  //METHODS FOR INITS
  public void brakeChassis(){
    leftMaster.setIdleMode(IdleMode.kBrake);
    leftSlave.setIdleMode(IdleMode.kBrake);
    rightMaster.setIdleMode(IdleMode.kBrake);
    rightSlave.setIdleMode(IdleMode.kBrake);
  }

  public void coastChassis(){
    leftMaster.setIdleMode(IdleMode.kCoast);
    leftSlave.setIdleMode(IdleMode.kCoast);
    rightMaster.setIdleMode(IdleMode.kCoast);
    rightSlave.setIdleMode(IdleMode.kCoast);
  }

  //METHOD FOR TELEOPERATED
  public void drive(double x, double y, double triggerLeft, double triggerRight){
      double gas = triggerLeft * 0.5 + triggerRight * 0.5;
      chassis.arcadeDrive(x * gas, y * gas);
  }

  public Rotation2d getHeading() {
    return Rotation2d.fromDegrees(-gyro.getAngle());
  }

  public DifferentialDriveWheelSpeeds getSpeeds() {
    return new DifferentialDriveWheelSpeeds(
        leftMaster.getEncoder().getVelocity() / kGearRatio * 2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches) / 60,
        rightMaster.getEncoder().getVelocity() / kGearRatio * 2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches) / 60
    );
  }

  public double[] getWheelSpeeds(){
    return new double[] {
        leftMaster.getEncoder().getVelocity() / kGearRatio * 2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches) / 60,
        rightMaster.getEncoder().getVelocity() / kGearRatio * 2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches) / 60
    };
  }

  public DifferentialDriveKinematics getKinematics() {
    return kinematics;
  }

  public Pose2d getPose() {
    return pose;
  }

  public SimpleMotorFeedforward getFeedforward() {
    return feedforward;
  }

  public PIDController getLeftPIDController() {
    return leftPIDController;
  }

  public PIDController getRightPIDController() {
    return rightPIDController;
  }

  public void setOutputVolts(double leftVolts, double rightVolts) {
    leftMaster.set(leftVolts / 12);
    rightMaster.set(rightVolts / 12);
  }

  public void reset() {
    odometry.resetPosition(new Pose2d(), getHeading());
  }

  @Override
  public void periodic() {
    double [] speedArray = new double [2];
    speedArray = getWheelSpeeds();
    pose = odometry.update(getHeading(), speedArray[0], speedArray[1]);
  }
}
