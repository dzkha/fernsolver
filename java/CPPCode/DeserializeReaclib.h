#ifndef DESERIALIZEREACLIB_H
#define DESERIALIZEREACLIB_H
//Begin section for file DeserializeReaclib.h
//TODO: Add definitions that you want preserved
//End section for file DeserializeReaclib.h
#include "../rt.jar/java.io/FileOutputStream.h"
#include "../rt.jar/java.io/PrintWriter.h"



class ReactionClass1; //Dependency Generated Source:DeserializeReaclib Target:ReactionClass1





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class DeserializeReaclib
{

    //Begin section for DeserializeReaclib
    //TODO: Add attributes that you want preserved
    //End section for DeserializeReaclib

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int minZ;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int maxZ;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int minN;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int maxN;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int reactionClass;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const char * inputFile;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * outputFile;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static FileOutputStream outstream;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static PrintWriter toOut;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        boolean orderByReactionClass;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int numberObjects;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int numberMatches;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int main(const char * args); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int deserializeFile(int Z, int N); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int outputReactionData(ReactionClass1 * instance); 



};  //end class DeserializeReaclib



#endif
