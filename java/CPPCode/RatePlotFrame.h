#ifndef RATEPLOTFRAME_H
#define RATEPLOTFRAME_H
//Begin section for file RatePlotFrame.h
//TODO: Add definitions that you want preserved
//End section for file RatePlotFrame.h
#include "../rt.jar/java.awt/Frame.h"
#include "../rt.jar/java.awt/Font.h"
#include "../rt.jar/java.awt/FontMetrics.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class RatePlotFrame : Frame
{

    //Begin section for RatePlotFrame
    //TODO: Add attributes that you want preserved
    //End section for RatePlotFrame

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static  gp;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font buttonFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics buttonFontMetrics;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int makePS(const char * file); 




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        RatePlotFrame(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int printThisFrame(int xoff, int yoff, boolean makeBorder); 



};  //end class RatePlotFrame



#endif
