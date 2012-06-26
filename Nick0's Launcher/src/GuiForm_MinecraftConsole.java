import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GuiForm_MinecraftConsole extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Vars
    
    public JTextPane consoleDisplay;
    public static GuiForm_MinecraftConsole minecraftConsole = null;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiForm_MinecraftConsole()
    {
        super();

        setTitle("Nick0's Launcher - Console");
        setSize(600, 400);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JScrollPane(createConsole()));
        addActionsListeners();

        setVisible(Preferences_ConfigLoader.CONFIG_ShowConsoleOnStartup);
        if ( SystemTray_MinecraftIcon.CheckBox_displayConsole != null ) { SystemTray_MinecraftIcon.CheckBox_displayConsole.setState(Preferences_ConfigLoader.CONFIG_ShowConsoleOnStartup); }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System Functions

    private void addActionsListeners()
    {
        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    private void onClose()
    {
        setVisible(false);
        SystemTray_MinecraftIcon.CheckBox_displayConsole.setState(false);
    }

    private JTextPane createConsole()
    {
        consoleDisplay = new JTextPane();
        consoleDisplay.setEditable(false);

        return consoleDisplay;
    }

    public void setVisible(boolean visible)
    {
        if ( visible ) { setLocationRelativeTo(null); }
        super.setVisible(visible);
    }
    
    public static void updateTextPane(final String text) { SwingUtilities.invokeLater(new Runnable() { public void run()
    {
        if ( minecraftConsole == null ) { minecraftConsole = new GuiForm_MinecraftConsole(); }

        Document doc = minecraftConsole.consoleDisplay.getDocument();
        try { doc.insertString(doc.getLength(), text + "\n", null); }
        catch ( BadLocationException e ) { throw new RuntimeException(e); }
        minecraftConsole.consoleDisplay.setCaretPosition(doc.getLength() - 1);
    } }); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
