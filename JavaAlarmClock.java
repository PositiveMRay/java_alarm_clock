package com.posray.javaalarmclock;

//import static javax.xml.XMLConstants.XML_NS_PREFIX;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
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

    int minEdge = Math.min(getWidth(), getHeight());
    int width = minEdge;
    int height = minEdge;

    // Draw clock face
    g.setColor(Color.BLACK);
    g.fillOval(0, 0, width, height);

    // Draw clock border
    g.setColor(Color.WHITE);
    g.drawOval(0, 0, width - 1, height - 1);

    // Draw clock center
    int centerX = width / 2;
    int centerY = height / 2;
    int centerSize = 6;
    g.fillOval(centerX - centerSize / 2, centerY - centerSize / 2, centerSize, centerSize);

    // Calculate angles for hour, minute, and second hands
    double hourAngle = Math.toRadians(30 * hour + 0.5 * minute);
    double minuteAngle = Math.toRadians(6 * minute);
    double secondAngle = Math.toRadians(6 * second);

    // Draw hour hand
    int hourLength = Math.min(width, height) / 4;
    int hourX = (int) (centerX + hourLength * Math.sin(hourAngle));
    int hourY = (int) (centerY - hourLength * Math.cos(hourAngle));
    g.drawLine(centerX, centerY, hourX, hourY);

    // Draw minute hand
    int minLength = Math.min(width, height) / 3;
    int minuteX = (int) (centerX + minLength * Math.sin(minuteAngle));
    int minuteY = (int) (centerY - minLength * Math.cos(minuteAngle));
    g.drawLine(centerX, centerY, minuteX, minuteY);

    // Draw second hand
    int secLength = Math.min(width, height) / 3;
    int secX = (int) (centerX + secLength * Math.sin(secondAngle));
    int secY = (int) (centerY - secLength * Math.cos(secondAngle));
    g.setColor(Color.RED);
    g.drawLine(centerX, centerY, secX, secY);

    // Draw tick marks
    g.setColor(Color.WHITE);
    for (int i = 0; i < 60; i++) {
        double angle = Math.toRadians(6 * i);
        int innerRadius = Math.min(width, height) / 2 - 10;
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
        JFrame frame = new JFrame("Java Alarm Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setBackground(Color.BLACK);
        frame.setSize(400, 400);
        JavaAlarmClock clock = new JavaAlarmClock();
        frame.add(clock);
        frame.setVisible(true);

        //System.out.println( "Hello Remote World!" );
        //System.out.println("The XML namespace prefix is: " + XML_NS_PREFIX);
    }
}
