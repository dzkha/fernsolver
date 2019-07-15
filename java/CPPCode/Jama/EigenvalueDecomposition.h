#ifndef EIGENVALUEDECOMPOSITION_H
#define EIGENVALUEDECOMPOSITION_H
//Begin section for file EigenvalueDecomposition.h
//TODO: Add definitions that you want preserved
//End section for file EigenvalueDecomposition.h
#include "../../rt.jar/java.io/Serializable.h"
#include "Matrix.h"





//Eigenvalues&nbsp;and&nbsp;eigenvectors&nbsp;of&nbsp;a&nbsp;real&nbsp;matrix.&nbsp;<br><P><br>&nbsp;&nbsp;&nbsp;&nbsp;If&nbsp;A&nbsp;is&nbsp;symmetric,&nbsp;then&nbsp;A&nbsp;=&nbsp;V*D*V'&nbsp;where&nbsp;the&nbsp;eigenvalue&nbsp;matrix&nbsp;D&nbsp;is<br>&nbsp;&nbsp;&nbsp;&nbsp;diagonal&nbsp;and&nbsp;the&nbsp;eigenvector&nbsp;matrix&nbsp;V&nbsp;is&nbsp;orthogonal.<br>&nbsp;&nbsp;&nbsp;&nbsp;I.e.&nbsp;A&nbsp;=&nbsp;V.times(D.times(V.transpose()))&nbsp;and&nbsp;<br>&nbsp;&nbsp;&nbsp;&nbsp;V.times(V.transpose())&nbsp;equals&nbsp;the&nbsp;identity&nbsp;matrix.<br><P><br>&nbsp;&nbsp;&nbsp;&nbsp;If&nbsp;A&nbsp;is&nbsp;not&nbsp;symmetric,&nbsp;then&nbsp;the&nbsp;eigenvalue&nbsp;matrix&nbsp;D&nbsp;is&nbsp;block&nbsp;diagonal<br>&nbsp;&nbsp;&nbsp;&nbsp;with&nbsp;the&nbsp;real&nbsp;eigenvalues&nbsp;in&nbsp;1-by-1&nbsp;blocks&nbsp;and&nbsp;any&nbsp;complex&nbsp;eigenvalues,<br>&nbsp;&nbsp;&nbsp;&nbsp;lambda&nbsp;+&nbsp;i*mu,&nbsp;in&nbsp;2-by-2&nbsp;blocks,&nbsp;[lambda,&nbsp;mu;&nbsp;-mu,&nbsp;lambda].&nbsp;&nbsp;The<br>&nbsp;&nbsp;&nbsp;&nbsp;columns&nbsp;of&nbsp;V&nbsp;represent&nbsp;the&nbsp;eigenvectors&nbsp;in&nbsp;the&nbsp;sense&nbsp;that&nbsp;A*V&nbsp;=&nbsp;V*D,<br>&nbsp;&nbsp;&nbsp;&nbsp;i.e.&nbsp;A.times(V)&nbsp;equals&nbsp;V.times(D).&nbsp;&nbsp;The&nbsp;matrix&nbsp;V&nbsp;may&nbsp;be&nbsp;badly<br>&nbsp;&nbsp;&nbsp;&nbsp;conditioned,&nbsp;or&nbsp;even&nbsp;singular,&nbsp;so&nbsp;the&nbsp;validity&nbsp;of&nbsp;the&nbsp;equation<br>&nbsp;&nbsp;&nbsp;&nbsp;A&nbsp;=&nbsp;V*D*inverse(V)&nbsp;depends&nbsp;upon&nbsp;V.cond().
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class EigenvalueDecomposition : Serializable
{

    //Begin section for EigenvalueDecomposition
    //TODO: Add attributes that you want preserved
    //End section for EigenvalueDecomposition

    private:


        //Row&nbsp;and&nbsp;column&nbsp;dimension&nbsp;(square&nbsp;matrix).<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;matrix&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int n;



        //Symmetry&nbsp;flag.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;symmetry&nbsp;flag.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        boolean issymmetric;



        //Arrays&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;eigenvalues.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;eigenvalues.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * d;



        //Arrays&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;eigenvalues.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;eigenvalues.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * e;



        //Array&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;eigenvectors.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;eigenvectors.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * V;



        //Array&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;nonsymmetric&nbsp;Hessenberg&nbsp;form.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;storage&nbsp;of&nbsp;nonsymmetric&nbsp;Hessenberg&nbsp;form.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * H;



        //Working&nbsp;storage&nbsp;for&nbsp;nonsymmetric&nbsp;algorithm.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;working&nbsp;storage&nbsp;for&nbsp;nonsymmetric&nbsp;algorithm.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * ort;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double cdivr;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double cdivi;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int tred2(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int tql2(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int orthes(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int cdiv(double xr, double xi, double yr, double yi); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int hqr2(); 




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const double * realEigenvalues;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const double * imagEigenvalues;



        //Check&nbsp;for&nbsp;symmetry,&nbsp;then&nbsp;construct&nbsp;the&nbsp;eigenvalue&nbsp;decomposition<br>&nbsp;&nbsp;&nbsp;@param&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;Square&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Structure&nbsp;to&nbsp;access&nbsp;D&nbsp;and&nbsp;V.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        EigenvalueDecomposition(Matrix Arg); 



};  //end class EigenvalueDecomposition



#endif
