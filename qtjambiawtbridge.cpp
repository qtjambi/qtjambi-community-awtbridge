#include <qtjambi_core.h>

#include <QtGui/QWidget>

#include <jni.h>

const char *qtjambi_awt_title = "Qt Jambi / AWT";

extern "C" JNIEXPORT jobject JNICALL Java_com_trolltech_research_qtjambiawtbridge_QtJambiAwtBridge_getEmbeddedFrame
(JNIEnv *env, jclass, jlong widgetNativeId)
{
#ifdef Q_OS_WIN32
    const char *embeddedFramePackageName = "sun/awt/windows/";
    const char *embeddedFrameClassName = "WEmbeddedFrame";
#else
    const char *embeddedFramePackageName = "sun/awt/X11/";
    const char *embeddedFrameClassName = "XEmbeddedFrame";
#endif

    QWidget *widget = reinterpret_cast<QWidget *>(qtjambi_from_jlong(widgetNativeId));
    if (widget == 0) {
        qWarning("%s [%s:%d]: Component host widget has been deleted\n", qtjambi_awt_title, __FILE__, __LINE__);
        return 0;
    }

    jclass embeddedFrameClass = resolveClass(env, embeddedFrameClassName, embeddedFramePackageName);
    if (embeddedFrameClass == 0) {
        qWarning("%s [%s:%d]: Embedded frame class '%s%s' not found\n",
            qtjambi_awt_title, __FILE__, __LINE__, embeddedFramePackageName, embeddedFrameClassName);
        return 0;
    }

    jmethodID constructor = resolveMethod(env, "<init>", "(I)V",
                                          embeddedFrameClassName,
                                          embeddedFramePackageName);
    // Different constructors on different JREs.
    if (constructor == 0) {
        env->ExceptionClear();
        constructor = resolveMethod(env, "<init>", "(J)V",
                                    embeddedFrameClassName,
                                    embeddedFramePackageName);
    }

    if (constructor == 0) {
        qWarning("%s [%s:%d]: Cannot find constructor in embedded frame class\n", qtjambi_awt_title, __FILE__, __LINE__);
        return 0;
    }

    WId windowId = widget->winId();

    return env->NewObject(embeddedFrameClass, constructor, windowId);
}


