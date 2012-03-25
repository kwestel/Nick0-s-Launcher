import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GuiForm_AlternativeJar extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default Vars

    public static GuiElement_Panel mainPanel;
    
    public JLabel Label_MainTitle;
    public GuiElement_Button Button_DownloadButton;
    public GuiElement_Button Button_CloseButton;
    public GuiElement_AlternativeJarSelector ComboBox_JarSelector;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiForm_AlternativeJar()
    {
        super();

        setTitle("Minecraft Alternatifs");

        setSize(300, 200);
        setResizable(false);

        setContentPane(createFrameContent());
        addActionsListeners();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Frame Creation

    private JPanel createFrameContent()
    {
        mainPanel = new GuiElement_Panel("alternativeJar.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label_MainTitle = new JLabel("<html><h1><b><u>Minecraft Alternatifs</u></b></h1></html>");
        ComboBox_JarSelector = new GuiElement_AlternativeJarSelector();
        Button_DownloadButton = new GuiElement_Button("<html><b>Télécharger</b></html>");
        Button_CloseButton = new GuiElement_Button("Fermer");

        // Default GridBagLayout Value
        gbc.gridheight = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Label_MainTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(ComboBox_JarSelector, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(Button_DownloadButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(Button_CloseButton, gbc);

        return mainPanel;
    }

    private void addActionsListeners()
    {
        ActionListener downloadButtonListener = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            newForm(false);
            new GuiForm_UpdaterForm(ComboBox_JarSelector.getSelectedJarURL(), ComboBox_JarSelector.getSelectedJarFileName());
        } };
        Button_DownloadButton.addActionListener(downloadButtonListener);

        ActionListener closeButtonListener = new ActionListener() { public void actionPerformed(ActionEvent arg0) { onClose(); } };
        Button_CloseButton.addActionListener(closeButtonListener);
        
        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // On Closing Window

    private void onClose()
    {
        newForm(false);
        GuiForm_PreferenceFrame.newForm(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Functions

    private static GuiForm_AlternativeJar AlternativeJarForm = null;

    public static GuiForm_AlternativeJar newForm(boolean visible)
    {
        AlternativeJarForm = ( AlternativeJarForm == null ) ? ( new GuiForm_AlternativeJar() ) : AlternativeJarForm;
        AlternativeJarForm.setLocationRelativeTo(null);
        AlternativeJarForm.setVisible(visible);
        return AlternativeJarForm;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
