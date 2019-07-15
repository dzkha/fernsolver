#ifndef PLOTREACTIONLIST_H
#define PLOTREACTIONLIST_H
//Begin section for file PlotReactionList.h
//TODO: Add definitions that you want preserved
//End section for file PlotReactionList.h
#include "../rt.jar/java.awt/Panel.h"
#include "../rt.jar/java.awt/Color.h"
#include "../rt.jar/java.awt/Frame.h"



class Checkbox; //Dependency Generated Source:PlotReactionList Target:Checkbox


class ReactionClass1; //Dependency Generated Source:PlotReactionList Target:ReactionClass1





//@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
class PlotReactionList : Panel
{

    //Begin section for PlotReactionList
    //TODO: Add attributes that you want preserved
    //End section for PlotReactionList

    private:


        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color color1;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Color color2;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int maxCases;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int nR;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Checkbox * cbt;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        Checkbox * cb;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        ReactionClass1 * rArray;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int * reactionGroups;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int numberGroups;



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int numberComponents;




    public:

        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        PlotReactionList(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int reactionGrouper(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        const char * stringSetter(int i); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int loadData(); 



        //@generated "UML to C++ (com.ibm.xtools.transform.uml2.cpp.CPPTransformation)"
        int makeTheWarning(int X, int Y, int width, int height, Color fg, Color bg, const char * title, const char * text, boolean oneLine, Frame frame); 



};  //end class PlotReactionList



#endif
