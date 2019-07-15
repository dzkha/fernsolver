#ifndef SHOWISOTOPEFLUX_H
#define SHOWISOTOPEFLUX_H
//Begin section for file ShowIsotopeFlux.h
//TODO: Add definitions that you want preserved
//End section for file ShowIsotopeFlux.h
#include "../rt.jar/java.awt/Color.h"
#include "ContourFrameFlux.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class ShowIsotopeFlux
{

    //Begin section for ShowIsotopeFlux
    //TODO: Add attributes that you want preserved
    //End section for ShowIsotopeFlux

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Color isoBC;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static ContourFrameFlux cd;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ShowIsotopeFlux(const char * title, const char * contourLegend, boolean isLog, double lower, double upper, double * twa); 



};  //end class ShowIsotopeFlux



#endif
