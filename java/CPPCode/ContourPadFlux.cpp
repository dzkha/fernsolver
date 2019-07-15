#include "ContourPadFlux.h"
//Begin section for file ContourPadFlux.cpp
//TODO: Add definitions that you want preserved
//End section for file ContourPadFlux.cpp



//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::zmax;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::nmax;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::protonNumber = 1;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::neutronNumber = 1;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::width;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::height;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double ContourPadFlux::currentTime;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ContourPadFlux::ContourPadFlux(const char * contourLegend, double * contourRange, Color * contourColor, double * twa) : 
mouseX(0),mouseY(0),maxPlotN(0),maxPlotZ(0),animateIt(false),sleepTime(0),loopDelayFac(1),loopFlag(false),contourRange(new double[ContourFrameFlux.numberContours]),contourColor(new Color[ContourFrameFlux.numberContours]),minDripN(new int[IsotopePad.minDripN.length]),maxDripN(new int[IsotopePad.maxDripN.length]),isPStable(new boolean[110][200]),minZDrip(new int[200]),frameColor(MyColors.gray153),nonSelectColor(MyColors.blueGray),isoLabelColor(Color.white),smallFont(new java.awt.Font("Arial",Font.BOLD,9)),smallFontMetrics(getFontMetrics(smallFont)),realSmallFont(new java.awt.Font("Arial",Font.PLAIN,10)),realSmallFontMetrics(getFontMetrics(realSmallFont)),tinyFont(new java.awt.Font("Arial",Font.PLAIN,9)),tinyFontMetrics(getFontMetrics(tinyFont)),bigFont(new java.awt.Font("Arial",Font.PLAIN,16)),bigFontMetrics(getFontMetrics(bigFont)),timeFont(new java.awt.Font("Arial",Font.PLAIN,12)),timeFontMetrics(getFontMetrics(timeFont)),savingFrameNow(false),movieMode(0),isoColorIndex(new byte[zmax + 1][nmax + 1][ts + 1]),currentColorIndex(new byte[zmax + 1][nmax + 1]),previousColorIndex(new byte[zmax + 1][nmax + 1])
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::run() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::startThread() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::setColorIndices() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::boxRepainter(int t) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::update(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::paint(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::drawMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::nonZeroMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::diffMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::bkgMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::mousePressed(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::mouseEntered(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::mouseExited(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::mouseClicked(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::mouseReleased(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::getNZ(int x, int y) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::drawColorSquare(int x, int y, int delx, int dely, Color bgcolor, Color frameColor, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::setIsoLabel(int z, int n, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
const char * ContourPadFlux::returnSymbol(int z) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::putContourScale(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::putTimer(int xoff, int yoff, boolean withBox, boolean withBkg, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::PSfile(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::PSfileNonZero(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::PSfileBkg(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::PSfileDiff(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPadFlux::saveFrames() 
{
    //TODO Auto-generated method stub
    return 0;
}
