package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ConcurrentGUI extends JFrame {

    private final JLabel display = new JLabel();
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JButton up = new JButton("Up");
    private final JButton down = new JButton("Down");
    private final JButton stop = new JButton("Stop");

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public ConcurrentGUI() {
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
        final Agent agent = new Agent();
        new Thread(agent).start();
        stop.addActionListener(e -> {
            agent.stopTheCount();
            this.up.setEnabled(false);
            this.down.setEnabled(false);
            this.stop.setEnabled(false);
            });
        up.addActionListener(e -> {
            this.display.setText(Integer.toString(agent.count++));
        });
        down.addActionListener(e -> {
            this.display.setText(Integer.toString(agent.count--));
        });
        }

        private class Agent implements Runnable {

            private volatile boolean stop;
            private volatile int count;
            @Override
            public void run() {
                while (!stop) {
                    try {
                        SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(
                                Integer.toString(Agent.this.count)));
                        this.count++;
                        Thread.sleep(100);
                    } catch (InterruptedException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            public void stopTheCount() {
                this.stop = true;
            }

        }
}
