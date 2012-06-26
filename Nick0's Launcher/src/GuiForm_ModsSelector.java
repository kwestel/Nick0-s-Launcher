import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GuiForm_ModsSelector extends GuiExtend_JFrame
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default Vars

    public static GuiElement_Panel mainPanel;

    public GuiElement_Button Button_Supprimer;

    public JLabel Label_MainTitle;
    public JLabel Label_DragNDrop;
    public GuiElement_JTable Table_Mods;
    public GuiElement_BaseComboBox ComboBox_Versions;
    public JScrollPane Scroll_Table;

    private static String lastSavedModName;
    private static String lastSavedModVersion;

    private String versionsToRefresh = "Toutes";

    private boolean ignoreNextScan = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiForm_ModsSelector()
    {
        super();

        setTitle("Mods Selector");

        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setContentPane(createFrameContent(true));
        addActionsListeners();

        startVersionScan();

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

    private JPanel createFrameContent(boolean createPanel)
    {
        if ( createPanel ) { mainPanel = new GuiElement_Panel("modsSelector.jpg"); }
        else { mainPanel.removeAll(); }

        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        Label_MainTitle = new JLabel("<html><h1><b><u>Nick0's Launcher - Mods Selector</u></b></h1></html>");
        Label_DragNDrop = new JLabel("<html><b><i>Glissez/Déposez un mod directement sur le tableau pour l'installer<br/>Vous pouvez activer/désactiver un mod en cliquant sur la dernière colonne.</i></b></html>");
        Button_Supprimer = new GuiElement_Button("Supprimer");
        Button_Supprimer.setEnabled(false);

        ComboBox_Versions = new GuiElement_BaseComboBox();

        Table_Mods = new GuiElement_JTable(System_Mods.getData(versionsToRefresh), new String[] {"Mod", "Version", "Activation"});

        Scroll_Table = new JScrollPane(Table_Mods);
        Scroll_Table.setPreferredSize(new Dimension(515/4*3, 300/4*3));

        // Default GridBagLayout Value
        gbc.gridheight = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Label_MainTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 10);
        mainPanel.add(Scroll_Table, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(Button_Supprimer, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(ComboBox_Versions, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 0, 0);
        mainPanel.add(Label_DragNDrop, gbc);

        return mainPanel;
    }

    private void addActionsListeners()
    {
        DropTarget tableDropTarget = new DropTarget() { public synchronized void drop(DropTargetDropEvent dtde)
        {
            try { handleTransfer(dtde); }
            catch ( Exception e ) { e.printStackTrace(); }

            super.drop(dtde);
        } };
        Table_Mods.setDropTarget(tableDropTarget);

        Scroll_Table.setDropTarget(new DropTarget() { public synchronized void drop(DropTargetDropEvent dtde)
        {
            try { handleTransfer(dtde); }
            catch ( Exception e ) { e.printStackTrace(); }

            super.drop(dtde);
        } });


        ActionListener preferencesListener = new ActionListener() { public void actionPerformed(ActionEvent e)
        {
            System_FileManager.removeFile(Main_RealLauncher.getModsDirPath() + File.separator + lastSavedModVersion + File.separator + lastSavedModName + ".zip", false);
            newFormAndUpdate(true);
        } };
        Button_Supprimer.addActionListener(preferencesListener);

        ActionListener comboBoxListener = new ActionListener() { public void actionPerformed(ActionEvent e) {
            if ( !ignoreNextScan )
            {
                ignoreNextScan = true;
                return;
            }
            versionsToRefresh = ComboBox_Versions.getSelection();
            newFormAndUpdate(true);
        } };
        ComboBox_Versions.addActionListener(comboBoxListener);

        ListSelectionListener tableSelectionListener = new ListSelectionListener() { public void valueChanged( ListSelectionEvent event )
        {
            try
            {
                int[] selectedRows = Table_Mods.getSelectedRows();
                if ( selectedRows.length > 1 ) { throw new Exception(); }

                int lastRow = selectedRows[selectedRows.length-1];

                lastSavedModName = (String)Table_Mods.getValueAt(lastRow, 0);
                lastSavedModVersion = (String)Table_Mods.getValueAt(lastRow, 1);

                Button_Supprimer.setEnabled(true);
            }
            catch ( Exception e ) { Button_Supprimer.setEnabled(false); }
        } };
        Table_Mods.getSelectionModel().addListSelectionListener(tableSelectionListener);

        WindowListener formListener = new WindowAdapter() { public void windowClosing(WindowEvent e) { onClose(); } };
        addWindowListener(formListener);
    }

    private void onClose()
    {
        newForm(false);
        GuiForm_PreferenceFrame.newForm(true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Transfer File Handler

    private static void handleTransfer(DropTargetDropEvent dtde) throws IOException, UnsupportedFlavorException
    {
        Transferable transferableData = dtde.getTransferable();
        DataFlavor[] dataFlavors = transferableData.getTransferDataFlavors();

        for ( DataFlavor actualFlavor : dataFlavors )
        {
            if ( actualFlavor.isFlavorJavaFileListType() )
            {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                List list = (List)transferableData.getTransferData(actualFlavor);

                if ( list.size() > 1 )
                {
                    dtde.dropComplete(true);
                    return;
                }

                String filePath = list.get(0).toString();
                if ( filePath.toLowerCase().endsWith(".zip") ) { modReceived(filePath); }

                dtde.dropComplete(true);
                return;
            }
        }
    }

    private static void modReceived(String modPath)
    {
        String modName = modPath.substring(modPath.lastIndexOf(File.separator)+1, modPath.length()-4);
        newForm(false);
        new GuiForm_InstallNewMod(modPath, modName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Minecraft Version Scan

    private void startVersionScan() { new Thread() { public void run()
    {
        ComboBox_Versions.setEnabled(false);
        ComboBox_Versions.setModel(new DefaultComboBoxModel(new String[] { "Toutes" }));

        ArrayList<String> allVersions = new ArrayList<String>();
        allVersions.add("Toutes");
        Collections.addAll(allVersions, System_Mods.getAllDifferentModVersions());

        ComboBox_Versions.setModel(new DefaultComboBoxModel(allVersions.toArray(new String[allVersions.size()])));
        ComboBox_Versions.selectStringEntry(versionsToRefresh);
        ComboBox_Versions.setEnabled(true);
    }}.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Functions

    private static GuiForm_ModsSelector ModsSelectorForm = null;

    public static GuiForm_ModsSelector newForm(boolean visible)
    {
        ModsSelectorForm = ( ModsSelectorForm == null ) ? ( new GuiForm_ModsSelector() ) : ModsSelectorForm;
        ModsSelectorForm.setLocationRelativeTo(null);
        ModsSelectorForm.setVisible(visible);

        return ModsSelectorForm;
    }

    public static GuiForm_ModsSelector newFormAndUpdate(boolean visible)
    {
        ModsSelectorForm = newForm(visible);

        ModsSelectorForm.setContentPane(ModsSelectorForm.createFrameContent(false));
        ModsSelectorForm.addActionsListeners();
        ModsSelectorForm.startVersionScan();
        ModsSelectorForm.ignoreNextScan = false;

        return ModsSelectorForm;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
