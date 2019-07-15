#ifndef KBSPLINEPATHINTERPOLATOR_H
#define KBSPLINEPATHINTERPOLATOR_H
//Begin section for file KBSplinePathInterpolator.h
//TODO: Add definitions that you want preserved
//End section for file KBSplinePathInterpolator.h
#include "KBKeyFrame.h"





//KBSplinePathInterpolator&nbsp;behavior.&nbsp;&nbsp;This&nbsp;class&nbsp;defines&nbsp;the&nbsp;base&nbsp;class&nbsp;for<br>all&nbsp;Kochanek-Bartels&nbsp;(also&nbsp;known&nbsp;as&nbsp;TCB&nbsp;or&nbsp;Tension-Continuity-Bias)&nbsp;<br>Spline&nbsp;Path&nbsp;Interpolators.&nbsp;&nbsp;<br><br>@since&nbsp;Java3D&nbsp;1.2
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class KBSplinePathInterpolator
{

    //Begin section for KBSplinePathInterpolator
    //TODO: Add attributes that you want preserved
    //End section for KBSplinePathInterpolator

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int keysLength;



        //Constructs&nbsp;a&nbsp;KBSplinePathInterpolator&nbsp;node&nbsp;with&nbsp;a&nbsp;null&nbsp;alpha&nbsp;value&nbsp;and<br>a&nbsp;null&nbsp;target&nbsp;of&nbsp;TransformGroup<br><br>@since&nbsp;Java&nbsp;3D&nbsp;1.3
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBSplinePathInterpolator(); 



        //Process&nbsp;the&nbsp;new&nbsp;array&nbsp;of&nbsp;key&nbsp;frames
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        virtual int processKeyFrames(KBKeyFrame * keys) = 0; 



    protected:


        //An&nbsp;array&nbsp;of&nbsp;KBKeyFrame&nbsp;for&nbsp;interpolator
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBKeyFrame * keyFrames;



        //This&nbsp;value&nbsp;is&nbsp;the&nbsp;distance&nbsp;between&nbsp;knots&nbsp;<br>value&nbsp;which&nbsp;can&nbsp;be&nbsp;used&nbsp;in&nbsp;further&nbsp;calculations&nbsp;by&nbsp;the&nbsp;subclass.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float currentU;



        //The&nbsp;lower&nbsp;knot
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int lowerKnot;



        //The&nbsp;upper&nbsp;knot
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int upperKnot;



        //@deprecated&nbsp;As&nbsp;of&nbsp;Java&nbsp;3D&nbsp;version&nbsp;1.3,&nbsp;replaced&nbsp;by<br><code>computePathInterpolation(float)</code>
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        virtual int computePathInterpolation() = 0; 



        //This&nbsp;method&nbsp;computes&nbsp;the&nbsp;bounding&nbsp;knot&nbsp;indices&nbsp;and&nbsp;interpolation&nbsp;value<br>"CurrentU"&nbsp;given&nbsp;the&nbsp;current&nbsp;value&nbsp;of&nbsp;the&nbsp;knots[]&nbsp;array&nbsp;and&nbsp;the&nbsp;<br>specified&nbsp;alpha&nbsp;value<br>@param&nbsp;alphaValue&nbsp;alpha&nbsp;value&nbsp;between&nbsp;0.0&nbsp;and&nbsp;1.0<br><br>@since&nbsp;Java&nbsp;3D&nbsp;1.3
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        virtual int computePathInterpolation(float alphaValue) = 0; 



    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const int arrayLength;



        //@deprecated&nbsp;As&nbsp;of&nbsp;Java&nbsp;3D&nbsp;version&nbsp;1.3,&nbsp;replaced&nbsp;by<br><code>KBSplinePathInterpolator(Alpha,&nbsp;TransformGroup,&nbsp;TCBKeyFrame[])&nbsp;</code>
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBSplinePathInterpolator( alpha, KBKeyFrame * keys); 



        //Constructs&nbsp;a&nbsp;new&nbsp;KBSplinePathInterpolator&nbsp;object&nbsp;that&nbsp;interpolates<br>between&nbsp;keyframes&nbsp;with&nbsp;specified&nbsp;alpha,&nbsp;target&nbsp;and&nbsp;an&nbsp;default<br>axisOfTranform&nbsp;set&nbsp;to&nbsp;identity.<br>It&nbsp;takes&nbsp;at&nbsp;least&nbsp;two&nbsp;key&nbsp;frames.&nbsp;The&nbsp;first&nbsp;key&nbsp;<br>frame's&nbsp;knot&nbsp;must&nbsp;have&nbsp;a&nbsp;value&nbsp;of&nbsp;0.0&nbsp;and&nbsp;the&nbsp;last&nbsp;knot&nbsp;must&nbsp;have&nbsp;a<br>value&nbsp;of&nbsp;1.0.&nbsp;&nbsp;An&nbsp;intermediate&nbsp;key&nbsp;frame&nbsp;with&nbsp;index&nbsp;k&nbsp;must&nbsp;have&nbsp;a&nbsp;<br>knot&nbsp;value&nbsp;strictly&nbsp;greater&nbsp;than&nbsp;the&nbsp;knot&nbsp;value&nbsp;of&nbsp;a&nbsp;key&nbsp;frame&nbsp;with&nbsp;<br>index&nbsp;less&nbsp;than&nbsp;k.&nbsp;Once&nbsp;this&nbsp;constructor&nbsp;has&nbsp;all&nbsp;the&nbsp;valid&nbsp;key&nbsp;frames<br>it&nbsp;creates&nbsp;its&nbsp;own&nbsp;list&nbsp;of&nbsp;key&nbsp;fames&nbsp;that&nbsp;duplicates&nbsp;the&nbsp;first&nbsp;key&nbsp;frame<br>at&nbsp;the&nbsp;beginning&nbsp;of&nbsp;the&nbsp;list&nbsp;and&nbsp;the&nbsp;last&nbsp;key&nbsp;frame&nbsp;at&nbsp;the&nbsp;end&nbsp;of&nbsp;the<br>list.&nbsp;<br>@param&nbsp;alpha&nbsp;the&nbsp;alpha&nbsp;object&nbsp;for&nbsp;this&nbsp;interpolator<br>@param&nbsp;target&nbsp;the&nbsp;TransformGroup&nbsp;node&nbsp;affected&nbsp;by&nbsp;this&nbsp;interpolator<br>@param&nbsp;keys&nbsp;an&nbsp;array&nbsp;of&nbsp;KBKeyFrame.&nbsp;Requires&nbsp;at&nbsp;least&nbsp;two&nbsp;key&nbsp;frames.<br><br>@since&nbsp;Java&nbsp;3D&nbsp;1.3
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBSplinePathInterpolator( alpha,  target, KBKeyFrame * keys); 



        //Constructs&nbsp;a&nbsp;new&nbsp;KBSplinePathInterpolator&nbsp;object&nbsp;that&nbsp;interpolates<br>between&nbsp;keyframes&nbsp;with&nbsp;specified&nbsp;alpha,&nbsp;target&nbsp;and&nbsp;axisOfTransform.<br>It&nbsp;takes&nbsp;at&nbsp;least&nbsp;two&nbsp;key&nbsp;frames.&nbsp;The&nbsp;first&nbsp;key&nbsp;<br>frame's&nbsp;knot&nbsp;must&nbsp;have&nbsp;a&nbsp;value&nbsp;of&nbsp;0.0&nbsp;and&nbsp;the&nbsp;last&nbsp;knot&nbsp;must&nbsp;have&nbsp;a<br>value&nbsp;of&nbsp;1.0.&nbsp;&nbsp;An&nbsp;intermediate&nbsp;key&nbsp;frame&nbsp;with&nbsp;index&nbsp;k&nbsp;must&nbsp;have&nbsp;a&nbsp;<br>knot&nbsp;value&nbsp;strictly&nbsp;greater&nbsp;than&nbsp;the&nbsp;knot&nbsp;value&nbsp;of&nbsp;a&nbsp;key&nbsp;frame&nbsp;with&nbsp;<br>index&nbsp;less&nbsp;than&nbsp;k.&nbsp;Once&nbsp;this&nbsp;constructor&nbsp;has&nbsp;all&nbsp;the&nbsp;valid&nbsp;key&nbsp;frames<br>it&nbsp;creates&nbsp;its&nbsp;own&nbsp;list&nbsp;of&nbsp;key&nbsp;fames&nbsp;that&nbsp;duplicates&nbsp;the&nbsp;first&nbsp;key&nbsp;frame<br>at&nbsp;the&nbsp;beginning&nbsp;of&nbsp;the&nbsp;list&nbsp;and&nbsp;the&nbsp;last&nbsp;key&nbsp;frame&nbsp;at&nbsp;the&nbsp;end&nbsp;of&nbsp;the<br>list.&nbsp;<br>@param&nbsp;alpha&nbsp;the&nbsp;alpha&nbsp;object&nbsp;for&nbsp;this&nbsp;interpolator<br>@param&nbsp;target&nbsp;the&nbsp;TransformGroup&nbsp;node&nbsp;affected&nbsp;by&nbsp;this&nbsp;interpolator<br>@param&nbsp;axisOfTransform&nbsp;the&nbsp;transform&nbsp;that&nbsp;defines&nbsp;the&nbsp;local&nbsp;coordinate<br>@param&nbsp;keys&nbsp;an&nbsp;array&nbsp;of&nbsp;KBKeyFrame.&nbsp;Requires&nbsp;at&nbsp;least&nbsp;two&nbsp;key&nbsp;frames<br><br>@since&nbsp;Java&nbsp;3D&nbsp;1.3
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBSplinePathInterpolator( alpha,  target,  axisOfTransform, KBKeyFrame * keys); 



        //This&nbsp;method&nbsp;retrieves&nbsp;the&nbsp;key&nbsp;frame&nbsp;at&nbsp;the&nbsp;specified&nbsp;index.<br>@param&nbsp;index&nbsp;the&nbsp;index&nbsp;of&nbsp;the&nbsp;key&nbsp;frame&nbsp;requested<br>@return&nbsp;the&nbsp;key&nbsp;frame&nbsp;at&nbsp;the&nbsp;associated&nbsp;index
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        virtual KBKeyFrame getKeyFrame(int index) = 0; 



        //Set&nbsp;the&nbsp;key&nbsp;frame&nbsp;at&nbsp;the&nbsp;specified&nbsp;index&nbsp;to&nbsp;<code>keyFrame</code><br>@param&nbsp;index&nbsp;Index&nbsp;of&nbsp;the&nbsp;key&nbsp;frame&nbsp;to&nbsp;change<br>@param&nbsp;keyFrame&nbsp;The&nbsp;new&nbsp;key&nbsp;frame<br>@since&nbsp;Java&nbsp;3D&nbsp;1.3
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        virtual int setKeyFrame(int index, KBKeyFrame keyFrame) = 0; 



        //Copies&nbsp;all&nbsp;KBSplinePathInterpolator&nbsp;information&nbsp;from<br><code>originalNode</code>&nbsp;into<br>the&nbsp;current&nbsp;node.&nbsp;&nbsp;This&nbsp;method&nbsp;is&nbsp;called&nbsp;from&nbsp;the<br><code>cloneNode</code>&nbsp;method&nbsp;which&nbsp;is,&nbsp;in&nbsp;turn,&nbsp;called&nbsp;by&nbsp;the<br><code>cloneTree</code>&nbsp;method.<P>&nbsp;<br><br>@param&nbsp;originalNode&nbsp;the&nbsp;original&nbsp;node&nbsp;to&nbsp;duplicate.<br>@param&nbsp;forceDuplicate&nbsp;when&nbsp;set&nbsp;to&nbsp;<code>true</code>,&nbsp;causes&nbsp;the<br>&nbsp;<code>duplicateOnCloneTree</code>&nbsp;flag&nbsp;to&nbsp;be&nbsp;ignored.&nbsp;&nbsp;When<br>&nbsp;<code>false</code>,&nbsp;the&nbsp;value&nbsp;of&nbsp;each&nbsp;node's<br>&nbsp;<code>duplicateOnCloneTree</code>&nbsp;variable&nbsp;determines&nbsp;whether<br>&nbsp;NodeComponent&nbsp;data&nbsp;is&nbsp;duplicated&nbsp;or&nbsp;copied.<br><br>@exception&nbsp;RestrictedAccessException&nbsp;if&nbsp;this&nbsp;object&nbsp;is&nbsp;part&nbsp;of&nbsp;a&nbsp;live<br>&nbsp;or&nbsp;compiled&nbsp;scenegraph.<br><br>@see&nbsp;Node#duplicateNode<br>@see&nbsp;Node#cloneTree<br>@see&nbsp;NodeComponent#setDuplicateOnCloneTree
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        virtual int duplicateNode( originalNode, boolean forceDuplicate) = 0; 



};  //end class KBSplinePathInterpolator



#endif
