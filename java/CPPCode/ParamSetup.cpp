#include "ParamSetup.h"
//Begin section for file ParamSetup.cpp
//TODO: Add definitions that you want preserved
//End section for file ParamSetup.cpp


//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean ParamSetup::helpWindowOpen = false;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double ParamSetup::LOG10 = 0.434294482;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ParamSetup::ParamSetup(int width, int height, const char * title, const char * text) : 
hf(new GenericHelpFrame("","",0,0,0,0,0,0)),titleFont(new java.awt.Font("SanSerif",Font.BOLD,12)),titleFontMetrics(getFontMetrics(titleFont)),buttonFont(new java.awt.Font("SanSerif",Font.BOLD,11)),buttonFontMetrics(getFontMetrics(buttonFont)),textFont(new java.awt.Font("SanSerif",Font.PLAIN,12)),textFontMetrics(getFontMetrics(textFont)),panelForeColor(Color.black),panelBackColor(Color.white),disablebgColor(new Color(230,230,230)),disablefgColor(new Color(153,153,153)),framebgColor(new Color(230,230,230)),cbg(new CheckboxGroup()),checkBox(new Checkbox[2])
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ParamSetup::itemStateChanged(ItemEvent check) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ParamSetup::printThisFrame(int xoff, int yoff, boolean makeBorder) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int ParamSetup::makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
const char * ParamSetup::makeHelpString() 
{
    //TODO Auto-generated method stub
    return 0;
}
