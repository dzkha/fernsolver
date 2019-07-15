#ifndef NZBOXES_H
#define NZBOXES_H
//Begin section for file NZBoxes.h
//TODO: Add definitions that you want preserved
//End section for file NZBoxes.h
#include "../rt.jar/java.io/Serializable.h"
#include "../rt.jar/java.awt/Color.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class NZBoxes : Serializable
{

    //Begin section for NZBoxes
    //TODO: Add attributes that you want preserved
    //End section for NZBoxes



    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int x;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int y;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int width;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int height;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color color;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        NZBoxes(int x, int y, int width, int height, Color c); 



};  //end class NZBoxes



#endif
