#include "IsotopePad.h"
//Begin section for file IsotopePad.cpp
//TODO: Add definitions that you want preserved
//End section for file IsotopePad.cpp


//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean IsotopePad::updateAbundance = false;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::zmax = StochasticElements.pmaxPlot;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::nmax = StochasticElements.nmaxPlot;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::protonNumber = 1;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::neutronNumber = 1;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::width;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::height;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean IsotopePad::isoColor = new boolean[110][200];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean IsotopePad::isAbundant = new boolean[110][200];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int * IsotopePad::minDripN = {1,0,1,3,3,3,3,4,5,5,5,6,7,8,8,8,8,9,9,10,10,11,12,13,14,15,16,17,18,19,21,22,23,24,25,26,27,29,30,31,32,33,35,36,37,38,40,41,42,43,44,46,47,48,49,51,52,53,55,56,58,59,61,62,64,65,67,69,70,72,73,75,77,78,80,81,83,85,87,88,90,92,93,95,98,101};

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int * IsotopePad::maxDripN = {1,2,4,6,8,9,12,14,14,17,31,33,35,38,40,42,44,46,49,51,53,55,58,60,62,64,66,69,71,73,75,77,80,82,84,86,88,91,93,95,97,99,102,104,106,108,110,113,115,117,119,121,124,126,128,130,133,134,137,139,141,144,146,148,150,153,155,157,159,161,164,166,168,170,173,175,177,179,182,184,186,187,191,193,192,194};

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
IsotopePad::IsotopePad() : 
xoffset(58),yoffset(35),boxWidth(SegreFrame.SMALLBOXSIZE),boxHeight(SegreFrame.SMALLBOXSIZE),mouseX(0),mouseY(0),maxPlotN(0),maxPlotZ(0),isPStable(new boolean[110][200]),minZDrip(new int[200]),selectColor(MyColors.AIpurple),frameColor(MyColors.gray204),nonSelectColor(new Color(0,0,180)),isoLabelColor(Color.white),initAbundColor(new Color(230,185,0)),smallFont(new java.awt.Font("SanSerif",Font.PLAIN,9)),smallFontMetrics(getFontMetrics(smallFont)),realSmallFont(new java.awt.Font("SanSerif",Font.PLAIN,10)),realSmallFontMetrics(getFontMetrics(realSmallFont)),tinyFont(new java.awt.Font("SanSerif",Font.PLAIN,9)),tinyFontMetrics(getFontMetrics(tinyFont)),bigFont(new java.awt.Font("SanSerif",Font.PLAIN,16)),bigFontMetrics(getFontMetrics(bigFont))
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::initPStable() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::drawMesh(int xoff, int yoff, int w, int h, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::mousePressed(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::mouseEntered(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::mouseExited(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::mouseClicked(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::mouseReleased(MouseEvent me) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::getNZ(int x, int y) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::drawColorSquare(int x, int y, int delx, int dely, Color bgcolor, Color frameColor, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::setIsoLabel(int z, int n, Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::paint(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::PSfile(const char * fileName) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::processSquares(int Z, int N) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
const char * IsotopePad::returnSymbol(int z) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsotopePad::makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame) 
{
    //TODO Auto-generated method stub
    return 0;
}
