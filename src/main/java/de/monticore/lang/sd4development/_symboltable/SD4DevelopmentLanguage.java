package de.monticore.lang.sd4development._symboltable;

public class SD4DevelopmentLanguage extends SD4DevelopmentLanguageTOP {

  private static final String LANG_NAME = "Sequence Diagrams for Development";

  private static final String FILE_EXTENSION = "sd";

  public SD4DevelopmentLanguage() {
    super(LANG_NAME, FILE_EXTENSION);
  }

  @Override
  protected SD4DevelopmentModelLoader provideModelLoader() {
    return new SD4DevelopmentModelLoader(this);
  }
}
