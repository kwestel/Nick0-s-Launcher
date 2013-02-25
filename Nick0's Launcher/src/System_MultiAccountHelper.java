import javax.swing.*;
import java.util.ArrayList;

public class System_MultiAccountHelper
{

    public static void saveUsername(String username, boolean offlineMode)
    {
        String usernameList = Preferences_ConfigFileWriter.getParameter("UsernameList");
        String offlineList = Preferences_ConfigFileWriter.getParameter("OfflineList");

        if ( usernameList == null || usernameList.equals("") )
        {
            Preferences_ConfigFileWriter.setParameter("UsernameList", username);
            Preferences_ConfigFileWriter.setParameter("OfflineList", offlineMode);
        }
        else if ( usernameList.contains(System_StringEncrypter.uk) )
        {
            String[] splitOfflineList = offlineList.split(System_StringEncrypter.uk);
            String[] splitUsernameList = usernameList.split(System_StringEncrypter.uk);

            for ( String actualUsername : splitUsernameList ) { if ( actualUsername.toLowerCase().trim().equals(username.toLowerCase().trim()) )
            {
                String newOfflineList = "";

                for (int i=0; i<splitOfflineList.length; i++) { newOfflineList += (username.toLowerCase().trim().equals(splitUsernameList[i].toLowerCase().trim()) ? offlineMode : splitOfflineList[i]) + (i==(splitOfflineList.length-1) ? "" : System_StringEncrypter.uk); }
                Preferences_ConfigFileWriter.setParameter("OfflineList", newOfflineList);

                return;
            } }

            Preferences_ConfigFileWriter.setParameter("UsernameList", usernameList + System_StringEncrypter.uk + username);
            Preferences_ConfigFileWriter.setParameter("OfflineList", offlineList + System_StringEncrypter.uk + offlineMode);
        }
        else
        {
            if ( !usernameList.toLowerCase().trim().equals(username.toLowerCase().trim()) )
            {
                Preferences_ConfigFileWriter.setParameter("UsernameList", usernameList + System_StringEncrypter.uk + username);
                Preferences_ConfigFileWriter.setParameter("OfflineList", offlineList + System_StringEncrypter.uk + offlineMode);
            }
            else { Preferences_ConfigFileWriter.setParameter("OfflineList", offlineMode); }
        }
    }

    public static void eraseUsername(String username)
    {
        final String usernameList = Preferences_ConfigFileWriter.getParameter("UsernameList");
        String offlineList = Preferences_ConfigFileWriter.getParameter("OfflineList");

        String newUsernameList = "";
        String newOfflineList = "";

        SystemTray_MinecraftIcon.displayErrorMessage("Compte supprimé", "Le compte \"" + username.trim() + "\" vient d'être supprimé de la liste des comptes.");

        final ArrayList<String> usernameToAdd = new ArrayList<String>();

        if ( usernameList.contains(System_StringEncrypter.uk) && offlineList.contains(System_StringEncrypter.uk) )
        {
            String[] splitUsernameList = usernameList.split(System_StringEncrypter.uk);
            String[] splitOfflineList = offlineList.split(System_StringEncrypter.uk);

            for ( int i=0; i<splitUsernameList.length; i++ )
            {
                if ( splitUsernameList[i].toLowerCase().trim().equals(username.toLowerCase().trim() ) ) { continue; }

                newUsernameList += splitUsernameList[i] + ( i+1 == splitUsernameList.length ? "" : System_StringEncrypter.uk );
                newOfflineList += splitOfflineList[i].toLowerCase().trim() + ( i+1 == splitOfflineList.length ? "" : System_StringEncrypter.uk );

                usernameToAdd.add(splitUsernameList[i].trim());
            }
        }
        else
        {
            if ( !usernameList.toLowerCase().trim().equals(username.toLowerCase().trim()) )
            {
                newUsernameList = usernameList;
                newOfflineList = offlineList.toLowerCase().trim();

                usernameToAdd.add(newUsernameList);
            }
        }

        SwingUtilities.invokeLater(new Runnable() { public void run() {
            GuiForm_MainFrame.mainFrame.ComboBox_UserName.removeAllItems();
            for ( String actualUsername : usernameToAdd ) { GuiForm_MainFrame.mainFrame.ComboBox_UserName.addItem(actualUsername.trim()); }
            GuiForm_MainFrame.mainFrame.ComboBox_UserName.addItem("Ajouter un nouveau compte...");
        } });

        Preferences_ConfigFileWriter.setParameter("UsernameList", newUsernameList.trim());
        Preferences_ConfigFileWriter.setParameter("OfflineList", newOfflineList.trim());

        Preferences_ConfigFileWriter.eraseParameter("EncP-" + username);
        Preferences_ConfigFileWriter.eraseParameter("EncH-" + username);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com

}
