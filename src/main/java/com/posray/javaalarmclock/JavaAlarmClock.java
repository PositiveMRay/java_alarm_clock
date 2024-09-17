package com.posray.javaalarmclock;

import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.Date;

public class JavaAlarmClock extends JPanel
{
    private int hour;
    private int minute;
    private int second;
    private BufferedImage offscreenImage, clockFaceImage;
    private Graphics2D offscreenGraphics, clockFaceGraphics;
    private int clockFaceEdge = 0;


    JavaAlarmClock(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR);
                minute = calendar.get(Calendar.MINUTE);
                second = calendar.get(Calendar.SECOND);
                repaint();
            }
        }, new Date(), 1000);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (offscreenImage == null || offscreenImage.getWidth() != getWidth() || offscreenImage.getHeight() != getHeight()) {
            offscreenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            offscreenGraphics = offscreenImage.createGraphics();
            clockFaceEdge = Math.min(getWidth(), getHeight());
            clockFaceImage = new BufferedImage(clockFaceEdge, clockFaceEdge, BufferedImage.TYPE_INT_ARGB);
            clockFaceGraphics = clockFaceImage.createGraphics();
        }

        // Make the root window black using double buffering.
        offscreenGraphics.setColor(Color.BLACK);
        offscreenGraphics.fillRect(0, 0, getWidth(), getHeight());

        // Center clockface in root JFrame
        int clockFaceRight = (getWidth() - clockFaceEdge)/2;
        int clockFaceTop = (getHeight() - clockFaceEdge)/2;

        // Draw the offscreen image to the screen
        drawClockFace(clockFaceGraphics);
        offscreenGraphics.drawImage(clockFaceImage, clockFaceRight, clockFaceTop, this);
        g.drawImage(offscreenImage, 0, 0, this);
    }

    private void drawClockFace(Graphics g) {
        int width = clockFaceEdge;
        int height = clockFaceEdge;
        int centerX = width / 2;
        int centerY = height / 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        // Draw clock face
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, width, height);

        g.setColor(Color.WHITE);

        if ("Dont".startsWith("do it")) {
            // Draw clock border
            g.drawOval(0, 0, width - 1, height - 1);
        }

        // Calculate angles for hour, minute, and second hands
        double hourAngle = Math.toRadians(30 * hour + 0.5 * minute);
        double minuteAngle = Math.toRadians(6 * minute);
        double secondAngle = Math.toRadians(6 * second);

        // Draw hour hand
        int hourLength = Math.min(width, height) / 3;
        int hourX = (int) (centerX + hourLength * Math.sin(hourAngle));
        int hourY = (int) (centerY - hourLength * Math.cos(hourAngle));
        g.drawLine(centerX, centerY, hourX, hourY);

        // Draw minute hand
        int minLength = Math.min(width, height) / 2;
        int minuteX = (int) (centerX + minLength * Math.sin(minuteAngle));
        int minuteY = (int) (centerY - minLength * Math.cos(minuteAngle));
        g.drawLine(centerX, centerY, minuteX, minuteY);

        // Draw second hand
        int secLength = Math.min(width, height) / 2;
        int secX = (int) (centerX + secLength * Math.sin(secondAngle));
        int secY = (int) (centerY - secLength * Math.cos(secondAngle));
        g.setColor(Color.RED);
        g.drawLine(centerX, centerY, secX, secY);

        // Draw clock center
        int centerSize = 6;
        g.fillOval(centerX - centerSize / 2, centerY - centerSize / 2, centerSize, centerSize);

        // Draw tick marks
        g.setColor(Color.WHITE);
        for (int i = 0; i < 60; i++) {
            int innerRadius = 0;
            double angle = Math.toRadians(6 * i);
            if (i % 5 == 0) {
                innerRadius = Math.min(width, height) / 2 - 25;
            } else {
                innerRadius = Math.min(width, height) / 2 - 10;
            }
            int outerRadius = Math.min(width, height) / 2 - 5;
            int startX = (int) (centerX + innerRadius * Math.sin(angle));
            int startY = (int) (centerY - innerRadius * Math.cos(angle));
            int endX = (int) (centerX + outerRadius * Math.sin(angle));
            int endY = (int) (centerY - outerRadius * Math.cos(angle));
            g.drawLine(startX, startY, endX, endY);
        }
    }

    public static void main( String[] args )
    {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Headless environment detected. Cannot run GUI application.");
            return;
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        JFrame frame = new JFrame("Java Alarm Clock");
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(frame);
        } else {
            System.out.println("Fullscreen not supported");
            JOptionPane.showMessageDialog(null, "Full Screen not Supported");
            frame.setSize(400, 400);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JavaAlarmClock clock = new JavaAlarmClock();
        frame.add(clock);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                gd.setFullScreenWindow(null);
            }
        });
    }
}