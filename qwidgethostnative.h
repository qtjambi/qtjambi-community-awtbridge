#ifndef QWIDGETHOSTNATIVE_H
#define QWIDGETHOSTNATIVE_H

#include <QtGui>

#if defined(QT_JAMBI_RUN)
#  define QWIDGETHOSTNATIVE_BASECLASS QWidget
#elif defined(Q_OS_WIN32)
#  include "qwinwidget.h"
#  define QWIDGETHOSTNATIVE_BASECLASS QWinWidget
#elif defined(Q_OS_MAC)
#  include <QMacNativeWidget>
#  define QWIDGETHOSTNATIVE_BASECLASS QMacNativeWidget
#else
#  include <QtGui/QX11EmbedWidget>
#  define QWIDGETHOSTNATIVE_BASECLASS QX11EmbedWidget
#endif

#include <jni.h>

class QWidgetHostNative: public QWIDGETHOSTNATIVE_BASECLASS
{
public:
    QWidgetHostNative(jobject parentWindow);

protected:
    void childEvent(QChildEvent *e);
    bool eventFilter(QObject *o, QEvent *e);
    void focusInEvent(QFocusEvent *e);
    bool event(QEvent *);
    void resizeEvent(QResizeEvent *);

private:
    WId getWindowHandle(jobject window);
};

#endif
