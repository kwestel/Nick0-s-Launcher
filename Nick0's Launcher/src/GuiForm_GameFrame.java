import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class GuiForm_GameFrame extends GuiExtend_JFrame
{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Fields

    // Basic Fields
        public static GuiForm_GameFrame gameFrame;
        public Applet minecraftApplet = null;

    // Enhanced Frame Fields
        private boolean enhancedFrame = true;
        private boolean enhancedFrame_showMiniMaxiButtons = true;
        private boolean enhancedFrame_showResizeBars = true;
        private boolean enhancedFrame_roundCorners = true;

        private JPanel enhancedFrame_dragPanel;
        private JLabel enhancedFrame_frameTitle;

        private JPanel enhancedFrame_closeButton;
        private JPanel enhancedFrame_maximizeButton;
        private JPanel enhancedFrame_minimizeButton;

        private JPanel resizeBar_Left;
        private JPanel resizeBar_Bottom;
        private JPanel resizeBar_Right;

        private JPanel resizeBar_BottomLeft;
        private JPanel resizeBar_BottomRight;

        private int lastClickedX = 0;
        private int lastClickedY = 0;

        private int lastWidth = 0;
        private int lastHeight = 0;

        private int savedSizeX = 0;
        private int savedSizeY = 0;

        private double dragPanelAnimationY = 0;
        private double resizeBarLeftAnimationX = -8;
        private double resizeBarRightAnimationX = 8;
        private double resizeBarBottomAnimationY = 8;

        private boolean resizeBarLeftShowState = false;
        private boolean resizeBarRightShowState = false;
        private boolean resizeBarBottomShowState = false;

        private boolean dragPanelShowState = true;
        private boolean firstTimeDone = false;

        private final int ANIMATION_OFF = 0;
        private final int ANIMATION_SHOW = 1;
        private final int ANIMATION_HIDE = 2;

        private int dragPanel_animationState = 0;
        private int leftResizeBar_animationState = 0;
        private int rightResizeBar_animationState = 0;
        private int bottomResizeBar_animationState = 0;

        private boolean isMaximized = false;
        private boolean isDragging = false;

        private boolean mouseHoverCloseButton = false;
        private boolean mousePressedCloseButton = false;

        private boolean mouseHoverMaximizeButton = false;
        private boolean mousePressedMaximizeButton = false;

        private boolean mouseHoverMinimizeButton = false;
        private boolean mousePressedMinimizeButton = false;

        private long lastDragBarClick = 0L;

    // Enhanced Frame Colors & Buffer Images
        private final static Color titleBarColor = new Color(95, 115, 135);

        private final static Color closeButtonColor_Normal = new Color(180, 85, 95);
        private final static Color closeButtonColor_Hover = new Color(220, 105, 120);
        private final static Color closeButtonColor_Pressed = new Color(140, 55, 65);

        private final static Color maximizeButtonColor_Normal = new Color(90, 165, 130);
        private final static Color maximizeButtonColor_Hover = new Color(110, 200, 160);
        private final static Color maximizeButtonColor_Pressed = new Color(50, 110, 80);

        private final static Color minimizeButtonColor_Normal = new Color(80, 140, 200);
        private final static Color minimizeButtonColor_Hover = new Color(95, 170, 240);
        private final static Color minimizeButtonColor_Pressed = new Color(40, 90, 140);

        private static BufferedImage closeButtonImage;
        private static BufferedImage maximizeButtonImage;
        private static BufferedImage restoreButtonImage;
        private static BufferedImage minimizeButtonImage;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor

    private GuiForm_GameFrame(String username)
    {
        super();
        setSizeModified = false;

        enhancedFrame = !Preferences_ConfigLoader.CONFIG_DisableEnhancedFrame;
        enhancedFrame_showMiniMaxiButtons = Preferences_ConfigLoader.CONFIG_EnhancedFrame_MaxiMiniButtons;
        enhancedFrame_showResizeBars = Preferences_ConfigLoader.CONFIG_EnhancedFrame_ResizeBorders;
        enhancedFrame_roundCorners = Preferences_ConfigLoader.CONFIG_EnhancedFrame_RoundCorners;

        setSize(Preferences_ConfigLoader.CONFIG_WindowSizeX, Preferences_ConfigLoader.CONFIG_WindowSizeY);
        setLocationRelativeTo(null);

        // gameFrame = this;

        if ( enhancedFrame )
        {
            setLayout(null);
            setUndecorated(true);

            initializeBufferImages();
            createEnhancedFrameContent();
            updateFrameShape();
        }
        else { setLayout(new BorderLayout()); }

        addActionsListeners();

        setTitle("Nick0's Launcher - Minecraft - " + username + " - Revision " + Main_RealLauncher.getLauncherRevision());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Action Listener Creation

    private void addActionsListeners()
    {
        addBasicActionsListeners();

        if ( !enhancedFrame ) { return; }

        addDragPanelActionsListeners();
        if ( enhancedFrame_showResizeBars ) { addResizeBarActionsListeners(); }
        if ( enhancedFrame_showMiniMaxiButtons ) { addMaxiMiniActionsListeners(); }
    }

    private void addBasicActionsListeners()
    {
        WindowAdapter closeListener = new WindowAdapter() { public void windowClosing(WindowEvent arg0) { closeOperation(); } };
        addWindowListener(closeListener);

        ComponentAdapter resizeListener = new ComponentAdapter() { @Override public void componentResized(ComponentEvent e) {

            if ( minecraftApplet == null ) { return; }

            if ( enhancedFrame && !isMaximized ) { updateFrameShape(); }
            else if ( !enhancedFrame) { updateAppletSize(); }
        } };
        addComponentListener(resizeListener);
    }

    private void addDragPanelActionsListeners()
    {
        MouseInputListener mouseListener = new MouseInputListener()
        {
            public void mouseClicked(MouseEvent e)
            {
                int doubleClickInterval = (Integer)(Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval"));
                if ( (System.currentTimeMillis()-lastDragBarClick) <= doubleClickInterval )
                {
                    toggleMaximize();
                    lastDragBarClick = 0L;
                }
                else { lastDragBarClick = System.currentTimeMillis(); }
            }
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) { }

            public void mousePressed(MouseEvent e) { lastClickedX = e.getX(); lastClickedY = e.getY(); }

            public void mouseReleased(MouseEvent e)
            {
                if ( !isDragging ) { return; }
                isDragging = false;
                if ( e.getLocationOnScreen().getY() < 1.0D ) { maximizeFrame(); }
            }

            public void mouseDragged(MouseEvent e)
            {
                isDragging = true;

                if ( isMaximized )
                {
                    lastClickedX = savedSizeX / 2;
                    lastClickedY = 10;

                    isMaximized = false;

                    setSize(savedSizeX, savedSizeY);
                }

                Point frameLocation = getLocationOnScreen();

                int newX = (int)frameLocation.getX() + ( e.getX() - lastClickedX );
                int newY = (int)frameLocation.getY() + ( e.getY() - lastClickedY );

                setLocation(newX, newY);
            }
        };
        enhancedFrame_dragPanel.addMouseListener(mouseListener);
        enhancedFrame_dragPanel.addMouseMotionListener(mouseListener);

        MouseInputListener closeButtonListener = new MouseInputListener()
        {
            public void mouseClicked(MouseEvent e) { closeOperation(); }
            public void mouseMoved(MouseEvent e) { }
            public void mouseDragged(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { mouseHoverCloseButton = true; closeButtonStateChanged(); }
            public void mouseExited(MouseEvent e) { mouseHoverCloseButton = false; closeButtonStateChanged(); }

            public void mousePressed(MouseEvent e) { mousePressedCloseButton = true; closeButtonStateChanged(); }
            public void mouseReleased(MouseEvent e) { mousePressedCloseButton = false; closeButtonStateChanged(); }

        };
        enhancedFrame_closeButton.addMouseListener(closeButtonListener);
        enhancedFrame_closeButton.addMouseMotionListener(closeButtonListener);

        new Thread()
        {
            public void run()
            {
                while ( true )
                {
                    try { Thread.currentThread().sleep(20); } catch ( Exception e ) { }

                    Point frameLocation = getLocation();
                    Dimension frameSize = getSize();
                    Point cursorPosition = MouseInfo.getPointerInfo().getLocation();

                    if ( enhancedFrame_showResizeBars )
                    {
                        leftResizeBar_mouseVerification(frameLocation, frameSize, cursorPosition);
                        rightResizeBar_mouseVerification(frameLocation, frameSize, cursorPosition);
                        bottomResizeBar_mouseVerification(frameLocation, frameSize, cursorPosition);
                    }

                    if ( /*!isFocused() ||*/ !firstTimeDone ) { continue; }

                    dragBar_mouseVerification(frameLocation, frameSize, cursorPosition);
                }
            }
        }.start();
    }

    private void addResizeBarActionsListeners()
    {
        MouseInputListener resizeBarLeftListener = new MouseInputListener() {
            public void mouseClicked(MouseEvent e) { }
            public void mousePressed(MouseEvent e) { lastClickedX = (int)e.getLocationOnScreen().getX(); lastWidth = getWidth(); }
            public void mouseReleased(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)); }
            public void mouseExited(MouseEvent e) { setCursor(Cursor.getDefaultCursor()); }
            public void mouseDragged(MouseEvent e)
            {
                Point mouseLocation = e.getLocationOnScreen();

                if ( (lastWidth + lastClickedX - (int)mouseLocation.getX()) < 450 ) { return; }

                setSize(lastWidth + lastClickedX - (int)mouseLocation.getX(), getHeight());

                setLocation((int)mouseLocation.getX(), (int)getLocation().getY());
                updateResizeBarPositions();
            }
        };
        resizeBar_Left.addMouseListener(resizeBarLeftListener);
        resizeBar_Left.addMouseMotionListener(resizeBarLeftListener);

        MouseInputListener resizeBarBottomListener = new MouseInputListener() {
            public void mouseClicked(MouseEvent e) { }
            public void mousePressed(MouseEvent e) { lastClickedY = (int)e.getLocationOnScreen().getY(); lastHeight = getHeight(); }
            public void mouseReleased(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)); }
            public void mouseExited(MouseEvent e) { setCursor(Cursor.getDefaultCursor()); }
            public void mouseDragged(MouseEvent e)
            {
                Point mouseLocation = e.getLocationOnScreen();
                setSize(getWidth(), lastHeight - (lastClickedY - (int) mouseLocation.getY()));
                updateResizeBarPositions();
            }
        };
        resizeBar_Bottom.addMouseListener(resizeBarBottomListener);
        resizeBar_Bottom.addMouseMotionListener(resizeBarBottomListener);

        MouseInputListener resizeBarRightListener = new MouseInputListener() {
            public void mouseClicked(MouseEvent e) { }
            public void mousePressed(MouseEvent e) { lastClickedX = (int)e.getLocationOnScreen().getX(); lastWidth = getWidth(); }
            public void mouseReleased(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)); }
            public void mouseExited(MouseEvent e) { setCursor(Cursor.getDefaultCursor()); }
            public void mouseDragged(MouseEvent e)
            {
                Point mouseLocation = e.getLocationOnScreen();
                setSize(lastWidth - (lastClickedX - (int) mouseLocation.getX()), getHeight());
                updateResizeBarPositions();
            }
        };
        resizeBar_Right.addMouseListener(resizeBarRightListener);
        resizeBar_Right.addMouseMotionListener(resizeBarRightListener);

        MouseInputListener resizeBarBottomLeftListener = new MouseInputListener() {
            public void mouseClicked(MouseEvent e) { }
            public void mousePressed(MouseEvent e)
            {
                Point mouseLocation = e.getLocationOnScreen();
                lastClickedX = (int)mouseLocation.getX();
                lastClickedY = (int)mouseLocation.getY();
                lastWidth = getWidth();
                lastHeight = getHeight();
            }
            public void mouseReleased(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)); }
            public void mouseExited(MouseEvent e) { setCursor(Cursor.getDefaultCursor()); }
            public void mouseDragged(MouseEvent e)
            {
                Point mouseLocation = e.getLocationOnScreen();

                if ( (lastWidth + lastClickedX - (int)mouseLocation.getX()) < 450 )
                {
                    setLocation((int)getLocation().getX(), (int)getLocation().getY());
                    setSize(getWidth(), lastHeight - (lastClickedY - (int) mouseLocation.getY()));
                }
                else
                {
                    setLocation((int)mouseLocation.getX(), (int)getLocation().getY());
                    setSize(lastWidth + lastClickedX - (int)mouseLocation.getX(), lastHeight - (lastClickedY - (int) mouseLocation.getY()));
                }

                updateResizeBarPositions();
            }
        };
        resizeBar_BottomLeft.addMouseListener(resizeBarBottomLeftListener);
        resizeBar_BottomLeft.addMouseMotionListener(resizeBarBottomLeftListener);

        MouseInputListener resizeBarBottomRightListener = new MouseInputListener() {
            public void mouseClicked(MouseEvent e) { }
            public void mousePressed(MouseEvent e)
            {
                Point mouseLocation = e.getLocationOnScreen();
                lastClickedX = (int)mouseLocation.getX();
                lastClickedY = (int)mouseLocation.getY();
                lastWidth = getWidth();
                lastHeight = getHeight();
            }
            public void mouseReleased(MouseEvent e) { }
            public void mouseMoved(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)); }
            public void mouseExited(MouseEvent e) { setCursor(Cursor.getDefaultCursor()); }
            public void mouseDragged(MouseEvent e)
            {
                Point mouseLocation = e.getLocationOnScreen();
                setSize(lastWidth - (lastClickedX - (int) mouseLocation.getX()), lastHeight - (lastClickedY - (int) mouseLocation.getY()));
                updateResizeBarPositions();
            }
        };
        resizeBar_BottomRight.addMouseListener(resizeBarBottomRightListener);
        resizeBar_BottomRight.addMouseMotionListener(resizeBarBottomRightListener);
    }

    private void addMaxiMiniActionsListeners()
    {
        MouseInputListener maximizeButtonListener = new MouseInputListener()
        {
            public void mouseClicked(MouseEvent e) { toggleMaximize(); }
            public void mouseMoved(MouseEvent e) { }
            public void mouseDragged(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { mouseHoverMaximizeButton = true; maximizeButtonStateChanged(); }
            public void mouseExited(MouseEvent e) { mouseHoverMaximizeButton = false; maximizeButtonStateChanged(); }

            public void mousePressed(MouseEvent e) { mousePressedMaximizeButton = true; maximizeButtonStateChanged(); }
            public void mouseReleased(MouseEvent e) { mousePressedMaximizeButton = false; maximizeButtonStateChanged(); }

        };
        enhancedFrame_maximizeButton.addMouseListener(maximizeButtonListener);
        enhancedFrame_maximizeButton.addMouseMotionListener(maximizeButtonListener);

        MouseInputListener minimizeButtonListener = new MouseInputListener()
        {
            public void mouseClicked(MouseEvent e) { setState(Frame.ICONIFIED); }
            public void mouseMoved(MouseEvent e) { }
            public void mouseDragged(MouseEvent e) { }

            public void mouseEntered(MouseEvent e) { mouseHoverMinimizeButton = true; minimizeButtonStateChanged(); }
            public void mouseExited(MouseEvent e) { mouseHoverMinimizeButton = false; minimizeButtonStateChanged(); }

            public void mousePressed(MouseEvent e) { mousePressedMinimizeButton = true; minimizeButtonStateChanged(); }
            public void mouseReleased(MouseEvent e) { mousePressedMinimizeButton = false; minimizeButtonStateChanged(); }

        };
        enhancedFrame_minimizeButton.addMouseListener(minimizeButtonListener);
        enhancedFrame_minimizeButton.addMouseMotionListener(minimizeButtonListener);
    }

    public void addMinecraftToFrame(final Applet minecraftApplet)
    {
        this.minecraftApplet = minecraftApplet;

        updateAppletSize();

        if ( enhancedFrame )
        {
            add(this.minecraftApplet);

            new Thread() { public void run() {
                try { Thread.currentThread().sleep(10000); } catch ( Exception e ) { }
                firstTimeDone = true;
            } }.start();
        }
        else { add(this.minecraftApplet, BorderLayout.CENTER); }
    }

    private void dragBar_mouseVerification(Point frameLocation, Dimension frameSize, Point cursorPosition)
    {
        if ( !isFocused() ) { dragBar_stateChanged(true); return; }

        int dividedHeight = (int)((frameSize.getHeight() / 100D) * 10D);

        Point topLeftClamp = new Point((int)frameLocation.getX() - 25, (int)frameLocation.getY() - 25);
        Point bottomRightClamp = new Point((int)frameLocation.getX() + (int)frameSize.getWidth() + 25, (int)frameLocation.getY() + dividedHeight);

        dragBar_stateChanged(isPointInZone(cursorPosition, topLeftClamp, bottomRightClamp));
    }

    private void leftResizeBar_mouseVerification(Point frameLocation, Dimension frameSize, Point cursorPosition)
    {
        Point topLeftClamp = new Point((int)frameLocation.getX() - 50, (int)frameLocation.getY() - 5);
        Point bottomRightClamp = new Point((int)frameLocation.getX() + 50, (int)frameLocation.getY() + (int)frameSize.getHeight() + 5);

        resizeBarLeft_stateChanged(isPointInZone(cursorPosition, topLeftClamp, bottomRightClamp));
    }

    private void rightResizeBar_mouseVerification(Point frameLocation, Dimension frameSize, Point cursorPosition)
    {
        Point topLeftClamp = new Point((int)frameLocation.getX() + (int)frameSize.getWidth() - 50, (int)frameLocation.getY() - 5);
        Point bottomRightClamp = new Point((int)frameLocation.getX() + (int)frameSize.getWidth() + 50, (int)frameLocation.getY() + (int)frameSize.getHeight() + 5);

        resizeBarRight_stateChanged(isPointInZone(cursorPosition, topLeftClamp, bottomRightClamp));
    }

    private void bottomResizeBar_mouseVerification(Point frameLocation, Dimension frameSize, Point cursorPosition)
    {
        Point topLeftClamp = new Point((int)frameLocation.getX() - 50, (int)frameLocation.getY() + (int)frameSize.getHeight() - 50);
        Point bottomRightClamp = new Point((int)frameLocation.getX() + (int)frameSize.getWidth() + 50, (int)frameLocation.getY() + (int)frameSize.getHeight() + 50);

        resizeBarBottom_stateChanged(isPointInZone(cursorPosition, topLeftClamp, bottomRightClamp));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Form Helper

    public static GuiForm_GameFrame newForm(boolean visible, String username)
    {
        gameFrame = ( gameFrame == null ) ? ( new GuiForm_GameFrame(username) ) : gameFrame;

        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(visible);

        return gameFrame;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Component Creation

    private void createEnhancedFrameContent()
    {
        if ( enhancedFrame_showMiniMaxiButtons ) { createMiniMaxiButtons(); }
        createTitleBar();
        if ( enhancedFrame_showResizeBars ) { createResizeBars(); }
    }

    private void createMiniMaxiButtons()
    {
        enhancedFrame_maximizeButton = new JPanel(){ @Override public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if ( maximizeButtonImage == null || restoreButtonImage == null ) { return; }
            int x = (this.getWidth() - maximizeButtonImage.getWidth(null)) / 2;
            int y = (this.getHeight() - maximizeButtonImage.getHeight(null)) / 2;
            g.drawImage(isMaximized ? restoreButtonImage : maximizeButtonImage, x, y, null);
        } };
        enhancedFrame_maximizeButton.setBackground(maximizeButtonColor_Normal);
        enhancedFrame_maximizeButton.setOpaque(true);
        enhancedFrame_maximizeButton.setBounds(getWidth()-50-30, 0, 30, 20);
        add(enhancedFrame_maximizeButton);

        enhancedFrame_minimizeButton = new JPanel(){ @Override public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if ( minimizeButtonImage == null ) { return; }
            int x = (this.getWidth() - minimizeButtonImage.getWidth(null)) / 2;
            int y = (this.getHeight() - minimizeButtonImage.getHeight(null)) / 2;
            g.drawImage(minimizeButtonImage, x, y, null);
        } };
        enhancedFrame_minimizeButton.setBackground(minimizeButtonColor_Normal);
        enhancedFrame_minimizeButton.setOpaque(true);
        enhancedFrame_minimizeButton.setBounds(getWidth() - 50 - 30 - 30, 0, 30, 20);
        add(enhancedFrame_minimizeButton);
    }

    private void createTitleBar()
    {
        enhancedFrame_closeButton = new JPanel(){ @Override public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if ( closeButtonImage == null ) { return; }
            int x = (this.getWidth() - closeButtonImage.getWidth(null)) / 2 - 1;
            int y = (this.getHeight() - closeButtonImage.getHeight(null)) / 2;
            g.drawImage(closeButtonImage, x, y, null);
        } };
        enhancedFrame_closeButton.setBackground(closeButtonColor_Normal);
        enhancedFrame_closeButton.setOpaque(true);
        enhancedFrame_closeButton.setBounds(getWidth()-50, 0, 50, 20);
        add(enhancedFrame_closeButton);

        // 2] Drag Panel ( Title Bar )
        enhancedFrame_dragPanel = new JPanel();
        enhancedFrame_dragPanel.setBackground(titleBarColor);
        enhancedFrame_dragPanel.setBounds(0, 0, getWidth(), 20);
        add(enhancedFrame_dragPanel);

        enhancedFrame_dragPanel.setLayout(new BorderLayout());
        enhancedFrame_frameTitle = new JLabel();
        enhancedFrame_frameTitle.setHorizontalAlignment(JLabel.CENTER);
        enhancedFrame_dragPanel.add(enhancedFrame_frameTitle, BorderLayout.CENTER);
    }

    private void createResizeBars()
    {
        // 3] Resize Corners
        resizeBar_BottomLeft = new JPanel();
        resizeBar_BottomLeft.setBackground(Color.black);
        resizeBar_BottomLeft.setBounds(0, getHeight() - 8, 8, 8);
        resizeBar_BottomLeft.setVisible(false);
        add(resizeBar_BottomLeft);

        resizeBar_BottomRight = new JPanel();
        resizeBar_BottomRight.setBackground(Color.black);
        resizeBar_BottomRight.setBounds(getWidth() - 8, getHeight() - 8, 8, 8);
        resizeBar_BottomRight.setVisible(false);
        add(resizeBar_BottomRight);

        // 4] Resize Bars
        resizeBar_Left = new JPanel();
        resizeBar_Left.setBackground(Color.black);
        resizeBar_Left.setBounds(-8, 0, 8, getHeight());
        add(resizeBar_Left);

        resizeBar_Bottom = new JPanel();
        resizeBar_Bottom.setBackground(Color.black);
        resizeBar_Bottom.setBounds(0, getHeight(), getWidth(), 8);
        add(resizeBar_Bottom);

        resizeBar_Right = new JPanel();
        resizeBar_Right.setBackground(Color.black);
        resizeBar_Right.setBounds(getWidth(), 0, 8, getHeight());
        add(resizeBar_Right);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Useful Methods

    private void updateAppletSize()
    {
        // Le +1 est SUPER important !
        // Lorsque l'applet prends la taille de l'écran, la barre de menu est invisible

        minecraftApplet.setBounds(0, 0, getWidth(), getHeight() + (enhancedFrame ? 1 : 0));
    }

    public void setSize(int sizeX, int sizeY)
    {
        checkResizeBarVisibility();

        if ( sizeX < 450 ) { sizeX = 450; }
        if ( sizeY < 253 ) { sizeY = 253; }

        super.setSize(sizeX, sizeY);

        if ( minecraftApplet != null ) { updateAppletSize(); }
        if ( enhancedFrame_dragPanel != null && enhancedFrame )
        {
            setTitleBarNewPosition((int) dragPanelAnimationY);
            updateResizeBarPositions();
            updateFrameShape();
        }
    }

    public void setTitle(String newTitle)
    {
        if ( enhancedFrame_frameTitle != null && enhancedFrame) { enhancedFrame_frameTitle.setText("<html><b>" + newTitle + "</b></html>"); }
        super.setTitle(newTitle);
    }

    private void toggleMaximize()
    {
        if ( isMaximized )
        {
            isMaximized = false;

            setSize(savedSizeX, savedSizeY);
            centerFrameOnScreen();
        }
        else { maximizeFrame(); }
    }

    private void maximizeFrame()
    {
        isMaximized = true;

        savedSizeX = getWidth();
        savedSizeY = getHeight();

        Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());
        Rectangle screenSize = getGraphicsConfiguration().getBounds();

        int startPositionX = screenSize.x + screenInsets.left;
        int startPositionY = screenSize.y + screenInsets.top;

        int sizeX = screenSize.width - ( screenInsets.left + screenInsets.right );
        int sizeY = screenSize.height - ( screenInsets.top + screenInsets.bottom );

        setLocation(startPositionX, startPositionY);
        setSize(sizeX, sizeY);
    }

    private void updateFrameShape()
    {
        if ( !enhancedFrame ) { return; }
        setShape(isMaximized || !enhancedFrame_roundCorners ? null : new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
    }

    private void closeOperation()
    {
        new Thread() { public void run() {
            try { Thread.currentThread().sleep(5000); } catch ( Throwable t ) { t.printStackTrace(); }
            System.exit(1);
        } }.start();

        try { Main_RealLauncher.minecraftInstance.destroy(); } catch ( Throwable t ) { t.printStackTrace(); }
        try { Main_RealLauncher.minecraftInstance.stop(); } catch ( Throwable t ) { t.printStackTrace(); }

        System.exit(0);
    }

    private boolean isPointInZone(Point pointToVerify, Point topLeft, Point bottomRight)
    {
        if ( pointToVerify.getX() < topLeft.getX() ) { return false; }
        if ( pointToVerify.getY() < topLeft.getY() ) { return false; }

        if ( pointToVerify.getX() > bottomRight.getX() ) { return false; }
        if ( pointToVerify.getY() > bottomRight.getY() ) { return false; }

        return true;
    }

    private void checkResizeBarVisibility()
    {
        if ( !enhancedFrame ) { return; }

        if ( resizeBar_Left != null ) { resizeBar_Left.setVisible(!isMaximized); }
        if ( resizeBar_Bottom != null ) { resizeBar_Bottom.setVisible(!isMaximized); }
        if ( resizeBar_Right != null ) { resizeBar_Right.setVisible(!isMaximized); }

        if ( resizeBar_BottomLeft != null ) { resizeBar_BottomLeft.setVisible(!isMaximized && resizeBar_BottomLeft.isVisible()); }
        if ( resizeBar_BottomRight != null ) { resizeBar_BottomRight.setVisible(!isMaximized && resizeBar_BottomRight.isVisible()); }
    }

    private void centerFrameOnScreen()
    {
        Rectangle screenSize = getGraphicsConfiguration().getBounds();
        this.setLocation(screenSize.width/2 - this.getSize().width/2 + screenSize.x, screenSize.height/2 - this.getSize().height/2 + screenSize.y);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Enhanced Frame - Animation

    private void dragPanel_startAnimationProcess() { new Thread() { public void run()
    {
        double counter = 0;
        double newValueY = 0.0D;
        if ( dragPanel_animationState == ANIMATION_SHOW ) { newValueY = 20.0D; }

        while ( dragPanel_animationState != ANIMATION_OFF && (dragPanel_animationState == ANIMATION_HIDE ? Math.round(newValueY) < 20 : Math.round(newValueY) > 1) )
        {
            try { Thread.currentThread().sleep(20); } catch ( Exception e ) { }
            counter++;

            int hideMode;
            if ( dragPanel_animationState == ANIMATION_HIDE ) { hideMode = -1; }
            else if ( dragPanel_animationState == ANIMATION_SHOW ) { hideMode = 1; }
            else { return; }

            newValueY = (Math.cos(counter/6D) * 10.0D * hideMode) + 10.0D;

            setTitleBarNewPosition((int) (dragPanelAnimationY = (-newValueY)));
        }

        dragPanelAnimationY = 0.0D;
        if ( dragPanel_animationState == ANIMATION_HIDE ) { dragPanelAnimationY = -20.0D; }

        setTitleBarNewPosition((int)dragPanelAnimationY);

        dragPanel_animationState = ANIMATION_OFF;
    } }.start(); }

    private void setTitleBarNewPosition(int newPosition)
    {
        if ( !enhancedFrame ) { return; }

        if ( enhancedFrame_dragPanel != null ) { enhancedFrame_dragPanel.setBounds(0, newPosition, getWidth(), 20); }
        if ( enhancedFrame_closeButton != null ) { enhancedFrame_closeButton.setBounds(getWidth()-50, newPosition, 50, 20); }

        if ( !enhancedFrame_showMiniMaxiButtons ) { return; }

        if ( enhancedFrame_maximizeButton != null ) { enhancedFrame_maximizeButton.setBounds(getWidth()-50-30, newPosition, 30, 20); }
        if ( enhancedFrame_minimizeButton != null ) { enhancedFrame_minimizeButton.setBounds(getWidth()-50-30-30, newPosition, 30, 20); }
    }

    private void updateResizeBarPositions()
    {
        if ( resizeBar_Left != null ) { resizeBar_Left.setBounds((int)resizeBarLeftAnimationX, 0, 8, getHeight()); }
        if ( resizeBar_Bottom != null ) { resizeBar_Bottom.setBounds(0, getHeight() - 8 + (int)resizeBarBottomAnimationY, getWidth(), 8); }
        if ( resizeBar_Right != null ) { resizeBar_Right.setBounds(getWidth() - 8 + (int)resizeBarRightAnimationX, 0, 8, getHeight()); }

        if ( resizeBar_BottomLeft != null ) { resizeBar_BottomLeft.setBounds(0, getHeight()-8, 8, 8); }
        if ( resizeBar_BottomRight != null ) { resizeBar_BottomRight.setBounds(getWidth()-8, getHeight()-8, 8, 8); }
    }

    private void dragBar_stateChanged(boolean newState)
    {
        if ( !firstTimeDone ) { return; }

        boolean oldState = dragPanelShowState;
        dragPanelShowState = newState;

        if ( oldState != dragPanelShowState )
        {
            if ( dragPanel_animationState == ANIMATION_OFF ) { dragPanel_startAnimationProcess(); }

            if ( dragPanelShowState ) { dragPanel_animationState = ANIMATION_SHOW; }
            else { dragPanel_animationState = ANIMATION_HIDE; }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Left Resize Bar

    private void resizeBarLeft_stateChanged(boolean newState)
    {
        boolean oldState = resizeBarLeftShowState;
        resizeBarLeftShowState = newState;

        if ( oldState != resizeBarLeftShowState)
        {
            if ( leftResizeBar_animationState == ANIMATION_OFF ) { resizeBarLeft_startAnimationProcess(); }

            if (resizeBarLeftShowState) { leftResizeBar_animationState = ANIMATION_SHOW; }
            else { leftResizeBar_animationState = ANIMATION_HIDE; }
        }
    }

    private void resizeBarLeft_startAnimationProcess() { new Thread() { public void run()
    {
        checkAnimationForCorners();

        double counter = 0;
        double newValueY = 0.0D;
        if ( leftResizeBar_animationState == ANIMATION_SHOW ) { newValueY = 8.0D; }

        while ( leftResizeBar_animationState != ANIMATION_OFF && (leftResizeBar_animationState == ANIMATION_HIDE ? Math.round(newValueY) < 8 : Math.round(newValueY) > 1) )
        {
            try { Thread.currentThread().sleep(20); } catch ( Exception e ) { }
            counter++;

            int hideMode;
            if ( leftResizeBar_animationState == ANIMATION_HIDE ) { hideMode = -1; }
            else if ( leftResizeBar_animationState == ANIMATION_SHOW ) { hideMode = 1; }
            else { return; }

            newValueY = (Math.cos(counter/6D) * 4.0D * hideMode) + 4.0D;

            resizeBarLeftAnimationX = -newValueY;
            updateResizeBarPositions();
        }

        resizeBarLeftAnimationX = 0.0D;
        if ( leftResizeBar_animationState == ANIMATION_HIDE ) { resizeBarLeftAnimationX = -8.0D; }

        updateResizeBarPositions();

        leftResizeBar_animationState = ANIMATION_OFF;

        checkAnimationForCorners();
    } }.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Right Resize Bar

    private void resizeBarRight_stateChanged(boolean newState)
    {
        boolean oldState = resizeBarRightShowState;
        resizeBarRightShowState = newState;

        if ( oldState != resizeBarRightShowState)
        {
            if ( rightResizeBar_animationState == ANIMATION_OFF ) { resizeBarRight_startAnimationProcess(); }

            if (resizeBarRightShowState) { rightResizeBar_animationState = ANIMATION_SHOW; }
            else { rightResizeBar_animationState = ANIMATION_HIDE; }
        }
    }

    private void resizeBarRight_startAnimationProcess() { new Thread() { public void run()
    {
        checkAnimationForCorners();

        double counter = 0;
        double newValueY = 0.0D;
        if ( rightResizeBar_animationState == ANIMATION_SHOW ) { newValueY = 8.0D; }

        while ( rightResizeBar_animationState != ANIMATION_OFF && (rightResizeBar_animationState == ANIMATION_HIDE ? Math.round(newValueY) < 8 : Math.round(newValueY) > 1) )
        {
            try { Thread.currentThread().sleep(20); } catch ( Exception e ) { }
            counter++;

            int hideMode;
            if ( rightResizeBar_animationState == ANIMATION_HIDE ) { hideMode = -1; }
            else if ( rightResizeBar_animationState == ANIMATION_SHOW ) { hideMode = 1; }
            else { return; }

            newValueY = (Math.cos(counter/6D) * 4.0D * hideMode) + 4.0D;

            resizeBarRightAnimationX = newValueY;
            updateResizeBarPositions();
        }

        resizeBarRightAnimationX = 0.0D;
        if ( rightResizeBar_animationState == ANIMATION_HIDE ) { resizeBarRightAnimationX = 8.0D; }

        updateResizeBarPositions();

        rightResizeBar_animationState = ANIMATION_OFF;

        checkAnimationForCorners();
    } }.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Bottom Resize Bar

    private void resizeBarBottom_stateChanged(boolean newState)
    {
        boolean oldState = resizeBarBottomShowState;
        resizeBarBottomShowState = newState;

        if ( oldState != resizeBarBottomShowState)
        {
            if ( bottomResizeBar_animationState == ANIMATION_OFF ) { resizeBarBottom_startAnimationProcess(); }

            if ( resizeBarBottomShowState ) { bottomResizeBar_animationState = ANIMATION_SHOW; }
            else { bottomResizeBar_animationState = ANIMATION_HIDE; }
        }
    }

    private void resizeBarBottom_startAnimationProcess() { new Thread() { public void run()
    {
        checkAnimationForCorners();

        double counter = 0;
        double newValueY = 0.0D;
        if ( bottomResizeBar_animationState == ANIMATION_SHOW ) { newValueY = 8.0D; }

        while ( bottomResizeBar_animationState != ANIMATION_OFF && (bottomResizeBar_animationState == ANIMATION_HIDE ? Math.round(newValueY) < 8 : Math.round(newValueY) > 1) )
        {
            try { Thread.currentThread().sleep(20); } catch ( Exception e ) { }
            counter++;

            int hideMode;
            if ( bottomResizeBar_animationState == ANIMATION_HIDE ) { hideMode = -1; }
            else if ( bottomResizeBar_animationState == ANIMATION_SHOW ) { hideMode = 1; }
            else { return; }

            newValueY = (Math.cos(counter/6D) * 4.0D * hideMode) + 4.0D;

            resizeBarBottomAnimationY = newValueY;
            updateResizeBarPositions();
        }

        resizeBarBottomAnimationY = 0.0D;
        if ( bottomResizeBar_animationState == ANIMATION_HIDE ) { resizeBarBottomAnimationY = 8.0D; }

        updateResizeBarPositions();

        bottomResizeBar_animationState = ANIMATION_OFF;

        checkAnimationForCorners();
    } }.start(); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Corners Resize

    private void checkAnimationForCorners()
    {
        if ( isMaximized )
        {
            resizeBar_BottomLeft.setVisible(false);
            resizeBar_BottomRight.setVisible(false);

            return;
        }

        if ( leftResizeBar_animationState == ANIMATION_OFF && resizeBarLeftShowState ) { resizeBar_BottomLeft.setVisible(true); }
        else if ( bottomResizeBar_animationState == ANIMATION_OFF && resizeBarBottomShowState ) { resizeBar_BottomLeft.setVisible(true); }
        else { resizeBar_BottomLeft.setVisible(false); }

        if ( rightResizeBar_animationState == ANIMATION_OFF && resizeBarRightShowState ) { resizeBar_BottomRight.setVisible(true); }
        else if ( bottomResizeBar_animationState == ANIMATION_OFF && resizeBarBottomShowState ) { resizeBar_BottomRight.setVisible(true); }
        else { resizeBar_BottomRight.setVisible(false); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Close Button Helper

    private static void initializeBufferImages()
    {
        try { closeButtonImage = ImageIO.read(GuiForm_GameFrame.class.getResource("icons/closeButton.png")); }
        catch ( Exception e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'ouvrir l'image : \"closeButton.png\"", false, true); }

        try { maximizeButtonImage = ImageIO.read(GuiForm_GameFrame.class.getResource("icons/maximizeButton.png")); }
        catch ( Exception e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'ouvrir l'image : \"maximizeButton.png\"", false, true); }

        try { restoreButtonImage = ImageIO.read(GuiForm_GameFrame.class.getResource("icons/restoreButton.png")); }
        catch ( Exception e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'ouvrir l'image : \"restoreButton.png\"", false, true); }

        try { minimizeButtonImage = ImageIO.read(GuiForm_GameFrame.class.getResource("icons/minimizeButton.png")); }
        catch ( Exception e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'ouvrir l'image : \"minimizeButton.png\"", false, true); }
    }

    private void closeButtonStateChanged()
    {
        if ( mousePressedCloseButton ) { enhancedFrame_closeButton.setBackground(closeButtonColor_Pressed); }
        else
        {
            if ( mouseHoverCloseButton ) { enhancedFrame_closeButton.setBackground(closeButtonColor_Hover); }
            else { enhancedFrame_closeButton.setBackground(closeButtonColor_Normal); }
        }
    }

    private void maximizeButtonStateChanged()
    {
        if ( mousePressedMaximizeButton ) { enhancedFrame_maximizeButton.setBackground(maximizeButtonColor_Pressed); }
        else
        {
            if ( mouseHoverMaximizeButton ) { enhancedFrame_maximizeButton.setBackground(maximizeButtonColor_Hover); }
            else { enhancedFrame_maximizeButton.setBackground(maximizeButtonColor_Normal); }
        }
    }

    private void minimizeButtonStateChanged()
    {
        if ( mousePressedMinimizeButton ) { enhancedFrame_minimizeButton.setBackground(minimizeButtonColor_Pressed); }
        else
        {
            if ( mouseHoverMinimizeButton ) { enhancedFrame_minimizeButton.setBackground(minimizeButtonColor_Hover); }
            else { enhancedFrame_minimizeButton.setBackground(minimizeButtonColor_Normal); }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
