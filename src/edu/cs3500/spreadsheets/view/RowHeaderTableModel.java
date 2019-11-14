package edu.cs3500.spreadsheets.view;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class RowHeaderTableModel extends DefaultTableModel {
  private TableModel dataModel;

  public RowHeaderTableModel(TableModel dataModel) {
    this.dataModel = dataModel;
  }

  @Override
  public int getRowCount() {
    if (dataModel != null) {
      return dataModel.getRowCount();
    } else {
      return 0;
    }
  }

  @Override
  public int getColumnCount() {
    return 1;
  }

  @Override
  public String getColumnName(int column) {
    return "";
  }

  @Override
  public Object getValueAt(int row, int column) {
    return String.valueOf(row + 1);
  }
}
