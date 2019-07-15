#ifndef MAGICSQUAREEXAMPLE_H
#define MAGICSQUAREEXAMPLE_H
//Begin section for file MagicSquareExample.h
//TODO: Add definitions that you want preserved
//End section for file MagicSquareExample.h
#include "../Matrix.h"





//Example&nbsp;of&nbsp;use&nbsp;of&nbsp;Matrix&nbsp;Class,&nbsp;featuring&nbsp;magic&nbsp;squares.
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class MagicSquareExample
{

    //Begin section for MagicSquareExample
    //TODO: Add attributes that you want preserved
    //End section for MagicSquareExample


    private:

        //Shorten&nbsp;spelling&nbsp;of&nbsp;print.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int print(const char * s); 




    public:

        //Generate&nbsp;magic&nbsp;square&nbsp;test&nbsp;matrix.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Matrix magic(int n); 



        //Format&nbsp;double&nbsp;with&nbsp;Fw.d.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * fixedWidthDoubletoString(double x, int w, int d); 



        //Format&nbsp;integer&nbsp;with&nbsp;Iw.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * fixedWidthIntegertoString(int n, int w); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int main(const char * argv); 



};  //end class MagicSquareExample



#endif
