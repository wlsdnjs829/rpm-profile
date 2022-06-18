package com.jinwon.rpm.profile.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

@UtilityClass
public class ModelMapperUtil {

    private final ModelMapper modelMapper = new ModelMapper();

    private ModelMapper getModelMapper() {
        return modelMapper;
    }

    public <T, E> E map(T source, Class<E> t) {
        return getModelMapper().map(source, t);
    }

}
