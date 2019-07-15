#ifndef KBCUBICSPLINECURVE_H
#define KBCUBICSPLINECURVE_H
//Begin section for file KBCubicSplineCurve.h
//TODO: Add definitions that you want preserved
//End section for file KBCubicSplineCurve.h
#include "KBCubicSplineSegment.h"



class KBKeyFrame; //Dependency Generated Source:KBCubicSplineCurve Target:KBKeyFrame





//KBCubicSplineCurve&nbsp;is&nbsp;a&nbsp;container&nbsp;class&nbsp;that&nbsp;holds&nbsp;a&nbsp;number&nbsp;of&nbsp;<br>KBCubicSplineSegments&nbsp;<br><br>@since&nbsp;Java3D&nbsp;1.2
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class KBCubicSplineCurve
{

    //Begin section for KBCubicSplineCurve
    //TODO: Add attributes that you want preserved
    //End section for KBCubicSplineCurve

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float totalCurveLength;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineSegment * cubicSplineSegment;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineCurve(); 



        //This&nbsp;method&nbsp;takes&nbsp;a&nbsp;list&nbsp;of&nbsp;key&nbsp;frames&nbsp;and&nbsp;creates&nbsp;spline&nbsp;segments<br>from&nbsp;it.&nbsp;It&nbsp;requires&nbsp;at&nbsp;least&nbsp;four&nbsp;key&nbsp;frames&nbsp;to&nbsp;be&nbsp;passed&nbsp;to&nbsp;it.<br>Given&nbsp;n&nbsp;key&nbsp;frames,&nbsp;it&nbsp;creates&nbsp;n-3&nbsp;KBCubicSplineSegments.<br>@param&nbsp;the&nbsp;list&nbsp;of&nbsp;key&nbsp;frames&nbsp;that&nbsp;specify&nbsp;the&nbsp;motion&nbsp;path&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineCurve(KBKeyFrame * keys); 



        //This&nbsp;method&nbsp;takes&nbsp;a&nbsp;list&nbsp;of&nbsp;spline&nbsp;segments&nbsp;creates&nbsp;the&nbsp;<br>KBCubicSplineCurve.&nbsp;<br>@param&nbsp;the&nbsp;list&nbsp;of&nbsp;segments&nbsp;that&nbsp;comprise&nbsp;the&nbsp;complete&nbsp;motion&nbsp;path
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineCurve(KBCubicSplineSegment * s); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int computeTotalCurveLength(); 




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int numSegments;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineSegment * segments;



        //This&nbsp;method&nbsp;returns&nbsp;the&nbsp;KBCubicSplineSegments&nbsp;pointed&nbsp;to&nbsp;by&nbsp;index&nbsp;<br>@param&nbsp;the&nbsp;index&nbsp;of&nbsp;the&nbsp;KBCubicSplineSegment&nbsp;required&nbsp;<br>@return&nbsp;the&nbsp;KBCubicSplineSegment&nbsp;pointed&nbsp;to&nbsp;by&nbsp;index
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineSegment getSegment(int index); 



};  //end class KBCubicSplineCurve



#endif
