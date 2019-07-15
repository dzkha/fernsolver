#include "ContourFrame.h"
//Begin section for file ContourFrame.cpp
//TODO: Add definitions that you want preserved
//End section for file ContourFrame.cpp



//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean ContourFrame::includeReaction = new boolean[9];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean ContourFrame::helpWindowOpen = false;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::SMALLBOXSIZE = 12;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::MEDBOXSIZE = 26;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::LARGEBOXSIZE = 33;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::numberContours = 11;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double * ContourFrame::contourRange = new double[numberContours];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
Color * ContourFrame::contourColor = new Color[numberContours];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ContourPad ContourFrame::gp;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ScrollPane ContourFrame::sp;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ContourFrame::ContourFrame() : 
titleFont(new java.awt.Font("SanSerif",Font.BOLD,12)),titleFontMetrics(getFontMetrics(titleFont)),buttonFont(new java.awt.Font("SanSerif",Font.BOLD,11)),buttonFontMetrics(getFontMetrics(buttonFont)),textFont(new java.awt.Font("SanSerif",Font.PLAIN,12)),textFontMetrics(getFontMetrics(textFont)),logContour(StochasticElements.logContour),minLogContour(StochasticElements.minLogContour),maxLogContour(StochasticElements.maxLogContour),minLinContour(StochasticElements.minLinContour),maxLinContour(StochasticElements.maxLinContour),sleepZero(1010),hf(new MyHelpFrame()),panelBackColor(MyColors.gray204),panelForeColor(MyColors.gray51),currentSizeIndex(0),contourFraction(new double[numberContours]),cboxPanel(new Panel()),loopcbox(new Checkbox[2]),cbg(new CheckboxGroup()),popColorMap(StochasticElements.popColorMap)
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::makePS(const char * file) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::itemStateChanged(ItemEvent check) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double ContourFrame::stringToDouble(const char * s) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::stringToInt(const char * s) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::printThisFrame(int xoff, int yoff, boolean makeBorder) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
Color ContourFrame::returnRGB(double x) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double ContourFrame::contourMax() 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::componentHidden(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::componentMoved(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::componentResized(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::componentShown(ComponentEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::windowActivated(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::windowClosed(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::windowClosing(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::windowDeactivated(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::windowDeiconified(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::windowIconified(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ContourFrame::windowOpened(WindowEvent e) 
{
    //TODO Auto-generated method stub
    return 0;
}
