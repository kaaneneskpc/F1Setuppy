package com.kaaneneskpc.f1setupinstructor.core.data.repository;

import com.kaaneneskpc.f1setupinstructor.core.database.dao.HistoryDao;
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
public final class HistoryRepositoryImpl_Factory implements Factory<HistoryRepositoryImpl> {
  private final Provider<HistoryDao> historyDaoProvider;

  private HistoryRepositoryImpl_Factory(Provider<HistoryDao> historyDaoProvider) {
    this.historyDaoProvider = historyDaoProvider;
  }

  @Override
  public HistoryRepositoryImpl get() {
    return newInstance(historyDaoProvider.get());
  }

  public static HistoryRepositoryImpl_Factory create(Provider<HistoryDao> historyDaoProvider) {
    return new HistoryRepositoryImpl_Factory(historyDaoProvider);
  }

  public static HistoryRepositoryImpl newInstance(HistoryDao historyDao) {
    return new HistoryRepositoryImpl(historyDao);
  }
}
