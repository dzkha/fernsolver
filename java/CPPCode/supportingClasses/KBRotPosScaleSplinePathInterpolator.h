#ifndef KBROTPOSSCALESPLINEPATHINTERPOLATOR_H
#define KBROTPOSSCALESPLINEPATHINTERPOLATOR_H
//Begin section for file KBRotPosScaleSplinePathInterpolator.h
//TODO: Add definitions that you want preserved
//End section for file KBRotPosScaleSplinePathInterpolator.h
#include "KBSplinePathInterpolator.h"
#include "ColorRamping.h"
#include "KBCubicSplineCurve.h"
#include "KBCubicSplineSegment.h"
#include "TimeDisplay.h"
#include "KBKeyFrame.h"





//KBRotPosScaleSplinePathInterpolator&nbsp;behavior.&nbsp;&nbsp;This&nbsp;class&nbsp;defines&nbsp;a&nbsp;<br>behavior&nbsp;that&nbsp;varies&nbsp;the&nbsp;rotational,&nbsp;translational,&nbsp;and&nbsp;scale&nbsp;components&nbsp;<br>of&nbsp;its&nbsp;target&nbsp;TransformGroup&nbsp;by&nbsp;using&nbsp;the&nbsp;Kochanek-Bartels&nbsp;cubic&nbsp;spline&nbsp;<br>interpolation&nbsp;to&nbsp;interpolate&nbsp;among&nbsp;a&nbsp;series&nbsp;of&nbsp;key&nbsp;frames<br>(using&nbsp;the&nbsp;value&nbsp;generated&nbsp;by&nbsp;the&nbsp;specified&nbsp;Alpha&nbsp;object).&nbsp;&nbsp;The<br>interpolated&nbsp;position,&nbsp;orientation,&nbsp;and&nbsp;scale&nbsp;are&nbsp;used&nbsp;to&nbsp;generate<br>a&nbsp;transform&nbsp;in&nbsp;the&nbsp;local&nbsp;coordinate&nbsp;system&nbsp;of&nbsp;this&nbsp;interpolator.&nbsp;&nbsp;
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class KBRotPosScaleSplinePathInterpolator : KBSplinePathInterpolator
{

    //Begin section for KBRotPosScaleSplinePathInterpolator
    //TODO: Add attributes that you want preserved
    //End section for KBRotPosScaleSplinePathInterpolator

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         rotation;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         pitchMat;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         bankMat;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         tMat;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         sMat;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         iPos;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         iScale;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float iHeading;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float iPitch;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float iBank;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         objectCA;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ColorRamping myColorRamp;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         myAlpha;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineCurve cubicSplineCurve;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineSegment * cubicSplineSegments;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int numSegments;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int currentSegmentIndex;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBCubicSplineSegment currentSegment;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         histColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        TimeDisplay myTimeDisplay;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        boolean timeDisplayInterpolate;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        boolean colorRampingInterpolate;




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         targetAppearance;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        TimeDisplay timeDisplayInstance;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ColorRamping colorRamp;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         axisOfRotPosScale;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBKeyFrame * keyFrames;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBRotPosScaleSplinePathInterpolator(); 



        //Constructs&nbsp;a&nbsp;new&nbsp;KBRotPosScaleSplinePathInterpolator&nbsp;object&nbsp;that&nbsp;<br>varies&nbsp;the&nbsp;rotation,&nbsp;translation,&nbsp;and&nbsp;scale&nbsp;of&nbsp;the&nbsp;target&nbsp;<br>TransformGroup's&nbsp;transform.&nbsp;&nbsp;At&nbsp;least&nbsp;2&nbsp;key&nbsp;frames&nbsp;are&nbsp;required&nbsp;for&nbsp;<br>this&nbsp;interpolator.&nbsp;The&nbsp;first&nbsp;key<br>frame's&nbsp;knot&nbsp;must&nbsp;have&nbsp;a&nbsp;value&nbsp;of&nbsp;0.0&nbsp;and&nbsp;the&nbsp;last&nbsp;knot&nbsp;must&nbsp;have&nbsp;a<br>value&nbsp;of&nbsp;1.0.&nbsp;&nbsp;An&nbsp;intermediate&nbsp;key&nbsp;frame&nbsp;with&nbsp;index&nbsp;k&nbsp;must&nbsp;have&nbsp;a<br>knot&nbsp;value&nbsp;strictly&nbsp;greater&nbsp;than&nbsp;the&nbsp;knot&nbsp;value&nbsp;of&nbsp;a&nbsp;key&nbsp;frame&nbsp;with<br>index&nbsp;less&nbsp;than&nbsp;k.<br>@param&nbsp;alpha&nbsp;the&nbsp;alpha&nbsp;object&nbsp;for&nbsp;this&nbsp;interpolator<br>@param&nbsp;target&nbsp;the&nbsp;TransformGroup&nbsp;node&nbsp;affected&nbsp;by&nbsp;this&nbsp;interpolator<br>@param&nbsp;axisOfTransform&nbsp;the&nbsp;transform&nbsp;that&nbsp;specifies&nbsp;the&nbsp;local<br>coordinate&nbsp;system&nbsp;in&nbsp;which&nbsp;this&nbsp;interpolator&nbsp;operates.<br>@param&nbsp;keys&nbsp;an&nbsp;array&nbsp;of&nbsp;key&nbsp;frames&nbsp;that&nbsp;defien&nbsp;the&nbsp;motion&nbsp;path&nbsp;
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBRotPosScaleSplinePathInterpolator( alpha,  target,  axisOfTransform, KBKeyFrame * keys,  targetCA, ColorRamping targetColorRamp); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBRotPosScaleSplinePathInterpolator( alpha,  target,  axisOfTransform, KBKeyFrame * keys, TimeDisplay targetTimeDisplay); 



        //Set&nbsp;the&nbsp;key&nbsp;frame&nbsp;at&nbsp;the&nbsp;specified&nbsp;index&nbsp;to&nbsp;<code>keyFrame</code><br>@param&nbsp;index&nbsp;Index&nbsp;of&nbsp;the&nbsp;key&nbsp;frame&nbsp;to&nbsp;change<br>@param&nbsp;keyFrame&nbsp;The&nbsp;new&nbsp;key&nbsp;frame
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int setKeyFrame(int index, KBKeyFrame keyFrame); 



        //Computes&nbsp;the&nbsp;new&nbsp;transform&nbsp;for&nbsp;this&nbsp;interpolator&nbsp;for&nbsp;a&nbsp;given<br>alpha&nbsp;value.<br><br>@param&nbsp;alphaValue&nbsp;alpha&nbsp;value&nbsp;between&nbsp;0.0&nbsp;and&nbsp;1.0<br>@param&nbsp;transform&nbsp;object&nbsp;that&nbsp;receives&nbsp;the&nbsp;computed&nbsp;transform&nbsp;for<br>the&nbsp;specified&nbsp;alpha&nbsp;value<br><br>@since&nbsp;Java&nbsp;3D&nbsp;1.3
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int computeTransform(float alphaValue,  transform); 



        //Copies&nbsp;KBRotPosScaleSplinePathInterpolator&nbsp;information&nbsp;from<br><code>originalNode</code>&nbsp;into<br>the&nbsp;current&nbsp;node.&nbsp;&nbsp;This&nbsp;method&nbsp;is&nbsp;called&nbsp;from&nbsp;the<br><code>cloneNode</code>&nbsp;method&nbsp;which&nbsp;is,&nbsp;in&nbsp;turn,&nbsp;called&nbsp;by&nbsp;the<br><code>cloneTree</code>&nbsp;method.<P>&nbsp;<br><br>@param&nbsp;originalNode&nbsp;the&nbsp;original&nbsp;node&nbsp;to&nbsp;duplicate.<br>@param&nbsp;forceDuplicate&nbsp;when&nbsp;set&nbsp;to&nbsp;<code>true</code>,&nbsp;causes&nbsp;the<br>&nbsp;<code>duplicateOnCloneTree</code>&nbsp;flag&nbsp;to&nbsp;be&nbsp;ignored.&nbsp;&nbsp;When<br>&nbsp;<code>false</code>,&nbsp;the&nbsp;value&nbsp;of&nbsp;each&nbsp;node's<br>&nbsp;<code>duplicateOnCloneTree</code>&nbsp;variable&nbsp;determines&nbsp;whether<br>&nbsp;NodeComponent&nbsp;data&nbsp;is&nbsp;duplicated&nbsp;or&nbsp;copied.<br><br>@exception&nbsp;RestrictedAccessException&nbsp;if&nbsp;this&nbsp;object&nbsp;is&nbsp;part&nbsp;of&nbsp;a&nbsp;live<br>&nbsp;or&nbsp;compiled&nbsp;scenegraph.<br><br>@see&nbsp;Node#duplicateNode<br>@see&nbsp;Node#cloneTree<br>@see&nbsp;NodeComponent#setDuplicateOnCloneTree
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         cloneNode(boolean forceDuplicate); 



        //Copies&nbsp;KBRotPosScaleSplinePathInterpolator&nbsp;information&nbsp;from<br><code>originalNode</code>&nbsp;into<br>the&nbsp;current&nbsp;node.&nbsp;&nbsp;This&nbsp;method&nbsp;is&nbsp;called&nbsp;from&nbsp;the<br><code>cloneNode</code>&nbsp;method&nbsp;which&nbsp;is,&nbsp;in&nbsp;turn,&nbsp;called&nbsp;by&nbsp;the<br><code>cloneTree</code>&nbsp;method.<P>&nbsp;<br><br>@param&nbsp;originalNode&nbsp;the&nbsp;original&nbsp;node&nbsp;to&nbsp;duplicate.<br>@param&nbsp;forceDuplicate&nbsp;when&nbsp;set&nbsp;to&nbsp;<code>true</code>,&nbsp;causes&nbsp;the<br>&nbsp;<code>duplicateOnCloneTree</code>&nbsp;flag&nbsp;to&nbsp;be&nbsp;ignored.&nbsp;&nbsp;When<br>&nbsp;<code>false</code>,&nbsp;the&nbsp;value&nbsp;of&nbsp;each&nbsp;node's<br>&nbsp;<code>duplicateOnCloneTree</code>&nbsp;variable&nbsp;determines&nbsp;whether<br>&nbsp;NodeComponent&nbsp;data&nbsp;is&nbsp;duplicated&nbsp;or&nbsp;copied.<br><br>@exception&nbsp;RestrictedAccessException&nbsp;if&nbsp;this&nbsp;object&nbsp;is&nbsp;part&nbsp;of&nbsp;a&nbsp;live<br>&nbsp;or&nbsp;compiled&nbsp;scenegraph.<br><br>@see&nbsp;Node#duplicateNode<br>@see&nbsp;Node#cloneTree<br>@see&nbsp;NodeComponent#setDuplicateOnCloneTree
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int duplicateNode( originalNode, boolean forceDuplicate); 



};  //end class KBRotPosScaleSplinePathInterpolator



#endif
