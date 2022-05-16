package frc.robot.Commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.IntakeSubsystem;

public class CollectBalls extends CommandBase {
    private final IntakeSubsystem intakeSubsystem;
    private final boolean mode;

    public CollectBalls(IntakeSubsystem intakeSubsystem, boolean mode){
        this.intakeSubsystem = intakeSubsystem;
        this.mode = mode;
        addRequirements(intakeSubsystem); //Avoids conflict due to simultaneous use of intakeSubsystem
    }
    
    @Override
    public void initialize(){ //Runs once every time command is called
    }

    @Override
    public void execute(){
        if(mode){intakeSubsystem.collectBalls();}
        else{intakeSubsystem.returnBall();}
    }

    @Override
    public void end(boolean interreupted){
        intakeSubsystem.setOff();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
