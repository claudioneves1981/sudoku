package br.com.dio.enuns;

public enum GameStatusEnum {

    NON_STARTED("O jogo não foi iniciado"),
    INCOMPLETE("O jogo está incompleto"),
    COMPLETE("O jogo está completo");

    private final String label;

    GameStatusEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
