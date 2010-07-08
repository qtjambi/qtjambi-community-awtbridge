package com.trolltech.research.qtjambiawtbridge.examples;

import com.trolltech.qt.gui.*;
import com.trolltech.research.qtjambiawtbridge.QComponentHost;

import javax.swing.*;
import java.awt.*;

public class AwtInQt extends QWidget {

    public AwtInQt() {
        QGridLayout layout = new QGridLayout(this);

        // A few Qt widgets
        layout.addWidget(new QLabel("First name:"), 0, 0);
        layout.addWidget(new QLineEdit(), 0, 1);
        layout.addWidget(new QLabel("Last name:"), 1, 0);
        layout.addWidget(new QLineEdit(), 1, 1);

        // The AWT part of the GUI
        {
            JPanel panel = new JPanel();

            panel.setLayout(new GridLayout(1, 2));

            panel.add(new JLabel("Social security number:"));
            panel.add(new JTextField());

            // Add the AWT panel to Qt's layout
            layout.addWidget(new QComponentHost(panel), 2, 0, 1, 2);
        }

        {
            JPanel panel = new JPanel();

            panel.setLayout(new GridLayout(2, 4));

            panel.add(new JLabel("Phone number:"));
            panel.add(new JTextField());

            panel.add(new JLabel("Address:"));
            panel.add(new JTextField());

            panel.add(new JLabel("Credit card #:"));
            panel.add(new JTextField());

            panel.add(new JLabel("Expiration date:"));
            panel.add(new JTextField());

            // Add the AWT panel to Qt's layout
            layout.addWidget(new QComponentHost(panel), 4, 0, 1, 2);
        }

    }

    public static void main(String[] args) {
        QApplication.initialize(args);

        AwtInQt awtInQt = new AwtInQt();
        awtInQt.show();

        QApplication.exec();

        System.exit(0);
    }

}
