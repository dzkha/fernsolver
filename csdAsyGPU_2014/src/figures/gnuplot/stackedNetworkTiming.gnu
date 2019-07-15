set macro  # Enable macro definition

# Some macro definitions

# Colors: hex #RRGGBB
label_color = "#867961"
tic_color = "#383838"
title_color = "#383838"
myblue_color = "#5ea2c6
myred_color = "#bb6255"
mygreen_color = "#668874"

# Width and height of postscript figure in inches
width = 8
height = 5

# Line styles
#set style line 1 lt rgb myblue_color lw 1   # Define linestyle 1
#set style line 2 lt rgb myred_color lw 1    # Define linestyle 2
#set style line 3 lt rgb mygreen_color lw 1  # Define linestyle 3

# Point styles
set style line 1 lc rgb "#5ea2c6" pt 5   # square
set style line 2 lc rgb "#5ea2c6" pt 7   # circle
set style line 3 lc rgb 'dark-orange' pt 9   # triangle

#set xtics rotate        # Rotates x tic numbers by 90 degrees
#set ytics rotate        # Rotates y tic numbers by 90 degrees
# Set tic labeling with color
set xtics textcolor rgb tic_color
set ytics textcolor rgb tic_color
set bmargin 4  # Bottom margin
# Set screen display to same aspect ratio as postscript plot
set size ratio height/width

#set title 'Time for one GPU network integration step' textcolor rgb title_color
set xlabel 'Number networks' textcolor rgb tic_color
set ylabel 'Time (s)' textcolor rgb tic_color
# Uncomment following to set log or log-log plots
#set logscale x
#set logscale y
set xrange [0:128]
set yrange[0.000:0.002]
set pointsize 1.0    # Size of the plotted points
#set key top left    # Move legend to upper left
unset key            # Don't show legend
#set timestamp       # Date/time

# Read data from file and plot to screen

plot "async_150_full_perTimestep.dat" using 1:3 ls 2 with lines title 'Integration time, one step' 
#replot "async_150_full_perTimestep.dat" using 1:3 ls 2 with points title 'Integration time, one step

#plot "stackedNetworkTiming.dat" using 1:3 ls 3 title 'Integration time, one step' 

# Plot to postscript file

set out "stackedNetworkTiming.eps"    # Output file
set terminal postscript eps size width, height enhanced color solid lw 2 "Arial" 32
replot               # Plot to postscript file

# Plot to PNG file

set out "stackedNetworkTiming.png"
# Assume 72 pixels/inch and make bitmap twice as large for display resolution
set terminal pngcairo transparent size 2*width*72, 2*height*72 lw 2 enhanced font 'Arial,28'
replot

quit