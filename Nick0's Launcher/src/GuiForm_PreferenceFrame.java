import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GuiForm_PreferenceFrame extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default Vars

    public static GuiElement_Panel mainPanel;
    
    public JLabel Label_MainTitle;
    public JLabel Label_JarSelectorText;
    public JLabel Label_HomeDir;
    public JLabel Label_ActualHomeDir;
    public JLabel Label_UpdateFunctions;

    public GuiElement_Button Button_ForceUpdate;
    public GuiElement_Button Button_ChangeHomeDir;
    public GuiElement_Button Button_RestoreHomeDir;

    public GuiElement_CheckBox CheckBox_EnableJarSelector;
    public GuiElement_CheckBox CheckBox_DisableUpdate;
    public GuiElement_CheckBox CheckBox_RAMSelector;
    public GuiElement_CheckBox CheckBox_SaveLastJar;
    
    public JSpinner Field_RAMEntry;
    public JFileChooser FileChooser;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiForm_PreferenceFrame()
    {
        super();

        setTitle("Nick0's Launcher - Préférences - By Nicnl");
        setSize(425, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(createFrameContent());
        addActionsListeners();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Frame Creation

    private JPanel createFrameContent()
    {
        mainPanel = new GuiElement_Panel("prefs.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        
        Label_MainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Préférences</u></b></h1></html>");
        Label_HomeDir = new JLabel("<html><u>- Emplacement de téléchargement :</u></html>");
        Label_JarSelectorText = new JLabel("<html><u>- Options avancées :</u></html>");
        Label_UpdateFunctions = new JLabel("<html><u>- Mises à jour Minecraft :</u></html>");
        Label_ActualHomeDir = new JLabel(Main_RealLauncher.homeDir);
        CheckBox_EnableJarSelector = new GuiElement_CheckBox("Activer le selectionneur de .jar");
        CheckBox_SaveLastJar = new GuiElement_CheckBox("Sauvegarder le dernier .jar utilisé");
        CheckBox_DisableUpdate = new GuiElement_CheckBox("Désactiver les mises à jour");
        CheckBox_RAMSelector = new GuiElement_CheckBox("Modifier la RAM de Minecraft");
        Button_ForceUpdate = new GuiElement_Button("Réinstaller Minecraft !");
        Button_ChangeHomeDir = new GuiElement_Button("Déplacer le dossier .minecraft");
        Button_RestoreHomeDir = new GuiElement_Button("Restaurer l'emplacement original");
        FileChooser = new JFileChooser("Emplacement du dossier .minecraft");
        Field_RAMEntry = new JSpinner();

        Field_RAMEntry.setModel(new SpinnerNumberModel(1024, 128, 4096, 128));

        FileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        CheckBox_DisableUpdate.setSelected(Preferences_ConfigLoader.CONFIG_updatesDisabled);
        CheckBox_RAMSelector.setSelected(Preferences_ConfigLoader.CONFIG_ramSelector);
        
        Button_RestoreHomeDir.setEnabled(!Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir));

        Field_RAMEntry.setEnabled(Preferences_ConfigLoader.CONFIG_ramSelector);
        Field_RAMEntry.setValue(Preferences_ConfigLoader.CONFIG_selectedRam);

        CheckBox_EnableJarSelector.setSelected(Preferences_ConfigLoader.CONFIG_jarSelector);
        CheckBox_SaveLastJar.setSelected(Preferences_ConfigLoader.CONFIG_SaveLastJar);
        CheckBox_SaveLastJar.setEnabled(Preferences_ConfigLoader.CONFIG_jarSelector);

        // Label : Main Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(Label_MainTitle, gbc);

        // Label : Mises à jour
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(Label_UpdateFunctions, gbc);

        // Button : Force Update
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Button_ForceUpdate, gbc);

        // CheckBox : Disable Update
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(CheckBox_DisableUpdate, gbc);

        // Label : HomeDir
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(20, 0, 5, 0);
        mainPanel.add(Label_HomeDir, gbc);

        // Button : Change HomeDir
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Button_ChangeHomeDir, gbc);

        // Button : Reset HomeDir
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Button_RestoreHomeDir, gbc);

        // Label : Actual HomeDir
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 0);
        mainPanel.add(Label_ActualHomeDir, gbc);

        // Label : JarSelector
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(20, 0, 5, 0);
        mainPanel.add(Label_JarSelectorText, gbc);

        // Checkbox : Jar Selector
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(CheckBox_EnableJarSelector, gbc);

        // Checkbox : Save Last Jar
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(CheckBox_SaveLastJar, gbc);

        // Checkbox : Enable RAM Selector
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(CheckBox_RAMSelector, gbc);

        // Field : RAM Selector
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Field_RAMEntry, gbc);

        return mainPanel;
    }

    private void addActionsListeners()
    {
        ActionListener listenerMcReinstall = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            Button_ForceUpdate.setEnabled(false);
            Button_ForceUpdate.setText("Minecraft sera reinstallé !");
            Preferences_ConfigLoader.MinecraftReinstallForcer = true;
        } };
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

        ItemListener ramSelectorListener = new ItemListener() { public void itemStateChanged(ItemEvent e)
        {
            boolean selected = CheckBox_RAMSelector.isSelected();
            Field_RAMEntry.setEnabled(selected);
            if ( !selected ) { Field_RAMEntry.setValue(1024); }
        } };
        CheckBox_RAMSelector.addItemListener(ramSelectorListener);

        ItemListener saveLastJarListener = new ItemListener() { public void itemStateChanged(ItemEvent e)
        {
            boolean selected = CheckBox_EnableJarSelector.isSelected();
            CheckBox_SaveLastJar.setEnabled(selected);
            if ( !selected ) { CheckBox_SaveLastJar.setSelected(false); }
        } };
        CheckBox_EnableJarSelector.addItemListener(saveLastJarListener);

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    private void onClose()
    {
        boolean storedPref_JarSelector = Preferences_ConfigLoader.CONFIG_jarSelector;
        boolean storedPref_RamBool = Preferences_ConfigLoader.CONFIG_ramSelector;
        int storedPref_RamInt = Preferences_ConfigLoader.CONFIG_selectedRam;
        
        saveNewPreferences();

        if ( storedPref_JarSelector != Preferences_ConfigLoader.CONFIG_jarSelector ) { GuiForm_MainFrame.mainFrame.resetInterface(); }

        if ( ( storedPref_RamInt != Preferences_ConfigLoader.CONFIG_selectedRam ) || ( storedPref_RamBool != Preferences_ConfigLoader.CONFIG_ramSelector ) )
        {
            // Demande de reboot ( ram changed )
            String tempText = "Vous avez changé la ram allouée.\n" +
            "Afin d'appliquer ces réglages, vous devez relancer le launcher.\n\n" +
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

        GuiForm_MainFrame.mainFrame.setLocationRelativeTo(null);
        GuiForm_MainFrame.mainFrame.setVisible(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sauvegarde Des Preferences

    private void saveNewPreferences()
    {
        Preferences_ConfigLoader.CONFIG_updatesDisabled = CheckBox_DisableUpdate.isSelected();
        Preferences_ConfigLoader.CONFIG_jarSelector = CheckBox_EnableJarSelector.isSelected();
        Preferences_ConfigLoader.CONFIG_ramSelector = CheckBox_RAMSelector.isSelected();
        Preferences_ConfigLoader.CONFIG_SaveLastJar = CheckBox_SaveLastJar.isSelected();

        try { Preferences_ConfigLoader.CONFIG_selectedRam = Integer.parseInt(Field_RAMEntry.getValue().toString()); }
        catch ( NumberFormatException e )
        {
            System_ErrorHandler.handleError("La RAM entrée est invalide : \"" + Field_RAMEntry.getValue().toString() + "\"", false, true);
            Preferences_ConfigLoader.CONFIG_selectedRam = 1024;
        }

        Preferences_ConfigFileWriter.setParameter("DisableUpdates", Preferences_ConfigLoader.CONFIG_updatesDisabled);
        Preferences_ConfigFileWriter.setParameter("JarSelector", Preferences_ConfigLoader.CONFIG_jarSelector);
        Preferences_ConfigFileWriter.setParameter("RamSelector", Preferences_ConfigLoader.CONFIG_ramSelector);
        Preferences_ConfigFileWriter.setParameter("SaveLastJar", Preferences_ConfigLoader.CONFIG_SaveLastJar);
        Preferences_ConfigFileWriter.setParameter("RAM", Preferences_ConfigLoader.CONFIG_selectedRam);
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
