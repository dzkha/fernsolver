#include "ContourPad.h"
//Begin section for file ContourPad.cpp
//TODO: Add definitions that you want preserved
//End section for file ContourPad.cpp



//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::zmax;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::nmax;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::protonNumber = 1;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::neutronNumber = 1;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::width;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::height;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double ContourPad::currentTime = StochasticElements.timeNow[StochasticElements.numdt - 1];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ContourPad::ContourPad(double * contourRange, Color * contourColor) : 
mouseX(0),mouseY(0),maxPlotN(0),maxPlotZ(0),animateIt(false),sleepTime(0),loopDelayFac(1),loopFlag(false),contourRange(new double[ContourFrame.numberContours]),contourColor(new Color[ContourFrame.numberContours]),minDripN(new int[IsotopePad.minDripN.length]),maxDripN(new int[IsotopePad.maxDripN.length]),isPStable(new boolean[110][200]),minZDrip(new int[200]),frameColor(MyColors.gray153),nonSelectColor(MyColors.blueGray),isoLabelColor(Color.white),smallFont(new java.awt.Font("Arial",Font.BOLD,9)),smallFontMetrics(getFontMetrics(smallFont)),realSmallFont(new java.awt.Font("Arial",Font.PLAIN,10)),realSmallFontMetrics(getFontMetrics(realSmallFont)),tinyFont(new java.awt.Font("Arial",Font.PLAIN,9)),tinyFontMetrics(getFontMetrics(tinyFont)),bigFont(new java.awt.Font("Arial",Font.PLAIN,16)),bigFontMetrics(getFontMetrics(bigFont)),timeFont(new java.awt.Font("Arial",Font.PLAIN,12)),timeFontMetrics(getFontMetrics(timeFont)),savingFrameNow(false),movieMode(0),isoColorIndex(new byte[zmax + 1][nmax + 1][ts + 1]),currentColorIndex(new byte[zmax + 1][nmax + 1]),previousColorIndex(new byte[zmax + 1][nmax + 1])
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::run() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::startThread() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::setColorIndices() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::boxRepainter(int t) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::update(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::paint(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::drawMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::nonZeroMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::diffMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::bkgMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::mousePressed(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::mouseEntered(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::mouseExited(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::mouseClicked(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::mouseReleased(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::getNZ(int x, int y) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::drawColorSquare(int x, int y, int delx, int dely, Color bgcolor, Color frameColor, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::setIsoLabel(int z, int n, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
const char * ContourPad::returnSymbol(int z) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::putContourScale(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::putTimer(int xoff, int yoff, boolean withBox, boolean withBkg, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::PSfile(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::PSfileNonZero(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::PSfileBkg(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::PSfileDiff(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourPad::saveFrames() 
{
    //TODO Auto-generated method stub
    return 0;
}
