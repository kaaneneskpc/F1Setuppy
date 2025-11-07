package com.kaaneneskpc.f1setupinstructor.domain.usecase;

import com.kaaneneskpc.f1setupinstructor.domain.repository.SetupRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class GetSetupsImpl_Factory implements Factory<GetSetupsImpl> {
  private final Provider<SetupRepository> setupRepositoryProvider;

  private GetSetupsImpl_Factory(Provider<SetupRepository> setupRepositoryProvider) {
    this.setupRepositoryProvider = setupRepositoryProvider;
  }

  @Override
  public GetSetupsImpl get() {
    return newInstance(setupRepositoryProvider.get());
  }

  public static GetSetupsImpl_Factory create(Provider<SetupRepository> setupRepositoryProvider) {
    return new GetSetupsImpl_Factory(setupRepositoryProvider);
  }

  public static GetSetupsImpl newInstance(SetupRepository setupRepository) {
    return new GetSetupsImpl(setupRepository);
  }
}
