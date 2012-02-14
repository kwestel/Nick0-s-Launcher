import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GuiForm_MainFrame extends GuiExtend_JFrame
{

    public GuiElement_Button Button_ConnectButton;
    public GuiElement_Button Button_PrefsButton;

    public GuiElement_CheckBox Check_Offline;
    public GuiElement_CheckBox Check_SaveLogin;
    public GuiElement_CheckBox Check_EnableMods;

    public JLabel Label_MainTitle;
    public JLabel Label_UsernameLabel;
    public JLabel Label_PASSLabel;
    public JLabel Label_Copyright;
    public JLabel Label_actualRam;

    public JTextField Field_UserName;
    public JPasswordField Field_Password;

    public GuiElement_JarSelector ComboBox_JarSelector;

    public GuiElement_Panel mainPanel;

    private static boolean modsCanBeEnabled;

    public GuiForm_MainFrame()
    {
        super();

        setTitle("Nick0's Launcher V1");

        changeSize();

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(createFrameContent());
        addActionsListeners();

        setVisible(true);
    }

    private JPanel createFrameContent()
    {
        mainPanel = new GuiElement_Panel("main.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label_MainTitle = new JLabel("<html><b><u>Accès à votre compte minecraft :</u></b></html>");
        Label_UsernameLabel = new JLabel("<html><b>Pseudo :</b></html>");
        Label_PASSLabel = new JLabel("<html><b>Mot de passe :</b></html>");
        Label_Copyright = new JLabel("<html><u>By Nicnl</u></html>");
        Label_actualRam = new JLabel("<html><u>RAM allouée : " + ( Runtime.getRuntime().maxMemory() / 1024 / 1024 ) + " Mb" + "</u></html>");
        Field_UserName = new JTextField(20);
        Field_Password = new JPasswordField(20);
        Button_ConnectButton = new GuiElement_Button("<html><b>Connexion</b></html>");
        Button_PrefsButton = new GuiElement_Button("Réglages...");
        Check_Offline = new GuiElement_CheckBox("Offline mode");
        Check_SaveLogin = new GuiElement_CheckBox("Sauvegarder MDP");

        if ( modsCanBeEnabled )
        {
            Check_EnableMods = new GuiElement_CheckBox("Activer les mods");
            Check_EnableMods.setSelected(Preferences_ConfigLoader.CONFIG_modsButtonChecked);
        }
        
        if ( Preferences_ConfigLoader.CONFIG_jarSelector )
        {
            ComboBox_JarSelector = new GuiElement_JarSelector();
            ComboBox_JarSelector.SelectStringEntry(Preferences_ConfigLoader.CONFIG_LastJarSaved);
        }

        Check_Offline.setEnabled(!Web_MinecraftUpdater.checkCorruptedMinecraft() || Preferences_ConfigLoader.MinecraftReinstallForcer);
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
        if ( new File(Main_RealLauncher.getConfigFilePath()).exists() )
        {
            gbc.gridx = 1;
            gbc.gridy = 7;
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            mainPanel.add(Check_Offline, gbc);
        }
        
        // CheckBox : Enable Mods
        if ( modsCanBeEnabled )
        {
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            mainPanel.add(Check_EnableMods, gbc);
        }

        // Button : Preferences
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(Button_PrefsButton, gbc);

        // Label : Copyright :P
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(Label_Copyright, gbc);

        // Label : RAM
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.PAGE_END;
        mainPanel.add(Label_actualRam, gbc);

        return mainPanel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Action Listeners - Main Function

    private void addActionsListeners()
    {
        DocumentListener usernameListener = new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e) { verifyButtons(); }
            public void removeUpdate(DocumentEvent e) { verifyButtons(); }
            public void insertUpdate(DocumentEvent e) { verifyButtons(); }
        };
        Field_UserName.getDocument().addDocumentListener(usernameListener);

        DocumentListener passwordListener = new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e) { verifyBoxChanged(); }
            public void removeUpdate(DocumentEvent e) { verifyBoxChanged(); }
            public void insertUpdate(DocumentEvent e) { verifyBoxChanged(); }
        };
        Field_Password.getDocument().addDocumentListener(passwordListener);

        ActionListener loginListener = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            if ( Preferences_ConfigLoader.CONFIG_jarSelector )
            {
                String selectedItem = ComboBox_JarSelector.getSelection();
                System_MinecraftLoader.jarList[3] = ( selectedItem == null ) ? "" : selectedItem;
            }

            System_MinecraftLoader.LoadMods = modsCanBeEnabled && Check_EnableMods.isSelected();
            Preferences_ConfigLoader.CONFIG_modsButtonChecked = System_MinecraftLoader.LoadMods;

            Main_RealLauncher.startLogin(Field_UserName.getText(), Main_RealLauncher.PasswordNotDisplayed ? Main_RealLauncher.getStoredPassword() : ( new String(Field_Password.getPassword()) ));
        } };
        Field_Password.addActionListener(loginListener);
        Field_UserName.addActionListener(loginListener);
        Button_ConnectButton.addActionListener(loginListener);

        ActionListener preferencesListener = new ActionListener() { public void actionPerformed(ActionEvent e)
        {
            setVisible(false);
            GuiForm_PreferenceFrame.newForm(true);
        } };
        Button_PrefsButton.addActionListener(preferencesListener);

        ItemListener checkOfflineListener = new ItemListener() { public void itemStateChanged(ItemEvent  e)
        {
            Field_Password.setEnabled(!Check_Offline.isSelected());
            Check_SaveLogin.setEnabled(!Check_Offline.isSelected());
            if ( Check_Offline.isSelected() ) { Field_Password.setText(""); }
            else { Button_ConnectButton.setEnabled(false); }
            verifyButtons();
        } };
        Check_Offline.addItemListener(checkOfflineListener);

        ItemListener checkSavePassListener = new ItemListener() { public void itemStateChanged(ItemEvent  e)
        {
            if ( !Check_SaveLogin.isSelected() && Main_RealLauncher.PasswordNotDisplayed ) { Field_Password.setText(""); }
        } };
        Check_SaveLogin.addItemListener(checkSavePassListener);

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Action Listener - Helpers

    private boolean firstExecutionDone = false;

    private void verifyButtons()
    {
        if ( ( ( new String(Field_Password.getPassword()) ).equals("") && !Check_Offline.isSelected() ) || Field_UserName.getText().equals("") ) { Button_ConnectButton.setEnabled(false); }
        else { Button_ConnectButton.setEnabled(true); }
    }

    private void verifyBoxChanged()
    {
        if ( Main_RealLauncher.PasswordNotDisplayed && firstExecutionDone ) { disableAntiDisplaying(); }
        firstExecutionDone = true;

        verifyButtons();
    }
    
    private void disableAntiDisplaying()
    {
        Main_RealLauncher.PasswordNotDisplayed = false;
        
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                Field_Password.setText("");
                Check_SaveLogin.setSelected(false);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Interface - Helpers

    public void resetInterface()
    {
        changeSize();
        setContentPane(resetFrameContent());
        mainPanel.updateUI();
    }

    private void changeSize()
    {
        File ModsFolder = new File(Main_RealLauncher.getModsDirPath());
        modsCanBeEnabled = ModsFolder.exists() && (ModsFolder.list().length > 0);

        int YSizeToAdd = Preferences_ConfigLoader.CONFIG_jarSelector ? 30 : 0;
        YSizeToAdd += modsCanBeEnabled ? 30 : 0;
        setSize(325, 290 + YSizeToAdd);
    }

    private JPanel resetFrameContent()
    {
        mainPanel.removeAll();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        if ( Preferences_ConfigLoader.CONFIG_jarSelector )
        {
            ComboBox_JarSelector = new GuiElement_JarSelector();
            ComboBox_JarSelector.SelectStringEntry(Preferences_ConfigLoader.CONFIG_LastJarSaved);
        }
        else { ComboBox_JarSelector = null; }

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
        if ( new File(Main_RealLauncher.getConfigFilePath()).exists() )
        {
            gbc.gridx = 1;
            gbc.gridy = 7;
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            mainPanel.add(Check_Offline, gbc);
        }

        // CheckBox : Enable Mods
        if ( modsCanBeEnabled )
        {
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            gbc.fill = GridBagConstraints.FIRST_LINE_START;
            mainPanel.add(Check_EnableMods, gbc);
        }

        // Button : Preferences
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 5, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(Button_PrefsButton, gbc);

        // Label : Copyright :P
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(Label_Copyright, gbc);

        // Label : RAM
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.PAGE_END;
        mainPanel.add(Label_actualRam, gbc);

        return mainPanel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SUPRA-Close/Open Function
    
    public void setVisible(boolean option)
    {
        if ( option )
        {
            Check_Offline.setEnabled(!Web_MinecraftUpdater.checkCorruptedMinecraft() || Preferences_ConfigLoader.MinecraftReinstallForcer);
        }
        super.setVisible(option);
    }

    public void onClose()
    {
        Preferences_ConfigFileWriter.updateConfigFile(Check_Offline.isSelected());
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Functions

    public static GuiForm_MainFrame mainFrame = null;

    public static void destroyWindow()
    {
        mainFrame.setVisible(false);
        mainFrame.dispose();
        mainFrame = null;
    }
    
    public static GuiForm_MainFrame newForm(boolean visible)
    {
        mainFrame = ( mainFrame == null ) ? ( new GuiForm_MainFrame() ) : mainFrame;
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(visible);
        return mainFrame;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}