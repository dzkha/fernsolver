#ifndef TIMESTEPBEHAVIOR_H
#define TIMESTEPBEHAVIOR_H
//Begin section for file TimestepBehavior.h
//TODO: Add definitions that you want preserved
//End section for file TimestepBehavior.h
#include "Histogram3D.h"
#include "ColorRamping.h"
#include "../../java.util.jar/java.util/Enumeration.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class TimestepBehavior
{

    //Begin section for TimestepBehavior
    //TODO: Add attributes that you want preserved
    //End section for TimestepBehavior

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         targetTG;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         targetT3D;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         posVector;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int step;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Histogram3D targetHistogram;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int targetMaxProton;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int targetMaxNeutron;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ColorRamping targetColorRamp;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         histColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         ca;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        TimestepBehavior(Histogram3D histogram, int maxProton, int maxNeutron, ColorRamping colorRamp); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int initialize(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int processStimulus(Enumeration criteria); 



};  //end class TimestepBehavior



#endif
