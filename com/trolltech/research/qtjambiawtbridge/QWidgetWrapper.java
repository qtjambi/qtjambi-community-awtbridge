package com.trolltech.research.qtjambiawtbridge;

import com.trolltech.research.qtjambiawtbridge.generated.QWidgetHostNative;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.Qt;

import javax.swing.*;
import java.awt.*;

class QWidgetWrapper extends QWidgetHostNative {
    private QWidgetHost host;

    public QWidgetWrapper(QWidget child, QWidgetHost host) {
        super(host);

        // Call this to make sure the widget is connected to a native window
        winId();

        this.host = host;
        child.setParent(this);

        // Make sure widget fills entire parent rect
        QHBoxLayout layout = new QHBoxLayout(this);
        layout.setMargin(0);
        layout.setContentsMargins(0, 0, 0, 0);
        layout.setSpacing(0);
        layout.addWidget(child);

        setFocusPolicy(Qt.FocusPolicy.TabFocus);
        setVisible(true);
    }

    @Override
    public void closeEvent(QCloseEvent e) {
        e.setAccepted(false);
    }

    private void focusAwt(final boolean backwards) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Container c = host.getFocusCycleRootAncestor();

                if (c != null) {
                    host.requestFocus();
                    Component component = backwards
                        ? c.getFocusTraversalPolicy().getComponentBefore(c, host)
                        : c.getFocusTraversalPolicy().getComponentAfter(c, host);

                    component.requestFocus();
                }
            }
        });
    }

    @Override
    protected void showEvent(QShowEvent e) {
        move(0, 0);
        resize(host.getBounds().width, host.getBounds().height);
    }

    @Override
    protected synchronized void focusInEvent(QFocusEvent event) {
        if (event.reason() == Qt.FocusReason.BacktabFocusReason) {
            focusAwt(true);
        } else if (event.reason() == Qt.FocusReason.TabFocusReason) {
            focusAwt(false);
        }
    }
}
