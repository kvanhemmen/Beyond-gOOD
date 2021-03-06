package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;

/**
 * Represents an abstract implementation of the Function interface containing the common elements
 *      between its subclasses, namely its ArrayList of Formula arguments.
 * @param <T> represents the return type of a Function.
 */
public abstract class AbstractFunction<T extends Formula> implements Function<T> {
  protected ArrayList<Formula> args;

  /**
   * Represents the constructor for the AbstractFunction class, which establishes the ArrayList of
   *    Functions arguments.
   * @param args represents the given arguments to a Function.
   */
  protected AbstractFunction(ArrayList<Formula> args) {
    this.args = args;
  }

  @Override
  public Formula evaluate(Worksheet worksheet, Coord cellLoc) {
    return evaluateFunction(args, worksheet, cellLoc);
  }

  @Override
  public String getPrintString(Worksheet worksheet, Coord cellLoc, boolean clean) {
    return evaluateFunction(args, worksheet, cellLoc).getPrintString(worksheet, cellLoc, clean);
  }

  @Override
  public <R> R accept(FormulaVisitor<R> visitor) {
    return visitor.visitFunction(this);
  }

  @Override
  public ValueType getValueType() {
    return ValueType.NONE;
  }

  @Override
  public ArrayList<Formula> getArguments() {
    return (ArrayList<Formula>) this.args.clone();
  }

  protected boolean anyNullArgs(Worksheet worksheet, Coord cellLoc) {
    for (Formula f : args) {
      if (f.evaluate(worksheet, cellLoc) == null) {
        return true;
      }
    }

    return false;
  }

  protected boolean allNullArgs(Worksheet worksheet, Coord cellLoc) {
    boolean allNull = true;
    for (Formula f : args) {
      if (f.evaluate(worksheet, cellLoc) != null) {
        allNull = false;
      }
    }

    return allNull;
  }
}
