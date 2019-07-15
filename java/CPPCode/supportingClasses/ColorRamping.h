#ifndef COLORRAMPING_H
#define COLORRAMPING_H
//Begin section for file ColorRamping.h
//TODO: Add definitions that you want preserved
//End section for file ColorRamping.h




//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class ColorRamping
{

    //Begin section for ColorRamping
    //TODO: Add attributes that you want preserved
    //End section for ColorRamping

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double r;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double g;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double b;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float max;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        float min;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ColorRamping(float inMin, float inMax); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int getColor(float inV,  color); 



};  //end class ColorRamping



#endif
