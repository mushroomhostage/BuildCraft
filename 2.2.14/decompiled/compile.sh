#!/bin/sh
CLASSPATH=../../craftbukkit-1.2.5-R1.1-MCPC-SNAPSHOT-71.jar javac \
    net/minecraft/server/BuildCraftBlockUtil.java \
    net/minecraft/server/{mod_,}BuildCraftCore.java `find buildcraft/core/ -name '*.java'` \
    net/minecraft/server/{mod_,}BuildCraftBuilders.java `find buildcraft/builders/ -name '*.java'` \
    net/minecraft/server/{mod_,}BuildCraftEnergy.java `find buildcraft/energy/ -name '*.java'` \
    net/minecraft/server/{mod_,}BuildCraftFactory.java `find buildcraft/factory/ -name '*.java'` \
    net/minecraft/server/{mod_,}BuildCraftTransport.java `find buildcraft/transport/ -name '*.java'` \
    `find buildcraft/api/ -name '*.java'`


