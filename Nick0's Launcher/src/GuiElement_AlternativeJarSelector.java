import javax.swing.*;
import java.util.ArrayList;

public class GuiElement_AlternativeJarSelector extends GuiElement_BaseComboBox
{
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Jar List
    
    private static String[] alternativeJarList = new String[]
    {
            "Minefield", "http://www.minefield.fr/java/minefield.jar", "Minefield.jar",
            "Caldera", "http://caldera-minecraft.fr/launcher/caldera.jar", "Caldera.jar",
            "Himin", "http://files.himin.fr/MinecraftDownload/minecraft.jar", "Himin.jar"
    };
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    public GuiElement_AlternativeJarSelector()
    {
        super();

        ArrayList<String> jarList = new ArrayList<String>();
        for ( int i=0; i<alternativeJarList.length/3; i++ )
        {
            String actualJar = alternativeJarList[i*3];
            jarList.add(actualJar);
        }
        
        setModel(new DefaultComboBoxModel(jarList.toArray(new String[jarList.size()])));
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Specific Private Functions
    
    public String getSelectedJarURL()
    {
        for (int i=0; i<getItemCount(); i++) { if ( getItemAt(i).equals(getSelection()) ) { return alternativeJarList[i*3+1]; } }
        return null;
    }
    
    public String getSelectedJarFileName()
    {
        for (int i=0; i<getItemCount(); i++) { if ( getItemAt(i).equals(getSelection()) ) { return alternativeJarList[i*3+2]; } }
        return null;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
