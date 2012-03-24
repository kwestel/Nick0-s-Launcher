import javax.swing.*;

public class GuiElement_LWJGLSelector extends JComboBox
{

    private static final DefaultComboBoxModel emptyModel = new DefaultComboBoxModel(new String[] { "Actualisation en cours..." });
    
    public GuiElement_LWJGLSelector()
    {
        super();
        
        setOpaque(false);
        if ( !System_UserHomeDefiner.SystemOS.equals("macosx") ) { setBorder(null); }
        
        updateLWJGLVersions(false, false, false);
    }
    
    public String getSelection() { return (String)getSelectedItem(); }
    
    public void updateLWJGLVersions(final boolean versionAlpha, final boolean versionBeta, final boolean versionRC)
    {
        setEnabled(false);
        setModel(emptyModel);
        
        new Thread()
        {
            public void run()
            {
                System_LogWriter.write("Recuperation des versions LWJGL");
        
                String[] originalWebPage = Web_ClientServerProtocol.readServerWebPage("http://heanet.dl.sourceforge.net/project/java-game-lib/Official%20Releases/");
                String[] LWJGLVersions = System_NativesHelper.getNativesVersions(originalWebPage, versionAlpha, versionBeta, versionRC);
        
                setModel(new DefaultComboBoxModel(LWJGLVersions));

                setSelectedIndex(LWJGLVersions.length-1);
                
                setEnabled(true);
            }
        }.start();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
