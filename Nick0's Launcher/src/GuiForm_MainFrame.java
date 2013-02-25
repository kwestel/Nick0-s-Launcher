import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
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
    public GuiElement_CheckBox Check_EnableNicnlMods;

    public JLabel Label_MainTitle;
    public JLabel Label_UsernameLabel;
    public JLabel Label_PASSLabel;
    public JLabel Label_Copyright;
    public JLabel Label_actualRam;

    public JComboBox ComboBox_UserName;
    public JPasswordField Field_Password;

    public GuiElement_JarSelector ComboBox_JarSelector;

    public GuiElement_Panel mainPanel;

    private static boolean modsCanBeEnabled;

    public boolean ignoreNextPasswordEntry = false;
    public boolean ignoreNextUsernameEntry = false;

    public static boolean formInitialized = false;
    private static boolean autoLoginStarted = false;
    private static String lastUsernameSelected = null;

    // Last Username Used To Decode Password
    private static String lastUsernameForPasswordSelected = null;

    public GuiForm_MainFrame()
    {
        super();
        formInitialized = false;

        setTitle("Nick0's Launcher - Revision " + Main_RealLauncher.getLauncherRevision());

        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        File ModsFolder = new File(Main_RealLauncher.getModsDirPath());
        modsCanBeEnabled = ModsFolder.exists() && (ModsFolder.list().length>0);

        System_LogWriter.write("Création du contenu de la fenêtre principale...");
        setContentPane(createFrameContent(true));

        System_LogWriter.write("Ajout des Actions Listeners aux éléments GUIs...");
        addActionsListeners();

        verifyButtons();
        changeSize();
        setVisible(true);

        formInitialized = true;
    }

    private JPanel createFrameContent(boolean createPanel)
    {
        if ( createPanel ) { mainPanel = new GuiElement_Panel("main.jpg"); }
        else { mainPanel.removeAll(); }

        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        if ( createPanel )
        {
            Label_MainTitle = new JLabel("<html><b><u>Accès à votre compte Minecraft :</u></b></html>");
            Label_UsernameLabel = new JLabel("<html><b>Pseudo :</b></html>");
            Label_PASSLabel = new JLabel("<html><b>Mot de passe :</b></html>");
            Label_Copyright = new JLabel("<html><u>Nick0's Launcher - R" + Main_RealLauncher.getLauncherRevision() + " - By Nicnl</u></html>");
            Label_actualRam = new JLabel("<html><u>RAM allouée : " + ( Runtime.getRuntime().maxMemory() / 1024 / 1024 ) + " Mb" + "</u></html>");
            ComboBox_UserName = new JComboBox();
            Field_Password = new JPasswordField(20);
            Button_ConnectButton = new GuiElement_Button("<html><b><span style='color:gray'>Connexion</span></b></html>");
            Button_PrefsButton = new GuiElement_Button("Réglages...");
            Check_Offline = new GuiElement_CheckBox("Offline mode");
            Check_SaveLogin = new GuiElement_CheckBox("Sauvegarder MDP");

            ComboBox_UserName.setEditable(true);
            ComboBox_UserName.setSelectedItem("");
            ComboBox_UserName.addItem("Ajouter un nouveau compte...");

            if ( modsCanBeEnabled ) { Check_EnableMods = new GuiElement_CheckBox("Activer les mods"); }
            Check_EnableNicnlMods = new GuiElement_CheckBox("CnG Mods");

            if ( Preferences_ConfigLoader.CONFIG_jarSelector ) { ComboBox_JarSelector = new GuiElement_JarSelector(); }
            Button_ConnectButton.setEnabled(false);
        }
        else
        {
            if ( Preferences_ConfigLoader.CONFIG_jarSelector )
            {
                if ( ComboBox_JarSelector == null ) { ComboBox_JarSelector = new GuiElement_JarSelector(); }
                else { ComboBox_JarSelector.updateJars(); }
            }
        }

        if ( modsCanBeEnabled ) { Check_EnableMods.setSelected(Preferences_ConfigLoader.CONFIG_modsButtonChecked); }
        Check_EnableNicnlMods.setSelected(Preferences_ConfigLoader.CONFIG_NicnlModsButtonChecked);

        Check_Offline.setEnabled(!System_UpdaterHelper.checkCorruptedMinecraft());

        Field_Password.setEnabled(!Check_Offline.isSelected());
        Check_SaveLogin.setEnabled(!Check_Offline.isSelected());

        if ( Check_Offline.isSelected() ) { Field_Password.setText(""); }
        else { Button_ConnectButton.setEnabled(false); }

        // Default Value
        gbc.gridheight = 1;

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 15, 0);
        mainPanel.add(Label_MainTitle, gbc);

        // Label : Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(3, 0, 3, 0);
        mainPanel.add(Label_UsernameLabel, gbc);

        // Field : Username
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        mainPanel.add(ComboBox_UserName, gbc);

        // Label : Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 3, 0);
        mainPanel.add(Label_PASSLabel, gbc);

        // Field : Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        mainPanel.add(Field_Password, gbc);

        // ComboBox : JarSelector
        if ( Preferences_ConfigLoader.CONFIG_jarSelector )
        {
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 0, 5, 0);
            mainPanel.add(ComboBox_JarSelector, gbc);
        }

        // Button : Connect
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(Button_ConnectButton, gbc);

        // CheckBox : Save Logins
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Check_SaveLogin, gbc);

        // CheckBox : Offline Mode
        if ( new File(Main_RealLauncher.getConfigFilePath()).exists() )
        {
            gbc.gridx = 1;
            gbc.gridy = 7;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 0, 0);
            mainPanel.add(Check_Offline, gbc);
        }
        
        // CheckBox : Enable Mods
        if ( modsCanBeEnabled )
        {
            gbc.gridx = 0;
            gbc.gridy = 8;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(0, 0, 0, 0);
            mainPanel.add(Check_EnableMods, gbc);
        }

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Check_EnableNicnlMods, gbc);

        // Button : Preferences
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 0, 15, 0);
        mainPanel.add(Button_PrefsButton, gbc);

        // Label : Copyright :P
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Label_Copyright, gbc);

        // Label : RAM
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Label_actualRam, gbc);

        return mainPanel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Action Listeners - Main Function

    private void addActionsListeners()
    {
        DocumentListener usernameListener = new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e) { verifyButtons(); verifyUsernameRemoving(); }
            public void removeUpdate(DocumentEvent e) { verifyButtons(); verifyUsernameRemoving(); }
            public void insertUpdate(DocumentEvent e) { verifyButtons(); verifyUsernameRemoving(); }
        };
        ((JTextComponent)ComboBox_UserName.getEditor().getEditorComponent()).getDocument().addDocumentListener(usernameListener);

        ItemListener comboBoxListener = new ItemListener() { public void itemStateChanged(ItemEvent e) { if ( e.getStateChange() == ItemEvent.SELECTED )
        {
            ignoreNextUsernameEntry = true;

            String typedLoginField = ((JTextComponent)ComboBox_UserName.getEditor().getEditorComponent()).getText();

            String forceUsernameRegexResult = System_RegexHelper.extractForceUsername(typedLoginField);
            if ( forceUsernameRegexResult != null ) { typedLoginField = typedLoginField.replace(forceUsernameRegexResult, ""); }

            String forceSessionIDRegexResult = System_RegexHelper.extractForceSessionID(typedLoginField);
            if ( forceSessionIDRegexResult != null ) { typedLoginField = typedLoginField.replace(forceSessionIDRegexResult, ""); }

            if ( !typedLoginField.equals("") ) { lastUsernameSelected = ((JTextComponent)ComboBox_UserName.getEditor().getEditorComponent()).getText(); }

            if ( ComboBox_UserName.getSelectedItem().equals("Ajouter un nouveau compte...") )
            {
                SwingUtilities.invokeLater(new Runnable() { public void run() {
                    ComboBox_UserName.setSelectedItem("");
                    ((JTextComponent) ComboBox_UserName.getEditor().getEditorComponent()).setText("");
                    ComboBox_UserName.getEditor().getEditorComponent().requestFocus();
                } });
                Main_RealLauncher.disablePassword();
            }
            else
            {
                String actualSelectedUsername = (String)ComboBox_UserName.getSelectedItem();

                String newUsernameRegexResult = System_RegexHelper.extractForceUsername(actualSelectedUsername);
                if ( newUsernameRegexResult != null ) { actualSelectedUsername = actualSelectedUsername.replace(newUsernameRegexResult, ""); }

                String forceSessionIDRegexResult_2 = System_RegexHelper.extractForceSessionID(actualSelectedUsername);
                if ( forceSessionIDRegexResult_2 != null ) { actualSelectedUsername = actualSelectedUsername.replace(forceSessionIDRegexResult_2, ""); }

                if ( lastUsernameForPasswordSelected == null || !lastUsernameForPasswordSelected.equals(actualSelectedUsername) ) { Main_RealLauncher.loadPassword(actualSelectedUsername); }
                lastUsernameForPasswordSelected = actualSelectedUsername;
            }
        } } };

        ComboBox_UserName.addItemListener(comboBoxListener);

        DocumentListener passwordListener = new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e) { passwordBoxChanged(); }
            public void removeUpdate(DocumentEvent e) { passwordBoxChanged(); }
            public void insertUpdate(DocumentEvent e) { passwordBoxChanged(); }
        };
        Field_Password.getDocument().addDocumentListener(passwordListener);

        ActionListener loginListener = new ActionListener() { public void actionPerformed(ActionEvent e)
        {
            if ( autoLoginStarted )
            {
                autoLoginStarted = false;
                verifyButtons();
                return;
            }

            if ( Preferences_ConfigLoader.CONFIG_jarSelector )
            {
                String selectedItem = ComboBox_JarSelector.getSelection();
                System_MinecraftLoader.minecraftJarToLoad = ( selectedItem == null || selectedItem.equals("") ) ? "minecraft.jar" : selectedItem;
            }

            System_MinecraftLoader.LoadMods = modsCanBeEnabled && Check_EnableMods != null && Check_EnableMods.isSelected();
            Preferences_ConfigLoader.CONFIG_modsButtonChecked = System_MinecraftLoader.LoadMods;

            Preferences_ConfigLoader.CONFIG_NicnlModsButtonChecked = Check_EnableNicnlMods.isSelected();

            new Thread() { public void run()
            {
                String typedLoginField = ((JTextComponent)ComboBox_UserName.getEditor().getEditorComponent()).getText();

                String forceUsernameRegexResult = System_RegexHelper.extractForceUsername(typedLoginField);
                if ( forceUsernameRegexResult != null )
                {
                    typedLoginField = typedLoginField.replace(forceUsernameRegexResult, "");
                    Web_MainTransaction.forceUsername = forceUsernameRegexResult.substring(15, forceUsernameRegexResult.length()-1);
                }
                else { Web_MainTransaction.forceUsername = null; }

                String forceSessionIDRegexResult = System_RegexHelper.extractForceSessionID(typedLoginField);
                if ( forceSessionIDRegexResult != null )
                {
                    typedLoginField = typedLoginField.replace(forceSessionIDRegexResult, "");
                    Web_MainTransaction.forceSessionID = ( new String(Field_Password.getPassword()) );
                }
                else { Web_MainTransaction.forceSessionID = null; }

                ((JTextComponent)ComboBox_UserName.getEditor().getEditorComponent()).setText(typedLoginField);

                Main_RealLauncher.startLogin(typedLoginField, Main_RealLauncher.a ? Main_RealLauncher.gB() : ( new String(Field_Password.getPassword()) ));
            } }.start();
        } };

        Field_Password.addActionListener(loginListener);
        ComboBox_UserName.getEditor().addActionListener(loginListener);
        Button_ConnectButton.addActionListener(loginListener);

        ActionListener preferencesListener = new ActionListener() { public void actionPerformed(ActionEvent e)
        {
            autoLoginStarted = false;
            verifyButtons();

            setVisible(false);
            GuiForm_PreferenceFrame.newForm(true);
        } };
        Button_PrefsButton.addActionListener(preferencesListener);

        ItemListener checkOfflineListener = new ItemListener() { public void itemStateChanged(ItemEvent  e)
        {
            autoLoginStarted = false;
            Field_Password.setEnabled(!Check_Offline.isSelected());
            Check_SaveLogin.setEnabled(!Check_Offline.isSelected());

            if ( Check_Offline.isSelected() ) { Field_Password.setText(""); }
            else { Button_ConnectButton.setEnabled(false); }

            verifyButtons();
        } };
        Check_Offline.addItemListener(checkOfflineListener);

        ItemListener checkSavePassListener = new ItemListener() { public void itemStateChanged(ItemEvent  e)
        {
            autoLoginStarted = false;
            if ( !Check_SaveLogin.isSelected() && Main_RealLauncher.a) { Field_Password.setText(""); }
        } };
        Check_SaveLogin.addItemListener(checkSavePassListener);

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Action Listener - Helpers

    private void verifyButtons()
    {
        autoLoginStarted = false;

        if ( ( ( new String(Field_Password.getPassword()) ).equals("") && !Check_Offline.isSelected() ) || ComboBox_UserName.getEditor().getEditorComponent().equals("") ) { Button_ConnectButton.setEnabled(false); }
        else { Button_ConnectButton.setEnabled(true); }

        Button_ConnectButton.setText("<html><b><span style='color:" + (Button_ConnectButton.isEnabled() ? "black" : "gray") + "'>Connexion</span></b></html>");
    }

    private void verifyUsernameRemoving()
    {
        SwingUtilities.invokeLater(new Runnable() { public void run() {
            if ( ignoreNextUsernameEntry )
            {
                ignoreNextUsernameEntry = false;
                return;
            }

            String typedLoginField = ((JTextComponent)ComboBox_UserName.getEditor().getEditorComponent()).getText();

            String forceUsernameRegexResult = System_RegexHelper.extractForceUsername(typedLoginField);
            if ( forceUsernameRegexResult != null ) { typedLoginField = typedLoginField.replace(forceUsernameRegexResult, ""); }

            String forceSessionIDRegexResult = System_RegexHelper.extractForceSessionID(typedLoginField);
            if ( forceSessionIDRegexResult != null ) { typedLoginField = typedLoginField.replace(forceSessionIDRegexResult, ""); }

            if ( typedLoginField.toLowerCase().trim().equals("") && lastUsernameSelected != null && !lastUsernameSelected.toLowerCase().trim().equals("") && !lastUsernameSelected.equals("Ajouter un nouveau compte...") )
            {
                System_MultiAccountHelper.eraseUsername(lastUsernameSelected);
                Check_Offline.setSelected(false);
                Check_SaveLogin.setSelected(false);
                Main_RealLauncher.disablePassword();
            }
        } });
    }

    private void passwordBoxChanged()
    {
        if (ignoreNextPasswordEntry)
        {
            ignoreNextPasswordEntry = false;
            return;
        }

        if ( Main_RealLauncher.a) { disableAntiDisplaying(); }
        verifyButtons();
    }
    
    public void disableAntiDisplaying()
    {
        Main_RealLauncher.a = false;
        
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
        setContentPane(createFrameContent(false));
        mainPanel.updateUI();
    }

    private void changeSize()
    {
        /*
        int sizeX = 325;
        int sizeY = 290 + 30 + (Preferences_ConfigLoader.CONFIG_jarSelector ? 30 : 0);

        setSize(sizeX, sizeY);
        validate();
        */

        pack();
        setSize(getWidth()+20, getHeight()+10);
        validate();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SUPRA-Close/Open Function
    
    public void setVisible(boolean option)
    {
        if ( option ) { Check_Offline.setEnabled(!System_UpdaterHelper.checkCorruptedMinecraft()); }
        super.setVisible(option);
    }

    public void onClose()
    {
        Preferences_ConfigFileWriter.writeConfigFile("", false, !Main_RealLauncher.a, true);
        formInitialized = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Functions

    public static GuiForm_MainFrame mainFrame = null;

    public static void destroyWindow()
    {
        if ( mainFrame == null ) { return; }

        mainFrame.setVisible(false);
        mainFrame.dispose();
        mainFrame = null;
    }
    
    public static GuiForm_MainFrame newForm(boolean visible)
    {
        mainFrame = ( mainFrame == null ) ? ( new GuiForm_MainFrame() ) : mainFrame;
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(visible);
        if ( visible && Preferences_ConfigLoader.CONFIG_jarSelector ) { mainFrame.ComboBox_JarSelector.updateJars(); }
        return mainFrame;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Automatic Launcher Functions

    public static void setRandomPasswordString(final String randomPasswordString) { SwingUtilities.invokeLater(new Runnable() { public void run()
    {
        mainFrame.ignoreNextPasswordEntry = true;
        mainFrame.Field_Password.setText(randomPasswordString);
        mainFrame.Check_SaveLogin.setSelected(true);

        mainFrame.verifyButtons();

        if ( Preferences_ConfigLoader.CONFIG_AutoLogin && !mainFrame.Check_Offline.isSelected() ) { startAutoLogin(); }
    } }); }

    public static void setUsername(final String username, final String... additionalUsernameList) { SwingUtilities.invokeLater(new Runnable() { public void run()
    {
        //((JTextComponent)mainFrame.ComboBox_UserName.getEditor().getEditorComponent()).setText(username);

        mainFrame.ComboBox_UserName.removeItem("Ajouter un nouveau compte...");
        for ( String actualUsername : additionalUsernameList ) { mainFrame.ComboBox_UserName.addItem(actualUsername); }
        mainFrame.ComboBox_UserName.addItem("Ajouter un nouveau compte...");

        mainFrame.ComboBox_UserName.setSelectedItem(username);

        ((JTextComponent)mainFrame.ComboBox_UserName.getEditor().getEditorComponent()).setCaretPosition(username.length());

        mainFrame.verifyButtons();
        if ( Preferences_ConfigLoader.CONFIG_AutoLogin && mainFrame.Check_Offline.isSelected() ) { startAutoLogin(); }
    } }); }

    private static void startAutoLogin() { new Thread() { public void run()
    {
        autoLoginStarted = true;
        String bulletState = "•••";
        int autoLogin = 3;

        while ( mainFrame != null && mainFrame.isVisible() && autoLoginStarted )
        {
            if ( autoLogin == -1 )
            {
                autoLoginStarted = false;
                mainFrame.Button_ConnectButton.doClick();
                break;
            }

            bulletState = bulletState.substring(0, autoLogin);
            mainFrame.Button_ConnectButton.setText("<html><b><span style='color:green'>" + bulletState + " Annuler - Auto Login : " + autoLogin + " " + bulletState + "</span></b></html>");

            try { Thread.currentThread().sleep(600); }
            catch ( InterruptedException e ) { }
            autoLogin--;
        }
    } }.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Disable/Enable Interface ( While Launcher Login )

    public static boolean[] savedGuiElementsStates;
    private static boolean isConnecting = false;
    public static String customText = null;

    public static void disableLoginWindow(final boolean offlineMode)
    {
        isConnecting = true;

        savedGuiElementsStates = new boolean[]
        {
            mainFrame.Button_ConnectButton.isEnabled(), mainFrame.Button_PrefsButton.isEnabled(), mainFrame.Check_Offline.isEnabled(),
            mainFrame.Check_SaveLogin.isEnabled(), (mainFrame.Check_EnableMods != null && mainFrame.Check_EnableMods.isEnabled()), (mainFrame.Check_EnableNicnlMods != null && mainFrame.Check_EnableNicnlMods.isEnabled()),
            mainFrame.ComboBox_UserName.isEnabled(), mainFrame.Field_Password.isEnabled(), (mainFrame.ComboBox_JarSelector != null && mainFrame.ComboBox_JarSelector.isEnabled())
        };

        mainFrame.Button_ConnectButton.setEnabled(false);
        mainFrame.Button_PrefsButton.setEnabled(false);
        mainFrame.Check_Offline.setEnabled(false);

        mainFrame.Check_SaveLogin.setEnabled(false);
        if ( mainFrame.Check_EnableMods != null ) { mainFrame.Check_EnableMods.setEnabled(false); }
        if ( mainFrame.Check_EnableNicnlMods != null ) { mainFrame.Check_EnableNicnlMods.setEnabled(false); }

        mainFrame.ComboBox_UserName.setEnabled(false);
        mainFrame.Field_Password.setEnabled(false);
        if ( mainFrame.ComboBox_JarSelector != null ) { mainFrame.ComboBox_JarSelector.setEnabled(false); }

        mainFrame.Button_ConnectButton.setText("<html><b>" + ((customText == null) ? (offlineMode ? "Démarrage de Minecraft" : "Connexion en cours") : customText) + "</b></html>");

        new Thread() { public void run()
        {
            String bulletState = "";

            while ( mainFrame != null && mainFrame.isVisible() && isConnecting )
            {
                mainFrame.Button_ConnectButton.setText("<html><b><span style='color:gray'>" + bulletState + " " + ((customText == null) ? (offlineMode ? "Démarrage de Minecraft" : "Connexion en cours") : customText) + " " + bulletState + "</span></b></html>");

                try { Thread.currentThread().sleep(offlineMode ? 333 : 1000); }
                catch ( InterruptedException e ) { }

                bulletState = bulletState.length() == 3 ? "" : bulletState+"•";
            }
        } }.start();
    }

    public static void enableLoginWindow()
    {
        isConnecting = false;

        mainFrame.Button_ConnectButton.setEnabled(savedGuiElementsStates[0]);
        mainFrame.Button_PrefsButton.setEnabled(savedGuiElementsStates[1]);
        mainFrame.Check_Offline.setEnabled(savedGuiElementsStates[2]);

        mainFrame.Check_SaveLogin.setEnabled(savedGuiElementsStates[3]);
        if ( mainFrame.Check_EnableMods != null ) { mainFrame.Check_EnableMods.setEnabled(savedGuiElementsStates[4]); }
        if ( mainFrame.Check_EnableNicnlMods != null ) { mainFrame.Check_EnableNicnlMods.setEnabled(savedGuiElementsStates[5]); }

        mainFrame.ComboBox_UserName.setEnabled(savedGuiElementsStates[6]);
        mainFrame.Field_Password.setEnabled(savedGuiElementsStates[7]);
        if ( mainFrame.ComboBox_JarSelector != null ) { mainFrame.ComboBox_JarSelector.setEnabled(savedGuiElementsStates[8]); }

        mainFrame.verifyButtons();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
