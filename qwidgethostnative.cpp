#include "qwidgethostnative.h"

#include "qtjambi_core.h"

#include <jawt.h>
#include <jawt_md.h>

#include <QtGui/QApplication>

static const char *qtjambi_awt_title = "Qt Jambi / AWT";

QWidgetHostNative::QWidgetHostNative(jobject parentWindow)

#if defined(Q_OS_WIN32)
    : QWIDGETHOSTNATIVE_BASECLASS(getWindowHandle(parentWindow))
#elif defined(Q_OS_MAC)
    : QWIDGETHOSTNATIVE_BASECLASS(parentWindow)
#else
    : QWIDGETHOSTNATIVE_BASECLASS(0)
#endif

{
#if !defined(Q_OS_WIN32) && !defined(Q_OS_MAC)
    embedInto(getWindowHandle(parentWindow));
#endif
}

WId QWidgetHostNative::getWindowHandle(jobject awtWidget)
{
    Q_ASSERT(awtWidget != 0);

    // Call winId() to make sure widget gets a window handle
    QApplication::setAttribute(Qt::AA_NativeWindows);

    JNIEnv *env = qtjambi_current_environment();
    Q_ASSERT(env != 0);

    JAWT awt;
    awt.version = JAWT_VERSION_1_3;
    if (JAWT_GetAWT(env, &awt) == JNI_FALSE) {
        qWarning("%s [%s:%d]: Couldn't get JAWT interface\n", qtjambi_awt_title, __FILE__, __LINE__);
        return false;
    }

    WId awtWindowId = 0;
    {
        JAWT_DrawingSurface *ds;
        ds = awt.GetDrawingSurface(env, awtWidget);
        if (ds == 0) {
            qWarning("%s [%s:%d]: Couldn't get drawing surface\n", qtjambi_awt_title, __FILE__, __LINE__);
            return false;
        }

        jint lock = ds->Lock(ds);
        if (lock & JAWT_LOCK_ERROR) {
            qWarning("%s [%s:%d]: Couldn't lock drawing surface\n", qtjambi_awt_title, __FILE__, __LINE__);
            return false;
        }

        {
            JAWT_DrawingSurfaceInfo *dsi = ds->GetDrawingSurfaceInfo(ds);
            if (dsi == 0) {
                qWarning("%s [%s:%d]: Couldn't get drawing surface info\n", qtjambi_awt_title, __FILE__, __LINE__);
                return false;
            }

#if defined(Q_OS_WIN32)
            awtWindowId = reinterpret_cast<JAWT_Win32DrawingSurfaceInfo *>(dsi->platformInfo)->hwnd;
#elif defined(Q_OS_MAC)
            awtWindowId = (WId)reinterpret_cast<JAWT_MacOSXDrawingSurfaceInfo *>(dsi->platformInfo)->cocoaViewRef;
#else
            awtWindowId = reinterpret_cast<JAWT_X11DrawingSurfaceInfo *>(dsi->platformInfo)->drawable;
#endif
            ds->FreeDrawingSurfaceInfo(dsi);
        }

        ds->Unlock(ds);
        awt.FreeDrawingSurface(ds);
    }

    return awtWindowId;
}

/* ### Empty implementations to work around limitation in generator */
void QWidgetHostNative::childEvent(QChildEvent *e)
{
    QWIDGETHOSTNATIVE_BASECLASS::childEvent(e);
}

bool QWidgetHostNative::eventFilter(QObject *o, QEvent *e)
{
    return QWIDGETHOSTNATIVE_BASECLASS::eventFilter(o, e);
}

void QWidgetHostNative::focusInEvent(QFocusEvent *e)
{
    QWIDGETHOSTNATIVE_BASECLASS::focusInEvent(e);
}

bool QWidgetHostNative::event(QEvent *e)
{
    return QWIDGETHOSTNATIVE_BASECLASS::event(e);
}

void QWidgetHostNative::resizeEvent(QResizeEvent *e)
{
    QWIDGETHOSTNATIVE_BASECLASS::resizeEvent(e);
}
