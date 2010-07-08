package com.trolltech.research.qtjambiawtbridge;

import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


// sun.awt.x11.embeddedFrame on Linux
// see text file for osx info
class RedirectContainer {

    private static class FocusComponent extends Component {
        private static final long serialVersionUID = 1L;
        private QComponentHost host;

        public FocusComponent(final boolean isFocusIn, final QComponentHost host) {
            this.host = host;
            
            setFocusable(true);
            setBounds(0, 0, 1, 1);

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {

                    if (e.getOppositeComponent() == null) {
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
                        return ;
                    }

                    if (isFocusIn) {
                        host.focusQt(true);
                    } else {
                        host.focusQt(false);
                    }
                }

            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1, 1);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(1, 1);
        }
    }

    private static final long serialVersionUID = 1L;

    static Frame embedComponent(QComponentHost parent, Component component) {
        Frame f = QtJambiAwtBridge.getEmbeddedFrame(parent.nativeId());
        if (f != null) {
            // Connect the frame to its window handle, since it's no longer
            // handled by Swing (for event processing)
            f.addNotify();
            
            GridBagLayout layout = new GridBagLayout();

            f.setLayout(layout);
            f.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
            f.setFocusTraversalPolicyProvider(true);

            GridBagConstraints focusConstraints = new GridBagConstraints();
            focusConstraints.ipadx = 0;
            focusConstraints.ipady = 0;
            focusConstraints.anchor = GridBagConstraints.CENTER;
            focusConstraints.fill = GridBagConstraints.NONE;
            focusConstraints.insets.left = 0;
            focusConstraints.insets.right = 0;
            focusConstraints.insets.bottom = 0;
            focusConstraints.insets.top = 0;

            FocusComponent focusIn = new FocusComponent(true, parent);

            layout.setConstraints(focusIn, focusConstraints);
            f.add(focusIn);

            {
                GridBagConstraints constraints = (GridBagConstraints) focusConstraints.clone();
                constraints.fill = GridBagConstraints.BOTH;
                constraints.weightx = 1;
                constraints.weighty = 1;

                f.add(component);
            }

            FocusComponent focusOut = new FocusComponent(false, parent);
            layout.setConstraints(focusOut, focusConstraints);
            f.add(focusOut);

            f.pack();
        }

        return f;
    }

}
