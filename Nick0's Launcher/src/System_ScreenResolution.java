import java.awt.*;

public class System_ScreenResolution
{
    
    public static int getMaximumScreenX()
    {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();

        int maximumScreenX = 0;

        for ( GraphicsDevice actualScreen : graphicsDevices )
        {
            DisplayMode displayMode = actualScreen.getDisplayMode();
            maximumScreenX += displayMode.getWidth();
        }

        return maximumScreenX;
    }

    public static int getMaximumScreenY()
    {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();

        int maximumScreenY = 0;

        for ( GraphicsDevice actualScreen : graphicsDevices )
        {
            DisplayMode displayMode = actualScreen.getDisplayMode();
            maximumScreenY += displayMode.getHeight();
        }

        return maximumScreenY;
    }
    
}
