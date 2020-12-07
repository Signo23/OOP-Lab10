package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AnotherConcurrentGUI extends JFrame {
    private final JLabel display = new JLabel();
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JButton up = new JButton("Up");
    private final JButton down = new JButton("Down");
    private final JButton stop = new JButton("Stop");
    private final Agent agent = new Agent();
    private static final long STOP = TimeUnit.SECONDS.toMillis(10);


    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public AnotherConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(this.display);
        panel.add(this.up);
        panel.add(this.down);
        panel.add(this.stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        stop.addActionListener(e -> stopAll());
        up.addActionListener(e -> agent.up());
        down.addActionListener(e -> agent.down());

        new Thread(agent).start();
        new Thread(new Agent() {
            @Override
            public void run() {
                try {
                    Thread.sleep(STOP);
                    stopAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        }

    private void stopAll() {
        agent.stopTheCount();
        stopButtons();
    }

    public void stopButtons() {
        this.up.setEnabled(false);
        this.down.setEnabled(false);
        this.stop.setEnabled(false);
    }

        private class Agent implements Runnable {

            private volatile boolean stop;
            private volatile boolean up = true;
            private int count;
            @Override
            public void run() {
                while (!stop) {
                    try {
                        SwingUtilities.invokeAndWait(() -> display.setText(
                                Integer.toString(this.count)));
                        this.count += up ? 1 : -1;
                        Thread.sleep(100);
                    } catch (InterruptedException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            public void stopTheCount() {
                this.stop = true;
            }

            public void up() {
                this.up = true;
            }

            public void down() {
                this.up = false;
            }
        }
}
