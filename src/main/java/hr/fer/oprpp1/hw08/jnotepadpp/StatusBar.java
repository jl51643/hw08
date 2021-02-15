package hr.fer.oprpp1.hw08.jnotepadpp;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Statusbar model
 */
public class StatusBar extends JPanel {

    /**
     * Component label
     */
    private final JLabel left;

    /**
     * Component label
     */
    private final JLabel right;

    /**
     * Document attribute
     */
    private int length, ln, col, sel;

    /**
     * Constructing new statusbar
     */
    public StatusBar() {
        super();
        setLayout(new GridLayout(1, 3));
        setBorder(BorderFactory.createMatteBorder(3, 0, 0, 0, Color.gray));

        left = new JLabel();
        left.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.lightGray));

        right = new JLabel();
        right.setBorder(BorderFactory.createMatteBorder(0, 0,0, 1, Color.lightGray));
        right.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel clockPanel = new JPanel(new BorderLayout());
        clockPanel.add(new Clock(), BorderLayout.CENTER);


        add(left);
        add(right);
        add(clockPanel);
    }

    /**
     * Updates statusbar values
     */
    public void updateStatusBar() {
        left.setText("Length : " + length);
        right.setText("Ln : " + ln + " Col : " + col + " Sel : " + sel);
    }

    /**
     * Sets statusbar value
     *
     * @param length length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Sets statusbar value
     *
     * @param ln line of caret
     */
    public void setLn(int ln) {
        this.ln = ln;
    }

    /**
     * Sets statusbar value
     *
     * @param col colon of caret
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Sets statusbar value
     *
     * @param sel length of selected text
     */
    public void setSel(int sel) {
        this.sel = sel;
    }

    /**
     * Clock model
     */
    static class Clock extends JComponent {

        volatile String time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        public Clock() {
            updateTime();

            Thread t = new Thread(()->{
                while(true) {
                    try {
                        Thread.sleep(500);
                    } catch(Exception ex) {}
                    SwingUtilities.invokeLater(this::updateTime);
                }
            });
            t.setDaemon(true);
            t.start();
        }

        private void updateTime() {
            time = formatter.format(LocalDateTime.now());
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Insets ins = getInsets();
            Dimension dim = getSize();
            Rectangle r = new Rectangle(ins.left, ins.top, dim.width-ins.left-ins.right, dim.height-ins.top-ins.bottom);
            if(isOpaque()) {
                g.setColor(getBackground());
                g.fillRect(r.x, r.y, r.width, r.height);
            }
            g.setColor(getForeground());

            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(time);
            int h = fm.getAscent();

            g.drawString(time, /*r.x + (r.width-w)/2*/getWidth() - w, r.y+r.height-(r.height-h)/2
            );
        }
    }
}
