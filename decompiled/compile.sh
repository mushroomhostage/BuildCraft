#!/bin/sh
CLASSPATH=../../craftbukkit-1.2.5-R1.1-MCPC-SNAPSHOT-71.jar javac \
    `find buildcraft/core/ -name '*.java'` net/minecraft/server/mod_BuildCraftCore.java \
    `find buildcraft/builders/ -name '*.java'` net/minecraft/server/mod_BuildCraftBuilders.java \
    `find buildcraft/energy/ -name '*.java'` net/minecraft/server/mod_BuildCraftEnergy.java \
    `find buildcraft/factory/ -name '*.java'` net/minecraft/server/mod_BuildCraftFactory.java \
    `find buildcraft/transport/ -name '*.java'` net/minecraft/server/mod_BuildCraftTransport.java \
    `find buildcraft/api/ -name '*.java'`


