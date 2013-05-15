#!/bin/bash

cp -f ../out/production/task6/*.class .

for i in `seq 1 16`; do
    java MatrixMultiplication 500 $i graphgen
done > stats.dat

gnuplot -e "set xlabel \"Threads\"; set ylabel \"Time (microseconds)\"; plot \"stats.dat\" using 1:2 title 'Performance stats' with lines" -p
rm -f stats.dat *.class
