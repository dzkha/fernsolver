#ifndef KBKEYFRAME_H
#define KBKEYFRAME_H
//Begin section for file KBKeyFrame.h
//TODO: Add definitions that you want preserved
//End section for file KBKeyFrame.h




//This&nbsp;class&nbsp;represents&nbsp;a&nbsp;Key&nbsp;Frame&nbsp;that&nbsp;can&nbsp;be&nbsp;used&nbsp;for&nbsp;Kochanek-Bartels<br>(also&nbsp;called&nbsp;TCB&nbsp;or&nbsp;Tension-Continuity-Bias&nbsp;Splines)&nbsp;spline&nbsp;interpolation.<br><br>@since&nbsp;Java3D&nbsp;1.2
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class KBKeyFrame
{

    //Begin section for KBKeyFrame
    //TODO: Add attributes that you want preserved
    //End section for KBKeyFrame


    private:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBKeyFrame(); 




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         position;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float heading;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float pitch;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float bank;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         scale;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float tension;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float continuity;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float bias;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float knot;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int linear;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBKeyFrame(KBKeyFrame kf); 



        //Creates&nbsp;a&nbsp;key&nbsp;frame&nbsp;using&nbsp;the&nbsp;given&nbsp;inputs.&nbsp;<br><br>@param&nbsp;k&nbsp;knot&nbsp;value&nbsp;for&nbsp;this&nbsp;key&nbsp;frame&nbsp;<br>@param&nbsp;l&nbsp;the&nbsp;linear&nbsp;flag&nbsp;(0&nbsp;-&nbsp;Spline&nbsp;Interp,&nbsp;1,&nbsp;Linear&nbsp;Interp<br>@param&nbsp;pos&nbsp;the&nbsp;position&nbsp;at&nbsp;the&nbsp;key&nbsp;frame<br>@param&nbsp;hd&nbsp;the&nbsp;heading&nbsp;value&nbsp;at&nbsp;the&nbsp;key&nbsp;frame<br>@param&nbsp;pi&nbsp;the&nbsp;pitch&nbsp;value&nbsp;at&nbsp;the&nbsp;key&nbsp;frame<br>@param&nbsp;bk&nbsp;the&nbsp;bank&nbsp;value&nbsp;at&nbsp;the&nbsp;key&nbsp;frame<br>@param&nbsp;s&nbsp;the&nbsp;scales&nbsp;at&nbsp;the&nbsp;key&nbsp;frame<br>@param&nbsp;t&nbsp;tension&nbsp;(-1.0&nbsp;<&nbsp;t&nbsp;<&nbsp;1.0)<br>@param&nbsp;c&nbsp;continuity&nbsp;(-1.0&nbsp;<&nbsp;c&nbsp;<&nbsp;1.0)<br>@param&nbsp;b&nbsp;bias&nbsp;(-1.0&nbsp;<&nbsp;b&nbsp;<&nbsp;1.0)
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        KBKeyFrame(float k, int l,  pos, float hd, float pi, float bk,  s, float t, float c, float b); 



        //Prints&nbsp;information&nbsp;comtained&nbsp;in&nbsp;this&nbsp;key&nbsp;frame&nbsp;<br>@param&nbsp;tag&nbsp;string&nbsp;tag&nbsp;for&nbsp;identifying&nbsp;debug&nbsp;message
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int debugPrint(const char * tag); 



};  //end class KBKeyFrame



#endif
