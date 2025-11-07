package com.kaaneneskpc.f1setupinstructor.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class ResearchServiceImpl_Factory implements Factory<ResearchServiceImpl> {
  @Override
  public ResearchServiceImpl get() {
    return newInstance();
  }

  public static ResearchServiceImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ResearchServiceImpl newInstance() {
    return new ResearchServiceImpl();
  }

  private static final class InstanceHolder {
    static final ResearchServiceImpl_Factory INSTANCE = new ResearchServiceImpl_Factory();
  }
}
