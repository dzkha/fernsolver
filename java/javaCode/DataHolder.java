// -----------------------------------------------------------------------------------
//  Class DataHolder to hold various important global data
//  in static variables
// -----------------------------------------------------------------------------------

class DataHolder {

    // Boolean array for active reaction classes (1-8)

    static boolean [][][] includeReaction = new boolean[110][200][9];

    // Boolean array for active reaction components

    static boolean [][][] RnotActive = new boolean[110][200][50];

    // Boolean array indicating whether isotope has been opened for
    // individual reaction selection and saved

    static boolean [][] wasOpened = new boolean[110][200];

    // Arrays for if which reactions to include is read in from file.  These
    // will be initialized in ChooseActiveRates.

    static int [][][] useReadRates = new int [110][200][50];
    static int [][] maxReadRates = new int [110][200];


    // ----------------------------------------------------------------------------------------------
    //  Empty constructor.  This class only used to hold global data in
    //  static variables so it doesn't need to be instantiated.
    // ----------------------------------------------------------------------------------------------

    public DataHolder (int Z, int N) {

    }

}