import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class GuiForm_GameFrame extends GuiExtend_JFrame
{

    public static GuiForm_GameFrame gameFrame;
    public Applet minecraftApplet;
    private boolean enhancedWindow = false;

    private GuiForm_GameFrame(String username)
    {
        super("Nick0's Launcher - Minecraft - " + username + " - Revision " + Main_RealLauncher.getLauncherRevision());
        setSizeModified = false;

        setSize(Preferences_ConfigLoader.CONFIG_WindowSizeX, Preferences_ConfigLoader.CONFIG_WindowSizeY);
        setLocationRelativeTo(null);

        addActionsListeners();

        gameFrame = this;

        if ( enhancedWindow )
        {
            setUndecorated(true);
        }
    }

    private void addActionsListeners()
    {
        WindowAdapter closeListener = new WindowAdapter() { public void windowClosing(WindowEvent arg0)
        {
            try
            {
                Main_RealLauncher.minecraftInstance.destroy();
                Main_RealLauncher.minecraftInstance.stop();
            }
            catch ( Throwable t ) { t.printStackTrace(); }
            System.exit(0);
        } };
        addWindowListener(closeListener);
    }

    public void addMinecraftToFrame(Applet minecraftApplet)
    {
        minecraftApplet.setSize(getWidth(), getHeight());
        setLayout(new BorderLayout());
        add(minecraftApplet, BorderLayout.CENTER);

        // setLayout(null);

        /*JPanel lolPanel = new JPanel();
        lolPanel.setBackground(Color.green);
        lolPanel.setBounds(0, 0, getWidth(), 25);
        add(lolPanel);*/

        // minecraftApplet.setBounds(0, 0, getWidth(), getHeight());
        // add(minecraftApplet);

        /*RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20);
        setShape(roundedRectangle);*/

        /*
        minecraftApplet.setSize(getWidth(), getHeight());

        JPanel panel = new JPanel() { public boolean isOptimizedDrawingEnabled() { return false; } };
        panel.setLayout(new OverlayLayout(panel));

        JPanel lolPanel = new JPanel();
        Dimension perfectDimension = new Dimension(minecraftApplet.getWidth(), 25);
        //lolPanel.setPreferredSize(perfectDimension);
        lolPanel.setMaximumSize(perfectDimension);
        //lolPanel.setMinimumSize(perfectDimension);
        //lolPanel.setSize(perfectDimension);

        lolPanel.setBackground(Color.green);
        lolPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lolPanel.setAlignmentY(0.0f);
        //lolPanel.set

        panel.add(lolPanel);
        panel.add(minecraftApplet);

        add(panel, BorderLayout.CENTER);

        // add(panelContainer, BorderLayout.CENTER);

        // add(minecraftApplet);

        MouseMotionListener drawing2 = new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent e)
            {
                int endX = e.getX();
                int endY = e.getY();
                // repaint();
                System.out.println("Mouse Position : " + endX + " / " + endY);
            }

            public void mouseMoved(MouseEvent e)
            {
                int endX = e.getX();
                int endY = e.getY();
                // repaint();
                System.out.println("Mouse Position : " + endX + " / " + endY);
            }
        };
        addMouseMotionListener(drawing2);
        */
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
