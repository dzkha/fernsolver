#ifndef MATRIX_H
#define MATRIX_H
//Begin section for file Matrix.h
//TODO: Add definitions that you want preserved
//End section for file Matrix.h
#include "../../rt.jar/java.lang/Cloneable.h"
#include "../../rt.jar/java.io/Serializable.h"
#include "LUDecomposition.h"
#include "QRDecomposition.h"
#include "CholeskyDecomposition.h"
#include "SingularValueDecomposition.h"
#include "EigenvalueDecomposition.h"
#include "../../rt.jar/java.io/PrintWriter.h"
#include "../../rt.jar/java.text/NumberFormat.h"
#include "../../rt.jar/java.io/BufferedReader.h"





//&nbsp;&nbsp;&nbsp;Jama&nbsp;=&nbsp;Java&nbsp;Matrix&nbsp;class.<br><P><br>&nbsp;&nbsp;&nbsp;The&nbsp;Java&nbsp;Matrix&nbsp;Class&nbsp;provides&nbsp;the&nbsp;fundamental&nbsp;operations&nbsp;of&nbsp;numerical<br>&nbsp;&nbsp;&nbsp;linear&nbsp;algebra.&nbsp;&nbsp;Various&nbsp;constructors&nbsp;create&nbsp;Matrices&nbsp;from&nbsp;two&nbsp;dimensional<br>&nbsp;&nbsp;&nbsp;arrays&nbsp;of&nbsp;double&nbsp;precision&nbsp;floating&nbsp;point&nbsp;numbers.&nbsp;&nbsp;Various&nbsp;"gets"&nbsp;and<br>&nbsp;&nbsp;&nbsp;"sets"&nbsp;provide&nbsp;access&nbsp;to&nbsp;submatrices&nbsp;and&nbsp;matrix&nbsp;elements.&nbsp;&nbsp;Several&nbsp;methods&nbsp;<br>&nbsp;&nbsp;&nbsp;implement&nbsp;basic&nbsp;matrix&nbsp;arithmetic,&nbsp;including&nbsp;matrix&nbsp;addition&nbsp;and<br>&nbsp;&nbsp;&nbsp;multiplication,&nbsp;matrix&nbsp;norms,&nbsp;and&nbsp;element-by-element&nbsp;array&nbsp;operations.<br>&nbsp;&nbsp;&nbsp;Methods&nbsp;for&nbsp;reading&nbsp;and&nbsp;printing&nbsp;matrices&nbsp;are&nbsp;also&nbsp;included.&nbsp;&nbsp;All&nbsp;the<br>&nbsp;&nbsp;&nbsp;operations&nbsp;in&nbsp;this&nbsp;version&nbsp;of&nbsp;the&nbsp;Matrix&nbsp;Class&nbsp;involve&nbsp;real&nbsp;matrices.<br>&nbsp;&nbsp;&nbsp;Complex&nbsp;matrices&nbsp;may&nbsp;be&nbsp;handled&nbsp;in&nbsp;a&nbsp;future&nbsp;version.<br><P><br>&nbsp;&nbsp;&nbsp;Five&nbsp;fundamental&nbsp;matrix&nbsp;decompositions,&nbsp;which&nbsp;consist&nbsp;of&nbsp;pairs&nbsp;or&nbsp;triples<br>&nbsp;&nbsp;&nbsp;of&nbsp;matrices,&nbsp;permutation&nbsp;vectors,&nbsp;and&nbsp;the&nbsp;like,&nbsp;produce&nbsp;results&nbsp;in&nbsp;five<br>&nbsp;&nbsp;&nbsp;decomposition&nbsp;classes.&nbsp;&nbsp;These&nbsp;decompositions&nbsp;are&nbsp;accessed&nbsp;by&nbsp;the&nbsp;Matrix<br>&nbsp;&nbsp;&nbsp;class&nbsp;to&nbsp;compute&nbsp;solutions&nbsp;of&nbsp;simultaneous&nbsp;linear&nbsp;equations,&nbsp;determinants,<br>&nbsp;&nbsp;&nbsp;inverses&nbsp;and&nbsp;other&nbsp;matrix&nbsp;functions.&nbsp;&nbsp;The&nbsp;five&nbsp;decompositions&nbsp;are:<br><P><UL><br>&nbsp;&nbsp;&nbsp;<LI>Cholesky&nbsp;Decomposition&nbsp;of&nbsp;symmetric,&nbsp;positive&nbsp;definite&nbsp;matrices.<br>&nbsp;&nbsp;&nbsp;<LI>LU&nbsp;Decomposition&nbsp;of&nbsp;rectangular&nbsp;matrices.<br>&nbsp;&nbsp;&nbsp;<LI>QR&nbsp;Decomposition&nbsp;of&nbsp;rectangular&nbsp;matrices.<br>&nbsp;&nbsp;&nbsp;<LI>Singular&nbsp;Value&nbsp;Decomposition&nbsp;of&nbsp;rectangular&nbsp;matrices.<br>&nbsp;&nbsp;&nbsp;<LI>Eigenvalue&nbsp;Decomposition&nbsp;of&nbsp;both&nbsp;symmetric&nbsp;and&nbsp;nonsymmetric&nbsp;square&nbsp;matrices.<br></UL><br><DL><br><DT><B>Example&nbsp;of&nbsp;use:</B></DT><br><P><br><DD>Solve&nbsp;a&nbsp;linear&nbsp;system&nbsp;A&nbsp;x&nbsp;=&nbsp;b&nbsp;and&nbsp;compute&nbsp;the&nbsp;residual&nbsp;norm,&nbsp;||b&nbsp;-&nbsp;A&nbsp;x||.<br><P><PRE><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;double[][]&nbsp;vals&nbsp;=&nbsp;{{1.,2.,3},{4.,5.,6.},{7.,8.,10.}};<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Matrix&nbsp;A&nbsp;=&nbsp;new&nbsp;Matrix(vals);<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Matrix&nbsp;b&nbsp;=&nbsp;Matrix.random(3,1);<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Matrix&nbsp;x&nbsp;=&nbsp;A.solve(b);<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Matrix&nbsp;r&nbsp;=&nbsp;A.times(x).minus(b);<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;double&nbsp;rnorm&nbsp;=&nbsp;r.normInf();<br></PRE></DD><br></DL><br><br>@author&nbsp;The&nbsp;MathWorks,&nbsp;Inc.&nbsp;and&nbsp;the&nbsp;National&nbsp;Institute&nbsp;of&nbsp;Standards&nbsp;and&nbsp;Technology.<br>@version&nbsp;5&nbsp;August&nbsp;1998
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class Matrix : Cloneable, Serializable
{

    //Begin section for Matrix
    //TODO: Add attributes that you want preserved
    //End section for Matrix

    private:


        //Array&nbsp;for&nbsp;internal&nbsp;storage&nbsp;of&nbsp;elements.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;internal&nbsp;array&nbsp;storage.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double * A;



        //Row&nbsp;and&nbsp;column&nbsp;dimensions.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;row&nbsp;dimension.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;column&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int m;



        //Row&nbsp;and&nbsp;column&nbsp;dimensions.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;row&nbsp;dimension.<br>&nbsp;&nbsp;&nbsp;@serial&nbsp;column&nbsp;dimension.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int n;



        //Check&nbsp;if&nbsp;size(A)&nbsp;==&nbsp;size(B)
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int checkMatrixDimensions(Matrix B); 




    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const double * array;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const double * arrayCopy;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const double * columnPackedCopy;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const double * rowPackedCopy;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const int rowDimension;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const int columnDimension;



        //Construct&nbsp;an&nbsp;m-by-n&nbsp;matrix&nbsp;of&nbsp;zeros.&nbsp;<br>&nbsp;&nbsp;&nbsp;@param&nbsp;m&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;rows.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;n&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;colums.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix(int m, int n); 



        //Construct&nbsp;an&nbsp;m-by-n&nbsp;constant&nbsp;matrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;m&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;rows.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;n&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;colums.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;s&nbsp;&nbsp;&nbsp;&nbsp;Fill&nbsp;the&nbsp;matrix&nbsp;with&nbsp;this&nbsp;scalar&nbsp;value.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix(int m, int n, double s); 



        //Construct&nbsp;a&nbsp;matrix&nbsp;from&nbsp;a&nbsp;2-D&nbsp;array.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;Two-dimensional&nbsp;array&nbsp;of&nbsp;doubles.<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;IllegalArgumentException&nbsp;All&nbsp;rows&nbsp;must&nbsp;have&nbsp;the&nbsp;same&nbsp;length<br>&nbsp;&nbsp;&nbsp;@see&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;#constructWithCopy
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix(double * A); 



        //Construct&nbsp;a&nbsp;matrix&nbsp;quickly&nbsp;without&nbsp;checking&nbsp;arguments.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;Two-dimensional&nbsp;array&nbsp;of&nbsp;doubles.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;m&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;rows.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;n&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;colums.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix(double * A, int m, int n); 



        //Construct&nbsp;a&nbsp;matrix&nbsp;from&nbsp;a&nbsp;one-dimensional&nbsp;packed&nbsp;array<br>&nbsp;&nbsp;&nbsp;@param&nbsp;vals&nbsp;One-dimensional&nbsp;array&nbsp;of&nbsp;doubles,&nbsp;packed&nbsp;by&nbsp;columns&nbsp;(ala&nbsp;Fortran).<br>&nbsp;&nbsp;&nbsp;@param&nbsp;m&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;rows.<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;IllegalArgumentException&nbsp;Array&nbsp;length&nbsp;must&nbsp;be&nbsp;a&nbsp;multiple&nbsp;of&nbsp;m.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix(double * vals, int m); 



        //Construct&nbsp;a&nbsp;matrix&nbsp;from&nbsp;a&nbsp;copy&nbsp;of&nbsp;a&nbsp;2-D&nbsp;array.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;Two-dimensional&nbsp;array&nbsp;of&nbsp;doubles.<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;IllegalArgumentException&nbsp;All&nbsp;rows&nbsp;must&nbsp;have&nbsp;the&nbsp;same&nbsp;length
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Matrix constructWithCopy(double * A); 



        //Make&nbsp;a&nbsp;deep&nbsp;copy&nbsp;of&nbsp;a&nbsp;matrix
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix copy(); 



        //Clone&nbsp;the&nbsp;Matrix&nbsp;object.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
         clone(); 



        //Get&nbsp;a&nbsp;single&nbsp;element.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i&nbsp;&nbsp;&nbsp;&nbsp;Row&nbsp;index.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j&nbsp;&nbsp;&nbsp;&nbsp;Column&nbsp;index.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A(i,j)<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double get(int i, int j); 



        //Get&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i0&nbsp;&nbsp;&nbsp;Initial&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i1&nbsp;&nbsp;&nbsp;Final&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j0&nbsp;&nbsp;&nbsp;Initial&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j1&nbsp;&nbsp;&nbsp;Final&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A(i0:i1,j0:j1)<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix getMatrix(int i0, int i1, int j0, int j1); 



        //Get&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;r&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;row&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;c&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;column&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A(r(:),c(:))<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix getMatrix(int * r, int * c); 



        //Get&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i0&nbsp;&nbsp;&nbsp;Initial&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i1&nbsp;&nbsp;&nbsp;Final&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;c&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;column&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A(i0:i1,c(:))<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix getMatrix(int i0, int i1, int * c); 



        //Get&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;r&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;row&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i0&nbsp;&nbsp;&nbsp;Initial&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i1&nbsp;&nbsp;&nbsp;Final&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A(r(:),j0:j1)<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix getMatrix(int * r, int j0, int j1); 



        //Set&nbsp;a&nbsp;single&nbsp;element.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i&nbsp;&nbsp;&nbsp;&nbsp;Row&nbsp;index.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j&nbsp;&nbsp;&nbsp;&nbsp;Column&nbsp;index.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;s&nbsp;&nbsp;&nbsp;&nbsp;A(i,j).<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int set(int i, int j, double s); 



        //Set&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i0&nbsp;&nbsp;&nbsp;Initial&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i1&nbsp;&nbsp;&nbsp;Final&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j0&nbsp;&nbsp;&nbsp;Initial&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j1&nbsp;&nbsp;&nbsp;Final&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;X&nbsp;&nbsp;&nbsp;&nbsp;A(i0:i1,j0:j1)<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int setMatrix(int i0, int i1, int j0, int j1, Matrix X); 



        //Set&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;r&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;row&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;c&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;column&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;X&nbsp;&nbsp;&nbsp;&nbsp;A(r(:),c(:))<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int setMatrix(int * r, int * c, Matrix X); 



        //Set&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;r&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;row&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j0&nbsp;&nbsp;&nbsp;Initial&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;j1&nbsp;&nbsp;&nbsp;Final&nbsp;column&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;X&nbsp;&nbsp;&nbsp;&nbsp;A(r(:),j0:j1)<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int setMatrix(int * r, int j0, int j1, Matrix X); 



        //Set&nbsp;a&nbsp;submatrix.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i0&nbsp;&nbsp;&nbsp;Initial&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;i1&nbsp;&nbsp;&nbsp;Final&nbsp;row&nbsp;index<br>&nbsp;&nbsp;&nbsp;@param&nbsp;c&nbsp;&nbsp;&nbsp;&nbsp;Array&nbsp;of&nbsp;column&nbsp;indices.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;X&nbsp;&nbsp;&nbsp;&nbsp;A(i0:i1,c(:))<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;ArrayIndexOutOfBoundsException&nbsp;Submatrix&nbsp;indices
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int setMatrix(int i0, int i1, int * c, Matrix X); 



        //Matrix&nbsp;transpose.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;A'
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix transpose(); 



        //One&nbsp;norm<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;maximum&nbsp;column&nbsp;sum.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double norm1(); 



        //Two&nbsp;norm<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;maximum&nbsp;singular&nbsp;value.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double norm2(); 



        //Infinity&nbsp;norm<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;maximum&nbsp;row&nbsp;sum.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double normInf(); 



        //Frobenius&nbsp;norm<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;sqrt&nbsp;of&nbsp;sum&nbsp;of&nbsp;squares&nbsp;of&nbsp;all&nbsp;elements.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double normF(); 



        //Unary&nbsp;minus<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;-A
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix uminus(); 



        //C&nbsp;=&nbsp;A&nbsp;+&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A&nbsp;+&nbsp;B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix plus(Matrix B); 



        //A&nbsp;=&nbsp;A&nbsp;+&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A&nbsp;+&nbsp;B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix plusEquals(Matrix B); 



        //C&nbsp;=&nbsp;A&nbsp;-&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A&nbsp;-&nbsp;B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix minus(Matrix B); 



        //A&nbsp;=&nbsp;A&nbsp;-&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A&nbsp;-&nbsp;B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix minusEquals(Matrix B); 



        //Element-by-element&nbsp;multiplication,&nbsp;C&nbsp;=&nbsp;A.*B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A.*B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix arrayTimes(Matrix B); 



        //Element-by-element&nbsp;multiplication&nbsp;in&nbsp;place,&nbsp;A&nbsp;=&nbsp;A.*B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A.*B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix arrayTimesEquals(Matrix B); 



        //Element-by-element&nbsp;right&nbsp;division,&nbsp;C&nbsp;=&nbsp;A./B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A./B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix arrayRightDivide(Matrix B); 



        //Element-by-element&nbsp;right&nbsp;division&nbsp;in&nbsp;place,&nbsp;A&nbsp;=&nbsp;A./B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A./B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix arrayRightDivideEquals(Matrix B); 



        //Element-by-element&nbsp;left&nbsp;division,&nbsp;C&nbsp;=&nbsp;A.\B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A.\B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix arrayLeftDivide(Matrix B); 



        //Element-by-element&nbsp;left&nbsp;division&nbsp;in&nbsp;place,&nbsp;A&nbsp;=&nbsp;A.\B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A.\B
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix arrayLeftDivideEquals(Matrix B); 



        //Multiply&nbsp;a&nbsp;matrix&nbsp;by&nbsp;a&nbsp;scalar,&nbsp;C&nbsp;=&nbsp;s*A<br>&nbsp;&nbsp;&nbsp;@param&nbsp;s&nbsp;&nbsp;&nbsp;&nbsp;scalar<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;s*A
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix times(double s); 



        //Multiply&nbsp;a&nbsp;matrix&nbsp;by&nbsp;a&nbsp;scalar&nbsp;in&nbsp;place,&nbsp;A&nbsp;=&nbsp;s*A<br>&nbsp;&nbsp;&nbsp;@param&nbsp;s&nbsp;&nbsp;&nbsp;&nbsp;scalar<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;replace&nbsp;A&nbsp;by&nbsp;s*A
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix timesEquals(double s); 



        //Linear&nbsp;algebraic&nbsp;matrix&nbsp;multiplication,&nbsp;A&nbsp;*&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;another&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Matrix&nbsp;product,&nbsp;A&nbsp;*&nbsp;B<br>&nbsp;&nbsp;&nbsp;@exception&nbsp;&nbsp;IllegalArgumentException&nbsp;Matrix&nbsp;inner&nbsp;dimensions&nbsp;must&nbsp;agree.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix times(Matrix B); 



        //LU&nbsp;Decomposition<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LUDecomposition<br>&nbsp;&nbsp;&nbsp;@see&nbsp;LUDecomposition
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        LUDecomposition lu(); 



        //QR&nbsp;Decomposition<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;QRDecomposition<br>&nbsp;&nbsp;&nbsp;@see&nbsp;QRDecomposition
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        QRDecomposition qr(); 



        //Cholesky&nbsp;Decomposition<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CholeskyDecomposition<br>&nbsp;&nbsp;&nbsp;@see&nbsp;CholeskyDecomposition
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        CholeskyDecomposition chol(); 



        //Singular&nbsp;Value&nbsp;Decomposition<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SingularValueDecomposition<br>&nbsp;&nbsp;&nbsp;@see&nbsp;SingularValueDecomposition
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        SingularValueDecomposition svd(); 



        //Eigenvalue&nbsp;Decomposition<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EigenvalueDecomposition<br>&nbsp;&nbsp;&nbsp;@see&nbsp;EigenvalueDecomposition
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        EigenvalueDecomposition eig(); 



        //Solve&nbsp;A*X&nbsp;=&nbsp;B<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;right&nbsp;hand&nbsp;side<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;solution&nbsp;if&nbsp;A&nbsp;is&nbsp;square,&nbsp;least&nbsp;squares&nbsp;solution&nbsp;otherwise
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix solve(Matrix B); 



        //Solve&nbsp;X*A&nbsp;=&nbsp;B,&nbsp;which&nbsp;is&nbsp;also&nbsp;A'*X'&nbsp;=&nbsp;B'<br>&nbsp;&nbsp;&nbsp;@param&nbsp;B&nbsp;&nbsp;&nbsp;&nbsp;right&nbsp;hand&nbsp;side<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;solution&nbsp;if&nbsp;A&nbsp;is&nbsp;square,&nbsp;least&nbsp;squares&nbsp;solution&nbsp;otherwise.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix solveTranspose(Matrix B); 



        //Matrix&nbsp;inverse&nbsp;or&nbsp;pseudoinverse<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;inverse(A)&nbsp;if&nbsp;A&nbsp;is&nbsp;square,&nbsp;pseudoinverse&nbsp;otherwise.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Matrix inverse(); 



        //Matrix&nbsp;determinant<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;determinant
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double det(); 



        //Matrix&nbsp;rank<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;effective&nbsp;numerical&nbsp;rank,&nbsp;obtained&nbsp;from&nbsp;SVD.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int rank(); 



        //Matrix&nbsp;condition&nbsp;(2&nbsp;norm)<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ratio&nbsp;of&nbsp;largest&nbsp;to&nbsp;smallest&nbsp;singular&nbsp;value.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double cond(); 



        //Matrix&nbsp;trace.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sum&nbsp;of&nbsp;the&nbsp;diagonal&nbsp;elements.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        double trace(); 



        //Generate&nbsp;matrix&nbsp;with&nbsp;random&nbsp;elements<br>&nbsp;&nbsp;&nbsp;@param&nbsp;m&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;rows.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;n&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;colums.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;An&nbsp;m-by-n&nbsp;matrix&nbsp;with&nbsp;uniformly&nbsp;distributed&nbsp;random&nbsp;elements.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Matrix random(int m, int n); 



        //Generate&nbsp;identity&nbsp;matrix<br>&nbsp;&nbsp;&nbsp;@param&nbsp;m&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;rows.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;n&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;colums.<br>&nbsp;&nbsp;&nbsp;@return&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;An&nbsp;m-by-n&nbsp;matrix&nbsp;with&nbsp;ones&nbsp;on&nbsp;the&nbsp;diagonal&nbsp;and&nbsp;zeros&nbsp;elsewhere.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Matrix identity(int m, int n); 



        //Print&nbsp;the&nbsp;matrix&nbsp;to&nbsp;stdout.&nbsp;&nbsp;&nbsp;Line&nbsp;the&nbsp;elements&nbsp;up&nbsp;in&nbsp;columns<br>with&nbsp;a&nbsp;Fortran-like&nbsp;'Fw.d'&nbsp;style&nbsp;format.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;w&nbsp;&nbsp;&nbsp;&nbsp;Column&nbsp;width.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;d&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;digits&nbsp;after&nbsp;the&nbsp;decimal.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int print(int w, int d); 



        //Print&nbsp;the&nbsp;matrix&nbsp;to&nbsp;the&nbsp;output&nbsp;stream.&nbsp;&nbsp;&nbsp;Line&nbsp;the&nbsp;elements&nbsp;up&nbsp;in<br>columns&nbsp;with&nbsp;a&nbsp;Fortran-like&nbsp;'Fw.d'&nbsp;style&nbsp;format.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;output&nbsp;Output&nbsp;stream.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;w&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Column&nbsp;width.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;d&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Number&nbsp;of&nbsp;digits&nbsp;after&nbsp;the&nbsp;decimal.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int print(PrintWriter output, int w, int d); 



        //Print&nbsp;the&nbsp;matrix&nbsp;to&nbsp;stdout.&nbsp;&nbsp;Line&nbsp;the&nbsp;elements&nbsp;up&nbsp;in&nbsp;columns.<br>Use&nbsp;the&nbsp;format&nbsp;object,&nbsp;and&nbsp;right&nbsp;justify&nbsp;within&nbsp;columns&nbsp;of&nbsp;width<br>characters.<br>Note&nbsp;that&nbsp;is&nbsp;the&nbsp;matrix&nbsp;is&nbsp;to&nbsp;be&nbsp;read&nbsp;back&nbsp;in,&nbsp;you&nbsp;probably&nbsp;will&nbsp;want<br>to&nbsp;use&nbsp;a&nbsp;NumberFormat&nbsp;that&nbsp;is&nbsp;set&nbsp;to&nbsp;US&nbsp;Locale.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;format&nbsp;A&nbsp;&nbsp;Formatting&nbsp;object&nbsp;for&nbsp;individual&nbsp;elements.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;width&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Field&nbsp;width&nbsp;for&nbsp;each&nbsp;column.<br>&nbsp;&nbsp;&nbsp;@see&nbsp;java.text.DecimalFormat#setDecimalFormatSymbols
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int print(NumberFormat format, int width); 



        //Print&nbsp;the&nbsp;matrix&nbsp;to&nbsp;the&nbsp;output&nbsp;stream.&nbsp;&nbsp;Line&nbsp;the&nbsp;elements&nbsp;up&nbsp;in&nbsp;columns.<br>Use&nbsp;the&nbsp;format&nbsp;object,&nbsp;and&nbsp;right&nbsp;justify&nbsp;within&nbsp;columns&nbsp;of&nbsp;width<br>characters.<br>Note&nbsp;that&nbsp;is&nbsp;the&nbsp;matrix&nbsp;is&nbsp;to&nbsp;be&nbsp;read&nbsp;back&nbsp;in,&nbsp;you&nbsp;probably&nbsp;will&nbsp;want<br>to&nbsp;use&nbsp;a&nbsp;NumberFormat&nbsp;that&nbsp;is&nbsp;set&nbsp;to&nbsp;US&nbsp;Locale.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;output&nbsp;the&nbsp;output&nbsp;stream.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;format&nbsp;A&nbsp;formatting&nbsp;object&nbsp;to&nbsp;format&nbsp;the&nbsp;matrix&nbsp;elements&nbsp;<br>&nbsp;&nbsp;&nbsp;@param&nbsp;width&nbsp;&nbsp;Column&nbsp;width.<br>&nbsp;&nbsp;&nbsp;@see&nbsp;java.text.DecimalFormat#setDecimalFormatSymbols
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int print(PrintWriter output, NumberFormat format, int width); 



        //Read&nbsp;a&nbsp;matrix&nbsp;from&nbsp;a&nbsp;stream.&nbsp;&nbsp;The&nbsp;format&nbsp;is&nbsp;the&nbsp;same&nbsp;the&nbsp;print&nbsp;method,<br>so&nbsp;printed&nbsp;matrices&nbsp;can&nbsp;be&nbsp;read&nbsp;back&nbsp;in&nbsp;(provided&nbsp;they&nbsp;were&nbsp;printed&nbsp;using<br>US&nbsp;Locale).&nbsp;&nbsp;Elements&nbsp;are&nbsp;separated&nbsp;by<br>whitespace,&nbsp;all&nbsp;the&nbsp;elements&nbsp;for&nbsp;each&nbsp;row&nbsp;appear&nbsp;on&nbsp;a&nbsp;single&nbsp;line,<br>the&nbsp;last&nbsp;row&nbsp;is&nbsp;followed&nbsp;by&nbsp;a&nbsp;blank&nbsp;line.<br>&nbsp;&nbsp;&nbsp;@param&nbsp;input&nbsp;the&nbsp;input&nbsp;stream.
        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static Matrix read(BufferedReader input); 



};  //end class Matrix



#endif
