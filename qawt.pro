!exists($(JAVADIR)) {
   error("Please set your JAVADIR environment variable to point to your local JDK..") ;
}

TARGET = com_trolltech_research_qtjambiawtbridge_generated


QTJAMBI_LOCATION_TMP=$$(JAMBIDIR)
isEmpty(QTJAMBI_LOCATION_TMP) {
    error("Please specify the JAMBIDIR environment variable.");
}

!exists($$(JAMBIDIR)/qtjambi/qtjambi_include.pri) {
    error("$JAMBIDIR/qtjambi/qtjambi_include.pri was not found!");
}



SOURCES += qtjambiawtbridge.cpp qwidgethostnative.cpp qcomponenthostnative.cpp
HEADERS += qwidgethostnative.h qcomponenthostnative.h

include($$(JAMBIDIR)/qtjambi/qtjambi_include.pri)
include(cpp/com_trolltech_research_qtjambiawtbridge_generated/com_trolltech_research_qtjambiawtbridge_generated.pri)



DESTDIR = ./lib
DLLDESTDIR= ./bin

win*{
   INCLUDEPATH += $$PWD/qtwinmigrate
   LIBS += "$$(JAVADIR)/lib/jawt.lib"
   include(qtwinmigrate/qtwinmigrate.pri)
} else {
   INCLUDEPATH += $$PWD/linux
   LIBPATH += $$JAVADIR/lib
   LIBS += -ljawt
}

win32: LIBS += -lgdi32 -luser32 

LIBPATH += $$(JAVADIR)/jre/lib/i386
