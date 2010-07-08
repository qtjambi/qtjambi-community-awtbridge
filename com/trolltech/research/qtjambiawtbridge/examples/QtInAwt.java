package com.trolltech.research.qtjambiawtbridge.examples;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.research.qtjambiawtbridge.QWidgetHost;

public class QtInAwt extends JFrame {
    
    private static final long serialVersionUID = 1L;

    public QtInAwt() {
        setLayout(new GridLayout(4, 1));
        
        {
            JPanel row = new JPanel();
            row.setLayout(new GridLayout(1, 2));
            row.add(new JLabel("First name:"));
            row.add(new JTextField());
            
            add(row);
        }
        
        {
            JPanel row = new JPanel();
            row.setLayout(new GridLayout(1, 2));            
            row.add(new JLabel("Last name:"));
            row.add(new JTextField());
            
            add(row);
        }
        
        // The Qt part of the GUI
        {
            QWidget row = new QWidget();
            QGridLayout layout = new QGridLayout(row); 
            layout.addWidget(new QLabel("Social security number:"), 0, 0);
            layout.addWidget(new QLineEdit(), 0, 1);

            // Add the Qt widget to AWT's layout            
            add(new QWidgetHost(row));
        }                
        
        {
            QWidget row = new QWidget();
            QGridLayout layout = new QGridLayout(row); 
            layout.addWidget(new QLabel("Phone number:"), 0, 0);
            layout.addWidget(new QLineEdit(), 0, 1);
            
            layout.addWidget(new QLabel("Address:"), 1, 0);
            layout.addWidget(new QLineEdit(), 1, 1);
            
            layout.addWidget(new QLabel("Credit card #:"), 0, 2);
            layout.addWidget(new QLineEdit(), 0, 3);

            layout.addWidget(new QLabel("Expiration date:"), 1, 2);
            layout.addWidget(new QLineEdit(), 1, 3);

            // Add the Qt widget to AWT's layout
            add(new QWidgetHost(row));
        }                
        
    }
    
    public static void main(String args[]) {
        // We have to initialize Qt Jambi in order to use its widgets
        QApplication.initialize(args);
        
        QtInAwt qtInAwt = new QtInAwt();
        qtInAwt.setVisible(true);
        qtInAwt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        qtInAwt.setSize(640, 480);
        
        // We need to run a Qt Jambi event loop in this thread in order
        // for its widgets to receive events
        QApplication.exec();        
    }
    
}
