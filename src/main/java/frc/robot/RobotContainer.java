package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrajectoryParameterizer.TrajectoryGenerationException;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;

import java.util.Arrays;
import frc.robot.Drivetrain;


public class RobotContainer {
    private Drivetrain drivetrain = new Drivetrain();

    public Command getAutonomousCommand() {
        TrajectoryConfig config = new TrajectoryConfig(
            Units.feetToMeters(2.0), Units.feetToMeters(2.0));
        config.setKinematics(drivetrain.getKinematics());
    
        Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            Arrays.asList(new Pose2d(), new Pose2d(1.0, 0, new Rotation2d()),
                new Pose2d(0, 1.2, Rotation2d.fromDegrees(90.0))),
            config
        );
    
        RamseteCommand command = new RamseteCommand(
            trajectory,
            drivetrain::getPose,
            new RamseteController(2, 0.7),
            drivetrain.getFeedforward(),
            drivetrain.getKinematics(),
            drivetrain::getSpeeds,
            drivetrain.getLeftPIDController(),
            drivetrain.getRightPIDController(),
            drivetrain::setOutputVolts,
            drivetrain
        );
    
        return command.andThen(() -> drivetrain.setOutputVolts(0, 0));
      }
    
      public void reset() {
        drivetrain.reset();
      }
}
