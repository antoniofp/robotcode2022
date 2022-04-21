package frc.robot;

public class Auto {
    double distance;
    double KP = 0.3;
    double KI = 0.05;

    public double calculatePid(double traveled , double dist){
        distance = dist;
        double error = distance - traveled;
        double output = error * KP;
        return output;
    }

    public double rotateTo(double angle, double actualAngle){
        
        return angle;
    }

}
