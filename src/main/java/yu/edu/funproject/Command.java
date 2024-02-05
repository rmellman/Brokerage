package yu.edu.funproject;


import java.util.function.Function;

public final class Command {
    private String symbol;
    private Function<String,Boolean> undo;
    public Command(String symbol, Function<String, Boolean> undo) {
        this.symbol = symbol;
        this.undo = undo;
    }
    public String getSymbol(){
        return symbol;
    }

    public boolean undo()
    {
        return undo.apply(symbol);
    }
}