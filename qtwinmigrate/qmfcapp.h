
// Declaration of the QMfcApp classes

#ifndef QMFCAPP_H
#define QMFCAPP_H

#include <QtGui/QApplication>

#if defined(_AFXDLL) && defined(_MSC_VER)
#define QTWINMIGRATE_WITHMFC
class CWinApp;
#endif

#if defined(Q_WS_WIN)
#  if !defined(QT_QTWINMIGRATE_EXPORT) && !defined(QT_QTWINMIGRATE_IMPORT)
#    define QT_QTWINMIGRATE_EXPORT
#  elif defined(QT_QTWINMIGRATE_IMPORT)
#    if defined(QT_QTWINMIGRATE_EXPORT)
#      undef QT_QTWINMIGRATE_EXPORT
#    endif
#    define QT_QTWINMIGRATE_EXPORT __declspec(dllimport)
#  elif defined(QT_QTWINMIGRATE_EXPORT)
#    undef QT_QTWINMIGRATE_EXPORT
#    define QT_QTWINMIGRATE_EXPORT __declspec(dllexport)
#  endif
#else
#  define QT_QTWINMIGRATE_EXPORT
#endif

class QT_QTWINMIGRATE_EXPORT QMfcApp : public QApplication
{
public:
    static bool pluginInstance(Qt::HANDLE plugin = 0);

#ifdef QTWINMIGRATE_WITHMFC
    static int run(CWinApp *mfcApp);
    static QApplication *instance(CWinApp *mfcApp);
    QMfcApp(CWinApp *mfcApp, int &argc, char **argv);
#endif
    ~QMfcApp();

    bool winEventFilter(MSG *msg, long *result);

    static void enterModalLoop();
    static void exitModalLoop();

private:
#ifdef QTWINMIGRATE_WITHMFC
    static char ** mfc_argv;
    static int mfc_argc;
    static CWinApp *mfc_app;
#endif

    int idleCount;
    bool doIdle;
};

#endif // QMFCAPP_H
