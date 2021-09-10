package com.company;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;

public class Drawing extends Canvas implements ChangeListener {

    public static final int IMAGE_SIZE = 512;

    static final int SIZE_MIN = -5;
    static final int SIZE_MAX = 50;
    static final int SIZE_INIT = 20;

    static final int XC_MIN = -50;
    static final int XC_MAX = 50;
    static final int XC_INIT = -7;

    static final int YC_MIN = -50;
    static final int YC_MAX = 50;
    static final int YC_INIT = 0;

    public BufferedImage main_image = new BufferedImage(IMAGE_SIZE,IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);

    private JSlider x_slide;
    private JSlider y_slide;
    private JSlider size_slide;

    public Drawing() {
        setSize(Drawing.IMAGE_SIZE, Drawing.IMAGE_SIZE);
        calculate_colors(XC_INIT / 10.0, YC_INIT / 10.0, SIZE_INIT / 10.0);

        x_slide = new JSlider(JSlider.HORIZONTAL, XC_MIN, XC_MAX, XC_INIT);
        y_slide = new JSlider(JSlider.HORIZONTAL, YC_MIN, YC_MAX, YC_INIT);
        size_slide = new JSlider(JSlider.VERTICAL, SIZE_MIN, SIZE_MAX, SIZE_INIT);

        x_slide.addChangeListener(this);
        y_slide.addChangeListener(this);
        size_slide.addChangeListener(this);

    }

    // return number of iterations to check if c = a + ib is in Mandelbrot set
    public static int mand(Complex z0, int max) {
        Complex z = z0;
        for (int t = 0; t < max; t++) {
            if (z.abs() > 2.0) return t;
            z = z.times(z).plus(z0);
        }
        return max;
    }
    public void calculate_colors(double xc,double yc, double size){
        //double xc   = Double.parseDouble(args[0]);
        //double yc   = Double.parseDouble(args[1]);
        //double size = Double.parseDouble(args[2]);

        int max = 255;   // maximum number of iterations
        int n = IMAGE_SIZE;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double x0 = xc - size/2 + size*i/n;
                double y0 = yc - size/2 + size*j/n;
                Complex z0 = new Complex(x0, y0);
                int gray = max - mand(z0, max);
                Color color = new Color(gray, gray, gray);
                main_image.setRGB(i,n-1-j,color.getRGB());
                //main_image.set(i, n-1-j, color);
            }
        }
    }

    public static void main(String[] args) {
       Drawing my_mandel = new Drawing();

       JFrame frame = new JFrame("My Drawing");

       JPanel slider_panel = new JPanel();
       GridLayout grid =  new GridLayout(0,3);
       slider_panel.setLayout(grid);
       slider_panel.add(my_mandel.x_slide);
       slider_panel.add(my_mandel.y_slide);
       slider_panel.add(my_mandel.size_slide);

       frame.add(my_mandel,BorderLayout.NORTH);
       frame.add(slider_panel,BorderLayout.SOUTH);
       frame.pack();
       frame.setVisible(true);
       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void paint(Graphics g) {
        g.drawImage(main_image,0,0,IMAGE_SIZE, IMAGE_SIZE, null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            double xc = x_slide.getValue() / 10.0;
            double yc = y_slide.getValue() / 10.0;
            double size = size_slide.getValue() / 10.0;
            calculate_colors(xc, yc, size);
            this.repaint();
        }

    }
}
