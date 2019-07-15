#ifndef TIMEDISPLAY_H
#define TIMEDISPLAY_H
//Begin section for file TimeDisplay.h
//TODO: Add definitions that you want preserved
//End section for file TimeDisplay.h
#include "../../rt.jar/java.awt/TextField.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class TimeDisplay
{

    //Begin section for TimeDisplay
    //TODO: Add attributes that you want preserved
    //End section for TimeDisplay

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double maxTime;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double minTime;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double curTime;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const char * curTimeStr;




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        TextField timeText;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        TimeDisplay(double minimumTime, double maximumTime); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        TimeDisplay(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int setMinMaxTime(double minimumTime, double maximumTime); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int updateDisplayFromAlpha(float alphaValue); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int updateDisplayFromTime(double curTime); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double getTimeFromAlpha(float alphaValue); 



};  //end class TimeDisplay



#endif
