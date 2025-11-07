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
public final class FakeResearchService_Factory implements Factory<FakeResearchService> {
  @Override
  public FakeResearchService get() {
    return newInstance();
  }

  public static FakeResearchService_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FakeResearchService newInstance() {
    return new FakeResearchService();
  }

  private static final class InstanceHolder {
    static final FakeResearchService_Factory INSTANCE = new FakeResearchService_Factory();
  }
}
