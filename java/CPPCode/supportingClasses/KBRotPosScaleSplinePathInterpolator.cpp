#include "KBRotPosScaleSplinePathInterpolator.h"
//Begin section for file KBRotPosScaleSplinePathInterpolator.cpp
//TODO: Add definitions that you want preserved
//End section for file KBRotPosScaleSplinePathInterpolator.cpp


//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
KBRotPosScaleSplinePathInterpolator::KBRotPosScaleSplinePathInterpolator() : 
rotation(new Transform3D()),pitchMat(new Matrix4d()),bankMat(new Matrix4d()),tMat(new Matrix4d()),sMat(new Matrix4d()),iPos(new Vector3f()),iScale(new Point3f()),myAlpha(new Alpha()),cubicSplineCurve(new KBCubicSplineCurve()),histColor(new Color3f())
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
KBRotPosScaleSplinePathInterpolator::KBRotPosScaleSplinePathInterpolator( alpha,  target,  axisOfTransform, KBKeyFrame * keys,  targetCA, ColorRamping targetColorRamp) : 
rotation(new Transform3D()),pitchMat(new Matrix4d()),bankMat(new Matrix4d()),tMat(new Matrix4d()),sMat(new Matrix4d()),iPos(new Vector3f()),iScale(new Point3f()),myAlpha(new Alpha()),cubicSplineCurve(new KBCubicSplineCurve()),histColor(new Color3f())
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
KBRotPosScaleSplinePathInterpolator::KBRotPosScaleSplinePathInterpolator( alpha,  target,  axisOfTransform, KBKeyFrame * keys, TimeDisplay targetTimeDisplay) : 
rotation(new Transform3D()),pitchMat(new Matrix4d()),bankMat(new Matrix4d()),tMat(new Matrix4d()),sMat(new Matrix4d()),iPos(new Vector3f()),iScale(new Point3f()),myAlpha(new Alpha()),cubicSplineCurve(new KBCubicSplineCurve()),histColor(new Color3f())
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBRotPosScaleSplinePathInterpolator::setKeyFrame(int index, KBKeyFrame keyFrame) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBRotPosScaleSplinePathInterpolator::computeTransform(float alphaValue,  transform) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
 KBRotPosScaleSplinePathInterpolator::cloneNode(boolean forceDuplicate) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int KBRotPosScaleSplinePathInterpolator::duplicateNode( originalNode, boolean forceDuplicate) 
{
    //TODO Auto-generated method stub
    return 0;
}
