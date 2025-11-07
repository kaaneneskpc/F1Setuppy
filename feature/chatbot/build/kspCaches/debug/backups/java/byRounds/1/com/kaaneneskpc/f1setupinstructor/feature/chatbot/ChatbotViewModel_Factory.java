package com.kaaneneskpc.f1setupinstructor.feature.chatbot;

import com.kaaneneskpc.f1setupinstructor.domain.usecase.AskChatbot;
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
public final class ChatbotViewModel_Factory implements Factory<ChatbotViewModel> {
  private final Provider<AskChatbot> askChatbotProvider;

  private ChatbotViewModel_Factory(Provider<AskChatbot> askChatbotProvider) {
    this.askChatbotProvider = askChatbotProvider;
  }

  @Override
  public ChatbotViewModel get() {
    return newInstance(askChatbotProvider.get());
  }

  public static ChatbotViewModel_Factory create(Provider<AskChatbot> askChatbotProvider) {
    return new ChatbotViewModel_Factory(askChatbotProvider);
  }

  public static ChatbotViewModel newInstance(AskChatbot askChatbot) {
    return new ChatbotViewModel(askChatbot);
  }
}
