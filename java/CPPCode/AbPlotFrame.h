#ifndef ABPLOTFRAME_H
#define ABPLOTFRAME_H
//Begin section for file AbPlotFrame.h
//TODO: Add definitions that you want preserved
//End section for file AbPlotFrame.h
#include "../rt.jar/java.awt/Frame.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class AbPlotFrame : Frame
{

    //Begin section for AbPlotFrame
    //TODO: Add attributes that you want preserved
    //End section for AbPlotFrame

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static  gp;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int makePS(const char * file); 




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        AbPlotFrame(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int printThisFrame(int xoff, int yoff, boolean makeBorder); 



};  //end class AbPlotFrame



#endif
