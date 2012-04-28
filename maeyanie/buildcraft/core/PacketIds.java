package buildcraft.core;


public enum PacketIds {

   TileDescription("TileDescription", 0),
   TileUpdate("TileUpdate", 1),
   PipeItem("PipeItem", 2),
   DiamondPipeContents("DiamondPipeContents", 3),
   DiamondPipeGUI("DiamondPipeGUI", 4),
   AutoCraftingGUI("AutoCraftingGUI", 5),
   FillerGUI("FillerGUI", 6),
   TemplateGUI("TemplateGUI", 7),
   BuilderGUI("BuilderGUI", 8),
   EngineSteamGUI("EngineSteamGUI", 9),
   EngineCombustionGUI("EngineCombustionGUI", 10);
   // $FF: synthetic field
   private static final PacketIds[] $VALUES = new PacketIds[]{TileDescription, TileUpdate, PipeItem, DiamondPipeContents, DiamondPipeGUI, AutoCraftingGUI, FillerGUI, TemplateGUI, BuilderGUI, EngineSteamGUI, EngineCombustionGUI};


   private PacketIds(String var1, int var2) {}

}
