import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GuiForm_GameFrame extends GuiExtend_JFrame
{

    public static GuiForm_GameFrame gameFrame;
    public Applet minecraftApplet;

    private GuiForm_GameFrame(String username)
    {
        super("Nick0's Launcher - Minecraft - " + username + " - Revision " + Main_RealLauncher.getLauncherRevision());
        setSizeModified = false;

        setSize(Preferences_ConfigLoader.CONFIG_WindowSizeX, Preferences_ConfigLoader.CONFIG_WindowSizeY);
        setLocationRelativeTo(null);

        addActionsListeners();

        gameFrame = this;
    }

    private void addActionsListeners()
    {
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent arg0)
        {
            Main_RealLauncher.minecraftInstance.stop();
            Main_RealLauncher.minecraftInstance.destroy();
            System.exit(0);
        } });
    }

    public void addMinecraftToFrame(Applet minecraftApplet)
    {
        minecraftApplet.setSize(getWidth(), getHeight());

        setLayout(new BorderLayout());
        add((this.minecraftApplet=minecraftApplet), "Center");
    }

    public static GuiForm_GameFrame newForm(boolean visible, String username)
    {
        gameFrame = ( gameFrame == null ) ? ( new GuiForm_GameFrame(username) ) : gameFrame;
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(visible);
        return gameFrame;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
