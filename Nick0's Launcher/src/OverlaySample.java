import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

/** @see http://stackoverflow.com/a/13437388/230513 */
public class OverlaySample {

    public static void main(String args[]) {
        JFrame frame = new JFrame("Overlay Sample");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new OverlayLayout(panel));
        panel.add(create(1, "One", Color.gray.brighter()));
        panel.add(create(2, "Two", Color.gray));
        panel.add(create(3, "Three", Color.gray.darker()));
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private static JLabel create(final int index, String name, Color color) {
        JLabel label = new JLabel(name) {
            private static final int N = 100;

            @Override
            public boolean isOpaque() {
                return true;
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(index * N, index * N);
            }

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(index * N, index * N);
            }
        };
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setBackground(color);
        return label;
    }
}