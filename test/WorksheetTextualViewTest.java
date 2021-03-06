import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import edu.cs3500.spreadsheets.model.BeyondGoodWorksheet;
import edu.cs3500.spreadsheets.model.BeyondGoodWorksheetBuilder;
import edu.cs3500.spreadsheets.model.WorksheetAdapter;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;

import static org.junit.Assert.assertEquals;

/**
 * Testing the WorksheetTextualView class.
 */
public class WorksheetTextualViewTest {
  @Test
  public void testFile1() throws FileNotFoundException {
    BeyondGoodWorksheet test1 = WorksheetReader.read(new BeyondGoodWorksheetBuilder(),
            new FileReader("resources/test1.gOOD"));

    StringBuilder viewOutput = new StringBuilder();
    WorksheetTextualView textualView = new WorksheetTextualView(new WorksheetAdapter(test1),
            viewOutput);
    textualView.renderView();
    assertEquals(test1, WorksheetReader.read(new BeyondGoodWorksheetBuilder(),
            new StringReader(textualView.getAppendable().toString())));
  }

  @Test
  public void testFile2() throws FileNotFoundException {
    BeyondGoodWorksheet test2 = WorksheetReader.read(new BeyondGoodWorksheetBuilder(),
            new FileReader("resources/test2.gOOD"));

    StringBuilder viewOutput = new StringBuilder();
    WorksheetTextualView textualView = new WorksheetTextualView(new WorksheetAdapter(test2),
            viewOutput);
    textualView.renderView();
    assertEquals(test2, WorksheetReader.read(new BeyondGoodWorksheetBuilder(),
            new StringReader(textualView.getAppendable().toString())));
  }

  @Test
  public void testFileNBA() throws FileNotFoundException {
    BeyondGoodWorksheet testNBA = WorksheetReader.read(new BeyondGoodWorksheetBuilder(),
            new FileReader("resources/testNBAData.gOOD"));

    StringBuilder viewOutput = new StringBuilder();
    WorksheetTextualView textualView = new WorksheetTextualView(new WorksheetAdapter(testNBA),
            viewOutput);
    textualView.renderView();
    assertEquals(testNBA, WorksheetReader.read(new BeyondGoodWorksheetBuilder(),
            new StringReader(textualView.getAppendable().toString())));
  }
}
