import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class GuiForm_PreferenceFrame extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default Vars

    //public static GuiElement_Panel mainPanel;

    public JLabel Label_JarSelectorText;
    public JLabel Label_ActualHomeDir;
    public JLabel Label_LWJGL;
    public JLabel Label_ModifySize;
    public JLabel Label_LabelSizeX;
    public JLabel Label_LabelSizeY;

    public GuiElement_Button Button_ForceUpdate;
    public GuiElement_Button Button_ChangeHomeDir;
    public GuiElement_Button Button_RestoreHomeDir;
    public GuiElement_Button Button_LWJGLHelp;
    public GuiElement_Button Button_AutomaticJarRename_Help;

    public GuiElement_CheckBox CheckBox_EnableJarSelector;
    public GuiElement_CheckBox CheckBox_DisableUpdate;
    public GuiElement_CheckBox CheckBox_RAMSelector;
    public GuiElement_CheckBox CheckBox_RemoveMETAINF;
    public GuiElement_CheckBox CheckBox_SaveLastJar;
    public GuiElement_CheckBox CheckBox_LWJGLSelector;
    public GuiElement_CheckBox CheckBox_ModifySize;
    public GuiElement_CheckBox CheckBox_ErreurSonore;
    public GuiElement_CheckBox CheckBox_AutomaticJarRename;
    public GuiElement_CheckBox CheckBox_ShowTrayIcon;
    public GuiElement_CheckBox CheckBox_ShowConsoleOnStartup;
    public GuiElement_CheckBox CheckBox_AutoLogin;
    public GuiElement_CheckBox CheckBox_AutoUpdate;
    public GuiElement_CheckBox CheckBox_ShowErrorNotifications;

    public JSpinner Field_ModifySizeX;
    public JSpinner Field_ModifySizeY;

    public JSpinner Field_RAMEntry;
    public JFileChooser FileChooser;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiForm_PreferenceFrame()
    {
        super();

        setTitle("Nick0's Launcher - Préférences - By Nicnl");
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(createFrameContent());

        finalizeContentCreation();
        addActionsListeners();

        JPanel selectedPanel = (JPanel)((JTabbedPane)getContentPane()).getSelectedComponent();
        Dimension preferredDimension = selectedPanel.getPreferredSize();
        setSize(550, preferredDimension.getHeight() + 100);
        validate();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Frame Creation

    private JTabbedPane createFrameContent()
    {
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setBorder(null);

        tabPane.addTab("Mises à Jour", null, createUpdatePane(), "Préférences de de mises à jour...");
        tabPane.addTab("Dossier d'Installation", null, createInstallationPathPanel(), "Déplacer le dossier d'installation de Minecraft...");
        tabPane.addTab("Avancé", null, createAdvancedPanel(), "Réglages avancés...");
        tabPane.addTab("Fenêtre", null, createWindowSizePanel(), "Réglages de la fenêtre de jeu...");
        tabPane.addTab("Versions alternatives", null, null, "Téléchargez des versions alternatives de Minecraft...");
        tabPane.addTab("Mods Selector", null, null, "Selecteur de mods...");
        tabPane.addTab("Autre", null, createOtherPanel(), "Tout le reste :D'");

        return tabPane;
    }

    private JPanel createUpdatePane()
    {
        JPanel updatePanel = new GuiElement_Panel("prefs.jpg");

        updatePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Default GBC Height Size
        gbc.gridheight = 1;

        JLabel mainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Mises à Jour</u></b></h1></html>");
        Button_ForceUpdate = new GuiElement_Button("Réinstaller Minecraft !");
        CheckBox_DisableUpdate = new GuiElement_CheckBox("Désactiver les mises à jour de Minecraft");
        CheckBox_AutoUpdate = new GuiElement_CheckBox("Télécharger automatiquement les mises à jour du Launcher");

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 25, 0);
        updatePanel.add(mainTitle, gbc);

        // Button : Force Update
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 7, 0);
        updatePanel.add(Button_ForceUpdate, gbc);

        // CheckBox : Disable Update
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 7, 0);
        updatePanel.add(CheckBox_DisableUpdate, gbc);

        // Checkbox : Auto Update
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        updatePanel.add(CheckBox_AutoUpdate, gbc);

        return updatePanel;
    }

    private JPanel createInstallationPathPanel()
    {
        JPanel installationPathPanel = new GuiElement_Panel("prefs.jpg");

        installationPathPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Default GBC Height Size
        gbc.gridheight = 1;

        JLabel mainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Installation</u></b></h1></html>");
        FileChooser = new JFileChooser("Emplacement du dossier .minecraft");
        Button_ChangeHomeDir = new GuiElement_Button("Déplacer le dossier .minecraft");
        Button_RestoreHomeDir = new GuiElement_Button("Restaurer l'emplacement original");
        Label_ActualHomeDir = new JLabel(Main_RealLauncher.homeDir);

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 25, 0);
        installationPathPanel.add(mainTitle, gbc);

        // Button : Change HomeDir
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        installationPathPanel.add(Button_ChangeHomeDir, gbc);

        // Button : Reset HomeDir
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        installationPathPanel.add(Button_RestoreHomeDir, gbc);

        // Label : Actual HomeDir
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 0);
        installationPathPanel.add(Label_ActualHomeDir, gbc);

        return installationPathPanel;
    }

    private JPanel createAdvancedPanel()
    {
        JPanel advancedPanel = new GuiElement_Panel("prefs.jpg");

        advancedPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Default GBC Height Size
        gbc.gridheight = 1;

        JLabel mainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Avancé</u></b></h1></html>");
        Label_JarSelectorText = new JLabel("<html><u><b>- Options avancées :</b></u></html>");
        CheckBox_EnableJarSelector = new GuiElement_CheckBox("Activer le selectionneur de .jar");
        CheckBox_SaveLastJar = new GuiElement_CheckBox("Sauvegarder le dernier .jar utilisé");
        CheckBox_RAMSelector = new GuiElement_CheckBox("Modifier la RAM de Minecraft");
        Field_RAMEntry = new JSpinner();
        CheckBox_RemoveMETAINF = new GuiElement_CheckBox("Automatiquement supprimer le META-INF");
        Label_LWJGL = new JLabel("<html><u><b>- LWJGL Selector :</b></u></html>");
        Button_LWJGLHelp = new GuiElement_Button("Qu'est-ce que c'est ?");
        CheckBox_LWJGLSelector = new GuiElement_CheckBox("Télécharger les libraries Java LWJGL officielles");

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 5, 0);
        advancedPanel.add(mainTitle, gbc);

        // Label : JarSelector
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(5, 0, 5, 0);
        advancedPanel.add(Label_JarSelectorText, gbc);

        // Checkbox : Jar Selector
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        advancedPanel.add(CheckBox_EnableJarSelector, gbc);

        // Checkbox : Save Last Jar
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        advancedPanel.add(CheckBox_SaveLastJar, gbc);

        // Checkbox : Enable RAM Selector
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        advancedPanel.add(CheckBox_RAMSelector, gbc);

        // Field : RAM Selector
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        advancedPanel.add(Field_RAMEntry, gbc);

        // Checkbox : Automatic META-INF Remove
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        advancedPanel.add(CheckBox_RemoveMETAINF, gbc);

        // Label : LWJGL Selector
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(20, 0, 5, 0);
        advancedPanel.add(Label_LWJGL, gbc);

        // Button : LWJGL Help
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 7, 0);
        advancedPanel.add(Button_LWJGLHelp, gbc);

        // Checkbox : LWJGL Selector
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        advancedPanel.add(CheckBox_LWJGLSelector, gbc);

        return advancedPanel;
    }

    private JPanel createWindowSizePanel()
    {
        JPanel windowSizePanel = new GuiElement_Panel("prefs.jpg");

        windowSizePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Default GBC Height Size
        gbc.gridheight = 1;

        JLabel mainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Fenêtre de jeu</u></b></h1></html>");
        Label_ModifySize = new JLabel("<html><u><b>- Taille de la fenêtre de Minecraft :</b></u></html>");
        CheckBox_ModifySize = new GuiElement_CheckBox("Modifier la taille de la fenêtre");
        Label_LabelSizeX = new JLabel("Largeur : ");
        Label_LabelSizeY = new JLabel("Hauteur : ");
        Field_ModifySizeX = new JSpinner();
        Field_ModifySizeY = new JSpinner();

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 5, 0);
        windowSizePanel.add(mainTitle, gbc);

        // Label : JarSelector
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(2, 0, 5, 0);
        windowSizePanel.add(Label_ModifySize, gbc);

        // Checkbox : Modify Game Size
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        windowSizePanel.add(CheckBox_ModifySize, gbc);

        // Label : SizeX
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        windowSizePanel.add(Label_LabelSizeX, gbc);

        // Label : SizeY
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        windowSizePanel.add(Label_LabelSizeY, gbc);

        // Field : SizeX
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        windowSizePanel.add(Field_ModifySizeX, gbc);

        // Field : SizeY
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        windowSizePanel.add(Field_ModifySizeY, gbc);

        return windowSizePanel;
    }

    private JPanel createOtherPanel()
    {
        JPanel otherPanel = new GuiElement_Panel("prefs.jpg");

        otherPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Default GBC Height Size
        gbc.gridheight = 1;

        JLabel mainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Autre</u></b></h1></html>");
        CheckBox_ErreurSonore = new GuiElement_CheckBox("Message d'erreur sonore");
        CheckBox_AutomaticJarRename = new GuiElement_CheckBox("Renommer automatiquement les jars");
        Button_AutomaticJarRename_Help = new GuiElement_Button("Qu'est-ce que c'est ?");
        CheckBox_ShowTrayIcon = new GuiElement_CheckBox("Afficher l'icône du Launcher dans la zone de notifications");
        CheckBox_ShowConsoleOnStartup = new GuiElement_CheckBox("Afficher automatiquement la console au démarrage de Minecraft");
        CheckBox_ShowErrorNotifications = new GuiElement_CheckBox("Afficher les erreurs de Minecraft dans l'icône de notifications");
        CheckBox_AutoLogin = new GuiElement_CheckBox("Se conneter automatiquement à Minecraft ( Auto-Login )");
        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 25, 0);
        otherPanel.add(mainTitle, gbc);

        // Checkbox : Erreur Sonore
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        otherPanel.add(CheckBox_ErreurSonore, gbc);

        // Checkbox : Automatic Jar Rename
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        otherPanel.add(CheckBox_AutomaticJarRename, gbc);

        // Button : Automatic Jar Rename Help
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        otherPanel.add(Button_AutomaticJarRename_Help, gbc);

        // Checkbox : Show Console On Startup
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        otherPanel.add(CheckBox_ShowConsoleOnStartup, gbc);

        // Checkbox : Show Console On Startup
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        otherPanel.add(CheckBox_ShowErrorNotifications, gbc);

        // Checkbox : Show Tray Icon
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        otherPanel.add(CheckBox_ShowTrayIcon, gbc);

        // Checkbox : Auto Login
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        otherPanel.add(CheckBox_AutoLogin, gbc);

        return otherPanel;
    }

    private void finalizeContentCreation()
    {
        Field_RAMEntry.setModel(new SpinnerNumberModel(1024, 128, 16384, 128));

        FileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        CheckBox_DisableUpdate.setSelected(Preferences_ConfigLoader.CONFIG_updatesDisabled);
        CheckBox_RAMSelector.setSelected(Preferences_ConfigLoader.CONFIG_ramSelector);

        Button_RestoreHomeDir.setEnabled(! Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir));
        Field_RAMEntry.setEnabled(Preferences_ConfigLoader.CONFIG_ramSelector);
        Field_RAMEntry.setValue(Preferences_ConfigLoader.CONFIG_selectedRam);

        CheckBox_EnableJarSelector.setSelected(Preferences_ConfigLoader.CONFIG_jarSelector);
        CheckBox_SaveLastJar.setSelected(Preferences_ConfigLoader.CONFIG_SaveLastJar);
        CheckBox_SaveLastJar.setEnabled(Preferences_ConfigLoader.CONFIG_jarSelector);
        CheckBox_LWJGLSelector.setSelected(Preferences_ConfigLoader.CONFIG_LWJGLSelector);

        CheckBox_ModifySize.setSelected(Preferences_ConfigLoader.CONFIG_ModifyWindowSize);
        Field_ModifySizeX.setEnabled(Preferences_ConfigLoader.CONFIG_ModifyWindowSize);
        Field_ModifySizeY.setEnabled(Preferences_ConfigLoader.CONFIG_ModifyWindowSize);

        Field_ModifySizeX.setModel(new SpinnerNumberModel(Preferences_ConfigLoader.CONFIG_WindowSizeX, 128, System_ScreenResolution.getMaximumScreenX(), 1));
        Field_ModifySizeY.setModel(new SpinnerNumberModel(Preferences_ConfigLoader.CONFIG_WindowSizeY, 128, System_ScreenResolution.getMaximumScreenY(), 1));

        CheckBox_ErreurSonore.setSelected(Preferences_ConfigLoader.CONFIG_erreurSonore);
        CheckBox_RemoveMETAINF.setSelected(Preferences_ConfigLoader.CONFIG_RemoveMETAINF);
        CheckBox_AutomaticJarRename.setSelected(Preferences_ConfigLoader.CONFIG_AutomaticJarRename);

        CheckBox_ShowTrayIcon.setSelected(Preferences_ConfigLoader.CONFIG_ShowTrayIcon);
        CheckBox_ShowConsoleOnStartup.setSelected(Preferences_ConfigLoader.CONFIG_ShowConsoleOnStartup);
        CheckBox_AutoLogin.setSelected(Preferences_ConfigLoader.CONFIG_AutoLogin);
        CheckBox_AutoUpdate.setSelected(Preferences_ConfigLoader.CONFIG_AutoUpdate);
        CheckBox_ShowErrorNotifications.setSelected(Preferences_ConfigLoader.CONFIG_ShowErrorNotifications);
    }

    private void addActionsListeners()
    {
        ActionListener listenerMcReinstall = new ActionListener() { public void actionPerformed(ActionEvent arg0) { Updater_SystemFunctions.reinstallGameFromPreferences(); } };
        Button_ForceUpdate.addActionListener(listenerMcReinstall);

        ActionListener listenerChangeHomeDir = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            FileChooser.showOpenDialog(FileChooser.getParent());
            Main_RealLauncher.homeDir = FileChooser.getSelectedFile().getAbsolutePath();
            Label_ActualHomeDir.setText(Main_RealLauncher.homeDir);
            Button_RestoreHomeDir.setEnabled(!Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir));

            Preferences_ConfigFileWriter.setParameter("HomeDir", Main_RealLauncher.homeDir);
        } };
        Button_ChangeHomeDir.addActionListener(listenerChangeHomeDir);

        ActionListener listenerResetHomeDir = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            Main_RealLauncher.homeDir = Main_RealLauncher.configFileDir;
            Label_ActualHomeDir.setText(Main_RealLauncher.homeDir);
            Button_RestoreHomeDir.setEnabled(!Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir));

            Preferences_ConfigFileWriter.setParameter("HomeDir", Main_RealLauncher.homeDir);
        } };
        Button_RestoreHomeDir.addActionListener(listenerResetHomeDir);

        ItemListener ramSelectorListener = new ItemListener() { public void itemStateChanged(ItemEvent e) { boolean selected = CheckBox_RAMSelector.isSelected(); Field_RAMEntry.setEnabled(selected); if ( !selected ) { Field_RAMEntry.setValue(1024); } } };
        CheckBox_RAMSelector.addItemListener(ramSelectorListener);

        ItemListener saveLastJarListener = new ItemListener() { public void itemStateChanged(ItemEvent e)
        {
            boolean selected = CheckBox_EnableJarSelector.isSelected();
            CheckBox_SaveLastJar.setEnabled(selected);
            if ( !selected ) { CheckBox_SaveLastJar.setSelected(false); }
        } };
        CheckBox_EnableJarSelector.addItemListener(saveLastJarListener);

        ItemListener LWJGLSelectorEnablerListener = new ItemListener() { public void itemStateChanged(ItemEvent e)
        {
            if ( CheckBox_LWJGLSelector.isSelected() ) { GuiForm_LWJGLForm.newForm(true); setVisible(false); }
            else
            {
                Preferences_ConfigLoader.CONFIG_LWJGLSelector = false;
                Preferences_ConfigLoader.CONFIG_LWJGLAddress = "";

                newForm(false);

                Updater_SystemFunctions.updateOnlyNatives();
            }
        } };
        CheckBox_LWJGLSelector.addItemListener(LWJGLSelectorEnablerListener);

        ActionListener LWJGLHelpButton = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            System_MessageBox.openMessageBox("Nick0's Launcher - Aide - LWJGL Selector", "Pour afficher le jeu en 3D, Minecraft utilise des libraries appelées \"LWJGL\".",
                "Lorsque vous intallez Minecraft, vous téléchargez ces libraries depuis les serveurs de Mojang.",
                "",
                "Le problème est que Mojang ne propose qu'une version périmée : la 2.4.2 datant de 2010.",
                "Or, il existe des versions plus récentes, comme la 2.8.3, datant de Janvier 2012.",
                "Et les versions plus récentes sont plus performantes.",
                "",
                "Le LWJGL Selector vous permet de télécharger les libraries LWJGL,",
                "directement depuis le site des personnes qui l'ont crée.",
                "",
                "En clair, vous pouvez télécharger la dernière version en date.",
                "Votre jeu fonctionnera donc plus rapidement.");
        } };
        Button_LWJGLHelp.addActionListener(LWJGLHelpButton);

        ActionListener automaticRenameHelp = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            System_MessageBox.openMessageBox("Nick0's Launcher - Aide - Automatic Renaming", "Lorsque vous téléchargez Minecraft,",
                "le fichier du jeu sera enregistré sous 'minecraft.jar'",
                "",
                "Cette fonctionalité va automatiquement créer une copie votre jeu en fonction de la version.",
                "",
                "Exemple : Si vous téléchargez Minecraft 1.2.5,",
                "le nom du fichier sera : 'minecraft_1.2.5.jar'",
                "",
                "Si vous utilisez le Jar Selector, cette fonctionalité est très pratique,",
                "car vos jeux seront automatiquement triés : 1.2.3, 1.2.4, 1.2.5, etc...");
        } };
        Button_AutomaticJarRename_Help.addActionListener(automaticRenameHelp);

        ItemListener modifySizeListener = new ItemListener() { public void itemStateChanged(ItemEvent e)
        {
            boolean selected = CheckBox_ModifySize.isSelected();
            Field_ModifySizeX.setEnabled(selected);
            Field_ModifySizeY.setEnabled(selected);
            if ( !selected )
            {
                Field_ModifySizeX.setValue(950);
                Field_ModifySizeY.setValue(550);
            }
        } };
        CheckBox_ModifySize.addItemListener(modifySizeListener);

        ChangeListener changeListener = new ChangeListener() { public void stateChanged(ChangeEvent evt)
        {

            if ( ((JTabbedPane)getContentPane()).getSelectedIndex() == 4 )
            {
                newForm(false);
                GuiForm_AlternativeJar.newForm(true);
                ((JTabbedPane)getContentPane()).setSelectedIndex(0);
            }
            else if ( ((JTabbedPane)getContentPane()).getSelectedIndex() == 5 )
            {
                newForm(false);
                GuiForm_ModsSelector.newForm(true);
                ((JTabbedPane)getContentPane()).setSelectedIndex(0);
            }
            else
            {
                JPanel selectedPanel = (JPanel)((JTabbedPane)getContentPane()).getSelectedComponent();
                Dimension preferredDimension = selectedPanel.getPreferredSize();
                setSize(getWidth(), preferredDimension.getHeight() + 100);
                validate();
            }

        } };
        ((JTabbedPane)getContentPane()).addChangeListener(changeListener);

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    private void onClose()
    {
        boolean storedPref_JarSelector = Preferences_ConfigLoader.CONFIG_jarSelector;
        boolean storedPref_RamBool = Preferences_ConfigLoader.CONFIG_ramSelector;
        int storedPref_RamInt = Preferences_ConfigLoader.CONFIG_selectedRam;
        boolean storedPref_ShowTrayIcon = Preferences_ConfigLoader.CONFIG_ShowTrayIcon;
        
        saveNewPreferences();

        if ( storedPref_JarSelector != Preferences_ConfigLoader.CONFIG_jarSelector ) { GuiForm_MainFrame.mainFrame.resetInterface(); }

        if ( ( storedPref_RamInt != Preferences_ConfigLoader.CONFIG_selectedRam ) || ( storedPref_RamBool != Preferences_ConfigLoader.CONFIG_ramSelector ) || ( storedPref_ShowTrayIcon != Preferences_ConfigLoader.CONFIG_ShowTrayIcon ) )
        {
            String tempText = "Vous avez modifé des paramètres système,\n" +
            "Qui ne peuvent être effectifs qu'après un rechargement du Launcher.\n\n" +
            "Voulez vous le relancer maintenant ?";
            int userResponse = JOptionPane.showConfirmDialog(new JInternalFrame(), tempText, "Redémarrage requis", JOptionPane.YES_NO_OPTION);

            if ( userResponse == 0 )
            {
                if ( Preferences_ConfigLoader.CONFIG_ramSelector ) { Main_ReLauncher.main(null); }
                else { Main_ReLauncher.loadLauncher(true); }
                System.exit(0);
                return;
            }
        }

        GuiForm_MainFrame.newForm(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sauvegarde Des Preferences

    private void saveNewPreferences()
    {
        Preferences_ConfigLoader.CONFIG_updatesDisabled = CheckBox_DisableUpdate.isSelected();
        Preferences_ConfigLoader.CONFIG_jarSelector = CheckBox_EnableJarSelector.isSelected();
        Preferences_ConfigLoader.CONFIG_ramSelector = CheckBox_RAMSelector.isSelected();
        Preferences_ConfigLoader.CONFIG_SaveLastJar = CheckBox_SaveLastJar.isSelected();
        Preferences_ConfigLoader.CONFIG_LWJGLSelector = CheckBox_LWJGLSelector.isSelected();
        Preferences_ConfigLoader.CONFIG_erreurSonore = CheckBox_ErreurSonore.isSelected();
        Preferences_ConfigLoader.CONFIG_RemoveMETAINF = CheckBox_RemoveMETAINF.isSelected();
        Preferences_ConfigLoader.CONFIG_AutomaticJarRename = CheckBox_AutomaticJarRename.isSelected();
        Preferences_ConfigLoader.CONFIG_ShowConsoleOnStartup = CheckBox_ShowConsoleOnStartup.isSelected();
        Preferences_ConfigLoader.CONFIG_ShowTrayIcon = CheckBox_ShowTrayIcon.isSelected();
        Preferences_ConfigLoader.CONFIG_AutoLogin = CheckBox_AutoLogin.isSelected();
        Preferences_ConfigLoader.CONFIG_AutoUpdate = CheckBox_AutoUpdate.isSelected();
        Preferences_ConfigLoader.CONFIG_ShowErrorNotifications = CheckBox_ShowErrorNotifications.isSelected();

        try { Preferences_ConfigLoader.CONFIG_selectedRam = Integer.parseInt(Field_RAMEntry.getValue().toString()); }
        catch ( NumberFormatException e )
        {
            System_ErrorHandler.handleError("La RAM entrée est invalide : \"" + Field_RAMEntry.getValue().toString() + "\"", false, true);
            Preferences_ConfigLoader.CONFIG_selectedRam = 1024;
        }

        Preferences_ConfigLoader.CONFIG_ModifyWindowSize = CheckBox_ModifySize.isSelected();
        try
        {
            if ( !Preferences_ConfigLoader.CONFIG_ModifyWindowSize ) { throw new NumberFormatException(); }
            
            Preferences_ConfigLoader.CONFIG_WindowSizeX = Integer.parseInt(Field_ModifySizeX.getValue().toString());
            Preferences_ConfigLoader.CONFIG_WindowSizeY = Integer.parseInt(Field_ModifySizeY.getValue().toString());
        }
        catch ( NumberFormatException e )
        {
            Preferences_ConfigLoader.CONFIG_WindowSizeX = Main_RealLauncher.defaultWindowSizeX;
            Preferences_ConfigLoader.CONFIG_WindowSizeY = Main_RealLauncher.defaultWindowSizeY;
        }
        Preferences_ConfigLoader.CONFIG_WindowSize = Preferences_ConfigLoader.CONFIG_WindowSizeX + "," + Preferences_ConfigLoader.CONFIG_WindowSizeY;

        Preferences_ConfigLoader.SYSTEM_SavePreferences();
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Functions

    private static GuiForm_PreferenceFrame preferenceForm = null;
    
    public static GuiForm_PreferenceFrame newForm(boolean visible)
    {
        preferenceForm = ( preferenceForm == null ) ? ( new GuiForm_PreferenceFrame() ) : preferenceForm;
        preferenceForm.setLocationRelativeTo(null);
        preferenceForm.setVisible(visible);
        return preferenceForm;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
