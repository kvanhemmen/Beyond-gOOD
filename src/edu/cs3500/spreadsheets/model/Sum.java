package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Sum extends AbstractFunction<DoubleValue> {

  public Sum(ArrayList<Formula> args) {
    super(args);
  }

  @Override
  public DoubleValue evaluateFunction(ArrayList<Formula> args, HashMap<Coord, Cell> worksheet) {
    double sum = 0.0;
    for (Formula arg : args) {
      if (arg.getValueType() == ValueType.NONE) {
        sum += ((DoubleValue) arg.evaluate(worksheet)).getValue();
      }
    }

    return new DoubleValue(sum);
  }
}