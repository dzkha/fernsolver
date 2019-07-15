#ifndef HOSTCOMMANDS_H
#define HOSTCOMMANDS_H
//Begin section for file HostCommands.h
//TODO: Add definitions that you want preserved
//End section for file HostCommands.h




//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class HostCommands
{

    //Begin section for HostCommands
    //TODO: Add attributes that you want preserved
    //End section for HostCommands

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * hostname;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * issueMachineCommand(const char * comm, bool plines); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int processCompiledOutput(const char * comm, bool plines); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static int processLine(const char * line); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double stringToDouble(const char * s); 




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        HostCommands(); 



};  //end class HostCommands



#endif
