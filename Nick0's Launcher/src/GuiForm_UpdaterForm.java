import javax.swing.*;
import java.awt.*;

public class GuiForm_UpdaterForm extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Vars
    
    public JLabel Label_Status;
    public JLabel label_MainTitle;
    public JProgressBar ProgressBar;
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    
    public GuiForm_UpdaterForm(boolean forceDownload, boolean updateAllJars, boolean startGame)
    {
        super();
        
        setTitle("Nick0's Updater");
        setSize(275, 125);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(createFrameContent());
        setVisible(true);
        
        Thread_UpdateMinecraft downloadThread;
        downloadThread = (new Thread_UpdateMinecraft(forceDownload, updateAllJars, startGame, this));
        downloadThread.start();
    }
    
    public GuiForm_UpdaterForm(String downloadURL, String jarFileName, boolean startGame)
    {
        super();
        
        setTitle("Nick0's Updater");
        setSize(275, 125);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(createFrameContent());
        setVisible(true);
        
        Thread_UpdateMinecraft downloadThread;
        downloadThread = (new Thread_UpdateMinecraft(downloadURL, jarFileName, startGame, this));
        downloadThread.start();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System Functions

    private JPanel createFrameContent()
    {
        JPanel mainPanel = new GuiElement_Panel("updater.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        label_MainTitle = new JLabel("<html><b><u>Mise à jour de Minecraft en cours...</u></b></html>");
        Label_Status = new JLabel("minecraft.jar");
        ProgressBar = new JProgressBar();

        // MAIN TITLE
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 40);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(label_MainTitle, gbc);

        // Progress Bar
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(ProgressBar, gbc);

        // Label Status
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.CENTER;
        gbc.insets = new Insets(2, 0, 0, 0);
        mainPanel.add(Label_Status, gbc);

        return mainPanel;
    }
    
    public void updateStatus(int value, String actualStatus)
    {
        ProgressBar.setValue(value);
        Label_Status.setText(actualStatus);
    }
    
    public void downloadFinished()
    {
        setVisible(false);
        Main_RealLauncher.startMinecraft();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
