package com.kaaneneskpc.f1setupinstructor.domain.usecase;

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
public final class AskChatbotImpl_Factory implements Factory<AskChatbotImpl> {
  @Override
  public AskChatbotImpl get() {
    return newInstance();
  }

  public static AskChatbotImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AskChatbotImpl newInstance() {
    return new AskChatbotImpl();
  }

  private static final class InstanceHolder {
    static final AskChatbotImpl_Factory INSTANCE = new AskChatbotImpl_Factory();
  }
}
