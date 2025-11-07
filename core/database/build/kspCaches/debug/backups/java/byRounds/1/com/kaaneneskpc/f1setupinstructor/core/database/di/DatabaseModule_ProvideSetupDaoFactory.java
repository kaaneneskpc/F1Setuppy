package com.kaaneneskpc.f1setupinstructor.core.database.di;

import com.kaaneneskpc.f1setupinstructor.core.database.AppDatabase;
import com.kaaneneskpc.f1setupinstructor.core.database.dao.SetupDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideSetupDaoFactory implements Factory<SetupDao> {
  private final Provider<AppDatabase> appDatabaseProvider;

  private DatabaseModule_ProvideSetupDaoFactory(Provider<AppDatabase> appDatabaseProvider) {
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public SetupDao get() {
    return provideSetupDao(appDatabaseProvider.get());
  }

  public static DatabaseModule_ProvideSetupDaoFactory create(
      Provider<AppDatabase> appDatabaseProvider) {
    return new DatabaseModule_ProvideSetupDaoFactory(appDatabaseProvider);
  }

  public static SetupDao provideSetupDao(AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSetupDao(appDatabase));
  }
}
