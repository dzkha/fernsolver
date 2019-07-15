#ifndef FRIEDELPARSER_H
#define FRIEDELPARSER_H
//Begin section for file FriedelParser.h
//TODO: Add definitions that you want preserved
//End section for file FriedelParser.h
#include "../rt.jar/java.io/FileOutputStream.h"
#include "../rt.jar/java.io/PrintWriter.h"
#include "ReactionClass1.h"
#include "../rt.jar/java.awt/Point.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class FriedelParser
{

    //Begin section for FriedelParser
    //TODO: Add attributes that you want preserved
    //End section for FriedelParser

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static FileOutputStream to;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static PrintWriter toChar;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * s;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int buffNumber;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int minZ;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int maxZ;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int minN;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int maxN;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static ReactionClass1 tempInstance;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Point In;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Point Out;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int parseBuffer(int targetZ, int targetN); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double stringToDouble(const char * s); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * charToString(char c); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int returnZ(const char * s); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int * parseNucSymbols(byte * b); 



        //A&nbsp;convenience&nbsp;method&nbsp;to&nbsp;throw&nbsp;an&nbsp;exception
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int abort(const char * msg); 




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int main(const char * args); 



        //The&nbsp;following&nbsp;defines&nbsp;a<br>static&nbsp;copy()&nbsp;method&nbsp;that&nbsp;other&nbsp;programs&nbsp;can&nbsp;use&nbsp;to&nbsp;copy&nbsp;files.<br>Before&nbsp;copying&nbsp;the&nbsp;file,&nbsp;however,&nbsp;it&nbsp;performs&nbsp;a&nbsp;lot&nbsp;of&nbsp;tests&nbsp;to&nbsp;make<br>sure&nbsp;everything&nbsp;is&nbsp;as&nbsp;it&nbsp;should&nbsp;be.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int copy(const char * from_name, const char * to_name); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int callExit(const char * message); 



};  //end class FriedelParser



#endif
