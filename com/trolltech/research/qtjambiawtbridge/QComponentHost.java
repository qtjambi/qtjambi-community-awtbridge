package com.trolltech.research.qtjambiawtbridge;

import com.trolltech.research.qtjambiawtbridge.generated.QComponentHostNative;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.QEvent;

import javax.swing.*;
import java.awt.*;

public final class QComponentHost extends QComponentHostNative {
    static {
        Toolkit.getDefaultToolkit();
        javax.swing.UIManager.getDefaults();
    }

    private Component component;
    private Frame container;
    public QComponentHost(final Component hostedComponent, QWidget parent) {
        super(parent);
        component = hostedComponent;
        setFocusPolicy(Qt.FocusPolicy.TabFocus);
    }

    @Override
    public void resizeEvent(QResizeEvent e) {
        if (container == null)
            return;
        container.setBounds(0, 0, e.size().width(), e.size().height());
    }

    @Override
    public boolean event(QEvent e) {
        return super.event(e);
    }

    @Override
    protected void focusInEvent(QFocusEvent e) {
        final boolean backwards = (e.reason() == Qt.FocusReason.BacktabFocusReason);

        if (!backwards && e.reason() != Qt.FocusReason.TabFocusReason)
            return;

        try {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    focusAwt(backwards);
                }
            });
        } catch (Exception f) {
            f.printStackTrace();
        }
    }
    
    @Override
    public void showEvent(QShowEvent e) {
        // By trial and error, the only way to get the window handle is
        // to put the component inside a Frame (which gets a window handle)
        // and retrieving this in the paint method. Also, we need to make
        // sure the Qt window is visible (if you attach prior to this
        // then showing the window will hang indefinitely) hence we do
        // this in the showEvent
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    container = RedirectContainer.embedComponent(QComponentHost.this, component);
                    container.setVisible(true);
                }
            });
        } catch (Throwable f) { f.printStackTrace(); }
    }

    public QComponentHost(Component hostedComponent) {
        this(hostedComponent, null);
    }

    @Override
    protected void closeEvent(QCloseEvent e) {
        container.setVisible(false);
    }    

    @Override
    public QSize minimumSizeHint() {
        if (container == null)
            return new QSize();

        Dimension dim = container.getMinimumSize();
        return new QSize(dim.width, dim.height);
    }

    @Override
    public QSize sizeHint() {
        if (container == null)
            return new QSize();

        Dimension d = component.getPreferredSize();
        return new QSize((int) d.getWidth(), (int) d.getHeight());
    }

    private void setFocusInQt(QWidget w, Qt.FocusReason reason) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        w.window().activateWindow();
        w.setFocus(reason);
        w.update();
    }

    private void focusAwt(final boolean backwards) {
        container.toFront();

        Component c = null;
        if (component instanceof Container) {
            c = !backwards
                ? container.getFocusTraversalPolicy().getFirstComponent((Container) component)
                : container.getFocusTraversalPolicy().getLastComponent((Container) component);
        } else {
            c = component;
        }

        if (c != null)
            c.requestFocus();

    }
    

    void focusQt(final boolean backwards) {
        QApplication.invokeAndWait(new Runnable() {
            public void run() {
                QWidget startWidget = backwards ? QtJambiAwtBridge.previousInFocusChain(QComponentHost.this)
                                                : nextInFocusChain();
                QWidget w = startWidget;
                while (w.focusPolicy() != Qt.FocusPolicy.TabFocus
                       && w.focusPolicy() != Qt.FocusPolicy.StrongFocus) {
                    w = backwards ? QtJambiAwtBridge.previousInFocusChain(w) : w.nextInFocusChain();

                    if (w == startWidget)
                        break;
                }

                if (w.focusPolicy() == Qt.FocusPolicy.TabFocus
                    || w.focusPolicy() == Qt.FocusPolicy.StrongFocus) {
                    w.window().setAttribute(Qt.WidgetAttribute.WA_KeyboardFocusChange, true);
                    setFocusInQt(w, backwards ? Qt.FocusReason.BacktabFocusReason : Qt.FocusReason.TabFocusReason);
                }
            }
        });
    }

}
