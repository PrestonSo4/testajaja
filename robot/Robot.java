package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.RetractArmCommand;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;
  private AddressableLED m_LEDLight = new AddressableLED(Constants.OperatorConstants.kLEDLightsChannel);
  private AddressableLEDBuffer m_LEDBuffer = new AddressableLEDBuffer(Constants.OperatorConstants.kLEDLightsLength);
  private VerifyJoysticks m_verifyJoysticks = new VerifyJoysticks();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    m_robotContainer.initDashboard();
    SmartDashboard.putBoolean("Get Cube", true);
    SmartDashboard.putBoolean("Lights On", true);
    SmartDashboard.putBoolean("Use AutoBalance", false);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    m_verifyJoysticks.VerifyJoysticksPeriodically();
  }

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    CommandScheduler.getInstance().cancelAll();
    m_robotContainer.putShuffleBoardAutoCommands();
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    m_autonomousCommand.schedule();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    CommandScheduler.getInstance().cancelAll();
    m_robotContainer.configureBindings();

    m_robotContainer.putShuffleBoardAutoCommands();
    m_robotContainer.updateDashBoard();

    new RetractArmCommand(m_robotContainer.m_armSystem).schedule();

    m_LEDLight.setLength(m_LEDBuffer.getLength());
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    if (SmartDashboard.getBoolean("Lights On", true)) {
      if (SmartDashboard.getBoolean("Get Cube", true)) {

        for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
          m_LEDBuffer.setRGB(i, 0, 255, 255);
        }
        m_LEDLight.setData(m_LEDBuffer);
        m_LEDLight.start();
      } else {
        for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
          m_LEDBuffer.setRGB(i, 170, 255, 0);
        }
        m_LEDLight.setData(m_LEDBuffer);
        m_LEDLight.start();
      }
    } else {
        for (var i = 0; i < m_LEDBuffer.getLength(); i++) {
          m_LEDBuffer.setRGB(i, 0, 0, 0);
        }
        m_LEDLight.setData(m_LEDBuffer);
        m_LEDLight.start();
    }
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
  }

  @Override
  public void disabledInit() {
      CommandScheduler.getInstance().cancelAll();
      m_robotContainer.m_driveSystem.calibrate();
  }
}
