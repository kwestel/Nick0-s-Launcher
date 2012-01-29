import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

public class Gui_MainFrame extends Gui_BaseExtend_JFrame
{

    public Gui_Button Button_ConnectButton;
    public Gui_Button Button_PrefsButton;

    public Gui_CheckBox Check_Offline;
    public Gui_CheckBox Check_SaveLogin;

    public JLabel Label_MainTitle;
    public JLabel Label_UsernameLabel;
    public JLabel Label_PASSLabel;
    public JLabel Label_Copyright;
    public JLabel Label_actualRam;

    public JTextField Field_UserName;
    public JPasswordField Field_Password;

    public Gui_JarSelector ComboBox_JarSelector;

    public Gui_Panel mainPanel;

    public Gui_MainFrame()
    {
        super();

        setTitle("Nick0's Launcher V1");
        setSize(325, 275 + (Preferences_ConfigLoader.CONFIG_jarSelector ? 30 : 0));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(createFrameContent());
        addActionsListeners();

        setVisible(true);
    }

    private JPanel createFrameContent()
    {
        mainPanel = new Gui_Panel("main.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label_MainTitle = new JLabel("<html><b><u>Accès à votre compte minecraft :</u></b></html>");
        Label_UsernameLabel = new JLabel("<html><b>Pseudo :</b></html>");
        Label_PASSLabel = new JLabel("<html><b>Mot de passe :</b></html>");
        Label_Copyright = new JLabel("<html><u>By Nicnl</u></html>");
        Label_actualRam = new JLabel("<html><u>RAM allouée : " + ( Runtime.getRuntime().maxMemory() / 1024 / 1024 ) + " Mb" + "</u></html>");
        Field_UserName = new JTextField(20);
        Field_Password = new JPasswordField(20);
        Button_ConnectButton = new Gui_Button("<html><b>Connexion</b></html>");
        Button_PrefsButton = new Gui_Button("Réglages...");
        Check_Offline = new Gui_CheckBox("Offline mode");
        Check_SaveLogin = new Gui_CheckBox("Sauvegarder MDP");
        
        if ( Preferences_ConfigLoader.CONFIG_jarSelector )
        {
            ComboBox_JarSelector = new Gui_JarSelector();
        }

        Button_ConnectButton.setEnabled(false);

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 15, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        mainPanel.add(Label_MainTitle, gbc);

        // Label : Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(3, 0, 3, 0);
        mainPanel.add(Label_UsernameLabel, gbc);

        // Field : Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        mainPanel.add(Field_UserName, gbc);

        // Label : Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.insets = new Insets(3, 0, 3, 0);
        mainPanel.add(Label_PASSLabel, gbc);

        // Field : Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        mainPanel.add(Field_Password, gbc);

        // ComboBox : JarSelector
        if ( Preferences_ConfigLoader.CONFIG_jarSelector )
        {
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 0, 5, 0);
            mainPanel.add(ComboBox_JarSelector, gbc);
        }

        // Button : Connect
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_END;
        mainPanel.add(Button_ConnectButton, gbc);

        // CheckBox : Save Logins
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        mainPanel.add(Check_SaveLogin, gbc);

        // CheckBox : Offline Mode
        if ( new File(Main_RealLauncher.configFileDir + Main_RealLauncher.configFileName).exists() )
        {
            gbc.gridx = 1;
            gbc.gridy = 7;
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            mainPanel.add(Check_Offline, gbc);
        }

        // Button : Preferences
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(Button_PrefsButton, gbc);

        // Label : Copyright :P
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(Label_Copyright, gbc);

        // Label : RAM
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.PAGE_END;
        mainPanel.add(Label_actualRam, gbc);

        return mainPanel;
    }

    private void addActionsListeners()
    {
        DocumentListener textListener = new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e) { verifyButtons(); }
            public void removeUpdate(DocumentEvent e) { verifyButtons(); }
            public void insertUpdate(DocumentEvent e) { verifyButtons(); }
        };
        Field_Password.getDocument().addDocumentListener(textListener);
        Field_UserName.getDocument().addDocumentListener(textListener);

        ActionListener loginListener = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            if ( Preferences_ConfigLoader.CONFIG_jarSelector && !ComboBox_JarSelector.getSelectedItem().equals("minecraft.jar") )
            {
                System_MinecraftLoader.jarList[3] = (String)ComboBox_JarSelector.getSelectedItem();
            }
            Main_RealLauncher.startLogin(Field_UserName.getText(), Field_Password.getText());
        } };
        Field_Password.addActionListener(loginListener);
        Field_UserName.addActionListener(loginListener);
        Button_ConnectButton.addActionListener(loginListener);

        ActionListener preferencesListener = new ActionListener() { public void actionPerformed(ActionEvent e)
        {
            setVisible(false);
            Gui_PreferenceForm.newForm(true);
        } };
        Button_PrefsButton.addActionListener(preferencesListener);

        ItemListener checkListener = new ItemListener() { public void itemStateChanged(ItemEvent  e)
        {
            Field_Password.setEnabled(!Check_Offline.isSelected());
            if ( Check_Offline.isSelected() ) { Field_Password.setText(""); }
            else { Button_ConnectButton.setEnabled(false); }
            verifyButtons();
        } };
        Check_Offline.addItemListener(checkListener);
    }
    
    private void verifyButtons()
    {
        if ( ( Field_Password.getText().equals("") && !Check_Offline.isSelected() ) || Field_UserName.getText().equals("") ) { Button_ConnectButton.setEnabled(false); }
        else { Button_ConnectButton.setEnabled(true); }
    }
    
    public Gui_MainFrame closeWindow()
    {
        setVisible(false);
        try { finalize(); }
        catch ( Throwable throwable ) { throwable.printStackTrace(); }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
