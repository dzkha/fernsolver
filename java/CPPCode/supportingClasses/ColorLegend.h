#ifndef COLORLEGEND_H
#define COLORLEGEND_H
//Begin section for file ColorLegend.h
//TODO: Add definitions that you want preserved
//End section for file ColorLegend.h
#include "../../rt.jar/java.applet/Applet.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class ColorLegend : Applet
{

    //Begin section for ColorLegend
    //TODO: Add attributes that you want preserved
    //End section for ColorLegend

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         ColorLegend;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float x;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float y;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float z;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int N;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float minColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float maxColor;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ColorLegend(float min, float max); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ColorLegend(float min, float max, float xstep, float height, float width, int totalStep); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         createColorLegend(); 



};  //end class ColorLegend



#endif
