package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;

public abstract class AbstractCell implements Cell {
  protected ArrayList<Coord> directRefs;
  protected Coord location;

  /**
   * Public constructor for when cell is null in worksheet.
   *
   * @param location coordinates for where cell is to be created in spreadsheet
   */
  protected AbstractCell(Coord location) {
    this.location = location;
    this.directRefs = new ArrayList<>();
  }

  /**
   * Public constructor for when cell is represented by BlankCell
   * in worksheet, i.e. when cell has existing direct references.
   *
   * @param existingRefs ArrayList of direct references to existing BlankCell
   * @param location coordinates fro where cell is to be created in spreadsheet
   */
  protected AbstractCell(Coord location, ArrayList<Coord> existingRefs) {
    this.location = location;
    this.directRefs = existingRefs;
  }

  @Override
  public ArrayList referencedBy() {
    return directRefs;
  }

  @Override
  public String getCellName() {
    return location.toString();
  }
}