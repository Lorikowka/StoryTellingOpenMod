package com.storyteam.storytelling.client;

import java.util.ArrayList;
import net.minecraftforge.common.util.BlockSnapshot;

public interface IClientCapturingBlockSnapshots {
   void setCapturingClientBlockSnapshots(boolean var1);

   ArrayList<BlockSnapshot> getCapturedClientSideBlockSnapshots();
}
