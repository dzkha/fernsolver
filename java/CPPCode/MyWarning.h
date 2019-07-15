#ifndef MYWARNING_H
#define MYWARNING_H
//Begin section for file MyWarning.h
//TODO: Add definitions that you want preserved
//End section for file MyWarning.h
#include "../rt.jar/java.awt/Frame.h"
#include "../rt.jar/java.awt/Font.h"
#include "../rt.jar/java.awt/FontMetrics.h"
#include "../rt.jar/java.awt/Color.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class MyWarning : Frame
{

    //Begin section for MyWarning
    //TODO: Add attributes that you want preserved
    //End section for MyWarning

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font warnFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics warnFontMetrics;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        MyWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine); 



};  //end class MyWarning



#endif
