#ifndef QRDECOMPOSITION_H
#define QRDECOMPOSITION_H
//Begin section for file QRDecomposition.h
//TODO: Add definitions that you want preserved
//End section for file QRDecomposition.h
#include "../../rt.jar/java.io/Serializable.h"
#include "Matrix.h"





//QR&nbsp;Decomposition.<br><P><br>&nbsp;&nbsp;&nbsp;For&nbsp;an&nbsp;m-by-n&nbsp;matrix&nbsp;A&nbsp;with&nbsp;m&nbsp;>=&nbsp;n,&nbsp;the&nbsp;QR&nbsp;decomposition&nbsp;is&nbsp;an&nbsp;m-by-n<br>&nbsp;&nbsp;&nbsp;orthogonal&nbsp;matrix&nbsp;Q&nbsp;and&nbsp;an&nbsp;n-by-n&nbsp;upper&nbsp;triangular&nbsp;matrix&nbsp;R&nbsp;so&nbsp;that<br>&nbsp;&nbsp;&nbsp;A&nbsp;=&nbsp;Q*R.<br><P><br>&nbsp;&nbsp;&nbsp;The&nbsp;QR&nbsp;decompostion&nbsp;always&nbsp;exists,&nbsp;even&nbsp;if&nbsp;the&nbsp;matrix&nbsp;does&nbsp;not&nbsp;have<br>&nbsp;&nbsp;&nbsp;full&nbsp;rank,&nbsp;so&nbsp;the&nbsp;constructor&nbsp;will&nbsp;never&nbsp;fail.&nbsp;&nbsp;The&nbsp;primary&nbsp;use&nbsp;of&nbsp;the<br>&nbsp;&nbsp;&nbsp;QR&nbsp;decomposition&nbsp;is&nbsp;in&nbsp;the&nbsp;least&nbsp;squares&nbsp;solution&nbsp;of&nbsp;nonsquare&nbsp;systems<br>&nbsp;&nbsp;&nbsp;of&nbsp;simultaneous&nbsp;linear&nbsp;equations.&nbsp;&nbsp;This&nbsp;will&nbsp;fail&nbsp;if&nbsp;isFullRank()<br>&nbsp;&nbsp;&nbsp;returns&nbsp;false.
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class QRDecomposition : Serializable
{

    //Begin section for QRDecomposition
    //TODO: Add attributes that you want preserved
    //End section for QRDecomposition

    private:


        //Array&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;decomposition.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;array&nbsp;storage.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * QR;



        //Row&nbsp;and&nbsp;column&nbsp;dimensions.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;column&nbsp;dimension.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;row&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int m;



        //Row&nbsp;and&nbsp;column&nbsp;dimensions.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;column&nbsp;dimension.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;row&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int n;



        //Array&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;diagonal&nbsp;of&nbsp;R.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;diagonal&nbsp;of&nbsp;R.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * Rdiag;




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const boolean fullRank;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const Matrix h;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const Matrix r;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const Matrix q;



        //QR&nbsp;Decomposition,&nbsp;computed&nbsp;by&nbsp;Householder&nbsp;reflections.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;Rectangular&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Structure&nbsp;to&nbsp;access&nbsp;R&nbsp;and&nbsp;the&nbsp;Householder&nbsp;vectors&nbsp;and&nbsp;compute&nbsp;Q.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        QRDecomposition(Matrix A); 



        //Least&nbsp;squares&nbsp;solution&nbsp;of&nbsp;A*X&nbsp;=&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;A&nbsp;Matrix&nbsp;with&nbsp;as&nbsp;many&nbsp;rows&nbsp;as&nbsp;A&nbsp;and&nbsp;any&nbsp;number&nbsp;of&nbsp;columns.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;X&nbsp;that&nbsp;minimizes&nbsp;the&nbsp;two&nbsp;norm&nbsp;of&nbsp;Q*R*X-B.<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;IllegalArgumentException&nbsp;&nbsp;Matrix&nbsp;row&nbsp;dimensions&nbsp;must&nbsp;agree.<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;RuntimeException&nbsp;&nbsp;Matrix&nbsp;is&nbsp;rank&nbsp;deficient.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix solve(Matrix B); 



};  //end class QRDecomposition



#endif
