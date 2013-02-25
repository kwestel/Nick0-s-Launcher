import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SystemTray_MinecraftIcon
{

    public static boolean trayIconInitialized;

    public static CheckboxMenuItem CheckBox_hideMinecraft;
    public static CheckboxMenuItem CheckBox_displayConsole;
    public static MenuItem Item_exitItem;

    public static TrayIcon trayIcon;

    //public static MenuItem Item_mainMenu;
    //public static MenuItem Item_restartMinecraft;

    public static void enableTrayIcon() { new Thread() { public void run() { try { _enableTrayIcon(); } catch ( Exception e ) { System_ErrorHandler.handleException(e, false); trayIconInitialized = false; } } }.start(); }

    private static void _enableTrayIcon() throws IOException, AWTException
    {
        if ( !Preferences_ConfigLoader.CONFIG_ShowTrayIcon )
        {
            trayIconInitialized = false;
            System_LogWriter.write("[SysTray] System Tray Disabled ! !");
            return;
        }

        if ( !SystemTray.isSupported() )
        {
            trayIconInitialized = false;
            System_LogWriter.write("[SysTray] L'OS hôte ne supporte pas les System Trays !");
            return;
        }
        else { trayIconInitialized = true; }

        PopupMenu popupMenu = new PopupMenu();
        trayIcon = new TrayIcon(ImageIO.read(SystemTray_MinecraftIcon.class.getResource("icons/Icon_Original.png")), "Nick0's Launcher", popupMenu);
        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();

        // Create d pop-up menu components
        CheckBox_hideMinecraft = new CheckboxMenuItem("Masquer Minecraft");
        CheckBox_displayConsole = new CheckboxMenuItem("Afficher la Console");
        //Item_restartMinecraft = new MenuItem("Redémarrer Minecraft");
        //Item_mainMenu = new MenuItem("Menu Principal");
        Item_exitItem = new MenuItem("Quitter");

        //Add components to pop-up menu
        popupMenu.add(CheckBox_hideMinecraft);
        popupMenu.add(CheckBox_displayConsole);
        //popupMenu.addSeparator();
        //popupMenu.add(Item_restartMinecraft);
        //popupMenu.add(Item_mainMenu);
        popupMenu.addSeparator();
        popupMenu.add(Item_exitItem);

        trayIcon.setPopupMenu(popupMenu);

        tray.add(trayIcon);

        addActionListeners(trayIcon, tray);
    }

    private static void addActionListeners(final TrayIcon trayIcon, final SystemTray tray)
    {
        MouseListener mouseListener = new MouseListener()
        {
            public void mouseClicked(MouseEvent e) { verifyButtonActivationState(); }
            public void mousePressed(MouseEvent e) { }
            public void mouseReleased(MouseEvent e) { }
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }
        };
        trayIcon.addMouseListener(mouseListener);

        trayIcon.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            if ( System_LauncherUpdater.updateAvaivable ) { try { System_LauncherUpdater.displayUpdateConfirmationWindow(); } catch ( IOException e1 ) { System_ErrorHandler.handleException(e1, false); } }
            else if ( CheckBox_hideMinecraft.isEnabled()  )
            {
                CheckBox_hideMinecraft.setState(!CheckBox_hideMinecraft.getState());
                if ( GuiForm_GameFrame.gameFrame != null ) { GuiForm_GameFrame.gameFrame.setVisible(!GuiForm_GameFrame.gameFrame.isVisible()); }
            }
        } });

        ItemListener hideMinecraftListener = new ItemListener() { public void itemStateChanged(ItemEvent e) { if ( GuiForm_GameFrame.gameFrame != null ) { GuiForm_GameFrame.gameFrame.setVisible(!GuiForm_GameFrame.gameFrame.isVisible()); } } };
        CheckBox_hideMinecraft.addItemListener(hideMinecraftListener);

        ItemListener displayConsoleListener = new ItemListener() { public void itemStateChanged(ItemEvent e) { { GuiForm_Console.rawJavaFrame.setVisible(!GuiForm_Console.rawJavaFrame.isVisible()); } } };
        CheckBox_displayConsole.addItemListener(displayConsoleListener);

        //ActionListener restartMinecraftListener = new ActionListener() { public void actionPerformed(ActionEvent e) { Main_RealLauncher.restartMinecraft(); } };
        //Item_restartMinecraft.addActionListener(restartMinecraftListener);

        //ActionListener mainMenuListener = new ActionListener() { public void actionPerformed(ActionEvent e) { Main_ReLauncher.loadLauncher(true); } };
        //Item_mainMenu.addActionListener(mainMenuListener);

        ActionListener exitLauncherListener = new ActionListener() { public void actionPerformed(ActionEvent e) { System.exit(0); } };
        Item_exitItem.addActionListener(exitLauncherListener);
    }

    private static void verifyButtonActivationState()
    {
        CheckBox_displayConsole.setEnabled(GuiForm_GameFrame.gameFrame != null);
        CheckBox_hideMinecraft.setEnabled(GuiForm_GameFrame.gameFrame != null);
        //Item_restartMinecraft.setEnabled(GuiForm_GameFrame.gameFrame != null);

        //Item_mainMenu.setEnabled(GuiForm_MainFrame.mainFrame == null || !GuiForm_MainFrame.mainFrame.isVisible());
    }

    private static void displayUpdateMessage() { if ( SystemTray_MinecraftIcon.trayIcon != null ) { SystemTray_MinecraftIcon.trayIcon.displayMessage("Mise à jour disponible", "Une mise à jour du launcher est disponible !\n[Revision : " + System_LauncherUpdater.latestLauncherRevision + "]\nCliquez sur ce message, ou double cliquez sur l'icône pour démarrer la mise à jour.", TrayIcon.MessageType.NONE); } }

    public static void displayExceptionMessage(String exception) { if ( SystemTray_MinecraftIcon.trayIcon != null ) { SystemTray_MinecraftIcon.trayIcon.displayMessage("Erreur détectée " + ( exception.contains("\n") ? ( exception.split("\n")[0].toLowerCase().contains("nullpointerexception") ? " : variable null !" : "!" ) : "!" ), exception, TrayIcon.MessageType.ERROR); } }

    public static void displayErrorMessage(String errorTitle, String errorText) { if ( Preferences_ConfigLoader.CONFIG_ShowErrorNotifications && Preferences_ConfigLoader.CONFIG_ShowTrayIcon && SystemTray_MinecraftIcon.trayIconInitialized && SystemTray_MinecraftIcon.trayIcon != null ) { SystemTray_MinecraftIcon.trayIcon.displayMessage(errorTitle, errorText, TrayIcon.MessageType.ERROR); } }

    public static void displaySpamUpdateMessage() { new Thread() { public void run() { while(true)
    {
        displayUpdateMessage();
        try { Thread.currentThread().sleep(15000); }
        catch ( InterruptedException e ) { }
    } } }.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
