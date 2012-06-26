import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GuiForm_LWJGLForm extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default Vars

    public static GuiElement_Panel mainPanel;

    public JLabel Label_MainTitle;
    public JLabel Label_Warning;

    public GuiElement_CheckBox Check_Alpha;
    public GuiElement_CheckBox Check_Beta;
    public GuiElement_CheckBox Check_RC;

    public GuiElement_Button Button_Validate;

    public GuiElement_LWJGLSelector ComboBox_LWJGLSelector;

    private boolean versionValidated = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiForm_LWJGLForm()
    {
        super();

        setTitle("Nick0's Launcher - LWJGL");

        setResizable(false);

        setContentPane(createFrameContent());
        addActionsListeners();

        changeSize();
        setLocationRelativeTo(null);
    }

    private void changeSize()
    {
        pack();
        setSize(getWidth()+20, getHeight()+10);
        validate();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Frame Creation

    private JPanel createFrameContent()
    {
        mainPanel = new GuiElement_Panel("lwjgl.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label_MainTitle = new JLabel("<html><h1><b><u>LWJGL Selector</u></b></h1></html>");
        Label_Warning = new JLabel("<html><u><b>Toutes les version ne peuvent<br/>pas être installées.</b></u></html>");
        ComboBox_LWJGLSelector = new GuiElement_LWJGLSelector();
        Check_Alpha = new GuiElement_CheckBox("Alpha");
        Check_Beta = new GuiElement_CheckBox("Beta");
        Check_RC = new GuiElement_CheckBox("RC");
        Button_Validate = new GuiElement_Button("Valider");

        Button_Validate.setEnabled(false);

        // Default GridBagLayout Value
        gbc.gridheight = 1;

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 6, 0);
        mainPanel.add(Label_MainTitle, gbc);

        // ComboBox : LWJGL Selector
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(ComboBox_LWJGLSelector, gbc);

        // CheckBox : Check Alpha
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Check_Alpha, gbc);

        // CheckBox : Check Beta
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Check_Beta, gbc);

        // CheckBox : Check RC
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Check_RC, gbc);

        // Button : Valider
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(25, 0, 0, 0);
        mainPanel.add(Button_Validate, gbc);

        // Label : Attention
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(Label_Warning, gbc);

        return mainPanel;
    }

    private void addActionsListeners()
    {
        ItemListener checkOfflineListener = new ItemListener() { public void itemStateChanged(ItemEvent e)
        {
            Button_Validate.setEnabled(false);
            ComboBox_LWJGLSelector.updateLWJGLVersions(Check_Alpha.isSelected(), Check_Beta.isSelected(), Check_RC.isSelected());
        } };
        Check_Alpha.addItemListener(checkOfflineListener);
        Check_Beta.addItemListener(checkOfflineListener);
        Check_RC.addItemListener(checkOfflineListener);

        ActionListener validateButton = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            String LWJGLVersion = ComboBox_LWJGLSelector.getSelection();

            String LWJGLAddress = "http://heanet.dl.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%20" + LWJGLVersion + "/lwjgl-" + LWJGLVersion + ".zip";
            Preferences_ConfigLoader.CONFIG_LWJGLAddress = LWJGLAddress;
            Preferences_ConfigFileWriter.setParameter("LWJGLAddress", LWJGLAddress);

            Preferences_ConfigLoader.CONFIG_LWJGLSelector = true;

            newForm(false);

            Updater_SystemFunctions.updateOnlyNatives();
            versionValidated = true;
        } };
        Button_Validate.addActionListener(validateButton);

        ComboBox_LWJGLSelector.addActionListener (new ActionListener () { public void actionPerformed(ActionEvent e) {
            Button_Validate.setEnabled(true);
        } });

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // On Closing Window
    
    private void onClose()
    {
        GuiForm_PreferenceFrame preferenceFrame = GuiForm_PreferenceFrame.newForm(true);
        if ( !versionValidated )
        {
            Preferences_ConfigLoader.CONFIG_LWJGLSelector = false;
            Preferences_ConfigLoader.CONFIG_LWJGLAddress = "";
            preferenceFrame.CheckBox_LWJGLSelector.setSelected(false);
        }
        newForm(false);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Functions

    private static GuiForm_LWJGLForm LWJGLForm = null;
    
    public static GuiForm_LWJGLForm newForm(boolean visible)
    {
        LWJGLForm = ( LWJGLForm == null ) ? ( new GuiForm_LWJGLForm() ) : LWJGLForm;
        LWJGLForm.setLocationRelativeTo(null);
        LWJGLForm.setVisible(visible);
        return LWJGLForm;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
