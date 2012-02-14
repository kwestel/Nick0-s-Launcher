import javax.swing.*;
import java.awt.*;

public class GuiForm_UpdaterForm extends GuiExtend_JFrame
{

    public JLabel Label_Status;
    public JLabel label_MainTitle;
    public JProgressBar ProgressBar;
    
    public GuiForm_UpdaterForm(boolean forceDownload, boolean updateAllJars)
    {
        super();
        
        setTitle("Nick0's Updater");
        setSize(275, 125);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(createFrameContent());
        setVisible(true);
        
        Thread_UpdateAllJars downloadThread = updateAllJars ? ( new Thread_UpdateAllJars(forceDownload, this) ) : ( new Thread_UpdateAllJars(this) );
        downloadThread.start();
    }

    private JPanel createFrameContent()
    {
        JPanel mainPanel = new JPanel();
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
