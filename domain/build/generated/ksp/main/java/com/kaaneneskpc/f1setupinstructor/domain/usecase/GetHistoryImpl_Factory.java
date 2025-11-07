package com.kaaneneskpc.f1setupinstructor.domain.usecase;

import com.kaaneneskpc.f1setupinstructor.domain.repository.HistoryRepository;
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
public final class GetHistoryImpl_Factory implements Factory<GetHistoryImpl> {
  private final Provider<HistoryRepository> historyRepositoryProvider;

  private GetHistoryImpl_Factory(Provider<HistoryRepository> historyRepositoryProvider) {
    this.historyRepositoryProvider = historyRepositoryProvider;
  }

  @Override
  public GetHistoryImpl get() {
    return newInstance(historyRepositoryProvider.get());
  }

  public static GetHistoryImpl_Factory create(
      Provider<HistoryRepository> historyRepositoryProvider) {
    return new GetHistoryImpl_Factory(historyRepositoryProvider);
  }

  public static GetHistoryImpl newInstance(HistoryRepository historyRepository) {
    return new GetHistoryImpl(historyRepository);
  }
}
