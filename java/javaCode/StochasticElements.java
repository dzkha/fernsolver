// ----------------------------------------------------------------------------------------------------------------
//  The class StochasticElements implements an explicit algorithm
//  for evolution of element abundances.  This version is adapted to implement
//  asymptotic solutions when Fplus ~ Fminus, and more recently partial
//  equilibrium solutions.
//
//  Mike Guidry, April 24, 2010
// -----------------------------------------------------------------------------------------------------------------

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Vector;
import java.util.Properties;
import java.text.*;
import java.lang.*;

//import com.sun.j3d.utils.applet.MainFrame;

/*  NOTES:
 RGC[Z][N][j]  is reaction group class for reaction j
 totalEquilReactions is total equilibrated reactions used in partial equilibrium
 RCGmember[Z][N][j]  is member index of reaction j in reaction group
 reacIsActive[Z][N][j]  is whether reaction j NOT in equilibrium
 isotopeInEquil[Z][N] is whether is any any reaction group that is equilibrated
 */

class StochasticElements implements Runnable {

	static FileOutputStream to;
	static FileOutputStream to2;
	static FileOutputStream to3;
	static FileOutputStream to4;
	static FileOutputStream to5;
	static FileOutputStream to6;
	static FileOutputStream to7;
	static FileOutputStream to8;
	static FileOutputStream to9;
	static FileOutputStream to10;
	static FileOutputStream to11;
	static FileOutputStream to12;
	static FileOutputStream to13;
	static FileOutputStream to14;
	static FileOutputStream to15;
	static FileOutputStream to16;
	static FileOutputStream to17;
	static FileOutputStream to18;
	static FileOutputStream to19;
	static FileOutputStream to20;

	static PrintWriter toChar;
	static PrintWriter toY;
	static PrintWriter toDiag;
	static PrintWriter toRestart;
	static PrintWriter toPlot;
	static PrintWriter toF90;
	static PrintWriter toX;
	static PrintWriter toRateStuff;
	static PrintWriter toFluxQSS;
	static PrintWriter toReduced;
	static PrintWriter toGraph;
	static PrintWriter toHydroProfile;
	static PrintWriter toNet;
	static PrintWriter toAbundance;
	static PrintWriter toGroups;
	static PrintWriter toEquil;
	static PrintWriter toTau;
	static PrintWriter toTauRatio;
	static PrintWriter toRateData;   // Data file for CUDA rate test
	static PrintWriter toCUDAnet;

	private static Properties printprefs = new Properties();

	static long computeTime; // Elapsed computing time in millisec

	static final double LOG10 = 0.434294481903251; // Conversion natural log to
													// log10
	static final double MEV = 931.494; // Conversion of amu to MeV
	static final double ECON = 9.5768e17; // Convert MeV/nucleon/s to erg/g/s

	// Collect default GUI parameters collected below for easy change:

	static boolean doSS = false; // Flag to enable steady state approximation
	static boolean doAsymptotic = true; // Flag to enable possible asymptotic
										// processing
	static boolean sophia = false;  // Whether to use Sophia He formula for
									// asymptotic
	static boolean asyPC = false;   // Whether to use predictor-corrector for
									// asymptotic
	static boolean isMott = false;  // Whether to use the Mott thesis asymptotic
									// formula
	int nit = 1; // Max iterations for QSS

	static double T9 = 5; // Temperature in units of 10^9 K
	static double rho = 1e8; // Density (g/cm^3)

	static double stochasticFactor = 0.01;// 0.01; // Integration step tolerance
											// factor
	
	/*
	 * Variable massTol is the maximum permitted deviation of the sum of mass
	 * fractions between two successive timesteps. If the deviation is larger in
	 * a step, the step size is reduced until deviation less than massTol.
	 * Should be adjusted to the largest value that gives acceptable
	 * conservation of particle number for a given problem. Can be reset from
	 * the ParameterSetup panel.
	 */
	
	static double massTol = 1e-7;
	static boolean useCustomTimestepper = false; // Controls whether constant
													// stochasticFactor and
													// massTol are
	// used (if false), or whether customizeTimeStepper() is called to
	// adjust these parameters with time (if true).

	// Temporary diagnostic boolean to control whether diagnostic print
	// statements made looking for why Y can sometimes
	// become small negative in the predictor-corrector schemes (Oran-Boris and
	// Mott). Set to false to suppress that
	// diagnostic output.

	static boolean checkPC = false;

	static boolean plotEnergy = false; // Whether to plot energies
	static boolean plotdE = false; // If plotEnergy true, plot E (false) or dE
									// (true)
	static double Ye = 0.5; // Electron fraction
	static byte abundPref = 2; // Pref for abund input. 0: X, 1: Y, 2: file (see
								// AbundanceData.java)

	static int tintMax = 402; // max number of time intervals
	static int nintervals = 100; // number intervals to output plot info (max is
									// tintMax)

	// Following are the actual integration limits
	static double logtmin = -20; // base-10 log of min time
	static double logtmax = -2;// -3; // base-10 log of max time

	// These are the corresponding plotting limits (which can be a subset of the
	// integration range. Set initially to the integration limits but can be
	// changed
	// in the parameter setting interface.
	static double logtminPlot = -16;// -16;
	static double logtmaxPlot = logtmax;

	// Filenames to be displayed in dropdown of network selection combo box in
	// ChooseActiveIsotopes.
	// The default choice is set by the variable activeFileName.

	static String[] networkFileName = { "jin/approx13.inp", "jin/netsu150.inp",
			"jin/approx19.inp", "jin/reduced48.inp", "jin/reduced70.inp",
			"jin/reduced116.inp", "jin/reduced194.inp", "jin/reduced268.inp",
			"jin/reduced365.inp", "jin/reducedNova.inp",
			"jin/reducedNova134.inp", "jin/tripleAlpha.inp", "jin/cno.inp",
			"jin/cnoBi.inp", "jin/cnoAll.inp", "jin/pp.inp", "jin/alpha4.inp",
			"jin/alpha4Plus.inp", "jin/alpha4PlusN.inp",
			"jin/alpha4PlusNP.inp", "jin/alphaPlusNP.inp",
			"jin/alphaPlusP.inp", "jin/alpha3.inp", "jin/test.inp" };

	static String activeFileName = "jin/approx13.inp";
	static String activeRatesFileName = "jin/approx13Rates.inp";

	static boolean constantHydro = true; // True: constant T9,rho,Ye; False:
											// read hydro profile
	static String profileFileName = "jin/torch47Profile.inp"; // Hydro profile

	static String abundFileName = "jin/abundance.inp"; // Initial abundances if
														// read from file

	static int xtics = 5; // Number of tic intervals on horizontal axis
	static int ytics = 14; // Number of tic intervals on vertical axis
	// Min and max for y axis in plot
	static double yminPlot = -14;
	static double ymaxPlot = 0;

	// Decimal places for x and y axes on plot
	static int xdeci = 2;
	static int ydeci = 1;

	// Delay between progress meter updates in milliseconds
	long promUpdateMillis = 2000;
	long promTimeTarget;

	static boolean useReadRatesFlag = false;
	static boolean initialRatesZeroFlag = false;

	static int maxToPlot = 500; // Upper limit on curves to plot. The number of
								// 1D
	// plots to be made is the min of this number and
	// maxIsotopesUsed. However, legends will be displayed
	// only for a max of 101 curves.

	static boolean noNeutronsFlag = false; // If true, suppress neutron
											// reactions
	static int maxLightIon = 1000; // Max mass light ion to allow reaction
	// of heavy seed with. e.g., if = 4,
	// He4 + C12 allowed but not C12 + C12
	// Cutoff for Y in output of reduced network
	private double reducedNetYcut = 1e-25;

	// Set range of Z and N to be processed in network. Note that the data
	// subdirectory must
	// contain serialized data files for the corresponding Z and N ranges, and
	// see the note below
	// concerning the display ranges pmaxPlot and nmaxPlot.

	static byte pmax = 53;// 45;//36; // Max proton number to calc
	static byte nmax = 56;// 50;//65;//55; // Max neutron number to calc
	static byte pmin = 3; // Min proton number to calc as heavy seed
	static final byte nWidth = 127; // Max number neutrons for given Z

	// Note: pmax and nmax set array sizes, so generally pmaxPlot must be not
	// greater than
	// pmax and nmaxPlot not greater than nmax.

	static byte pmaxPlot = 36;// 53;//23;//53;//45;//36; // Max proton number to
								// plot in Segre
	static byte nmaxPlot = 46;// 56;//27;//56;//50;//65; // Max neutron number
								// to plot in Segre
	static final int maxSeeds = 500; // Max number of seed isotopes
	static final byte maxRates = 50; // Max # rates from any one isotope

	static double ERelease; // Energy released
	static double dERelease; // Energy released per unit time from Q values
	// (updated from ReactionClass1)
	static double YH; // Hydrogen-1 abundance
	static double YHe; // Helium-4 abundance
	static double initH; // Initial number of protons
	static double initHe; // Initial number of He-4
	static double nT; // Total # nucleons (conserved)
	static final double totalSeeds = 1e30; // Total # of heavy (A>4) seeds
	static int numberNuclei = 0;
	static int numberCouplings = 0;
	static double f; // Conversion factor between the
	// number of counts and the
	// abundance (abundance Y = number/f;
	// mass fraction X = (number/f)*A where
	// A=Z+N is the atomic mass number).

	static int numberSeeds = 0;
	static double[][] pop = new double[pmax + 1][nmax];
	static double[][] tempPop = new double[pmax + 1][nmax];

	static double[][] dpop = new double[pmax + 1][nmax]; // Total pop update for
															// timestep
	static double[][] dpopPlus = new double[pmax + 1][nmax]; // Positive pop
																// update for
																// timestep
	static double[][] dpopMinus = new double[pmax + 1][nmax]; // Negative pop
																// update for
																// timestep
	static boolean[][] isAsymptotic = new boolean[pmax + 1][nmax]; // Whether
																	// asymptotic
																	// soln

	// Whether an isotope is ever populated in the entire calculation
	static boolean[][] hasBeenPopulated = new boolean[pmax + 1][nmax + 1];

	// Max pop for a given isotope at any time in calculation
	private double[][] maxpop = new double[pmax][nmax];

	// Quantities needed for asymptotic approximations

	static double[][] Fplus = new double[pmax][nmax]; // Flux increasing
														// population
	static double[][] Fminus = new double[pmax][nmax]; // Flux decreasing
														// population
	static double[][] FratPrev = new double[pmax][nmax]; // Fplus/keff from
															// previous timestep
	static double[][] keff = new double[pmax][nmax]; // Effective depletion
														// constant

	static double[][] Y = new double[pmax][nmax];
	static double[][] Yzero = new double[pmax][nmax];
	static double[] seedY = new double[maxSeeds];

	static final int[][] numberReactions = new int[pmax + 1][nWidth];

	static int tintNow = 0; // index of current time interval

	static int numdt; // number of time intervals
	static double[] timeIntervals = new double[tintMax];

	// Static variable timeNow used in classes ContourFrame, ContourPlot, and
	// AbPlotFrame.

	static double[] timeNow = new double[tintMax];
	static double[] eNow = new double[tintMax];
	static double[] deNow = new double[tintMax];
	static double elapsedTime = 0;
	static double logtfac;

	static double[][][] intPop = new double[pmax + 1][nmax + 1][tintMax + 1];
	static double[][][] sFplus = new double[pmax + 1][nmax + 1][tintMax + 1]; // F+ save
	static double[][][] sFminus = new double[pmax + 1][nmax + 1][tintMax + 1]; // F- save
	// Graphics working arrays
	static double[][][] twa = new double[pmax + 1][nmax + 1][tintMax + 1]; 
	static double[][][] twa2 = new double[pmax + 1][nmax + 1][tintMax + 1]; 

	static byte seedProtonNumber[] = new byte[maxSeeds];
	static byte seedNeutronNumber[] = new byte[maxSeeds];
	static double seedNumber[] = new double[maxSeeds];

	static byte numberRates;
	static double Rrates[] = new double[maxRates];
	static double activeRates[][] = new double[maxSeeds][maxRates];

	// Following will hold light ion on light ion rates for He4 (alpha), He3, H3 (triton), 
	// H2 (deuteron), H1 (proton) and neutron, respectively.
	
	static double alphaRates[] = new double[41]; 
	static double he3Rates[] = new double[11]; 
	static double h3Rates[] = new double[8]; 
	static double h2Rates[] = new double[7];
	static double h1Rates[] = new double[3];
	static double n0Rates[] = new double[1]; 

	static double time;
	static double tmax;
	static double tmin;
	static double deltaTime;
	static byte reaction;
	static byte Z;
	static byte protonNumberZero;
	static byte N;
	static byte neutronNumberZero;
	static byte seedIndex;
	static double startNumber;
	static double maxValue;

	static GraphicsGoodies2 gg = new GraphicsGoodies2();
	public static ContourPlotter cd;
	static ReactionClass1 RObject[][][];
	double[][][] masterRates = new double[pmax][nmax][maxRates];

	static double boxThresh;         // Minimum pop number for box for processing
	static double fluxFloor = 0;     // Minimum flux to process
	static boolean outdone = false;  // Flag to indicate whether last output time passed
	static double gridSpillOff;      // Total test particles that would transfer off grid

	static double[][] flux = new double[maxSeeds][maxRates]; // Fluxes at given timestep
	static String maxRateString;     // Reaction description of max rate reaction
	static String minRateString;     // Reaction description of min rate reaction

	static double[] popOut = new double[maxRates]; // Particles transferred from
	                                               // given isotope in a timestep
	static int[] tempIndex = new int[maxRates]; // Reaction index for a given transition
	static boolean integrateWithJava = true;    // True=java, false=compiled integration

	static double fastestCurrentRate = 1e-30;
	static double slowestCurrentRate = 1e30;
	static double[] fastestRate = new double[tintMax];
	static double[] slowestRate = new double[tintMax];
	static double[] tstep = new double[tintMax];    // Store size of timestep for plot
	static int maxSeedsUsed = 0;

	static final double longCheck = 2e30;

	// Cut limits when reading in rates. This must be selected carefully
	// for a given case. Leave limitRates = false unless analyzed carefully.
	// It appears to only improve speed by maybe 25% in typical cases, so not so useful.

	boolean limitRates = false;
	static double rateFloor = 5e2;
	double T9min = 1;
	double T9max = 8;
	static double[] T9cases = new double[5];

	Thread runner = null;
	boolean runThread = false;
	int sleepTime = 0;

	// Variables associated with counting the total number of reaction
	// links at each timestep.

	static int reacCounter = 0;
	static int[] tempReacNum = new int[20];
	static int[][] reacNum = new int[20][tintMax + 1];
	static int[] reacTot = new int[tintMax + 1];
	static int[] activeIsotopes = new int[tintMax + 1];
	static double[] reactionsPerIsotope = new double[tintMax + 1];

	// Logical variables for diagnostic output. Roughly diagnoseI and
	// diagnoseII represent, respectively, increasing detail. If diagnoseI or
	// diagnoseII are true, diagnostic info will be sent to the file
	// diagnostics.out for the single time interval immediately after the time
	// set by the variable tbeg.

	static boolean diagnoseI = false;
	static boolean diagnoseII = false;    // If this is true, make diagnoseI true too

	// Diagnostics flags. Both must be false to begin with

	static boolean doHotStep = false;
	static boolean doneHotStep = false;

	// Beginning of timestep for which diagnostics will be output to diagnostics.out.
	// When the integration passes the time tbeg, diagnostics will be sent to
	// the file diagnostics.out for the next timestep (only), if diagnoseI or diagnoseII
	// are true.

	static final double tbeg = 2.2273e-6;

	// Cubic spline interpolator class for table interpolations

	static SplineInterpolator interpolateT;
	static SplineInterpolator interpolaterho;
	SplineInterpolator interpolatepf = new SplineInterpolator();

	// Boolean determining whether the hydro profile interpolation is cubic spline
	// (if splineInterpolation=true) or linear (if splineInterpolation=false).
	// This was causing a crash in the color interpolation on output if set to true.
	// Added exception handling in InterpolateColorTable to prevent that, but did not
	// track down why it is happening. Seems to be associated with the 2D flux plotter.

	static boolean splineInterpolation = false;

	// Arrays for hydro profile if hydro profile used

	static double[] hydroTime;       // 1D array for time in hydro profile
	static double[] hydroT;          // 1D array for T in hydro profile
	static double[] hydrorho;        // 1D array for rho in hydro profile

	// Arrays for equation of state lookup if lookup used

	static double[] eqStateEnergy;   // Energy for equation of state lookup
	static double[] eqStateT;        // Temperature for equation of state lookup

	String tempstring1 = "-----------------------------------------------------------------";

	String tempstring2, tempstring3, tempstring3b, tempstring4, tempstring5;

	static int[][][] serialIndex = new int[pmax][nWidth][50];

	static int totalTimeSteps;
	static int totalTimeStepsZero;

	// Current arguments for RObject[Z][N-nmin][j] and .reacString
	static String currentReacString;
	static int currentZdis;
	static int currentNdis;
	static int currentRObIndex;

	static double rescaler;

	static int couplingCounter = 0;   // Total number of reaction couplings in timestep
	static int maxCouplings = 0;      // Max couplings for any timestep

	// Number curves to be displayed in final plot. Set from AbPlotFrame
	// and read by GraphicsGoodies.
	
	static int numberCurvesToShow;    

	// Flag to keep from starting calculation before setting parameters
	static boolean parametersWereSet = false; 

	static boolean isNeutronReaction[][][];     // Whether neutrons on left side
	static boolean isProtonReaction[][][];      // Whether protons on left side
	static boolean isAlphaReaction[][][];       // Whether alphas on left side

	static double maxdpopp;
	static double tchecker;
	static int maxdpoppZ;
	static int maxdpoppN;
	static double sumpop;

	boolean outputPlotFluxes = true;    // Flag to output flux info to fluxQSS.out for plot
	boolean showFluxLines = false;      // Flag to output fluxes to screen at plot timesteps

	static double sumX = 1;             // Sum of mass fractions X. Should be 1.
	double sumXLast = 1;                // Value of sumX from last timestep
	double massChecker;                 // Check for adaptive timestep on sumX

	// Following variables control how many contours, whether lin or log,
	// and the spacing of the contours for the output n-z plane animation.
	// The contours are mapped to the interval 0-1. The min and max must lie
	// in this interval (and for log scales neither can be zero). These
	// variables are read by the class ContourFrame to set up the contour display.

	static boolean logContour = true;         // log (true) or linear (false) contours
	static double minLogContour = 1.0E-15;    // Minimum rel contour if log scale
	static double maxLogContour = 1.0;        // Maximum rel contour if log scale
	static double minLinContour = 0.0;        // Minimum rel contour if linear scale
	static double maxLinContour = 1.0;        // Maximum rel contour if linear scale

	double asycut = 1;               // Cutoff for kdt to make asymptotic approx (default 1)
	static int numberAsymptotic;     // Number of isotopes asymptotic
	int numberAsymptoticLast = 0;    // Number asymptotic from last step

	static int numberStacked = 5;
	static double[] timestepStacker = new double[numberStacked];

	static double[][] masses = new double[pmax + 1][nmax + 1]; // Isotopic masses
	static double networkMass;                   // Total mass in network
	static double lastMass;                      // Total network mass prev timestep
	static double dEReleaseA;                    // Differential E release from masses
	static double EReleaseA;                     // Total E released from masses

	// Estimate of total isotopes populated in run. It is the number of non-zero
	// populations written to the restart file, so is actually the total number of
	// non-zero populations after the last timestep.
	
	static int numberIsotopesPopulated = 0; 

	static boolean plotY = false;       // True to plot Y and false to plot X.
	static boolean linesOnly = true;    // True to plot line; false to plot isotope symbols
	static boolean blackOnly = false;   // True to plot black lines; false for color

	// Switch to write 3D output. If set to true, data are sent to filename in 
	// variable outfile3D for viewing with rateViewer3D.

	static boolean write3DOutput = false;

	// Parameter to set the lowerCutoff to be displayed in 3D animation. If
	// abundance < lower3DCutoff, it's truncated to zero.

	private static double lower3DCutoff;

	// Variables controlling 3d File output name and upper ranges in p and n for output
	private static String outfile3D = "output/3D.data";
	private static int pmax3D = pmaxPlot - 1;
	private static int nmax3D = nmaxPlot - 1;

	// Whether to output abundances Y (false) or mass fractions X (true) for 3D plot
	private static boolean showMassFraction = true;

	static double deltaTimeRestart; // Saved correct dt when dt cut at plot output step

	static boolean saveRestartFiles = true;           // Whether to write restart files
	static boolean renormalizeMassFractions = false;  // Whether to renormalize Sum X

	// Timestep set by flux constraint, before requiring particle number conservation.
	static double dtFlux; 
	// Timestep after iterating to require particle number conservation.
	static double stepTime; 
	

	private double asysumX;               // sumX for asymptotic isotopes

	static double Ymin = 0.0; // 1e-28;   // Cutoff abundance for box processing

	static String myComment = "(Optional comment)";   // Comment text inserted in output

	// Isotope selection mode: 1=all, 2=mouse selected by shift-click, 3=read from file 
	// (filename in variable activeFileName). See class ChooseActiveIsotopes.
	
	static int isoSelectionMode = 3; 

	// Rate selection mode: 1=all, 2=mouse selected by ctrl-click, 3=read from file
	// (name in variable activeRatesFileName). See class ChooseActiveRates.
	
	static int rateSelectionMode = 1; 

	// Initial abundance specified by: 1=individual selection, 2=read from file 
	// (name in variable abundFileName), 3=solar abundances.
	
	static int initAbundMode = 2; 

	// Formatted output strings summarizing parameters
	static String os1 = "";         
	static String os2 = "";
	static String os3 = "";
	static String os4 = "";
	static String os5 = "";

	// The sum of deviations of sumX from 1 if renormalizeMassFractions is true.
	private double totalCorrectionSumX = 0; 

	// Whether to include only triple-alpha for alpha-alpha in lightIonBurner (true), or
    // to include all alpha-alpha reactions (false).
	
	static boolean tripleAlphaOnly = false; 

	// Lower limit on loop in lightIonBurner. Value depends on boolean thripleAlphaOnly.
	private int alphaLow; 

	// Whether to display SVN version numbers in output. Program will crash unless the 
	// shell from which launched recognizes the subversion command "svn info" (meaning 
	// that a command-line version of subversion is installed). If this is not true, 
	// set this variable to false.
	
	static boolean showSVNversion = true; 
	static String vSVN = "";              // String to hold SVN version numbers

	private double sumFplus;              // Sum of enhancing fluxes F+
	private double sumFminus;             // Sum of depleting fluxes F-

	// Store Z,N of isotopes active in the network
	static int[] Zactive;
	static int[] Nactive;
	static int numberActiveIsotopes;      // Total number active isotopes

	private String massFileName = "jin/masses.inp";     // Input file for mass table

	// Output file graphFile.out of alpha rates for Maple utilityPlotter.mw to read.
	private boolean plotAlphaRates = false; 

	// Format reduced network file for compiled code. If false, format for Java code.
	private boolean writeCompiledReduced = true; 

	// Parameters for adjustment of timestep in advanceOneTimestep() to ensure
	// mass conservation. (Note: The values of these are now set dynamically in 
	// setBumper(massTol).)
	
	private double upbumper;   // = 0.8*massTol; // 0.9*massTol
	private double downbumper; // = 0.2; //0.1;
	private double massTolUp;  // = 0.5* massTol; //0.25*massTol;

	// Whether to do integration, or read in and plot old file
	static boolean doIntegrate = true; 
	static String oldYfile = "Y.inp";

	// Variables to hold plot output file data
	static double[] T9Save = new double[tintMax];
	static double[] rhoSave = new double[tintMax];
	static double[] YeSave = new double[tintMax];
	static double[] dtSave = new double[tintMax];
	static double[] sumXSave = new double[tintMax];

	// Variables holding sum of mass fractions in isotopic regions for later plotting
	static double[] lightX = new double[tintMax];    // Sum X for Z < 6
	static double[] CMgX = new double[tintMax];      // Sum X for 6 <= Z < 14
	static double[] SiX = new double[tintMax];       // Sum X for 14 <= Z < 24
	static double[] FeX = new double[tintMax];       // Sum X for 24 <= Z <36
	static double[] NSEX = new double[tintMax];      // Sum X for light + Fe

	// Following variables to deal with partition function information (to be read from
	// the Rauscher file winvn.nosmo)

	// Array to hold value of partition function at 24 discrete Ts for each Z, N
	static double[][][] pf = new double[pmax + 1][nmax + 1][24];

	// Array to hold the partition function at current T for each active isotope (Z,N)
	static double[][] pfNow = new double[pmax + 1][nmax + 1];

	// Temperatures (T9) for 24 entries of partition function array pf. These
	// will be converted to log10(T9) in the constructor below since interpolation will
	// actually be done in log10(T9) rather than in T9 directly.

	static double[] Tpf = { 0.1, 0.15, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
			1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };

	// File containing partition function entries
	static String pfFile = "jin/partitionFunction.inp";

	// Variable to hold the lower index for interpolation of the partition functions in T
	int lowPFindex;

	// Lowest T9 for partition function interp (partition functions assumed 1 below this)
	double pfCut = 1;
	double logpfCut = LOG10 * Math.log(pfCut);

	// Class variable to hold current logT (base-10 log of T9)
	static double logT;

	// Variables to hold the min and max Z for the current network, and the min
	// and max N for each Z between the min and max. These limits will permit sums over
	// the boxes to be restricted to those in the network rather than everything between
	// the driplines

	static int minNetZ;                       // min Z in network
	static int maxNetZ;                       // max Z in network
	static int[] minNetN = new int[100];      // min N for each Z
	static int[] maxNetN = new int[100];      // max N for each Z

	int nmin;

	// Atomic mass for each (Z,N) in amu
	static double AA[][] = new double[pmax + 1][nmax + 1]; 
	// Temporary storage of Fminus[Z][N], Fminus[Z][N], and keff[Z][N]
	double FplusZero[][] = new double[pmax + 1][nmax + 1]; 
	double FminusZero[][] = new double[pmax + 1][nmax + 1];
	double keffZero[][] = new double[pmax + 1][nmax + 1]; 

	static String calcModeString = "";    // String holding calculation mode
	static int boxPopuli = 0;             // Total boxes populated for AbPlotFrame to plot
	public String hostName = "";          // String to hold hostname of machine for output

	// The output data file that can be read back in and plotted by choosing
	// "Plot Old" on the interface (But copy to new name since this file is overwritten 
	// when the program is launched again.)

	static String outputDataFile = "output/Y.out";

	static String mapleXoutFile = "output/X.out";   // Output data file for Maple

	// Output data file for hydroProfile.out hydro profile corresponding to
	// current calculation. This file is overwritten when the program is loaded, so 
	// copy to another name to save for later input as a hydro profile. This for example 
	// is useful if a Flash coupled hydro data file is being plotted, since then the 
	// output file will contain the hydro profile from the Flash calculation
	// that can be used for later postprocessing.

	static String hydroProfileFile = "output/hydroProfile.out";

	// Which color table to use in contour plots. Current choices are "cardall",
	// "guidry", "bluehot", "caleblack", "calewhite", "guidry2", "hot", "greyscale", 
	// and "none". If "none" or not in this list, reverts to calculating Guidry 
	// overlapping gaussians, which is the same as "guidry" table. "guidry2" is a 
	// variant on "guidry". Setting popColorInvert=true inverts the color map for the
	// population display. See the class MyColors for definitions of color maps.
	// The variable popColorMap can be set from the parameter window.

	static String popColorMap = "guidry2";
	static boolean popColorInvert = false;

	// As for population color maps above, but for the 3D flux animation. The
	// variable fluxColorMap can be set from the parameter window.

	static String fluxColorMap = "caleblack";
	static boolean fluxColorInvert = true;

	static boolean doFluxPlots = true;      // Whether to plot fluxes
	// Long or short format for line plots (set from parameter window)
	static boolean longFormat = false; 
	// Number of curve legends to put on line plot in short format
	static int legendsShortFormat = 31; 
	// Name of fortran executable for compiled integration
	String fortranExecutable = "approx13"; 

	boolean isCNONetwork = false;
	boolean isAlphaNetwork = false;

	IsoVector[] netVector;           // Objects defining network vector components

	int reactionVector[][][][];

	// Parameters to deal with defining reaction groups for approach to equilibrium
	boolean readReactionGroups = false;
	String reactionGroupFile = "jin/alpha4Groups.inp";
	int numberReactionGroups;              // Number reaction groups

	ReactionGroup RGgroup[];

	// Whether reaction has been used already in forming reaction groups
	boolean reactionChosen[][][] = new boolean[pmax + 1][nmax + 1][50];

	// Array to hold equilbrium abundances for a given equiibrium reaction pair.
	// Dimension 7 because from reaclib classes that is max number of isotopes
	// that could participate in an equilibrium pair.

	double[] equiAbund = new double[7];

	int RGC[][][] = new int[pmax][nmax][maxRates]; // Reaction group class for each reaction
	int RGCmember[][][] = new int[pmax][nmax][maxRates]; // Reaction group member index
	boolean reacIsActive[][][] = new boolean[pmax][nmax][maxRates]; // Is reaction active

	int totalReactions;       // Total reactions for isotope
	int totalEquilReactions;  // Total equilibrated reactions for isotope

	/*  -----------------------------------------------------------------------
	 * Partial equilibrium control parameters: Generally to just display
	 * equilibrium quantities (without imposing equilibrium) set
	 * equilibrate=true and imposeEquil=false. To impose equilibrium, set
	 * imposeEquil = true (and equilibrate = false to suppress some output). For
	 * either equilibrate or imposeEquil, actions only taken after time >
	 * equilibrateTime. To do normal asymptotic integration (no PE), set both
	 * equilibrate and equilibrateTime to false, and be sure appropriate values
	 * for stochasticFactor and massTol are set in the popup interface.
	 * ------------------------------------------------------------------------ */
	
	// Whether to compute and display partial equilibrium quantities
	static boolean equilibrate = false;
	// Whether actually to impose partial equilibrium
	static boolean imposeEquil = false;  
	// Time to begin trying to impose partial equilibrium
	static double equilibrateTime = 1e-9; 
	// Tolerance for checking whether Ys in RG in equil
	static double equiTol = 0.01;     
	boolean displayEquilReac = false; // Whether to output reactions in equil at
									  // each plot timestep
	boolean displayAsyIsotopes = true;// Whether to display asy isotopes to screen
	static double dtScale = 0.7;      // Scale factor for time=dtScale*dt if
							          // time>startLinear
	static double startLinear = 1e20; // Controls when starting linear timestep
								      // with partial equil
	double Ythresh = 0;               // Threshold abundance for imposing equil in reaction
						              // couple
	boolean normX = true;             // Controls whether sum X = 1 imposed on equilibrium
	int maxit = 5;                    // Max iterations for Newton-Raphson
	double newtonTol = 0.1 * equiTol; // Tolerance for Newton-Raphson iteration
	double maxDevious = 0.5;          // Max allowed deviation of Y from equil value in
								      // numerical step
	boolean displayDetails = false;   // Diagnostic output control
	boolean displayE = false;         // If this & displayDetails false, no equil diagnostics
	boolean showAddRemove = false;    // Whether to write message when RG
									  // added/removed from equil
	boolean outputEquilPlot = false;  // Whether to output equilibrium data for
									  // plotting to output/equil.out
	boolean outputTauPlot = true;     // Whether to output PE equil time for
									  // plotting to output/tau.out
	// --------------------------------------------------------------------------------------

	int masterNits = 0;               // Current number of Newton-Raphson iterations
	int totalEquilIsotopes = 0;       // Total isotopes in partial equilibrium [from
								      // restoreEquilibrium()]
	int countEquilIsotopes = 0;       // Total isotopes in partial equilibrium [from
								      // restoreEquilibriumProg()]
	int countConstraints = 0;         // Number reaction groups in equil (counted in
								      // restoreEquilibriumProg()]
	double fracRGequilibrated = 0;    // Fraction of reaction groups satisfying
									  // equilibration condition
	double XeqInit = 0;               // Sum X of equilibrium isotopes
	double Xneq = 0;                  // Sum X of isotopes not in equilibrium
	double K;
	double ytil12;
	
	// Whether isotope part of partial equil reaction
	boolean[][] isotopeInEquil = new boolean[pmax + 1][nmax]; 
	// isotopeInEquil[][] from last step
	boolean[][] isotopeInEquilLast = new boolean[pmax + 1][nmax]; 

	// Fastest PE timescale at each plot output step, loose criteria
	double fastestPEtimescale; 
	// Fastest PE timescale at each plot output step, stringent criteria
	double fastestPEtimescale2; 
	// Fastest PE timescale at each plot output step, average of loose and stringent
	double fastestPEtimescaleAVG; 
	// Temporary time to start detailed rate/flux output
	double diagnoseLimit = 1e8; 
	
	// Whether plotting rates (true) or Y or X (false). Needed to  deal with formatting 
	// in GraphicsGoodies since added new long and short format plots. This is set to 
	// true while any rates are being plotted and to false otherwise.
	
	static boolean amPlottingRates = false; 
	
	double maxFluxRG = 0;       // Maximum net flux from from reaction group
	int maxFluxRGIndex;         // RGgroup[] index for max net flux maxFluxRG
	double XcorrFac = 1;        // Rescaling factor for sumX in
							    // restoreEquilibriumProg()
	double mostDevious = 0;     // Largest deviation of equilibrium k ratio from equil
	int mostDeviousIndex;       // Index of RG with mostDevious
	double plotDevious;         // mostDevious stored for plotting
	double SFsave;              // Initial value of stochasticFactor

	int countPotentialEquil = 0;
	int nontrivialRG = 0;

	int savedTimeSteps = 0;     // Integration timesteps to last plot output interval

	// ------------------------------------------------------------------------------------
	// Constructor method implementing the explicit evolution of
	// the population starting from some initial distribution
	// ------------------------------------------------------------------------------------

	StochasticElements() {

		// Iconify the main frame when the integration starts
		if (doIntegrate)
			ChooseIsotopes.cd.setState(Frame.ICONIFIED);

		if (activeFileName.compareTo("jin/cno.inp") == 0
				|| activeFileName.compareTo("jin/cnoBi.inp") == 0)
			isCNONetwork = true;
		if (activeFileName.compareTo("jin/approx13.inp") == 0)
			isAlphaNetwork = true;

		// Prevent conflict between choice of steady state or asymptotic approximations
		if (doSS)
			doAsymptotic = false;
		if (!doAsymptotic)
			asyPC = false;

		// Set string giving mode of calculation
		if (!integrateWithJava) {
			calcModeString = "F90Asy";
		} else if (doSS) {
			calcModeString = "QSS(maxIt=" + nit + ")";
		} else if (doAsymptotic) {
			if (asyPC) {
				if (isMott) {
					calcModeString = "AsyMott";
				} else {
					calcModeString = "AsyOB";
				}
			} else {
				calcModeString = "Asy";
			}
		} else {
			calcModeString = "Exp";
		}

		if (imposeEquil)
			calcModeString += "+PE";

		boxThresh = Ymin * totalSeeds; // Determines threshold pop to process
										// box

		// Convert the array Tpf[] containing the values of T9 for the partition
		// function pf[][][] array into log_10 T9 entries since we will do the actual
		// interpolation inlog T9 rather than in T9 directly.

		// for(int i=0; i<24; i++){
		// 		Tpf[i] = LOG10*Math.log(Tpf[i]);
		// }

		// Initialize the partition function array to 1 so that if there is no
		// partition function for an isotope read in its default value will be 1 
		// instead of 0 (which would be problematic since inverse reactions get 
		// multiplied by ratios of partition functions and this ratio will become 
		// infinite if the denominator is zero).

		for (int i = 0; i < pmax + 1; i++) {
			for (int j = 0; j < nmax + 1; j++) {
				for (int k = 0; k < 24; k++) {
					pf[i][j][k] = 1;
				}
			}
		}

		// Determine whether loop in lightIonBurner only goes over triple-alpha
		// or over all alpha-alpha reactions. Normally tripleAlphaOnly is false 
		// but it should be set to true to run an alpha-only network (since allowing 
		// all alpha-alpha reactions will produce isotopes that are not part of a 
		// standard alpha network). NOTE: this option is currently disabled further 
		// below.

		if (tripleAlphaOnly) {
			alphaLow = 39;
		} else {
			alphaLow = 0;
		}

		// Initialize array that tells us whether an isotope has ever been populated
		// in the entire calculation

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				hasBeenPopulated[i][j] = false;
			}
		}

		// Set up to array that will be used by the method rcheck to screen input
		// rates, rejecting those too small to contribute in the temperature
		// range of interest.

		if (limitRates) {
			double delt = (LOG10 * Math.log(T9max * 1e9) - LOG10
					* Math.log(T9min * 1e9)) / 4;
			for (int i = 0; i < 5; i++) {
				T9cases[i] = Math.pow(10, LOG10 * Math.log(T9min * 1e9) + i
						* delt) / 1e9;
			}
		}

		// If compiled integration, check to see that required input directory
		// and executable exists. Exit with error message if they don't.

		if (!integrateWithJava) {

			String argh = "inputs";
			File chk = new File(argh);
			if (chk.exists() && chk.isDirectory()) {
				System.out.println("  --> Directory '" + argh + "' found");
			} else {
				Cvert.callExit("*** No directory '" + argh
						+ "' found in this directory. Terminating.");
			}
			argh = fortranExecutable;
			chk = new File(argh);
			if (chk.exists()) {
				System.out.println("  --> Executable " + argh + " found");
			} else {
				Cvert.callExit("*** No executable '" + argh
						+ "' found in this directory. Terminating.");
			}
		}

		// Rename output file since it will be overwritten shortly.

		File tfc = new File(outputDataFile);
		File tfc2 = new File(outputDataFile + "-save");
		if (tfc2.exists())
			tfc2.delete();
		if (tfc.exists()) {
			tfc.renameTo(tfc2);
		}

		// startThread();

		// Define character output streams to files that will store
		// some details of the calculation (useful for diagnostics). The
		// file that are written are overwritten each time
		// StochasticElements is run, so copy to a new file if you wish to
		// save these detailed diagnostics for a run. The output streams
		// must be wrapped in a try-catch clause to catch exceptions.

		try {

			String tmpS = "output/stochastic.tmp";   // Written by toChar.println();
			String tmpS2 = outputDataFile;           // Master data written by toY.println();
			String tmpS3 = "output/diagnostics.out"; // Written by toDiag.println();
			String tmpS4 = "output/restart.out";     // Written by toRestart.println();
			String tmpS5 = "output/plotfile.out";    // Written by toPlot.println();
			String tmpS6 = "approx13.rc";            // Written by toF90.println();
			String tmpS7 = mapleXoutFile;            // Written by toX.println();
			String tmpS8 = "output/rateStuff.out";   // Written by toRateStuff.println();
			String tmpS9 = "output/fluxQSS.out";     // Written by toFluxQSS.println();
			String tmpS10 = "output/reduced.out";    // Written by toReduced.println();
			String tmpS11 = "output/graphFile.out";  // Utility 1D plots(toGraph)
			// Hydro profile if old file read in for plot
			String tmpS12 = hydroProfileFile; 
			// F90 network runtime input file (toNet)
			String tmpS13 = "inputs/net.inp"; 
			// F90 abundance runtime input (toAbundance)
			String tmpS14 = "inputs/abund.inp"; 
			// Reaction groups file (toGroups)
			String tmpS15 = "output/reactionGroups.out"; 
			// Equilibrium quantities (toEquil)
			String tmpS16 = "output/equil.out";  
			// PE equilibration times alpha net reactions (toTau)
			String tmpS17 = "output/tau.out"; 
			// Ratio equilibrium tau to timestep
			String tmpS18 = "output/tauRatio.out";
			// Rate data output for CUDA calculation
			String tmpS19 = "output/rateLibrary.data";
			// Network data for CUDA calculation
			String tmpS20 = "output/CUDAnet.inp";

			to = new FileOutputStream(tmpS);         // byte output stream
			toChar = new PrintWriter(to);            // character output stream
			to2 = new FileOutputStream(tmpS2);
			toY = new PrintWriter(to2);
			to3 = new FileOutputStream(tmpS3);
			toDiag = new PrintWriter(to3);
			to4 = new FileOutputStream(tmpS4);
			toRestart = new PrintWriter(to4);
			to5 = new FileOutputStream(tmpS5);
			toPlot = new PrintWriter(to5);
			if (!integrateWithJava) {
				to6 = new FileOutputStream(tmpS6);
				toF90 = new PrintWriter(to6);
			}
			to7 = new FileOutputStream(tmpS7);
			toX = new PrintWriter(to7);
			to8 = new FileOutputStream(tmpS8);
			toRateStuff = new PrintWriter(to8);
			to9 = new FileOutputStream(tmpS9);
			toFluxQSS = new PrintWriter(to9);
			to10 = new FileOutputStream(tmpS10);
			toReduced = new PrintWriter(to10);
			to11 = new FileOutputStream(tmpS11);
			toGraph = new PrintWriter(to11);
			to12 = new FileOutputStream(tmpS12);
			toHydroProfile = new PrintWriter(to12);
			if (!integrateWithJava) {
				to13 = new FileOutputStream(tmpS13);
				toNet = new PrintWriter(to13);
				to14 = new FileOutputStream(tmpS14);
				toAbundance = new PrintWriter(to14);
			}
			to15 = new FileOutputStream(tmpS15);
			toGroups = new PrintWriter(to15);
			to16 = new FileOutputStream(tmpS16);
			toEquil = new PrintWriter(to16);
			to17 = new FileOutputStream(tmpS17);
			toTau = new PrintWriter(to17);
			to18 = new FileOutputStream(tmpS18);
			toTauRatio = new PrintWriter(to18);
			to19 = new FileOutputStream(tmpS19);
			toRateData = new PrintWriter(to19);
			to20 = new FileOutputStream(tmpS20);
			toCUDAnet = new PrintWriter(to20);

			// Read the machine hostname for later output
			hostName = HostCommands.getHostname();

			// Note that the file read in by readMassTable() appears to give different 
			// mass excesses than that read in using readpfFile(), so suppress
			// readMassTable() and use mass excesses read in using readpfFile(), 
			// corresponding to the Rauscher tables found in winvn.nosmo. Note that 
			// we only read these in if we are going to do a new integration. If we are 
			// just reading in an old data file to visualize, this info is not needed.

			if (doIntegrate) {
				// Mass excess table (now using masses from pfFile below)
				// readMassTable(); 
				// Read in partition function information & mass excesses
				readpfFile(pfFile); 
			}

			// Initialize amu array
			for (int i = 0; i < pmax + 1; i++) {
				for (int j = 0; j < nmax + 1; j++) {
					// Note: masses[Z][N] contains the mass excesses, not masses
					// AA[i][j] = (double)(i+j) + masses[i][j]/MEV;
					AA[i][j] = (double) (i + j);
				}
			}

			if (doIntegrate) {

				setGlobalVariables();
				loadData();

				if (readReactionGroups)
					inputReactionGroups(reactionGroupFile); // Read in reaction groups
				writeReactionGroups();
				bookkeeping();

				// Read in hydro profile from data file if not constant hydro
				if (!constantHydro) {
					readHydroProfile();
				}

				// Set up for main time integration loop
				if (integrateWithJava) {
					System.out.println("Using Java for integration:");
				} else {
					System.out.println(
						"Calling compiled code for explicit asymptotic integration:"
					);
				}
				computeTime = System.currentTimeMillis(); // compute timer
				tmin = Math.pow(10, logtmin);
				tmax = Math.pow(10, logtmax);
				time = tmin;
				tintNow = 0;

				// Set initial temperature and density etc. from hydro profile
				if (!constantHydro)
					updateHydro();

				// Output initial values of important variables to all streams
				tempstring2 = "Particles=" + totalSeeds + " maxLight="
						+ maxLightIon + " tripleAlphaOnly=" + tripleAlphaOnly
						+ " normX=" + renormalizeMassFractions;
				tempstring3 = "noNeutronsFlag=" + noNeutronsFlag + " Zmax="
						+ pmax + " Zmin=" + pmin + " nT="
						+ gg.decimalPlace(8, nT) + " T9="
						+ gg.decimalPlace(8, T9);
				tempstring3b = "rho=" + gg.decimalPlace(3, rho) + " Ye=" + Ye
						+ " logtmin=" + logtminPlot + " logtmax=" + logtmaxPlot
						+ " diagnoseI=" + diagnoseI;
				tempstring4 = "diagnoseII=" + diagnoseII + " tbeg=" + tbeg
						+ " limitRates=" + limitRates + " rateFloor="
						+ rateFloor;
				if (useCustomTimestepper) {
					tempstring5 = "E/A(init)="
							+ gg.decimalPlace(4, ERelease / nT) + " Mode="
							+ calcModeString + " Custom SF & dX Ymin=" + Ymin;
				} else {
					tempstring5 = "E/A(init)="
							+ gg.decimalPlace(4, ERelease / nT) + " Mode="
							+ calcModeString + " SF=" + stochasticFactor
							+ " MassTol=" + massTol + " Ymin=" + Ymin;
				}

				System.out.println(timeDateString());
				System.out.println("host=" + hostName);
				if (myComment.compareTo("(Optional comment)") != 0
						&& myComment.compareTo("") != 0
						&& myComment.compareTo("Optional") != 0) {
					System.out.println(myComment);
				}
				System.out.println();

				if (showSVNversion) {
					// vSVN = "StochasticElements.java "
					// + GetVersionSVN.getTheVersion("StochasticElements.java",
					// 6)
					// + "  Directory " + GetVersionSVN.getTheVersion(".", 5);
					System.out.println(tempstring1);
					System.out.println(vSVN);
				}

				System.out.println(tempstring1);
				System.out.println("INITIAL VALUES:");
				System.out.println(tempstring2);
				System.out.println(tempstring3);
				System.out.println(tempstring3b);
				System.out.println(tempstring4);
				System.out.println(tempstring5);
				outStrings();
				System.out.println(os4);
				System.out.println(tempstring1);

				toDiag.println();
				toDiag.println(tempstring1);
				toDiag.println(tempstring2);
				toDiag.println(tempstring3);
				toDiag.println(tempstring4);
				toDiag.println(tempstring5);
				toDiag.println(tempstring1);

				toChar.println();
				toChar.println(tempstring1);
				toChar.println(tempstring2);
				toChar.println(tempstring3);
				toChar.println(tempstring4);
				toChar.println(tempstring5);
				toChar.println(tempstring1);

				toY.println();
				toY.println(tempstring1);
				toY.println(tempstring2);
				toY.println(tempstring3);
				toY.println(tempstring4);
				toY.println(tempstring5);
				toY.println(tempstring1);

				if (deltaTime == 0) deltaTime = 0.01 * Math.pow(10, logtmin);

				// So correct dt will be printed 1st step
				timestepStacker[numberStacked - 1] = deltaTime; 
				deltaTimeRestart = deltaTime;

				// startThread();

				// Integrate either with Java or with a compiled program
				
				// Write the CUDA network file
				writeCUDAnetworkV();
				
				// Write the CUDA rates file
				writeCUDAratefile();
				

				if (integrateWithJava) {      // Java integrator

					stochasticIntegrator();

					computeTime = System.currentTimeMillis() - computeTime;

					// Output minimum and maximum rates at each timestep and
					// related info
					// to stochastic.tmp. This can be plotted with
					// utilityPlotter.mw.
					/*
					 * toChar.println();toChar.println(
					 * "Paste following 7 columns of numbers (omit headers) into a file named"
					 * );toChar.println(
					 * "rateStuff.out, which the Maple program maxRatePlotter.mw can read and plot."
					 * );toChar.println(
					 * "This same information is written directly to the file rateStuff.out also."
					 * );toChar.println(
					 * "(The Maple program maxRate2Plotter compares two such files.)"
					 * ); toChar.println();
					 * toChar.println("Time    maxRate     1/maxRate  "
					 * +"minRate    1/minRate    dt  dt/maxExplicit");
					 */

					for (int i = 0; i < numdt; i++) {

						/*
						 * toChar.println(gg.decimalPlace(4,timeIntervals[i])
						 * +"  "+gg.decimalPlace(4,fastestRate[i])
						 * +"  "+gg.decimalPlace(4,1/fastestRate[i]) + "  " +
						 * gg.decimalPlace(4,slowestRate[i]) + "  " +
						 * gg.decimalPlace(4,1/slowestRate[i]) + "  " +
						 * gg.decimalPlace(4,tstep[i]) + "  " +
						 * gg.decimalPlace(4,tstep[i]*fastestRate[i]));
						 */

						// And also write it directly to rateStuff.out, which can be
						// read with maxRatePlotter.mw. The program max2RatePlotter.mw 
						// can compare two such files, named rateStuff.out
						// and rateStuff2.out. To compare run this program to create 
						// rateStuff.out, rename to rateStuff2.out, then run this 
						// program again to create new rateStuff.out

						toRateStuff.println(
							gg.decimalPlace(4, timeIntervals[i])
							+ "  "
							+ gg.decimalPlace(4, fastestRate[i])
							+ "  "
							+ gg.decimalPlace(4, 1/fastestRate[i])
							+ "  "
							+ gg.decimalPlace(4, slowestRate[i])
							+ "  "
							+ gg.decimalPlace(4, 1/slowestRate[i])
							+ "  "
							+ gg.decimalPlace(4, tstep[i])
							+ "  "
							+ gg.decimalPlace(4, tstep[i]*fastestRate[i])
						);
					}

					// If saveRestartFiles is false (meaning that a restart file
					// was not written at each plot output step), output a final 
					// restart file. If saveRestartFiles is true, a final restart 
					// file has already been output at the final timestep.

					if (!saveRestartFiles)
						writeRestart(); // Write restart file

					// Diagnostic: output # of reactions per timestep array to disk
					// saveReacNum();

					// Store total boxes populated for use by plotter in AbPlotFrame
					boxPopuli = totalBoxesPopulated();

					// Generate output strings
					outStrings();
					
					// Generate output file for reduced network
					writeReducedNetwork();

					writeHydroProfile();

					// The calculation is now complete. Following statements output
					// files for later plotting of results and set up for graphical 
					// display of the results.

					savePop();
					writeToMaple();
					displayPlots();
					
				} else {               // Compiled integrator
					
					compiledIntegrator();
					initializeDataArrays();
					outStrings();
					readOldAbundances("output/Y.inp");
					displayPlots();
					writeToMaple();
					writeHydroProfile();
				}

			} else {                   // If reading in old file to plot
				initializeDataArrays();
				outStrings();
				readOldAbundances(oldYfile);
				displayPlots();
				writeToMaple();
				writeHydroProfile();
			}

		} catch (IOException e) {
			;
		}

		// Flush and close the i/o streams

		// No exceptions to be caught for following because methods
		// of PrintWriter never throw exceptions. Must flush before
		// streams are closed to ensure that all data get output.

		toChar.flush();
		toChar.close();
		toY.flush();
		toY.close();
		toDiag.flush();
		toDiag.close();
		toRestart.flush();
		toRestart.close();
		toPlot.flush();
		toPlot.close();
		if (!integrateWithJava) {
			toF90.flush();
			toF90.close();
		}
		toX.flush();
		toX.close();
		toRateStuff.flush();
		toRateStuff.close();
		toFluxQSS.flush();
		toFluxQSS.close();
		toReduced.flush();
		toReduced.close();
		toGraph.flush();
		toGraph.close();
		toHydroProfile.flush();
		toHydroProfile.close();
		if (!integrateWithJava) {
			toNet.flush();
			toNet.close();
			toAbundance.flush();
			toAbundance.close();
		}
		toGroups.flush();
		toGroups.close();
		toEquil.flush();
		toEquil.close();
		toTau.flush();
		toTau.close();
		toTauRatio.flush();
		toTauRatio.close();
		toRateData.flush();
		toRateData.close();
		toCUDAnet.flush();
		toCUDAnet.close();

		// But exceptions must be caught for the following

		try {
			to.close();
			to2.close();
			to3.close();
			to4.close();
			to5.close();
			if (!integrateWithJava) {
				to6.close();
			}
			to7.close();
			to8.close();
			to9.close();
			to10.close();
			to11.close();
			to12.close();
			if (!integrateWithJava) {
				to13.close();
				to14.close();
			}
			to15.close();
			to16.close();
			to17.close();
			to18.close();
			to19.close();
			to20.close();
		} catch (IOException e) {
			;
		}

		// Print final summaries and reprint input quantities if an actual
		// integration was performed (doIntegrate = true). Omit this output if we 
		// are replotting an old data set.

		if (doIntegrate) {

			// Print elapsed compute time and grid spilloff
			System.out.println();
			System.out.println(timeDateString());
			System.out.println("host=" + hostName);
			if (myComment.compareTo("(Optional_comment)") != 0
					&& myComment.compareTo("") != 0
					&& myComment.compareTo("Optional") != 0) {
				System.out.println(myComment);
			}
			if (showSVNversion)
				System.out.println(vSVN);

			System.out.println();
			System.out.println("Time: "
					+ (double) computeTime
					/ 1000
					+ "s"
					+ "  RawSteps: "
					+ totalTimeSteps
					+ "  Steps: "
					+ (totalTimeSteps - totalTimeStepsZero)
					+ "  Steps/s: "
					+ gg.decimalPlace(2, 1000 * (double) totalTimeSteps
							/ (double) computeTime));
			System.out.println("Possible Isotopes: " + numberActiveIsotopes
					+ "  Possible Couplings: " + numberCouplings
					+ " Total Boxes Pop=" + totalBoxesPopulated());
			System.out.println("Max isotopes used: " + maxSeedsUsed
					+ "  Max couplings used: " + maxCouplings
					+ "  Isotopes processed: " + numberIsotopesPopulated
					+ "\nsumX=" + gg.decimalPlace(8, sumX) + " Xcorr="
					+ gg.decimalPlace(4, totalCorrectionSumX) + " E/A(final)="
					+ gg.decimalPlace(4, ERelease / nT));

			System.out.println();

			// Print initial values again

			System.out.println(tempstring1);
			System.out.println("PRECEDING CALCULATED WITH INITIAL VALUES:");
			System.out.println(tempstring2);
			System.out.println(tempstring3);
			System.out.println(tempstring3b);
			System.out.println(tempstring4);
			System.out.println(tempstring5);
			System.out.println(os4);
			System.out.println(tempstring1);
			System.out.println();

			// Close the progress meter window and stop thread

			if (SegreFrame.prom != null)
				SegreFrame.prom.makeQuit();
			runThread = false;

			// writeReactionGroups();

		}

	} /* end StochasticElements constructor */
	

	// ----------------------------------------------------------------------------------------------
	// Method to implement the stochastic integration loop using Java
	// ----------------------------------------------------------------------------------------------

	public void stochasticIntegrator() {

		// Get SVN revision numbers and construct string for output documentation
		try {
			vSVN = "" //"StochasticElements.java "
					+ GetVersionSVN.getTheVersion("StochasticElements.java", 6)
			// + "  Directory " + GetVersionSVN.getTheVersion(".", 5)
			;
		} catch (IOException e) {
			vSVN = "Could not get StochasticElements.java SVN version";
		}

		SFsave = stochasticFactor;

		// int tindexerFlip = 10000; //300;
		// int tindexer = tindexerFlip - 10;

		// Initialize the progress meter

		SegreFrame.prom.sets1("Computing ...");
		SegreFrame.prom.sets2("Plot Intervals: " + String.valueOf(tintNow - 1)
				+ "/" + String.valueOf(numdt - 1));

		// Compute total nucleons in initial network. This was calculated earlier in
		// setAbundances but that code is left over from original stochastic
		// formulation and nT may not be computed correctly there if there are
		// more than 1H and 4He light-ion (Z<3) species in the network.

		nT = f = countNeutrons() + countProtons();

		// Compute total mass of initial network
		lastMass = returnNetworkMass();

		deltaTimeRestart = deltaTime;

		// Compute bump parameters for initial value of massTol
		bumperSetter(massTol);

		// Precompute logs and powers of T9 in ReactionClass1
		// before rate calculations. Compute partition functions at initial T

		prepReactionClass1(T9);
		pfUpdate(T9);
		// pfUpdate(LOG10*Math.log(T9));

		// Initialize reacIsActive array for reactions to true
		for (int i = 0; i < pmax; i++) {
			for (int j = 0; j < nmax; j++) {
				for (int k = 0; k < maxRates; k++) {
					reacIsActive[i][j][k] = true;
				}
			}
		}

		promTimeTarget = 0;

		// This while loop advances the network integration by one timestep
		while (tintNow < numdt) {

			// Reset fastest rate at each step to prevent a reaction from a previous
			// timestep that was fast but is now in equilibrium from being output as 
			// the fastest rate for this step (since in partial equilibrium approx
			// that reaction has been removed from the numerical integration).

			fastestCurrentRate = 1e-30;
			maxRateString = "";

			dERelease = 0;           // Initialize; will be updated from ReactionClass1
			couplingCounter = 0;
			totalTimeSteps++;

			if (imposeEquil && time > equilibrateTime
					&& totalEquilReactions > 0 && displayE) {
				System.out
						.println("\n-----------------------------------------------------------------------------------------------------------------------------");
				System.out.println(" Begin timestep " + totalTimeSteps
						+ " t_0=" + time + " Last dt=" + deltaTimeRestart);
				System.out
						.println("-----------------------------------------------------------------------------------------------------------------------------\n");
			}

			totalEquilReactions = 0;
			totalEquilIsotopes = 0;

			String set1;
			String set2;

			// Update sceen progress meter at constant real time intervals of
			// promUpdateMillis ms

			if (System.currentTimeMillis() > promTimeTarget) {
				promTimeTarget = System.currentTimeMillis() + promUpdateMillis;
				countPE();
				set1 = "Plot: " + String.valueOf(tintNow) + "/"
						+ String.valueOf(numdt - 1) + "  t="
						+ gg.decimalPlace(4, time) + "  dt="
						+ gg.decimalPlace(3, deltaTimeRestart) + "  Steps: "
						+ totalTimeSteps;
				set2 = "T9=" + gg.decimalPlace(3, T9) + "  Asy: "
						+ String.valueOf(numberAsymptoticLast) + "/"
						+ String.valueOf(numberSeeds) + "  PE: "
						+ countPotentialEquil + "/" + nontrivialRG + "  sumX="
						+ gg.decimalPlace(3, sumX);
				SegreFrame.prom.sets1(set1);
				SegreFrame.prom.sets2(set2);
			}

			// // Update the screen progress meter after tindexerFlip
			// integration steps
			// tindexer ++;
			// if(tindexer > tindexerFlip){
			// countPE();
			// set1 = "Plot: "+String.valueOf(tintNow) + "/"
			// + String.valueOf(numdt-1) +"  t="+gg.decimalPlace(4,time)
			// +"  dt="+gg.decimalPlace(3,deltaTimeRestart)+"  Steps: "+totalTimeSteps;
			// set2 = "T9="+gg.decimalPlace(3,T9)+"  Asy: "
			// +String.valueOf(numberAsymptoticLast)
			// + "/" + String.valueOf(numberSeeds)
			// +"  PE: "+countPotentialEquil+"/"+nontrivialRG
			// + "  sumX="+gg.decimalPlace(3,sumX);
			// SegreFrame.prom.sets1(set1);
			// SegreFrame.prom.sets2(set2);
			// tindexer = 0;
			// }

			// Update temperature and density etc. from hydro profile, and
			// partition functions
			// at the new temperature

			if (!constantHydro) {
				updateHydro();
				pfUpdate(Math.pow(10, logT) / 1e9);
				// pfUpdate(logT-9); // logT in K but pfUpdate expects arg to be T9
			}

			// Sweep isotope grid and inventory seed isotopes with non-zero populations
			pruneSeeds();

			// Invoke lightIonBurner to handle separately reactions of light ions with
			// themselves (for example, triple-alpha).

			lightIonBurner();

			// Compute all the rates for all reactions for active isotopes. Then
			// update all fluxes based on these computed rates and the current 
			// population.

			updateHeavyRates();
			updateHeavyFluxes();

			// Method advanceOneTimestep() will now advance the integration by one
			// numerical timestep. It updates abundances and returns the timestep 
			// chosen, which is used to update to the current time.

			deltaTime = advanceOneTimestep();

			time += deltaTime;              // Update time for step just taken

			// Now that the numerical timestep has been taken, we must consider
			// whether any reactions during the timestep were in PE. If they were 
			// their fluxes were removed from the numerical integration (the PE 
			// approximation), but now at the end of the numerical timestep we must 
			// restore them to PE since the numerical timestep will generally 
			// disturb PE because of coupling to reactions not satisfying the 
			// equilibrium conditions. We must also determine which reactions satisfy 
			// the partial equilibrium conditions for the next timestep, since this 
			// could have changed in this timestep.

			// First output some diagnostics if diagnostic flags are set
			if (imposeEquil && time > equilibrateTime) {
				if (totalEquilReactions > 0) {
					if (displayE) {
						System.out.println(
							"\nEnd numerical timestep: dt chosen="
							+ gg.decimalPlace(6, deltaTime)
							+ " time_new="
							+ gg.decimalPlace(6, time) + " sumX="
							+ gg.decimalPlace(4, sumX)
						);
						System.out.println(
						 "\nChange of abundances in equilibrated reactions for this numerical timestep:"
						);
						System.out.println(
							"Z  N     Asymp       Yzero[Z][N]      Yend[Z][N]"
							+ "       Yzero-Yend"
						);
					}

					for (int i = minNetZ; i <= maxNetZ; i++) {
						int indy = Math.min(maxNetN[i], nmax - 1);
						for (int j = minNetN[i]; j <= indy; j++) {
							if (isInNet(i, j)) {
								// Only output if isotope in equilibrated RG
								if (isotopeInEquilLast[i][j] && displayE) { 
									System.out.println(i
										+ "  "
										+ j
										+ "      "
										+ isAsymptotic[i][j]
										+ "      "
										+ gg.decimalPlace(6, Yzero[i][j])
										+ "      "
										+ gg.decimalPlace(6, Y[i][j])
										+ "       "
										+ gg.decimalPlace(6,
												(Yzero[i][j] - Y[i][j]))
									);
								}
							}
						}
					}
				}
				if (totalEquilReactions > 0 && displayE) {
					System.out.println();
					String tsss = // totalTimeSteps+" t="+gg.decimalPlace(7,time)+
					"Equilibrated reactions = " + totalEquilReactions;
					if (totalEquilReactions > 0)
						tsss += ("  " + returnEquilibratedReactionString());
					System.out.println(tsss);
				}
			}

			// Compute equilibrium quantities. If equilibrate is true, equilibrium
			// quantities are calculated and displayed but only if imposeEquil is 
			// true is partial equilibrium actually imposed.

			if (equilibrate && time > equilibrateTime) {
				// System.out.println("\nCompute Partial Equilibrium Quantities (timestep "
				// +totalTimeSteps+" time="+gg.decimalPlace(6,time)+"):");

				for (int i = 0; i < numberReactionGroups; i++) {
					// Determine, based on this timestep, which reactions are in
					// equilibrium
					RGgroup[i].computeEquilibrium();
					// String checks =
					// " R0="+gg.decimalPlace(4,RGgroup[i].eqcheck[0]);
					// for(int j=1; j<RGgroup[i].niso; j++){
					// checks +=
					// (" R"+j+"="+gg.decimalPlace(4,RGgroup[i].eqcheck[j]));
					// }

					// System.out.println("  RG="+i+" "+RGgroup[i].reactions[RGgroup[i].refreac].reacString
					// +" "+
					// checks+" lam="+gg.decimalPlace(4,RGgroup[i].lambda)
					// +" lamEq="+gg.decimalPlace(4,RGgroup[i].lambdaEq)+" "+RGgroup[i].isEquil);
				}
			}

			// Call method to re-establish disturbed equilibrium after numerical
			// timestep
			if (imposeEquil && time > equilibrateTime) {
				for (int i = 0; i < numberReactionGroups; i++) {
					RGgroup[i].computeEquilibrium();
				}

				if (totalEquilReactions > 0 && time < 1e20) {
					// Choose one of the following
					// restoreEquilibrium(); // Does Newton-Raphson with matrix
					// solve
					restoreEquilibriumProg(); // Sets prog variables to equil
												// and uses no matrix solve or
												// iteration
				} else if (time < 50) {
					// Ythresh = 1e-12;
				} else {
					// Ythresh = 1e-20;
				}
				// fixEquilibrium2(); // This one is specific to the 4-isotope
				// alpha test case.
			}

			// System.out.println("  Ythresh="+Ythresh);

			// Store most recent timestep in stackdt array, which is averaged to
			// get average
			// recent timestep for plotting purposes. deltaTime is the actual
			// timestep just
			// taken but store instead deltaTimeRestart, which is the timestep
			// that would have
			// been taken if not at a plot output step (deltaTime and
			// deltaTimeRestart are equal
			// unless at a plot output step, in which case deltaTime <=
			// deltaTimeRestart).

			if (!(time >= timeIntervals[tintNow] && !outdone)) {
				stackdt(deltaTimeRestart);
			}

			// Add energy increment for this timestep from Q values to total
			// energy

			ERelease += dERelease * deltaTime;

			// Update counter for max couplings actually used in a timestep

			// NOTE: there is an error somewhere since maxCouplings is sometimes
			// larger than the
			// possible couplings (numberCouplings) if QSS is invoked.

			maxCouplings = Math.max(maxCouplings, couplingCounter);

			// If at graphics output interval, output graphics and stuff

			if (time >= timeIntervals[tintNow] && !outdone) {

				storePop();

				// Output rates for alpha network for plotting

				double tlc = LOG10 * Math.log(time);
				if (plotAlphaRates && tlc > logtminPlot && tlc < logtmaxPlot)
					utilityGrapher();

				// Reaction counting

				reacTot[tintNow] = 0;
				for (int mm = 0; mm < 20; mm++) {
					reacNum[mm][tintNow] = tempReacNum[mm];
					reacTot[tintNow] += mm * tempReacNum[mm];
				}
				activeIsotopes[tintNow] = numberSeeds;
				reactionsPerIsotope[tintNow] = (double) reacTot[tintNow]
						/ (double) activeIsotopes[tintNow];

				// Compute fastest equilibrium timescale in network
				if (equilibrate)
					findFastestPEtimescale();

				tintNow++;
				if (tintNow >= numdt)
					outdone = true;

				fastestCurrentRate = 1e-30;
				slowestCurrentRate = 1e30;

				// Update screen progress meter

				// SegreFrame.prom.sets2("Step: "+String.valueOf(tintNow) + "/"
				// + String.valueOf(numdt-1)
				// +"   Iso: "+String.valueOf(numberAsymptoticLast)
				// + "/" + String.valueOf(numberSeeds) +
				// "T9:"+gg.decimalPlace(2,T9));

				// Set timestep back to what it would have been if it had not
				// been shortened
				// to match the plot output step. This makes integration more
				// stable because
				// otherwise the timestep after this plot output step will start
				// at a randomly smaller
				// value than it would have been had a plot output step not
				// intervened.

				deltaTime = deltaTimeRestart;

				// Display equilibrium quantities

				if (equilibrate || imposeEquil) {
					showEquilibriumStuff();
					dumpEquilibriumData();
				}

				// Output flux information

				// writeReactionFluxesZN();
				writeReactionFluxesZNall();
				writeReactionFluxesRG();

			} // End output at graphics step

			// Initialize reaction counting temp array

			for (int mm = 0; mm < 20; mm++) {
				tempReacNum[mm] = 0;
			}

			// Zero differential population counters for next timestep. Also
			// store current
			// values in previous values for asymptotic formulas, and update the
			// maxpop
			// array for each box that will be used to generate a reduced
			// network file
			// at the end of the calculation. Also store current values of
			// Y[Z][N] in
			// Yzero[Z][N] for initial values of Y needed in partial
			// equilibrium.

			for (int i = minNetZ; i <= maxNetZ; i++) {
				int indy = Math.min(maxNetN[i], nmax - 1);
				for (int j = minNetN[i]; j <= indy; j++) {
					dpop[i][j] = 0;
					dpopPlus[i][j] = 0;
					dpopMinus[i][j] = 0;
					if (keff[i][j] > 0) {
						FratPrev[i][j] = Fplus[i][j] / keff[i][j];
					}
					if (pop[i][j] > maxpop[i][j])
						maxpop[i][j] = pop[i][j];
					if (pop[i][j] != 0)
						hasBeenPopulated[i][j] = true;
					Yzero[i][j] = Y[i][j];

					// Temporary diagnostic but note that following if() should
					// probably be applied to this entire
					// loop since e.g. for alpha network this is processing
					// odd-Z that aren't in the network.

					// if(IsotopePad.isoColor[i][j])
					// System.out.println("t="+time+" Y0["+i+"]["+j+"]="
					// +gg.decimalPlace(4,Yzero[i][j]));

					// Also set array keeping track of isotopes participating in
					// partial equil to false

					isotopeInEquilLast[i][j] = isotopeInEquil[i][j]; // Keep old
																		// value
					isotopeInEquil[i][j] = false;

				}
			}

		} /* End while-loop over timesteps */

	} /* End method stochasticIntegrator */

	// -------------------------------------------------------------------------------------------------------------
	// Method to use a compiled code to implement the integration. See the
	// class HostCommand for details
	// -------------------------------------------------------------------------------------------------------------

	public void compiledIntegrator() {

		// Write the input data files the F90 code expects to read

		writeCompiledInput();
		writeCompiledNetwork();
		writeCompiledAbundance();

		// Execute the compiled code and process its output

		try {
			HostCommands.processCompiledOutput("./" + fortranExecutable, true);
		} catch (IOException e) {
			;
		}

	}

	// ----------------------------------------------------------------------------------------------------------------------
	// The light-ion burner handles separately reactions of light ions with
	// themselves, for example, triple-alpha. In the stochastic algorithm it is
	// natural to assume most reactions occur because of a bath of protons,
	// helium-4, and neutrons interacting with the heavier seed nuclei. So the
	// primary bookkeeping tracks the heavy seed population, adjusting the
	// hydrogen, helium, and neutron population appropriately for each heavy
	// seed
	// reaction. But this does not account for reactions like He4 + He4 + He4
	// --> C12,
	// which has no heavy seeds on its left side. The method lightIonBurner()
	// handles
	// these reactions. Only He done so far. Do others later.
	// -----------------------------------------------------------------------------------------------------------------------

	public void lightIonBurner() {

		// Compute the rates that will be needed for light-ion reactions, then
		// use these
		// rates and current populations to compute the corresponding fluxes.

		updateLightIonRates();
		updateLightIonFluxes();

	}

	// --------------------------------------------------------------------------------------------------------------
	// Method to compute the light ion rates needed in LightIonBurner.
	// --------------------------------------------------------------------------------------------------------------

	public void updateLightIonRates() {

		// He4 light-ion on light-ion:

		// Following (restriction to triple-alpha) is currently disabled:

		// For all He light-ion, following sum is from 0 to <41, but making it
		// from
		// i=39; i<41 restricts it to triple-alpha. Value of alphaLow (0 or 39)
		// is
		// determined by the boolean tripleAlphaOnly.
		
		totalReactions = 0;
		totalEquilReactions = 0;

		alphaLow = 0;

		for (int i = alphaLow; i < numberReactions[2][2]; i++) {
			totalReactions++;
			masterRates[2][2][i] = RObject[2][2][i].returnk(T9, rho, Ye);
			alphaRates[i] = masterRates[2][2][i];

			// Multiply by Y factors for 2-body and 3-body reactions. This would
			// have already been
			// done if alphaRates had been computed directly from prob() as
			// before.

			if (RObject[2][2][i].reacIndex > 3) {
				alphaRates[i] *= Y[RObject[2][2][i].isoIn[0].x][RObject[2][2][i].isoIn[0].y];
			}
			if (RObject[2][2][i].reacIndex == 8) {
				alphaRates[i] *= Y[RObject[2][2][i].isoIn[1].x][RObject[2][2][i].isoIn[1].y];
			}
			// alphaRates[i] = RObject[2][2][i].prob(T9,rho,Ye); // Old

			// Store rate in Reaction object
			RGgroup[RGC[2][2][i]].reactions[RGCmember[2][2][i]].rate = alphaRates[i];
		}

		// He3 light-ion on light-ion:

		for (int i = 0; i < numberReactions[2][1]; i++) {
			totalReactions++;
			masterRates[2][1][i] = RObject[2][1][i].returnk(T9, rho, Ye);
			he3Rates[i] = masterRates[2][1][i];

			// Multiply by Y factors for 2-body and 3-body reactions. This would
			// have already been
			// done if he3Rates had been computed directly from prob() as
			// before.

			if (RObject[2][1][i].reacIndex > 3) {
				he3Rates[i] *= Y[RObject[2][1][i].isoIn[0].x][RObject[2][1][i].isoIn[0].y];
			}
			if (RObject[2][1][i].reacIndex == 8) {
				he3Rates[i] *= Y[RObject[2][1][i].isoIn[1].x][RObject[2][1][i].isoIn[1].y];
			}
		}

		// H3 (triton) light-ion on light-ion:

		for (int i = 0; i < numberReactions[1][2]; i++) {
			totalReactions++;
			masterRates[1][2][i] = RObject[1][2][i].returnk(T9, rho, Ye);
			h3Rates[i] = masterRates[1][2][i];

			// Multiply by Y factors for 2-body and 3-body reactions. This would
			// have already been
			// done if h3Rates had been computed directly from prob() as before.

			if (RObject[1][2][i].reacIndex > 3) {
				h3Rates[i] *= Y[RObject[1][2][i].isoIn[0].x][RObject[1][2][i].isoIn[0].y];
			}
			if (RObject[1][2][i].reacIndex == 8) {
				h3Rates[i] *= Y[RObject[1][2][i].isoIn[1].x][RObject[1][2][i].isoIn[1].y];
			}
		}

		// H2 (deuteron) light-ion on light-ion:

		for (int i = 0; i < numberReactions[1][1]; i++) {
			totalReactions++;
			masterRates[1][1][i] = RObject[1][1][i].returnk(T9, rho, Ye);
			h2Rates[i] = masterRates[1][1][i];

			// Multiply by Y factors for 2-body and 3-body reactions. This would
			// have already been
			// done if h2Rates had been computed directly from prob() as before.

			if (RObject[1][1][i].reacIndex > 3) {
				h2Rates[i] *= Y[RObject[1][1][i].isoIn[0].x][RObject[1][1][i].isoIn[0].y];
			}
			if (RObject[1][1][i].reacIndex == 8) {
				h2Rates[i] *= Y[RObject[1][1][i].isoIn[1].x][RObject[1][1][i].isoIn[1].y];
			}
		}

		// H1 (proton) light-ion on light-ion:

		for (int i = 0; i < numberReactions[1][0]; i++) {
			totalReactions++;
			masterRates[1][0][i] = RObject[1][0][i].returnk(T9, rho, Ye);
			h1Rates[i] = masterRates[1][0][i];

			// Multiply by Y factors for 2-body and 3-body reactions. This would
			// have already been
			// done if h1Rates had been computed directly from prob() as before.

			if (RObject[1][0][i].reacIndex > 3) {
				h1Rates[i] *= Y[RObject[1][0][i].isoIn[0].x][RObject[1][0][i].isoIn[0].y];
			}
			if (RObject[1][0][i].reacIndex == 8) {
				h1Rates[i] *= Y[RObject[1][0][i].isoIn[1].x][RObject[1][0][i].isoIn[1].y];
			}
		}

		// Neutron light-ion on light-ion:

		for (int i = 0; i < numberReactions[0][1]; i++) {
			totalReactions++;
			masterRates[0][1][i] = RObject[0][1][i].returnk(T9, rho, Ye);
			n0Rates[i] = masterRates[0][1][i];

			// Multiply by Y factors for 2-body and 3-body reactions. This would
			// have already been
			// done if n0Rates had been computed directly from prob() as before.

			if (RObject[0][1][i].reacIndex > 3) {
				n0Rates[i] *= Y[RObject[0][1][i].isoIn[0].x][RObject[0][1][i].isoIn[0].y];
			}
			if (RObject[0][1][i].reacIndex == 8) {
				n0Rates[i] *= Y[RObject[0][1][i].isoIn[1].x][RObject[0][1][i].isoIn[1].y];
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// Method to compute the light ion fluxes needed in LightIonBurner.
	// --------------------------------------------------------------------------------------------------------------

	public void updateLightIonFluxes() {

		int kk;
		double[] Lflux = new double[50];
		double[] Lpopout = new double[50];

		//totalReactions = 0;
		//totalEquilReactions = 0;

		// 4He light-ion reactions
		// -----------------------------------------------------------------------------------------------------------------------

		// Note: Z=N=2 following necessary when the newZNQ method of
		// ReactionClass1
		// is invoked below because the ReactionClass1 object RObject[][][] uses
		// static
		// StochasticElements.Z and StochasticElements.N in its newZNQ method.

		Z = 2;
		N = 2;
		kk = 0;

		// Following (restriction to triple-alpha) is currently disabled:

		// Fluxes. For all He light-ion, following sum is from 0 to <41, but
		// making it from
		// i=39; i<41 restricts it to triple-alpha. Value of alphaLow (0 or 39)
		// is
		// determined by the boolean tripleAlphaOnly.

		for (int i = 0; i < numberReactions[2][2]; i++) {
			// totalReactions ++;

			// Remove flux from sum if reaction is in equilibrium.

			// NOTE: following if () added because otherwise it breaks the
			// imposeEquil = false case
			// Ensure reacIsActive and isEquil are anticorrelated
			if (imposeEquil)
				reacIsActive[2][2][i] = !RGgroup[RGC[2][2][i]].isEquil;

			if (reacIsActive[2][2][i]) { // If reaction is not in equilibrium
				Lflux[i] = alphaRates[i] * pop[2][2];
				// Store flux in Reaction object
				RGgroup[RGC[2][2][i]].reactions[RGCmember[2][2][i]].flux = Lflux[i];
				if (Lflux[i] > fluxFloor) {
					Lpopout[kk] = Lflux[i]; // This is flux now, not pop change
					tempIndex[kk] = i;
					kk++;
				}
				fastSlowRates(i, 2, 2, alphaRates[i]);
			} else { // If reaction is in equilibrium
				totalEquilReactions++;
				// Store flux in Reaction object but don't add to flux for this
				// timestep
				RGgroup[RGC[2][2][i]].reactions[RGCmember[2][2][i]].flux = 0;
			}
		}

		// Update all populations corresponding to these reactions
		for (int i = 0; i < kk; i++) {
			try {
				if (Lpopout[i] > 0) {
					RObject[2][2][tempIndex[i]].newZNQ(Lpopout[i]);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				gridSpillOff += Lpopout[i];
			}
		}

		// 3He light-ion reactions
		// -------------------------------------------------------------------------------------------------------------------------

		Z = 2;
		N = 1;
		kk = 0;

		for (int i = 0; i < numberReactions[2][1]; i++) {
			// totalReactions ++;

			// Remove flux from sum if reaction is in equilibrium.

			// NOTE: following if () added because otherwise it breaks the
			// imposeEquil = false case
			// Ensure reacIsActive and isEquil are anticorrelated
			if (imposeEquil)
				reacIsActive[2][1][i] = !RGgroup[RGC[2][1][i]].isEquil;

			if (reacIsActive[2][1][i]) { // If reaction is not in equilibrium
				Lflux[i] = he3Rates[i] * pop[2][1];
				// Store flux in Reaction object
				RGgroup[RGC[2][1][i]].reactions[RGCmember[2][1][i]].flux = Lflux[i];
				if (Lflux[i] > fluxFloor) {
					Lpopout[kk] = Lflux[i]; // This is flux now, not pop change
					tempIndex[kk] = i;
					kk++;
				}
				fastSlowRates(i, 2, 1, he3Rates[i]);
			} else { // If reaction is in equilibrium
				totalEquilReactions++;
				// Store flux in Reaction object but don't add to flux for this
				// timestep
				RGgroup[RGC[2][1][i]].reactions[RGCmember[2][1][i]].flux = 0;
			}
		}
		// Update all populations corresponding to these reactions
		for (int i = 0; i < kk; i++) {
			try {
				if (Lpopout[i] > 0) {
					RObject[2][1][tempIndex[i]].newZNQ(Lpopout[i]);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				gridSpillOff += Lpopout[i];
			}
		}

		// 3H (triton) light-ion reactions
		// ------------------------------------------------------------------------------------------------------------------

		Z = 1;
		N = 2;
		kk = 0;

		for (int i = 0; i < numberReactions[1][2]; i++) {
			// totalReactions ++;

			// Remove flux from sum if reaction is in equilibrium.

			// NOTE: following if () added because otherwise it breaks the
			// imposeEquil = false case
			// Ensure reacIsActive and isEquil are anticorrelated
			if (imposeEquil)
				reacIsActive[1][2][i] = !RGgroup[RGC[1][2][i]].isEquil;

			if (reacIsActive[1][2][i]) { // If reaction is not in equilibrium
				Lflux[i] = h3Rates[i] * pop[1][2];
				// Store flux in Reaction object
				RGgroup[RGC[1][2][i]].reactions[RGCmember[1][2][i]].flux = Lflux[i];
				if (Lflux[i] > fluxFloor) {
					Lpopout[kk] = Lflux[i]; // This is flux now, not pop change
					tempIndex[kk] = i;
					kk++;
				}
				fastSlowRates(i, 1, 2, h3Rates[i]);
			} else { // If reaction is in equilibrium
				totalEquilReactions++;
				// Store flux in Reaction object but don't add to flux for this
				// timestep
				RGgroup[RGC[1][2][i]].reactions[RGCmember[1][2][i]].flux = 0;
			}
		}
		// Update all populations corresponding to these reactions
		for (int i = 0; i < kk; i++) {
			try {
				if (Lpopout[i] > 0) {
					RObject[1][2][tempIndex[i]].newZNQ(Lpopout[i]);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				gridSpillOff += Lpopout[i];
			}
		}

		// 2H (deuteron) light-ion reactions
		// ------------------------------------------------------------------------------------------------------------

		Z = 1;
		N = 1;
		kk = 0;

		for (int i = 0; i < numberReactions[1][1]; i++) {
			// totalReactions ++;

			// Remove flux from sum if reaction is in equilibrium.

			// NOTE: following if () added because otherwise it breaks the
			// imposeEquil = false case
			// Ensure reacIsActive and isEquil are anticorrelated
			if (imposeEquil)
				reacIsActive[1][1][i] = !RGgroup[RGC[1][1][i]].isEquil;

			if (reacIsActive[1][1][i]) { // If reaction is not in equilibrium
				Lflux[i] = h2Rates[i] * pop[1][1];
				// Store flux in Reaction object
				RGgroup[RGC[1][1][i]].reactions[RGCmember[1][1][i]].flux = Lflux[i];
				if (Lflux[i] > fluxFloor) {
					Lpopout[kk] = Lflux[i]; // This is flux now, not pop change
					tempIndex[kk] = i;
					kk++;
				}
				fastSlowRates(i, 1, 1, h2Rates[i]);
			} else { // If reaction is in equilibrium
				totalEquilReactions++;
				// Store flux in Reaction object but don't add to flux for this
				// timestep
				RGgroup[RGC[1][1][i]].reactions[RGCmember[1][1][i]].flux = 0;
			}
		}
		// Update all populations corresponding to these reactions
		for (int i = 0; i < kk; i++) {
			try {
				if (Lpopout[i] > 0) {
					RObject[1][1][tempIndex[i]].newZNQ(Lpopout[i]);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				gridSpillOff += Lpopout[i];
			}
		}

		// 1H (proton) light-ion reactions
		// ---------------------------------------------------------------------------------------------------------------

		Z = 1;
		N = 0;
		kk = 0;

		for (int i = 0; i < numberReactions[1][0]; i++) {
			// totalReactions ++;

			// Remove flux from sum if reaction is in equilibrium.

			// NOTE: following if () added because otherwise it breaks the
			// imposeEquil = false case
			// Ensure reacIsActive and isEquil are anticorrelated
			if (imposeEquil)
				reacIsActive[1][0][i] = !RGgroup[RGC[1][0][i]].isEquil;

			if (reacIsActive[1][0][i]) { // If reaction is not in equilibrium
				Lflux[i] = h1Rates[i] * pop[1][0];
				// Store flux in Reaction object
				RGgroup[RGC[1][0][i]].reactions[RGCmember[1][0][i]].flux = Lflux[i];
				if (Lflux[i] > fluxFloor) {
					Lpopout[kk] = Lflux[i]; // This is flux now, not pop change
					tempIndex[kk] = i;
					kk++;
				}
				fastSlowRates(i, 1, 0, h1Rates[i]);
			} else { // If reaction is in equilibrium
				totalEquilReactions++;
				// Store flux in Reaction object but don't add to flux for this
				// timestep
				RGgroup[RGC[1][0][i]].reactions[RGCmember[1][0][i]].flux = 0;
			}
		}
		// Update all populations corresponding to these reactions
		for (int i = 0; i < kk; i++) {
			try {
				if (Lpopout[i] > 0) {
					RObject[1][0][tempIndex[i]].newZNQ(Lpopout[i]);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				gridSpillOff += Lpopout[i];
			}
		}

		// Neutron light-ion reactions
		// ---------------------------------------------------------------------------------------------------------------------

		Z = 0;
		N = 1;
		kk = 0;

		for (int i = 0; i < numberReactions[0][1]; i++) {
			// totalReactions ++;

			// Remove flux from sum if reaction is in equilibrium.

			// NOTE: following if () added because otherwise it breaks the
			// imposeEquil = false case
			// Ensure reacIsActive and isEquil are anticorrelated
			if (imposeEquil)
				reacIsActive[0][1][i] = !RGgroup[RGC[0][1][i]].isEquil;

			if (reacIsActive[0][1][i]) { // If reaction is not in equilibrium
				Lflux[i] = n0Rates[i] * pop[0][1];
				// Store flux in Reaction object
				RGgroup[RGC[0][1][i]].reactions[RGCmember[0][1][i]].flux = Lflux[i];
				if (Lflux[i] > fluxFloor) {
					Lpopout[kk] = Lflux[i]; // This is flux now, not pop change
					tempIndex[kk] = i;
					kk++;
				}
				fastSlowRates(i, 0, 1, n0Rates[i]);
			} else { // If reaction is in equilibrium
				totalEquilReactions++;
				// Store flux in Reaction object but don't add to flux for this
				// timestep
				RGgroup[RGC[0][1][i]].reactions[RGCmember[0][1][i]].flux = 0;
			}
		}
		// Update all populations corresponding to these reactions
		for (int i = 0; i < kk; i++) {
			try {
				if (Lpopout[i] > 0) {
					RObject[0][1][tempIndex[i]].newZNQ(Lpopout[i]);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				gridSpillOff += Lpopout[i];
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// Method to find non-zero box populations for next integration step. The
	// 1-D
	// array seedNumber is used to hold the non-zero seeds for this timestep.
	// Note that this sum only goes over the "heavy seeds" with Z>=pmin. The
	// light ion on light ion reactions (where for the reactants Z < pmin) are
	// treated
	// separately in LightIonBurner().
	// --------------------------------------------------------------------------------------------------------------

	public void pruneSeeds() {
		int k = 0;
		for (int i = pmin; i < pmax; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (pop[i][j] > boxThresh) {
					seedNumber[k] = pop[i][j];
					seedProtonNumber[k] = (byte) i;
					seedNeutronNumber[k] = (byte) j;
					k++;
				}
			}
		}
		numberSeeds = k++;
		maxSeedsUsed = Math.max(maxSeedsUsed, numberSeeds);
	}

	// --------------------------------------------------------------
	// Method to load data into reaction array RObject[][][]
	// by deserializing reaction objects from disk
	// --------------------------------------------------------------

	void loadData() {

		// Tabulate Z and N of active isotopes for later use in limiting
		// reactions to only those that preserve the network.

		tabulateActiveIsotopes();

		// Set up network vectors for implementing variations of the Mott partial
		// equilibrium scheme. In an N-isotope network, each isotopic species 
		// corresponds to a unit vector in an N-dimensional space.

		setupNetworkVectors();

		// Set up reaction vector array. Each reaction will correspond to a
		// vector in the vector space of isotopic species set up in 
		// setupNetworkVectors().

		reactionVector = new int[pmax][nWidth][50][numberActiveIsotopes];

		// Now set up reaction arrays for heavy seed nuclei

		// Set up a 3-D array to hold reaction objects (multi-D
		// arrays in Java are not true multi-D arrays; they
		// are instead arrays of arrays). Further, we will make
		// the length of the 3rd dimension variable.
		// First set up constant first two dimensions of 3D array:

		RObject = new ReactionClass1[pmax + 1][nWidth][];
		isNeutronReaction = new boolean[pmax + 1][nWidth][];
		isProtonReaction = new boolean[pmax + 1][nWidth][];
		isAlphaReaction = new boolean[pmax + 1][nWidth][];

		// Below we will set the variable 3rd dimension by making each
		// element of RObject[k+pmin][i] a 1-d array of some length.
		// Note the offset of the arrays from N. Generally,
		// Z = k+pmin and N = i+nmin.

		// First deserialize and extract the rates for light-ion reactions like
		// triple-alpha that involve no heavy seeds on the left side. These
		// will be treated separately to keep the bookkeeping transparent.

		String[] HHeFilenames = { 
				"data/iso0_1.ser",                      // neutrons
				"data/iso1_0.ser",                      // protons
				"data/iso1_1.ser",                      // deuterons
				"data/iso1_2.ser",                      // tritons
				"data/iso2_1.ser",                      // 3He
				"data/iso2_2.ser" };                    // 4He

		// Numbers of protons and neutrons for each light-ion case
		int[] Znumber = { 0, 1, 1, 1, 2, 2 }; 
		int[] Nnumber = { 1, 0, 1, 2, 1, 2 }; 

		int indy = 0;

		// Array to hold light-ion/light-ion reaction objects

		// Unlike for isotopes with Z>=3 we will not offset the neutron index
		// from the first isotope of that N in the network and use the neutron number
		// directly for the second index of RObject[][][] for the light-ion on
		// light-ion reaction objects with Z < 3.

		RObject[0][1] = new ReactionClass1[1];       // neutron-light reactions
		RObject[1][0] = new ReactionClass1[3];       // proton-light reactions
		RObject[1][1] = new ReactionClass1[7];       // deuteron-light reactions
		RObject[1][2] = new ReactionClass1[8];       // triton-light reactions
		RObject[2][1] = new ReactionClass1[11];      // 3He-light reactions
		RObject[2][2] = new ReactionClass1[41];      // alpha-light reactions

		// Process the light-ion on light-ion reactions separately (reactions in
		// which all reactants have Z < 3 (e.g., triple alpha)). This is to avoid 
		// double counting since for example the reaction 4He+12C -> 16O appears in 
		// both the 4He and 12C reaction objects.

		// Following variables will count the number of each type accepted
		int alphaCounter = 0;
		int he3Counter = 0;
		int h3Counter = 0;
		int h2Counter = 0;
		int h1Counter = 0;
		int n0Counter = 0;

		// Following variables will count the number of each type considered
		int alphaTries = 0;
		int he3Tries = 0;
		int h3Tries = 0;
		int h2Tries = 0;
		int h1Tries = 0;
		int n0Tries = 0;

		for (int i = 0; i < HHeFilenames.length; i++) {

			indy = 0;
			try {
				
				// Wrap input file stream in an object input stream
				FileInputStream fileIn = new FileInputStream(HHeFilenames[i]);
				ObjectInputStream in = new ObjectInputStream(fileIn);

				// Read from the input stream the initial integer giving
				// the number of objects that were serialized in this file
				// and the array giving the number of objects of each
				// reaction type.

				int numberObjects = in.readInt();
				int[] numberEachType = (int[]) in.readObject();  // Read, but not used

				// Now read in the reaction objects for this Z and N
				ReactionClass1 tempo;
				for (int j = 0; j < numberObjects; j++) {
					tempo = (ReactionClass1) in.readObject();

					// Following logic restricts to Z<3 for all isotopes on left side
					if ((tempo.reacIndex < 4 && tempo.isoIn[0].x < 3)
							|| (tempo.reacIndex > 3 && tempo.reacIndex < 8
									&& tempo.isoIn[0].x < 3 && tempo.isoIn[1].x < 3)
							|| (tempo.reacIndex == 8 && tempo.isoIn[0].x < 3
									&& tempo.isoIn[1].x < 3 && tempo.isoIn[2].x < 3)) {

						// Index i corresponds to n(0), p(1), d(2), t(3) 3He(4), 
						// and 4He(5)

						switch (i) {

						case 0:       // Neutrons
							if (tempo.reacIndex == 1) {

								if (pruneReactions(0, 1, tempo)
										&& !DataHolder.RnotActive[0][1][n0Tries]) {
									n0Counter++;
									RObject[Znumber[i]][Nnumber[i]][indy] = tempo;

									// Add neutron light-ion reactions to reaction vectors
									if (isInNet(0, 1)) {
										createReactionVector(Znumber[i],
											Nnumber[i], indy, tempo);
									}
									serialIndex[0][1][indy] = j;
									indy++;
								}
							}
							n0Tries++;
							break;

						case 1:       // Protons
							if (tempo.reacIndex == 4 && tempo.isoIn[1].x == 1
									&& tempo.isoIn[1].y == 0) {

								if (pruneReactions(1, 0, tempo)
										&& !DataHolder.RnotActive[1][0][h1Tries]) {
									h1Counter++;
									RObject[Znumber[i]][Nnumber[i]][indy] = tempo;

									// Add h1 light-ion reactions to reaction vectors
									if (isInNet(1, 0)) {
										createReactionVector(Znumber[i],
												Nnumber[i], indy, tempo);
									}
									serialIndex[1][0][indy] = j;
									indy++;
								}
							}
							h1Tries++;
							break;

						case 2:       // Deuterons
							if (tempo.reacIndex == 2
									|| (tempo.reacIndex == 4 && tempo.isoIn[1].x < 2)
									|| (tempo.reacIndex == 5
										&& tempo.isoIn[1].x == 1 && tempo.isoIn[1].y == 1)
									|| tempo.reacIndex == 6) {

								if (pruneReactions(1, 1, tempo)
										&& !DataHolder.RnotActive[1][1][h2Tries]) {
									h2Counter++;
									RObject[Znumber[i]][Nnumber[i]][indy] = tempo;

									// Add h2 light-ion reactions to reaction vectors
									if (isInNet(1, 1)) {
										createReactionVector(Znumber[i],
												Nnumber[i], indy, tempo);
									}
									serialIndex[1][1][indy] = j;
									indy++;
								}
							}
							h2Tries++;
							break;

						case 3:       // Tritons
							if (tempo.reacIndex < 4
									|| (tempo.reacIndex == 4 && tempo.isoIn[1].x < 2)
									|| (tempo.reacIndex == 5 && tempo.isoIn[1].x < 2)
									|| tempo.reacIndex == 6
									&& tempo.isoIn[1].x < 2) {

								if (pruneReactions(1, 2, tempo)
										&& !DataHolder.RnotActive[1][2][h3Tries]) {
									h3Counter++;
									RObject[Znumber[i]][Nnumber[i]][indy] = tempo;

									// Add h3 light-ion reactions to reaction vectors
									if (isInNet(1, 2)) {
										createReactionVector(Znumber[i],
												Nnumber[i], indy, tempo);
									}
									serialIndex[1][2][indy] = j;
									indy++;
								}
							}
							h3Tries++;
							break;

						case 4:       // 3-He
							if (tempo.reacIndex < 4
									|| (tempo.reacIndex == 4 && tempo.isoIn[1].y < 2)
									|| (tempo.reacIndex == 5 && tempo.isoIn[1].y < 2)
									|| tempo.reacIndex == 6) {

								if (pruneReactions(2, 1, tempo)
										&& !DataHolder.RnotActive[2][1][he3Tries]) {
									he3Counter++;
									RObject[Znumber[i]][Nnumber[i]][indy] = tempo;

									// Add he3 light-ion reactions to reaction vectors
									if (isInNet(2, 1)) {
										createReactionVector(Znumber[i],
											Nnumber[i], indy, tempo);
									}
									serialIndex[2][1][indy] = j;
									indy++;
								}
							}
							he3Tries++;
							break;

						case 5:       // 4-He

							if (pruneReactions(2, 2, tempo)
									&& !DataHolder.RnotActive[2][2][alphaTries]) {
								alphaCounter++;
								RObject[Znumber[i]][Nnumber[i]][indy] = tempo;

								// Add alpha light-ion reactions to reaction vectors
								if (isInNet(2, 2)) {
									createReactionVector(Znumber[i],
											Nnumber[i], indy, tempo);
								}
								// Add serial index for alpha light-ion reactions
								serialIndex[2][2][indy] = j;
								indy++;
							}
							alphaTries++;
							break;
						}

					}
				}

				// Close the input streams
				in.close();
				fileIn.close();

			} catch (Exception e) {
				System.out.println(e);
			}

		}

		numberReactions[2][2] = alphaCounter;
		numberReactions[2][1] = he3Counter;
		numberReactions[1][2] = h3Counter;
		numberReactions[1][1] = h2Counter;
		numberReactions[1][0] = h1Counter;
		numberReactions[0][1] = n0Counter;

		// Loops over heavy seeds

		for (int k = 0; k < (pmax - pmin); k++) { // loop over Z
			SegreFrame.prom.sets1("Loading Rates:  Z=" + (k + pmin) + "/" + (pmax - 1));
			int nmin = minNetN[k + pmin];
			for (int i = 0; i < nWidth; i++) { // loop over N

				// Skip unless this isotope selected
				if (!IsotopePad.isoColor[k + pmin][i + nmin]) {
					continue;
				}

				// Construct name of the serialized file corresponding
				// to this isotope. These files are produced by the
				// class FriedelParser from the Thielemann reaclib reaction
				// library. They should be in a subdirectory of the present
				// directory called "data", and their names should have
				// the standard form "isoZ_N.ser", where Z is the
				// proton number and N the neutron number of the isotope.

				String file = "data/iso" + (k + pmin) + "_" + (i + nmin) + ".ser";

				try {
					// Wrap input file stream in an object input stream
					FileInputStream fileIn = new FileInputStream(file);
					ObjectInputStream in = new ObjectInputStream(fileIn);

					// Read from the input stream the initial integer giving
					// the number of objects that were serialized in this file

					int numberObjects = in.readInt();

					// Read from the input stream the 9-component int array
					// giving the number of reactions of each type. Entry 0 is
					// the total (=numberObjects). Array entries 1-8 give the
					// the subtotals for each of the 8 reaction types

					int[] numberEachType = (int[]) in.readObject();

					// Calculate the number of objects to read in for this
					// value of Z=k+pmin and N=i+nmin. Remember that
					// DataHolder.RnotActive[][][] is false if the reaction
					// is to be included and true if it is to be excluded
					// (i.e., is not active). Distinguish two cases. If
					// the isotope has been opened for individual adjustment
					// of reactions to include, the array RnotActive controlling
					// which reactions are included is already updated. If
					// the isotope has been selected but not opened for
					// individual adjustment (default: all reactions from
					// the global checked reactions classes included), the
					// array RnotActive has not been updated and we must
					// do so before proceeding.

					int len = 0;
					int[] tempArray = new int[9];

					// The array tempArray will hold in positions 1-8 a one
					// if the corresponding reaction class is selected and
					// a zero otherwise.

					tempArray[0] = 0;
					for (int q = 1; q < 9; q++) {
						if (DataHolder.includeReaction[k + pmin][i + nmin][q]) {
							tempArray[q] = 1;
						} else {
							tempArray[q] = 0;
						}
					}

					ReactionClass1[] tempRclass1 = new ReactionClass1[50];

					// Read all the reaction objects for this isotope (Z=k+pmin
					// and N=i+nmin) into a temporary array of type ReactionClass1.
					// In the process, if the corresponding DataHolder.wasOpened[][]
					// value is false (indicating that the isotope was not opened
					// for editing and has the global default list of reactions),
					// update the array DataHolder.RnotActive[][][] to false
					// entries for each reaction to be included and true for each to
					// be excluded.

					for (int m = 0; m < numberObjects; m++) {
						tempRclass1[m] = (ReactionClass1) in.readObject();

						// If which rates are active read from file (see ChooseActiveRates)
						if (StochasticElements.useReadRatesFlag) {
							boolean checker = true;
							for (int p = 0; p < DataHolder.maxReadRates[k
									+ pmin][i + nmin]; p++) {
								if (DataHolder.useReadRates[k + pmin][i + nmin][p] == m) {
									checker = false;
									break;
								}
							}
							DataHolder.RnotActive[k + pmin][i + nmin][m] = checker;
							// If all initial rates to be ignored (set with
							// mouse ctrl-click)
						} else if (initialRatesZeroFlag) {
							// If all rates for active reaction classes are
							// active (default)
						} else if (!DataHolder.wasOpened[k + pmin][i + nmin]) {
							if (tempArray[tempRclass1[m].reacIndex] == 1) {
								DataHolder.RnotActive[k + pmin][i + nmin][m] = false;
							} else {
								DataHolder.RnotActive[k + pmin][i + nmin][m] = true;
							}
						}

						if (!DataHolder.RnotActive[k + pmin][i + nmin][m]) {
							DataHolder.RnotActive[k + pmin][i + nmin][m] = !pruneReactions(
								k + pmin, i + nmin, tempRclass1[m]);
						}
					}

					// Determine how many reactions can contribute for this
					// case if not overridden by the no-neutrons and max
					// ion mass flags below. Store in the variable len.

					for (int q = 0; q < numberObjects; q++) {
						if (!DataHolder.RnotActive[k + pmin][i + nmin][q]) {
							len++;
						}
					}

					numberReactions[k + pmin][i] = len;

					// Set the variable 3rd dimension of the 3-D array RObject by 
					// making each element of RObject[k][i] a 1-d array of length len.

					RObject[k + pmin][i] = new ReactionClass1[len];
					isNeutronReaction[k + pmin][i] = new boolean[len];
					isProtonReaction[k + pmin][i] = new boolean[len];
					isAlphaReaction[k + pmin][i] = new boolean[len];

					// Deserialize the objects to the array RObject[][][]. Implement
					// the possibility of suppressing neutron reactions (with the
					// flag noNeutronsFlag) and reactions involving ions heavier
					// than maxLightIon mass units. Does so by reading the
					// deserialized object from disk, but not storing in RObject[][][]
					// if conditions are not met. The index m counts the number of
					// objects actually stored in RObject[][][]; the index mm counts
					// the number of objects read in and tested but not necessarily
					// stored because of the conditions on reactions classes or
					// restrictions on neutron reactions or max ion mass set in
					// the parameters interface. Generally, m <= mm.

					int m = 0;   // Number of reaction objects stored in Robject[][][]
					int mm = 0;  // Number of objects read in and tested

					while (mm < numberObjects) {
						ReactionClass1 tryIt = tempRclass1[mm];

						rcheck(tryIt);

						boolean selectFlag = true;
						// If no neutrons flag (presently only suppresses classes 4 & 5)
						if ((tryIt.reacIndex == 4 || tryIt.reacIndex == 5)
								&& tryIt.isoIn[0].x == 0
								&& tryIt.isoIn[0].y == 1 && noNeutronsFlag) {
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							// If limiting mass of interacting ion
						} else if ((tryIt.reacIndex == 4 || tryIt.reacIndex == 5)
								&& ((tryIt.isoIn[0].x + tryIt.isoIn[0].y) > maxLightIon)) {
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							// If including only rates above a floor rate
						} else if (limitRates && !rcheck(tryIt)) {
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Omitting (low rate) "
									+ tryIt.reacString);
						}

						// Suppress al*6 and al-6 reactions in favor of al26 reactions

						int zz = k + pmin;
						int nn = i + nmin;
						if (zz == 11 && nn == 11) {
							if (mm == 8) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 11 && nn == 12) {
							if (mm == 14 || mm == 15) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 12 && nn == 11) {
							if (mm == 14) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 12 && nn == 13) {
							if (mm > 7 && mm < 14) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 12 && nn == 14) {
							if (mm == 10 || mm == 11) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 14 && nn == 12) {
							if (mm == 1 || mm == 11) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 14 && nn == 13) {
							if (mm == 4 || mm == 5 || mm == 6) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 13 && nn == 12) {
							if (mm == 6) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 13 && nn == 13) {
							if ((mm > 0 && mm < 3) || (mm > 7 && mm < 16)
									|| (mm > 19 && mm < 25)
									|| (mm > 30 && mm < 39)) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 13 && nn == 14) {
							if (mm == 1) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 15 && nn == 14) {
							if (mm == 12) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 14 && nn == 15) {
							if (mm == 11) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						if (zz == 15 && nn == 15) {
							if (mm == 5) {
								numberReactions[k + pmin][i]--;
								selectFlag = false;
								System.out.println("Al26: " + tryIt.reacString);
							}
						}

						// Suppress specific individual reactions because of problems
						// in the reaction library. Add one if-statement for each case.
						// Z for the isotope is k+pmin, N is i+nmin, and the index mm is
						// the sequence number (first one is zero) of the reactions
						// serialized for given Z and N. It matches the serialIndex for
						// the reaction that is defined below. The serialIndex is
						// printed in the file stochastic.tmp if the reaction is not
						// suppressed. It is a unique identifier for a reaction. Thus, 
						// to add a new reaction to this list, get its serialIndex by 
						// looking at the output file stochastic.tmp and set mm to 
						// that in the if-statement.

						// alpha+14C -> n+17O
						if ((k + pmin) == 6 && (i + nmin) == 8 && mm == 20) { 
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Suppressing reaction "+ tryIt.reacString);
							toChar.println("Suppressing reaction "+ tryIt.reacString);
						}

						// n+17O -> alpha+14C
						if ((k + pmin) == 8 && (i + nmin) == 9 && mm == 7) { 
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Suppressing reaction "+ tryIt.reacString);
							toChar.println("Suppressing reaction "+ tryIt.reacString);
						}

						// p+12N -> 13O (resonant)
						if ((k + pmin) == 7 && (i + nmin) == 5 && mm == 3) { 
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Suppressing reaction "+ tryIt.reacString);
							toChar.println("Suppressing reaction "+ tryIt.reacString);
						}

						// 13O -> p+12N (resonant)
						if ((k + pmin) == 8 && (i + nmin) == 5 && mm == 1) { 
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Suppressing reaction "+ tryIt.reacString);
							toChar.println("Suppressing reaction "+ tryIt.reacString);
						}

						// p+18Ne -> n+18Na
						if ((k + pmin) == 10 && (i + nmin) == 8 && mm == 12) { 
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Suppressing reaction "+ tryIt.reacString);
							toChar.println("Suppressing reaction "+ tryIt.reacString);
						}

						// p+30S -> 31Cl
						if ((k + pmin) == 16 && (i + nmin) == 14 && mm == 7) { 
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Suppressing reaction "+ tryIt.reacString);
							toChar.println("Suppressing reaction "+ tryIt.reacString);
						}

						// 31Cl -> p+30S
						if ((k + pmin) == 17 && (i + nmin) == 14 && mm == 3) { 
							numberReactions[k + pmin][i]--;
							selectFlag = false;
							System.out.println("Suppressing reaction "+ tryIt.reacString);
							toChar.println("Suppressing reaction "+ tryIt.reacString);
						}

						if (!DataHolder.RnotActive[k + pmin][i + nmin][mm]
								&& selectFlag) {
							RObject[k + pmin][i][m] = tryIt;

							// Store the serial index (position of this reaction object
							// in the deserialization stream for give Z and N) as a
                            // unique identifier of the reaction RObject[k+pmin][i][m].

							serialIndex[k + pmin][i + nmin][m] = mm;

							// Create reaction vector corresponding to this reaction

							createReactionVector(k + pmin, i + nmin, m,
									RObject[k + pmin][i][m]);

							// Set flags for whether reaction involves neutrons,
							// protons, or alphas on the left side

							isNeutronReaction[k + pmin][i][m] = false;
							isProtonReaction[k + pmin][i][m] = false;
							isAlphaReaction[k + pmin][i][m] = false;

							if (RObject[k + pmin][i][m].reacIndex > 3
									&& RObject[k + pmin][i][m].reacIndex < 8) {
								int prot = RObject[k + pmin][i][m].isoIn[0].x;
								int neut = RObject[k + pmin][i][m].isoIn[0].y;
								if (prot == 0 && neut == 1) {
									isNeutronReaction[k + pmin][i][m] = true;
								} else if (prot == 1 && neut == 0) {
									isProtonReaction[k + pmin][i][m] = true;
								} else if (prot == 2 && neut == 2) {
									isAlphaReaction[k + pmin][i][m] = true;
								}
							} else if (RObject[k + pmin][i][m].reacIndex == 8) {
								int prot1 = RObject[k + pmin][i][m].isoIn[0].x;
								int neut1 = RObject[k + pmin][i][m].isoIn[0].y;
								int prot2 = RObject[k + pmin][i][m].isoIn[1].x;
								int neut2 = RObject[k + pmin][i][m].isoIn[1].y;
								if ((prot1 == 0 && neut1 == 1)
										|| (prot2 == 0 && neut2 == 1)) {
									isNeutronReaction[k + pmin][i][m] = true;
								} else if ((prot1 == 1 && neut1 == 0)
										|| (prot2 == 1 && neut2 == 0)) {
									isProtonReaction[k + pmin][i][m] = true;
								} else if ((prot1 == 2 && neut1 == 2)
										|| (prot2 == 2 && neut2 == 2)) {
									isAlphaReaction[k + pmin][i][m] = true;
								}
							}

							if (StochasticElements.useReadRatesFlag) {
								toChar.println("-- Z=" + (k + pmin) + " N="
									+ (i + nmin) + " m=" + m
									+ " SerialIndex = "
									+ serialIndex[k + pmin][i + nmin][m]
									+ " Class = "
									+ RObject[k + pmin][i][m].reacIndex
									+ " Inverse = "
									+ RObject[k + pmin][i][m].reverseR
									+ "  "
									+ RObject[k + pmin][i][m].reacString);
							}

							m++;
						}
						mm++;
					}

					// Close the input streams
					in.close();
					fileIn.close();
				}                       // -- end try
				catch (Exception e) {
					System.out.println(e);
				}
			}           /* end loop over N for heavy seeds */
		}               /* end loop over Z for heavy seeds */

		listActiveReactions();

		// List the light-ion reactions being included in output file stochastic.tmp

		toChar.println();
		toChar.println("\nLIGHT-LIGHT REACTIONS USED (preceded by class number):");
		toChar.println();

		for (int i = 0; i < numberReactions[0][1]; i++) {
			toChar.println(RObject[0][1][i].reacIndex + ":  "
				+ RObject[0][1][i].reacString + "  index=" + i
				+ " serialIndex=" + serialIndex[0][1][i]);
		}
		if (numberReactions[0][1] > 0)
			toChar.println();

		for (int i = 0; i < numberReactions[1][0]; i++) {
			toChar.println(RObject[1][0][i].reacIndex + ":  "
				+ RObject[1][0][i].reacString + "  index=" + i
				+ " serialIndex=" + serialIndex[1][0][i]);
		}
		if (numberReactions[1][0] > 0)
			toChar.println();

		for (int i = 0; i < numberReactions[1][1]; i++) {
			toChar.println(RObject[1][1][i].reacIndex + ":  "
				+ RObject[1][1][i].reacString + "  index=" + i
				+ " serialIndex=" + serialIndex[1][1][i]);
		}
		if (numberReactions[1][1] > 0)
			toChar.println();

		for (int i = 0; i < numberReactions[1][2]; i++) {
			toChar.println(RObject[1][2][i].reacIndex + ":  "
				+ RObject[1][2][i].reacString + "  index=" + i
				+ " serialIndex=" + serialIndex[1][2][i]);
		}
		if (numberReactions[1][2] > 0)
			toChar.println();

		for (int i = 0; i < numberReactions[2][1]; i++) {
			toChar.println(RObject[2][1][i].reacIndex + ":  "
				+ RObject[2][1][i].reacString + "  index=" + i
				+ " serialIndex=" + serialIndex[2][1][i]);
		}
		if (numberReactions[2][1] > 0)
			toChar.println();

		for (int i = 0; i < numberReactions[2][2]; i++) {
			toChar.println(RObject[2][2][i].reacIndex + ":  "
				+ RObject[2][2][i].reacString + "  index=" + i
				+ " serialIndex=" + serialIndex[2][2][i]);
		}

	}              /* End method loadData */

	// ------------------------------------------------------------------------------------------------
	// Method to check rates being read in and to reject them if they
	// are too slow to be likely to contribute in a given case
	// ------------------------------------------------------------------------------------------------

	static boolean rcheck(ReactionClass1 rc) {
		double ck;
		double ratemax = 0;
		for (int i = 0; i < 5; i++) {

			// With new faster rate calculation with powers and logs of T
			// computed once per
			// timestep, need to specify these quantities in ReactionClass1
			// before calling the
			// .rate method (As of svn revision 397, Feb. 18, 2009).
			// (Not tested after inserting this.)

			// Precompute logs and powers of T9 in ReactionClass1
			// before rate calculations

			prepReactionClass1(T9);

			ck = rc.rate(T9cases[i]);
			if (ck > rateFloor) {
				// System.out.println("Accepting " + rc.reacString +
				// "  rate="+ck);
				return true;
			}
			ratemax = Math.max(ratemax, ck);
		}
		// System.out.println("Rejecting " + rc.reacString +
		// "  max rate="+ratemax);
		return false;
	}

	// --------------------------------------------------------------------------------------------------
	// Method to write population array to disk for later plotting
	// --------------------------------------------------------------------------------------------------

	static void savePop() {

		String ts = "0 0 0 0 0";
		String rowString = "";

		toY
				.println("Everything before the triple dollar sign is ignored, as are blank lines");
		toY.println();
		toY.println("$$$"); // delimiter indicating beginning of data
		toY.println();

		toY.println(numdt + " " + totalBoxesPopulated() + " " + nT);

		// The strings ts output placeholder zeros for possible later variables
		toY.println(ts);
		toY.println(ts);
		toY.println(ts);
		toY.println();

		toY.println("Plot_Times:");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(6, timeNow[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("Integrated_Energy(time):");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(4, eNow[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("dE/dt(time):");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(4, deNow[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("dt(time):");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(4, tstep[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("T9(time):");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(4, T9Save[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("rho(time):");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(4, rhoSave[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("Ye(time):");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(4, YeSave[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("sumX(time):");
		for (int i = 0; i < numdt; i++) {
			rowString += gg.decimalPlace(4, sumXSave[i]) + " ";
		}
		toY.println(rowString);

		rowString = "";
		toY.println();

		toY.println("Abundances_Y(Z,N,time):");

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (hasBeenPopulated[i][j]) {
					toY.println();
					toY.println(i + " " + j);
					rowString = "";
					for (int k = 0; k < numdt; k++) {
						rowString += gg.decimalPlace(4, intPop[i][j][k] / nT)
								+ " ";
					}
					toY.println(rowString);
				}
			}
		}

		// Output parameters used in the calculation

		// Ensure that there are no blank text fields to mess up
		// whitespace-delimited token sequence.
		// Method replaceWhiteSpace() trims leading and trailing whitespace and
		// replaces all other
		// whitespace in a string with its second argument (underscore in this
		// case).

		myComment = replaceWhiteSpace(myComment, "_");
		if (myComment.compareTo("") == 0)
			myComment = "NoComment";
		if (myComment.compareTo("(Optional comment)") == 0)
			myComment = "NoComment";
		if (activeFileName.compareTo("") == 0)
			activeFileName = "file.inp";
		if (activeRatesFileName.compareTo("") == 0)
			activeRatesFileName = "file.inp";
		if (abundFileName.compareTo("") == 0)
			abundFileName = "file.inp";
		if (profileFileName.compareTo("") == 0)
			profileFileName = "file.inp";

		toY.println();
		toY.println("Java" + calcModeString + " " + myComment + " "
				+ computeTime + " " + stochasticFactor + " " + massTol);
		toY.println(Ymin + " " + boxPopuli + " " + numberActiveIsotopes + " "
				+ numberAsymptotic + " " + sumX);
		toY.println(nT + " " + ERelease + " " + renormalizeMassFractions + " "
				+ constantHydro + " " + T9);
		toY.println(rho + " " + profileFileName + " " + isoSelectionMode + " "
				+ activeFileName + " " + rateSelectionMode);
		toY.println(activeRatesFileName + " " + initAbundMode + " "
				+ abundFileName + " " + plotY);

		rowString = "";
		toY.println();

		toY.println("Fplus(Z,N,time):");

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (hasBeenPopulated[i][j]) {
					toY.println();
					toY.println(i + " " + j);
					rowString = "";
					for (int k = 0; k < numdt; k++) {
						rowString += sFplus[i][j][k] + " "; // gg.decimalPlace(4,sFplus[i][j][k])+" ";
					}
					toY.println(rowString);
				}
			}
		}

		rowString = "";
		toY.println();

		toY.println("Fminus(Z,N,time):");

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (hasBeenPopulated[i][j]) {
					toY.println();
					toY.println(i + " " + j);
					rowString = "";
					for (int k = 0; k < numdt; k++) {
						rowString += sFminus[i][j][k] + " "; // gg.decimalPlace(4,sFminus[i][j][k])+" ";
					}
					toY.println(rowString);
				}
			}
		}

		System.out.println("  --> Master data to " + outputDataFile
				+ " (Rename to keep)");
		System.out
				.println("      Plot renamed file by choosing Plot Old from interface");
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// Diagnostic method to output number reactions array to disk. Not presently
	// called.
	// ---------------------------------------------------------------------------------------------------------------------------

	static void saveReacNum() {

		int[] tempArray = new int[20];

		// Print out array
		toChar.println();
		toChar.println("Number Reactions:");

		for (int j = 0; j < tintNow; j++) {
			String rowString = "Time=" + gg.decimalPlace(3, timeIntervals[j]);
			for (int i = 0; i < 20; i++) {
				rowString += " " + reacNum[i][j];
			}
			toChar.println(rowString);
		}
		toChar.println();

		for (int j = 0; j < tintNow; j++) {
			for (int i = 0; i < 20; i++) {
				tempArray[i] += reacNum[i][j];
			}
		}

		toChar.println();
		toChar.println("Summed Reactions:");

		String rowString = "";
		for (int i = 0; i < 20; i++) {
			rowString += " " + tempArray[i];
		}
		toChar.println(rowString);

		toChar.println();
		toChar.println("Reactions Per Isotope:");

		double sumAverage = 0;
		for (int j = 0; j < tintNow; j++) {
			sumAverage += reactionsPerIsotope[j];
			toChar.println("t=" + gg.decimalPlace(3, timeIntervals[j])
					+ " isotopes=" + activeIsotopes[j] + " reactions="
					+ reacTot[j] + " ratio="
					+ gg.decimalPlace(3, reactionsPerIsotope[j]));
		}
		toChar.println();
		toChar.println("Average reactions per isotope="
				+ gg.decimalPlace(3, (sumAverage / (double) tintNow)));
		toChar.println();
	}

	// -----------------------------------------------------------------------------
	// Method to count total protons in the network.
	// -----------------------------------------------------------------------------

	public static double countProtons() {
		double pNumber = 0;
		for (int i = 0; i < pmax; i++) {
			for (int j = 0; j < nmax; j++) {
				pNumber += (pop[i][j]) * i;
			}
		}
		return pNumber;
	}

	// -------------------------------------------------------------------------------
	// Method to count total neutrons in the network.
	// -------------------------------------------------------------------------------

	public static double countNeutrons() {
		double nNumber = 0;
		for (int i = 0; i < pmax; i++) {
			for (int j = 0; j < nmax; j++) {
				nNumber += (pop[i][j]) * j;
			}
		}
		return nNumber;
	}

	// -----------------------------------------------------------------------
	// Method to store the values at current time
	// interval for later plotting; also outputs to screen.
	// -----------------------------------------------------------------------

	public void storePop() {

		// Check conservation of neutron and proton number. (Should be
		// separately conserved if weak interactions negligible.) Total
		// nucleon number nT should be conserved under all conditions.

		double neutNumber = countNeutrons();
		double protNumber = countProtons();

		timeStepOutput(protNumber, neutNumber);

		double maxratT = 0;
		int maxZT = -1;
		int maxNT = -1;

		String tempSt;

		sumX = 0;

		for (int i = 0; i < pmax; i++) {
			for (int j = 0; j < nmax; j++) {
				double mass = AA[i][j];
				if (dpop[i][j] != 0) {
					double rat1 = dpop[i][j] / totalSeeds;
					double rat2 = dpop[i][j] / pop[i][j];
					if (Math.abs(dpop[i][j]) > maxratT) {
						maxratT = dpop[i][j];
						maxZT = i;
						maxNT = j;
					}
				}

				intPop[i][j][tintNow] = pop[i][j];
				sFplus[i][j][tintNow] = Fplus[i][j];
				sFminus[i][j][tintNow] = Fminus[i][j];
				sumX += massFrac(i, j);
			}
		}

		sumXSave[tintNow] = sumX;

		if (maxZT > -1 && maxNT > -1) {
			tempSt = "AbsXfer: Z=" + maxZT + " N=" + maxNT + " dn="
					+ gg.decimalPlace(3, maxratT) + " dn/Tot="
					+ gg.decimalPlace(2, maxratT / totalSeeds) + " dX/X="
					+ gg.decimalPlace(2, maxratT / (Y[maxZT][maxNT] * nT))
					+ " Xcorr=" + gg.decimalPlace(3, totalCorrectionSumX);
			System.out.println(tempSt);
			toChar.println(tempSt);
		}

		timeNow[tintNow] = time;

		// Integrated and differential energy release. The boolean EfromQ
		// determines whether the energy release is computed from the
		// Q values (true) or the mass differences (false).

		boolean EfromQ = true;
		if (EfromQ) {
			deNow[tintNow] = dERelease / nT;
			eNow[tintNow] = ERelease / nT; // E from summing Q
		} else {
			deNow[tintNow] = dEReleaseA / nT / deltaTime;
			eNow[tintNow] = EReleaseA / nT; // E from mass differences
		}

		timeIntervals[tintNow] = time;
		fastestRate[tintNow] = fastestCurrentRate;
		slowestRate[tintNow] = slowestCurrentRate;

		tstep[tintNow] = dtAvg();
		T9Save[tintNow] = T9;
		rhoSave[tintNow] = rho;
		YeSave[tintNow] = Ye;

		// Store the mass fractions lightX[tintNow], CMgX[tintNow],
		// SiX[tintNow],
		// FeX[tintNow], NSEX[tintNow] in different regions for later plotting

		checkXdist(tintNow);

		// Write restart file restart.out at every plot timestep

		if (saveRestartFiles)
			writeRestart();

		tempSt = "Z_maxdY=" + maxdpoppZ + " N_maxdY=" + maxdpoppN + " maxdY="
				+ gg.decimalPlace(3, maxdpopp * dtAvg() / nT) // Max change in Y
																// in timestep
				+ " dt_Flux=" + gg.decimalPlace(3, dtFlux) // dt set by flux
															// considerations
															// only
				+ " sumX=" + gg.decimalPlace(7, sumX);
		System.out.println(tempSt);
		toChar.println(tempSt);

		String tempRateString = replaceThisWithThat(maxRateString, " ", "");
		tempRateString = replaceThisWithThat(tempRateString, "-->", " -> ");
		tempSt =
		// "Slow="+gg.decimalPlace(3,slowestRate[tintNow]) +
		"Fast=" + deci(3, fastestRate[tintNow]) + " (" + tempRateString + ")"
				+ " SF=" + stochasticFactor + " massTol=" + massTol + " Steps="
				+ (totalTimeSteps - totalTimeStepsZero);
		System.out.println(tempSt);
		toChar.println(tempSt);

		// Output optional approach to equilibrium information

		if (outputPlotFluxes && showFluxLines && totalTimeSteps > 1) {
			tempSt = "         ----------------------------------------------------";
			System.out.println(tempSt);
			toChar.println(tempSt);
			tempSt = "          Flux (units: Ydot)  F+ = In, F- = Out, F = F+ - F-";
			System.out.println(tempSt);
			toChar.println(tempSt);
			tempSt = "         ----------------------------------------------------";
			System.out.println(tempSt);
			toChar.println(tempSt);
			writeTheFluxes(2, 2);
			writeTheFluxes(6, 6);
			writeTheFluxes(8, 8);
			writeTheFluxes(10, 10);
			writeTheFluxes(12, 12);
			writeTheFluxes(14, 14);
			writeTheFluxes(16, 16);
			writeTheFluxes(18, 18);
			writeTheFluxes(20, 20);
			writeTheFluxes(22, 22);
			writeTheFluxes(24, 24);
			writeTheFluxes(26, 26);
			writeTheFluxes(28, 28);
			writeTheFluxes(30, 30);
		}

		if (doAsymptotic && totalTimeSteps > 1) {

			tempSt = "Asymptotic: ";

			// Enumerate isotopes being treated asymptotically for output to
			// stochastic.tmp.

			double countem = 0;
			for (int i = minNetZ; i <= maxNetZ; i++) {
				int indy = Math.min(maxNetN[i], nmax - 1);
				for (int j = minNetN[i]; j <= indy; j++) {
					if (isAsymptotic[i][j]) {
						tempSt += ("(" + i + "," + j + ") ");
						countem++;
					}
					if (tempSt.length() > 70) {
						toChar.println(tempSt);
						if (displayAsyIsotopes)
							System.out.println(tempSt);
						tempSt = "";
					}
				}
			}
			if (countem > 0 && tempSt.length() <= 70) {
				toChar.println(tempSt);
				if (displayAsyIsotopes)
					System.out.println(tempSt);
			}

			// Output CNO information if running the CNO cycle

			if (isCNONetwork) {
				System.out.println("CNO: Y10=" + gg.decimalPlace(4, Y[1][0])
						+ " Y22=" + gg.decimalPlace(4, Y[2][2]) + " Y66="
						+ gg.decimalPlace(4, Y[6][6]) + " Y76="
						+ gg.decimalPlace(4, Y[7][6]) + " Y67="
						+ gg.decimalPlace(4, Y[6][7]));
				System.out.println("Y77="
						+ gg.decimalPlace(4, Y[7][7])
						+ " Y87="
						+ gg.decimalPlace(4, Y[8][7])
						+ " Y78="
						+ gg.decimalPlace(4, Y[7][8])
						+ " sumY_CNO="
						+ gg.decimalPlace(4, (Y[6][6] + Y[7][6] + Y[6][7]
								+ Y[7][7] + Y[8][7] + Y[7][8])));
			}

			// Check for which reaction groups satisfy conditions for partial
			// equilibrium, even if partial
			// equilibrium is not being imposed

			nontrivialRG = 0;
			countPotentialEquil = 0;

			String tracString = "";
			for (int kk = 0; kk < numberReactionGroups; kk++) {
				// Changed following to >0 to count 1-member groups too
				if (RGgroup[kk].members > 0)
					nontrivialRG++;
				// Changed to count groups that would be in equil if no time or
				// Ythresh threshhold
				if (RGgroup[kk].isEquil) {
					// if(RGgroup[kk].getEquil()){
					// if(RGgroup[kk].testForEquilibrium(equiTol)) {
					countPotentialEquil++;
					tracString += replaceThisWithThat(
							RGgroup[kk].reactions[RGgroup[kk].refreac].reacString
									+ "|", " ", "");
				}
			}
			fracRGequilibrated = (double) countPotentialEquil
					/ (double) nontrivialRG;

			// Summarize asymptotic and equil to screen

			String tempStSys = "Asymptotic:";
			// String tempStSys = "Steps="+(totalTimeSteps - totalTimeStepsZero)
			// + " Asymptotic:";
			if (numberAsymptoticLast == 0) {
				tempStSys += "none";
			} else {
				tempStSys += (numberAsymptoticLast + "/" + currentNonZero());
			}
			if (!imposeEquil) {
				if (countPotentialEquil == 0) {
					tempStSys += " Pot Equil:none";
				} else {
					tempStSys += (" Pot Equil:" + countPotentialEquil + "/" + nontrivialRG);
					if (displayEquilReac && !imposeEquil && equilibrate)
						tempStSys += (" |" + tracString);
				}
			}
			if (totalEquilReactions == 0) {
				tempStSys += "\nEquil Imposed:none";
			} else {
				tempStSys += ("\nEquil Imposed:" + totalEquilReactions + "/"
						+ totalReactions);
				if (displayEquilReac)
					tempStSys += ("\n" + (replaceThisWithThat(
							returnEquilibratedReactionString(), " ", "")));
			}
			System.out.println(tempStSys);

			// Summarize asymptotic and equil to stochastic.tmp

			if (countem == 0) {
				tempSt += "none";
				toChar.println(tempSt);
			} else {
				tempSt = ("<" + (int) countem + " isotopes out of "
						+ numberSeeds + " asymptotic>");
				toChar.println(tempSt);
			}

			toChar.println("Total Reactions=" + totalReactions
					+ " Number equilibrated=" + totalEquilReactions);
			// System.out.println("Total Reactions="+totalReactions +
			// " Number equilibrated="+totalEquilReactions );
		}

		// Output flux plotting information if the network is an alpha network

		if (isAlphaNetwork && totalTimeSteps > 1) {
			tempSt = gg.decimalPlace(5, LOG10 * Math.log(time)) + " ";
			tempSt += gg.decimalPlace(4, LOG10 * Math.log(tstep[tintNow]))
					+ " ";
			tempSt += fluxFiler(2, 2);
			tempSt += fluxFiler(6, 6);
			tempSt += fluxFiler(8, 8);
			tempSt += fluxFiler(10, 10);
			tempSt += fluxFiler(12, 12);
			tempSt += fluxFiler(14, 14);
			tempSt += fluxFiler(16, 16);
			tempSt += fluxFiler(18, 18);
			tempSt += fluxFiler(20, 20);
			tempSt += fluxFiler(22, 22);
			tempSt += fluxFiler(24, 24);
			tempSt += fluxFiler(26, 26);
			tempSt += fluxFiler(28, 28);
			tempSt += fluxFiler(30, 30);
			tempSt += fluxFiler(1, 0);
			tempSt += fluxFiler(0, 1);
			tempSt += fluxFiler(13, 13);
			tempSt += fluxFiler(26, 28);
			toFluxQSS.println(tempSt);
		} else if (isCNONetwork && totalTimeSteps > 1) {
			tempSt = gg.decimalPlace(5, LOG10 * Math.log(time)) + " ";
			tempSt += gg.decimalPlace(4, LOG10 * Math.log(tstep[tintNow]))+ " ";
			tempSt += fluxFiler(1, 0);
			tempSt += fluxFiler(2, 2);
			tempSt += fluxFiler(6, 6);
			tempSt += fluxFiler(7, 6);
			tempSt += fluxFiler(6, 7);
			tempSt += fluxFiler(7, 7);
			tempSt += fluxFiler(8, 7);
			tempSt += fluxFiler(7, 8);
			toFluxQSS.println(tempSt);
		}

		System.out.println(tempstring1);

		// Output stuff to plot file plotfile.out for Maple plots
		// (ECON converts from MeV/nucleon/s to erg/g/s)

		if (totalTimeSteps > 1) {
			toPlot.println(
				deci(12, LOG10 * Math.log(time))+ " "
				+ deci(12, LOG10 * Math.log(deltaTimeRestart))+ " "
				// + deci(8, LOG10*Math.log(timestepStacker[9])) + " "
				+ deci(8, LOG10 * Math.log(deltaTime))+ " "
				+ deci(4, LOG10 * Math.log(Math.abs(Y[0][1])))+ " "
				+ deci(4, LOG10 * Math.log(Math.abs(Y[1][0])))+ " "
				+ deci(4, LOG10 * Math.log(Math.abs(Y[2][2] * 4)))+ " "
				+ deci(4, LOG10*Math.log(Math.abs(ECON*dERelease/nT)))+ " "
				+ deci(4, LOG10*Math.log(Math.abs(ECON*ERelease/nT)))+ " "
				+ deci(4, T9)+ " "
				+ deci(4, rho)+ " "
				+ deci(8, LOG10*Math.log(1/fastestRate[tintNow]))+ " "
				+ deci(4, LOG10*Math.log(Math.abs(ECON*dEReleaseA /nT/deltaTime)))+ " "
				+ deci(4, LOG10*Math.log(Math.abs(ECON*EReleaseA/nT)))+ " "
				+ deci(4, (double) numberAsymptoticLast/(double)currentNonZero())+ " "
				+ deci(4, (double) totalEquilReactions/ (double) totalReactions)+ " "
				+ deci(4, fracRGequilibrated)+ " "
				+ deci(4, plotDevious)+ " "
				+ deci(4, XcorrFac)+ " "
				+ deci(4, stochasticFactor)+ " "
				+ deci(4, deltaTimeRestart*maxdpopp/nT/Y[maxdpoppZ][maxdpoppN])+ " "
				+ deci(4, maxFluxRG / nT)+ " "
				+ deci(4, sumX)+ " "
				+ deci(4, LOG10 * Math.log(dtFlux))+ " "
				+ deci(12, time)+ " "
				+ deci(4, massTol)+ " "
				+ (totalTimeSteps - totalTimeStepsZero - savedTimeSteps)+ " " 
				+ (totalTimeSteps - totalTimeStepsZero));

		}

		savedTimeSteps = totalTimeSteps - totalTimeStepsZero;

		// Flush the output streams to send intermediate stuff to files so
		// they can be read during the calculation

		toChar.flush();
		toY.flush();
		toDiag.flush();
		toRestart.flush();
		toPlot.flush();
		if (!integrateWithJava)
			toF90.flush();
		toX.flush();
		toRateStuff.flush();
		toFluxQSS.flush();
		toReduced.flush();
		toGraph.flush();
	}

	// ---------------------------------------------------------------------------
	// Method to set plot time intervals (this is for plotting output; it has
	// nothing to do with the integration timestep)
	// ---------------------------------------------------------------------------

	static void setTimeIntervals() {

		/*
		 * // If time intervals are linear:
		 * 
		 * double dt = (tmax-tmin)/(double)tintMax; for(int i=0; i<tintMax; i++)
		 * { timeIntervals[i] = dt;
		 * System.out.println("time interval "+i+" = "+timeIntervals[i]
		 * +"tmax="+tmax+" tmin="+tmin); } double dt = logtmin;
		 */

		// If time intervals are logarithmic:
		// Assume each successive interval to be on log scale
		// where logtminPlot is the base-10 log of the minimum
		// time to plot and logtmaxPlot is the base-10 log of the maximum
		// time to plot. For example, if logtminPlot=0 and logtmaxPlot = 4
		// and nintervals is equal to 4, the plotting timestep intervals will 
		// be at times of 1, 10, 100, 1000, and 10,000 seconds.

		double tempsum = logtminPlot;
		numdt = nintervals + 1;
		double expofac = (logtmaxPlot - logtminPlot) / (double) nintervals;
		timeIntervals[0] = Math.pow(10, logtminPlot);

		for (int i = 1; i < numdt; i++) {
			tempsum += expofac;
			timeIntervals[i] = Math.pow(10, tempsum);
		}
	}

	// -----------------------------------------------------------------------------
	// Method to set some global variables for the calculation
	// -----------------------------------------------------------------------------

	public static void setGlobalVariables() {
		time = 0;
		setTimeIntervals();
		setAbundances();
	}

	// ----------------------------------------------------------------------
	// Method to call system exit
	// ----------------------------------------------------------------------

	public static void callExit(String message) {
		System.out.println();
		System.out.println(message);
		System.out.println();
		System.exit(1);
	}

	// -----------------------------------------------------------------------
	// Method to set isotopic abundances
	// -----------------------------------------------------------------------

	public static void setAbundances() {

		// If this is not a restart (if it is restart, nT was read in
		// AbundanceData from restart)

		if (nT == 0) {
			double den = 0;

			// Note: The values of seedY[i] are set from the class AbundanceData
			// by commands of the form StochasticElements.seedY[i] = YY

			for (int i = 0; i < numberSeeds; i++) {
				den += seedY[i];
			}
			den = 0;
			for (int i = minNetZ; i <= maxNetZ; i++) {
				int indy = Math.min(maxNetN[i], nmax - 1);
				for (int j = minNetN[i]; j <= indy; j++) {
					den += Y[i][j];
				}
			}

			f = totalSeeds / den;

			// Note: The values of static variables YH and YHe are set here from
			// the class AbundanceData by commands of the form 
			// StochasticElements.YH = value.

			pop[1][0] = initH = (f * YH);
			pop[2][2] = initHe = (f * YHe);
			nT = pop[1][0] + 4 * pop[2][2];

			for (int i = 0; i < numberSeeds; i++) {
				seedNumber[i] = (f * seedY[i]);
				pop[seedProtonNumber[i]][seedNeutronNumber[i]] = seedNumber[i];
				int A = seedProtonNumber[i] + seedNeutronNumber[i];
				nT += (seedNumber[i] * A);
			}
		}
		f = nT;

		// NOTE: this code for nT is left over from original stochastic
		// formulation and nT may not be computed correctly here if there are
		// more than 1H and 4He light-ion (Z<3) species in the network. It will be
		// recomputed correctly as the sum of all nucleons in the initial
		// network at the beginning of the integration in stochasticIntegrator().
	}

	// ------------------------------------------------------------------------------
	// Method to update temperature and density according to hydro profile
	// ------------------------------------------------------------------------------

	static void updateHydro() {

		// Make calls to the methods interpolateT.splint(time) and
		// interpolaterho.splint(time) to interpolate log T and
		// log rho, respectively, as a function of log time.

		double logrho;
		double logt = LOG10 * Math.log(time);
		logT = interpolateT.splint(logt);
		T9 = Math.pow(10, logT) / 1e9;
		logrho = interpolaterho.splint(logt);
		rho = Math.pow(10, logrho);

		// Precompute logs and powers of T9 in ReactionClass1 before rate calculations
		prepReactionClass1(T9);
	}

	// -----------------------------------------------------------------------
	// Print for contour plotter
	// -----------------------------------------------------------------------

	public static void print() {

		ContourPlotter p = new ContourPlotter();
		Toolkit toolkit = p.getToolkit();
		PrintJob job = toolkit.getPrintJob(p, "nzPlane", printprefs);

		if (job == null) return;

		Graphics page = job.getGraphics();

		Dimension size = p.getSize();

		Dimension pagesize = job.getPageDimension();
		page.translate((pagesize.width - 700) / 2, (pagesize.height - 650) / 2);
		page.drawRect(1, 1, 700, 650);
		p.print(page);

		page.dispose();
		job.end();

	}

	// -------------------------------------------------------------------------------
	// Compute some statistics on isotopes and couplings in the network and
	// send some basic reaction data to the output file for reactions included
	// -------------------------------------------------------------------------------

	public static void bookkeeping() {

		// Compute some statistics on isotopes and couplings in the network and
		// send some basic reaction data to the output file for reactions included

		// Neutron + light-ion reactions

		if (IsotopePad.isoColor[0][1]) {
			toChar.println();
			toChar.println("Z=0  N=1 Light-Light Reactions:");
			for (int k = 0; k < numberReactions[0][1]; k++) {
				numberCouplings++;
				toChar.println();
				prepReactionClass1(0.01);
				double r1 = RObject[0][1][k].rate(0.01);
				prepReactionClass1(0.1);
				double r2 = RObject[0][1][k].rate(0.1);
				prepReactionClass1(0.5);
				double r3 = RObject[0][1][k].rate(0.5);
				prepReactionClass1(1.0);
				double r4 = RObject[0][1][k].rate(1.0);
				prepReactionClass1(3.0);
				double r5 = RObject[0][1][k].rate(3.0);
				prepReactionClass1(6.0);
				double r6 = RObject[0][1][k].rate(6.0);
				prepReactionClass1(10.0);
				double r7 = RObject[0][1][k].rate(10.0);
				toChar.println(k + " Serial=" + serialIndex[0][1][k]
						+ "  Class=" + RObject[0][1][k].reacIndex + "  "
						+ RObject[0][1][k].reacString + "  Q = "
						+ RObject[0][1][k].Q + "  p0=" + RObject[0][1][k].p0
						+ "  p1=" + RObject[0][1][k].p1);
				toChar.println("p2=" + RObject[0][1][k].p2 + "  p3="
						+ RObject[0][1][k].p3 + "  p4=" + RObject[0][1][k].p4
						+ "  p5=" + RObject[0][1][k].p5 + "  p6="
						+ RObject[0][1][k].p6);
				toChar.println("Mol R(T9): " + " R(0.01)=" + deci(3, r1)
						+ " R(0.1)=" + deci(3, r2) + " R(0.5)=" + deci(3, r3));
				toChar.println("R(1.0)=" + deci(3, r4) + " R(3.0)="
						+ deci(3, r5) + " R(6.0)=" + deci(3, r6) + " R(10.0)="
						+ deci(3, r7));
			}
		}

		// 1H + light-ion reactions

		if (IsotopePad.isoColor[1][0]) {
			toChar.println();
			toChar.println("Z=1  N=0 Light-Light Reactions:");
			for (int k = 0; k < numberReactions[1][0]; k++) {
				numberCouplings++;
				toChar.println();
				prepReactionClass1(0.01);
				double r1 = RObject[1][0][k].rate(0.01);
				prepReactionClass1(0.1);
				double r2 = RObject[1][0][k].rate(0.1);
				prepReactionClass1(0.5);
				double r3 = RObject[1][0][k].rate(0.5);
				prepReactionClass1(1.0);
				double r4 = RObject[1][0][k].rate(1.0);
				prepReactionClass1(3.0);
				double r5 = RObject[1][0][k].rate(3.0);
				prepReactionClass1(6.0);
				double r6 = RObject[1][0][k].rate(6.0);
				prepReactionClass1(10.0);
				double r7 = RObject[1][0][k].rate(10.0);
				toChar.println(k + " Serial=" + serialIndex[1][0][k]
						+ "  Class=" + RObject[1][0][k].reacIndex + "  "
						+ RObject[1][0][k].reacString + "  Q = "
						+ RObject[1][0][k].Q + "  p0=" + RObject[1][0][k].p0
						+ "  p1=" + RObject[1][0][k].p1);
				toChar.println("p2=" + RObject[1][0][k].p2 + "  p3="
						+ RObject[1][0][k].p3 + "  p4=" + RObject[1][0][k].p4
						+ "  p5=" + RObject[1][0][k].p5 + "  p6="
						+ RObject[1][0][k].p6);
				toChar.println("Mol R(T9): " + " R(0.01)=" + deci(3, r1)
						+ " R(0.1)=" + deci(3, r2) + " R(0.5)=" + deci(3, r3));
				toChar.println("R(1.0)=" + deci(3, r4) + " R(3.0)="
						+ deci(3, r5) + " R(6.0)=" + deci(3, r6) + " R(10.0)="
						+ deci(3, r7));
			}
		}

		// 2H + light-ion reactions

		if (IsotopePad.isoColor[1][1]) {
			toChar.println();
			toChar.println("Z=1  N=1 Light-Light Reactions:");
			for (int k = 0; k < numberReactions[1][1]; k++) {
				numberCouplings++;
				toChar.println();
				prepReactionClass1(0.01);
				double r1 = RObject[1][1][k].rate(0.01);
				prepReactionClass1(0.1);
				double r2 = RObject[1][1][k].rate(0.1);
				prepReactionClass1(0.5);
				double r3 = RObject[1][1][k].rate(0.5);
				prepReactionClass1(1.0);
				double r4 = RObject[1][1][k].rate(1.0);
				prepReactionClass1(3.0);
				double r5 = RObject[1][1][k].rate(3.0);
				prepReactionClass1(6.0);
				double r6 = RObject[1][1][k].rate(6.0);
				prepReactionClass1(10.0);
				double r7 = RObject[1][1][k].rate(10.0);
				toChar.println(k + " Serial=" + serialIndex[1][1][k]
						+ "  Class=" + RObject[1][1][k].reacIndex + "  "
						+ RObject[1][1][k].reacString + "  Q = "
						+ RObject[1][1][k].Q + "  p0=" + RObject[1][1][k].p0
						+ "  p1=" + RObject[1][1][k].p1);
				toChar.println("p2=" + RObject[1][1][k].p2 + "  p3="
						+ RObject[1][1][k].p3 + "  p4=" + RObject[1][1][k].p4
						+ "  p5=" + RObject[1][1][k].p5 + "  p6="
						+ RObject[1][1][k].p6);
				toChar.println("Mol R(T9): " + " R(0.01)=" + deci(3, r1)
						+ " R(0.1)=" + deci(3, r2) + " R(0.5)=" + deci(3, r3));
				toChar.println("R(1.0)=" + deci(3, r4) + " R(3.0)="
						+ deci(3, r5) + " R(6.0)=" + deci(3, r6) + " R(10.0)="
						+ deci(3, r7));
			}
		}

		// 3H + light-ion reactions

		if (IsotopePad.isoColor[1][2]) {
			toChar.println();
			toChar.println("Z=1  N=2 Light-Light Reactions:");
			for (int k = 0; k < numberReactions[1][2]; k++) {
				numberCouplings++;
				toChar.println();
				prepReactionClass1(0.01);
				double r1 = RObject[1][2][k].rate(0.01);
				prepReactionClass1(0.1);
				double r2 = RObject[1][2][k].rate(0.1);
				prepReactionClass1(0.5);
				double r3 = RObject[1][2][k].rate(0.5);
				prepReactionClass1(1.0);
				double r4 = RObject[1][2][k].rate(1.0);
				prepReactionClass1(3.0);
				double r5 = RObject[1][2][k].rate(3.0);
				prepReactionClass1(6.0);
				double r6 = RObject[1][2][k].rate(6.0);
				prepReactionClass1(10.0);
				double r7 = RObject[1][2][k].rate(10.0);
				toChar.println(k + " Serial=" + serialIndex[1][2][k]
						+ "  Class=" + RObject[1][2][k].reacIndex + "  "
						+ RObject[1][2][k].reacString + "  Q = "
						+ RObject[1][2][k].Q + "  p0=" + RObject[1][2][k].p0
						+ "  p1=" + RObject[1][2][k].p1);
				toChar.println("p2=" + RObject[1][2][k].p2 + "  p3="
						+ RObject[1][2][k].p3 + "  p4=" + RObject[1][2][k].p4
						+ "  p5=" + RObject[1][2][k].p5 + "  p6="
						+ RObject[1][2][k].p6);
				toChar.println("Mol R(T9): " + " R(0.01)=" + deci(3, r1)
						+ " R(0.1)=" + deci(3, r2) + " R(0.5)=" + deci(3, r3));
				toChar.println("R(1.0)=" + deci(3, r4) + " R(3.0)="
						+ deci(3, r5) + " R(6.0)=" + deci(3, r6) + " R(10.0)="
						+ deci(3, r7));
			}
		}

		// 3He + light-ion reactions

		if (IsotopePad.isoColor[2][1]) {
			toChar.println();
			toChar.println("Z=2  N=1 Light-Light Reactions:");
			for (int k = 0; k < numberReactions[2][1]; k++) {
				numberCouplings++;
				toChar.println();
				prepReactionClass1(0.01);
				double r1 = RObject[2][1][k].rate(0.01);
				prepReactionClass1(0.1);
				double r2 = RObject[2][1][k].rate(0.1);
				prepReactionClass1(0.5);
				double r3 = RObject[2][1][k].rate(0.5);
				prepReactionClass1(1.0);
				double r4 = RObject[2][1][k].rate(1.0);
				prepReactionClass1(3.0);
				double r5 = RObject[2][1][k].rate(3.0);
				prepReactionClass1(6.0);
				double r6 = RObject[2][1][k].rate(6.0);
				prepReactionClass1(10.0);
				double r7 = RObject[2][1][k].rate(10.0);
				toChar.println(k + " Serial=" + serialIndex[2][1][k]
						+ "  Class=" + RObject[2][1][k].reacIndex + "  "
						+ RObject[2][1][k].reacString + "  Q = "
						+ RObject[2][1][k].Q + "  p0=" + RObject[2][1][k].p0
						+ "  p1=" + RObject[2][1][k].p1);
				toChar.println("p2=" + RObject[2][1][k].p2 + "  p3="
						+ RObject[2][1][k].p3 + "  p4=" + RObject[2][1][k].p4
						+ "  p5=" + RObject[2][1][k].p5 + "  p6="
						+ RObject[2][1][k].p6);
				toChar.println("Mol R(T9): " + " R(0.01)=" + deci(3, r1)
						+ " R(0.1)=" + deci(3, r2) + " R(0.5)=" + deci(3, r3));
				toChar.println("R(1.0)=" + deci(3, r4) + " R(3.0)="
						+ deci(3, r5) + " R(6.0)=" + deci(3, r6) + " R(10.0)="
						+ deci(3, r7));
			}
		}

		// alpha + light-ion reactions

		if (IsotopePad.isoColor[2][2]) {
			toChar.println();
			toChar.println("Z=2  N=2 Light-Light Reactions:");
			for (int k = 0; k < numberReactions[2][2]; k++) {
				numberCouplings++;
				toChar.println();
				prepReactionClass1(0.01);
				double r1 = RObject[2][2][k].rate(0.01);
				prepReactionClass1(0.1);
				double r2 = RObject[2][2][k].rate(0.1);
				prepReactionClass1(0.5);
				double r3 = RObject[2][2][k].rate(0.5);
				prepReactionClass1(1.0);
				double r4 = RObject[2][2][k].rate(1.0);
				prepReactionClass1(3.0);
				double r5 = RObject[2][2][k].rate(3.0);
				prepReactionClass1(6.0);
				double r6 = RObject[2][2][k].rate(6.0);
				prepReactionClass1(10.0);
				double r7 = RObject[2][2][k].rate(10.0);
				toChar.println(k + " Serial=" + serialIndex[2][2][k]
						+ "  Class=" + RObject[2][2][k].reacIndex + "  "
						+ RObject[2][2][k].reacString + "  Q = "
						+ RObject[2][2][k].Q + "  p0=" + RObject[2][2][k].p0
						+ "  p1=" + RObject[2][2][k].p1);
				toChar.println("p2=" + RObject[2][2][k].p2 + "  p3="
						+ RObject[2][2][k].p3 + "  p4=" + RObject[2][2][k].p4
						+ "  p5=" + RObject[2][2][k].p5 + "  p6="
						+ RObject[2][2][k].p6);
				toChar.println("Mol R(T9): " + " R(0.01)=" + deci(3, r1)
						+ " R(0.1)=" + deci(3, r2) + " R(0.5)=" + deci(3, r3));
				toChar.println("R(1.0)=" + deci(3, r4) + " R(3.0)="
						+ deci(3, r5) + " R(6.0)=" + deci(3, r6) + " R(10.0)="
						+ deci(3, r7));
			}
		}

		// Heavy seeds

		for (int i = pmin; i < pmax; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (IsotopePad.isoColor[i][j]) {
					numberNuclei++;
					toChar.println();
					toChar.println();
					toChar.println("Z=" + i + " N=" + j + " Reactions:");
					int countem = -1;
					for (int k = 0; k < numberReactions[i][j - minNetN[i]]; k++) {
						countem++;
						numberCouplings++;
						toChar.println();
						prepReactionClass1(0.01);
						double r1 = RObject[i][j - minNetN[i]][k].rate(0.01);
						prepReactionClass1(0.1);
						double r2 = RObject[i][j - minNetN[i]][k].rate(0.1);
						prepReactionClass1(0.5);
						double r3 = RObject[i][j - minNetN[i]][k].rate(0.5);
						prepReactionClass1(1.0);
						double r4 = RObject[i][j - minNetN[i]][k].rate(1.0);
						prepReactionClass1(3.0);
						double r5 = RObject[i][j - minNetN[i]][k].rate(3.0);
						prepReactionClass1(6.0);
						double r6 = RObject[i][j - minNetN[i]][k].rate(6.0);
						prepReactionClass1(10.0);
						double r7 = RObject[i][j - minNetN[i]][k].rate(10.0);

						toChar.println(countem + " Serial="
								+ serialIndex[i][j][k] + "  Class="
								+ RObject[i][j - minNetN[i]][k].reacIndex
								+ "  "
								+ RObject[i][j - minNetN[i]][k].reacString
								+ "  Q = " + RObject[i][j - minNetN[i]][k].Q
								+ "  p0=" + RObject[i][j - minNetN[i]][k].p0
								+ "  p1=" + RObject[i][j - minNetN[i]][k].p1);
						toChar.println("p2=" + RObject[i][j - minNetN[i]][k].p2
								+ "  p3=" + RObject[i][j - minNetN[i]][k].p3
								+ "  p4=" + RObject[i][j - minNetN[i]][k].p4
								+ "  p5=" + RObject[i][j - minNetN[i]][k].p5
								+ "  p6=" + RObject[i][j - minNetN[i]][k].p6);
						toChar.println("Mol R(T9): " + " R(0.01)="
								+ deci(3, r1) + " R(0.1)=" + deci(3, r2)
								+ " R(0.5)=" + deci(3, r3));

						toChar.println("R(1.0)=" + deci(3, r4) + " R(3.0)="
								+ deci(3, r5) + " R(6.0)=" + deci(3, r6)
								+ " R(10.0)=" + deci(3, r7));
						toChar.flush();
					}
				}
			}
		}
	}

	// -----------------------------------------------------------------------------------------
	// Method to display plot windows at the end of the calculation
	// -----------------------------------------------------------------------------------------

	public void displayPlots() {

		display3DPlots();

		// Create a 2d flux animation but only if the calculation is not with
		// the F90 code
		// (the flux info is not yet exported to the Y.inp data file for the F90
		// case)

		if (doFluxPlots) {
			create2DFluxAnimation();
		}

		// Create a 2d contour display interface for the population results

		ShowIsotopes si = new ShowIsotopes();

		// Create a customized plot frame and display it

		amPlottingRates = false; // To ensure correct formatting

		final AbPlotFrame mpf = new AbPlotFrame();
		if (longFormat) { // If long vertical lineplot format
			if (numberCurvesToShow <= 50) {
				mpf.setSize(525, 700); // With single-column legend if <= 51
										// curves
			} else {
				mpf.setSize(600, 700); // With double-column legend
			}
		} else {
			// Short vertical format
			mpf.setSize(580, 515);
		}

		mpf.setTitle(" Elemental Abundances");
		mpf.setLocation(50, 50);
		mpf.setResizable(true);
		mpf.setBackground(new Color(255, 255, 255));

		// Create a menu bar and add menu to it for the plot frame
		MenuBar plotmb = new MenuBar();
		mpf.setMenuBar(plotmb);
		Menu plotMenu = new Menu("File");
		plotmb.add(plotMenu);

		// Create menu items with keyboard shortcuts for the plot frame
		MenuItem ss, pp, qq;
		plotMenu.add(ss = new MenuItem("Save as Postscript", new MenuShortcut(
				KeyEvent.VK_S)));
		plotMenu
				.add(pp = new MenuItem("Print", new MenuShortcut(KeyEvent.VK_P)));
		plotMenu.addSeparator(); // Menu separator
		plotMenu
				.add(qq = new MenuItem("Quit", new MenuShortcut(KeyEvent.VK_Q)));

		// Create and register action listeners for menu items
		ss.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PlotFileDialogue fd = new PlotFileDialogue(100, 100, 400, 110,
						Color.black, Color.lightGray, "Choose File Name",
						"Choose a postscript file name:");
				fd.setResizable(false);
				fd.hT.setText("X.ps");
				fd.show();
			}
		});

		pp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mpf.printThisFrame(50, 125, false);
			}
		});

		qq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mpf.hide();
				mpf.dispose();
			}
		});
		mpf.show();
	}

	// -------------------------------------------------------------------------------------------------------------
	// Method to create 2D animated display of populations. Not presently used.
	// -------------------------------------------------------------------------------------------------------------

	public void create2DPopAnimation() {

		String tmp = "Abundance Y";
		String tmp2 = "Abundances";
		if (!plotY) {
			tmp = "Mass Frac X";
			tmp2 = "Mass Fractions";
		}

		// Fill the temporary working array that will be plotted

		for (int i = 0; i <= pmax; i++) {
			int indy = Math.min(IsotopePad.maxDripN[i], nmax - 1);
			for (int j = IsotopePad.minDripN[i]; j <= indy; j++) {
				for (int k = 0; k <= nintervals + 1; k++) {
					twa[i][j][k] = intPop[i][j][k] / nT; // If plotting
															// abundance Y
					if (!plotY)
						twa[i][j][k] *= ((double) (i + j)); // If plotting mass
															// fraction X
				}
			}
		}

		ShowIsotopeFlux sip = new ShowIsotopeFlux(tmp2, tmp, logContour,
				minLogContour, maxLogContour, twa);

	}

	// -------------------------------------------------------------------------
	// Method to create 2D animated display of fluxes
	// -------------------------------------------------------------------------

	public void create2DFluxAnimation() {

		double avg = 0;
		double diff = 0;
		double min = 1e50;
		double max = 0;

		int Zmin = -1;
		int Nmin = -1;
		int tmin = -1;

		// Fill the temporary working array that will be plotted

		for (int i = 0; i <= pmax; i++) {
			int indy = Math.min(IsotopePad.maxDripN[i], nmax - 1);
			for (int j = IsotopePad.minDripN[i]; j <= indy; j++) {
				for (int k = 0; k < nintervals + 1; k++) {
					avg = (sFplus[i][j][k] + sFminus[i][j][k]);
					diff = sFplus[i][j][k] - sFminus[i][j][k];
					if (avg != 0 && diff != 0) {
						twa2[i][j][k] = Math.abs(diff / avg);
						if (twa2[i][j][k] > max)
							max = twa2[i][j][k];
						if (twa2[i][j][k] < min && twa2[i][j][k] > 0) {
							min = twa2[i][j][k];
							Zmin = i;
							Nmin = j;
							tmin = k;
						}
					} else {
						twa2[i][j][k] = -1; // Non-active (F+ = F- = 0 or twa2 =
											// 0)
					}
				}
			}
		}

		// Create frame to display 2D animated plotter. Note that we are passing
		// arrays with 402
		// elements in the time direction (third index) but if only 100
		// timesteps were taken they only
		// need be that long in the time direction. A later fix might save
		// memory, though initial
		// tests suggest otherwise? Note that
		// to determine the lengths of the 3D array twa we can use twa.length
		// (length in the Z
		// direction), twa[0].length (length in the N direction), and
		// twa[0][0].length (length in the
		// time direction), assuming the array to be rectangular.

		ShowIsotopeFlux sif = new ShowIsotopeFlux("Fluxes", "Flux ratio", true,
				min, max, twa2);

	}

	// ----------------------------------------------------------------
	// Method to initiate thread processes
	// ----------------------------------------------------------------

	public void run() {
		while (runThread) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				;
			}
			System.out.println("sleeping");
		}
	}

	// ----------------------------------------------------------------
	// Method to start thread
	// ----------------------------------------------------------------

	public void startThread() {
		runner = new Thread(this);
		runner.setPriority(Thread.NORM_PRIORITY);
		runner.start();
	}

	// -----------------------------------------------------------------------------------------------------
	// Method to read in hydro profile, parse it into variables and store in
	// arrays, and set up spline interpolation tables based on these arrays.
	// -----------------------------------------------------------------------------------------------------

	public void readHydroProfile() {

		String s = null;

		// Create instance of ReadAFile and use it to read in the file,
		// returning file content as a string s.

		ReadAFile raf = new ReadAFile(150000);

		// readASCIIFile method throws IOException, so it must be caught

		try {
			s = raf.readASCIIFile(profileFileName);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		// System.out.println("\nRaw file string:\n\n"+s+"\n");

		// -----------------------------------------------------------------------------
		// Parse the buffer read in from the ascii file and use it
		// to set values for variables.
		// ------------------------------------------------------------------------------

		// Set up temporary arrays to hold parsed input

		int npoints = 3000;
		double[] tempTime = new double[npoints];
		double[] tempT = new double[npoints];
		double[] temprho = new double[npoints];
		int numberColumns = 4;

		// Break string into tokens with whitespace delimiter

		StringTokenizer st = new StringTokenizer(s.trim());

		// Process first line containing column labels. If the first column
		// label is 'Step'
		// (case insensitive) assume that the format is original 4-column with
		// step number,
		// time in seconds, temperature in K, and density in g/cm^3. If the
		// first column label
		// is 'Time' (case insensitive), assume that the format is 3-column:
		// time in seconds,
		// temperature IN UNITS OF T9, and density in g/cm^3.

		String coltest = st.nextToken();
		if (coltest.trim().substring(0, 4).toLowerCase().compareTo("step") == 0) {
			numberColumns = 4;
		} else if (coltest.trim().substring(0, 4).toLowerCase().compareTo(
				"time") == 0) {
			numberColumns = 3;
		} else {
			callExit("Error: 1st line of " + profileFileName
					+ " must start with 'Step' (4-column format)"
					+ " or 'Time' (3-column format). Instead it starts with "
					+ coltest.trim().substring(0, 4));
		}

		for (int i = 0; i < numberColumns - 1; i++) {
			st.nextToken();
		}

		toChar.println();
		toChar.println("Hydro profile after tokenizing and processing:");
		toChar.println();

		// Loop over remaining tokens and process. Notice the methods to convert
		// the
		// string tokens to either integers or doubles.

		int i = 0;

		while (st.hasMoreTokens()) {
			if (numberColumns == 4)
				st.nextToken(); // Skip index column
			tempTime[i] = SegreFrame.stringToDouble(st.nextToken());
			tempT[i] = SegreFrame.stringToDouble(st.nextToken());
			if (numberColumns == 3)
				tempT[i] *= 1e9;
			temprho[i] = SegreFrame.stringToDouble(st.nextToken());
			i++;
		}

		// Now set up actual data arrays (of length i) in the base-10 log of
		// temperature and density

		hydroTime = new double[i];
		hydroT = new double[i];
		hydrorho = new double[i];

		for (int k = 0; k < i; k++) {
			hydroTime[k] = LOG10 * Math.log(tempTime[k]);
			hydroT[k] = LOG10 * Math.log(tempT[k]);
			hydrorho[k] = LOG10 * Math.log(temprho[k]);
			toChar.println("log t=" + hydroTime[k] + " log T=" + hydroT[k]
					+ " log rho=" + hydrorho[k]);
		}

		// -----------------------------------------------------------------------
		// Set up spline interpolators for T and rho as function of time.
		// Do log-log interpolations: log T and log rho vs. log time.
		// -----------------------------------------------------------------------

		// Instantiate two instances of SplineInterpolator to interpolate in
		// temperature and density.

		interpolateT = new SplineInterpolator(); // Interpolate temperature
		interpolaterho = new SplineInterpolator(); // Interpolate density

		// Use SplineInterpolator objects to create second derivative arrays
		// as basis for 1D cubic spline interpolation

		interpolateT.spline(hydroTime, hydroT);
		interpolaterho.spline(hydroTime, hydrorho);

		// Calls to the methods interpolateT.splint(time) and
		// interpolaterho.splint(time) will now interpolate log T and
		// log rho, respectively, as a function of log time.

		// // Test interpolation
		// 
		// double t1 = 2.222e-6;
		// double t2 = 2.2225e-6;
		// double t6 = LOG10*Math.log(t1);
		// double t55 = LOG10*Math.log(t2);
		// double log6 = interpolateT.splint(t6);
		// double T6 = Math.pow(10,log6);
		// double log55 = interpolaterho.splint(t55);
		// double rho55 = Math.pow(10,log55);
		// 
		// toChar.println();
		// toChar.println("Interpolation tests:");
		// toChar.println();
		// toChar.println("t="+t1+" log t="+t6+" log T="+log6+" T="+T6);
		// toChar.println("t="+t2+" log t="+t55+" log rho="+log55+" rho="+rho55);
	}

	// -----------------------------------------------------------------------------------------------------------
	// Method to read in isotopic masses and place in mass array. Presently
	// not using this because we are reading mass excesses with the partition
	// functions in method readpfFile(). Note that there seems to be a
	// discrepancy between the mass excesses read in using this method and
	// those read in using readpfFile(). The latter agrees with the Wapstra
	// mass tables, and is broader in Z and N than the data in the file
	// massFileName, so use it.
	// -----------------------------------------------------------------------------------------------------------

	public void readMassTable() {

		String s = null;

		// Create instance of ReadAFile and use it to read in the file,
		// returning file content as a string s.

		ReadAFile raf = new ReadAFile();

		// readASCIIFile method throws IOException, so it must be caught

		try {
			s = raf.readASCIIFile(massFileName);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		// System.out.println("\nRaw file string:\n\n"+s+"\n");

		// -----------------------------------------------------------------------------
		// Parse the buffer read in from the ascii file and use it
		// to set values for variables.
		// ------------------------------------------------------------------------------

		// Break string into tokens with whitespace delimiter

		StringTokenizer st = new StringTokenizer(s.trim());

		// Process first line: read and ignore 3 tokens (the 3 column labels)

		st.nextToken();
		st.nextToken();
		st.nextToken();

		// Loop over remaining tokens and process. Notice the methods to convert
		// the
		// string tokens to either integers or doubles.

		int i = 0;
		int z, n;
		double tm;

		while (st.hasMoreTokens()) {
			z = Integer.parseInt(st.nextToken());
			n = Integer.parseInt(st.nextToken());
			tm = SegreFrame.stringToDouble(st.nextToken());
			if (z < pmax && n < nmax && tm > 0) {
				masses[z][n] = tm - (double) (z + n) * MEV; // Store mass excess
															// rather than mass
			}
			i++;
		}
		System.out.println("Mass excess table read in from " + massFileName
				+ "; " + i + " mass excesses stored");

	}

	// ---------------------------------------------------------------------
	// Diagnostic method 0. Not currently used.
	// ---------------------------------------------------------------------

	public void diagnostic0() {
		toDiag.println();
		toDiag.println();
		toDiag
				.println("-------------------------------------------------------");
		toDiag.println("Time=" + deci(6, time) + "  dt=" + deci(6, deltaTime)
				+ "  fluxFloor=" + deci(2, fluxFloor));
		toDiag
				.println("-------------------------------------------------------");
	}

	// ---------------------------------------------------------------------
	// Diagnostic method 1. Not currently used.
	// ---------------------------------------------------------------------

	public void diagnostic1() {
		toDiag.println();
		toDiag.println();
		toDiag
				.println("  Z=" + Z + " N=" + N + " Y=" + deci(4, Y[Z][N])
						+ " pop[Z][N]=" + deci(4, pop[Z][N]) + " time="
						+ deci(5, time));
		toDiag.println();
	}

	// ---------------------------------------------------------------------
	// Diagnostic method 2. Not currently used.
	// ---------------------------------------------------------------------

	public void diagnostic2(int i, int j, int nmin) {

		if (doHotStep) {
			String tres = "nr";
			if (RObject[Z][N - nmin][j].resonant)
				tres = "r";
			toDiag.println("     [" + i + " " + j + "]" + " "
					+ RObject[Z][N - nmin][j].reacString + " (" + tres + ")"
					+ " Flux=" + deci(3, flux[i][j]) + " Rate="
					+ deci(3, Rrates[j]));
			if (diagnoseII) {
				toDiag.println("       p_n=" + RObject[Z][N - nmin][j].p0 + " "
						+ RObject[Z][N - nmin][j].p1 + " "
						+ RObject[Z][N - nmin][j].p2 + " "
						+ RObject[Z][N - nmin][j].p3 + " "
						+ RObject[Z][N - nmin][j].p4 + " "
						+ RObject[Z][N - nmin][j].p5 + " "
						+ RObject[Z][N - nmin][j].p6);
			}
		}
	}

	// ---------------------------------------------------------------------
	// Diagnostic method 3. Not currently used.
	// ---------------------------------------------------------------------

	public void diagnostic3() {
		toDiag.println();
		toDiag.println("     Summary Accepted Rescaled Transfers from" + " Z="
				+ Z + " N=" + N + ":");
		toDiag.println();
	}

	// ---------------------------------------------------------------------
	// Diagnostic method 4. Not currently used.
	// ---------------------------------------------------------------------

	public void diagnostic4(int nmin, int k) {
		toDiag.println("        Class="
				+ RObject[Z][N - nmin][tempIndex[k]].reacIndex + "   "
				+ RObject[Z][N - nmin][tempIndex[k]].reacString + "   popOut="
				+ deci(4, popOut[k]));
	}

	// ---------------------------------------------------------------------
	// Diagnostic method 5. Not currently used.
	// ---------------------------------------------------------------------

	public void diagnostic5(int kk) {
		if (diagnoseI && doHotStep) {
			toDiag.println("           Accepted: " + "  popOut="
					+ deci(4, popOut[kk - 1]));
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// Diagnostic method to output selected rates and fluxes. Method not
	// currently being used.
	// ------------------------------------------------------------------------------------------------------------------------------------

	public void rateOtter(int Z, int N, int max, String lab) {
		int zarg = Z - pmin;
		int narg = N - minNetN[Z];
		System.out.println(" " + lab + " Z=" + Z + " N=" + N + " Pop="
				+ deci(3, pop[Z][N]) + " dt=" + deci(3, deltaTime));

		// Following commented out because this needs to be fixed to be
		// consistent with new faster rate calculation
		// that computes log and powers of T9 only once per timestep

		// for(int rnum=0; rnum<max; rnum++){
		// System.out.println("   "+RObject[zarg][narg][rnum].reacString
		// +"  R="+ deci(3,RObject[zarg][narg][rnum].rate(T9))
		// +" R(1/s)="+deci(3,RObject[zarg][narg][rnum].prob(T9,rho,Ye))
		// +" flux="+deci(3,RObject[zarg][narg][rnum].prob(T9,rho,Ye)*deltaTime)
		// );
		// }

		System.out.println("   " + tempstring1);
	}

	// ------------------------------------------------------------------------------------------------
	// Method to output information at the end of a printout timestep
	// ------------------------------------------------------------------------------------------------

	public void timeStepOutput(double protNumber, double neutNumber) {

		String st1;

		if (tintNow == 0)
			totalTimeStepsZero = totalTimeSteps;

		st1 = "t="
				+ deci(8, time)
				+ " logt="
				+ deci(6, LOG10 * Math.log(time))
				+ " dt="
				+ deci(4, deltaTimeRestart)
				+ " dt'="
				+ deci(4, dtAvg())
				+ " "
				+ (tintNow)
				+ "/"
				+ (numdt - 1)
				+ " "
				+ deci(
						1,
						((double) (System.currentTimeMillis() - computeTime) / 1000))
				+ " s";
		System.out.println(st1);
		toChar.println();
		toChar.println(st1);

		st1 = "(Z+N)/nT=" + deci(4, ((protNumber + neutNumber) / nT)) + " Z/N="
				+ deci(4, protNumber / neutNumber);

		st1 += " T=" + deci(2, T9 * 1e9) + " rho=" + deci(2, rho) + " Links="
				+ couplingCounter + " Boxes=" + currentNonZero() + "("
				+ totalBoxesPopulated() + ")";

		System.out.println(st1);
		toChar.println(st1);

		st1 = "H=" + deci(3, pop[1][0] / nT) + " N=" + deci(3, pop[0][1] / nT)
				+ " 4He=" + deci(3, (4 * pop[2][2] / nT)) + " 12C="
				+ deci(3, 12 * pop[6][6] / nT) + " 16O="
				+ deci(3, 16 * pop[8][8] / nT) + " 20Ne="
				+ deci(3, 20 * pop[10][10] / nT);

		System.out.println(st1);
		toChar.println(st1);

		st1 = "24Mg="
				+ deci(3, 24 * pop[12][12] / nT)
				// +" 26Al="+deci(3,26*pop[13][13]/nT)
				// +" 27Si="+deci(3,(27*pop[14][13]/nT))
				+ " 28Si=" + deci(3, 28 * pop[14][14] / nT) + " 32S="
				+ deci(3, 32 * pop[16][16] / nT) + " 36Ar="
				+ deci(3, 36 * pop[18][18] / nT) + " 40Ca="
				+ deci(3, 40 * pop[20][20] / nT) + " 44Ti="
				+ deci(3, 44 * pop[22][22] / nT)
		// +" 15N="+deci(3,15*pop[7][8]/nT)
		// +" 15O="+deci(3,15*pop[8][7]/nT)
		;

		System.out.println(st1);
		toChar.println(st1);

		st1 = "48Cr=" + deci(3, 48 * pop[24][24] / nT) + " 52Fe="
				+ deci(3, 52 * pop[26][26] / nT) + " 54Fe="
				+ deci(3, 54 * pop[26][28] / nT) + " 56Fe="
				+ deci(3, 56 * pop[26][30] / nT) + " 56Ni="
				+ deci(3, 56 * pop[28][28] / nT);

		System.out.println(st1);
		toChar.println(st1);

		st1 =
		// +" Over="+ restartTimeSteps + " Rat="
		// +deci(2,((double)restartTimeSteps/(double)totalTimeSteps))
		"dE/dt=" + deci(3, (ECON * dERelease / nT)) + " dE/dt_mass="
				+ deci(3, (ECON * dEReleaseA / nT / deltaTime)) + " sumE="
				+ deci(3, (ECON * ERelease / nT)) + " sumE_mass="
				+ deci(3, (ECON * EReleaseA / nT));

		System.out.println(st1);
		toChar.println(st1);
	}

	// ------------------------------------------------------------------------------------------------------------------------
	// Method to output selected fluxes for plotting. The toPlot method writes
	// to the
	// file fluxes.out. This method is not presently called.
	// ------------------------------------------------------------------------------------------------------------------------

	public void plotAlphaReactionFluxes() {

		int Z, N, nmin, i, j, k1, k2;
		double f1, f2, f3, f4, f5, f6, f7, f8, f9, f10; // Forward fluxes
		double b1, b2, b3, b4, b5, b6, b10; // Reverse fluxes
		double d1, d2, d3, d4, d5, d6, d10; // (Forward - Reverse) fluxes
		String s1, s2, s3, s4, s5, s6, s7, s8, s9, s10; // Identifier string
		String s = "/";

		// Fluxes are either in X (mass fraction) per second or Y (mole
		// fraction) per second
		// depending on which statements are commented out.

		// 16O<->20Ne pair

		Z = 8;
		N = 8;
		nmin = minNetN[Z];
		i = Z;
		j = N - nmin;
		k1 = serialLookup(Z, N, 6);
		k2 = serialLookup(Z, N, 7);
		// Calculate as X
		f3 = (RObject[i][j][k1].prob(T9, rho, Ye) + RObject[i][j][k2].prob(T9,
				rho, Ye))
				* pop[Z][N] * 20 / nT;
		f3 /= 20; // Convert to Y
		s3 = RObject[i][j][k1].reacString;
		Z = 10;
		N = 10;
		nmin = minNetN[Z];
		i = Z;
		j = N - nmin;
		k1 = serialLookup(Z, N, 3);
		k2 = serialLookup(Z, N, 4);
		b3 = (RObject[i][j][k1].prob(T9, rho, Ye) + RObject[i][j][k2].prob(T9,
				rho, Ye))
				* pop[Z][N] * 20 / nT;
		b3 /= 20;
		s3 = s3 + s + RObject[i][j][k1].reacString + ": ";
		d3 = f3 - b3;
		// s4 = "16O<->20Ne: ";

		// 20Ne<->24Mg pair

		Z = 10;
		N = 10;
		nmin = minNetN[Z];
		i = Z;
		j = N - nmin;
		k1 = serialLookup(Z, N, 9);
		k2 = serialLookup(Z, N, 10);
		f4 = (RObject[i][j][k1].prob(T9, rho, Ye) + RObject[i][j][k2].prob(T9,
				rho, Ye))
				* pop[Z][N] * 20 / nT;
		f4 /= 20;
		s4 = RObject[i][j][k1].reacString;
		Z = 12;
		N = 12;
		nmin = minNetN[Z];
		i = Z;
		j = N - nmin;
		k1 = serialLookup(Z, N, 3);
		k2 = serialLookup(Z, N, 4);
		b4 = (RObject[i][j][k1].prob(T9, rho, Ye) + RObject[i][j][k2].prob(T9,
				rho, Ye))
				* pop[Z][N] * 20 / nT;
		b4 /= 20;
		s4 = s4 + s + RObject[i][j][k1].reacString + ": ";
		d4 = f4 - b4;
		// s4 = "20Ne<->24Mg: ";

		// 12C + 12 C -> 4He + 20Ne

		Z = 6;
		N = 6;
		nmin = minNetN[Z];
		i = Z;
		j = N - nmin;
		k1 = serialLookup(Z, N, 21);
		f7 = RObject[i][j][k1].prob(T9, rho, Ye) * pop[Z][N] * 20 / nT;
		f7 /= 20;
		s7 = RObject[i][j][k1].reacString + ": ";

		// 12C + 20Ne <-> 4He + 28Si pair

		Z = 10;
		N = 10;
		nmin = minNetN[Z];
		i = Z;
		j = N - nmin;
		k1 = serialLookup(Z, N, 20);
		f10 = RObject[i][j][k1].prob(T9, rho, Ye) * pop[Z][N] * 20 / nT;
		f10 /= 20;
		s10 = RObject[i][j][k1].reacString;
		Z = 14;
		N = 14;
		nmin = minNetN[Z];
		i = Z;
		j = N - nmin;
		k1 = serialLookup(Z, N, 21);
		b10 = RObject[i][j][k1].prob(T9, rho, Ye) * pop[Z][N] * 20 / nT;
		b10 /= 20;
		s10 = s10 + s + RObject[i][j][k1].reacString + ": ";
		d10 = f10 - b10;
		// s4 = "20Ne<->24Mg: ";

		double neTotal = f3 - b3 - f4 + b4 + f7 - f10 + b10;

		System.out
				.println("\n" + "steps=" + totalTimeSteps + " t_n="
						+ deci(8, time) + " dt=" + deci(8, deltaTime)
						+ " t_(n+1)=" + deci(8, (time + deltaTime)) + "\nf3="
						+ deci(6, f3) + " b3=" + deci(6, b3) + " f4="
						+ deci(6, f4) + " b4=" + deci(6, b4) + "\nf7="
						+ deci(6, f7) + " f10=" + deci(6, f10) + " b10="
						+ deci(6, b10) + "\nd3=" + deci(6, d3) + " d4="
						+ deci(6, d4) + "\nNeIn="
						+ deci(6, (f3 + b4 + f7 + b10)) + " NeOut="
						+ deci(6, (b3 + f4 + f10)) + " NeNet="
						+ deci(6, neTotal) + " NeNet_norm="
						+ deci(6, neTotal / (-54809.648301)) + "\nf3*dt="
						+ deci(7, f3 * deltaTime) + " b3*dt="
						+ deci(7, b3 * deltaTime) + " f4*dt="
						+ deci(7, f4 * deltaTime) + " b4*dt="
						+ deci(7, b4 * deltaTime) + "\nf7*dt="
						+ deci(7, f7 * deltaTime) + " f10*dt="
						+ deci(7, f10 * deltaTime) + " b10*dt="
						+ deci(7, b10 * deltaTime) + " d3*dt="
						+ deci(7, d3 * deltaTime) + "\nd4*dt="
						+ deci(7, d4 * deltaTime) + " NeIn="
						+ deci(7, ((f3 + b4 + f7 + b10) * deltaTime))
						+ " NeOut=" + deci(7, ((b3 + f4 + f10) * deltaTime))
						+ " NeNet=" + deci(7, neTotal * deltaTime)
						+ "\nX_20Ne=" + deci(6, 20 * pop[10][10] / nT)
						+ " X_16O=" + deci(6, 16 * pop[8][8] / nT) + " X_24Mg="
						+ deci(6, 24 * pop[12][12] / nT) + " X_12C="
						+ deci(6, 12 * pop[6][6] / nT) + " X_4He="
						+ deci(6, 4 * pop[2][2] / nT) + "\nY_20Ne="
						+ deci(6, pop[10][10] / nT) + " Y_16O="
						+ deci(6, pop[8][8] / nT) + " Y_24Mg="
						+ deci(6, pop[12][12] / nT) + " Y_12C="
						+ deci(6, pop[6][6] / nT) + " Y_4He="
						+ deci(6, pop[2][2] / nT));
	}

	// --------------------------------------------------------------------------------------------------------------
	// Method to find index m for RObject[Z][N-nmin][m] (where
	// nmin=minNetN[Z]), corresponding to the serial index ser. The
	// serial index stored in serialIndex[Z][N][i] is the unique global index
	// that identifies a reaction in ReactionClass1 objects serialized to
	// the disk for a given Z and N. RObject[][][] is an array of ReactionClass1
	// objects, but since not all reactions for a given Z and N are necessarily
	// read in for a given calculation, the third index of RObject[][][] is not
	// generally the serial index. This method finds the location in the
	// RObject[][][] array of a particular reaction characterized by the serial
	// index ser. The serial index for all reactions for an isotope (Z,N) can be
	// displayed by running
	// java LoadR1 data/isoZ_N.ser
	// assuming the serialized files to be in the subdirectory named data.
	// ---------------------------------------------------------------------------------------------------------------

	public int serialLookup(int Z, int N, int ser) {
		for (int i = 0; i < 50; i++) {
			if (ser == serialIndex[Z][N][i])
				return i;
		}
		return -1; // If no match
	}

	// -----------------------------------------------------------------------------------------------------------
	// Method to return the current differential population transfer dpop[Z][N]
	// -----------------------------------------------------------------------------------------------------------

	static double dpopNow(int Z, int N) {
		return dpopPlus[Z][N] - dpopMinus[Z][N];
	}

	// -------------------------------------------------------------------------------------------------
	// Method to write fluxes to the screen and file stochastic.tmp for
	// isotope (z,n)
	// -------------------------------------------------------------------------------------------------

	static void writeTheFluxes(int z, int n) {

		String tempSt = "F(" + z + "," + n + ")="
				+ deci(4, (Fplus[z][n] - Fminus[z][n]) / nT) + " F+="
				+ deci(4, Fplus[z][n] / nT) + " F-="
				+ deci(4, Fminus[z][n] / nT) + " Y=" + deci(3, Y[z][n]) + " X="
				+ deci(3, Y[z][n] * AA[z][n]) + " Fdt/Y="
				+ deci(3, dpop[z][n] / pop[z][n]) + " kdt="
				+ deci(3, keff[z][n] * deltaTime);
		System.out.println(tempSt);
		toChar.println(tempSt);
	}

	// ----------------------------------------------------------------------------------------------------------------------------
	// Method to write log10 of fluxes to file fluxesQSS.out for isotope (z, n)
	// with " " delimiter.
	// ----------------------------------------------------------------------------------------------------------------------------

	static String fluxFiler(int z, int n) {
		int dp = 4;
		double defaultf = 1e-25;
		double f1 = defaultf;
		double f2 = defaultf;
		double f3 = defaultf;
		double f4 = defaultf;
		double f5 = defaultf;
		double f6 = defaultf;
		double f7 = defaultf;
		double f8 = defaultf;

		// Be sure everything is positive before taking logs

		if (Fplus[z][n] > 0) {
			f1 = Fplus[z][n] / nT;
		}
		if (Fminus[z][n] > 0) {
			f2 = Fminus[z][n] / nT;
		}
		if (dpop[z][n] != 0) {
			f3 = Math.abs(Fplus[z][n] - Fminus[z][n]) / nT;
		}
		if (Y[z][n] > 0) {
			f4 = Y[z][n];
		}
		if (keff[z][n] > 0)
			f5 = keff[z][n];
		if (term1(z, n) > 0)
			f6 = term1(z, n) / nT;
		if (term1(z, n) - term2(z, n, deltaTime) > 0)
			f7 = term1(z, n) / nT - term2(z, n, deltaTime) / nT;
		f8 = keff[z][n] * tstep[tintNow];

		return deci(dp, LOG10 * Math.log(f1)) + " "
				+ deci(dp, LOG10 * Math.log(f2)) + " "
				+ deci(dp, LOG10 * Math.log(f3)) + " "
				+ deci(dp, LOG10 * Math.log(f4)) + " "
				+ deci(dp, LOG10 * Math.log(f5)) + " "
				+ deci(dp, LOG10 * Math.log(f6)) + " "
				+ deci(dp, LOG10 * Math.log(f7)) + " "
				+ deci(dp, LOG10 * Math.log(f8)) + " ";
	}

	// ---------------------------------------------------------------------------------------------------------------------------------
	// Method to return string that is log10 of quantities for asymptotic
	// approx. It is not
	// presently called from anywhere.
	// ---------------------------------------------------------------------------------------------------------------------------------

	static String asyFiler(int z, int n) {
		int dp = 4;
		double defaultf = 1e-25;
		double f5 = defaultf;
		double f6 = defaultf;
		double f7 = defaultf;
		double f8 = defaultf;

		// Be sure everything is positive before taking logs

		if (keff[z][n] > 0)
			f5 = keff[z][n];
		if (term1(z, n) > 0)
			f6 = term1(z, n) / nT;
		if (term1(2, 2) / nT - term2(2, 2, deltaTime) / nT > 0)
			f7 = term1(2, 2) / nT - term2(2, 2, deltaTime) / nT;
		f8 = keff[z][n] * deltaTime;

		return deci(dp, LOG10 * Math.log(f5)) + " "
				+ deci(dp, LOG10 * Math.log(f6)) + " "
				+ deci(dp, LOG10 * Math.log(f7)) + " "
				+ deci(dp, LOG10 * Math.log(f8)) + " ";
	}

	// ---------------------------------------------------------------------------------------------------------
	// Method to output information for isotopes being treated asymptotically.
	// Not currently called.
	// ---------------------------------------------------------------------------------------------------------

	public void showAsymptotic(int z, int n) {
		String tempSt = "";
		if (isAsymptotic[z][n]) {
			tempSt = " (" + z + "," + n + "): kdt="
					+ deci(4, (keff[z][n] * deltaTime)) + " t1="
					+ deci(4, term1(z, n) / nT) + " t2="
					+ deci(4, term2(z, n, deltaTime) / nT) + " asy="
					+ deci(4, (Y[z][n]));
			System.out.println(tempSt);
		}
	}

	// -----------------------------------------------------------------------------------------------------
	// Method to initialize pop and dpop arrays if timestep is to be retried
	// -----------------------------------------------------------------------------------------------------

	public void cleanUpPops() {
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				dpopPlus[i][j] = 0;
				dpopMinus[i][j] = 0;
				dpop[i][j] = 0;
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------
	// Method to keep track of last 10 values of deltaTime. Called with
	// the current value of the timestep dtNow as argument.
	// ---------------------------------------------------------------------------------------------------

	public void stackdt(double dtNow) {

		// Shift all entries in the array down by one position
		// and add the latest timestep to the top position.

		for (int i = 0; i < numberStacked - 1; i++) {
			timestepStacker[i] = timestepStacker[i + 1];
		}
		timestepStacker[numberStacked - 1] = dtNow;
	}

	// --------------------------------------------------------------------------------------------------
	// Method to return current mass fraction of isotope (z,n). Assumes
	// that pop has been updated for this timestep.
	// --------------------------------------------------------------------------------------------------

	public double massFrac(int z, int n) {
		return (pop[z][n]) * AA[z][n] / nT;
	}

	// -------------------------------------------------------------------------------------------------------------------
	// Method to return the current total mass (in MeV) of all isotopes in the
	// network.
	// -------------------------------------------------------------------------------------------------------------------

	public double returnNetworkMass() {
		double sumM = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				sumM += pop[i][j] * masses[i][j];
			}
		}
		return sumM;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Method to display 3D plot windows at the end of the calculation or output
	// files for 3D rateviewer to read later.
	// -------------------------------------------------------------------------------------------------------------

	public void display3DPlots() {

		lower3DCutoff = minLogContour;
		pmax3D = pmax - 1;
		nmax3D = nmax - 1;

		// Create 3D Plot or Write plot data (for rateviewer3D) to file

		double[][][] plot3DAbundance = new double[(int) pmax + 1][(int) nmax + 1][numdt];
		double[] plot3DAbundanceTime = new double[numdt];
		double sfactor = 1;

		// Update progress meter

		if (write3DOutput) {
			if (SegreFrame.prom != null) {
				SegreFrame.prom.sets1("Writing 3D plot data");
				SegreFrame.prom.sets2(" ");
			}
		}

		// Creating arrays for parameters for 3D Plot class

		for (int i = 0; i <= pmax3D; i++) {
			for (int j = 0; j <= nmax3D; j++) {
				for (int k = 0; k < numdt; k++) {
					if (showMassFraction)
						sfactor = AA[i][j];
					plot3DAbundance[i][j][k] = sfactor * intPop[i][j][k] / f;
				}
			}
		}

		for (int k = 0; k < numdt; k++) {
			plot3DAbundanceTime[k] = timeNow[k];
		}

		// if write3DOutput is true, write the data to filename held in variable
		// outfile3D;
		// else, display 3D Plot

		if (write3DOutput) {
			try {
				PrintWriter write3DOutputStream = new PrintWriter(
						new FileWriter(outfile3D));

				// Printing output to File. First print header, then print data

				write3DOutputStream.println(numdt + " " + pmax3D + " " + nmax3D
						+ " " + lower3DCutoff);

				for (int k = 0; k < numdt; k++) {
					for (int i = 0; i <= pmax3D; i++) {
						for (int j = 0; j <= nmax3D; j++) {
							write3DOutputStream.println(k + " "
									+ plot3DAbundanceTime[k] + " " + i + " "
									+ j + " " + plot3DAbundance[i][j][k]);
							if (SegreFrame.prom != null)
								SegreFrame.prom.sets2("Time interval = " + k);
						}
					}
				}
				write3DOutputStream.flush();
				write3DOutputStream.close();
				System.out
						.println("  --> 3D plot file written to " + outfile3D);
			} catch (IOException e) {
				System.out.println("File to write to output file " + outfile3D
						+ " " + e);
			}

		} else {
			// new MainFrame(new elementProto(plot3DAbundance,
			// plot3DAbundanceTime, pmax3D, nmax3D, lower3DCutoff), 800, 700);
		}
	}

	// --------------------------------------------------------------------------------------------
	// Method to output restart file
	// --------------------------------------------------------------------------------------------

	private void writeRestart() {

		// Output final Y abundances for restart file. Note that N is
		// first and Z is second in list. Also output total energy released
		// to this point ERelease (the energy per test particle is ERelease/nT).
		// Restart file is output as restart.out. This may be used as an input
		// file for a new run (see the file AbundanceData.java).

		numberIsotopesPopulated = 0;

		// Following 5 lines delete the restart file at each plot output step
		// and replace it with a new one corresponding to the current plot
		// output step. If these lines are commented out the restart
		// file restart.out will have restarts for ALL plot output steps
		// appended one after the other.

		toRestart.close();
		try {
			to4 = new FileOutputStream("output/restart.out");
		} catch (IOException e) {
			;
		}
		toRestart = new PrintWriter(to4);

		toRestart.println("Y(Restart:logt=" + deci(6, LOG10 * Math.log((time)))
				+ ")");

		toRestart.println((time) + " " + deltaTimeRestart + " " + ERelease
				+ " " + stochasticFactor + " " + massTol + " " + Ymin + " "
				+ nT);

		toRestart.println("n  z  Y");
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (Math.abs(pop[i][j] / nT) > Ymin) {
					toRestart.println(j + "  " + i + "  " + pop[i][j] / nT); // N,Z,Y(Z,N)
					numberIsotopesPopulated++;
				}
			}
		}
		toRestart.flush();
	}

	// ---------------------------------------------------------------------------------------------------
	// Method to output reduced network file. If writeCompiledReduced is
	// false the file is generated for the Java code. If it is true, a default
	// file is generated for the compiled code that includes fields for
	// spin, binding energy, and partition function information.
	// ---------------------------------------------------------------------------------------------------

	private void writeReducedNetwork() {

		String s;
		int A;
		String pf2;
		String pf3;
		String pf4;

		int count = 0;

		if (!writeCompiledReduced)
			toReduced.println("Z  N");

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (Math.abs(maxpop[i][j] / nT) > reducedNetYcut) {
					if (writeCompiledReduced) {
						A = i + j;
						if (i == 0) {
							s = "n";
						} else {
							s = IsotopePad.returnSymbol(i).toLowerCase();
							s += Integer.toString(A);
						}
						s += (" " + Integer.toString(A));
						s += (" " + Integer.toString(i));
						s += (" " + Integer.toString(j));
						s += (" 0.0 " + Double.toString(masses[i][j]));

						// Add partition functions
						pf2 = pf3 = pf4 = "";
						for (int k = 0; k < 8; k++) {
							pf2 += (Double.toString(pf[i][j][k]) + " ");
							pf3 += (Double.toString(pf[i][j][k + 8]) + " ");
							pf4 += (Double.toString(pf[i][j][k + 16]) + " ");
						}

						s += ("\n" + pf2.trim());
						s += ("\n" + pf3.trim());
						s += ("\n" + pf4.trim());
					} else {
						s = i + " " + j;
					}
					toReduced.println(s);
					count++;
				}
			}
		}
		System.out
				.println("\n  --> Reduced network written to output/reduced.out: "
						+ count + " isotopes");
	}

	// ---------------------------------------------------------------------------------------------------
	// Method to output network file in format for F90 calculation.
	// ---------------------------------------------------------------------------------------------------

	private void writeCompiledNetwork() {

		String s;
		int A;
		String pf2;
		String pf3;
		String pf4;

		int count = 0;

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (IsotopePad.isoColor[i][j]) {

					A = i + j;
					if (i == 0) {
						s = "n";
					} else {
						s = IsotopePad.returnSymbol(i).toLowerCase();
						s += Integer.toString(A);
					}
					s += (" " + Integer.toString(A));
					s += (" " + Integer.toString(i));
					s += (" " + Integer.toString(j));
					s += (" 0.0 " + Double.toString(masses[i][j]));

					// Add partition functions
					pf2 = pf3 = pf4 = "";
					for (int k = 0; k < 8; k++) {
						pf2 += (Double.toString(pf[i][j][k]) + " ");
						pf3 += (Double.toString(pf[i][j][k + 8]) + " ");
						pf4 += (Double.toString(pf[i][j][k + 16]) + " ");
					}

					s += ("\n" + pf2.trim());
					s += ("\n" + pf3.trim());
					s += ("\n" + pf4.trim());

					toNet.println(s);
					count++;
				}
			}
		}
		toNet.flush();
		System.out.println("\n  --> Compiled network written");
	}
	
	// ---------------------------------------------------------------------------------------------------
	// THIS METHOD NOW DEPRECATED.  REPLACED BY FOLLOWING METHOD writeCUCAnetworkV().
	// Method to output network file in format for CUDA calculation.
	// ---------------------------------------------------------------------------------------------------

	private void writeCUDAnetwork() {
		
		// *****DEPRECATED.  See writeCUDAnetworkV() below. ***** //

		String s;
		int A;
		String pf2;
		String pf3;
		String pf4;

		int count = 0;

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (IsotopePad.isoColor[i][j]) {

					A = i + j;
					if (i == 0) {
						s = "n";
					} else {
						s = IsotopePad.returnSymbol(i).toLowerCase();
						s += Integer.toString(A);
					}
					s += (" " + Integer.toString(A));
					s += (" " + Integer.toString(i));
					s += (" " + Integer.toString(j));
					s += (" " + Double.toString(Y[i][j]));
					s += (" " + Double.toString(masses[i][j]));

					// Add partition functions
					pf2 = pf3 = pf4 = "";
					for (int k = 0; k < 8; k++) {
						pf2 += (Double.toString(pf[i][j][k]) + " ");
						pf3 += (Double.toString(pf[i][j][k + 8]) + " ");
						pf4 += (Double.toString(pf[i][j][k + 16]) + " ");
					}

					s += ("\n" + pf2.trim());
					s += ("\n" + pf3.trim());
					s += ("\n" + pf4.trim());

					toCUDAnet.println(s);
					count++;
				}
			}
		}
		toCUDAnet.flush();
		System.out.println("\n  --> CUDA network written in output/CUDAnet.inp\n");
	}
	
	// ---------------------------------------------------------------------------------------------------
	// Method to output network file in format for CUDA calculation.
	// ---------------------------------------------------------------------------------------------------

	private void writeCUDAnetworkV() {

		String s;
		int A;
		String pf2;
		String pf3;
		String pf4;
		String symbol;
		int count = 0;
		int i, j;

		for(int m=0; m<numberActiveIsotopes; m++){
			i = netVector[m].Z;
			j = netVector[m].N;
			A = (int) netVector[m].A;
			symbol = netVector[m].symbol;
			s = symbol;
			s += (" " + Integer.toString(A));
			s += (" " + Integer.toString(i));
			s += (" " + Integer.toString(j));
			s += (" " + Double.toString(Y[i][j]));
			s += (" " + Double.toString(masses[i][j]));

			// Add partition functions
			pf2 = pf3 = pf4 = "";
			for (int k = 0; k < 8; k++) {
				pf2 += (Double.toString(pf[i][j][k]) + " ");
				pf3 += (Double.toString(pf[i][j][k + 8]) + " ");
				pf4 += (Double.toString(pf[i][j][k + 16]) + " ");
			}

			s += ("\n" + pf2.trim());
			s += ("\n" + pf3.trim());
			s += ("\n" + pf4.trim());

			toCUDAnet.println(s);
			count++;
		}
				

		toCUDAnet.flush();
		System.out.println("\n  --> CUDA network written in output/CUDAnet.inp\n");
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Method to count total isotopes that have had non-zero population during
	// the whole calculation.
	// -----------------------------------------------------------------------------------------------------------------

	static int totalBoxesPopulated() {

		int count = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (hasBeenPopulated[i][j])
					count++;
			}
		}
		return count;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Method to count total isotopes that currently have non-zero populations
	// -----------------------------------------------------------------------------------------------------------------

	static int currentNonZero() {

		int count = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (pop[i][j] != 0)
					count++;
			}
		}
		return count;
	}

	// --------------------------------------------------------------------------------------------
	// Method to return string to display current date and time
	// --------------------------------------------------------------------------------------------

	static String timeDateString() {

		// Date string
		Date now = new Date();
		DateFormat df = DateFormat.getDateInstance();
		String sss = df.format(now);

		// Time string (24-hour format)
		String hrs, mins, secs;
		int hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		hrs = Integer.toString(hr);
		if (hr < 10)
			hrs = "0" + hrs;

		int min = Calendar.getInstance().get(Calendar.MINUTE);
		mins = Integer.toString(min);
		if (min < 10)
			mins = "0" + mins;

		int sec = Calendar.getInstance().get(Calendar.SECOND);
		secs = Integer.toString(sec);
		if (sec < 10)
			secs = "0" + secs;

		return hrs + ":" + mins + ":" + secs + " " + sss;

	}

	// ----------------------------------------------------------------------------------------------------------
	// Method to set up output strings summarizing parameters of calculation
	// ----------------------------------------------------------------------------------------------------------

	private void outStrings() {

		String tempis = StochasticElements.timeDateString() + "|" + hostName
				+ "|" + calcModeString;
		
		String tempis2;

		if (myComment.compareTo("(Optional comment)") != 0
				&& myComment.compareTo("") != 0
				&& myComment.compareTo("Optional") != 0) {
			tempis += ("|" + myComment);
		}
		os1 = tempis;

		tempis = "t=" + deci(1, (double) computeTime / 1000) + "s";
		if (useCustomTimestepper) {
			tempis += ("|Custom SF & dX");
		} else {
			tempis += ("|SF=" + stochasticFactor);
			tempis += ("|dX=" + massTol);
		}
		tempis += ("|Ymin=" + Ymin);
		tempis += ("|Iso=" + boxPopuli + "/" + numberActiveIsotopes) + "/"
				+ numberAsymptotic;
		tempis += ("|sumX=" + deci(4, sumX));
		tempis += ("|E/A=" + deci(4, ERelease / nT));
		tempis += ("|normX=" + renormalizeMassFractions);
		os2 = tempis;

		if (constantHydro) {
			tempis = ("T9=" + T9 + "|rho=" + rho);
			tempis2 = ("T9=" + T9 + " rho=" + rho);
		} else {
			tempis = ("Hydro=" + profileFileName);
			tempis2 = tempis;
		}

		switch (isoSelectionMode) {

		case 1:
			tempis += "|Net=all";
			tempis2 += " Net=all";
			break;

		case 2:
			tempis += "|Net=mouse shift-click";
			tempis2 += " Net=mouse shift-click";
			break;

		case 3:
			tempis += "|Net=" + activeFileName;
			tempis2 += " Net=" + activeFileName;
			break;
		}

		switch (rateSelectionMode) {

		case 1:
			tempis += "|Rates=all";
			tempis2 += " Rates=all";
			break;

		case 2:
			tempis += "|Rates=mouse ctrl-click";
			tempis2 += " Rates=mouse ctrl-click";
			break;

		case 3:
			tempis += "|Rates=" + activeRatesFileName;
			tempis2 += " Rates=" + activeRatesFileName;
			break;
		}

		switch (initAbundMode) {

		case 1:
			tempis += "|InitY=individual";
			tempis2 += " InitY=individual";
			break;

		case 2:
			tempis += "|InitY=" + abundFileName;
			tempis2 += " InitY=" + abundFileName;
			break;

		case 3:
			tempis += "|InitY=solar";
			tempis2 += " InitY=solar";
			break;
		}
		
		os3 = tempis;
		os4 = tempis2;
		
		if(imposeEquil){
			tempis = "|eqTime="+deci(1,equilibrateTime)+"|tol="+deci(3,equiTol);
			tempis = tempis + "|equil:"+totalEquilReactions+"/"+totalReactions;
			os5 = tempis;
		}
	}

	// --------------------------------------------------------------------------------------------------
	// Update fastest and slowest rates encountered so far
	// --------------------------------------------------------------------------------------------------

	private void fastSlowRates(int j, int Zindex, int Nindex, double testRate) {
		if (testRate > fastestCurrentRate && pop[Z][N] > 0) {
			fastestCurrentRate = testRate;
			maxRateString = RObject[Zindex][Nindex][j].reacString;
		}
		if (testRate > 0 && testRate < slowestCurrentRate && pop[Z][N] > 0) {
			slowestCurrentRate = testRate;
			minRateString = RObject[Zindex][Nindex][j].reacString;
		}
		if (fastestCurrentRate == 1e-30)
			maxRateString = "All rates zero";
	}

	// ------------------------------------------------------------------------------------------
	// Method to count total reactions for given seed isotope
	// ------------------------------------------------------------------------------------------

	private void countReactions() {
		if (reacCounter < 20) {
			tempReacNum[reacCounter]++;
		} else {
			tempReacNum[19]++;
		}
		reacCounter = 0;
	}

	// ----------------------------------------------------------------------------------------------------------------------
	// Method to return the sum of mass fractions on the grid. This
	// method is not currently being used except for diagnostics.
	// ----------------------------------------------------------------------------------------------------------------------

	private double sumMassFractions() {
		double sumX = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				sumX += pop[i][j] * AA[i][j];
			}
		}
		sumX /= nT;
		return sumX;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Method to return the sum of mass fractions for isotopes not participating
	// in partial equilibrium
	// -------------------------------------------------------------------------------------------------------------------------------------------

	private double sumXEquil() {
		double temp = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (isotopeInEquil[i][j]) {
					temp += Y[i][j] * AA[i][j];
				}
			}
		}
		return temp;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Method to return the sum of mass fractions for isotopes participating in
	// partial equilibrium
	// -------------------------------------------------------------------------------------------------------------------------------------------

	private double sumXNotEquil() {
		double temp = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (!isotopeInEquil[i][j]) {
					temp += Y[i][j] * AA[i][j];
				}
			}
		}
		return temp;
	}

	// -----------------------------------------------------------------------------------------------------------------------------
	// Method to advance the network from time to time + dt.
	// First it finds appropriate timestep based on last timestep and flux
	// considerations,
	// on conservation of mass fraction to tolerance, and finally on ensuring
	// that the
	// timestep is not larger than the distance to the next plot output time. It
	// returns the
	// correct dt for this step. In the process it fills the pop[][] arrays with
	// the
	// correct updated populations, and places in the class variable
	// deltaTimeRestart
	// the proper trial dt to start with in the next integrations step
	// (deltaTimeRestart is
	// equal to the dt returned by the method unless at a plot output interval).
	// When this
	// method returns dt the populations have been updated to the appropriate
	// values at
	// the time time+dt.
	// -----------------------------------------------------------------------------------------------------------------------------

	private double advanceOneTimestep() {

		double dt;
		double test1, test2;

		// Find the isotope in population changing most rapidly
		maxFlux();

		// Compute effective decay constants needed for asymptotic approximation
		computekeff();

		// If custom timestepping, reset stochasticFactor and massTol according
		// to the current time
		if (useCustomTimestepper)
			customizeTimestepper();

		// First estimate trial dt based on dt from previous step and flux
		// considerations
		dtFlux = Math.min(0.1 * time, totalSeeds * stochasticFactor / maxdpopp); // This
																					// is
																					// the
																					// usual
																					// prescription
		// dtFlux = Math.min(0.1*time, nT*stochasticFactor/maxdpopp);
		// dtFlux = Math.min(0.1*time, nT*stochasticFactor/maxFluxRG); //
		// Doesn't work as well
		dt = Math.min(dtFlux, deltaTimeRestart); // This is the usual
													// prescription
		// dt = 0.5*(dtFlux+deltaTimeRestart);

		if (checkPC)
			System.out.println("t=" + deci(5, time) + " dt=" + deci(4, dt)
					+ " Initial trial population update:");

		// if(time <1e-10) dt = 0.7*time;
		// if(time > 1e21) dt = 0.2*time;

		// Update populations based on this trial timestep
		doPopulationUpdate(dt);

		// Alter timestepping for partial equil according to magnitude of
		// mostDevious from last timestep
		if (imposeEquil && time > equilibrateTime) {
			double deviousMax = 0.5;
			double deviousMin = 0.1;
			plotDevious = mostDevious; // For the toPlot output stream
			if (mostDevious > deviousMax) {
				dt *= 0.93;
			} else if (mostDevious < deviousMin) {
				dt *= 1.03;
			}
			doPopulationUpdate(dt);
		}

		// Now modify timestep if necessary to ensure that particle number is
		// conserved to
		// specified tolerance (but not too high a tolerance). Using updated
		// populations
		// based on the trial timestep computed above, test for conservation of
		// particle
		// number and modify trial timestep accordingly.

		test1 = sumXLast - 1;
		test2 = sumX - 1;
		massChecker = Math.abs(sumXLast - sumX);

		// New timestepping logic that replaces former up or down iterations to
		// ensure
		// conservation of particle number with a single up or down scale of dt.

		if (time < equilibrateTime || !imposeEquil) {
			if (Math.abs(test2) > Math.abs(test1) && massChecker > massTol) {
				dt *= Math.max(massTol / massChecker, downbumper);
				if (checkPC)
					System.out.println("t=" + deci(5, time) + " dt="
							+ deci(4, dt) + " Pop update after downbump:");
				doPopulationUpdate(dt);
			} else if (massChecker < massTolUp) {
				dt *= (massTol / (Math.max(massChecker, upbumper)));
				if (checkPC)
					System.out.println("t=" + deci(5, time) + " dt="
							+ deci(4, dt) + " Pop update after upbump:");
				doPopulationUpdate(dt);
			}
		}

		// Option to start a linear timestep if imposing partial equilibrium
		if (time > startLinear && imposeEquil) {
			dt = dtScale * time;
			doPopulationUpdate(dt);
		}

		// Finally check to be sure that timestep will not overstep next plot
		// output time
		// and adjust to match if necessary. The method fitToTimestep(dt) will
		// adjust
		// dt only if at a plot output interval. In that case it will also
		// recompute the
		// pop[][] values corresponding to the adjusted time interval.

		dt = fitToTimestep(dt);

		// Convert all Be-8 to alpha particles since lifetime of Be-8 to decay
		// to two
		// alpha particles is short compared with typical integration steps.

		pop[2][2] = pop[2][2] + 2 * pop[4][4];
		pop[4][4] = 0;
		Y[2][2] = pop[2][2] / nT;
		Y[4][4] = 0;

		sumXLast = sumX;
		// if(renormalizeMassFractions && numberAsymptotic > 0)
		// renormalizeAsyX();
		if (renormalizeMassFractions && numberAsymptotic > 0)
			renormalizeX();
		networkMassDifference();

		// Return the proper dt for this timestep. (Current time is time+dt).
		// pop arrays have already been updated to correct values for time+dt.
		// The class variable deltaTimeRestart now holds the proper trial dt to
		// start
		// with in the next integrations step (deltaTimeRestart is equal to the
		// dt
		// returned below unless at a plot output interval).

		return dt;
	}

	// ---------------------------------------------------------------------------------------------------------------
	// Method to update the populations with current trial timestep passed as dt
	// ---------------------------------------------------------------------------------------------------------------

	private void doPopulationUpdate(double dt) {

		sumX = 0;
		asysumX = 0;
		numberAsymptoticLast = numberAsymptotic;
		numberAsymptotic = 0;
		double asycheck;

		// Steady state approximation

		if (doSS) {
			steadyState(dt);
			return;
		}

		// If not steady state approximation

		// Loop over all isotopes and either update by original explicit
		// asymptotic algorithm,
		// or do the predictor step of a predictor-corrector asymptotic
		// algorithm.

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {

				// Update by explicit or asymptotic method, depending on value
				// of keff*dt
				// and assuming that the timestep is dt.

				if (doAsymptotic) {
					asycheck = keff[i][j] * dt;
					if (asycheck > asycut) {
						asymptoticUpdate(i, j, dt);
						isAsymptotic[i][j] = true;
						numberAsymptotic++;
						asysumX += (pop[i][j] * AA[i][j]); // Will multiply by
															// 1/nT below

						// Diagnostics
						if (checkPC && pop[i][j] < 0)
							System.out.println("    Line 5650, Predictor: t="
									+ deci(5, time) + " Y ="
									+ deci(4, pop[i][j] / nT)
									+ " negative for Z=" + i + " N=" + j);
						if (checkPC && pop[i][j] < 0)
							callExit("    Line 5650, Predictor: Y ="
									+ deci(4, pop[i][j] / nT)
									+ " negative for Z=" + i + " N=" + j);

					} else {
						explicitUpdate(i, j, dt);
						isAsymptotic[i][j] = false;
					}
				} else {
					explicitUpdate(i, j, dt);
					isAsymptotic[i][j] = false;
				}
				sumX += (pop[i][j] * AA[i][j]); // Will multiply by 1/nT below
			}
		}
		sumX /= nT; // Convert to proper units
		asysumX /= nT;

		// If asyPC is true, now do corrector step for the asymptotic algorithm

		if (asyPC) {

			// Save current values of F+, F-, and keff for later use and clear
			// flux accumulators
			// for corrector step

			for (int i = minNetZ; i <= maxNetZ; i++) {
				int indy = Math.min(maxNetN[i], nmax - 1);
				for (int j = minNetN[i]; j <= indy; j++) {
					FplusZero[i][j] = Fplus[i][j];
					FminusZero[i][j] = Fminus[i][j];
					keffZero[i][j] = keff[i][j];
					dpopPlus[i][j] = 0;
					dpopMinus[i][j] = 0;
				}
			}

			// Zero energy accumulators for next iteration

			dERelease = 0;
			dEReleaseA = 0;

			// Update the fluxes and keff based on the populations from the
			// predictor step

			updateLightIonFluxes();
			updateHeavyFluxes();
			computekeff();

			numberAsymptotic = 0;
			sumX = 0;
			asysumX = 0;

			for (int i = minNetZ; i <= maxNetZ; i++) {
				int indy = Math.min(maxNetN[i], nmax - 1);
				for (int j = minNetN[i]; j <= indy; j++) {
					asycheck = keff[i][j] * dt;
					if (asycheck > asycut) {
						asymptoticUpdatePC(i, j, dt);
						isAsymptotic[i][j] = true;
						numberAsymptotic++;
						asysumX += (pop[i][j] * AA[i][j]); // Will multiply by
															// 1/nT below
					} else { // Flux-limited explicit approximation
						explicitUpdatePC(i, j, dt);
						isAsymptotic[i][j] = false;
					}
					sumX += (pop[i][j] * AA[i][j]); // Will multiply by 1/nT
													// below
				}
			}
			sumX /= nT; // Convert to proper units
			asysumX /= nT;
		}
	}

	// ---------------------------------------------------------------------------------------------------
	// Method to update the populations by steady state approximation
	// ---------------------------------------------------------------------------------------------------

	public void steadyState(double dt) {

		// Iteraration loop
		for (int i = 0; i < nit; i++) {
			ssPredictor(dt);
			ssCorrector(dt);
			if (nit > 1) {
				for (int k = minNetZ; k <= maxNetZ; k++) {
					int indy = Math.min(maxNetN[k], nmax - 1);
					for (int j = minNetN[k]; j <= indy; j++) {
						// Clear flux accumulators for flux update for next
						// iteration
						dpopPlus[k][j] = 0;
						dpopMinus[k][j] = 0;
					}
				}

				// Zero energy accumulators for next iteration
				dERelease = 0;
				dEReleaseA = 0;

				updateLightIonFluxes();
				updateHeavyFluxes();
				computekeff();
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------
	// Method to implement steady state predictor
	// ---------------------------------------------------------------------------------------------------

	public void ssPredictor(double dt) {

		// Save current values of F+, F-, and keff for later use. tempPop[Z][N]
		// already
		// contains the saved populations before update (i.e., from last
		// timestep)

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				FplusZero[i][j] = Fplus[i][j];
				keffZero[i][j] = keff[i][j];
			}
		}

		// Loop over all active isotopes and calculate the predictor
		// populations. Unlike for
		// asymptotic method, we will update all populations with the same
		// predictor,
		// irrespective of value of keff*dt for an isotope.

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				double kdt = keff[i][j] * dt;
				pop[i][j] = tempPop[i][j] + (Fplus[i][j] - Fminus[i][j]) * dt
						/ (1 + kdt * alphaValue(kdt));
				// Clear flux accumulators for flux update for corrector step
				dpopPlus[i][j] = 0;
				dpopMinus[i][j] = 0;
			}
		}

		// Zero energy accumulators for the corrector step

		dERelease = 0;
		dEReleaseA = 0;

		// pop[Z][N] now contains the populations updated by the predictor for
		// timestep dt. Use
		// these predicted populations to update the fluxes and keff, using the
		// same rates as for the
		// predictor step.

		updateLightIonFluxes();
		updateHeavyFluxes();
		computekeff();

	}

	// ---------------------------------------------------------------------------------------------------
	// Method to implement steady state corrector
	// ---------------------------------------------------------------------------------------------------

	public void ssCorrector(double dt) {

		double kBar;
		double kdt;
		double alphaBar;
		double FplusTilde;
		sumX = 0;
		asysumX = 0;
		numberAsymptotic = 0;

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				kBar = 0.5 * (keffZero[i][j] + keff[i][j]);
				kdt = kBar * dt;
				alphaBar = alphaValue(kdt);
				FplusTilde = alphaBar * Fplus[i][j] + (1 - alphaBar)
						* FplusZero[i][j];
				pop[i][j] = tempPop[i][j]
						+ ((FplusTilde - kBar * tempPop[i][j]) * dt)
						/ (1 + alphaBar * kdt);
				Y[i][j] = pop[i][j] / nT;
				if (kdt >= 1) {
					isAsymptotic[i][j] = true;
					numberAsymptotic++;
					asysumX += (Y[i][j] * AA[i][j]);
				} else {
					isAsymptotic[i][j] = false;
				}
				sumX += (Y[i][j] * AA[i][j]);
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------
	// Method to calculate alpha(kdt) for steady state
	// ---------------------------------------------------------------------------------------------------

	public double alphaValue(double a) {
		if (a < 1e-20)
			a = 1e-20; // Necessary to start integration correctly.
		// if(a==0) a+=0.0000000001; // This old version could crash because a^2
		// and a^3 could exceed max number size
		a = 1 / a;
		double a2 = a * a;
		double a3 = a2 * a;
		return (180 * a3 + 60 * a2 + 11 * a + 1)
				/ (360 * a3 + 60 * a2 + 12 * a + 1);

	}

	// ---------------------------------------------------------------------------------------------------
	// Corrector step by explicit corrector algorithm
	// ---------------------------------------------------------------------------------------------------

	public void explicitUpdatePC(int z, int n, double dt) {

		pop[z][n] = tempPop[z][n]
				+ 0.5
				* (FplusZero[z][n] - FminusZero[z][n] + Fplus[z][n] - Fminus[z][n])
				* dt;
		Y[z][n] = pop[z][n] / nT;

	}

	// ---------------------------------------------------------------------------------------------------
	// Corrector step by asymptotic corrector algorithm
	// ---------------------------------------------------------------------------------------------------

	public void asymptoticUpdatePC(int z, int n, double dt) {
		double s = 0;
		double FplusBar = 0;
		double FminusBar = 0;
		double kBar = 0;

		if (isMott) {
			// Mott
			FplusBar = 0.5 * (FplusZero[z][n] + Fplus[z][n]);
			kBar = 0.5 * (keffZero[z][n] + keff[z][n]);
			// FminusBar = 0.5*(FminusZero[z][n] + Fminus[z][n]); // Error Mott
			// thesis p.40?
			FminusBar = kBar * tempPop[z][n]; // Possible modified form?
			pop[z][n] = tempPop[z][n] + (FplusBar - FminusBar) * dt
					/ (1 + 0.5 * kBar * dt);

			// Diagnostics
			if (checkPC && pop[z][n] < 0)
				System.out.println("    Line 5884, Mott Corrector: Y ="
						+ deci(4, pop[z][n] / nT) + " negative for Z=" + z
						+ " N=" + n);
			if (checkPC && pop[z][n] < 0)
				callExit("    Line 5884, Mott Corrector: Y ="
						+ deci(4, pop[z][n] / nT) + " negative for Z=" + z
						+ " N=" + n);

		} else {
			// Oran and Boris, p. 135
			s = 1 / keff[z][n] + 1 / keffZero[z][n];
			pop[z][n] = (tempPop[z][n] * (s - dt) + 0.5
					* (Fplus[z][n] + FplusZero[z][n]) * s * dt)
					/ (s + dt);

			// Diagnostics
			if (checkPC && pop[z][n] < 0)
				System.out.println("    Line 5888, O-B Corrector: Y ="
						+ deci(4, pop[z][n] / nT) + " negative for Z=" + z
						+ " N=" + n);
			if (checkPC && pop[z][n] < 0)
				callExit("    Line 5888, O-B Corrector: Y ="
						+ deci(4, pop[z][n] / nT) + " negative for Z=" + z
						+ " N=" + n);

		}
		Y[z][n] = pop[z][n] / nT;
	}

	// ----------------------------------------------------------------------------------------------------------------------
	// Method to determine the isotope with the max change in population. After
	// this
	// method is run, maxdpopp holds the absolute value of max change in
	// population
	// for an isotope and maxdpoppZ is Z and maxdpoppN is N for that isotope.
	// ----------------------------------------------------------------------------------------------------------------------

	private void maxFlux() {
		maxdpopp = 0;
		maxdpoppZ = 0;
		maxdpoppN = 0;
		double abby;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				tempPop[i][j] = pop[i][j];
				dpop[i][j] = dpopNow(i, j);
				abby = Math.abs(dpop[i][j]);
				if (abby > maxdpopp) {
					maxdpopp = abby;
					maxdpoppZ = i;
					maxdpoppN = j;
				}
			}
		}

		// Check fluxes from reaction groups

		if (imposeEquil && time > equilibrateTime && totalEquilReactions > 0) {
			maxFluxRGIndex = 0;
			maxFluxRG = 0.0;
			for (int i = 0; i < numberReactionGroups; i++) {
				if (RGgroup[i].netFlux > maxFluxRG) {
					maxFluxRG = RGgroup[i].netFlux;
					maxFluxRGIndex = i;
				}
			}
			// System.out.println("Flux check: t="+deci(6,time)+" Z="+maxdpoppZ
			// +" N="+maxdpoppN+" dpop="+deci(4,maxdpopp/totalSeeds)
			// +"  RGmax: "+RGgroup[maxFluxRGIndex].reactions[0].reacString
			// +" RGmaxflux="+deci(4,maxFluxRG/nT)
			// );
		}

	}

	// -------------------------------------------------------------------------------------------------------------------------------
	// Method to compute effective decay constant for isotopes in asymptotic
	// approximation
	// -------------------------------------------------------------------------------------------------------------------------------

	private void computekeff() {
		sumFplus = 0;
		sumFminus = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				// Compute fluxes and keff
				Fplus[i][j] = dpopPlus[i][j];
				Fminus[i][j] = dpopMinus[i][j];
				if (pop[i][j] > 0) {
					keff[i][j] = Fminus[i][j] / pop[i][j];
				} else {
					keff[i][j] = 0;
				}
				sumFplus += (Fplus[i][j] * AA[i][j]);
				sumFminus += (Fminus[i][j] * AA[i][j]);
			}
		}
		sumFplus /= nT;
		sumFminus /= nT;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Method to ensure that current timestep is not larger than the time to the
	// next plot output step. It accepts the current proposed timestep dt and
	// checks
	// it against the time to the next plot output step. If dt is smaller, it
	// returns
	// the original proposed timestep. If dt is larger, it returns the (smaller)
	// time to
	// the next plot step after storing what the timestep would have been in the
	// class
	// variable deltaTimeRestart for later use.
	// --------------------------------------------------------------------------------------------------------------------

	private double fitToTimestep(double dt) {

		// Don't make new timestep larger than the plot output step

		double tchk = timeIntervals[tintNow] - time;

		if (tchk < 0)
			callExit("\n***ERROR:\n" + "time=" + time + " greater than next"
					+ " plot interval=" + timeIntervals[tintNow]
					+ ".\nProbably because of"
					+ " too many plot intervals for the integration interval. "
					+ "\nTry reducing the value of the parameter Steps.");

		deltaTimeRestart = dt;
		if (dt >= tchk && tchk > 0) {

			dt = tchk;

			doPopulationUpdate(dt);

			// Set isAsymptotic array using the timestep we would have used if
			// not at a plot
			// output step. (It has been set with the shortened plot output step
			// in preceding
			// doPopulationUpdate, which will give a distorted view at the plot
			// output step of
			// how many isotopes are asymptotic on average unless corrected.)

			if (doAsymptotic)
				updateAsymptotic(deltaTimeRestart);
		}
		return dt;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------
	// Method to update the populations by explicit algorithm. This is invoked
	// if keff*dt < asycut.
	// Note that this step is the final result for the standard asymptotic
	// algorithm, or the
	// predictor step for either the Mott or Oran and Boris predictor-corrector
	// algorithms.
	// ------------------------------------------------------------------------------------------------------------------------------------

	public void explicitUpdate(int z, int n, double dt) {
		pop[z][n] = dpop[z][n] * dt + tempPop[z][n];
		Y[z][n] = pop[z][n] / nT;
	}

	// -----------------------------------------------------------------------------
	// Method to return the first term in asymptotic approx
	// -----------------------------------------------------------------------------

	static double term1(int z, int n) {
		if (keff[z][n] > 0) {
			return Fplus[z][n] / keff[z][n];
		} else {
			return 0;
		}
	}

	// --------------------------------------------------------------------------------
	// Method to return the second term in asymptotic approx
	// --------------------------------------------------------------------------------

	static double term2(int z, int n, double dt) {
		if (keff[z][n] > 0) {
			return (Fplus[z][n] / keff[z][n] - FratPrev[z][n])
					/ (keff[z][n] * dt);
		} else {
			return 0;
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------------
	// Method to update the populations by asymptotic algorithm (either original
	// asymptotic
	// method or predictor step if predictor-corrector algorithm). This method
	// is invoked for
	// an isotope if keff*dt >asycut.
	// -------------------------------------------------------------------------------------------------------------------------------

	public void asymptoticUpdate(int z, int n, double dt) {
		if (asyPC) {
			asyPredict(z, n, dt); // Asymptotic predictor step if
									// predictor-corrector algorithm
		} else {
			if (sophia) { // Sophia He algorithm
				pop[z][n] = (tempPop[z][n] + Fplus[z][n] * dt)
						/ (1 + keff[z][n] * dt);
				Y[z][n] = pop[z][n] / nT;
			} else {
				pop[z][n] = term1(z, n) - term2(z, n, dt); // Original
															// asymptotic
															// formula
				Y[z][n] = pop[z][n] / nT;
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------
	// Method to update the populations by asymptotic predictor-corrector
	// formula
	// ---------------------------------------------------------------------------------------------------------------

	public void asyPredict(int z, int n, double dt) {
		if (isMott) {
			pop[z][n] = tempPop[z][n] + (Fplus[z][n] - Fminus[z][n]) * dt
					/ (1 + keff[z][n] * dt); // Mott thesis
		} else {
			// Oran and Boris, p. 135
			pop[z][n] = (tempPop[z][n] * (2 / keff[z][n] - dt) + 2 * dt
					* Fplus[z][n] / keff[z][n])
					/ (2 / keff[z][n] + dt);

		}
		Y[z][n] = pop[z][n] / nT;
		// double term1 = tempPop[z][n]*(2/keff[z][n]-dt);
		// double term2 = 2*dt*Fplus[z][n]/keff[z][n];
		// System.out.println("        PC predictor: t="+deci(5,time)+" dt="+deci(4,dt)+" Z="+z+" N="+n+" k="+deci(4,keff[z][n])
		// +" 2/k="+deci(4,2/keff[z][n])+" F+="+deci(4,Fplus[z][n])+" tempPop="+deci(4,tempPop[z][n])
		// +" Y="+deci(4,Y[z][n])+" t1="+deci(4,term1)+" t2="+deci(4,term2)
		// );
	}

	// ---------------------------------------------------------------------------------------------------------------------
	// Method to effectively renormalize sum of mass fractions to unity by
	// computing
	// new value of total nucleon number nT after each integration step.
	// ---------------------------------------------------------------------------------------------------------------------

	public void renormalizeX() {
		totalCorrectionSumX += (sumX - 1);
		// Renormalize after each integration step
		f = nT = countNeutrons() + countProtons();
		sumXLast = 1;
	}

	// ---------------------------------------------------------------------------------------------------------------------
	// Method to renormalize asymptotic isotopes so the sumX is one.
	// ---------------------------------------------------------------------------------------------------------------------

	public void renormalizeAsyX() {
		double sumXexplicit = 0.0;
		double sumXasy = 0.0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (isAsymptotic[i][j]) {
					sumXasy += pop[i][j] * AA[i][j];
					// System.out.println("    Asymptotic: Z="+i+" N="+j+" X="+deci(8,pop[i][j]*AA[i][j]/nT)
					// +" sumXasy="+deci(8,sumXasy/nT)
					// );
				} else {
					sumXexplicit += pop[i][j] * AA[i][j];
					// System.out.println("    Explicit:: Z="+i+" N="+j+" X="+deci(8,pop[i][j]*AA[i][j]/nT)
					// +" sumXexp="+deci(8,sumXexplicit/nT)
					// );
				}
			}
		}
		sumXasy /= nT;
		sumXexplicit /= nT;
		double asyTarget = 1 - sumXexplicit;
		double renorm = asyTarget / sumXasy;
		// if(renorm < 0) callExit("renorm="+renorm);
		// System.out.println("t="+deci(6,time)+" sumXasy="+deci(8,sumXasy)
		// +" asyTarget="+deci(8,asyTarget)+" sumXexp="+deci(8,sumXexplicit)
		// +" renorm="+deci(5,renorm)
		// );

		if (renorm < 0)
			return;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (isAsymptotic[i][j]) {
					pop[i][j] *= renorm;
					Y[i][j] *= renorm;
				}
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Method to update the boolean isAsymptotic[][] array for a specific
	// assumed dt
	// --------------------------------------------------------------------------------------------------------------------

	private void updateAsymptotic(double dt) {
		numberAsymptotic = 0;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				double asycheck = keff[i][j] * dt;
				if (asycheck > asycut) {
					isAsymptotic[i][j] = true;
					numberAsymptotic++;
				} else {
					isAsymptotic[i][j] = false;
				}
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to calculate energy release by taking difference of network masses
	// before and after timestep. Alternative to summing Q-values.
	// ----------------------------------------------------------------------------------------------------------------

	private void networkMassDifference() {
		networkMass = returnNetworkMass();
		dEReleaseA = (lastMass - networkMass);
		EReleaseA += dEReleaseA;
		lastMass = networkMass;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Method to average the timestep array timestepStacker[] over the previous
	// numberStacked timesteps to output a more stable average timestep. Returns
	// the average timestep.
	// --------------------------------------------------------------------------------------------------------------------

	private double dtAvg() {
		double sumdt = 0;
		int numdt = 0;
		for (int i = 0; i < numberStacked; i++) {
			if (timestepStacker[i] > 0) {
				sumdt += timestepStacker[i];
				numdt++;
			}
		}
		if (numdt > 0) {
			return sumdt / (double) numdt;
		} else {
			return -1;
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Method to tabulate the Z and N in 1D arrays of active isotopes.
	// --------------------------------------------------------------------------------------------------------------------

	static void tabulateActiveIsotopes() {
		// Determine how many active isotopes there are
		int activeNumber = 0;
		for (int i = 0; i < pmax; i++) {
			for (int j = 0; j < nmax; j++) {
				if (IsotopePad.isoColor[i][j])
					activeNumber++;
			}
		}
		numberActiveIsotopes = activeNumber; // Store permanently

		// Create arrays to hold Z, N of active isotopes and array of IsoVector
		// objects to
		// hold the network population vector information

		Zactive = new int[activeNumber];
		Nactive = new int[activeNumber];

		// Fill arrays with Z, N of active isotopes in network

		int count = 0;
		for (int i = 0; i < pmax; i++) {
			for (int j = 0; j < nmax; j++) {
				if (IsotopePad.isoColor[i][j]) {
					Zactive[count] = i;
					Nactive[count] = j;
					count++;
					if (masses[1][0] > 0 && i != 6 && j != 6
							&& masses[i][j] == 0) {
						System.out.println("MISSING MASS: Z=" + i + " N=" + j
								+ " mass=" + masses[i][j]);
					}
				}
			}
		}

		// Determine the min and max Z in network, and the min and max N for
		// each Z.
		// Will use this to limit sums to only over active network.

		minNetZ = 99;
		maxNetZ = 0;
		boolean foundMinZ = false;
		boolean foundMinN = false;

		for (int i = 0; i < pmax; i++) {
			foundMinN = false;
			for (int j = 0; j < nmax; j++) {
				if (IsotopePad.isoColor[i][j] && !foundMinZ) {
					foundMinZ = true;
					minNetZ = i;
				}
				if (IsotopePad.isoColor[i][j] && !foundMinN) {
					foundMinN = true;
					minNetN[i] = j;
				}
				if (IsotopePad.isoColor[i][j]) {
					maxNetZ = i;
					maxNetN[i] = j;
				}
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Method to prune reactions to only those involving elements of the network
	// if
	// not all isotopes are active. Returns true if all isotopes in the reaction
	// are
	// in the network and false if any are not.
	// --------------------------------------------------------------------------------------------------------------------

	static boolean pruneReactions(int Z, int N, ReactionClass1 R) {

		int left = 0;
		int right = 0;

		// Determine number of reactants on left side and right side

		switch (R.reacIndex) {

		case 0:

			break;

		case 1: // a -> b

			left = 1;
			right = 1;
			break;

		case 2: // a -> b + c

			left = 1;
			right = 2;
			break;

		case 3: // a -> b + c + d

			left = 1;
			right = 3;
			break;

		case 4: // a + b -> c

			left = 2;
			right = 1;
			break;

		case 5: // a + b -> c + d

			left = 2;
			right = 2;
			break;

		case 6: // a + b -> c + d + e

			left = 2;
			right = 3;
			break;

		case 7: // a + b -> c + d + e + f

			left = 2;
			right = 4;
			break;

		case 8: // a + b + c -> d (+e)

			left = 3;
			// Deal with ambiguity of 1 or 2 species on right side for this
			// reaction class
			right = R.numberProducts;

			break;
		}

		// Now test whether reaction stays in network

		boolean isIn;

		// First test whether everything on the left side is in the network

		for (int l = 0; l < left; l++) {
			isIn = false;
			for (int i = 0; i < Zactive.length; i++) {
				if (R.isoIn[l].x == Zactive[i] && R.isoIn[l].y == Nactive[i]) {
					isIn = true;
					break;
				}
			}
			if (!isIn)
				return false;
		}

		// Then test whether everything on the right side is in the network

		for (int r = 0; r < right; r++) {
			isIn = false;
			for (int i = 0; i < Zactive.length; i++) {
				if (R.isoOut[r].x == Zactive[i] && R.isoOut[r].y == Nactive[i]) {
					isIn = true;
					break;
				}
			}
			if (!isIn)
				return false;
		}

		return true;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Method to determine whether a given reaction object correspond to a
	// light-ion
	// on light-ion reaction (e.g., alpha + proton -> ?), where light-ion means
	// Z<3.
	// This is called from PlotReactionList.
	// --------------------------------------------------------------------------------------------------------------------

	static boolean isLightIonReaction(ReactionClass1 tempo) {

		boolean isLight = false;
		if ((tempo.reacIndex < 4 && tempo.isoIn[0].x < 3)
				|| (tempo.reacIndex > 3 && tempo.reacIndex < 8
						&& tempo.isoIn[0].x < 3 && tempo.isoIn[1].x < 3)
				|| (tempo.reacIndex == 8 && tempo.isoIn[0].x < 3
						&& tempo.isoIn[1].x < 3 && tempo.isoIn[2].x < 3)) {
			isLight = true;
		}
		return isLight;
	}

	// --------------------------------------------------------------------------------------------------------------------
	// Method to output utility graphs. The fields output here are read by the
	// Maple
	// program utilityGrapher.mw assuming that the first column is the
	// independent
	// variable and subsequent columns are to be graphed as a function of the
	// independent variable. Use a space to delimit fields.
	// --------------------------------------------------------------------------------------------------------------------

	static void utilityGrapher() {

		int nmin, Z, N;
		double sum;
		String lab;
		int d = 4;

		String s = "";
		s += (deci(d, time));

		Z = 6;
		N = 6;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][2].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][3].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][2].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][4].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][4].reacString;
		s += (" " + deci(d, sum));

		Z = 8;
		N = 8;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][2].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][3].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][2].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][4].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][4].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][5].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][5].reacString;
		s += (" " + deci(d, sum));

		Z = 10;
		N = 10;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][2].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][3].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][2].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][4].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][4].reacString;
		s += (" " + deci(d, sum));

		Z = 12;
		N = 12;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][2].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][3].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][2].reacString;
		s += (" " + deci(d, sum));

		Z = 14;
		N = 14;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye)
				+ RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][2].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][2].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][3].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][3].reacString;
		s += (" " + deci(d, sum));

		Z = 16;
		N = 16;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		Z = 18;
		N = 18;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		Z = 20;
		N = 20;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		Z = 22;
		N = 22;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		Z = 24;
		N = 24;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		Z = 26;
		N = 26;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		Z = 28;
		N = 28;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		Z = 30;
		N = 30;
		nmin = minNetN[Z];

		sum = RObject[Z][N - nmin][0].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][0].reacString;
		s += (" " + deci(d, sum));

		sum = RObject[Z][N - nmin][1].prob(T9, rho, Ye);
		lab = RObject[Z][N - nmin][1].reacString;
		s += (" " + deci(d, sum));

		// Triple-alpha

		sum = RObject[2][2][39].prob(T9, rho, Ye)
				+ RObject[2][2][40].prob(T9, rho, Ye);
		lab = RObject[2][2][39].reacString;
		s += (" " + deci(d, sum));

		toGraph.println(s);
		if (tintNow == (numdt - 1))
			System.out.println("\n"
					+ "  --> Alpha rates written to graphics.out"
					+ " (Plot with utilityGrapher.mw)");

	}

	// ----------------------------------------------------------------------------------------------------
	// Method to read in a file containing previously-calculated abundance
	// data in order to plot it. Requires the class ReadAFile.
	// ----------------------------------------------------------------------------------------------------

	public void readOldAbundances(String filename) {

		String s = null;
		int tk = 0; // Token counter

		// Create instance of ReadAFile and use it to read in the file,
		// returning file content as a string s. Use overloaded constructor
		// for ReadAFile to specify the buffer size for byte stream readin.

		ReadAFile raf = new ReadAFile(6000000);

		// readASCIIFile method throws IOException, so it must be caught

		try {
			s = raf.readASCIIFile(filename);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		// System.out.println("\nRaw file string:\n\n"+s+"\n");

		// --------------------------------------------------------------------------------
		// Parse the buffer read in from the ascii file and use it
		// to set values for variables.
		// --------------------------------------------------------------------------------

		// Break string into tokens with whitespace delimiter

		StringTokenizer st = new StringTokenizer(s.trim());

		// Process tokens until $$$ is encountered, discarding everything before
		// then as comments.

		while (st.hasMoreTokens()) {
			String test = st.nextToken();
			if (test.compareTo("$$$") == 0)
				break;
		}

		// Loop over remaining tokens and process. Notice the methods to convert
		// the
		// string tokens to either integers or doubles.

		numdt = Integer.parseInt(st.nextToken());
		nintervals = numdt - 1;
		numberIsotopesPopulated = Integer.parseInt(st.nextToken());
		numberCurvesToShow = numberIsotopesPopulated;
		boxPopuli = numberCurvesToShow;
		nT = f = stringToDouble(st.nextToken());

		if (numdt > (tintMax - 1))
			callExit("\n**** StochasticElements: Error reading " + filename
					+ ": number timesteps (" + numdt + ") greater than max of "
					+ (tintMax - 1));
		if (numberIsotopesPopulated > maxToPlot)
			callExit("\n****Error reading " + filename + ": "
					+ "Number of curves (" + numberIsotopesPopulated
					+ ") exceeds max of " + maxToPlot);
		if (numberIsotopesPopulated == 0)
			callExit("\n****Error reading " + filename + ": "
					+ "Number isotopes populated is zero");

		for (int i = 0; i < 15; i++)
			st.nextToken(); // Ignore 15 placeholder variables not currently
							// used

		st.nextToken(); // Ignore Plot_Times label
		tk += 19;

		// Read in plot times
		for (int i = 0; i < numdt; i++) {
			timeNow[i] = stringToDouble(st.nextToken());
			tk++;
		}

		logtminPlot = LOG10 * Math.log(timeNow[0]);
		logtmaxPlot = LOG10 * Math.log(timeNow[numdt - 1]);
		time = timeNow[numdt - 1];

		st.nextToken();
		tk++; // Ignore Integrated_Energy label

		// Read in integrated energies
		for (int i = 0; i < numdt; i++) {
			eNow[i] = stringToDouble(st.nextToken());
			tk++;
		}

		st.nextToken();
		tk++; // Ignore dE/dt label

		// Read in dE/dt
		for (int i = 0; i < numdt; i++) {
			deNow[i] = stringToDouble(st.nextToken());
			tk++;
		}

		st.nextToken();
		tk++; // Ignore dt label

		// Read in dt
		for (int i = 0; i < numdt; i++) {
			tstep[i] = stringToDouble(st.nextToken());
			tk++;
		}

		st.nextToken();
		tk++; // Ignore T9 label

		// Read in T
		for (int i = 0; i < numdt; i++) {
			T9Save[i] = stringToDouble(st.nextToken());
			tk++;
		}

		st.nextToken();
		tk++; // Ignore rho label

		// Read in rho
		for (int i = 0; i < numdt; i++) {
			rhoSave[i] = stringToDouble(st.nextToken());
			tk++;
		}

		st.nextToken();
		tk++; // Ignore Ye label

		// Read in Ye
		for (int i = 0; i < numdt; i++) {
			YeSave[i] = stringToDouble(st.nextToken());
			tk++;
		}

		st.nextToken();
		tk++; // Ignore sumX label

		// Read in sumX
		for (int i = 0; i < numdt; i++) {
			sumXSave[i] = stringToDouble(st.nextToken());
			tk++;
		}

		st.nextToken();
		tk++; // Ignore Abundances_Y label

		// Loop over all numberIsotopesPopulated abundances, first reading Z and
		// N,
		// then the values of Y at numdt timesteps.

		for (int i = 0; i < numberIsotopesPopulated; i++) {
			int Z = Integer.parseInt(st.nextToken());
			tk++;
			int N = Integer.parseInt(st.nextToken());
			tk++;
			IsotopePad.isoColor[Z][N] = true;
			hasBeenPopulated[Z][N] = true;
			for (int j = 0; j < numdt; j++) {
				intPop[Z][N][j] = f * stringToDouble(st.nextToken());
				tk++;
			}

			// Following sets current Y so shift-click on 2D plotter box returns
			// correct
			// population at initially displayed last timestep if ShowIsotopes
			// method used

			Y[Z][N] = intPop[Z][N][numdt - 1] / nT;
		}

		// Read in the parameters used in the previous calculation

		if (st.hasMoreTokens()) {

			calcModeString = st.nextToken();
			tk++;
			myComment = st.nextToken();
			tk++;
			myComment = replaceThisWithThat(myComment, "_", " "); // Replace _
																	// with
																	// blank
																	// space
			if (myComment.compareTo("NoComment") == 0)
				myComment = "";
			computeTime = Integer.parseInt(st.nextToken());
			tk++;
			stochasticFactor = stringToDouble(st.nextToken());
			tk++;
			massTol = stringToDouble(st.nextToken());
			tk++;
			Ymin = stringToDouble(st.nextToken());
			tk++;
			boxPopuli = Integer.parseInt(st.nextToken());
			tk++;
			numberActiveIsotopes = Integer.parseInt(st.nextToken());
			tk++;
			numberAsymptotic = Integer.parseInt(st.nextToken());
			tk++;
			sumX = stringToDouble(st.nextToken());
			tk++;
			nT = stringToDouble(st.nextToken());
			tk++;
			ERelease = stringToDouble(st.nextToken());
			tk++;
			renormalizeMassFractions = Boolean.valueOf(st.nextToken());
			tk++;
			constantHydro = Boolean.valueOf(st.nextToken());
			tk++;
			T9 = stringToDouble(st.nextToken());
			tk++;
			rho = stringToDouble(st.nextToken());
			tk++;
			profileFileName = st.nextToken();
			tk++;
			isoSelectionMode = Integer.parseInt(st.nextToken());
			tk++;
			activeFileName = st.nextToken();
			tk++;
			rateSelectionMode = Integer.parseInt(st.nextToken());
			tk++;
			activeRatesFileName = st.nextToken();
			tk++;
			initAbundMode = Integer.parseInt(st.nextToken());
			tk++;
			abundFileName = st.nextToken();
			tk++;
			plotY = Boolean.valueOf(st.nextToken());
			tk++;

			// tabulateActiveIsotopes();

			// Put the parameters in output strings to display on the plot

			outStrings();

		}

		if (st.hasMoreTokens()) {

			doFluxPlots = true;

			st.nextToken();
			tk++; // Ignore Fplus label

			// Loop over all numberIsotopesPopulated abundances, first reading Z
			// and N,
			// then the values of Fplus at numdt timesteps.

			for (int i = 0; i < numberIsotopesPopulated; i++) {
				int Z = Integer.parseInt(st.nextToken());
				tk++;
				int N = Integer.parseInt(st.nextToken());
				tk++;
				for (int j = 0; j < numdt; j++) {
					sFplus[Z][N][j] = stringToDouble(st.nextToken());
					tk++;
				}
			}

			st.nextToken();
			tk++; // Ignore Fminus label

			// Loop over all numberIsotopesPopulated abundances, first reading Z
			// and N,
			// then the values of Fminus at numdt timesteps.

			for (int i = 0; i < numberIsotopesPopulated; i++) {
				int Z = Integer.parseInt(st.nextToken());
				tk++;
				int N = Integer.parseInt(st.nextToken());
				tk++;
				for (int j = 0; j < numdt; j++) {
					sFminus[Z][N][j] = stringToDouble(st.nextToken());
					tk++;
				}
			}

		} else {
			doFluxPlots = false;
			System.out.println("  --> Loading " + oldYfile
					+ "; no flux data so suppressing flux plot");
		}

		// Compute the sum of mass fractions in light ion, C-Mg, Si, and Fe
		// regions

		for (int j = 0; j < numdt; j++) {
			checkXdist(j);
		}

		System.out.println("  --> Data file " + oldYfile
				+ " from earlier calculation loaded");
	}

	// ------------------------------------------------------------------------------------------------------
	// Method to read in a file containing partition function information.
	// Requires the class ReadAFile. Also currently getting mass excesses
	// from this file instead of masses.inp
	// -------------------------------------------------------------------------------------------------------

	public void readpfFile(String filename) {

		String s = null;
		String symb = null;
		int Z;
		int N;
		double massex;

		// Create instance of ReadAFile and use it to read in the file,
		// returning file content as a string s. Use overloaded constructor
		// for ReadAFile to specify the buffer size for byte stream readin.

		ReadAFile raf = new ReadAFile(1500000);

		// readASCIIFile method throws IOException, so it must be caught

		try {
			s = raf.readASCIIFile(filename);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		// --------------------------------------------------------------------------------
		// Parse the buffer read in from the ascii file and use it
		// to set values for variables.
		// --------------------------------------------------------------------------------

		// Break string into tokens with whitespace delimiter

		StringTokenizer st = new StringTokenizer(s.trim());

		// Process tokens until $$$ is encountered, discarding everything before
		// that as comments.

		while (st.hasMoreTokens()) {
			String test = st.nextToken();
			if (test.compareTo("$$$") == 0)
				break;
		}

		// Loop over remaining tokens and process. Notice the methods to convert
		// the
		// string tokens to either integers or doubles.

		while (st.hasMoreTokens()) {
			symb = st.nextToken();
			st.nextToken(); // Skip mass number
			Z = Integer.parseInt(st.nextToken());
			N = Integer.parseInt(st.nextToken());
			st.nextToken(); // Skip spin
			massex = stringToDouble(st.nextToken());

			if (Z <= pmax && N <= nmax) {
				// Store mass excess
				masses[Z][N] = massex;
				// Store partition functions
				for (int i = 0; i < 24; i++) {
					pf[Z][N][i] = stringToDouble(st.nextToken());
				}
			} else {
				for (int i = 0; i < 24; i++) {
					st.nextToken();
				}
			}
		}
		System.out.println("\n  --> Partition function table loaded from "
				+ pfFile);
		System.out.println("  --> Mass excesses loaded from " + pfFile);
	}

	// -------------------------------------------------------------------------------------------------------
	// Method stringToDouble to convert a string to a double. Declared
	// static here because it is called from parseBuffer, which was
	// declared static.
	// -------------------------------------------------------------------------------------------------------

	static double stringToDouble(String s) {
		Double mydouble = Double.valueOf(s); // String to Double (object)
		return mydouble.doubleValue(); // Return primitive double
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to systematically zero data arrays to initialize for a new case
	// ----------------------------------------------------------------------------------------------------------------

	public void initializeDataArrays() {
		for (int i = 0; i <= pmax; i++) {
			for (int j = 0; j <= nmax; j++) {
				for (int k = 0; k <= tintMax; k++) {
					intPop[i][j][k] = 0;
					sFplus[i][j][k] = 0;
					sFminus[i][j][k] = 0;
					twa[i][j][k] = 0;
					twa2[i][j][k] = 0;
				}
				hasBeenPopulated[i][j] = false;
				IsotopePad.isoColor[i][j] = false;
			}
		}
		for (int i = 0; i < tintMax; i++) {
			eNow[i] = 0;
			deNow[i] = 0;
			timeNow[i] = 0;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to write output file for Maple plotter program plotter.mw
	// ----------------------------------------------------------------------------------------------------------------

	public void writeToMaple() {
		for (int i = 0; i < numdt; i++) {
			String s = " ";
			String S = deci(12, LOG10 * Math.log(timeNow[i])) + s
					+ deci(5, LOG10 * Math.log(tstep[i])) + s
					+ deci(4, LOG10 * Math.log(Math.abs(ECON * eNow[i]))) + s
					+ deci(4, LOG10 * Math.log(Math.abs(ECON * deNow[i]))) + s
					+ deci(5, T9Save[i]) + s
					+ deci(4, LOG10 * Math.log(rhoSave[i])) + s
					+ deci(4, YeSave[i]) + s + deci(4, sumXSave[i]) + s
					+ deci(4, ECON * deNow[i]) + s + deci(4, ECON * eNow[i])
					+ s + deci(4, lightX[i]) + s + deci(4, CMgX[i]) + s
					+ deci(4, SiX[i]) + s + deci(4, FeX[i]) + s
					+ deci(4, NSEX[i]);
			toX.println(S);
		}
		System.out.println("  --> Maple file written to " + mapleXoutFile
				+ " (Plot with plotter.mw)");
		System.out
				.println("  --> Maple file written to output/plotfile.out (Plot with timestepPlotter.mw)");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to write out the hydro profile
	// ----------------------------------------------------------------------------------------------------------------

	public void writeHydroProfile() {
		toHydroProfile.println("Index   time[s]   T[K]   rho[g/cm^3]");
		for (int i = 0; i < numdt; i++) {
			toHydroProfile.println(i + " " + deci(12, timeNow[i]) + " "
					+ deci(4, T9Save[i]) + " " + deci(4, rhoSave[i]));
		}
		System.out.println("  --> Hydro profile to " + hydroProfileFile
				+ " (Rename to keep)");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to prep ReactionClass1 with precomputed powers and logs of
	// T9 before a rate calculation. In the new version of ReactionClass1 to
	// increase
	// speed in loops over rates at the same T9 the logs and powers of T9 are
	// computed once and then reused. Thus this method must be called first
	// if ReactionClass1.rate(T9) is invoked after a change in T9.
	// ----------------------------------------------------------------------------------------------------------------

	static void prepReactionClass1(double T9) {
		ReactionClass1.logT9 = Math.log(T9);
		ReactionClass1.T913 = Math.pow(T9, 0.3333333);
		ReactionClass1.T953 = Math.pow(T9, 1.6666666);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to determine what mass fractions are in which isotopic regions.
	// The variable plotIndex is the index of the current graphics output
	// timestep.
	// ----------------------------------------------------------------------------------------------------------------

	void checkXdist(int plotIndex) {
		lightX[plotIndex] = 0;
		CMgX[plotIndex] = 0;
		SiX[plotIndex] = 0;
		FeX[plotIndex] = 0;
		NSEX[plotIndex] = 0;
		for (int i = 0; i <= pmax; i++) {
			int indy = Math.min(IsotopePad.maxDripN[i], nmax - 1);
			for (int j = IsotopePad.minDripN[i]; j <= indy; j++) {
				// i=proton number, j=neutron number
				if (i < 6) {
					lightX[plotIndex] += (intPop[i][j][plotIndex]) * AA[i][j]
							/ nT; // Light
				} else if (i < 14) {
					CMgX[plotIndex] += (intPop[i][j][plotIndex]) * AA[i][j]
							/ nT; // C-Mg
				} else if (i < 24) {
					SiX[plotIndex] += (intPop[i][j][plotIndex]) * AA[i][j] / nT; // Si
				} else if (i < 36) {
					FeX[plotIndex] += (intPop[i][j][plotIndex]) * AA[i][j] / nT; // Fe
				}
			}
		}
		NSEX[plotIndex] = lightX[plotIndex] + FeX[plotIndex];
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to return the partition function of isotope (Z,N) at log_10 of
	// temperature t9 (note that the third argument is log_10(t9), not t9).
	// ----------------------------------------------------------------------------------------------------------------

	// public double pfInterpolator(int Z, int N, double logt9){
	// // Return linear interpolant
	// return pf[Z][N][lowPFindex]
	// +( (logt9-Tpf[lowPFindex]) / (Tpf[lowPFindex+1]-Tpf[lowPFindex]) )
	// * (pf[Z][N][lowPFindex+1]-pf[Z][N][lowPFindex]);
	// }

	// Corrected version from Elisha

	public double pfInterpolator(int Z, int N, double t9) {

		double rdt;
		double term1;
		double term2;
		double sumterms;
		double bob;
		// Return linear interpolant

		// return pf[Z][N][lowPFindex]

		// +( (logt9-Tpf[lowPFindex]) / (Tpf[lowPFindex+1]-Tpf[lowPFindex]) )

		// * (pf[Z][N][lowPFindex+1]-pf[Z][N][lowPFindex]);
		rdt = (t9 - Tpf[lowPFindex]) / (Tpf[lowPFindex + 1] - Tpf[lowPFindex]);
		term1 = rdt * Math.log(pf[Z][N][lowPFindex + 1]);
		term2 = (1.0 - rdt) * Math.log(pf[Z][N][lowPFindex]);
		sumterms = term1 + term2;
		// return
		// Math.exp(rdt*Math.log(pf[Z][N][lowPFindex+1])+(1-rdt)*Math.log(pf[Z][N][lowPFindex]));
		bob = Math.exp(sumterms);
		// System.out.println("PF stuff: "+t9+" "+Z+" "+N+" "+rdt+" "+sumterms+" "+bob);
		return bob;

	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to update the partition functions for all isotopes at current log
	// T9
	// ----------------------------------------------------------------------------------------------------------------

	// public void pfUpdate(double logt9){
	// 
	// // Use existing instance of SplineInterpolator.bisection method to find
	// bracketing indices.
	// // Since the bisection index will be the same for all isotopes at this
	// temperature,
	// // compute it only once before looping over isotopes. The object
	// interpolatepf is an
	// // instance of the SplineInterpolator class. Its bisection method finds
	// the lower
	// // bracketing index in the array to begin interpolation.
	// 
	// // Return a partition function of 1 if T9<1 (the table allows
	// interpolation down to T9=0.1,
	// // but partitition functions below T9~1 are very close to one so don't
	// bother to
	// // interpolate them
	// 
	// if(logt9 < logpfCut) return;
	// lowPFindex = interpolatepf.bisection(Tpf,logt9);
	// for(int i=minNetZ; i<=maxNetZ; i++) {
	// int indy = Math.min(maxNetN[i], nmax-1);
	// for(int j=minNetN[i]; j<=indy; j++) {
	// pfNow[i][j] = pfInterpolator(i,j,logt9);
	// }
	// }
	// }

	// Elisha correction

	public void pfUpdate(double t9) {

		// Use existing instance of SplineInterpolator.bisection method to find
		// bracketing indices.
		// Since the bisection index will be the same for all isotopes at this
		// temperature,
		// compute it only once before looping over isotopes. The object
		// interpolatepf is an
		// instance of the SplineInterpolator class. Its bisection method finds
		// the lower
		// bracketing index in the array to begin interpolation.

		// Return a partition function of 1 if T9<1 (the table allows
		// interpolation down to T9=0.1,
		// but partitition functions below T9~1 are very close to one so don't
		// bother to
		// interpolate them

		if (LOG10 * Math.log(t9) < logpfCut)
			return;
		lowPFindex = interpolatepf.bisection(Tpf, t9);
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				pfNow[i][j] = pfInterpolator(i, j, t9);
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to update the rates for active isotopes at the current temperature
	// ----------------------------------------------------------------------------------------------------------------

	public void updateHeavyRates() {

		// Loop over heavy seed isotopes with non-zero populations, processing
		// their possible reactions in this timestep. The outer loop in i goes over
		// the isotopes selected in pruneSeeds(); the inner loop in j goes over all
		// possible reactions for that isotope in the timestep.

		for (int i = 0; i < numberSeeds; i++) {
			Z = seedProtonNumber[i];
			N = seedNeutronNumber[i];
			nmin = minNetN[Z];
			int Zindex = Z;
			int Nindex = N - nmin;
			int zleft = 0;
			int nleft = 0;
			int zright = 0;
			int nright = 0;
			double pfFactor = 1.0;
			boolean dopf = true;

			// Loop over all possible reactions for each seed isotope, computing
			// and storing the rates for each.

			for (int j = 0; j < numberReactions[Zindex][Nindex]; j++) {

				totalReactions++;

				// Basic rate associated with this reaction

				// Add rates to master rate array. These rates are not multiplied
				// by Y factors for 2-body and 3-body reactions yet. They are 
				// appropriate for partial equilibrium calculations and
				// correspond to the forward and backward k_f and k_r effective
				// rates in partial eqilibrium reaction pairs.

				masterRates[Z][N][j] = RObject[Zindex][Nindex][j].returnk(T9,
						rho, Ye);

				// Now compute the rates in s^-1 that will be needed for the
				// asymptotic approximation

				activeRates[i][j] = masterRates[Z][N][j];

				// Multiply by Y factors for 2-body and 3-body reactions. This would
				// have already been done if activeRates had been computed directly 
				// from prob() as before.

				if (RObject[Zindex][Nindex][j].reacIndex > 3) {
					activeRates[i][j] *= Y[RObject[Zindex][Nindex][j].isoIn[0].x][RObject[Zindex][Nindex][j].isoIn[0].y];
				}
				if (RObject[Zindex][Nindex][j].reacIndex == 8) {
					activeRates[i][j] *= Y[RObject[Zindex][Nindex][j].isoIn[1].x][RObject[Zindex][Nindex][j].isoIn[1].y];
				}

				// activeRates[i][j] now holds the values that would have been
				// computed directly by setting activeRates[i][j] =
				// RObject[Zindex][Nindex][j].prob(T9,rho,Ye);

				// Apply partition function factors if an inverse rate and
				// dopf=true and T9 > tCut.

				if (dopf && T9 > pfCut && RObject[Zindex][Nindex][j].reverseR) {
					if (RObject[Zindex][Nindex][j].reacIndex == 2) {
						zleft = RObject[Zindex][Nindex][j].isoIn[0].x;
						nleft = RObject[Zindex][Nindex][j].isoIn[0].y;
						zright = RObject[Zindex][Nindex][j].isoOut[1].x;
						nright = RObject[Zindex][Nindex][j].isoOut[1].y;
					} else if (RObject[Zindex][Nindex][j].reacIndex == 5) {
						zleft = RObject[Zindex][Nindex][j].isoIn[1].x;
						nleft = RObject[Zindex][Nindex][j].isoIn[1].y;
						zright = RObject[Zindex][Nindex][j].isoOut[1].x;
						nright = RObject[Zindex][Nindex][j].isoOut[1].y;
					}
					pfFactor = pfNow[zright][nright] / pfNow[zleft][nleft];
					activeRates[i][j] *= pfFactor;
					masterRates[Z][N][j] *= pfFactor;

					// Temporary diagnostics for partition functions
					// System.out.println("time="+deci(4,time)
					// +" T9="+deci(3,T9)
					// +" "+RObject[Zindex][Nindex][j].reacString
					// +" pfFac="+deci(4,pfFactor)
					// +" pfL="+deci(4,pfNow[zleft][nleft])
					// +" pfR="+deci(4,pfNow[zright][nright])
					// );

				} // end partition function correction

				// Store rate in Reaction object (units of s^-1)
				RGgroup[RGC[Z][N][j]].reactions[RGCmember[Z][N][j]].rate = activeRates[i][j];

			} // end reactions loop
		} // end seeds loop
	}

	// -------------------------------------------------------------------------------------------------------------------
	// Method to update the fluxes for active isotopes with current rates. Rates
	// are computed in updateHeavyRates(). Each time the temperature or density
	// changes the rates must be updated with updateHeavyRates() and then the
	// fluxes
	// updated with updateHeavyFluxes(), but if the temperature and density have
	// not
	// changed but populations have changed (for example, in the corrector step
	// of
	// a predictor-corrector algorithm), it is only necessary to
	// updateHeavyFluxes (rates
	// take longer to compute than the fluxes).
	// -------------------------------------------------------------------------------------------------------------------

	public void updateHeavyFluxes() {

		// Loop over heavy seed isotopes with non-zero populations, processing
		// their
		// possible reactions in this timestep. The outer loop in i goes over
		// the
		// isotopes selected in pruneSeeds(); the inner loop in j goes over all
		// possible reactions for that isotope in the timestep.

		for (int i = 0; i < numberSeeds; i++) {

			// Note: need to set Z and N because the ReactionClass1 object
			// method
			// RObject[Z][Nindex][tempIndex[k]].newZNQ(popOut[k]) below
			// uses the static variables StochasticElements.Z and
			// StochasticElements.N

			Z = seedProtonNumber[i];
			N = seedNeutronNumber[i];
			nmin = minNetN[Z];
			int kk = 0;
			int Zindex = Z;
			int Nindex = N - nmin;

			// Loop over all possible reactions for each heavy seed isotope,
			// computing and
			// storing the rates for each.

			for (int j = 0; j < numberReactions[Zindex][Nindex]; j++) {

				// Move following for this and for light on light to computation
				// of rates rather than fluxes
				// because if an iteration is used (e.g., predictor-corrector),
				// the fluxes get recomputed
				// so this will be overcounted if done in the flux update.

				// totalReactions ++;

				// No longer necessary to ensure that flux is positive since
				// division
				// of populations between explicit and asymptotic update
				// depending
				// on whether kdt > 1 ensures positive populations. Index kk
				// will be
				// used later to label the reactions included in the transfer.

				// Remove flux from sum if reaction is in equilibrium. Also
				// implemented in light-light sector.

				// NOTE: following if () added because otherwise it breaks the
				// imposeEquil = false case
				if (imposeEquil)
					reacIsActive[Z][N][j] = !RGgroup[RGC[Z][N][j]].isEquil;

				if (!imposeEquil || !RGgroup[RGC[Z][N][j]].isEquil) {
					Rrates[j] = activeRates[i][j];
					// In following right side equivalent to
					// Rrates[j]*seedNumber[i]
					flux[i][j] = Rrates[j]
							* pop[seedProtonNumber[i]][seedNeutronNumber[i]];
					// if(flux[i][j] < 0) flux[i][j] = 0;
					// Store flux in reaction group
					int RGCind = RGC[Z][N][j];
					int temprindex = RGgroup[RGCind].getMemberIndex(Z, N, j);
					RGgroup[RGCind].reactions[temprindex].flux = flux[i][j];
					fastSlowRates(j, Zindex, Nindex, Rrates[j]); // Track
																	// fast/slow
																	// rates
																	// integration
				} else {
					int RGCind = RGC[Z][N][j];
					int temprindex = RGgroup[RGCind].getMemberIndex(Z, N, j);
					RGgroup[RGCind].reactions[temprindex].flux = activeRates[i][j]
							* pop[seedProtonNumber[i]][seedNeutronNumber[i]];
					flux[i][j] = 0;
					totalEquilReactions++;

					if (displayE)
						System.out
								.println(totalTimeSteps
										+ " Remove "
										+ RObject[Zindex][Nindex][j].reacString
										+ "  from numerical integration (RG "
										+ RGCind
										+ "; reac="
										+ temprindex
										+ ") Flux="
										+ deci(
												6,
												RGgroup[RGCind].reactions[temprindex].flux
														/ nT)
										+ " netFluxRG="
										+ deci(6, RGgroup[RGCind].netFlux / nT)
										+ " netFlux*dt="
										+ deci(6, RGgroup[RGCind].netFlux
												* deltaTime / nT));
				}

				popOut[kk] = flux[i][j]; // popOut is now flux, not population
				tempIndex[kk] = j;
				kk++;
				couplingCounter++; // Count total couplings this timestep
				reacCounter++; // Count total reactions for this seed
				// fastSlowRates(j, Zindex, Nindex, Rrates[j]); // Keep track
				// fastest/slowest rates
			}

			countReactions(); // Store total reaction # this seed for this
								// timestep

			// Finally, update all populations corresponding to transitions from
			// this
			// seed isotope using newZNQ(popOut) method of a ReactionClass1
			// object.

			// NOTE: newZNQ now updates dpopPlus and dpopMinus rather than pop;
			// dpop = dpopPlus - dpopMinus is then added to pop at the end of
			// the timestep

			// NOTE: the following try-catch no longer seems necessary since the
			// truncation of the reactions to only those that remain in the
			// network that
			// is now implemented automatically ensures that no reaction takes
			// one off
			// the network grid. Leave for now, since it does not seem to
			// influence speed
			// significantly. Note that the catch clause records the total
			// population that
			// would have spilled off the grid and then terminates calculation
			// with a
			// specific error message if index goes out of bounds (rather than
			// letting the
			// normal java error output be generated for the index out of bounds
			// exception).
			// In the original application the catch clause recorded the grid
			// spilloff but allowed
			// the calculation to continue (since the exception was caught and
			// did not propagate
			// up the Java exception stack). This illustrates one powerful use
			// of try-catch.

			for (int k = 0; k < kk; k++) {
				try {

					// The newZNQ method updates the increase in flux dpopPlus
					// and
					// the decrease in flux dpopMinus for each isotope because
					// of
					// the current reaction. The total change in flux is
					// dpop = dpopPlus - dpopMinus, which will be computed after
					// looping
					// over all isotopes and reactions.

					if (reacIsActive[Z][N][tempIndex[k]])
						RObject[Zindex][Nindex][tempIndex[k]].newZNQ(popOut[k]);

				} catch (ArrayIndexOutOfBoundsException e) {
					gridSpillOff += popOut[k] * deltaTime;
					callExit("**************** Reaction off grid ***************: "
							+ RObject[Zindex][Nindex][tempIndex[k]].reacString);
				}
			}
		} /* end seed loop */
	}

	// -----------------------------------------------------------------------------------------------------------------------
	// Utility method to replace whitespace in string with user-specified
	// character given
	// by the argument replace.
	// ------------------------------------------------------------------------------------------------------------------------

	static String replaceWhiteSpace(String inputString, String replace) {
		int count = 0;
		String temp = "";
		inputString = inputString.trim(); // Trim leading and trailing
											// whitespace
		StringTokenizer tk = new StringTokenizer(inputString);
		while (tk.hasMoreTokens()) {
			if (count > 0)
				temp += replace;
			temp += tk.nextToken();
			count++;
		}
		return temp;
	}

	// -----------------------------------------------------------------------------------------------------------------------
	// Utility method to replace all instances of one character in a string with
	// a
	// second character. It also trims leading and trailing whitespace.
	// ------------------------------------------------------------------------------------------------------------------------

	static String replaceThisWithThat(String inputString, String target,
			String replace) {
		int count = 0;
		String temp = "";
		inputString = inputString.trim(); // Trim leading and trailing
											// whitespace
		StringTokenizer tk = new StringTokenizer(inputString, target);
		while (tk.hasMoreTokens()) {
			if (count > 0)
				temp += replace;
			temp += tk.nextToken();
			count++;
		}
		return temp;
	}

	// -----------------------------------------------------------------------------------------------------------------------
	// Method to write approx13.rc input file for compiled calculation
	// ------------------------------------------------------------------------------------------------------------------------

	public void writeCompiledInput() {
		String tf = "F";
		toF90.println("reactionLibraryFile = inputs/reaclib.nosmo");
		toF90.println("nucleiLibraryFile = inputs/net.inp");
		toF90.println("initialAbundanceFile = inputs/abund.inp");
		toF90.println("restrictNucleiFile = ");
		toF90.println("constantT9 = " + T9);
		toF90.println("constantDensity = " + rho);
		toF90.println("constantYe = " + Ye);
		if (constantHydro) {
			tf = "F";
		} else {
			tf = "T";
		}
		toF90.println("useThermoProfile = " + tf);
		if (splineInterpolation) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("useCubicInterpolation = " + tf);
		toF90.println("thermoProfileFile = " + profileFileName);
		if (SegreFrame.includeReaction[1]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType1 = " + tf);
		if (SegreFrame.includeReaction[2]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType2 = " + tf);
		if (SegreFrame.includeReaction[3]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType3 = " + tf);
		if (SegreFrame.includeReaction[4]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType4 = " + tf);
		if (SegreFrame.includeReaction[5]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType5 = " + tf);
		if (SegreFrame.includeReaction[6]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType6 = " + tf);
		if (SegreFrame.includeReaction[7]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType7 = " + tf);
		if (SegreFrame.includeReaction[8]) {
			tf = "T";
		} else {
			tf = "F";
		}
		toF90.println("includeReactionType8 = " + tf);
		if (logtmax > logtmaxPlot)
			logtmax = logtmaxPlot;
		toF90.println("startTime = " + Math.pow(10, logtmin));
		toF90.println("endTime = " + Math.pow(10, logtmax));
		toF90.println("plotStartTime = " + Math.pow(10, logtminPlot));
		toF90.println("plotEndTime = " + Math.pow(10, logtmaxPlot));
		toF90.println("initialTimeStep = " + 0.001 * Math.pow(10, logtmin));
		toF90.println("useAdaptiveTimeStep = T");
		toF90.println("stochasticPrecision = " + stochasticFactor);
		toF90.println("massTol = " + massTol);
		toF90.println("maxTimeStep = 1.0e20");
		toF90.println("logOutputTimeStep = 0.1");
		toF90.println("minimumRateFilter = 0.0e0");
		toF90.println("fluxThreshold = 0.0e0");
		toF90.println("abundanceThreshold = 0.0e0");
		toF90.flush();
	}

	// -----------------------------------------------------------------------------------------------------------------------
	// Method to write abundance file for compiled calculation
	// ------------------------------------------------------------------------------------------------------------------------

	public void writeCompiledAbundance() {
		double X;
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (IsotopePad.isAbundant[i][j]) {
					X = Y[i][j] * (double) (i + j);
					System.out.println(i + " " + j + " " + X);
					toAbundance.println(i + " " + j + " " + X);
				}
			}
		}
		toAbundance.flush();
	}
	

	// --------------------------------------------------------------------------------------------------------------
	// Method to set up network vectors. See also overloaded utility methods
	// returnNetIndex() defined below. In an N-isotope network,
	// each isotopic species corresponds to a unit vector in an
	// N-dimensional space. For example, for the 2-isotope network
	// [4He, 12C] the unit species vectors would be (0 1) and (1 0).
	// --------------------------------------------------------------------------------------------------------------

	public void setupNetworkVectors() {
		netVector = new IsoVector[numberActiveIsotopes];
		toChar.println();
		toChar.println("ABUNDANCE VECTOR COMPONENTS:");
		toChar.println();
		for (int k = 0; k < numberActiveIsotopes; k++) {
			int z = Zactive[k];
			int n = Nactive[k];
			String symb = String.valueOf(z + n) + Cvert.returnSymbol(z);
			netVector[k] = new IsoVector(z, n, symb);
			toChar.println(k + " Z=" + netVector[k].Z + " N=" + netVector[k].N
					+ " " + netVector[k].symbol);
		}
		toChar.println();
		toChar.println("REACTIONS AND REACTION VECTORS:");
		toChar.println();
	}

	// --------------------------------------------------------------------------------------------------------------
	// Method to return the network vector index given the Z and N (overloaded).
	// Set up network vectors first with setupNetworkVectors().
	// --------------------------------------------------------------------------------------------------------------

	public int returnNetIndex(int Z, int N) {
		for (int i = 0; i < numberActiveIsotopes; i++) {
			if (netVector[i].Z == Z && netVector[i].N == N)
				return i;
		}
		return -1;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Method to return the network vector index given the symbol (overloaded).
	// Set up network vectors first with setupNetworkVectors().
	// -------------------------------------------------------------------------------------------------------------

	public int returnNetIndex(String symb) {
		for (int i = 0; i < numberActiveIsotopes; i++) {
			if (netVector[i].symbol.compareTo(symb) == 0)
				return i;
		}
		return -1;
	}

	// -------------------------------------------------------------------------------------------------------------
	// Method to return true if given (Z,N) in the network, false otherwise.
	// -------------------------------------------------------------------------------------------------------------

	public boolean isInNet(int Z, int N) {
		if (returnNetIndex(Z, N) < 0) {
			return false;
		} else {
			return true;
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to create reaction vectors based on the isotope vector constructed
	// in setupNetworkVectors().
	// ----------------------------------------------------------------------------------------------------------------

	void createReactionVector(int Z, int N, int m, ReactionClass1 rob) {
		int trex[] = new int[numberActiveIsotopes];
		int nleft = rob.numberReactants;
		int nright = rob.numberProducts;
		for (int j = 0; j < nleft; j++) {
			int z = rob.isoIn[j].x;
			int n = rob.isoIn[j].y;
			int index = returnNetIndex(z, n);
			if (index == -1)
				return; // Return w/o creating reaction vector if isotope not in
						// network
			trex[index]--;
			String symbol = netVector[index].symbol;
		}
		for (int j = 0; j < nright; j++) {
			int z = rob.isoOut[j].x;
			int n = rob.isoOut[j].y;
			int index = returnNetIndex(z, n);
			if (index == -1)
				return; // Return w/o creating reaction vector if isotope not in
						// network
			trex[index]++;
			String symbol = netVector[index].symbol;
		}
		String ts = "";
		for (int j = 0; j < numberActiveIsotopes; j++) {
			reactionVector[Z][N][m][j] = trex[j];
			ts += (String.valueOf(trex[j]) + " ");
		}
		toChar.println(rob.reacString + "  RV[" + Z + "][" + N + "][" + m
				+ "] = (" + ts + ")");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Method to compare reaction vectors. z1, n1, m1 are the Z, N, and reaction
	// number for the first reactions; z2, n2, m2 the corresponding quantities
	// for the
	// second reaction. Returns 0 if the reaction vectors are different, 1 if
	// they are
	// equivalent, and -1 if they are the negatives of each other.
	// ----------------------------------------------------------------------------------------------------------------

	int compareReactionVectors(int z1, int n1, int m1, int z2, int n2, int m2) {
		int compare = 1;
		// If equal to each other
		for (int j = 0; j < numberActiveIsotopes; j++) {
			if (reactionVector[z1][n1][m1][j] != reactionVector[z2][n2][m2][j]) {
				compare = 0;
				break;
			}
		}
		if (compare == 1)
			return 1;
		// If equal but opposite in sign
		for (int j = 0; j < numberActiveIsotopes; j++) {
			if (reactionVector[z1][n1][m1][j] != -reactionVector[z2][n2][m2][j]) {
				return 0;
			}
		}
		return -1;
	}

	// ----------------------------------------------------------------------------------------------------
	// Method to read in a file containing reaction group information.
	// Requires the class ReadAFile. This may need modification since
	// it hasn't been checked since more recent changes in computing
	// the reaction groups.
	// ----------------------------------------------------------------------------------------------------

	public void inputReactionGroups(String filename) {
		String s = null;
		int tk = 0; // Token counter

		// Create instance of ReadAFile and use it to read in the file,
		// returning file content as a string s. Use overloaded constructor
		// for ReadAFile to specify the buffer size for byte stream readin.

		ReadAFile raf = new ReadAFile(25000);

		// readASCIIFile method throws IOException, so it must be caught

		try {
			s = raf.readASCIIFile(filename);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		// System.out.println("\nRaw file string:\n\n"+s+"\n");

		// --------------------------------------------------------------------------------
		// Parse the buffer read in from the ascii file and use it
		// to set values for variables.
		// --------------------------------------------------------------------------------

		// Break string into tokens with whitespace delimiter

		StringTokenizer st = new StringTokenizer(s.trim());

		// Process tokens until $$$ is encountered, discarding everything before
		// then as comments.

		while (st.hasMoreTokens()) {
			String test = st.nextToken();
			if (test.compareTo("$$$") == 0)
				break;
		}

		// Read an integer numGroups that is the number of groups and set the
		// number of
		// groups dimension of the array RGgroup[i]

		numberReactionGroups = Integer.parseInt(st.nextToken());
		RGgroup = new ReactionGroup[numberReactionGroups];

		// For each group read an integer numMembers that is the number of
		// members of that group and use that to set the second variable
		// dimension of
		// rgroup[][]. Then loop over each member reading and
		// storing the Z, N, and reaction serial index for each member.

		for (int i = 0; i < numberReactionGroups; i++) {
			int numMembers = Integer.parseInt(st.nextToken());
			int tclass = Integer.parseInt(st.nextToken()); // RG class
			for (int j = 0; j < numMembers; j++) {
				int tz = Integer.parseInt(st.nextToken());
				int tn = Integer.parseInt(st.nextToken());
				int ts = Integer.parseInt(st.nextToken());
				st.nextToken(); // Skip reaction label (accessible through
								// Reaction objects)
				addToReactionGroup(i, j, tz, tn, ts);
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------
	// Method to output reaction groups from the current calculation
	// ----------------------------------------------------------------------------------------------------------------------

	void writeReactionGroups() {
		int numberGroups = 0; // Equal locally (this method) to global variable
								// numberReactionGroups

		// If reaction groups were not read in from external file, form them
		// from the
		// reactions of the network.

		if (!readReactionGroups) {
			numberGroups = formEquilibriumGroups(true, 0); // Call to count
															// reaction groups
			numberReactionGroups = numberGroups;
			RGgroup = new ReactionGroup[numberGroups];
			formEquilibriumGroups(false, numberGroups); // Call to form reaction
														// groups
		} else {
			numberGroups = numberReactionGroups;
		}

		// Write reaction groups to toChar output file.

		toChar.println();
		toChar.println();
		String tempest = "REACTION GROUP SUMMARY";
		if (readReactionGroups)
			tempest += " (read from file " + reactionGroupFile + ")";
		toChar.println(tempest + ":");
		for (int i = 0; i < numberGroups; i++) {
			toChar.println();
			toChar.println("Group " + i);
			for (int j = 0; j < RGgroup[i].members; j++) {
				toChar.println("   " + j + "  Z=" + RGgroup[i].reactions[j].Z
						+ " N=" + RGgroup[i].reactions[j].N + " m="
						+ RGgroup[i].reactions[j].reacIndex + " ser="
						+ RGgroup[i].reactions[j].serialIndex + " class="
						+ RGgroup[i].reactions[j].reacClass + " react="
						+ RGgroup[i].reactions[j].numberReactants + " prod="
						+ RGgroup[i].reactions[j].numberProducts + " rgclass="
						+ RGgroup[i].reactions[j].reacGroupClass + "  "
						+ RGgroup[i].reactions[j].reacString + " ("
						+ RGgroup[i].reactions[j].resonanceType + ")"
						+ " forward=" + RGgroup[i].reactions[j].forward);
			}
		}

		// Write reaction groups to file output/reactionGroups.out.

		toGroups
				.println("File specifying reaction groups for partial equilibrium approx. Anything");
		toGroups
				.println("before triple dollar signs taken as comments.  First put integer specifying");
		toGroups
				.println("number of groups.  Follow with number of reaction elements in group,");
		toGroups
				.println("followed by one line for each reaction element of the group specifying Z, N,");
		toGroups
				.println("reaction serial index, and reaction label (reaction labels will be ignored on");
		toGroups
				.println("read-in but must have NO BLANK SPACES). Repeat for each reaction group.");
		toGroups
				.println("Entries are white-space delimited; extra whitespace and blank lines ignored.");
		toGroups
				.println("See method readReactionGroups() in StochasticElements for details.");
		toGroups.println();
		toGroups.println("$$$");
		toGroups.println();
		toGroups.println(numberGroups);
		for (int i = 0; i < numberGroups; i++) {
			toGroups.println();
			toGroups.println(RGgroup[i].members + " " + RGgroup[i].RGclass);
			for (int j = 0; j < RGgroup[i].members; j++) {
				toGroups.println(RGgroup[i].reactions[j].Z
						+ " "
						+ RGgroup[i].reactions[j].N
						+ " "
						+ RGgroup[i].reactions[j].serialIndex
						+ "   "
						+ Cvert.replaceWhiteSpace(
								RGgroup[i].reactions[j].reacString, ""));
			}
		}
	}

	// ----------------------------------------------------------------------------------------------------------------------
	// Method to loop through all current reactions and group into equilibrium
	// reaction
	// groups. If countOnly is true, no reaction groups are stored and the
	// method
	// returns the total number of reaction groups. If countOnly is false, the
	// reaction
	// groups are stored and maxGroups is the total number of reaction groups to
	// be
	// stored. Thus a typical usage is to first invoke formEquilibriumGroups
	// with
	// countOnly= true to return the total number of groups, and then to invoke
	// it
	// again with countOnly=false and the number of groups passed as the
	// argument
	// maxGroups:
	//
	// int max = formEquilibriumGroups(true, 0);
	// formEquilibriumGroups(false, max);
	//      
	// after which the array RGgroup[] will contain the reaction groups as
	// objects of type
	// ReactionGroup.
	// ----------------------------------------------------------------------------------------------------------------------

	int formEquilibriumGroups(boolean countOnly, int maxGroups) {
		int numGroups = 0;
		int rindex = 0;
		int nn, nnn;
		ReactionClass1 temp, temp2;
		Reaction tr[] = new Reaction[100];
		Reaction tempRG[];

		// Initialize reactionChosen array to false
		for (int i = 0; i < pmax; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (isInNet(i, j)) {
					for (int k = 0; k < reactionChosen[0][0].length; k++) {
						if (i == 2 && j == 1 && k < numberReactions[2][1]) {
							reactionChosen[i][j][k] = false; // He3 on light ion
						}
						if (i == 1 && j == 2 && k < numberReactions[1][2]) {
							reactionChosen[i][j][k] = false; // H3 on light ion
						}
						if (i == 1 && j == 1 && k < numberReactions[1][1]) {
							reactionChosen[i][j][k] = false; // H2 on light ion
						}
						if (i == 1 && j == 0 && k < numberReactions[1][0]) {
							reactionChosen[i][j][k] = false; // H1 on light ion
						}
						if (i == 0 && j == 1 && k < numberReactions[0][1]) {
							reactionChosen[i][j][k] = false; // neutrons on
																// light ion
						}
						if (i == 2 && j == 2 && k < numberReactions[2][2]) {
							reactionChosen[i][j][k] = false; // alpha on light
																// ion
						}
						if (i > 2) {
							reactionChosen[i][j][k] = false;
						}
					}
				}
			}
		}

		// Now loop over all reactions in the network and compare them with all
		// other reactions in
		// the network to form reaction groups (which correspond to groups of
		// reactions that have
		// the same reaction vectors up to a sign. Do this by nesting an inner
		// loop in Z, N, and reaction
		// number inside an outer loop in Z, N, and reaction number, comparing
		// reactions pairwise for
		// the same reactions vectors up to a sign. In the process use the
		// boolean array
		// reactionChosen to exclude comparisons to reactions that have already
		// been grouped in a
		// reaction group. If a reaction has no partners (no other reactions
		// with same reaction vector
		// up to a sign), it forms a reaction group of one element.

		if (!countOnly) {
			toChar.println();
			toChar.println();
			toChar.println("REACTION GROUP OBJECTS:");
		}
		// Outer loop over all Z and N
		for (int i = 0; i < pmax; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				// Account for different neutron offsets in RObject and
				// numberReactions depending
				// on whether Z >2.
				if (i < 3) {
					nn = j;
				} else {
					nn = j - minNetN[i];
				}
				// Check only isotopes that are in the network
				if (isInNet(i, j)) {
					int len = numberReactions[i][nn];
					// Loop over all reactions for this isotope
					for (int k = 0; k < len; k++) {
						if (reactionChosen[i][j][k]) {
							continue; // Skip if reaction already chosen
						} else {
							reactionChosen[i][j][k] = true;
							numGroups++;
						}
						temp = RObject[i][nn][k];
						int reacClass = temp.reacIndex;
						int numleft = temp.numberReactants;
						int numright = temp.numberProducts;
						int sindex = serialIndex[i][j][k];
						String reac = temp.reacString;
						if (!countOnly) {
							tr[0] = new Reaction(i, j, sindex);

							// Write all fields for this Reaction object out to
							// the toChar output stream.
							// Note that forward-reverse ambiguity for reaction
							// group class 1 or 4 is not
							// yet resolved at this point since
							// resolveForwardReverseAmbiguity() will
							// only be called after the reaction groups are
							// formed. Thus the diagnostic
							// values of .forward sent to toChar below may be
							// not the final values if
							// the reaction group class is 1 or 4 (A or D).

							if (!countOnly) {
								String vec = "[" + tr[0].rVectorString + "]";
								toChar.println("\n" + tr[0].reacString + " ("
										+ tr[0].resonanceType + ") object:\n"
										+ "Z=" + tr[0].Z + " N=" + tr[0].N
										+ " nindex=" + tr[0].nindex
										+ " serialIndex=" + tr[0].serialIndex
										+ " reacIndex=" + tr[0].reacIndex
										+ " reacClass=" + tr[0].reacClass
										+ " reacGroupClass="
										+ tr[0].reacGroupClass
										+ " reacGroupClassLett="
										+ tr[0].reacGroupClassLett
										+ " reacGroupSymbol="
										+ tr[0].reacGroupSymbol
										+ " numberReactants="
										+ tr[0].numberReactants
										+ " numberProducts="
										+ tr[0].numberProducts
										+ " resonanceType="
										+ tr[0].resonanceType + "\nrVector="
										+ vec + "\nrate=" + tr[0].rate
										+ " flux=" + tr[0].flux + " za="
										+ tr[0].isoZ[0] + " na="
										+ tr[0].isoN[0] + " zb="
										+ tr[0].isoZ[1] + " nb="
										+ tr[0].isoN[1] + " zc="
										+ tr[0].isoZ[2] + " nc="
										+ tr[0].isoN[2] + " zd="
										+ tr[0].isoZ[3] + " nd="
										+ tr[0].isoN[3] + " ze="
										+ tr[0].isoZ[4] + " ne="
										+ tr[0].isoN[4] + " forward="
										+ tr[0].forward);
							}
						}

						// Inner loop over all Z and N to compare reactions
						// pairwise
						rindex = 0;
						for (int ii = 0; ii < pmax; ii++) {
							int indy2 = Math.min(maxNetN[ii], nmax - 1);
							for (int jj = minNetN[ii]; jj <= indy2; jj++) {
								if (ii < 3) {
									nnn = jj;
								} else {
									nnn = jj - minNetN[ii];
								}
								if (isInNet(ii, jj)) {
									int len2 = numberReactions[ii][nnn];

									// Loop over all reactions for this isotope
									for (int kk = 0; kk < len2; kk++) {
										int crv = Math
												.abs(compareReactionVectors(i,
														j, k, ii, jj, kk));
										if (reactionChosen[ii][jj][kk]) {
											continue; // Skip if already chosen
										} else if (crv == 1) {
											rindex++;
											reactionChosen[ii][jj][kk] = true;
											temp2 = RObject[ii][nnn][kk];
											int reacClass2 = temp2.reacIndex;
											int numleft2 = temp2.numberReactants;
											int numright2 = temp2.numberProducts;
											int sindex2 = serialIndex[ii][jj][kk];
											tr[rindex] = new Reaction(ii, jj,
													sindex2);

											// Write all fields for this
											// Reaction object out to the toChar
											// output stream
											if (!countOnly) {
												String vec = "["
														+ tr[rindex].rVectorString
														+ "]";

												// Note that forward-reverse
												// ambiguity for reaction group
												// class 1 or 4 is not
												// yet resolved at this point
												// since
												// resolveForwardReverseAmbiguity()
												// will
												// only be called after the
												// reaction groups are formed.
												// Thus the diagnostic
												// values of forward sent to
												// toChar below may be not the
												// final values is
												// the reaction group class is 1
												// or 4 (A or D).

												toChar
														.println("\n"
																+ tr[rindex].reacString
																+ " ("
																+ tr[rindex].resonanceType
																+ ") object:\n"
																+ "Z="
																+ tr[rindex].Z
																+ " N="
																+ tr[rindex].N
																+ " nindex="
																+ tr[rindex].nindex
																+ " serialIndex="
																+ tr[rindex].serialIndex
																+ " reacIndex="
																+ tr[rindex].reacIndex
																+ " reacClass="
																+ tr[rindex].reacClass
																+ " reacGroupClass="
																+ tr[rindex].reacGroupClass
																+ " reacGroupClassLett="
																+ tr[rindex].reacGroupClassLett
																+ " reacGroupSymbol="
																+ tr[rindex].reacGroupSymbol
																+ " numberReactants="
																+ tr[rindex].numberReactants
																+ " numberProducts="
																+ tr[rindex].numberProducts
																+ " resonanceType="
																+ tr[rindex].resonanceType
																+ "\nrVector="
																+ vec
																+ "\nrate="
																+ tr[rindex].rate
																+ " flux="
																+ tr[rindex].flux
																+ " za="
																+ tr[rindex].isoZ[0]
																+ " na="
																+ tr[rindex].isoN[0]
																+ " zb="
																+ tr[rindex].isoZ[1]
																+ " nb="
																+ tr[rindex].isoN[1]
																+ " zc="
																+ tr[rindex].isoZ[2]
																+ " nc="
																+ tr[rindex].isoN[2]
																+ " zd="
																+ tr[rindex].isoZ[3]
																+ " nd="
																+ tr[rindex].isoN[3]
																+ " ze="
																+ tr[rindex].isoZ[4]
																+ " ne="
																+ tr[rindex].isoN[4]
																+ " forward="
																+ tr[rindex].forward);
											}
											String reac2 = temp2.reacString;
										}
									}
								}
							}
						} // End inner loop over all Z and N

						// Store reactions in reaction group array
						tempRG = new Reaction[rindex + 1];
						if (!countOnly) {
							for (int m = 0; m < rindex + 1; m++) {
								tempRG[m] = tr[m];
								// Store reaction group class for each reaction
								// in RGC[Z][N][reacIndex]
								RGC[tr[m].Z][tr[m].N][tr[m].reacIndex] = numGroups - 1;
								RGCmember[tr[m].Z][tr[m].N][tr[m].reacIndex] = m;
								// System.out.println("    Z="+tr[m].Z+" N="+tr[m].N+" i="+tr[m].reacIndex+" RG="
								// +RGC[tr[m].Z][tr[m].N][tr[m].reacIndex]+"  "+tr[m].reacString);
							}
						}

						// Create ReactionGroup object array
						if (!countOnly) {
							RGgroup[numGroups - 1] = new ReactionGroup(
									numGroups - 1, rindex + 1,
									tempRG[0].reacGroupClass, tempRG);
						}
					}
				}
			}
		}
		return numGroups;
	}

	// ----------------------------------------------------------------------------------------------------------------------
	// Method to add entries to a reaction group. The group is specified by the
	// integer "group" and the index of the reaction in that group is specified
	// by "index".
	// The Z and N (left side) for the seed is given by "Z" and "N", and
	// "reacIndex" is
	// the serial index from the serialized data files for the reaction.
	// ----------------------------------------------------------------------------------------------------------------------

	void addToReactionGroup(int group, int index, int Z, int N, int reacIndex) {
		RGgroup[group].reactions[index] = new Reaction(Z, N, reacIndex);
	}

	// -----------------------------------------------------------------------------------------------------------------------------------
	// Method to send list all ReactionClass1 objects RObject[][][] that were
	// read in and stored for this calculation to toChar output stream
	// -----------------------------------------------------------------------------------------------------------------------------------

	void listActiveReactions() {
		int len1 = RObject.length;
		int len2 = RObject[0].length;
		int Z = 0;
		int N = 0;
		toChar.println();
		toChar
				.println("SUMMARY OF OBJECTS RObject[Z][Nindex][reacIndex] USED:");
		toChar
				.println("NOTE: Nindex = N for Z<3; Nindex = N - Noff = N - minNetN[Z] for Z>2");
		toChar.println();
		for (int i = 0; i < len1; i++) {
			for (int j = 0; j < len2; j++) {
				if (RObject[i][j] != null) {
					int len3 = RObject[i][j].length;
					for (int k = 0; k < len3; k++) {
						Z = i;
						if (Z > 2) {
							N = j + minNetN[Z];
						} else {
							N = j;
						}
						if (RObject[i][j][k] != null) {
							toChar.println("RObject[" + i + "][" + j + "][" + k
									+ "]   " + RObject[i][j][k].reacString
									+ "  Z=" + Z + " N=" + N + " Noff="
									+ (N - j) + " index=" + k + " serialIndex="
									+ serialIndex[Z][N][k] + " Active="
									+ !DataHolder.RnotActive[i][j][k]);
						}
					}
				}
			}
		}
	}
	
	
	// -----------------------------------------------------------------------------
	// Method to output network rate parameters for CUDA calculation
	// ----------------------------------------------------------------------------

	void writeCUDAratefile() {
		
		int len1 = RObject.length;
		int len2 = RObject[0].length;
		int Z = 0;
		int N = 0;
		
		int reactionCount = 0;

// 
// 		for (int i = 0; i < len1; i++) {
// 			for (int j = 0; j < len2; j++) {
// 				if (RObject[i][j] != null) {
// 					int len3 = RObject[i][j].length;
// 					for (int k = 0; k < len3; k++) {
// 						Z = i;
// 						if (Z > 2) {
// 							N = j + minNetN[Z];
// 						} else {
// 							N = j;
// 						}
// 						if (RObject[i][j][k] != null) {
// 							// Output rates for CUDA test data file
// 							toRateData.println(
// 								  RObject[i][j][k].p0+" "
// 								+ RObject[i][j][k].p1+" "
// 								+ RObject[i][j][k].p2+" "
// 								+ RObject[i][j][k].p3+" "
// 								+ RObject[i][j][k].p4+" "
// 								+ RObject[i][j][k].p5+" "
// 								+ RObject[i][j][k].p6+" "
// 								+ RObject[i][j][k].Q+" "
// 								+ replaceWhiteSpace(RObject[i][j][k].reacString, "")
// 							);
// 						}
// 					}
// 				}
// 			}
// 		}
// 		toRateData.flush();
// 		System.out.println("  --> CUDA rate parameters written in output/rateLibrary.data\n");
		
		
		// NOTE: The reactiongroup class properties are accessible, as illustrated below.  Above\
		// uses RObject objects to write data. Below uses RGgroup objects (type ReactionGroup) to
		// write data. This may be preferable, since the ReactionGroup objects are designed to
		// implement PE systematically.
		
		
		toChar.println("\n----------------\nCUDA Output\n----------------");
		toChar.println("\nNetwork Vector:");
		for(int m=0; m<numberActiveIsotopes; m++){
			toChar.println(m+" "+netVector[m].symbol+" Z="+netVector[m].Z
				+" N="+netVector[m].N+" A="+(int)netVector[m].A
			);
		}
		
		for (int i = 0; i < numberReactionGroups; i++) {
			toChar.println();
			toChar.println("\nREACTION GROUP " + i+":");
			for (int j = 0; j < RGgroup[i].members; j++) {
				reactionCount ++;
				int boolInt = 0;
				if(RGgroup[i].reactions[j].forward) boolInt=1;
				
				// To access RObject[][][] objects we must take into account the
				// possible offset of the neutron number from its storage index
				// used for the RObject[][][] array.
				
				Z = RGgroup[i].reactions[j].Z;
				N = RGgroup[i].reactions[j].nindex;  // N offset by minNetN[Z]
// 				if (Z > 2) {
// 					N = RGgroup[i].reactions[j].nindex;
// 					//N = RGgroup[i].reactions[j].N - minNetN[Z];
// 				} else {
// 					N = RGgroup[i].reactions[j].N;
// 				}
				
				int mm = RGgroup[i].reactions[j].reacIndex;
				
				String reacLabel = replaceWhiteSpace(RGgroup[i].reactions[j].reacString, "");
				int reaclibClass = RGgroup[i].reactions[j].reacClass;
				int RGclass = RGgroup[i].reactions[j].reacGroupClass;
				int RGmemberIndex = j;
				//int nbody = RGgroup[i].reactions[j].numberReactants;
				int numReactants = RGgroup[i].reactions[j].numberReactants;
				int numProducts = RGgroup[i].reactions[j].numberProducts;
				int isEC = boolToInt(RObject[Z][N][mm].ecFlag);
				int isReverse = boolToInt(RObject[Z][N][mm].reverseR);
				// Compute the statistical factor for this reaction			
				double prefac = computePrefac(reaclibClass, RObject[Z][N][mm].isoIn);
				double Qvalue = RObject[Z][N][mm].Q;
				
				toRateData.println(
					reacLabel + " " +
					RGclass + " " +
					RGmemberIndex + " " +
					reaclibClass + " " +	
					numReactants + " " +
					numProducts + " " +
					isEC + " " +
					isReverse + " " +
					deci(8,prefac) + " " +
					deci(5,Qvalue)
				);
				
				double p0 = RObject[Z][N][mm].p0;
				double p1 = RObject[Z][N][mm].p1;
				double p2 = RObject[Z][N][mm].p2;
				double p3 = RObject[Z][N][mm].p3;
				double p4 = RObject[Z][N][mm].p4;
				double p5 = RObject[Z][N][mm].p5;
				double p6 = RObject[Z][N][mm].p6;
				
				toRateData.println(
					deci(8, p0) + " " +
					deci(8, p1) + " " +
					deci(8, p2) + " " +
					deci(8, p3) + " " +
					deci(8, p4) + " " +
					deci(8, p5) + " " +
					deci(8, p6)
				);
				
				
				toChar.println("\n  " + j + "  Z=" + RGgroup[i].reactions[j].Z
					+ " N=" + RGgroup[i].reactions[j].N + " m="
					+ RGgroup[i].reactions[j].reacIndex + " ser="
					+ RGgroup[i].reactions[j].serialIndex + " class="
					+ reaclibClass + " react=" + numReactants + " prod="
					+ numProducts + " rgclass=" + RGclass + "  " + reacLabel + " ("
					+ RGgroup[i].reactions[j].resonanceType + ")"
					+ " forward=" + RGgroup[i].reactions[j].forward
					+ " forward(int)=" + boolInt
					);
				
				
				toChar.println("     " +
					"p0="+deci(4,RObject[Z][N][mm].p0)+" p1="+deci(4,RObject[Z][N][mm].p1) +
					" p2="+deci(4,RObject[Z][N][mm].p2)+" p3="+deci(4,RObject[Z][N][mm].p3) +
					" p4="+deci(4,RObject[Z][N][mm].p4)+" p5="+deci(4,RObject[Z][N][mm].p5) +
					" p6="+deci(4,RObject[Z][N][mm].p6)+" Q="+deci(4,RObject[Z][N][mm].Q) 
				);
				
				boolInt = 0;
				if(RObject[Z][N][mm].ecFlag) boolInt=1;
				int boolInt2 = 0;
				if(RObject[Z][N][mm].reverseR) boolInt2 = 1;
				toChar.println("     " +
					"RG="+RGgroup[i].RGindex
					+" RGclass="+RGgroup[i].RGclass //reactions[j].reacGroupClass
					+" EC="+RObject[Z][N][mm].ecFlag
					+" ECint="+boolInt
					+" reverseR="+RObject[Z][N][mm].reverseR
					+" reverseRint="+boolInt2
					+" reaclibClass="+ RGgroup[i].reactions[j].reacClass
				);
				
				int num, num2; 
				String tempS, tempS2, tempS3, tempS4;
				
				int reactantZ [] = new int[5];
				int reactantN [] = new int[5];
				int productZ [] = new int[5];
				int productN [] = new int[5];
				
				// If forward
				if(RGgroup[i].reactions[j].forward){
					// Reactants
					num = RGgroup[i].reactions[j].numberReactants;
					reactantZ[0] = RGgroup[i].reactions[j].isoZ[0];
					reactantN[0] = RGgroup[i].reactions[j].isoN[0];
					tempS = "" + RGgroup[i].reactions[j].isoZ[0];
					tempS2 = "" + RGgroup[i].reactions[j].isoN[0];
					for (int p=1; p<num; p++){
						reactantZ[p] = RGgroup[i].reactions[j].isoZ[p];
						reactantN[p] = RGgroup[i].reactions[j].isoN[p];
						tempS += (" "+RGgroup[i].reactions[j].isoZ[p]);
						tempS2 += (" "+RGgroup[i].reactions[j].isoN[p]);
					}
					// Products
					num2 = RGgroup[i].reactions[j].numberProducts;
					productZ[0] = RGgroup[i].reactions[j].isoZ[num];
					productN[0] = RGgroup[i].reactions[j].isoN[num];
					tempS3 = "" + RGgroup[i].reactions[j].isoZ[num];
					tempS4 = "" + RGgroup[i].reactions[j].isoN[num];
					for (int p=num+1; p<num+num2; p++){
						productZ[p-num] = RGgroup[i].reactions[j].isoZ[p];
						productN[p-num] = RGgroup[i].reactions[j].isoN[p];
						tempS3 += (" "+RGgroup[i].reactions[j].isoZ[p]);
						tempS4 += (" "+RGgroup[i].reactions[j].isoN[p]);
					}
					toChar.println("     Zreact: {"+tempS+"}"+" Nreact: {"+tempS2+"}"
						+" Zprod: {"+tempS3+"}"+" Nprod: {"+tempS4+"}"
						+" num="+num+" num2="+num2
					);
				// If reverse
				} else {
					// Reactants
					int cut = RGgroup[i].reactions[j].numberProducts;
					num = RGgroup[i].reactions[j].numberReactants;
					reactantZ[0] = RGgroup[i].reactions[j].isoZ[cut];
					reactantN[0] = RGgroup[i].reactions[j].isoN[cut];
					tempS = "" + RGgroup[i].reactions[j].isoZ[cut];
					tempS2 = "" + RGgroup[i].reactions[j].isoN[cut];
					for (int p=cut+1; p<cut+num; p++){
						reactantZ[p-cut] = RGgroup[i].reactions[j].isoZ[p];
						reactantN[p-cut] = RGgroup[i].reactions[j].isoN[p];
						tempS += (" "+RGgroup[i].reactions[j].isoZ[p]);
						tempS2 += (" "+RGgroup[i].reactions[j].isoN[p]);
					}
					// Products
					num2 = RGgroup[i].reactions[j].numberProducts;
					productZ[0] = RGgroup[i].reactions[j].isoZ[0];
					productN[0] = RGgroup[i].reactions[j].isoN[0];
					tempS3 = "" + RGgroup[i].reactions[j].isoZ[0];
					tempS4 = "" + RGgroup[i].reactions[j].isoN[0];
					for (int p=1; p<num2; p++){
						productZ[p] = RGgroup[i].reactions[j].isoZ[p];
						productN[p] = RGgroup[i].reactions[j].isoN[p];
						tempS3 += (" "+RGgroup[i].reactions[j].isoZ[p]);
						tempS4 += (" "+RGgroup[i].reactions[j].isoN[p]);
					}
					toChar.println("     Zreact: {"+tempS+"}"+" Nreact: {"+tempS2+"}"
						+" Zprod: {"+tempS3+"}"+" Nprod: {"+tempS4+"}"
						+" num="+num+" num2="+num2+" cut="+cut
					);
				}
				
				String temper = "";
				
				for (int jj=0; jj<numReactants; jj++){
					temper += (reactantZ[jj] + " ");
				}		
				toRateData.println(temper);
				
				temper = "";
				for (int jj=0; jj<numReactants; jj++){
					temper += (reactantN[jj] + " ");
				}
				toRateData.println(temper);
				
				temper = "";
				for (int jj=0; jj<numProducts; jj++){
					temper += (productZ[jj] + " ");
				}		
				toRateData.println(temper);
				
				temper = "";
				for (int jj=0; jj<numProducts; jj++){
					temper += (productN[jj] + " ");
				}
				toRateData.println(temper);
				
				temper = "";
				for (int jj=0; jj<numReactants; jj++){
					temper += (returnNetIndex(reactantZ[jj], reactantN[jj]) + " ");
				}		
				toRateData.println(temper);
				
				temper = "";
				for (int jj=0; jj<numProducts; jj++){
					temper += (returnNetIndex(productZ[jj], productN[jj]) + " ");
				}		
				toRateData.println(temper);
				
				
				// Have to handle the RGclass = -1 case (4-bodies on right side) special.
				// Ignore it for now.
				
				if(RGgroup[i].niso > 0){
					for(int p=0; p<RGgroup[i].niso; p++){
					//for(int p=0; p<RGgroup[i].nspecies[RGgroup[i].RGclass-1]; p++){
						int zz = RGgroup[i].reactions[j].isoZ[p];
						int nn = RGgroup[i].reactions[j].isoN[p];
						toChar.println("     Z="+zz+" N=" +nn + " netIndex="
							+returnNetIndex(zz,nn)
						);
					}
				}
				//toChar.println("za="+RGgroup[i].reactions[j].za+" zb="+RGgroup[i].reactions[j].zb);
				
				// Output the reaction vector for this reaction
				tempS = ""+RGgroup[i].reactions[j].rVector[0];
				for (int p = 1; p<numberActiveIsotopes; p++){
					tempS += (" "+RGgroup[i].reactions[j].rVector[p]); 
				}
				toChar.println("     RV=[" + tempS+"]");
				toChar.println("     prefac="+ deci(8,prefac));
			}
		}
		toChar.println("\n--------------------\nEnd CUDA Output\n--------------------\n");
		
		toRateData.flush();
		toChar.println("  --> CUDA rate parameters written in output/rateLibrary.data\n");
	}
	
	
	// --------------------------------------------------------
    //  Helper method to convert boolean variables (false, true)
    //  to int variable (0, 1) for use in C for CUDA output
    // --------------------------------------------------------
	
	int boolToInt(boolean bool){
		int boolInt = 0;
		if(bool) boolInt = 1;
		return boolInt;
	}
	
	
	// --------------------------------------------------------
    //  Method to compute statistical factor for CUDA output
    // --------------------------------------------------------

    double computePrefac(int reaclibClass, Point[] isoIn) {

        double prefac=1.0;

        if( reaclibClass > 3 && reaclibClass < 8) {
            if(isoIn[0].x == isoIn[1].x && isoIn[0].y == isoIn[1].y){
                prefac=0.5;
            }
        }

        if (reaclibClass == 8) {
            if( isoIn[0].x == isoIn[1].x
                    && isoIn[1].x == isoIn[2].x
                    && isoIn[0].y == isoIn[1].y
                    && isoIn[1].y == isoIn[2].y) {
                prefac=1.0/6.0;
            }
        }
        return prefac;
    }
	
	

	// -----------------------------------------------------------------------------------
	// Utility method to check which reactions are active in
	// DataHolder.RnotActive[][][]
	// -----------------------------------------------------------------------------------
	
	static void checkActiveReactions(int Z, int N) {
		int len = DataHolder.RnotActive[0][0].length;
		for (int i = 0; i < len; i++) {
			System.out.println("Active Reactions: Z=" + Z + " N=" + N + " i="
					+ i + " " + !DataHolder.RnotActive[Z][N][i]);
		}
	}

	// ----------------------------------------------------------------------------------
	// Utility method to output partial equilibrium quantities for each reaction
	// group. Call after timestep has been taken.
	// ----------------------------------------------------------------------------------

	public void showEquilibriumStuff() {
		
		String equilString = deci(6, time) + " " + deci(6, deltaTime);
		String tauString = deci(6, LOG10 * Math.log(time)) + " "
				+ deci(5, LOG10 * Math.log(deltaTimeRestart)) + " ";
		String tauRatioString = deci(6, LOG10 * Math.log(time)) + " "
				+ deci(5, LOG10 * Math.log(deltaTimeRestart)) + " ";
		
		// Output Y0 and Yfinal for each isotope to toChar stream
		toChar.println();
		toChar
				.println("Initial and final Y[Z][N] and final X[Z][N] for timestep:");
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (IsotopePad.isoColor[i][j])
					toChar.println("t=" + deci(8, time) + " Y0[" + i + "][" + j
							+ "]=" + deci(6, Yzero[i][j]) + " Y[" + i + "]["
							+ j + "]=" + deci(6, Y[i][j]) + " X[" + i + "]["
							+ j + "]=" + deci(6, Y[i][j] * (double) (i + j)));
			}
		}
		
		// Loop over all reaction groups
		
		for (int i = 0; i < numberReactionGroups; i++) {
			tauString += deci(3, LOG10 * Math.log(RGgroup[i].tau)) + " ";
			// Stuff computed in computeEquilibriumRates(). Put double star if
			// in equilibrium
			String frontString = "RG ";
			if (RGgroup[i].isEquil)
				frontString = "**" + frontString;
			toChar.println("\n" + frontString + RGgroup[i].RGindex
					+ ": members=" + RGgroup[i].members + " kf="
					+ deci(4, RGgroup[i].rgkf) + " kr="
					+ deci(4, RGgroup[i].rgkr) + " netFlux="
					+ deci(5, (RGgroup[i].sumFluxes() / nT)) + "  Ref:  "
					+ RGgroup[i].reactions[RGgroup[i].refreac].reacString);
			// Stuff computed in putY0()
			for (int k = 0; k < RGgroup[i].niso; k++) {
				toChar
						.println("Put Y0:  t="
								+ deci(8, time)
								+ " RG="
								+ RGgroup[i].RGindex
								+ "  "
								+ RGgroup[i].reacGroupSymbol
								+ "  Z="
								+ RGgroup[i].isoZ[k]
								+ " N="
								+ RGgroup[i].isoN[k]
								+ " Y0="
								+ deci(
										6,
										Yzero[RGgroup[i].isoZ[k]][RGgroup[i].isoN[k]]));
			}
			// Stuff computed in computeC()
			String cstring = "";
			for (int k = 0; k < RGgroup[i].numberC; k++) {
				cstring += (" c" + k + "=" + deci(6, RGgroup[i].crg[k]));
			}
			toChar.println("RGclass=" + RGgroup[i].RGclass + "  "
					+ RGgroup[i].reactions[RGgroup[i].refreac].reacString
					+ "  " + cstring);
			// Stuff computed in computeQuad()
			if (RGgroup[i].RGclass < 5) {
				toChar.println(// RGgroup[i].reactions[RGgroup[i].refreac].reacString
						"a=" + deci(6, RGgroup[i].aa) + " b="
								+ deci(6, RGgroup[i].bb) + " c="
								+ deci(6, RGgroup[i].cc));
			} else {
				toChar.println(// RGgroup[i].reactions[RGgroup[i].refreac].reacString
						"a=" + deci(6, RGgroup[i].aa) + " b="
								+ deci(6, RGgroup[i].bb) + " c="
								+ deci(6, RGgroup[i].cc) + " alpha="
								+ deci(6, RGgroup[i].alpha) + " beta="
								+ deci(6, RGgroup[i].beta) + " gamma="
								+ deci(6, RGgroup[i].gamma));
			}
			if (RGgroup[i].members > 1) {
				if (RGgroup[i].qq > 0)
					toChar.println("$$$$ Error: q=" + RGgroup[i].qq
							+ " is not negative in RG " + RGgroup[i].RGindex
							+ " at t=" + time);
				toChar.println("q=" + deci(6, RGgroup[i].qq) + " tau="
						+ deci(6, RGgroup[i].tau) + " dt="
						+ deci(6, deltaTimeRestart) + " Y0_eq="
						+ deci(4, RGgroup[i].isoYeq[0]) + " Y0="
						+ deci(4, RGgroup[i].isoY[0]));
				String pops = "";
				String eqs = "";
				for (int k = 1; k < RGgroup[i].niso; k++) {
					pops += ("Y" + k + "_eq=" + deci(4, RGgroup[i].isoYeq[k])
							+ " Y" + k + "=" + deci(4, RGgroup[i].isoY[k]) + " ");
				}
				for (int k = 0; k < RGgroup[i].niso; k++) {
					eqs += ("R" + k + "=" + deci(6, RGgroup[i].eqcheck[k]) + " ");
				}
				eqs += (" (Rmax/equiTol="
						+ deci(4, RGgroup[i].maxeqcheck / equiTol) + ")");
				toChar.println(pops);
				toChar.println(eqs);
				toChar
						.println("YRatio="
								+ deci(5, RGgroup[i].equilRatio)
								+ " kratio="
								+ deci(5, RGgroup[i].kratio)
								+ " frac diff="
								+ deci(4, Math.abs(RGgroup[i].kratio
										- RGgroup[i].equilRatio)
										/ RGgroup[i].kratio) + " dt/tau="
								+ deci(4, deltaTimeRestart/RGgroup[i].tau)
								+ " isEquil="
								+ ("" + RGgroup[i].isEquil).toUpperCase()); // To
																			// get
																			// boolean->string->uppercase
			}

			equilString += " "
					+ i
					+ " "
					+ deci(5, RGgroup[i].maxeqcheck / equiTol)
					+ " "
					+ deci(4, Math.abs(RGgroup[i].kratio
							- RGgroup[i].equilRatio)
							/ RGgroup[i].kratio) + " "
					+ deci(4, RGgroup[i].tau / deltaTimeRestart);
					
			//tauRatioString += deci(4, LOG10*Math.log(RGgroup[i].tau/deltaTimeRestart))+" ";
			//tauRatioString += deci(4, LOG10*Math.log(deltaTimeRestart/RGgroup[i].tau))+" ";
			tauRatioString += deci(4, deltaTimeRestart/RGgroup[i].tau)+" ";
		} // End loop over reaction groups
		
		tauString += deci(3, LOG10 * Math.log(1 / fastestRate[tintNow - 1]))
				+ " ";
		tauString += deci(3, LOG10 * Math.log(fastestPEtimescale)) + " ";
		tauString += deci(3, LOG10 * Math.log(fastestPEtimescale2)) + " ";
		tauString += deci(3, LOG10 * Math.log(fastestPEtimescaleAVG)) + " ";
		if (outputTauPlot && time > equilibrateTime)
			toTau.println(tauString);
		// Flush buffer so Maple plots can be made during the calculation
		toTau.flush();
		toChar
			.println("--------------------------------------------------------------------------------");

		if (outputEquilPlot && time > equilibrateTime)
			toEquil.println(equilString);
		
		if(time > equilibrateTime){
			toTauRatio.println(tauRatioString);
			// Flush buffer so Maple plots can be made during the calculation
			toTauRatio.flush();
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------------
	// Diagnostic method to send to toChar -> stochastic.tmp data on parameters
	// needed for equilibrium
	// calculations. Should be called after integration step but before
	// Yzero[][] is updated for
	// the next timestep.
	// --------------------------------------------------------------------------------------------------------------------------------------

	void dumpEquilibriumData() {
		toChar.println("\nSUMMARY: PARTIAL EQUILIBRIUM DATA (time="
				+ deci(6, time) + " dt=" + deci(6, deltaTimeRestart) + ")\n");

		// Values of Yzero and final Y for timestep
		toChar.println("Initial and final abundances for timestep:\n");
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (isInNet(i, j))
					toChar.println("Y0[" + i + "][" + j + "]="
							+ deci(6, Yzero[i][j]) + "  Y[" + i + "][" + j
							+ "]=" + deci(6, Y[i][j]));
			}
		}

		// k values
		toChar
				.println("\nForward and reverse rates (masterRates[i][j][k], not multiplied by density factors yet):\n");
		for (int i = 0; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				int downer = 0;
				if (i > 2)
					downer = minNetN[i];
				for (int k = 0; k < numberReactions[i][j - downer]; k++) {
					toChar.println("Z=" + i + " N=" + j + "   [" + i + "]["
							+ (j - downer) + "][" + k + "]   "
							+ RObject[i][j - downer][k].reacString + "   k="
							+ deci(4, masterRates[i][j][k]));
				}
			}
		}
		toChar
				.println("--------------------------------------------------------------------------------------------------------------------------------");
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------
	// NOTE: superceded by writeReactionFluxesZNall() below.
	// Diagnostic method to send all reaction fluxes to the toChar stream.
	// Fluxes are divided by nT, so
	// they are in units of Y/second. This method sorts according to Z and N.
	// See also the method
	// writeReactionFluxesRG() which does the same thing but sorted according to
	// reaction group.
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	void writeReactionFluxesZN() {
		toChar
				.println("\n--------------------------------------------------------------------------------------------------------------------------------");
		toChar.println("REACTION FLUXES (units=Y/s):  t=" + deci(6, time)
				+ " dt=" + deci(6, dtAvg()));
		toChar
				.println("--------------------------------------------------------------------------------------------------------------------------------");

		// Display according to Z and N
		for (int i = 0; i < numberSeeds; i++) {
			int Z = seedProtonNumber[i];
			int N = seedNeutronNumber[i];
			int downer = 0;
			if (Z > 2)
				downer = minNetN[Z];
			int nReactions = numberReactions[Z][N - downer];
			double fracF = (Fplus[Z][N] - Fminus[Z][N])
					/ (Fplus[Z][N] + Fminus[Z][N]);
			toChar.println("Z=" + Z + " N=" + N + " F+ = "
					+ deci(10, (Fplus[Z][N] / nT)) + " F- = "
					+ deci(10, (Fminus[Z][N] / nT)) + " F+/F- = "
					+ deci(10, Fplus[Z][N] / Fminus[Z][N]) + " kdt="
					+ deci(2, keff[Z][N] * dtAvg()) + " Asy="
					+ isAsymptotic[Z][N]);
			for (int j = 0; j < nReactions; j++) {
				int rg = RGC[Z][N][j];
				int memindex = RGgroup[rg].getMemberIndex(Z, N, j);
				toChar.println("    " + RObject[Z][N - downer][j].reacString
						+ " RG=" + rg + "  F=" + deci(6, flux[i][j] / nT));
			}
		}
		toChar
				.println("--------------------------------------------------------------------------------------------------------------------------------");
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------
	// Diagnostic method to send all reaction fluxes to the toChar stream.
	// Fluxes are divided by nT, so
	// they are in units of Y/second. This method sorts according to Z and N.
	// See also the method
	// writeReactionFluxesRG() which does the same thing but sorted according to
	// reaction group. This
	// replaces writeRactionFluxesZN, which only looped over the heavy seeds.
	// This loops over all isotopes.
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	void writeReactionFluxesZNall() {
		toChar
				.println("\n--------------------------------------------------------------------------------------------------------------------------------");
		toChar.println("REACTION FLUXES (units=Y/s):  t=" + deci(6, time)
				+ " dt=" + deci(6, dtAvg()));
		toChar
				.println("--------------------------------------------------------------------------------------------------------------------------------");

		// Display according to Z and N

		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				if (isInNet(i, j)) {
					int Z = i;
					int N = j;
					int downer = 0;
					if (Z > 2)
						downer = minNetN[Z];
					int nReactions = numberReactions[Z][N - downer];
					toChar.println("Z=" + Z + " N=" + N + " F+ = "
							+ deci(10, (Fplus[Z][N] / nT)) + " F- = "
							+ deci(10, (Fminus[Z][N] / nT)) + " F+/F- = "
							+ deci(10, Fplus[Z][N] / Fminus[Z][N]) + " kdt="
							+ deci(2, keff[Z][N] * dtAvg()) + " Asy="
							+ isAsymptotic[Z][N]);
					for (int k = 0; k < nReactions; k++) {
						int rg = RGC[Z][N][k];
						int memindex = RGgroup[rg].getMemberIndex(Z, N, k);
						toChar.println("    "
								+ RObject[Z][N - downer][k].reacString
								+ " RG="
								+ rg
								+ "  F="
								+ deci(6, RGgroup[rg].reactions[memindex].flux
										/ nT));
					}
				}
			}
		}
		toChar
				.println("--------------------------------------------------------------------------------------------------------------------------------");
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------------
	// Diagnostic method to send all reaction fluxes to the toChar stream.
	// Fluxes are divided by nT, so
	// they are in units of Y/second. This method sorts according to reaction
	// group. See also the method
	// writeReactionFluxesZN() which does the same thing but sorted according to
	// Z and N.
	// -----------------------------------------------------------------------------------------------------------------------------------------------------------

	void writeReactionFluxesRG() {
		toChar
				.println("\n--------------------------------------------------------------------------------------------------------------------------------");
		toChar.println("RATES (units=1/s) and REACTION FLUXES (units=Y/s):  t="
				+ deci(6, time) + " dt=" + deci(6, dtAvg()));
		toChar
				.println("--------------------------------------------------------------------------------------------------------------------------------");

		// Display according to reaction group
		Reaction tr;
		for (int i = 0; i < numberReactionGroups; i++) {
			toChar.println("Reaction Group " + i + ":  sumF="
					+ deci(6, RGgroup[i].sumFluxes() / nT) + " kf="
					+ deci(6, RGgroup[i].rgkf) + " kr="
					+ deci(6, RGgroup[i].rgkr) + " isEquil="
					+ RGgroup[i].getEquil());
			for (int j = 0; j < RGgroup[i].members; j++) {
				tr = RGgroup[i].reactions[j];
				toChar.println("   " + tr.reacString + " rate="
						+ deci(6, tr.rate) + " s^(-1)  1/rate="
						+ deci(6, 1 / tr.rate) + " s" + "  F="
						+ deci(6, tr.flux / nT) + " Y/s");
			}
		}
		toChar
				.println("--------------------------------------------------------------------------------------------------------------------------------");
		toChar.println();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to return string of reactions that are in partial equilibrium
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	private String returnEquilibratedReactionString() {
		String ts = "Equil: | ";
		for (int i = 0; i < numberReactionGroups; i++) {
			if (RGgroup[i].isEquil) {
				String temp = RGgroup[i].reactions[RGgroup[i].refreac].reacString;
				temp = temp.replaceFirst("-->", "<->");
				ts += temp + "|";
			}
		}
		return ts;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to adjust populations at end of timestep when in partial
	// equilibrium to correct for deviations
	// from equilibrium during the timestep. Uses progress variables. This
	// method sets the progress variable
	// for each reaction group to its equilibrium value at the end of the
	// numerical timestep, and then sets
	// the corresponding equilibrium values of other isotopes in the reaction
	// group (since they are directly
	// related to the equilibrium value of the progress variable for the
	// reaction group). Then, the corrected
	// abundances Y for all isotopes participating in partial equilibrium are
	// averaged if they participate in
	// more than one reaction group. Finally, after Ys are updated by their
	// average equilibrium values, all
	// abundances are rescaled so that total nucleon number is conserved by the
	// overall timestep. Thus
	// this method for restoring equilbrilum does not require a matrix solution
	// or Newton-Raphson iteration.
	// It should scale approximately linearly with the network size. Contrast
	// with the different approach
	// taken in the method restoreEquilibrium().
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	private void restoreEquilibriumProg() {
		int Z;
		int N;
		countConstraints = 0;
		countEquilIsotopes = 0;
		int Ydim = 500; // Reaction groups in equilibrium
		int RGindex[] = new int[Ydim]; // Array to hold index of RG in
										// equilibrium
		double sumXNeq = 0.0;
		double sumXeq = 0.0;
		sumX = sumMassFractions();

		if (displayE)
			System.out
					.println("Restoring equilibrium using method restoreEquilibriumProg():");

		// In general when we compute the equilibrium value of say alpha in the
		// reaction group
		// alpha+16O <-> 20Ne, we are computing it using non-equilibrium values
		// of 16O and 20Ne
		// (i.e., their values will not be the values that they will have after
		// this step). Add a while loop
		// that permits iteration to try to fix this. Preliminary tests indicate
		// it has essentially no effect
		// except that things go crazy if too may iterations.

		int itcounter = 0;
		while (itcounter < 1) {
			countConstraints = 0;
			countEquilIsotopes = 0;
			itcounter++;

			// if(displayE)
			// System.out.println("--------- Iteration "+itcounter+" ------------------------");

			// Compute equilibrium value of the Ys participating in equilibrium
			// starting from the
			// value of Y at the end of the numerical timestep, presently stored
			// in Y[Z][N]. Do so
			// by first setting StochasticElements.Yzero[Z][N] to the current
			// value of Y[Z][N], which
			// is the numerically computed value at the END of the timestep.
			// Then evolve that
			// initial value to the corresponding equilibrium value
			// algebraically by calculating the
			// equilibrium value for that Yzero[Z][N] and setting Y[Z][N] to it
			// (a form of operator
			// splitting within the network timestep).

			evolveToEquilibrium();

			// Inventory reaction groups in equilibrium

			for (int i = 0; i < numberReactionGroups; i++) {
				if (RGgroup[i].isEquil) {
					RGindex[countConstraints] = i;
					countConstraints++;
					if (displayE)
						System.out.println("  RG=" + i + " "
								+ RGgroup[i].reactions[0].reacString);
					for (int j = 0; j < RGgroup[i].niso; j++) {
						Z = RGgroup[i].isoZ[j];
						N = RGgroup[i].isoN[j];
						if (!isotopeInEquil[Z][N]) {
							isotopeInEquil[Z][N] = true;
							countEquilIsotopes++;
						}
						if (displayE) {
							System.out.println("    Z=" + Z + " N=" + N
									+ " Ynum=" + deci(8, Y[Z][N]) + " Yeq="
									+ deci(8, RGgroup[i].isoYeq[j]));
						}
					}
				}
			}

			// Check mass fractions separately for isotopes participating in
			// equilibrium and those not
			sumXeq = sumXEquil();
			sumXNeq = sumXNotEquil();

			// Loop over reaction groups in equilibrium and compute equilibrated
			// Y[Z][N]
			// averaged over all reaction groups that are in equilibrium and
			// contain the isotope [Z][N].

			int numberCases = 0;
			double Ysum = 0;
			if (displayE) {
				System.out
						.println("\n---- "
								+ totalTimeSteps
								+ " t="
								+ deci(6, time)
								+ " equilReac="
								+ totalEquilReactions
								+ " ----------------------------------------------------------");
			}
			for (int i = minNetZ; i <= maxNetZ; i++) {
				int indy = Math.min(maxNetN[i], nmax - 1);
				for (int j = minNetN[i]; j <= indy; j++) {
					if (isotopeInEquil[i][j]) {
						numberCases = 0;
						if (displayE)
							System.out.println("Z=" + i + " N=" + j + " Y="
									+ deci(8, Y[i][j]));
						Ysum = 0;
						// Loop over only the RG in equi (countConstraints of
						// them; (RGindex[] holds their indices)
						for (int k = 0; k < countConstraints; k++) {
							int rgin = RGindex[k];
							// Loop over isotopes within this equilibrated, RG
							// checking for match
							for (int m = 0; m < RGgroup[rgin].niso; m++) {
								if (i == RGgroup[rgin].isoZ[m]
										&& j == RGgroup[rgin].isoN[m]) {
									Ysum += RGgroup[rgin].isoYeq[m];
									if (displayE) {
										System.out
												.println("  RG="
														+ RGgroup[rgin].RGindex
														+ " "
														+ RGgroup[rgin].reactions[0].reacString
														// +" Y="+deci(8,RGgroup[rgin].isoY[m])
														+ " Yeq="
														+ deci(
																8,
																RGgroup[rgin].isoYeq[m]));
									}
									numberCases++;
								}
							}
						}
						// Store Y averaged over all reaction groups in which it
						// participates
						Y[i][j] = Ysum / (double) numberCases;
						if (displayE)
							System.out
									.println("  Avg. Yeq=" + deci(8, Y[i][j]));
					}
				}
			}
		} // end while loop

		// Set up renormalization of all Ys so that this total integration step
		// conserves particle number

		double sumXeqTemp = sumXEquil();
		XcorrFac = 1 / (sumXNeq + sumXeqTemp); // Factor to enforce particle
												// number conservation
		sumXeq = sumXeqTemp;
		sumXNeq = sumXNotEquil();
		// Loop over all Ys and renormalize
		for (int i = minNetZ; i <= maxNetZ; i++) {
			int indy = Math.min(maxNetN[i], nmax - 1);
			for (int j = minNetN[i]; j <= indy; j++) {
				Y[i][j] *= XcorrFac;
				pop[i][j] = Y[i][j] * nT;
			}
		}
		sumX = sumMassFractions();
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to set abundances for reaction groups in equilibrium to the
	// current numerically integrated value
	// and then evolve the abundances algebraically to their equilibrium values.
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	public void evolveToEquilibrium() {
		int Z, N;
		// Set Y0 = current Y (at end of numerical step) for all RG in
		// equilibrium and recompute equil
		for (int i = 0; i < numberReactionGroups; i++) {
			if (RGgroup[i].isEquil) {
				for (int j = 0; j < RGgroup[i].niso; j++) {
					Z = RGgroup[i].isoZ[j];
					N = RGgroup[i].isoN[j];
					Yzero[Z][N] = Y[Z][N];
				}
				// Compute equilibrium with new values of Y0
				RGgroup[i].computeEquilibrium();
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to adjust populations at end of timestep when in partial
	// equilibrium to correct for deviations
	// from equilibrium during the timestep. This method sets all abundances to
	// their equilibrium values at
	// the end of a timestep, and enforces conservation of particle number, by a
	// Newton-Raphson iteration
	// with each step requiring a matrix solution. Contrast with the different
	// approach taken in the method
	// restoreEquilibriumProg().
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	private void restoreEquilibrium() {
		boolean displayDetails2 = false;
		XeqInit = 0;
		int Z, N;
		int index = -1;
		double YY, Yeq;
		int Ydim = 100;
		double tempY[] = new double[Ydim];
		int tempYZ[] = new int[Ydim];
		int tempYN[] = new int[Ydim];
		int RGindex[] = new int[Ydim];
		int nisoIndex[] = new int[Ydim];
		int Yin = 0;
		int Fin = 0;

		// First loop over reaction groups and count constraint equations and
		// corresponding
		// number of independent isotopes participating in equilibrium

		int countEquations = 0;
		int countIsotopes = 0;
		double maxDevEquil = 0.0;
		double devious = 0.0;

		if (displayE) {
			System.out.println();
			System.out
					.println("  Partial equilibrium disturbed at end of numerical timestep "
							+ totalTimeSteps + ":");
		}

		for (int i = 0; i < numberReactionGroups; i++) {
			if (RGgroup[i].isEquil) {
				if (imposeEquil && displayDetails)
					System.out.println("  Reaction group " + i
							+ " in equilibrium ");
				for (int j = 0; j < RGgroup[i].niso; j++) {
					Z = RGgroup[i].isoZ[j];
					N = RGgroup[i].isoN[j];
					countEquations++;
					RGindex[countIsotopes] = i;
					nisoIndex[countIsotopes] = j;
					if (!isotopeInEquil[Z][N]) {
						isotopeInEquil[Z][N] = true;
						tempY[countIsotopes] = Y[Z][N];
						tempYZ[countIsotopes] = Z;
						tempYN[countIsotopes] = N;
						countIsotopes++;
					}

					// Store current value of Y as Yzero for all components of
					// reaction
					Yzero[Z][N] = Y[Z][N];
					if (imposeEquil && displayDetails)
						System.out.println("    RG " + i + ": Set Yzero[" + Z
								+ "][" + N + "]=Y[" + Z + "][" + N + "]="
								+ Yzero[Z][N]);
				}
				// Compute equilibrium for this RG with Yzero = Y(end of
				// timestep) as initial Ys
				if (imposeEquil && displayDetails)
					System.out
							.println("      Recompute partial equilibrium with these values of Yzero");
				RGgroup[i].computeEquilibrium();
				devious = Math.abs((RGgroup[i].equilRatio - RGgroup[i].kratio)
						/ RGgroup[i].kratio);
				if (devious > maxDevEquil)
					maxDevEquil = devious;
				if (imposeEquil && displayE)
					System.out.println("     RG=" + i + " eqRatio="
							+ deci(4, RGgroup[i].equilRatio) + " kr/kf="
							+ deci(4, RGgroup[i].kratio) + " Deviation="
							+ deci(4, devious));

			} else {
				// Possible actions if not in equilbrium
			}

		}

		if (displayE)
			System.out
					.println("  Before restoring equilibrium:  Max fractional deviation from equil = "
							+ deci(4, maxDevEquil)
							+ "  sumX="
							+ deci(4, sumX)
							+ "\n");

		totalEquilIsotopes = countIsotopes;
		if (normX)
			countEquations++;

		// Keep timestep in range so that actual pops at end of timestep do not
		// differ too much
		// from their equilibrium values

		if (maxDevEquil > 0.5) {
			callExit("Exit maxDevEquil=" + maxDevEquil);
		}

		if (maxDevEquil > 0.25 && time > startLinear) {
			dtScale *= 0.99;
			System.out.println("++++++ Resetting dtScale=" + deci(4, dtScale));
		} else if (maxDevEquil < 0.05) {
			dtScale *= 1.0;
		}

		if (displayDetails2) {
			System.out.println("  Initial max fractional deviation from equil="
					+ deci(5, maxDevEquil));

			System.out
					.println("\n  Restore equilibrium using "
							+ countEquations
							+ " equations in "
							+ countIsotopes
							+ " isotopes. The initial isotope vector for iteration is:");
		}

		// Set up arrays needed for Newton-Raphson

		double Yvec[] = new double[countIsotopes];
		int YvecZ[] = new int[countIsotopes];
		int YvecN[] = new int[countIsotopes];
		double YvecA[] = new double[countIsotopes];
		double Fvec[] = new double[countEquations];
		double Yequil[] = new double[countEquations];
		int basisIndex[] = new int[countEquations];
		double Jac[][] = new double[countEquations][countIsotopes];

		// Fill arrays for Newton-Raphson from temp arrays

		for (int i = 0; i < totalEquilIsotopes; i++) {
			YvecZ[i] = tempYZ[i];
			YvecN[i] = tempYN[i];
			YvecA[i] = (double) (YvecZ[i] + YvecN[i]);
			Yvec[i] = tempY[i];
			if (displayDetails)
				System.out.println("$$$$$$$$$$ Z=" + YvecZ[i] + " N="
						+ YvecN[i] + " Yvec=" + Yvec[i] + " YvecA=" + YvecA[i]);
		}

		// Put non-zero Jacobian entries and also fill Yequil array

		countEquations = 0;
		for (int i = 0; i < numberReactionGroups; i++) {
			if (RGgroup[i].isEquil) {
				index = RGgroup[i].RGindex;
				for (int j = 0; j < RGgroup[i].niso; j++) {
					int z = RGgroup[i].isoZ[j];
					int n = RGgroup[i].isoN[j];
					int Jindex = findJacIndex(z, n, YvecZ, YvecN);
					basisIndex[countEquations] = Jindex;
					Yequil[countEquations] = RGgroup[i].isoYeq[j];
					Jac[countEquations][Jindex] = 1 / Yequil[countEquations];
					countEquations++;
				}
			}
		}

		if (displayE) {
			System.out
					.println("  Initial abundances for iteration set to final abundance for this numerical timestep:");
			for (int i = 0; i < totalEquilIsotopes; i++) {
				System.out.println("     Y[" + i + "]:  Z=" + YvecZ[i] + " N="
						+ YvecN[i] + " Y=" + deci(6, Yvec[i]) + " Yeq="
						+ deci(6, Yequil[i]) + " (Y-Yeq)="
						+ deci(6, (Yvec[i] - Yequil[i])));
			}
		}

		// Add Jacobian entries corresponding to particle number conservation
		if (normX) {
			for (int j = 0; j < totalEquilIsotopes; j++) {
				Jac[countEquations][j] = YvecA[j];
			}
		}

		// Sum X from equilibrium isotopes before starting iteration
		for (int i = 0; i < Yvec.length; i++) {
			XeqInit += Yvec[i] * YvecA[i];
		}

		// Sum X from non-equilibrium isotopes.
		Xneq = 0;
		for (int i = 0; i < netVector.length; i++) {
			if (!netVector[i].isEquil) {
				Xneq += Y[netVector[i].Z][netVector[i].N] * netVector[i].A;
			}
		}

		// Initial value of F vector
		Fvec = computeFvec(countEquations, basisIndex, Yvec, Yequil, YvecA,
				normX);

		if (displayDetails2) {
			System.out.println("\n  Initial values of the F vector:");
			for (int i = 0; i < Fvec.length; i++) {
				System.out.println("    F[" + i + "] = " + deci(6, Fvec[i]));
			}
		}

		if (displayDetails) {
			System.out.println("  Non-zero values of the Jacobian matrix:");
			for (int i = 0; i < Fvec.length; i++) {
				for (int j = 0; j < Yvec.length; j++) {
					if (Jac[i][j] != 0)
						System.out.println("    J[" + i + "][" + j + "] = "
								+ deci(6, Jac[i][j]));
				}
			}
		}

		int nit = 0;
		double xdev = 100;

		// Create object to do Newton-Raphson iteration

		NewtonRaphson test = new NewtonRaphson(Jac, Fvec, Yvec);

		// Compute initial xdev before start of iteration

		double tempxdev = 0;
		for (int i = 0; i < Fvec.length; i++) {
			tempxdev += Fvec[i] * Fvec[i];
		}
		tempxdev = Math.sqrt(tempxdev);

		if (displayDetails2)
			System.out
					.println("\n  Newton-Raphson iteration:\n    Iteration=0 xdev="
							+ deci(5, tempxdev));

		while (xdev > newtonTol && nit < maxit) {
			Yvec = test.doIteration(); // Holds the updated vector x + delx
			xdev = test.getxdev();
			if (displayDetails2)
				System.out.println("    Iteration=" + (nit + 1) + " xdev="
						+ deci(5, xdev));

			// Update the F matrix based on the new vector x + delx. Jacobian is
			// constant so it
			// does not need to be updated
			Fvec = computeFvec(countEquations, basisIndex, Yvec, Yequil, YvecA,
					normX);
			test.updateFMatrix(Fvec);
			masterNits = nit;
			nit++;

			double tempdelx[] = test.getdelx();
		}

		// Update the populations to restore equilibrium

		if (displayE)
			System.out
					.println("\n  Updated abundances to restore equilibrium after iteration:");

		for (int i = 0; i < totalEquilIsotopes; i++) {
			Y[YvecZ[i]][YvecN[i]] = Yvec[i];
			pop[YvecZ[i]][YvecN[i]] = Yvec[i] * nT;

			if (displayE)
				System.out.println("     Z=" + YvecZ[i] + " N=" + YvecN[i]
						+ " Yfinal=" + deci(6, Y[YvecZ[i]][YvecN[i]]) + " Yeq="
						+ deci(6, Yequil[i]) + " (Yfinal-Yeq)/Yeq="
						+ deci(6, (Yvec[i] - Yequil[i]) / Yequil[i]));

		}

		// Check max deviation from equilibrium after iteration

		if (displayE) {
			System.out.println();
			System.out
					.println("  Partial equilibrium restored at end of timestep "
							+ totalTimeSteps + ":");
		}

		maxDevEquil = 0.0;
		for (int i = 0; i < numberReactionGroups; i++) {
			if (RGgroup[i].isEquil) {
				RGgroup[i].computeEquilibrium();
				devious = Math.abs((RGgroup[i].equilRatio - RGgroup[i].kratio)
						/ RGgroup[i].kratio);
				if (devious > maxDevEquil)
					maxDevEquil = devious;

				if (displayE)
					System.out.println("     RG=" + i + " eqRatio="
							+ deci(4, RGgroup[i].equilRatio) + " kr/kf="
							+ deci(4, RGgroup[i].kratio) + " Deviation="
							+ deci(4, devious));

			}
		}

		sumX = sumMassFractions();

		if (displayE) {
			String temppp = "  After restoring equilibrium:  Max fractional deviation from equil = "
					+ deci(4, maxDevEquil) + "  sumX=" + deci(4, sumX);
			if (maxDevEquil > 0.05 || Math.abs(sumX - 1) > 0.01)
				temppp += " *****";
			System.out.println(temppp);
		}

	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to compute F(x) for the general case. Used by
	// restoreEquilibrium(). Returns a vector that is
	// the difference of the vectors Y and Yeq divided by Yeq except (if
	// normX=true) the last entry is the
	// difference between sum of X and 1.
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	double[] computeFvec(int numberEquations, int index[], double Yit[],
			double Yequil[], double YitA[], boolean normX) {

		boolean displayDetails2 = false;
		double[] f;
		if (normX) {
			f = new double[numberEquations + 1]; // If impose sum X = 1
		} else {
			f = new double[numberEquations]; // If no requirement on sum X
		}
		int countEquations = 0;

		// Compute the vector F
		for (int i = 0; i < numberReactionGroups; i++) {
			if (RGgroup[i].isEquil) {
				for (int j = 0; j < RGgroup[i].niso; j++) {
					int z = RGgroup[i].isoZ[j];
					int n = RGgroup[i].isoN[j];
					f[countEquations] = Yit[index[countEquations]]
							/ Yequil[countEquations] - 1.0;

					if (displayDetails2) {
						System.out
								.println("Fvec="
										+ deci(6, f[countEquations])
										+ " Yit="
										+ deci(6, Yit[index[countEquations]])
										+ " Yeq="
										+ deci(6, Yequil[countEquations])
										+ " RG="
										+ i
										+ " "
										+ RGgroup[i].reactions[RGgroup[i].refreac].reacString
										+ " Z=" + z + " N=" + n);
					}

					countEquations++;
				}
			}
		}

		// Add the equation requiring that sum X = 1 if normX = true

		if (normX) {

			double Xeq = 0;

			// Sum X from equilibrium isotopes
			for (int i = 0; i < Yit.length; i++) {
				Xeq += Yit[i] * YitA[i];
				if (displayDetails)
					System.out.println("   i=" + i + " Yit=" + Yit[i] + " A="
							+ YitA[i] + " Xeq=" + Xeq);
			}

			f[numberEquations] = Xeq + Xneq - 1; // Xeq -XeqInit;

			if (displayDetails2)
				System.out.println("Fvec(X)=" + deci(6, f[numberEquations]));

			if (displayDetails)
				System.out.println("      Xeq=" + deci(4, Xeq) + " Xneq="
						+ deci(4, Xneq) + " XeqInit=" + deci(4, XeqInit)
						+ " sumX=" + deci(4, (Xeq + Xneq)));
		}

		return f;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to find the index in the Y iterate vector corresponding to given Z
	// and N. Used by
	// restoreEquilibrium().
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	int findJacIndex(int z, int n, int[] Z, int[] N) {
		int index = -1;
		int len = Z.length;
		for (int i = 0; i < len; i++) {
			if (z == Z[i] && n == N[i]) {
				index = i;
				break;
			}
		}
		return index;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------
	// This method is specific to the 4-isotope alpha test case where only one
	// reaction group
	// comes into equilibrium
	// ------------------------------------------------------------------------------------------------------------------------------------------------------

	private void fixEquilibrium2() {

		// Use Newton Raphson to restore equilibrium

		// Take final values of Ys at end of numerical int step as initial
		// iterate
		double[] Yit = { Y[2][2], Y[8][8], Y[10][10] };
		ytil12 = Y[6][6];

		int nit = 0;
		int maxit = 5;
		double tol = 1e-4;
		double xdev = 100;

		NewtonRaphson test = new NewtonRaphson(computeJ2(Yit), computeF2(Yit),
				Yit);

		// Now iterate until the convergence measure NewtonRaphson.getxdev() is
		// less than
		// a tolerance parameter.

		while (xdev > tol && nit < maxit) {
			nit++;
			Yit = test.doIteration(); // returnx holds the updated vector x +
										// delx
			xdev = test.getxdev();

			// Update the F and J matrices based on the new vector x + delx
			test.updateFMatrix(computeF2(Yit));
			test.updateJMatrix(computeJ2(Yit));
		}

		// Update the population variables with the adjusted quantities

		Y[2][2] = Yit[0];
		pop[2][2] = Yit[0] * nT;
		Y[8][8] = Yit[1];
		pop[8][8] = Yit[1] * nT;
		Y[10][10] = Yit[2];
		pop[10][10] = Yit[2] * nT;

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to compute F(x) specifically for 4-isotope alpha test. Used by
	// fixEquilibrium2().
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------

	double[] computeF2(double[] x) {
		double[] f = new double[3];

		f[0] = x[0] - RGgroup[3].isoYeq[0];
		f[1] = x[1] - RGgroup[3].isoYeq[1];
		f[2] = x[2] - RGgroup[3].isoYeq[2];
		// f[3] = 4*x[0] + 12*ytil12 + 16*x[1] +20*x[2] - 1.0;
		System.out.println("               " + totalTimeSteps + " Y0=" + x[0]
				+ " Y1=" + x[1] + " Y2=" + x[2]);
		System.out.println("               " + totalTimeSteps + " Yeq0="
				+ RGgroup[3].isoYeq[0] + " Yeq1=" + RGgroup[3].isoYeq[1]
				+ " Yeq2=" + RGgroup[3].isoYeq[2]);
		System.out.println("               " + totalTimeSteps + " f0=" + f[0]
				+ " f1=" + f[1] + " f2=" + f[2]);
		return f;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to compute the Jacobian specifically for 4-isotope alpha test,
	// Used by fixEquilibrium2().
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	double[][] computeJ2(double[] x) {
		double[][] J = new double[3][x.length];

		J[0][0] = 1;
		J[0][1] = 0;
		J[0][2] = 0;

		J[1][0] = 0;
		J[1][1] = 1;
		J[1][2] = 0;

		J[2][0] = 0;
		J[2][1] = 0;
		J[2][2] = 1;

		// J[3][0] =4;
		// J[3][1] = 16;
		// J[3][2] = 20;

		return J;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to interpolate in logarithmic time. Given values of a variable s1
	// at time t1 and s2 at time t2,
	// this method returns the value of s at t interpolated linearly in log t
	// between log t1 and log t2. See also
	// method loglogInterpolator.
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	double logInterpolator(double t1, double s1, double t2, double s2, double t) {
		double logt1 = LOG10 * Math.log(t1);
		double logt2 = LOG10 * Math.log(t2);
		double logt = LOG10 * Math.log(t);
		double num = s2 - s1;
		double den = logt2 - logt1;
		double slope = num / den;
		double intercept = s1 - slope * logt1;
		return slope * logt + intercept;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to interpolate log quantity in logarithmic time. Given values of a
	// variable s1 at time t1 and s2 at
	// time t2, this method returns the value of s at t interpolated linearly in
	// log s vs. log t between log t1 and
	// log t2. See also logInterpolator.
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	double loglogInterpolator(double t1, double s1, double t2, double s2,
			double t) {
		double logt1 = LOG10 * Math.log(t1);
		double logt2 = LOG10 * Math.log(t2);
		double logs1 = LOG10 * Math.log(s1);
		double logs2 = LOG10 * Math.log(s2);
		double logt = LOG10 * Math.log(t);
		double num = logs2 - logs1;
		double den = logt2 - logt1;
		double slope = num / den;
		double intercept = logs1 - slope * logt1;
		return Math.pow(10, (slope * logt + intercept));
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Convenience method that is just shorthand for gg.decimalPlacei(c,d),
	// where gg is an instance of
	// the class GraphicsGoodies2 and the method decimalPlaces returns a string
	// with the number c (decimal
	// or exponential notation) rounded to a d decimal places.
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	public static String deci(int places, double number) {
		return gg.decimalPlace(places, number);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to count fraction of reaction groups that satisfy equilibrium
	// conditions
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------

	public void countPE() {
		nontrivialRG = 0;
		countPotentialEquil = 0;
		for (int kk = 0; kk < numberReactionGroups; kk++) {
			// Changed following to >0 to count 1-member groups too
			if (RGgroup[kk].members > 0)
				nontrivialRG++;
			if (RGgroup[kk].isEquilMaybe) {
				// if(RGgroup[kk].getEquil()){
				// if(RGgroup[kk].testForEquilibrium(equiTol)) {
				countPotentialEquil++;
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Method to set upbumper and downbumper and massTolUp given massTol
	// ------------------------------------------------------------------------------------------------------------------------------------------

	private void bumperSetter(double massTol) {
		upbumper = 0.9 * massTol; // 0.8*massTol; // 0.9*massTol
		downbumper = 0.1; // 0.2; //0.1;
		massTolUp = 0.25 * massTol; // 0.5* massTol; //0.25*massTol;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------
	// Method to find the fastest partial equilibrium rate at given time
	// ------------------------------------------------------------------------------------------------------------------------------------------

	private void findFastestPEtimescale() {
		double defaultMax = 1e25;
		fastestPEtimescale = defaultMax;
		fastestPEtimescale2 = defaultMax;
		String fastReaction = "none";
		String fastReaction2 = "none";
		for (int i = 0; i < numberReactionGroups; i++) {
			// System.out.println(
			// "  i="+i+" "+RGgroup[i].reactions[RGgroup[i].refreac].reacString.replaceFirst("-->",
			// "<->")
			// +" "+RGgroup[i].isEquil+" tau="+deci(4,RGgroup[i].tau)
			// );
			boolean hasAsy = false;
			int numberAsy = 0;
			for (int j = 0; j < RGgroup[i].niso; j++) {
				int z = RGgroup[i].isoZ[j];
				int n = RGgroup[i].isoN[j];
				if (isAsymptotic[z][n]) {
					hasAsy = true;
					numberAsy++;
				}
				/*
				 * System.out.println(
				 * "    "+" Z="+z+" N="+n+" "+isAsymptotic[z]
				 * [n]+" "+hasAsy+" No. Asy = "+numberAsy );
				 */
			}
			if (imposeEquil) { // If PE
				// Less stringent condition
				if (RGgroup[i].tau < fastestPEtimescale && !RGgroup[i].isEquil
						&& !hasAsy) {
					fastestPEtimescale = RGgroup[i].tau;
					fastReaction = RGgroup[i].reactions[RGgroup[i].refreac].reacString
							.replaceFirst("-->", "<->");
				}
				// More stringent condition
				if (RGgroup[i].tau < fastestPEtimescale && !RGgroup[i].isEquil
						&& numberAsy >= 1) {
					fastestPEtimescale2 = RGgroup[i].tau;
					fastReaction2 = RGgroup[i].reactions[RGgroup[i].refreac].reacString
							.replaceFirst("-->", "<->");
				}
			} else { // If Asy
				// Less stringent condition
				if (RGgroup[i].tau < fastestPEtimescale && numberAsy <= 1) {
					fastestPEtimescale = RGgroup[i].tau;
					fastReaction = RGgroup[i].reactions[RGgroup[i].refreac].reacString
							.replaceFirst("-->", "<->");
				}
				// More stringent condition
				if (RGgroup[i].tau < fastestPEtimescale2 && numberAsy == 0) {
					fastestPEtimescale2 = RGgroup[i].tau;
					fastReaction2 = RGgroup[i].reactions[RGgroup[i].refreac].reacString
							.replaceFirst("-->", "<->");
				}
			}

			// Make some composite of fastestPEtimescale and fastestPEtimescale2
			if (fastestPEtimescale2 < defaultMax) {
				fastestPEtimescaleAVG = Math.max(fastestPEtimescale,
						fastestPEtimescale2);
			} else {
				fastestPEtimescaleAVG = fastestPEtimescale;
			}
		}
		if (imposeEquil)
			System.out.println("FastPE1: "
					+ replaceThisWithThat(fastReaction, " ", "") + " tau="
					+ deci(3, fastestPEtimescale) + "\nfastPE2: "
					+ replaceThisWithThat(fastReaction2, " ", "") + " tau="
					+ deci(3, fastestPEtimescale2) + "\nfastPE: "
					+ deci(3, fastestPEtimescaleAVG));
		if (imposeEquil)
			System.out.println(tempstring1 + "\n");
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Method to set custom values of massTol and stochasticFactor. Invoked if
	// useCustomTimestepper is true.
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------

	private void customizeTimestepper() {

		double loggert = LOG10 * Math.log(time);
		// double mt = 1.0;
		if (loggert > -2.8) {
			stochasticFactor = 0.0001;
			massTol = 1;
			// equiTol=0.02;
		} else if (loggert > -4) {
			stochasticFactor = 0.0005;
			massTol = 1;
		} else if (loggert > -5) {
			stochasticFactor = 0.0005;
			massTol = 1;
		} else if (loggert > -6) {
			stochasticFactor = 0.002;
			massTol = 1;
		} else if (loggert > -7) {
			stochasticFactor = 0.002;
			massTol = 1;
		} else {
			stochasticFactor = 5e-3;
			massTol = 1;
		}

		// Recompute bump parameters in case massTol has changed
		bumperSetter(massTol);

	}


	/* ----------------------------------------------------------------
	Inner class defining a reaction group entry. This class defines an object
	with  fields that give Z, N, and the reaction index for a reaction, and 
	additional  information like the reaction string and whether it is resonant.
	Also  includes  the reaction vector in the field rVector[], and can store
	the current  value of the  rate and flux associated with the reaction. Put
	as inner class in  StochasticElements  to make it easy to access
	StochasticElements class variables and methods.  For  example, the object
	RObject and the method callExit are defined in the  outer class 
	StochasticElements, not in this inner class. Note that this class shares 
	some functional overlap with the main reaction class ReactionClass1. To
	keep things clean in the implementation of the partial equilibrium
	approximation,	it  was decided  to define new reaction objects with this
	class that are tailored to the  data and  methods required for the PE
	approximation.	
    ----------------------------------------------------------------------*/

	public class Reaction {

		public int Z;                  // Proton number
		public int N;                  // Neutron number
		public int nindex;             // Neutron zero index (offset from 0 for Z>2)
		public int serialIndex;        // Serial index (permanent reaction index in .ser files)
		public int reacIndex;          // Index of reaction in current run for each (Z,N)
		public int reacClass;          // Reaction class for reaclib (1-8)
		public int reacGroupClass;     // Reaction group class (1-5 surrogate for A-E)
		public String reacGroupClassLett; // Letter equivalent (A-E) for reacGroupClass
		public String reacGroupSymbol; // Schematic equil reaction (e.g. a+b<->c)
		public int numberReactants;    // Number species on the left side of reaction
		public int numberProducts;     // Number species on the right side of reaction
		public String reacString;      // String describing reaction
		public String resonanceType;   // Whether resonant (r) or non-resonant (nr)
		public int rVector[] = new int[numberActiveIsotopes]; // Reaction vector
		public String rVectorString;   // Concatenated String representation of rVector
		public double rate = 0.0;      // Current rate of reaction
		public double flux = 0.0;      // Current flux associated with reaction

		public int isoZ[] = new int[6];
		public int isoN[] = new int[6];

		public int za, zb, zc, zd, ze; // Z for isotopes in reaction classes like a+b <-> c+d
		public int na, nb, nc, nd, ne; // N for isotopes in reaction classes like a+b <-> c+d
		public boolean forward; // Whether reaction forward or reverse within reaction group class
                                // e.g., for class B (a+b<->c), a+b->c is true; c ->a+b is false

		Reaction(int Z, int N, int serialIndex) {
			this.Z = Z;
			this.N = N;
			// Account for different N offsets if Z>2
			if (Z > 2) {
				this.nindex = N - minNetN[Z];
			} else {
				this.nindex = N;
			}
			this.serialIndex = serialIndex;
			reacIndex = serialLookup(Z, N, serialIndex);
			if (reacIndex < 0)
				callExit("Failure of serial Index lookup: Z=" + Z + " N=" + N
						+ " serialIndex=" + serialIndex);
			this.reacString = RObject[Z][nindex][reacIndex].reacString;
			this.reacClass = RObject[Z][nindex][reacIndex].reacIndex;
			this.numberReactants = RObject[Z][nindex][reacIndex].numberReactants;
			this.numberProducts = RObject[Z][nindex][reacIndex].numberProducts;
			if (RObject[Z][nindex][reacIndex].resonant) {
				resonanceType = "r";
			} else {
				resonanceType = "nr";
			}
			String accum = "";
			for (int i = 0; i < numberActiveIsotopes; i++) {
				this.rVector[i] = reactionVector[Z][N][reacIndex][i];
				accum += this.rVector[i];
			}
			this.rVectorString = accum;

			// Set the reaction group class that reaction belongs to. The 5
			// reaction group classes
			// are labeled A, B, C, D, E, but the variable reacGroupClass is an
			// integer that takes
			// the values 1-5 (corresponding to A-E), except that if the
			// reaction is in class 7 (which
			// has no equilibrium reverse reactions in reaclib), reacGroupClass
			// is set to -1. The boolean
			// field this.forward determines whether in our bookkeeping for
			// reaction pairs a reaction is
			// forward or reverse. For the asymmmetric reactions (different
			// number of species on left
			// and right) we can asssign a convention unambiguously here. For
			// the symmetric reaction
			// group classes A and D we can't do that unambiguously here, so we
			// assign all reactions
			// in those reaction group classes to be forward here, and then will
			// resolve the ambiguity
			// later after the reaction groups are formed by using the method
			// resolveForwardReverseAmbiguity().

			switch (this.reacClass) {

			case 1: // a -> b (reaction group class A)
				this.reacGroupClass = 1;
				this.reacGroupClassLett = "A";
				this.reacGroupSymbol = "a<->b";
				isoZ[0] = RObject[Z][nindex][reacIndex].isoIn[0].x;
				isoN[0] = RObject[Z][nindex][reacIndex].isoIn[0].y;
				isoZ[1] = RObject[Z][nindex][reacIndex].isoOut[0].x;
				isoN[1] = RObject[Z][nindex][reacIndex].isoOut[0].y;
				// Following .forward is arbitrary placeholder since a <-> b is
				// symmetric.
				// Will resolve later with method
				// resolveForwardReverseAmbiguity().
				this.forward = true;
				break;

			case 2: // c -> a+b (reverse in reaction group class B)
				this.reacGroupClass = 2;
				this.reacGroupClassLett = "B";
				this.reacGroupSymbol = "a+b<->c";
				isoZ[0] = RObject[Z][nindex][reacIndex].isoOut[0].x;
				isoN[0] = RObject[Z][nindex][reacIndex].isoOut[0].y;
				isoZ[1] = RObject[Z][nindex][reacIndex].isoOut[1].x;
				isoN[1] = RObject[Z][nindex][reacIndex].isoOut[1].y;
				isoZ[2] = RObject[Z][nindex][reacIndex].isoIn[0].x;
				isoN[2] = RObject[Z][nindex][reacIndex].isoIn[0].y;
				this.forward = false;
				break;

			case 3: // d -> a+b+c (reverse in reaction group class C)
				this.reacGroupClass = 3;
				this.reacGroupClassLett = "C";
				this.reacGroupSymbol = "a+b+c<->d";
				isoZ[0] = RObject[Z][nindex][reacIndex].isoOut[0].x;
				isoN[0] = RObject[Z][nindex][reacIndex].isoOut[0].y;
				isoZ[1] = RObject[Z][nindex][reacIndex].isoOut[1].x;
				isoN[1] = RObject[Z][nindex][reacIndex].isoOut[1].y;
				isoZ[2] = RObject[Z][nindex][reacIndex].isoOut[2].x;
				isoN[2] = RObject[Z][nindex][reacIndex].isoOut[2].y;
				isoZ[3] = RObject[Z][nindex][reacIndex].isoIn[0].x;
				isoN[3] = RObject[Z][nindex][reacIndex].isoIn[0].y;
				this.forward = false;
				break;

			case 4: // a+b -> c (forward in reaction group class B)
				this.reacGroupClass = 2;
				this.reacGroupClassLett = "B";
				this.reacGroupSymbol = "a+b<->c";
				isoZ[0] = RObject[Z][nindex][reacIndex].isoIn[0].x;
				isoN[0] = RObject[Z][nindex][reacIndex].isoIn[0].y;
				isoZ[1] = RObject[Z][nindex][reacIndex].isoIn[1].x;
				isoN[1] = RObject[Z][nindex][reacIndex].isoIn[1].y;
				isoZ[2] = RObject[Z][nindex][reacIndex].isoOut[0].x;
				isoN[2] = RObject[Z][nindex][reacIndex].isoOut[0].y;
				this.forward = true;
				break;

			case 5: // a+b -> c+d (reaction group class D)
				this.reacGroupClass = 4;
				this.reacGroupClassLett = "D";
				this.reacGroupSymbol = "a+b<->c+d";
				isoZ[0] = RObject[Z][nindex][reacIndex].isoIn[0].x;
				isoN[0] = RObject[Z][nindex][reacIndex].isoIn[0].y;
				isoZ[1] = RObject[Z][nindex][reacIndex].isoIn[1].x;
				isoN[1] = RObject[Z][nindex][reacIndex].isoIn[1].y;
				isoZ[2] = RObject[Z][nindex][reacIndex].isoOut[0].x;
				isoN[2] = RObject[Z][nindex][reacIndex].isoOut[0].y;
				isoZ[3] = RObject[Z][nindex][reacIndex].isoOut[1].x;
				isoN[3] = RObject[Z][nindex][reacIndex].isoOut[1].y;
				// Following .forward is arbitrary placeholder since a+b<->c+d
				// is symmetric.
				// Will resolve later with method
				// resolveForwardReverseAmbiguity().
				this.forward = true;
				break;

			case 6: // a+b -> c+d+e (forward in reaction group class E)
				this.reacGroupClass = 5;
				this.reacGroupClassLett = "E";
				this.reacGroupSymbol = "a+b<->c+d+e";
				isoZ[0] = RObject[Z][nindex][reacIndex].isoIn[0].x;
				isoN[0] = RObject[Z][nindex][reacIndex].isoIn[0].y;
				isoZ[1] = RObject[Z][nindex][reacIndex].isoIn[1].x;
				isoN[1] = RObject[Z][nindex][reacIndex].isoIn[1].y;
				isoZ[2] = RObject[Z][nindex][reacIndex].isoOut[0].x;
				isoN[2] = RObject[Z][nindex][reacIndex].isoOut[0].y;
				isoZ[3] = RObject[Z][nindex][reacIndex].isoOut[1].x;
				isoN[3] = RObject[Z][nindex][reacIndex].isoOut[1].y;
				isoZ[4] = RObject[Z][nindex][reacIndex].isoOut[2].x;
				isoN[4] = RObject[Z][nindex][reacIndex].isoOut[2].y;
				this.forward = true;
				break;

			case 7: // a+b -> c+d+e+f, so no reverse reactions in REACLIB
				this.reacGroupClass = -1;
				this.reacGroupClassLett = "X";
				this.reacGroupSymbol = "a+b->c+d+e+f";
				isoZ[0] = RObject[Z][nindex][reacIndex].isoIn[0].x;
				isoN[0] = RObject[Z][nindex][reacIndex].isoIn[0].y;
				isoZ[1] = RObject[Z][nindex][reacIndex].isoIn[1].x;
				isoN[1] = RObject[Z][nindex][reacIndex].isoIn[1].y;
				isoZ[2] = RObject[Z][nindex][reacIndex].isoOut[0].x;
				isoN[2] = RObject[Z][nindex][reacIndex].isoOut[0].y;
				isoZ[3] = RObject[Z][nindex][reacIndex].isoOut[1].x;
				isoN[3] = RObject[Z][nindex][reacIndex].isoOut[1].y;
				isoZ[4] = RObject[Z][nindex][reacIndex].isoOut[2].x;
				isoN[4] = RObject[Z][nindex][reacIndex].isoOut[2].y;
				isoZ[5] = RObject[Z][nindex][reacIndex].isoOut[3].x;
				isoN[5] = RObject[Z][nindex][reacIndex].isoOut[3].y;
				this.forward = true;
				break;

			case 8:

				if (this.numberProducts > 1) { // c+d+e -> a+b (Reverse reaction
												// in reaction group class E)
					this.reacGroupClass = 5;
					this.reacGroupClassLett = "E";
					this.reacGroupSymbol = "a+b<->c+d+e";
					isoZ[0] = RObject[Z][nindex][reacIndex].isoOut[0].x;
					isoN[0] = RObject[Z][nindex][reacIndex].isoOut[0].y;
					isoZ[1] = RObject[Z][nindex][reacIndex].isoOut[1].x;
					isoN[1] = RObject[Z][nindex][reacIndex].isoOut[1].y;
					isoZ[2] = RObject[Z][nindex][reacIndex].isoIn[0].x;
					isoN[2] = RObject[Z][nindex][reacIndex].isoIn[0].y;
					isoZ[3] = RObject[Z][nindex][reacIndex].isoIn[1].x;
					isoN[3] = RObject[Z][nindex][reacIndex].isoIn[1].y;
					isoZ[4] = RObject[Z][nindex][reacIndex].isoIn[2].x;
					isoN[4] = RObject[Z][nindex][reacIndex].isoIn[2].y;
					this.forward = false;
				} else { // a+b+c -> d (Forward reaction in reaction group class
							// C)
					this.reacGroupClass = 3;
					this.reacGroupClassLett = "C";
					this.reacGroupSymbol = "a+b+c<->d";
					isoZ[0] = RObject[Z][nindex][reacIndex].isoIn[0].x;
					isoN[0] = RObject[Z][nindex][reacIndex].isoIn[0].y;
					isoZ[1] = RObject[Z][nindex][reacIndex].isoIn[1].x;
					isoN[1] = RObject[Z][nindex][reacIndex].isoIn[1].y;
					isoZ[2] = RObject[Z][nindex][reacIndex].isoIn[2].x;
					isoN[2] = RObject[Z][nindex][reacIndex].isoIn[2].y;
					isoZ[3] = RObject[Z][nindex][reacIndex].isoOut[0].x;
					isoN[3] = RObject[Z][nindex][reacIndex].isoOut[0].y;
					this.forward = true;
				}
				break;
			}

		}

		public void setRate(double rate) {
			this.rate = rate;
		}

		public double getRate() {
			return rate;
		}

		public void setFlux(double flux) {
			this.flux = flux;
		}

		public double getFlux() {
			return flux;
		}
	} /* end inner class Reaction */

	// -----------------------------------------------------------------------------------------------------------------------------
	// Inner class forming objects that are reaction groups.
	// -----------------------------------------------------------------------------------------------------------------------------

	public class ReactionGroup {

		final double THIRD = 0.33333333333333333333;
		final double TWOTHIRD = 0.6666666666666666666667;

		public int members;                        // Number reactions in reaction group
		final int nspecies[] = { 2, 3, 4, 4, 5 };  // # isotopic species for
													// reactions in 5 reaction
													// group classes
		public int RGindex;           // Reaction group index for particular object
		public int RGclass;           // Reaction group class (1-5, corresponding to A-E)
		public String RGclassLett;    // Letter A-E corresponding to RGclass 1-5.
		public String reacGroupSymbol; // Schematic reaction of group (e.g. a+b<->c)
		public double crg[];           // The constants c1, c2, ...
		public int numberC;            // Number of constants crg[] for this rg class
		public double rgkf;            // Forward rate parameter for partial equilibrium
		public double rgkr;            // Reverse rate parameter for partial equilibrium
		public Reaction reactions[];   // Array of reactions in reaction group
		public boolean isEquil;        // Whether this reaction group is in equilibrium
		public boolean isEquilMaybe;   // Whether would be in equil if no threshhold condition
		public int reacIndices[];  // Array holding reaction index for all reactions in group
		private int refreac = -1;  // Group member index for reference reaction for group
		public int Zarg[];             // Array holding Z argument for RObject[][][]
		public int Narg[];             // Array holding N argument for RObject[][][]

		private int niso;              // Value of nspecies[] for this ReactionGroup object
		private int Nindex;            // Value of N index for RObject[][][] for reference reaction
		private int isoZ[];            // Z for the niso isotopes in the reactions of the group
		private int isoN[];            // N for the niso isotopes in the reactions of the group
		private double isoA[];         // A for the niso isotopes in the reactions of the group
		private double isoY0[];        // Y0 for the niso isotopes in the reactions of the group
		private double isoYeq[];       // Y_eq for the niso isotopes in the reactions of the group
		private double isoY[];         // Current Y for the niso isotopes in the reactions of the group
		private double eqcheck[];      // Population ratio to check equilibrium
		private int abundVecIndex[];   // index of ref reaction isotopes in abundance vector

		private double aa, bb, cc;     // Quadratic coefficients a, b, c
		private double alpha, beta, gamma;  // Helper coefficients for cubic ~ quadratic approximation
		private double qq;             // q = 4ac-b^2
		private double rootq;          // Math.sqrt(-q)
		private double tau;            // Timescale for equilibrium

		private double equilRatio;     // Equilibrium ratio of abundances
		private double kratio;         // Ratio k_r/k_f. Equal to equilRatio at equilibrium

		private double netFlux;        // Net flux for this reaction group
		private double lambda;         // Progress variable for reaction pair
		private double lambdaEq;       // Equilbrium value of progress variable

		private double Yminner;        // Current minimum Y in reaction group
		private double mineqcheck;     // Current minimum value of eqcheck in reaction group
		private double maxeqcheck;     // Current max value of eqcheck in reaction group
		

		ReactionGroup(int RGindex, int members, int RGclass, Reaction reactions[]) {

			this.RGindex = RGindex;
			this.members = members;
			this.RGclass = RGclass;
			this.reactions = new Reaction[members];
			this.reactions = reactions;
			this.isEquil = false;
			this.reacIndices = new int[members];
			this.Zarg = new int[members];
			this.Narg = new int[members];

			// Fill reacIndices[] array. Holds the reacIndex for each reaction
			// in group. The corresponding
			// RObject[][][] for ith reaction in the group would be
			// RObject[z][n][m],
			// where z = Zarg[i], n = Narg[i], and m = reacindices[i].

			for (int i = 0; i < members; i++) {
				Zarg[i] = reactions[i].Z;
				Narg[i] = reactions[i].nindex;
				reacIndices[i] = reactions[i].reacIndex;
			}

			// If single-member group, be sure single reaction is labeled
			// "forward" (otherwise potential
			// problems for later logic in choosing reference reaction).
			// Because, e.g., a+b -> c is by
			// convention labeled "forward" for reaction group class B
			// (a+b<->c), a single unpaired reaction
			// c->a+b will be labeled "reverse" (.forward = false) and thus
			// there will be no forward reactions
			// in the corresponding 1-member reaction group.

			if (members == 1)
				reactions[0].forward = true;

			if (this.RGclass == 1 || this.RGclass == 4)
				resolveForwardReverseAmbiguity();

			this.checkReactionGroupAnomalies();

			if (RGclass > 0) {
				niso = nspecies[RGclass - 1];
			} else {
				niso = 6; // 6-member Reaclib class 7, which has no partner
							// reactions to form RG so RGclass=-1
			}

			isoZ = new int[niso];
			isoN = new int[niso];
			isoA = new double[niso];
			isoY0 = new double[niso];
			isoYeq = new double[niso];
			isoY = new double[niso];
			eqcheck = new double[niso];
			abundVecIndex = new int[niso];

			// Set the reference reaction for the reaction group by looping
			// through and choosing the
			// first reaction that has .forward = true.

			for (int i = 0; i < members; i++) {
				if (reactions[i].forward) {
					refreac = i;
					break;
				}
			}

			// refreac is now the index of the reference reaction. First take
			// care of anomalous case
			// where there are no forward reactions with a given reaction vector
			// (which could be generated
			// by suppressing all the forward reactions in a reaction group, for
			// example).

			if (refreac == -1) {
				refreac = 0;
				toChar.println("\n***** Reaction group " + this.RGindex
						+ " has no forward reactions******");
			}

			Nindex = Narg[refreac]; // Nindex argument for RObject[][][] for
									// reference reactions for group

			String bittystring = "";
			String refstring = "Reference: ";
			for (int k = 0; k < niso; k++) {
				isoZ[k] = reactions[refreac].isoZ[k];
				isoN[k] = reactions[refreac].isoN[k];
				isoA[k] = (double) (isoZ[k] + isoN[k]);
				abundVecIndex[k] = returnNetIndex(isoZ[k], isoN[k]);
				bittystring = " Z[" + k + "]=" + isoZ[k] + " N[" + k + "]="
						+ isoN[k];
				refstring += bittystring;
			}

			toChar.println();

			// Following would be triggered by reaction like b8 -> he4 + he4 +
			// e+ + nu except for the logic
			// above that sets the first member of a 1-reaction group to
			// .forward=true in all cases.

			if (refreac == -1)
				callExit("No forward reaction for reaction group "
						+ this.RGindex);

			toChar
					.println("------------------------------------------------------------------------------------------------------------------------");
			for (int i = 0; i < members; i++) {
				String fw = "r";
				if (reactions[i].forward)
					fw = "f";
				toChar.println("  RG=" + this.RGindex + " " + i + "  RGclass="
						+ this.RGclass + " m=" + reacIndices[i] + "   "
						+ RObject[Zarg[i]][Narg[i]][reacIndices[i]].reacString
						+ "  " + reactions[i].forward);
			}

			switch (RGclass) {

			case -1: // Reaclib class 7, which can't equilibrate
				RGclassLett = "X";
				reacGroupSymbol = "a+b ->c+d+e+f (no equil)";
				break;

			case 1:
				RGclassLett = "A";
				reacGroupSymbol = "a<->b";
				numberC = 1;
				crg = new double[numberC];
				break;

			case 2:
				RGclassLett = "B";
				reacGroupSymbol = "a+b<->c";
				numberC = 2;
				crg = new double[numberC];
				break;

			case 3:
				RGclassLett = "C";
				reacGroupSymbol = "a+b+c<->d";
				numberC = 3;
				crg = new double[numberC];
				break;

			case 4:
				RGclassLett = "D";
				reacGroupSymbol = "a+b<->c+d";
				numberC = 3;
				crg = new double[numberC];
				break;

			case 5:
				RGclassLett = "E";
				reacGroupSymbol = "a+b<->c+d+e";
				numberC = 4;
				crg = new double[numberC];
				break;
			}

			toChar.println("  RGclassLett=" + RGclassLett + "  Symbol="
					+ reacGroupSymbol + "  Reference:  "
					+ reactions[refreac].reacString + "\n" + "  " + refstring);
			toChar
					.println("------------------------------------------------------------------------------------------------------------------------");

		}

		// -----------------------------------------------------------------------------------------------------------------------------------
		// Method to resolve forward-reverse ambiguity for symmetric reaction
		// group classes
		// A (a <-> b) and D (a+b <-> c+d). Since there isn't a natural
		// definition of which reaction
		// vector corresponds to forward and which to reverse for these
		// symmetric reaction pairs,
		// use this method after the reaction classes have been constructed to
		// arbitrarily set the
		// first reaction in the reaction group to forward=true, and then set
		// all reactions having the
		// same reaction vector in the group to forward=true and all having the
		// negative of the
		// first reaction vector to forward=false in the reaction group.
		// -----------------------------------------------------------------------------------------------------------------------------------

		private void resolveForwardReverseAmbiguity() {

			int z1 = this.reactions[0].Z;
			int n1 = this.reactions[0].N;
			int m1 = this.reactions[0].reacIndex;

			for (int j = 0; j < this.members; j++) {
				int z2 = this.reactions[j].Z;
				int n2 = this.reactions[j].N;
				int m2 = this.reactions[j].reacIndex;
				int test = compareReactionVectors(z1, n1, m1, z2, n2, m2);
				if (test == 1) {
					this.reactions[j].forward = true;
				} else if (test == -1) {
					this.reactions[j].forward = false;
					// Switch indices
					if (this.RGclass == 4) {
						int za = reactions[j].isoZ[0];
						int na = reactions[j].isoN[0];
						int zb = reactions[j].isoZ[1];
						int nb = reactions[j].isoN[1];
						reactions[j].isoZ[0] = reactions[j].isoZ[2];
						reactions[j].isoN[0] = reactions[j].isoN[2];
						reactions[j].isoZ[1] = reactions[j].isoZ[3];
						reactions[j].isoN[1] = reactions[j].isoN[3];
						reactions[j].isoZ[2] = za;
						reactions[j].isoN[2] = na;
						reactions[j].isoZ[3] = zb;
						reactions[j].isoN[3] = nb;
					} else if (this.RGclass == 1) {
						int za = reactions[j].isoZ[0];
						int na = reactions[j].isoN[0];
						reactions[j].isoZ[0] = reactions[j].isoZ[1];
						reactions[j].isoN[0] = reactions[j].isoN[1];
						reactions[j].isoZ[1] = za;
						reactions[j].isoN[1] = na;
					}
				} else {
					// If we get to this option, something is badly wrong!
					callExit("Problem with reaction group " + this.RGindex
							+ ": Incompatible reaction vectors.");
				}
			}
		}

		// ----------------------------------------------------------------------------------------------------------------------------------------
		// Method to check for anomalous reaction groups caused by catalytic
		// elements (same isotope
		// appearing on both sides of the equation), which changes the effective
		// reaction group for the
		// reaction with respect to equilibration.
		// ----------------------------------------------------------------------------------------------------------------------------------------

		private void checkReactionGroupAnomalies() {
			int targetClass = this.RGclass;
			for (int j = 0; j < this.members; j++) {
				if (this.reactions[j].reacGroupClass != targetClass) {
					String theWord = "\n\n  *** REACTION GROUP ANOMALY FOR REACTION GROUP "
							+ this.RGindex
							+ " COMPONENT "
							+ j
							+ ":\n      Class "
							+ this.reactions[j].reacGroupClass
							+ " for "
							+ this.reactions[j].reacString
							+ " differs from class "
							+ targetClass
							+ " for reaction group.\n";
					toChar.println(theWord);
				}
			}
		}

		// --------------------------------------------------------------------------------------------------------------------------------------
		// Method to compute all partial equilibrium quantities
		// --------------------------------------------------------------------------------------------------------------------------------------

		public void computeEquilibrium() {
			mostDevious = 0;
			mostDeviousIndex = 0;
			this.computeEquilibriumRates();
			this.putY0();
			this.computeC();
			this.computeQuad();

			// Compute net flux and progress variable
			if (this.isEquil) {
				this.netFlux = this.sumFluxes();
				this.lambda = this.netFlux * deltaTime / nT;
				// if(imposeEquil && displayE){
				// System.out.println("  PROG: t="+gg.decimalPlace(6,time)
				// +" dt="+gg.decimalPlace(6,deltaTime)
				// +" RG="+this.RGindex
				// +" groupFlux="+gg.decimalPlace(6,this.netFlux/nT)
				// +" progress="+gg.decimalPlace(6,this.lambda)
				// +" progresseq="+gg.decimalPlace(6,this.lambdaEq));
				// for(int i=0; i<this.niso; i++){
				// System.out.println(
				// "    Z="+this.isoZ[i]+" N="+this.isoN[i]
				// +" Yeq="+gg.decimalPlace(6,this.isoYeq[i])
				// +" Y0="+gg.decimalPlace(6, this.isoY0[i])
				// +" Y="+gg.decimalPlace(6,Y[this.isoZ[i]][this.isoN[i]])
				// );
				// }
				// }
			}
		}

		// --------------------------------------------------------------------------------------------------------------------------------------
		// Method to compute the net forward and reverse rates k_f and k_r
		// required in
		// partial equilibrium approximation.
		// --------------------------------------------------------------------------------------------------------------------------------------

		private void computeEquilibriumRates() {

			double kf = 0;
			double kr = 0;

			// Sum all contributions from members of reaction group

			for (int j = 0; j < this.members; j++) {
				if (this.reactions[j].forward) {
					kf += masterRates[this.reactions[j].Z][this.reactions[j].N][this.reactions[j].reacIndex];
				} else {
					kr += masterRates[this.reactions[j].Z][this.reactions[j].N][this.reactions[j].reacIndex];
				}
			}

			// Store forward and reverse rates

			this.rgkf = kf;
			this.rgkr = kr;

		}

		// ----------------------------------------------------------------------------------------------------------------------------------------
		// Method to put the values of Y0 at beginning of timestep into the Y0[]
		// array for this object
		// ----------------------------------------------------------------------------------------------------------------------------------------

		public void putY0() {
			for (int k = 0; k < niso; k++) {
				isoY0[k] = Yzero[isoZ[k]][isoN[k]];
				isoY[k] = Y[isoZ[k]][isoN[k]];
			}
		}

		// Method to compute the values of the constants crg[]

		private void computeC() {

			switch (this.RGclass) {

			case -1: // Reaclib class 7, which can't equilibrate
				break;

			case 1:
				crg[0] = isoY0[0] + isoY0[1];
				break;

			case 2:
				crg[0] = isoY0[1] - isoY0[0];
				crg[1] = isoY0[1] + isoY0[2];
				break;

			case 3:
				crg[0] = isoY0[0] - isoY0[1];
				crg[1] = isoY0[0] - isoY0[2];
				crg[2] = THIRD * (isoY0[0] + isoY0[1] + isoY0[2]) + isoY0[3];
				break;

			case 4:
				crg[0] = isoY0[0] - isoY0[1];
				crg[1] = isoY0[0] + isoY0[2];
				crg[2] = isoY0[0] + isoY0[3];
				break;

			case 5:
				crg[0] = isoY0[0] + THIRD * (isoY0[2] + isoY0[3] + isoY0[4]);
				crg[1] = isoY0[0] - isoY0[1];
				crg[2] = isoY0[2] - isoY0[3];
				crg[3] = isoY0[2] - isoY0[4];
				break;
			}
		}

		// ----------------------------------------------------------------------------------------------------------------------------------------
		// Method to compute the quadratic coefficients needed for the
		// equilibrium solution
		// and to compute the equilibrium solution
		// ----------------------------------------------------------------------------------------------------------------------------------------

		private void computeQuad() {

			switch (this.RGclass) {

			case -1: // Reaclib class 7, which can't equilibrate

				break;

			case 1:
				aa = 0;
				bb = -rgkf;
				cc = rgkr;
				break;

			case 2:
				aa = -rgkf;
				bb = -(crg[0] * rgkf + rgkr);
				cc = rgkr * (crg[1] - crg[0]);
				break;

			case 3:
				aa = -rgkf * isoY0[0] + rgkf * (crg[0] + crg[1]);
				bb = -(rgkf * crg[0] * crg[1] + rgkr);
				cc = rgkr * (crg[2] + THIRD * (crg[0] + crg[1]));
				break;

			case 4:
				aa = rgkr - rgkf;
				bb = -rgkr * (crg[1] + crg[2]) + rgkf * crg[0];
				cc = rgkr * crg[1] * crg[2];

				break;

			case 5:
				alpha = crg[0] + THIRD * (crg[2] + crg[3]);
				beta = crg[0] - TWOTHIRD * crg[2] + THIRD * crg[3];
				gamma = crg[0] + THIRD * crg[2] - TWOTHIRD * crg[3];
				aa = (3 * crg[0] - isoY0[0]) * rgkr - rgkf;
				bb = crg[1] * rgkf
						- (alpha * beta + alpha * gamma + beta * gamma) * rgkr;
				cc = rgkr * alpha * beta * gamma;

				break;
			}

			// Compute the q = 4ac - b^2 parameter, equil timescale tau, and
			// isoYeq[0] (which is then
			// be used to compute the other isoYeq[].

			if (this.RGclass > 1) {
				qq = computeq(aa, bb, cc);
				rootq = Math.sqrt(-qq + 1.0e-30);
				if (members > 1) {
					tau = 1 / rootq;
				}
				isoYeq[0] = computeYeq(aa, bb, rootq);
			} else {
				qq = -1;
				tau = 1 / rgkf;
				isoYeq[0] = rgkr / rgkf;
			}

			// Compute the other equilibrium populations in the reaction pair
			// and abundance ratios

			switch (RGclass) {

			case -1: // Reaclib class 7, which can't equilibrate

				break;

			case 1:
				isoYeq[1] = crg[0] - isoYeq[0];
				equilRatio = isoY[0] / isoY[1];
				break;

			case 2:
				isoYeq[1] = crg[0] + isoYeq[0];
				isoYeq[2] = crg[1] - isoYeq[1];
				equilRatio = isoY[0] * isoY[1] / isoY[2];
				break;

			case 3:
				isoYeq[1] = isoYeq[0] - crg[0];
				isoYeq[2] = isoYeq[0] - crg[1];
				isoYeq[3] = crg[2] - isoYeq[0] + THIRD * (crg[0] + crg[1]);
				equilRatio = isoY[0] * isoY[1] * isoY[2] / isoY[3];
				break;

			case 4:
				isoYeq[1] = isoYeq[0] - crg[0];
				isoYeq[2] = crg[1] - isoYeq[0];
				isoYeq[3] = crg[2] - isoYeq[0];
				equilRatio = isoY[0] * isoY[1] / (isoY[2] * isoY[3]);
				break;

			case 5:
				isoYeq[1] = isoYeq[0] - crg[1];
				isoYeq[2] = alpha - isoYeq[0];
				isoYeq[3] = beta - isoYeq[0];
				isoYeq[4] = gamma - isoYeq[0];
				equilRatio = isoY[0] * isoY[1] / (isoY[2] * isoY[3] * isoY[4]);
				break;
			}

			// Compute the equilibrium value of the progress variable

			lambdaEq = isoY0[0] - isoYeq[0];

			// Compute the population ratios used to check equilibration

			computeEqRatios();
			kratio = rgkr / rgkf;

		}

		// ----------------------------------------------------------------------------------------------------------------------------------------
		// Method to compute q = 4ac-b^2 for quadratic solution
		// ----------------------------------------------------------------------------------------------------------------------------------------

		private double computeq(double a, double b, double c) {
			return 4 * a * c - b * b;
		}

		// Method to compute Yeq[0]

		private double computeYeq(double a, double b, double rootq) {
			return -0.5 * (b + rootq) / a;
		}

		// ----------------------------------------------------------------------------------------------------------------------------------------
		// Method to compute array of population ratios used to check
		// equilibration
		// ----------------------------------------------------------------------------------------------------------------------------------------

		void computeEqRatios() {

			double thisDevious = Math.abs((this.equilRatio - this.kratio)
					/ this.kratio);
			if (this.isEquil && thisDevious > mostDevious) {
				mostDevious = thisDevious;
				mostDeviousIndex = this.RGindex;
			}

			// The return statements in the following if-clauses cause reaction
			// groups already in
			// equilibrium to stay in equilibrium. If the maxDevious > tolerance
			// check is implemented
			// it can cause a reaction group to drop out of equilibrium.

			if (this.isEquil && thisDevious < maxDevious) {
				return;
			} else if (this.isEquil && thisDevious > maxDevious && imposeEquil
					&& time > equilibrateTime) {
				removeFromEquilibrium();
				return;
			}

			Yminner = 1000;
			maxeqcheck = 0;
			mineqcheck = 1000;

			// Determine if RG is in equilibrium: set isEquil to default value
			// of true and then try to falsify

			this.isEquil = true;
			this.isEquilMaybe = true;
			
			// Following doesn't seem to improve things
			//if(deltaTimeRestart/this.tau < 1) this.isEquil = false;

			for (int i = 0; i < niso; i++) {
				// Note: something like the following probably required because
				// otherwise we will divide
				// by zero for isotopes early in the calculation that have no
				// population.
				if (isoYeq[i] == 0 || isoY[i] == 0) {
					this.isEquil = false;
					this.isEquilMaybe = false;
					break;
				}
				eqcheck[i] = Math.abs(isoY[i] - isoYeq[i]) / isoYeq[i];

				if (eqcheck[i] < mineqcheck)
					mineqcheck = eqcheck[i];
				if (eqcheck[i] > maxeqcheck)
					maxeqcheck = eqcheck[i];
				if (isoYeq[i] < Yminner)
					Yminner = isoYeq[i];

				// Set equilibrium to false if any eqcheck[] greater than
				// tolerance, or if equilibrium abundance
				// is small relative to equiTol (which can cause numerical
				// issues in judging whether in
				// equilibrium).

				if (time < equilibrateTime || eqcheck[i] > equiTol
						|| isoYeq[i] < Ythresh) {
					this.isEquil = false;
					// break; // Note: this break won't affect results, but
					// affects diagnostic values of eqcheck[]
					// since they will all be zero after the break.
				}
			}
			
			// Following doesn't help; generally makes things worse. Probably because the 
			// reactions that are not in population equil even though their tau is much smaller
			// than the timestep involve isotopes like 20Ne that are strongly coupled to the heavy
			// ion reactions that don't come into equilibrium until very late.
			
			//if(deltaTimeRestart/this.tau > 10 && time > equilibrateTime) this.isEquil = true;

			// Check whether would be in equil without time or threshhold
			// condition

			for (int i = 0; i < niso; i++) {

				if (eqcheck[i] > equiTol) {
					this.isEquilMaybe = false;
					break;
				}
			}

			// Keep track of number of reaction in partial equilibrium.
			// totalEquilReactions is variable in StochasticElements class.
			// Because this class is
			// contained within the StochasticElements class, the variable can
			// be accessed directly
			// totalEquilReactions is only updated here if imposeEquil is false.
			// If imposeEquil is true,
			// totalEquilReaction is updated when the flux is suppressed for
			// equilibrium pairs.

			if (!imposeEquil && this.isEquil)
				totalEquilReactions += this.members;

			// Set isEquil field of network species vectors to true if isotope
			// participating in equilibrium

			if (this.isEquil) {
				if (showAddRemove) {
					System.out.println(totalTimeSteps + " Adding RG "
							+ this.RGindex + " to equil");
				}
				for (int i = 0; i < niso; i++) {
					netVector[this.abundVecIndex[i]].isEquil = true;
				}
			}

			// Set the activity array for each reaction in reaction group to
			// true if not in equil and false if it is,
			// if we are imposing equilibrium.

			if (imposeEquil && time > equilibrateTime) {
				for (int i = 0; i < this.members; i++) {
					reacIsActive[reactions[i].Z][reactions[i].N][reactions[i].reacIndex] = !this.isEquil;
				}
			}
		}

		// ----------------------------------------------------------------------------------------------------------------------------------------
		// Method to remove reaction group from equilibrium
		// ----------------------------------------------------------------------------------------------------------------------------------------

		public void removeFromEquilibrium() {
			this.isEquil = false;
			double thisDevious = Math.abs((this.equilRatio - this.kratio)
					/ this.kratio);
			if (showAddRemove) {
				System.out.println("\n******* " + totalTimeSteps
						+ " Remove RG " + this.RGindex
						+ " from equil; devious=" + deci(4, thisDevious)
						+ " Rmin=" + deci(4, mineqcheck) + " Rmax="
						+ deci(4, maxeqcheck) + " Ymin=" + deci(4, Yminner));
			}
			for (int i = 0; i < this.niso; i++) {
				netVector[this.abundVecIndex[i]].isEquil = false;
				if (showAddRemove) {
					System.out
							.println("   Z="
									+ this.isoZ[i]
									+ " N="
									+ this.isoN[i]
									+ " Y="
									+ deci(4, this.isoY[i])
									+ " Yeq="
									+ deci(4, this.isoYeq[i])
									+ " Rprev="
									+ deci(4, eqcheck[i])
									+ " Rnow="
									+ deci(4, Math.abs(isoY[i] - isoYeq[i])
											/ isoYeq[i]));
				}
			}
			for (int i = 0; i < this.members; i++) {
				reacIsActive[reactions[i].Z][reactions[i].N][reactions[i].reacIndex] = true;
				if (showAddRemove) {
					System.out.println("   -- Remove "
							+ this.reactions[i].reacString);
				}
			}
			// callExit("Stopping");
		}

		// ------------------------------------------------------------------------------------------------------------------------------------------------------
		// Method to test now whether this reaction group is in equilibrium. The
		// argument tol is the max
		// fractional deviation of any abundance from its equilibrium abundance.
		// The method returns true if
		// all abundances are within tol of their equilbrium abundances, false
		// otherwise. Compare with
		// method getEquil(). The difference is that getEquil() returns the
		// current value of the class variable
		// isEquil, which must have been previously computed, whereas the
		// present method determines whether
		// the equilibrium condition is satisfied now, and does not alter the
		// variable isEquil. (The method
		// setEquil() can be used to set the variable isEquil.)
		// ------------------------------------------------------------------------------------------------------------------------------------------------------

		boolean testForEquilibrium(double tol) {
			boolean inequil = true;
			double chk = 0.0;
			this.computeEquilibrium();
			for (int i = 0; i < niso; i++) {
				chk = Math.abs(isoY[i] - isoYeq[i]) / isoYeq[i];
				if (chk > tol) {
					inequil = false;
					break;
				}
			}
			return inequil;
		}

		// ------------------------------------------------------------------------------------------------------------------------------------------------------
		// Method to get member index of given reaction defined by its Z, N, and
		// RObject reacIndex in the
		// ReactionGroup. Returns -1 if it fails to find index, indicating
		// something is wrong in the bookkeeping.
		// Thus, for a reaction defined by RObject[Z}[Nindex][reacIndex], it can
		// be identified in the
		// ReactionGroup object RGgroup[] by first getting its RGgroup index
		// using
		//
		// int rgindex = RGC[Z][N][reacIndex];
		//
		// Then use that to return the member index of the reaction in RGGroup[]
		// by
		//
		// int mindex = RGgroup[grindex].
		//  
		// The fields of the RG object corresponding to this reaction can be
		// accessed by commands like
		//
		// String reacS = RGgroup[rgindex].reactions[mindex].reacString;
		// ------------------------------------------------------------------------------------------------------------------------------------------------------

		public int getMemberIndex(int Z, int N, int reacIndex) {
			int index = -1;
			for (int i = 0; i < this.members; i++) {
				int NN = Narg[i];
				if (Zarg[i] > 2)
					NN += minNetN[Zarg[i]];
				if (Zarg[i] == Z && NN == N && reacIndex == this.reacIndices[i]) {
					index = i;
					break;
				}
			}
			return index;
		}

		// ---------------------------------------------------------------------------------------------------------------------------------------------------------
		// Method to sum the total reaction flux associated with the reaction group
		// ---------------------------------------------------------------------------------------------------------------------------------------------------------

		public double sumFluxes() {
			double sumF = 0;
			for (int i = 0; i < this.members; i++) {
				if (this.reactions[i].forward) {
					sumF += this.reactions[i].flux;
				} else {
					sumF -= this.reactions[i].flux;
				}
			}
			return sumF;
		}

		// ------------------------------------------------------------------------------------------------------------------------------------------------------
		// Method to get boolean defining whether this reaction group is
		// presently in equibrium. See also the method testForEquilibrium().
		// ------------------------------------------------------------------------------------------------------------------------------------------------------

		public boolean getEquil() {
			return isEquil;
		}

		// ------------------------------------------------------------------------------------------------------------------------------------------------------
		// Method to set boolean determining whether this reaction group is in
		// equilibrium
		// ------------------------------------------------------------------------------------------------------------------------------------------------------

		public void setEquil(boolean isEquil) {
			this.isEquil = isEquil;
		}

	} /* end inner class ReactionGroup */

} /* end class StochasticElements */
