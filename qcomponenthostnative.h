#ifndef QCOMPONENTHOSTNATIVE_H
#define QCOMPONENTHOSTNATIVE_H

#include <QtGui>

#if defined(QT_JAMBI_RUN)
#  define QCOMPONENTHOSTNATIVE_BASECLASS QWidget
#elif defined(Q_OS_WIN32)
#  include "qwinhost.h"
#  define QCOMPONENTHOSTNATIVE_BASECLASS QWinHost
#else
#  include <QtGui/QX11EmbedContainer>
#  define QCOMPONENTHOSTNATIVE_BASECLASS QX11EmbedContainer
#endif

class QComponentHostNative: public QCOMPONENTHOSTNATIVE_BASECLASS
{
public:
    QComponentHostNative(QWidget *parent = 0);

protected:
    void childEvent(QChildEvent *e);
    bool eventFilter(QObject *o, QEvent *e);
    void focusInEvent(QFocusEvent *e);
    void paintEvent(QPaintEvent *e);
    void resizeEvent(QResizeEvent *e);
    void showEvent(QShowEvent *e);
    void hideEvent(QHideEvent *e);
    bool event(QEvent *e);

};

#endif
