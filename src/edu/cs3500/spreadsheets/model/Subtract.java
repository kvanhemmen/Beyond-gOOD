package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Subtract extends AbstractFunction<DoubleValue> {
  public Subtract(ArrayList<Formula> args) {
    super(args);
  }

  @Override
  public DoubleValue evaluateFunction(ArrayList<Formula> args, HashMap<Coord, Cell> worksheet) {
    if (args.size() != 2) {
      throw new IllegalArgumentException("Incorrect number of arguments.");
    } else if (args.get(0).getValueType() != ValueType.DOUBLE
            || args.get(1).getValueType() != ValueType.DOUBLE) {
      throw new IllegalArgumentException("Invalid arguments. Must use double arguments.");
    } else {
      return new DoubleValue(((DoubleValue) args.get(0).evaluate(worksheet)).getValue()
              - ((DoubleValue) args.get(0).evaluate(worksheet)).getValue());
    }
  }
}
