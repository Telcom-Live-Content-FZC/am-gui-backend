package mbs.am.gui.mapper;


import mbs.am.gui.entity.SignalCatalogEntity;
import mbs.am.gui.model.SignalCatalog;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SignalCatalogMapper {

    public SignalCatalog toModel(SignalCatalogEntity entity) {
        if (entity == null) return null;
        return SignalCatalog.builder()
                .key(entity.getKey())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .build();
    }

    public SignalCatalogEntity toEntity(SignalCatalog model) {
        if (model == null) return null;
        SignalCatalogEntity entity = new SignalCatalogEntity();
        entity.setKey(model.getKey());
        entity.setCategory(model.getCategory());
        entity.setDescription(model.getDescription());
        return entity;
    }
}
