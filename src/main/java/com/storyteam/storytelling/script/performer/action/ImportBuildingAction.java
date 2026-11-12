package com.storyteam.storytelling.script.performer.action;

import com.storyteam.storytelling.Storytelling;
import com.storyteam.storytelling.script.performer.ScriptPerformer;
import com.storyteam.storytelling.script.performer.report.HelpReportBuilder;
import com.storyteam.storytelling.system._core.structio.StructIO;
import com.storyteam.storytelling.system._core.structio.repo.StructureRepository;
import com.storyteam.storytelling.util.ScriptUtils;
import java.io.File;

public class ImportBuildingAction extends ScriptPerformerAction {
   private final String name;
   private boolean isDone = false;
   private boolean fastForward = false;

   public ImportBuildingAction(ScriptPerformer performer, String name) {
      super(performer);
      this.name = name;
   }

   public ImportBuildingAction(ScriptPerformer performer, String name, boolean fastForward) {
      super(performer);
      this.name = name;
      this.fastForward = fastForward;
   }

   @Override
   public void tick() {
      if (!this.isDone) {
         File structFile = StructIO.getStructurePath(this.name).toFile();
         if (!structFile.isFile()) {
            StructureRepository.downloadStructure(this.script.registryAccess(), this.name, Storytelling.LOG::info).join();
         }

         StructIO.importBuilding(ScriptUtils.server(), this.name);
         this.isDone = true;
      }
   }

   @Override
   public void reset() {
      super.reset();
      this.isDone = false;
   }

   @Override
   public boolean isDone() {
      return this.isDone;
   }

   @Override
   public void fastForwardToStage() {
      if (this.fastForward) {
         File structFile = StructIO.getStructurePath(this.name).toFile();
         if (!structFile.isFile()) {
            StructureRepository.downloadStructure(this.script.registryAccess(), this.name, Storytelling.LOG::info).join();
         }

         StructIO.importBuilding(ScriptUtils.server(), this.name);
      }
   }

   @Override
   public HelpReportBuilder gatherHelpReport(HelpReportBuilder builder) {
      return builder.append("Вставка датника.");
   }
}
