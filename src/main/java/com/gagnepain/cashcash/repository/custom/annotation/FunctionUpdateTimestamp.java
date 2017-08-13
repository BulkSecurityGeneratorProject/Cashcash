package com.gagnepain.cashcash.repository.custom.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.hibernate.annotations.ValueGenerationType;

@ValueGenerationType(generatedBy = FunctionUpdateValueGeneration.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionUpdateTimestamp {
}
