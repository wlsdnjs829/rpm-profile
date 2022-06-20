package com.jinwon.rpm.profile.utils;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import javax.annotation.PostConstruct;

@UtilityClass
public class ModelMapperUtil {

    private final ModelMapper modelMapper = new ModelMapper();

    @PostConstruct
    private void configuration() {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private ModelMapper getModelMapper() {
        return modelMapper;
    }

    public <T, E> E map(T source, Class<E> t) {
        return getModelMapper().map(source, t);
    }

}
