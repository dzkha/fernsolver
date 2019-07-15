#ifndef MAKEMYDIALOG_H
#define MAKEMYDIALOG_H
//Begin section for file MakeMyDialog.h
//TODO: Add definitions that you want preserved
//End section for file MakeMyDialog.h
#include "../rt.jar/java.awt/Frame.h"
#include "../rt.jar/java.awt/Font.h"
#include "../rt.jar/java.awt/FontMetrics.h"
#include "../rt.jar/java.awt/Color.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class MakeMyDialog : Frame
{

    //Begin section for MakeMyDialog
    //TODO: Add attributes that you want preserved
    //End section for MakeMyDialog

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font warnFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics warnFontMetrics;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int main(const char * args); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        MakeMyDialog(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame); 



};  //end class MakeMyDialog



#endif
