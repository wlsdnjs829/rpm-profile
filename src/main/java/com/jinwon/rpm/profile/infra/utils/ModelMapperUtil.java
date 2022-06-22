package com.jinwon.rpm.profile.infra.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Objects;

@UtilityClass
public class ModelMapperUtil {

    private final ModelMapper modelMapper = new CustomModelMapper();
    private final ModelMapper patchModelMapper = new CustomModelMapper();

    static {
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        patchModelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);
    }

    private ModelMapper getModelMapper() {
        return modelMapper;
    }

    private ModelMapper getPatchModelMapper() {
        return patchModelMapper;
    }

    public <T, E> E map(T source, Class<E> t) {
        return getModelMapper().map(source, t);
    }

    public void patchMap(Object source, Object destination) {
        getPatchModelMapper().map(source, destination);
    }

    public class CustomModelMapper extends ModelMapper {

        @Override
        public <D> D map(Object source, Class<D> destinationType) {
            if (Objects.isNull(source)) {
                return super.map(new Object(), destinationType);
            }

            return super.map(source, destinationType);
        }

    }

}
