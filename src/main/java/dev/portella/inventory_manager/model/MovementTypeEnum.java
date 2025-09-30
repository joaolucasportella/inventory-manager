package dev.portella.inventory_manager.model;

public enum MovementTypeEnum {
    entry("Entrada"),
    exit("Saída"),
    transfer("Transferência");

    private final String label;

    MovementTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
