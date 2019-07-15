#ifndef KBCUBICSPLINESEGMENT_H
#define KBCUBICSPLINESEGMENT_H
//Begin section for file KBCubicSplineSegment.h
//TODO: Add definitions that you want preserved
//End section for file KBCubicSplineSegment.h
#include "KBKeyFrame.h"





//The&nbsp;KBCubicSplineSegment&nbsp;class&nbsp;creates&nbsp;the&nbsp;representation&nbsp;of&nbsp;a&nbsp;<br>Kochanek-Bartel's&nbsp;(also&nbsp;known&nbsp;as&nbsp;the&nbsp;TCB&nbsp;or&nbsp;Tension-Continuity-Bias<br>Spline.&nbsp;&nbsp;This&nbsp;class&nbsp;takes&nbsp;4&nbsp;key&nbsp;frames&nbsp;as&nbsp;its&nbsp;input&nbsp;(using&nbsp;KBKeyFrame).&nbsp;<br>If&nbsp;interpolating&nbsp;between&nbsp;the&nbsp;i<sup>th</sup>&nbsp;and&nbsp;(i+1)<sup>th</sup>&nbsp;key&nbsp;<br>frame&nbsp;then&nbsp;the&nbsp;four&nbsp;key&nbsp;frames&nbsp;that&nbsp;need&nbsp;to&nbsp;be&nbsp;specified&nbsp;are&nbsp;the&nbsp;<br>(i-1)<sup>th</sup>,&nbsp;i<sup>th</sup>,&nbsp;(i+1)<sup>th</sup>&nbsp;<br>and&nbsp;(i+2)<sup>th</sup>&nbsp;&nbsp;keyframes&nbsp;in&nbsp;order.&nbsp;The&nbsp;KBCubicSegmentClass<br>then&nbsp;pre-computes&nbsp;the&nbsp;hermite&nbsp;interpolation&nbsp;basis&nbsp;coefficients&nbsp;if&nbsp;the&nbsp;<br>(i+1)<sup>th</sup>&nbsp;frame&nbsp;has&nbsp;the&nbsp;linear&nbsp;flag&nbsp;set&nbsp;to&nbsp;zero.&nbsp;These&nbsp;are&nbsp;used&nbsp;to&nbsp;<br>calculate&nbsp;the&nbsp;interpolated&nbsp;position,&nbsp;scale&nbsp;and&nbsp;quaternions&nbsp;when&nbsp;they&nbsp;<br>requested&nbsp;by&nbsp;the&nbsp;user&nbsp;using&nbsp;the&nbsp;getInterpolated*&nbsp;methods.&nbsp;If&nbsp;the&nbsp;the&nbsp;<br>(i+1)<sup>th</sup>&nbsp;frame's&nbsp;linear&nbsp;flag&nbsp;is&nbsp;set&nbsp;to&nbsp;1&nbsp;then&nbsp;the&nbsp;class&nbsp;uses&nbsp;<br>linear&nbsp;interpolation&nbsp;to&nbsp;calculate&nbsp;the&nbsp;interpolated&nbsp;position,&nbsp;scale,&nbsp;heading&nbsp;<br>pitch&nbsp;and&nbsp;bank&nbsp;it&nbsp;returns&nbsp;through&nbsp;the&nbsp;getInterpolated*&nbsp;methods.&nbsp;<br><br>@since&nbsp;Java3D&nbsp;1.2
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class KBCubicSplineSegment
{

    //Begin section for KBCubicSplineSegment
    //TODO: Add attributes that you want preserved
    //End section for KBCubicSplineSegment

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double * modRoot;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double * modCoeff;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBKeyFrame keyFrame;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         c0;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         c1;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         c2;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         c3;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         e0;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         e1;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         e2;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         e3;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float h0;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float h1;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float h2;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float h3;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float p0;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float p1;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float p2;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float p3;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float b0;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float b1;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float b2;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float b3;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_minus_t_in;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_minus_c_in;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_minus_b_in;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_plus_c_in;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_plus_b_in;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float ddb;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float dda;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_minus_t_out;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_minus_c_out;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_minus_b_out;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_plus_c_out;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float one_plus_b_out;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float dsb;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float dsa;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float length;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int linear;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineSegment(); 



        //Creates&nbsp;a&nbsp;cubic&nbsp;spline&nbsp;segment&nbsp;between&nbsp;two&nbsp;key&nbsp;frames&nbsp;using&nbsp;the&nbsp;<br>key&nbsp;frames&nbsp;provided.&nbsp;If&nbsp;creating&nbsp;a&nbsp;spline&nbsp;between&nbsp;the&nbsp;ith&nbsp;frame&nbsp;and&nbsp;<br>the&nbsp;(i+1)<sup>th</sup>&nbsp;frame&nbsp;then&nbsp;send&nbsp;down&nbsp;the&nbsp;(i&nbsp;-&nbsp;1)<sup>th</sup>,&nbsp;<br>i<sup>th</sup>&nbsp;,&nbsp;(i+1)<sup>th</sup>&nbsp;and&nbsp;the&nbsp;(i+2)<sup>th</sup>&nbsp;key&nbsp;<br>frames.&nbsp;&nbsp;<br><br>@param&nbsp;kf0&nbsp;(i&nbsp;-&nbsp;1)<sup>th</sup>&nbsp;Key&nbsp;Frame<br>@param&nbsp;kf1&nbsp;i<sup>th</sup>&nbsp;Key&nbsp;Frame&nbsp;<br>@param&nbsp;kf2&nbsp;(i&nbsp;+&nbsp;1)<sup>th</sup>&nbsp;Key&nbsp;Frame<br>@param&nbsp;kf3&nbsp;(i&nbsp;+&nbsp;2)<sup>th</sup>&nbsp;Key&nbsp;Frame&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineSegment(KBKeyFrame kf0, KBKeyFrame kf1, KBKeyFrame kf2, KBKeyFrame kf3); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int computeCommonCoefficients(KBKeyFrame kf0, KBKeyFrame kf1, KBKeyFrame kf2, KBKeyFrame kf3); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int computeHermiteCoefficients(KBKeyFrame kf0, KBKeyFrame kf1, KBKeyFrame kf2, KBKeyFrame kf3); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float computeSpeed(float u); 




    public:

        //Computes&nbsp;the&nbsp;length&nbsp;of&nbsp;the&nbsp;curve&nbsp;at&nbsp;a&nbsp;given&nbsp;point&nbsp;between<br>key&nbsp;frames.<br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float computeLength(float u); 



        //Computes&nbsp;the&nbsp;interpolated&nbsp;scale&nbsp;along&nbsp;the&nbsp;curve&nbsp;at&nbsp;a&nbsp;given&nbsp;point<br>between&nbsp;key&nbsp;frames&nbsp;and&nbsp;returns&nbsp;a&nbsp;Point3f&nbsp;with&nbsp;the&nbsp;interpolated&nbsp;<br>x,&nbsp;y,&nbsp;and&nbsp;z&nbsp;scale&nbsp;components.&nbsp;This&nbsp;routine&nbsp;uses&nbsp;linear<br>interpolation&nbsp;if&nbsp;the&nbsp;(i+1)<sup>th</sup>&nbsp;key&nbsp;frame's&nbsp;linear&nbsp;<br>value&nbsp;is&nbsp;equal&nbsp;to&nbsp;1.&nbsp;<br><br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.&nbsp;<br>@param&nbsp;newScale&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;x,y,z&nbsp;scale&nbsp;value&nbsp;in&nbsp;a&nbsp;Point3f
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int getInterpolatedScale(float u,  newScale); 



        //Computes&nbsp;the&nbsp;interpolated&nbsp;position&nbsp;along&nbsp;the&nbsp;curve&nbsp;at&nbsp;a&nbsp;given&nbsp;point<br>between&nbsp;key&nbsp;frames&nbsp;and&nbsp;returns&nbsp;a&nbsp;Point3f&nbsp;with&nbsp;the&nbsp;interpolated&nbsp;<br>x,&nbsp;y,&nbsp;and&nbsp;z&nbsp;scale&nbsp;components.&nbsp;This&nbsp;routine&nbsp;uses&nbsp;linear<br>interpolation&nbsp;if&nbsp;the&nbsp;(i+1)<sup>th</sup>&nbsp;key&nbsp;frame's&nbsp;linear<br>value&nbsp;is&nbsp;equal&nbsp;to&nbsp;1.<br><br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.<br>@param&nbsp;newPos&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;x,y,z&nbsp;position&nbsp;in&nbsp;a&nbsp;Point3f
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int getInterpolatedPosition(float u,  newPos); 



        //Computes&nbsp;the&nbsp;interpolated&nbsp;position&nbsp;along&nbsp;the&nbsp;curve&nbsp;at&nbsp;a&nbsp;given&nbsp;point<br>between&nbsp;key&nbsp;frames&nbsp;and&nbsp;returns&nbsp;a&nbsp;Vector3f&nbsp;with&nbsp;the&nbsp;interpolated&nbsp;<br>x,&nbsp;y,&nbsp;and&nbsp;z&nbsp;scale&nbsp;components.&nbsp;This&nbsp;routine&nbsp;uses&nbsp;linear<br>interpolation&nbsp;if&nbsp;the&nbsp;(i+1)<sup>th</sup>&nbsp;key&nbsp;frame's&nbsp;linear<br>value&nbsp;is&nbsp;equal&nbsp;to&nbsp;1.<br><br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.<br>@param&nbsp;newPos&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;x,y,z&nbsp;position&nbsp;in&nbsp;a&nbsp;Vector3f.&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int getInterpolatedPositionVector(float u,  newPos); 



        //Computes&nbsp;the&nbsp;interpolated&nbsp;heading&nbsp;along&nbsp;the&nbsp;curve&nbsp;at&nbsp;a&nbsp;given&nbsp;point<br>between&nbsp;key&nbsp;frames&nbsp;and&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;value&nbsp;as&nbsp;a&nbsp;float&nbsp;<br>This&nbsp;routine&nbsp;uses&nbsp;linear&nbsp;interpolation&nbsp;if&nbsp;the&nbsp;(i+1)<sup>th</sup>&nbsp;<br>key&nbsp;frame's&nbsp;linear&nbsp;value&nbsp;is&nbsp;equal&nbsp;to&nbsp;1.<br><br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.<br>@return&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;heading&nbsp;value&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float getInterpolatedHeading(float u); 



        //Computes&nbsp;the&nbsp;interpolated&nbsp;pitch&nbsp;along&nbsp;the&nbsp;curve&nbsp;at&nbsp;a&nbsp;given&nbsp;point<br>between&nbsp;key&nbsp;frames&nbsp;and&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;value&nbsp;as&nbsp;a&nbsp;float&nbsp;<br>This&nbsp;routine&nbsp;uses&nbsp;linear&nbsp;interpolation&nbsp;if&nbsp;the&nbsp;(i+1)<sup>th</sup>&nbsp;<br>key&nbsp;frame's&nbsp;linear&nbsp;value&nbsp;is&nbsp;equal&nbsp;to&nbsp;1.<br><br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.<br>@return&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;pitch&nbsp;value&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float getInterpolatedPitch(float u); 



        //Computes&nbsp;the&nbsp;interpolated&nbsp;bank&nbsp;along&nbsp;the&nbsp;curve&nbsp;at&nbsp;a&nbsp;given&nbsp;point<br>between&nbsp;key&nbsp;frames&nbsp;and&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;value&nbsp;as&nbsp;a&nbsp;float&nbsp;<br>This&nbsp;routine&nbsp;uses&nbsp;linear&nbsp;interpolation&nbsp;if&nbsp;the&nbsp;(i+1)<sup>th</sup>&nbsp;<br>key&nbsp;frame's&nbsp;linear&nbsp;value&nbsp;is&nbsp;equal&nbsp;to&nbsp;1.<br><br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.<br>@return&nbsp;returns&nbsp;the&nbsp;interpolated&nbsp;bank&nbsp;value&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float getInterpolatedBank(float u); 



        //Computes&nbsp;the&nbsp;ratio&nbsp;of&nbsp;the&nbsp;length&nbsp;of&nbsp;the&nbsp;spline&nbsp;from&nbsp;the&nbsp;i<sup>th</sup><br>key&nbsp;frame&nbsp;to&nbsp;the&nbsp;position&nbsp;specified&nbsp;by&nbsp;u&nbsp;to&nbsp;the&nbsp;length&nbsp;of&nbsp;the&nbsp;entire<br>spline&nbsp;segment&nbsp;from&nbsp;the&nbsp;i<sup>th</sup>&nbsp;key&nbsp;frame&nbsp;to&nbsp;the&nbsp;(i+1)<br><sup>th</sup>&nbsp;key&nbsp;frame.&nbsp;When&nbsp;the&nbsp;(i+1)<sup>th</sup>&nbsp;key&nbsp;frame's&nbsp;linear<br>value&nbsp;is&nbsp;equal&nbsp;to&nbsp;1,&nbsp;this&nbsp;is&nbsp;meaninful&nbsp;otherwise&nbsp;it&nbsp;should&nbsp;return&nbsp;u.&nbsp;<br><br>@param&nbsp;u&nbsp;specifies&nbsp;the&nbsp;point&nbsp;between&nbsp;keyframes&nbsp;where&nbsp;0&nbsp;<=&nbsp;u&nbsp;<=&nbsp;1.<br>@return&nbsp;the&nbsp;interpolated&nbsp;ratio&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float getInterpolatedValue(float u); 



};  //end class KBCubicSplineSegment



#endif
