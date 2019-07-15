#ifndef READVARIABLEDATA_H
#define READVARIABLEDATA_H
//Begin section for file ReadVariableData.h
//TODO: Add definitions that you want preserved
//End section for file ReadVariableData.h




//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class ReadVariableData
{

    //Begin section for ReadVariableData
    //TODO: Add attributes that you want preserved
    //End section for ReadVariableData


    private:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int getNonZeroAbundance(); 




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double maxAbundance;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double minAbundance;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int maxProton;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int maxNeutron;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int totalTimeStep;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int nonZeroAbundanceCount;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * population;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * time;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static boolean * nonZeroAbundance;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ReadVariableData(double * inputPopulation, double * inputTime, int inputMaxProton, int inputMaxNeutron); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ReadVariableData(double * inputPopulation, double * inputTime, int inputMaxProton, int inputMaxNeutron, double lowerCutoff); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ReadVariableData(const char * filename); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ReadVariableData(const char * cropProton, const char * cropNeutron, const char * filename); 



};  //end class ReadVariableData



#endif
