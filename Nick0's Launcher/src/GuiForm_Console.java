import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

public class GuiForm_Console
{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Cool Fields & Blabla...

    public JTabbedPane mainTabbedPain;
    public JPanel mainPanel;

    // JRVME Panel
    public JPanel JRVMEPanel;
    public JTextField JRVMEFieldInput;
    public JScrollPane JRVMEScrollPane;
    public JTextPane JRVMETextPane;

    public JScrollPane ConsoleScrollPane;
    public JTextPane ConsoleTextPane;

    public static JFrame rawJavaFrame = null;
    public static GuiForm_Console frameContent = null;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public Usable Functions

    public static void verifyAndInitializeConsole() { if ( rawJavaFrame == null ) { createForm(); } }

    public static void updateConsoleContent(final String text) { SwingUtilities.invokeLater(new Runnable() { public void run()
    {
        verifyAndInitializeConsole();

        Document textPaneDocument = frameContent.ConsoleTextPane.getDocument();

        try { textPaneDocument.insertString(textPaneDocument.getLength(), text + "\n", null); }
        catch ( Exception e ) { throw new RuntimeException(e); }

        frameContent.ConsoleTextPane.setCaretPosition(textPaneDocument.getLength() - 1);
    } }); }

    public static void updateJRVMEContent(final String text) { SwingUtilities.invokeLater(new Runnable() { public void run()
    {
        verifyAndInitializeConsole();

        frameContent.JRVMETextPane.setText(JRVME_System.messageStart + text);
    } }); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Internal Frame Functions

    private static void createForm()
    {
        rawJavaFrame = new JFrame("Nick0's Launcher - Console")
        {
            public void setVisible(boolean visible)
            {
                if ( visible ) { setLocationRelativeTo(null); }
                super.setVisible(visible);
            }
        };

        frameContent = new GuiForm_Console();

        rawJavaFrame.setContentPane(frameContent.mainPanel);

        setFrameProperties();
        setContentProperties();
        addActionListeners();

        rawJavaFrame.setVisible(Preferences_ConfigLoader.CONFIG_ShowConsoleOnStartup);
        if ( SystemTray_MinecraftIcon.CheckBox_displayConsole != null ) { SystemTray_MinecraftIcon.CheckBox_displayConsole.setState(Preferences_ConfigLoader.CONFIG_ShowConsoleOnStartup); }
    }

    private static void setContentProperties()
    {
        frameContent.JRVMETextPane.setBackground(Color.black);
        frameContent.JRVMETextPane.setForeground(Color.white);
        frameContent.JRVMETextPane.setCaretColor(Color.white);

        frameContent.ConsoleTextPane.setBackground(Color.black);
        frameContent.ConsoleTextPane.setForeground(Color.white);
        frameContent.ConsoleTextPane.setCaretColor(Color.white);

        frameContent.JRVMEFieldInput.setBackground(Color.black);
        frameContent.JRVMEFieldInput.setForeground(Color.white);
        frameContent.JRVMEFieldInput.setCaretColor(Color.white);

        JRVME_System.displayWelcomeMessage();
    }

    private static void setFrameProperties()
    {
        rawJavaFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        rawJavaFrame.pack();
        rawJavaFrame.setSize(600, 350);
        rawJavaFrame.setLocationRelativeTo(null);
        rawJavaFrame.setResizable(true);
    }

    private static void addActionListeners()
    {
        WindowListener closeListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        rawJavaFrame.addWindowListener(closeListener);

        frameContent.mainTabbedPain.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Component selectedComponent = frameContent.mainTabbedPain.getSelectedComponent();
                rawJavaFrame.setTitle((selectedComponent == frameContent.ConsoleScrollPane) ? "Nick0's Launcher - Console" : "Nick0's Launcher - J.R.V.M.E");
            }
        });

        ActionListener loginListener = new ActionListener() { public void actionPerformed(ActionEvent e)
        {
            String textCommand = frameContent.JRVMEFieldInput.getText();
            frameContent.JRVMEFieldInput.setText("");
            JRVME_System.commandReceived(textCommand);
        } };
        frameContent.JRVMEFieldInput.addActionListener(loginListener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Java Hooks

    private static void onClose()
    {
        rawJavaFrame.setVisible(false);
        SystemTray_MinecraftIcon.CheckBox_displayConsole.setState(false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // By Nicnl - nicnl25@gmail.com
}
