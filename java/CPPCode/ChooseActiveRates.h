#ifndef CHOOSEACTIVERATES_H
#define CHOOSEACTIVERATES_H
//Begin section for file ChooseActiveRates.h
//TODO: Add definitions that you want preserved
//End section for file ChooseActiveRates.h
#include "../rt.jar/java.awt.event/ItemListener.h"
#include "../rt.jar/java.awt/Frame.h"
#include "GenericHelpFrame.h"
#include "../rt.jar/java.awt/Font.h"
#include "../rt.jar/java.awt/FontMetrics.h"
#include "../rt.jar/java.awt/Color.h"
#include "../rt.jar/java.awt/CheckboxGroup.h"
#include "../rt.jar/java.awt/Checkbox.h"
#include "../rt.jar/java.awt/Panel.h"
#include "../rt.jar/java.awt/TextField.h"
#include "../rt.jar/java.awt/Label.h"
#include "../rt.jar/java.awt.event/ItemEvent.h"





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class ChooseActiveRates : ItemListener, Frame
{

    //Begin section for ChooseActiveRates
    //TODO: Add attributes that you want preserved
    //End section for ChooseActiveRates

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static boolean thisHelpWindowOpen;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        GenericHelpFrame hf;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font titleFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics titleFontMetrics;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font buttonFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics buttonFontMetrics;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Font textFont;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        FontMetrics textFontMetrics;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color panelForeColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color panelBackColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color disablebgColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color disablefgColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color textfieldColor;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        CheckboxGroup cbg;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Checkbox checkBox;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Panel panel1;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Panel cboxPanel;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static TextField fileName;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Label fileNameL;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        static const char * makeHelpString(); 




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ChooseActiveRates(int width, int height, const char * title, const char * text); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int itemStateChanged(ItemEvent check); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int disableAllRates(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int readIncludedRates(const char * filename); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int makeAllRatesActive(boolean isActive); 



};  //end class ChooseActiveRates



#endif
