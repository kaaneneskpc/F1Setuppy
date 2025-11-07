package com.kaaneneskpc.f1setupinstructor.core.data.repository;

import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao;
import com.kaaneneskpc.f1setupinstructor.core.network.FakeResearchService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.CoroutineScope;

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
public final class SetupRepositoryImpl_Factory implements Factory<SetupRepositoryImpl> {
  private final Provider<SetupDao> setupDaoProvider;

  private final Provider<FakeResearchService> researchServiceProvider;

  private final Provider<CoroutineScope> externalScopeProvider;

  private SetupRepositoryImpl_Factory(Provider<SetupDao> setupDaoProvider,
      Provider<FakeResearchService> researchServiceProvider,
      Provider<CoroutineScope> externalScopeProvider) {
    this.setupDaoProvider = setupDaoProvider;
    this.researchServiceProvider = researchServiceProvider;
    this.externalScopeProvider = externalScopeProvider;
  }

  @Override
  public SetupRepositoryImpl get() {
    return newInstance(setupDaoProvider.get(), researchServiceProvider.get(), externalScopeProvider.get());
  }

  public static SetupRepositoryImpl_Factory create(Provider<SetupDao> setupDaoProvider,
      Provider<FakeResearchService> researchServiceProvider,
      Provider<CoroutineScope> externalScopeProvider) {
    return new SetupRepositoryImpl_Factory(setupDaoProvider, researchServiceProvider, externalScopeProvider);
  }

  public static SetupRepositoryImpl newInstance(SetupDao setupDao,
      FakeResearchService researchService, CoroutineScope externalScope) {
    return new SetupRepositoryImpl(setupDao, researchService, externalScope);
  }
}
