#ifndef CHOLESKYDECOMPOSITION_H
#define CHOLESKYDECOMPOSITION_H
//Begin section for file CholeskyDecomposition.h
//TODO: Add definitions that you want preserved
//End section for file CholeskyDecomposition.h
#include "../../rt.jar/java.io/Serializable.h"
#include "Matrix.h"





//Cholesky&nbsp;Decomposition.<br>&nbsp;&nbsp;&nbsp;<P><br>&nbsp;&nbsp;&nbsp;For&nbsp;a&nbsp;symmetric,&nbsp;positive&nbsp;definite&nbsp;matrix&nbsp;A,&nbsp;the&nbsp;Cholesky&nbsp;decomposition<br>&nbsp;&nbsp;&nbsp;is&nbsp;an&nbsp;lower&nbsp;triangular&nbsp;matrix&nbsp;L&nbsp;so&nbsp;that&nbsp;A&nbsp;=&nbsp;L*L'.<br>&nbsp;&nbsp;&nbsp;<P><br>&nbsp;&nbsp;&nbsp;If&nbsp;the&nbsp;matrix&nbsp;is&nbsp;not&nbsp;symmetric&nbsp;or&nbsp;positive&nbsp;definite,&nbsp;the&nbsp;constructor<br>&nbsp;&nbsp;&nbsp;returns&nbsp;a&nbsp;partial&nbsp;decomposition&nbsp;and&nbsp;sets&nbsp;an&nbsp;internal&nbsp;flag&nbsp;that&nbsp;may<br>&nbsp;&nbsp;&nbsp;be&nbsp;queried&nbsp;by&nbsp;the&nbsp;isSPD()&nbsp;method.
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class CholeskyDecomposition : Serializable
{

    //Begin section for CholeskyDecomposition
    //TODO: Add attributes that you want preserved
    //End section for CholeskyDecomposition

    private:


        //Array&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;decomposition.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;array&nbsp;storage.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * L;



        //Row&nbsp;and&nbsp;column&nbsp;dimension&nbsp;(square&nbsp;matrix).<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;matrix&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int n;



        //Symmetric&nbsp;and&nbsp;positive&nbsp;definite&nbsp;flag.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;is&nbsp;symmetric&nbsp;and&nbsp;positive&nbsp;definite&nbsp;flag.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        boolean isspd;




    public:

        //Cholesky&nbsp;algorithm&nbsp;for&nbsp;symmetric&nbsp;and&nbsp;positive&nbsp;definite&nbsp;matrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;&nbsp;A&nbsp;&nbsp;&nbsp;Square,&nbsp;symmetric&nbsp;matrix.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Structure&nbsp;to&nbsp;access&nbsp;L&nbsp;and&nbsp;isspd&nbsp;flag.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        CholeskyDecomposition(Matrix Arg); 



        //Solve&nbsp;A*X&nbsp;=&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;&nbsp;B&nbsp;&nbsp;&nbsp;A&nbsp;Matrix&nbsp;with&nbsp;as&nbsp;many&nbsp;rows&nbsp;as&nbsp;A&nbsp;and&nbsp;any&nbsp;number&nbsp;of&nbsp;columns.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;X&nbsp;so&nbsp;that&nbsp;L*L'*X&nbsp;=&nbsp;B<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;IllegalArgumentException&nbsp;&nbsp;Matrix&nbsp;row&nbsp;dimensions&nbsp;must&nbsp;agree.<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;RuntimeException&nbsp;&nbsp;Matrix&nbsp;is&nbsp;not&nbsp;symmetric&nbsp;positive&nbsp;definite.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix solve(Matrix B); 



};  //end class CholeskyDecomposition



#endif
