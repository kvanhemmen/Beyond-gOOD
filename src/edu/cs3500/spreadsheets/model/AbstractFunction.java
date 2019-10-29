package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractFunction<T extends Formula> implements Function<T> {
  protected ArrayList<Formula> args;

  protected AbstractFunction(ArrayList<Formula> args) {
    this.args = args;
  }

  @Override
  public Formula evaluate(HashMap<Coord, Cell> worksheet) {
    return evaluateFunction(args, worksheet);
  }

  @Override
  public String getPrintString(HashMap<Coord, Cell> worksheet) {
    return evaluateFunction(args, worksheet).getPrintString(worksheet);
  }

  @Override
  public ValueType getValueType() {
    return ValueType.NONE;
  }
}
