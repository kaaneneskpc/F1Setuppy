package com.kaaneneskpc.f1setupinstructor.core.common.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DispatchersModule_ProvideCoroutineScopeFactory implements Factory<CoroutineScope> {
  @Override
  public CoroutineScope get() {
    return provideCoroutineScope();
  }

  public static DispatchersModule_ProvideCoroutineScopeFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CoroutineScope provideCoroutineScope() {
    return Preconditions.checkNotNullFromProvides(DispatchersModule.INSTANCE.provideCoroutineScope());
  }

  private static final class InstanceHolder {
    static final DispatchersModule_ProvideCoroutineScopeFactory INSTANCE = new DispatchersModule_ProvideCoroutineScopeFactory();
  }
}
