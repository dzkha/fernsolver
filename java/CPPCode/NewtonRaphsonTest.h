#ifndef NEWTONRAPHSONTEST_H
#define NEWTONRAPHSONTEST_H
//Begin section for file NewtonRaphsonTest.h
//TODO: Add definitions that you want preserved
//End section for file NewtonRaphsonTest.h




//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class NewtonRaphsonTest
{

    //Begin section for NewtonRaphsonTest
    //TODO: Add attributes that you want preserved
    //End section for NewtonRaphsonTest

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double K;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double ytil12;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        NewtonRaphsonTest(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double * computeF(double * x); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double * computeJ(double * x); 




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int main(const char * args); 



};  //end class NewtonRaphsonTest



#endif
