import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

public class Gui_PreferenceForm extends JFrame
{

    public static Gui_Panel mainPanel;
    
    public JLabel Label_MainTitle;
    public JLabel Label_JarSelectorText;
    public JLabel Label_HomeDir;
    public JLabel Label_ActualHomeDir;
    public JLabel Label_UpdateFunctions;

    public Gui_Button Button_ForceUpdate;
    public Gui_Button Button_ChangeHomeDir;
    public Gui_Button Button_RestoreHomeDir;

    public Gui_CheckBox CheckBox_EnableJarSelector;
    public Gui_CheckBox CheckBox_DisableUpdate;
    public Gui_CheckBox CheckBox_RAMSelector;
    
    public JSpinner Field_RAMEntry;

    public JFileChooser FileChooser;

    public Gui_PreferenceForm()
    {
        super();

        setTitle("Nick0's Launcher - Préférences - By Nicnl");
        setSize(425, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        loadAndSetPreferences();
        setContentPane(createFrameContent());
        enableActionListeners();
    }

    private JPanel createFrameContent()
    {
        mainPanel = new Gui_Panel("prefs.jpg");
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        
        Label_MainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Préférences</u></b></h1></html>");
        Label_HomeDir = new JLabel("<html><u>- Emplacement de téléchargement :</u></html>");
        Label_JarSelectorText = new JLabel("<html><u>- Options avancées :</u></html>");
        Label_UpdateFunctions = new JLabel("<html><u>- Mises à jour Minecraft :</u></html>");
        Label_ActualHomeDir = new JLabel(Main_RealLauncher.homeDir);
        CheckBox_EnableJarSelector = new Gui_CheckBox("Activer le selectionneur de .jar");
        CheckBox_DisableUpdate = new Gui_CheckBox("Désactiver les mises à jour");
        CheckBox_RAMSelector = new Gui_CheckBox("Modifier la RAM de Minecraft");
        Button_ForceUpdate = new Gui_Button("Réinstaller Minecraft !");
        Button_ChangeHomeDir = new Gui_Button("Déplacer le dossier .minecraft");
        Button_RestoreHomeDir = new Gui_Button("Restaurer l'emplacement original");
        FileChooser = new JFileChooser("Emplacement du dossier .minecraft");
        Field_RAMEntry = new JSpinner();

        Field_RAMEntry.setModel(new SpinnerNumberModel(1024, 128, 4096, 128));

        FileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        CheckBox_EnableJarSelector.setSelected(CONFIG_jarSelector);
        CheckBox_DisableUpdate.setSelected(CONFIG_updatesDisabled);
        CheckBox_RAMSelector.setSelected(CONFIG_ramSelector);
        
        Button_RestoreHomeDir.setEnabled(!Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir));

        Field_RAMEntry.setEnabled(CONFIG_ramSelector);
        Field_RAMEntry.setValue(CONFIG_selectedRam);

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

        // Checkbox : JarSelector
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(CheckBox_EnableJarSelector, gbc);

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
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0, 20, 0, 0);
        mainPanel.add(Field_RAMEntry, gbc);

        return mainPanel;
    }

    public void enableActionListeners()
    {
        ActionListener listenerMcReinstall = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            Button_ForceUpdate.setEnabled(false);
            Button_ForceUpdate.setText("Minecraft sera reinstallé !");
            MinecraftReinstallForcer = true;
        } };
        Button_ForceUpdate.addActionListener(listenerMcReinstall);

        ActionListener listenerChangeHomeDir = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            FileChooser.showOpenDialog(FileChooser.getParent());
            Main_RealLauncher.homeDir = FileChooser.getSelectedFile().getAbsolutePath();
            Label_ActualHomeDir.setText(Main_RealLauncher.homeDir);
            Button_RestoreHomeDir.setEnabled(!Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir));
            System_ConfigFileWriter.updateConfigFile();
        } };
        Button_ChangeHomeDir.addActionListener(listenerChangeHomeDir);

        ActionListener listenerResetHomeDir = new ActionListener() { public void actionPerformed(ActionEvent arg0)
        {
            Main_RealLauncher.homeDir = Main_RealLauncher.configFileDir;
            Label_ActualHomeDir.setText(Main_RealLauncher.homeDir);
            Button_RestoreHomeDir.setEnabled(!Main_RealLauncher.homeDir.equals(Main_RealLauncher.configFileDir));
            System_ConfigFileWriter.updateConfigFile();
        } };
        Button_RestoreHomeDir.addActionListener(listenerResetHomeDir);

        ItemListener ramSelectorListener = new ItemListener() { public void itemStateChanged(ItemEvent e)
        {
            boolean selected = CheckBox_RAMSelector.isSelected();
            Field_RAMEntry.setEnabled(selected);
            if ( !selected ) { Field_RAMEntry.setValue(1024); }
        } };
        CheckBox_RAMSelector.addItemListener(ramSelectorListener);

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { formClosing(); } };
        addWindowListener(formListener);
    }

    public void formClosing()
    {
        saveNewPreferences();
        Main_RealLauncher.MainFrame.setLocationRelativeTo(null);
        Main_RealLauncher.MainFrame.setVisible(true);
    }

    public void saveNewPreferences()
    {
        CONFIG_updatesDisabled = CheckBox_DisableUpdate.isSelected();
        
        CONFIG_jarSelector = CheckBox_EnableJarSelector.isSelected();
        
        CONFIG_ramSelector = CheckBox_RAMSelector.isSelected();
        try { CONFIG_selectedRam = Integer.parseInt(Field_RAMEntry.getValue().toString()); }
        catch ( NumberFormatException e )
        {
            System_ErrorHandler.handleError("La RAM entrée est invalide : \"" + Field_RAMEntry.getValue().toString() + "\"");
            CONFIG_selectedRam = 1024;
        }

        System_ConfigFileWriter.updateConfigFile();
    }

    public void loadAndSetPreferences()
    {
        System.out.println("Nick0's Launcher - Chargement des préférences...");
        
        String[] loadedPreferences = System_ConfigFileWriter.loadConfigFile();
        
        if ( loadedPreferences == null ) { return; }
        
        CONFIG_updatesDisabled = loadedPreferences[4].split("=")[1].equals("TRUE");
        CONFIG_jarSelector = loadedPreferences[5].split("=")[1].equals("TRUE");
        CONFIG_ramSelector = loadedPreferences[6].split("=")[1].equals("TRUE");

        CONFIG_selectedRam = Integer.parseInt(loadedPreferences[8].split("=")[1]);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Functions
    
    public static Gui_PreferenceForm preferenceForm = null;
    public static boolean MinecraftReinstallForcer = false;

    public static boolean CONFIG_updatesDisabled = false;
    public static boolean CONFIG_jarSelector = false;
    public static boolean CONFIG_ramSelector = false;
    public static int CONFIG_selectedRam = 1024;
    
    public static Gui_PreferenceForm newForm(boolean visible)
    {
        preferenceForm = ( preferenceForm == null ) ? ( new Gui_PreferenceForm() ) : preferenceForm;
        preferenceForm.setLocationRelativeTo(null);
        preferenceForm.setVisible(visible);
        return preferenceForm;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
