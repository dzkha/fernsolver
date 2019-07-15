#ifndef GRAPHICSGOODIES2_H
#define GRAPHICSGOODIES2_H
//Begin section for file GraphicsGoodies2.h
//TODO: Add definitions that you want preserved
//End section for file GraphicsGoodies2.h
#include "../rt.jar/java.awt/Frame.h"
#include "../rt.jar/java.awt/Font.h"
#include "../rt.jar/java.awt/Graphics.h"
#include "../rt.jar/java.awt/Color.h"



class Color; //Dependency Generated Source:GraphicsGoodies2 Target:Color





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class GraphicsGoodies2 : Frame
{

    //Begin section for GraphicsGoodies2
    //TODO: Add attributes that you want preserved
    //End section for GraphicsGoodies2



    public:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static double log10;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int rightSuperScript(const char * s, const char * ss, int x, int y, Font f, const char * relscale, Graphics g); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int rightSubScript(const char * s, const char * ss, int x, int y, Font f, const char * relscale, Graphics g); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const char * decimalPlace(int nright, double number); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int drawVector(int x1, int y1, int x2, int y2, Graphics g); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int drawVector(int x1, int y1, int r, double phi, Graphics g); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int drawDashedLine(int x1, int y1, int x2, int y2, int deldash, int delblank, Graphics g); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int drawDashedLine(int x1, int y1, int x2, int y2, Graphics g); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int drawDottedLine(int x1, int y1, int x2, int y2, Graphics g); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int plotIt(int plotmode, int x1, int y1, int x2, int y2, int kmax, int imax, int * mode, int dotSize, int xlegoff, int ylegoff, int xdplace, int ydplace, int * npoints, int doscalex, int doscaley, int * doplot, double xxmin, double xxmax, double yymin, double yymax, double delxmin, double delxmax, double delymin, double delymax, Color * lcolor, Color bgcolor, Color axiscolor, Color legendfg, Color framefg, Color dropShadow, Color legendbg, Color labelcolor, Color ticLabelColor, const char * xtitle, const char * ytitle, const char * curvetitle, int logStyle, int ytickIntervals, int xtickIntervals, boolean showLegend, double * xx, double * yy, Graphics g); 



};  //end class GraphicsGoodies2



#endif
