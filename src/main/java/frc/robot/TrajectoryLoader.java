package frc.robot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;

public class TrajectoryLoader {
    String trajectoryJSON = "paths/StraightBack.wpilib.json";

    public Trajectory loadTrajectory(){
        Trajectory trajectory = new Trajectory();
    try {
        Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
        trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
    } catch (IOException ex) {
        DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
    }
    return trajectory;
  }
}
