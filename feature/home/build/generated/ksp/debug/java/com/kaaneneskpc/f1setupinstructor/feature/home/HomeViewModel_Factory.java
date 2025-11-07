package com.kaaneneskpc.f1setupinstructor.feature.home;

import com.kaaneneskpc.f1setupinstructor.domain.usecase.GetSetups;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetSetups> getSetupsProvider;

  private HomeViewModel_Factory(Provider<GetSetups> getSetupsProvider) {
    this.getSetupsProvider = getSetupsProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getSetupsProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<GetSetups> getSetupsProvider) {
    return new HomeViewModel_Factory(getSetupsProvider);
  }

  public static HomeViewModel newInstance(GetSetups getSetups) {
    return new HomeViewModel(getSetups);
  }
}
