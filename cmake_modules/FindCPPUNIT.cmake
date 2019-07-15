#
# Find the CppUnit includes and library
#
# This module defines
# CPPUNIT_INCLUDE_DIR, where to find tiff.h, etc.
# CPPUNIT_LIBRARIES, the libraries to link against to use CppUnit.
# CPPUNIT_FOUND, If false, do not try to use CppUnit.

# also defined, but not for general use are
# CPPUNIT_LIBRARY, where to find the CppUnit library.
# CPPUNIT_DEBUG_LIBRARY, where to find the CppUnit library in debug
# mode.

SET(CPPUNIT_FOUND "NO")

FIND_PATH(CPPUNIT_INCLUDE_DIR /opt/cppunit-1.12.1_gcc-4.4.6)

# With Win32, important to have both
#IF(WIN32)
#  FIND_LIBRARY(CPPUNIT_LIBRARY cppunit
#               ${CPPUNIT_INCLUDE_DIR}/../lib
#               /usr/local/lib
#               /usr/lib)
#  FIND_LIBRARY(CPPUNIT_DEBUG_LIBRARY cppunitd
#               ${CPPUNIT_INCLUDE_DIR}/../lib
#               /usr/local/lib
#               /usr/lib)
#ELSE(WIN32)
  # On unix system, debug and release have the same name
FIND_LIBRARY(CPPUNIT_LIBRARY cppunit
               ${CPPUNIT_INCLUDE_DIR}/../lib
               /opt/cppunit-1.12.1_gcc-4.4.6/lib)
              
FIND_LIBRARY(CPPUNIT_DEBUG_LIBRARY cppunit
               ${CPPUNIT_INCLUDE_DIR}/../lib
               /opt/cppunit-1.12.1_gcc-4.4.6/lib)

FIND_PATH(CPPUNIT_INCLUDE_DIR Test.h
        PATH_SUFFIXES
        include
        include/cppunit
        PATHS
        /usr/local
        /usr
        ~/usr/local/cppunit-1.12.1
        /opt/cppunit-1.12.1_gcc-4.4.6/
)
       
MESSAGE(STATUS "Searching for CPPUnit")
#ENDIF(WIN32)
MESSAGE(STATUS ${CPPUNIT_LIBRARY})
IF(CPPUNIT_INCLUDE_DIR)
  IF(CPPUNIT_LIBRARY)
    SET(CPPUNIT_FOUND "YES")
    SET(CPPUNIT_LIBRARIES ${CPPUNIT_LIBRARY} ${CMAKE_DL_LIBS})
    SET(CPPUNIT_DEBUG_LIBRARIES ${CPPUNIT_DEBUG_LIBRARY} ${CMAKE_DL_LIBS})
  ELSE (CPPUNIT_LIBRARY)
    IF (CPPUNIT_FIND_REQUIRED)
      MESSAGE(SEND_ERROR "Could not find library CppUnit1.")
    ENDIF (CPPUNIT_FIND_REQUIRED)
  ENDIF(CPPUNIT_LIBRARY)
ELSE(CPPUNIT_INCLUDE_DIR)
  IF (CPPUNIT_FIND_REQUIRED)
    MESSAGE(SEND_ERROR "Could not find library CppUnit2.")
  ENDIF(CPPUNIT_FIND_REQUIRED)
ENDIF(CPPUNIT_INCLUDE_DIR)
