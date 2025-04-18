package app.ewarehouse.mapper;

import app.ewarehouse.dto.MBuyerDepositorDTO;
import app.ewarehouse.entity.MBuyerDepositor;
import app.ewarehouse.util.Mapper;

public class MBuyerDepositorMapper {
    public static MBuyerDepositorDTO mapToDTO(MBuyerDepositor entity) {
        if (entity == null) {
            return null;
        }

        return MBuyerDepositorDTO.builder()
                .vchEntityId(entity.getVchEntityId())
                .applicationDetails(Mapper.mapToBuyerResponse(entity.getApplicationDetails()))
                .enmStatus(entity.getEnmStatus())
                .dtmCreatedOn(entity.getDtmCreatedOn())
                .stmUpdatedOn(entity.getStmUpdatedOn())
                .intCreatedBy(entity.getIntCreatedBy())
                .intUpdatedBy(entity.getIntUpdatedBy())
                .bitDeletedFlag(entity.getBitDeletedFlag())
                .build();
    }
}
