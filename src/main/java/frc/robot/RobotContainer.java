package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Commands.CollectBalls;
import frc.robot.Commands.DriveJoystickCmd;
import frc.robot.Commands.FeedCmd;
import frc.robot.Commands.IntakeToggleCmd;
import frc.robot.Commands.ShootCmd;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.IntakeSubsystem;
import frc.robot.Subsystems.Shooter;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import java.util.Arrays;
import frc.robot.TrajectoryLoader;



public class RobotContainer {
    private Drivetrain drivetrainSubsystem = new Drivetrain();
    private IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    private Shooter shooterSubsystem = new Shooter();
    private TrajectoryLoader loader = new TrajectoryLoader();
    private final XboxController driverController = new XboxController(0);
    private final XboxController codriverController = new XboxController(1);

    private Trajectory trajectory = loader.loadTrajectory(); //Pathweaver Trajectory

    public RobotContainer(){
        drivetrainSubsystem.setDefaultCommand(new DriveJoystickCmd(drivetrainSubsystem, 
        () -> driverController.getRawAxis(1), 
        () -> driverController.getRawAxis(0),
        () -> driverController.getLeftTriggerAxis(),
        () -> driverController.getRightTriggerAxis()));

        configureButtonAction();
    }

    private void configureButtonAction() {
        new JoystickButton(driverController, 1).whileActiveOnce(new IntakeToggleCmd(intakeSubsystem, true));
        new JoystickButton(driverController, 2).whileActiveOnce(new IntakeToggleCmd(intakeSubsystem, false));
        new JoystickButton(driverController, 3).whileActiveContinuous(new CollectBalls(intakeSubsystem, true));
        new JoystickButton(driverController, 4).whileActiveContinuous(new CollectBalls(intakeSubsystem, false));
        new JoystickButton(codriverController, 1).whileActiveContinuous(new FeedCmd(intakeSubsystem, true));
        new JoystickButton(codriverController, 2).whileActiveContinuous(new FeedCmd(intakeSubsystem, false));
        new JoystickButton(codriverController, 6).whileActiveContinuous(new ShootCmd(shooterSubsystem));
    }

    //AUTO DRIVETRAIN
    public Command getAutonomousCommand() {
        /*
        TrajectoryConfig config = new TrajectoryConfig(
            Units.feetToMeters(2.0), Units.feetToMeters(2.0));
        config.setKinematics(drivetrainSubsystem.getKinematics());
    
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            Arrays.asList(new Pose2d(), new Pose2d(1.0, 0, new Rotation2d()),
                new Pose2d(0, 1.2, Rotation2d.fromDegrees(90.0))),
            config
        );*/


        RamseteCommand command = new RamseteCommand(
            trajectory,
            drivetrainSubsystem::getPose,
            new RamseteController(2, 0.7),
            drivetrainSubsystem.getFeedforward(),
            drivetrainSubsystem.getKinematics(),
            drivetrainSubsystem::getSpeeds,
            drivetrainSubsystem.getLeftPIDController(),
            drivetrainSubsystem.getRightPIDController(),
            drivetrainSubsystem::setOutputVolts,
            drivetrainSubsystem
        );
    
        return command.andThen(() -> drivetrainSubsystem.setOutputVolts(0, 0));
      }
    
      
    public void reset() {
        drivetrainSubsystem.reset();
    }
}
