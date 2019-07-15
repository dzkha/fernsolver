#ifndef CONTOURFILEDIALOGUEFLUX_H
#define CONTOURFILEDIALOGUEFLUX_H
//Begin section for file ContourFileDialogueFlux.h
//TODO: Add definitions that you want preserved
//End section for file ContourFileDialogueFlux.h
#include "../rt.jar/java.awt/Frame.h"
#include "../rt.jar/java.awt/Font.h"
#include "../rt.jar/java.awt/FontMetrics.h"
#include "../rt.jar/java.awt/TextField.h"
#include "../rt.jar/java.awt/Color.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class ContourFileDialogueFlux : Frame
{

    //Begin section for ContourFileDialogueFlux
    //TODO: Add attributes that you want preserved
    //End section for ContourFileDialogueFlux

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font warnFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics warnFontMetrics;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font buttonFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics buttonFontMetrics;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font inputFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics inputFontMetrics;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        TextField hT;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ContourFileDialogueFlux(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame); 



};  //end class ContourFileDialogueFlux



#endif
