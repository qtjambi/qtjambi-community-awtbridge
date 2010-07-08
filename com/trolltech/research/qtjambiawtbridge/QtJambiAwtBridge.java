package com.trolltech.research.qtjambiawtbridge;

import com.trolltech.qt.gui.QWidget;

import java.awt.*;

class QtJambiAwtBridge {
    public static QWidget previousInFocusChain(QWidget w ) {
        QWidget prev = null, next = w.nextInFocusChain();
        while (next != w) {
            prev = next;
            next = next.nextInFocusChain();
        }
        return prev;
    }

    static native Frame getEmbeddedFrame(long widgetNativeId);
}
