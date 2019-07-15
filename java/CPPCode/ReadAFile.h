#ifndef READAFILE_H
#define READAFILE_H
//Begin section for file ReadAFile.h
//TODO: Add definitions that you want preserved
//End section for file ReadAFile.h




//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class ReadAFile
{

    //Begin section for ReadAFile
    //TODO: Add attributes that you want preserved
    //End section for ReadAFile


    private:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ReadAFile(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ReadAFile(int bufferLength); 



        //A&nbsp;convenience&nbsp;method&nbsp;to&nbsp;throw&nbsp;an&nbsp;exception
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int abort(const char * msg); 




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int bufferLength;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const char * readASCIIFile(const char * from_name); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int callExit(const char * message); 



};  //end class ReadAFile



#endif
