#ifndef HORSTRING_H
#define HORSTRING_H
//Begin section for file HorString.h
//TODO: Add definitions that you want preserved
//End section for file HorString.h
#include "../rt.jar/java.awt/Font.h"
#include "../rt.jar/java.awt/FontMetrics.h"
#include "../rt.jar/java.awt/Graphics.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class HorString
{

    //Begin section for HorString
    //TODO: Add attributes that you want preserved
    //End section for HorString

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int w;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int length;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int newx;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const char * letter;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        HorString(const char * s, int x, int y, int leading, Font f, FontMetrics fm, Graphics g); 





};  //end class HorString



#endif
