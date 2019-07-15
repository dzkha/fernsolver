#include "KBCubicSplineSegment.h"
//Begin section for file KBCubicSplineSegment.cpp
//TODO: Add definitions that you want preserved
//End section for file KBCubicSplineSegment.cpp


//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double * KBCubicSplineSegment::modRoot = {0.046910077,0.230765345,0.5,0.769234655,0.953089922};

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double * KBCubicSplineSegment::modCoeff = {0.118463442,0.239314335,0.284444444,0.239314335,0.118463442};

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
KBCubicSplineSegment::KBCubicSplineSegment() : 
keyFrame(new KBKeyFrame[4])
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
KBCubicSplineSegment::KBCubicSplineSegment(KBKeyFrame kf0, KBKeyFrame kf1, KBKeyFrame kf2, KBKeyFrame kf3) : 
keyFrame(new KBKeyFrame[4])
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBCubicSplineSegment::computeCommonCoefficients(KBKeyFrame kf0, KBKeyFrame kf1, KBKeyFrame kf2, KBKeyFrame kf3) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBCubicSplineSegment::computeHermiteCoefficients(KBKeyFrame kf0, KBKeyFrame kf1, KBKeyFrame kf2, KBKeyFrame kf3) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
float KBCubicSplineSegment::computeLength(float u) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
float KBCubicSplineSegment::computeSpeed(float u) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBCubicSplineSegment::getInterpolatedScale(float u,  newScale) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBCubicSplineSegment::getInterpolatedPosition(float u,  newPos) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBCubicSplineSegment::getInterpolatedPositionVector(float u,  newPos) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
float KBCubicSplineSegment::getInterpolatedHeading(float u) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
float KBCubicSplineSegment::getInterpolatedPitch(float u) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
float KBCubicSplineSegment::getInterpolatedBank(float u) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
float KBCubicSplineSegment::getInterpolatedValue(float u) 
{
    //TODO Auto-generated method stub
}
