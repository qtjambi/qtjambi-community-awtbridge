package com.trolltech.research.qtjambiawtbridge;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QWidget;

public class QWidgetHost extends Canvas {
	private static final long serialVersionUID = 1L;
           
    private void focusQt(final boolean backwards) {                                                
        QApplication.invokeAndWait(new Runnable() {
            public void run() {                                                           
                QWidget startWidget = backwards ? QtJambiAwtBridge.previousInFocusChain(QWidgetHost.this.containedWidget) 
                                                : QWidgetHost.this.containedWidget.nextInFocusChain();
                QWidget w = startWidget;
                while (w.focusPolicy() != Qt.FocusPolicy.TabFocus 
                       && w.focusPolicy() != Qt.FocusPolicy.StrongFocus) {
                    w = backwards ? QtJambiAwtBridge.previousInFocusChain(w) : w.nextInFocusChain();
                    
                    if (w == startWidget)
                        break;
                }
                
                if (w.focusPolicy() == Qt.FocusPolicy.TabFocus 
                    || w.focusPolicy() == Qt.FocusPolicy.StrongFocus) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                    w.window().activateWindow();                                                                                   
                    w.window().setAttribute(Qt.WidgetAttribute.WA_KeyboardFocusChange, true);                                                        
                    w.setFocus(backwards ? Qt.FocusReason.BacktabFocusReason : Qt.FocusReason.TabFocusReason);
                    w.update();
                }
            }
        });
    }
    
	/**
	 * Constructs a binding layer between AWT and a QWidget. 
     * 
	 * @param containedWidget This widget will become a child of the QWidgetHost on the first AWT paint event.
	 */
	public QWidgetHost(QWidget containedWidget) {
		QApplication.setQuitOnLastWindowClosed(false);
        setFocusable(true);

        childWidget = containedWidget;
        sizeHint = new QSize();
        minimumSizeHint = new QSize();
                                       
        addFocusListener(new FocusAdapter() {
            @Override 
            public void focusGained(FocusEvent e) {                
                if (e.getOppositeComponent() == null) {
                    return ;
                }
                
                Container focusCycleRoot = getFocusCycleRootAncestor();
                boolean backwards =  (e.getOppositeComponent() == focusCycleRoot.getFocusTraversalPolicy().getComponentAfter(focusCycleRoot, QWidgetHost.this));
                focusQt(backwards);
            }
                            
        });
    }

    private QWidget childWidget;
    private QWidgetWrapper containedWidget;

    @Override
    public void paint(Graphics g) {
        if (containedWidget == null) {
            setVisible(false);
            QApplication.invokeAndWait(new Runnable() {
                public void run() {
                    containedWidget = new QWidgetWrapper(childWidget, QWidgetHost.this);
                    sizeHint = containedWidget.sizeHint();
                    minimumSizeHint = containedWidget.minimumSizeHint();
                }
            });
            setVisible(true);
        }

        super.paint(g);
    }

    @Override
	public void setBounds(final int x, final int y, final int width, final int height) {
        if (containedWidget != null) {
            QApplication.invokeAndWait(new Runnable() {
                public void run() {
                    containedWidget.move(0, 0);
                    containedWidget.resize(width, height);
                }
            });
        }

        super.setBounds(x, y, width, height);
	}
	
	@Override
	public Dimension getPreferredSize() {        
		if (containedWidget != null) {
			QApplication.invokeAndWait(new Runnable() {
				public void run() {
					sizeHint = containedWidget.sizeHint();
				}
			});
		} 
		return new Dimension(sizeHint.width(), sizeHint.height());
	}
    QSize sizeHint;
        	
	@Override
	public Dimension getMinimumSize() {
		if (containedWidget != null) {
			QApplication.invokeAndWait(new Runnable() {
				public void run() {
					minimumSizeHint = containedWidget.minimumSizeHint();
				}
			});
		} 
		return new Dimension(minimumSizeHint.width(), minimumSizeHint.height());
	}
	private QSize minimumSizeHint;
}
