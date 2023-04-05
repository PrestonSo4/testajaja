package frc.robot.Commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.spline.CubicHermiteSpline;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Subsystems.TankDriveSystem;

public class AutoBalanceCommand extends CommandBase{
    private boolean finished = false;
    private TankDriveSystem m_driveSystem;
    private double rate;
    private Timer timer = new Timer();
    private double cycle = 1;

    public AutoBalanceCommand(TankDriveSystem m_driveSystem, double rate) {
        this.m_driveSystem = m_driveSystem = m_driveSystem;
        this.rate = rate;
        addRequirements(m_driveSystem);
    }

    @Override
    public void initialize() {
        timer.start();
    }

    @Override
    public void execute() {
        double executeRate = rate  / cycle;
        if (MathUtil.applyDeadband(m_driveSystem.getGyroAngle() + 2.5, 2) > 0) {
            m_driveSystem.tankDrive(executeRate, executeRate, false);
        } else if (MathUtil.applyDeadband(m_driveSystem.getGyroAngle() + 2.5, 2) < 0) {
            m_driveSystem.tankDrive(-executeRate, -executeRate, false);
        } else {
            m_driveSystem.tankDrive(0, 0, false);
            if (timer.get() >= 0.5) {
                cycle += 0.5;
                timer.reset();
            }
        }
        // new WaitCommand(0.5).schedule();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_driveSystem.tankDrive(0, 0, false);
    }
}
