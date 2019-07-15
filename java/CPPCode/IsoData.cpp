#include "IsoData.h"
//Begin section for file IsoData.cpp
//TODO: Add definitions that you want preserved
//End section for file IsoData.cpp


//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
Checkbox IsoData::checkBox = new Checkbox[9];

//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
IsoData::IsoData(int width, int height, const char * title, const char * text) : 
titleFont(new java.awt.Font("SanSerif",Font.BOLD,12)),titleFontMetrics(getFontMetrics(titleFont)),buttonFont(new java.awt.Font("SanSerif",Font.BOLD,11)),buttonFontMetrics(getFontMetrics(buttonFont)),panelForeColor(Color.black),panelBackColor(new Color(204,204,204)),panelBackColor2(new Color(240,240,240)),panelBackColor3(new Color(190,190,190)),Z(IsotopePad.protonNumber),N(IsotopePad.neutronNumber)
{
    //TODO Auto-generated method stub
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsoData::upDateComponents() 
{
    //TODO Auto-generated method stub
    return 0;
}
//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
int IsoData::printThisFrame(int xoff, int yoff, boolean makeBorder) 
{
    //TODO Auto-generated method stub
    return 0;
}
