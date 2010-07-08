#include "qcomponenthostnative.h"

QComponentHostNative::QComponentHostNative(QWidget *parent)
    : QCOMPONENTHOSTNATIVE_BASECLASS(parent)
{
}

// ### Empty implementations to work around limitation in generator
void QComponentHostNative::childEvent(QChildEvent *e)
{
    QCOMPONENTHOSTNATIVE_BASECLASS::childEvent(e);
}

bool QComponentHostNative::eventFilter(QObject *o, QEvent *e)
{
    return QCOMPONENTHOSTNATIVE_BASECLASS::eventFilter(o, e);
}

void QComponentHostNative::focusInEvent(QFocusEvent *e)
{
    QCOMPONENTHOSTNATIVE_BASECLASS::focusInEvent(e);
}

void QComponentHostNative::paintEvent(QPaintEvent *e)
{
    QCOMPONENTHOSTNATIVE_BASECLASS::paintEvent(e);
}

void QComponentHostNative::resizeEvent(QResizeEvent *e)
{
    QCOMPONENTHOSTNATIVE_BASECLASS::resizeEvent(e);
}

void QComponentHostNative::showEvent(QShowEvent *e)
{
    QCOMPONENTHOSTNATIVE_BASECLASS::showEvent(e);
}

void QComponentHostNative::hideEvent(QHideEvent *e)
{
    QCOMPONENTHOSTNATIVE_BASECLASS::hideEvent(e);
}

bool QComponentHostNative::event(QEvent *e)
{
    return QCOMPONENTHOSTNATIVE_BASECLASS::event(e);
}
