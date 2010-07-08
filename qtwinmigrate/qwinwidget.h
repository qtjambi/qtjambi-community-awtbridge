
// Declaration of the QWinWidget classes

#ifndef QWINWIDGET_H
#define QWINWIDGET_H

#include <QtGui/QWidget>
#include "qmfcapp.h"

class CWnd;

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

class QT_QTWINMIGRATE_EXPORT QWinWidget : public QWidget
{
    Q_OBJECT
public:
    QWinWidget( HWND hParentWnd, QObject *parent = 0, Qt::WFlags f = 0 );
#ifdef QTWINMIGRATE_WITHMFC
    QWinWidget( CWnd *parnetWnd, QObject *parent = 0, Qt::WFlags f = 0 );
#endif
    ~QWinWidget();

    void show();
    void center();
    void showCentered();

    HWND parentWindow() const;

protected:
    void childEvent( QChildEvent *e );
    bool eventFilter( QObject *o, QEvent *e );

    bool focusNextPrevChild(bool next);
    void focusInEvent(QFocusEvent *e);

    bool winEvent(MSG *msg, long *result);

private:
    void init();

    void saveFocus();
    void resetFocus();

    HWND hParent;
    HWND prevFocus;
    bool reenable_parent;
};

#endif // QWINWIDGET_H
