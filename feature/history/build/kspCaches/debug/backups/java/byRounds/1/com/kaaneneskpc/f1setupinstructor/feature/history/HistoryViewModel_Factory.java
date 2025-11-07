package com.kaaneneskpc.f1setupinstructor.feature.history;

import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetHistory;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<GetHistory> getHistoryProvider;

  private HistoryViewModel_Factory(Provider<GetHistory> getHistoryProvider) {
    this.getHistoryProvider = getHistoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(getHistoryProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<GetHistory> getHistoryProvider) {
    return new HistoryViewModel_Factory(getHistoryProvider);
  }

  public static HistoryViewModel newInstance(GetHistory getHistory) {
    return new HistoryViewModel(getHistory);
  }
}
