package project.util;

public enum Type {
    ASSENZA,
    RITARDO,
    USCITA,
    ENTRATA,
    SCRITTO,
    ORALE;

    @Override
    public String toString() {
        return super.toString().charAt(0) + super.toString().substring(1).toLowerCase();
    }

    public boolean isEquals(String type) {
        return type.toUpperCase().contentEquals(super.name());
    }

    public int getIntValue() {
        switch(super.toString().toUpperCase()) {
            case "ASSENZA" -> {
                return 1;
            }
            case "ENTRATA" -> {
                return 2;
            }
            case "RITARDO" -> {
                return 3;
            }
            case "USCITA" -> {
                return 4;
            }
            case "SCRITTO" -> {
                return 5;
            }
            case "ORALE" -> {
                return 6;
            }
            default -> {
                return 0;
            }
        }
    }
}


