#ifndef SINGULARVALUEDECOMPOSITION_H
#define SINGULARVALUEDECOMPOSITION_H
//Begin section for file SingularValueDecomposition.h
//TODO: Add definitions that you want preserved
//End section for file SingularValueDecomposition.h
#include "../../rt.jar/java.io/Serializable.h"
#include "Matrix.h"





//Singular&nbsp;Value&nbsp;Decomposition.<br>&nbsp;&nbsp;&nbsp;<P><br>&nbsp;&nbsp;&nbsp;For&nbsp;an&nbsp;m-by-n&nbsp;matrix&nbsp;A&nbsp;with&nbsp;m&nbsp;>=&nbsp;n,&nbsp;the&nbsp;singular&nbsp;value&nbsp;decomposition&nbsp;is<br>&nbsp;&nbsp;&nbsp;an&nbsp;m-by-n&nbsp;orthogonal&nbsp;matrix&nbsp;U,&nbsp;an&nbsp;n-by-n&nbsp;diagonal&nbsp;matrix&nbsp;S,&nbsp;and<br>&nbsp;&nbsp;&nbsp;an&nbsp;n-by-n&nbsp;orthogonal&nbsp;matrix&nbsp;V&nbsp;so&nbsp;that&nbsp;A&nbsp;=&nbsp;U*S*V'.<br>&nbsp;&nbsp;&nbsp;<P><br>&nbsp;&nbsp;&nbsp;The&nbsp;singular&nbsp;values,&nbsp;sigma[k]&nbsp;=&nbsp;S[k][k],&nbsp;are&nbsp;ordered&nbsp;so&nbsp;that<br>&nbsp;&nbsp;&nbsp;sigma[0]&nbsp;>=&nbsp;sigma[1]&nbsp;>=&nbsp;...&nbsp;>=&nbsp;sigma[n-1].<br>&nbsp;&nbsp;&nbsp;<P><br>&nbsp;&nbsp;&nbsp;The&nbsp;singular&nbsp;value&nbsp;decompostion&nbsp;always&nbsp;exists,&nbsp;so&nbsp;the&nbsp;constructor&nbsp;will<br>&nbsp;&nbsp;&nbsp;never&nbsp;fail.&nbsp;&nbsp;The&nbsp;matrix&nbsp;condition&nbsp;number&nbsp;and&nbsp;the&nbsp;effective&nbsp;numerical<br>&nbsp;&nbsp;&nbsp;rank&nbsp;can&nbsp;be&nbsp;computed&nbsp;from&nbsp;this&nbsp;decomposition.
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class SingularValueDecomposition : Serializable
{

    //Begin section for SingularValueDecomposition
    //TODO: Add attributes that you want preserved
    //End section for SingularValueDecomposition

    private:


        //Arrays&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;U&nbsp;and&nbsp;V.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;U.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;V.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * U;



        //Arrays&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;U&nbsp;and&nbsp;V.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;U.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;V.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * V;



        //Array&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;singular&nbsp;values.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;singular&nbsp;values.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * s;



        //Row&nbsp;and&nbsp;column&nbsp;dimensions.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;row&nbsp;dimension.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;column&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int m;



        //Row&nbsp;and&nbsp;column&nbsp;dimensions.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;row&nbsp;dimension.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;column&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int n;




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const double * singularValues;



        //Construct&nbsp;the&nbsp;singular&nbsp;value&nbsp;decomposition<br>&nbsp;&nbsp;&nbsp;@param&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;Rectangular&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Structure&nbsp;to&nbsp;access&nbsp;U,&nbsp;S&nbsp;and&nbsp;V.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        SingularValueDecomposition(Matrix Arg); 



        //Two&nbsp;norm<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;max(S)
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double norm2(); 



        //Two&nbsp;norm&nbsp;condition&nbsp;number<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;max(S)/min(S)
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double cond(); 



        //Effective&nbsp;numerical&nbsp;matrix&nbsp;rank<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;nonnegligible&nbsp;singular&nbsp;values.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int rank(); 



};  //end class SingularValueDecomposition



#endif
