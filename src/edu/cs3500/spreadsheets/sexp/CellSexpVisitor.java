package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.cs3500.spreadsheets.model.And;
import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Concatenate;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.Formula;
import edu.cs3500.spreadsheets.model.FormulaCell;
import edu.cs3500.spreadsheets.model.GreaterThan;
import edu.cs3500.spreadsheets.model.LessThan;
import edu.cs3500.spreadsheets.model.Not;
import edu.cs3500.spreadsheets.model.Or;
import edu.cs3500.spreadsheets.model.Product;
import edu.cs3500.spreadsheets.model.Reference;
import edu.cs3500.spreadsheets.model.SquareRoot;
import edu.cs3500.spreadsheets.model.StringValue;
import edu.cs3500.spreadsheets.model.Subtract;
import edu.cs3500.spreadsheets.model.Sum;
import edu.cs3500.spreadsheets.model.Worksheet;

/**
 * A visitor that visits an s-expression and returns a Cell.
 */
public class CellSexpVisitor implements SexpVisitor<Cell> {
  private Coord location;
  private String rawContents;
  private Worksheet worksheet;

  /**
   * Public constructor for creating CellSexpVisitor object.
   * @param location location in which cell is being created
   * @param rawContents the raw contents of the cell as they were enetered by the user
   * @param worksheet the worksheet in which the cell is being created
   */
  public CellSexpVisitor(Coord location, String rawContents, Worksheet worksheet) {
    this.location = location;
    this.rawContents = rawContents;
    this.worksheet = worksheet;
  }

  @Override
  public Cell visitBoolean(boolean b) {
    return new FormulaCell(location, new BooleanValue(b), rawContents, worksheet);
  }

  @Override
  public Cell visitNumber(double d) {
    if (d == Math.floor(d) && !Double.isInfinite(d)) {
      String editedRaw = "" + ((int) Math.floor(d));
      return new FormulaCell(location, new DoubleValue(d), editedRaw, worksheet);
    } else {
      return new FormulaCell(location, new DoubleValue(d), rawContents, worksheet);
    }
  }

  @Override
  public Cell visitSList(List<Sexp> l) {
    FunctionCheckSexpVisitor functionChecker = new FunctionCheckSexpVisitor();
    if (l.get(0).accept(functionChecker) && l.size() > 1) {
      ArrayList<Formula> functionArgs = new ArrayList<>();
      ArrayList<Coord> refList = new ArrayList<>();
      FunctionArgsSexpVisitor argsVisitor = new FunctionArgsSexpVisitor();
      ReferenceSexpVisitor refVisitor = new ReferenceSexpVisitor();
      for (int i = 1; i < l.size(); i++) {
        if (i != l.size() - 1) {
          l.get(i).accept(argsVisitor);
          l.get(i).accept(refVisitor);
        } else {
          functionArgs = l.get(i).accept(argsVisitor);
          refList = l.get(i).accept(refVisitor);
        }
      }

      FunctionNameVisitor nameVisitor = new FunctionNameVisitor();
      switch (l.get(0).accept(nameVisitor)) {
        case "AND":
          return new FormulaCell(location, refList, new And(functionArgs), rawContents);
        case "CONCAT":
          return new FormulaCell(location, refList, new Concatenate(functionArgs), rawContents);
        case ">":
          return new FormulaCell(location, refList, new GreaterThan(functionArgs), rawContents);
        case "<":
          return new FormulaCell(location, refList, new LessThan(functionArgs), rawContents);
        case "NOT":
          return new FormulaCell(location, refList, new Not(functionArgs), rawContents);
        case "OR":
          return new FormulaCell(location, refList, new Or(functionArgs), rawContents);
        case "PRODUCT":
          return new FormulaCell(location, refList, new Product(functionArgs), rawContents);
        case "SQRT":
          return new FormulaCell(location, refList, new SquareRoot(functionArgs), rawContents);
        case "SUB":
          return new FormulaCell(location, refList, new Subtract(functionArgs), rawContents);
        case "SUM":
          return new FormulaCell(location, refList, new Sum(functionArgs), rawContents);
        default:
          // will never run
          throw new IllegalArgumentException("Should not run.");
      }
    } else if (l.size() == 1) {
      FunctionNameVisitor nameVisitor = new FunctionNameVisitor();
      String funcName = l.get(0).accept(nameVisitor);
      throw new IllegalArgumentException("Function " + funcName
              + "is being called with no arguments.");
    } else {
      throw new IllegalArgumentException("Too many arguments. Only enter one expression per cell.");
    }
  }

  @Override
  public Cell visitString(String s) {
    return new FormulaCell(location, new StringValue(s), rawContents, worksheet);
  }

  @Override
  public Cell visitSymbol(String s) {
    if (validReference(s) && parseCoord(s).equals(this.location)) {
      ArrayList<Coord> refList = new ArrayList<>();
      refList.add(parseCoord(s));
      return new FormulaCell(location, refList, new Reference(parseCoord(s)), rawContents);
    } else if (validReference(s)) {
      throw new IllegalArgumentException("Cell contains a cyclic reference.");
    } else {
      throw new IllegalArgumentException("Invalid input. "
              + "Input must be a boolean, number, String, or formula.");
    }
  }

  private boolean validReference(String s) {
    Pattern p = Pattern.compile("[^a-zA-Z0-9]");
    boolean validChars = !p.matcher(s).find();

    if (!validChars) {
      return false;
    }

    int firstNum = 0;
    int lastLetter = 1;

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (Character.isDigit(c)) {
        firstNum = i;
        break;
      }
    }

    for (int j = s.length() - 1; j >= 0; j--) {
      char d = s.charAt(j);
      if (Character.isLetter(d)) {
        lastLetter = j;
        break;
      }
    }

    return firstNum > lastLetter;
  }

  private Coord parseCoord(String s) {
    int firstNum = 0;
    int lastLetter = 0;

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (Character.isDigit(c)) {
        firstNum = i;
        break;
      }
    }

    for (int j = s.length() - 1; j >= 0; j--) {
      char d = s.charAt(j);
      if (Character.isLetter(d)) {
        lastLetter = j;
        break;
      }
    }

    int col = Coord.colNameToIndex(s.substring(0, lastLetter + 1));
    int row = Integer.parseInt(s.substring(firstNum));

    return new Coord(col, row);
  }
}
