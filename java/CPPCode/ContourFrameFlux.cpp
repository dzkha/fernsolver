#include "ContourFrameFlux.h"
//Begin section for file ContourFrameFlux.cpp
//TODO: Add definitions that you want preserved
//End section for file ContourFrameFlux.cpp



//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean ContourFrameFlux::includeReaction = new boolean[9];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean ContourFrameFlux::helpWindowOpen = false;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::SMALLBOXSIZE = 12;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::MEDBOXSIZE = 26;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::LARGEBOXSIZE = 33;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::numberContours = 11;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
Color * ContourFrameFlux::contourColor = new Color[numberContours];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ContourPadFlux ContourFrameFlux::gp;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ScrollPane ContourFrameFlux::sp;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ContourFrameFlux::ContourFrameFlux(const char * contourLegend, boolean isLog, double lower, double upper, double * twa) : 
titleFont(new java.awt.Font("SanSerif",Font.BOLD,12)),titleFontMetrics(getFontMetrics(titleFont)),buttonFont(new java.awt.Font("SanSerif",Font.BOLD,11)),buttonFontMetrics(getFontMetrics(buttonFont)),textFont(new java.awt.Font("SanSerif",Font.PLAIN,12)),textFontMetrics(getFontMetrics(textFont)),sleepZero(1010),hf(new MyHelpFrame()),panelBackColor(MyColors.gray204),panelForeColor(MyColors.gray51),currentSizeIndex(0),contourFraction(new double[numberContours]),contourRange(new double[numberContours]),cboxPanel(new Panel()),loopcbox(new Checkbox[2]),cbg(new CheckboxGroup())
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::makePS(const char * file) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::itemStateChanged(ItemEvent check) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double ContourFrameFlux::stringToDouble(const char * s) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::stringToInt(const char * s) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::printThisFrame(int xoff, int yoff, boolean makeBorder) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
Color ContourFrameFlux::returnRGB(double x) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double ContourFrameFlux::contourMax() 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::componentHidden(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::componentMoved(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::componentResized(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::componentShown(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::windowActivated(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::windowClosed(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::windowClosing(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::windowDeactivated(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::windowDeiconified(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::windowIconified(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrameFlux::windowOpened(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
