package com.storyteam.storytelling.system.rts.init;

import com.storyteam.storytelling.content.entities.data.EntityDataSerializerHL;
import com.storyteam.storytelling.system.rts.units.api.data.AntUnitData;
import com.storyteam.storytelling.system.rts.units.api.data.FormikProfession;
import java.util.function.UnaryOperator;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister(
   prefix = "system/rts/entity_data_serializer/"
)
public interface RTSEntityDataSerializersST {
   @RegistryName("ant_unit_data")
   EntityDataSerializerHL<AntUnitData> ANT_UNIT_DATA = EntityDataSerializerHL.simpleCopyable((buf, data) -> {
      buf.writeUtf(data.getProfession().toString());
      buf.writeBoolean(data.isElite());
   }, buf -> new AntUnitData(FormikProfession.valueOf(buf.readUtf()), buf.readBoolean()), UnaryOperator.identity());
}
