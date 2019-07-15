#include "LegendPad.h"
//Begin section for file LegendPad.cpp
//TODO: Add definitions that you want preserved
//End section for file LegendPad.cpp



//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
LegendPad::LegendPad(double * contourRange, Color * contourColor) : 
titleFont(new Font("SansSerif",Font.BOLD,11)),smallFont(new Font("SansSerif",Font.PLAIN,11)),realSmallFont(new Font("SansSerif",Font.PLAIN,9)),bigFont(new Font("SansSerif",Font.BOLD,14)),titleFontFontMetrics(getFontMetrics(titleFont)),smallFontFontMetrics(getFontMetrics(smallFont)),realSmallFontFontMetrics(getFontMetrics(realSmallFont)),bigFontFontMetrics(getFontMetrics(bigFont)),contourRange(new double[ContourFrame.numberContours]),contourColor(new Color[ContourFrame.numberContours])
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int LegendPad::paint(Graphics g) 
{
    //TODO Auto-generated method stub
    return 0;
}
