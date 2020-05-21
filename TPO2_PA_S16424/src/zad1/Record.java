package zad1;

public class Record {
    private String name;
    private int multiplication;
    private String symbol;
    private Double value;

    public Record(String name, String symbol, String value){
        String[] parts = symbol.split(" ");
        this.name = name;
        this.multiplication = Integer.valueOf(parts[0]);
        this.symbol = parts[1];
        this.value = Double.valueOf(value);
    }

    public String getName() {
        return name;
    }

    public int getMultiplication() {
        return multiplication;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getValue() {
        return value;
    }
}
