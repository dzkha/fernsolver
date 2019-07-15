#include "SegreFrame.h"
//Begin section for file SegreFrame.cpp
//TODO: Add definitions that you want preserved
//End section for file SegreFrame.cpp


//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
FileOutputStream SegreFrame::toFileS;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
PrintWriter SegreFrame::toFile;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean SegreFrame::includeReaction = new boolean[9];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
boolean SegreFrame::helpWindowOpen = false;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::SMALLBOXSIZE = 12;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::MEDBOXSIZE = 26;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::LARGEBOXSIZE = 33;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
IsotopePad SegreFrame::gp = new IsotopePad();

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
PlotParams SegreFrame::pm;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ScrollPane SegreFrame::sp;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
ProgressMeter SegreFrame::prom;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
StochasticElements SegreFrame::se;

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
SegreFrame::SegreFrame() : 
titleFont(new java.awt.Font("SanSerif",Font.BOLD,12)),titleFontMetrics(getFontMetrics(titleFont)),buttonFont(new java.awt.Font("SanSerif",Font.BOLD,11)),buttonFontMetrics(getFontMetrics(buttonFont)),textFont(new java.awt.Font("SanSerif",Font.PLAIN,12)),textFontMetrics(getFontMetrics(textFont)),hf(new MyHelpFrame()),panelBackColor(MyColors.gray204),panelForeColor(MyColors.gray51),warnColorBG(MyColors.warnColorBG),currentSizeIndex(0),cboxPanel(new Panel()),checkBox1(new Checkbox(" Class 1     ")),checkBox2(new Checkbox(" Class 2     ")),checkBox3(new Checkbox(" Class 3     ")),checkBox4(new Checkbox(" Class 4     ")),checkBox5(new Checkbox(" Class 5     ")),checkBox6(new Checkbox(" Class 6     ")),checkBox7(new Checkbox(" Class 7     ")),checkBox8(new Checkbox(" Class 8     ")),checkBox9(new Checkbox(" All Classes "))
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::makePS(const char * file) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::launchOldPlot() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::makeRepaint() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::itemStateChanged(ItemEvent check) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
double SegreFrame::stringToDouble(const char * s) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::stringToInt(const char * s) 
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::printThisFrame(int xoff, int yoff, boolean makeBorder) 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int SegreFrame::makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame) 
{
    //TODO Auto-generated method stub
    return 0;
}
