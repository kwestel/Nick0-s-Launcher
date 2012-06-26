import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class GuiForm_InstallNewMod extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default Vars

    public static GuiElement_Panel mainPanel;

    public JLabel Label_MainTitle;
    public JLabel Label_ModName;
    public JLabel Label_ModVersion;
    public JTextField Field_modName;
    public GuiElement_BaseComboBox ComboBox_minecraftVersions;
    public GuiElement_Button Button_Install;
    public GuiElement_Button Button_Cancel;

    private String modPath;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiForm_InstallNewMod(String modPath, String modName)
    {
        super();

        this.modPath = modPath;

        setTitle("Installation...");

        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setContentPane(createFrameContent(modName));
        addActionsListeners();

        startScan();

        changeSize();
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void changeSize()
    {
        pack();
        setSize(getWidth()+20, getHeight()+10);
        validate();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Frame Creation

    private JPanel createFrameContent(String modName)
    {
        mainPanel = new GuiElement_Panel("modsSelector.jpg");

        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label_MainTitle = new JLabel("<html><h1><b><u>Installer un mod</u></b></h1></html>");
        Label_ModName = new JLabel("<html><b><u>- Nom du mod :</u></b></html>");
        Label_ModVersion = new JLabel("<html><b><u>- Version du mod :</u></b></html>");
        Field_modName = new JTextField(modName);
        ComboBox_minecraftVersions = new GuiElement_BaseComboBox();
        Button_Cancel = new GuiElement_Button("Annuler");
        Button_Install = new GuiElement_Button("Installer");

        Button_Install.setEnabled(false);

        // Default GridBagLayout Value
        gbc.gridheight = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 12, 0);
        mainPanel.add(Label_MainTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Label_ModName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(Field_modName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Label_ModVersion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(ComboBox_minecraftVersions, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Button_Cancel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Button_Install, gbc);

        return mainPanel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Document Listeners

    private void addActionsListeners()
    {
        ActionListener installListener = new ActionListener() { public void actionPerformed(ActionEvent e)
        {
            if ( !Button_Install.isEnabled() ) { return; }
            System_Mods.installMod(ComboBox_minecraftVersions.getSelection(), Field_modName.getText(), modPath);
            onClose();
        } };
        Button_Install.addActionListener(installListener);
        Field_modName.addActionListener(installListener);

        ActionListener cancelListener = new ActionListener() { public void actionPerformed(ActionEvent e) { onClose(); } };
        Button_Cancel.addActionListener(cancelListener);

        DocumentListener modNameListener = new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e) { onTextChange(); }
            public void removeUpdate(DocumentEvent e) { onTextChange(); }
            public void changedUpdate(DocumentEvent e) { onTextChange(); }
        };
        Field_modName.getDocument().addDocumentListener(modNameListener);

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    private void onClose()
    {
        setVisible(false);
        dispose();
        GuiForm_ModsSelector.newFormAndUpdate(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Cool Verifications

    private void onTextChange()
    {
        if ( Field_modName.getText().equals("") ) { Button_Install.setEnabled(false); }
        else { Button_Install.setEnabled(ComboBox_minecraftVersions.isEnabled()); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Minecraft Version Scan

    private void startScan() { new Thread() { public void run()
    {
        ComboBox_minecraftVersions.setEnabled(false);
        ComboBox_minecraftVersions.setModel(new DefaultComboBoxModel(new String[] { "Actualisation..." }));

        String[] minecraftVersions = System_FileManager.getAllMinecraftVersions();

        ComboBox_minecraftVersions.setModel(new DefaultComboBoxModel(minecraftVersions));
        ComboBox_minecraftVersions.setEnabled(true);
        onTextChange();
    }}.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
