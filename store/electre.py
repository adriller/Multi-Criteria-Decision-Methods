#! /usr/bin/python
import time
import os
import sys
import glob

#method = ["saw", "topsis", "vikor"]
#inserir nome dos algoritmos
method = ["electreii"]
#nsensor = [1000, 10000,20000,30000,40000,50000,60000,70000,80000,90000,100000]
nsensor = [209555]
#nsensor = [1000, 10000,100000]
#escolhidos = [0.01, 0.05, 0.1]
escolhidos = [0.25]
criterios = [2,3,4,5,6]
#criterios = [2]
#criterios = [4]
#bd = [0,1,2]
bd = [1]
#seed = 0
#executar 10 vezes
replications = 2
#replications = 101

for n in nsensor:
 for e in escolhidos:
  for c in criterios:
   for b in bd:
    for m in method:	
     for seed in range(1,replications):
      	print "java -jar mcdm.jar " + str(m) + " " + str(n) + " " + " " + str(e) + " " + str(c) + " " + str(seed) + " " + str(b)
	os.system("java -jar mcdm.jar " + str(m) + " " + str(n) + " " + " " + str(e) + " " + str(c) + " " + str(seed) + " " + str(b))
#	print "teste"
