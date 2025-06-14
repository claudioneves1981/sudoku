package br.com.dio.enuns;

public enum HasErrorsEnum {

    HAS_ERRORS(" e contém erros"),
    NO_ERRORS(" e não contém erros");

    private final String label;

   HasErrorsEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
