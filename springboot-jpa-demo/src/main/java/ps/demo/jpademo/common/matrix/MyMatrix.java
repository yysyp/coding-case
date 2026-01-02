package ps.demo.jpademo.common.matrix;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ps.demo.jpademo.common.BeanTool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@ToString
public class MyMatrix implements Serializable {

    protected Integer rows = 0;

    protected Integer cols = 0;

    protected List<List<MyCell>> table = new ArrayList<>(new ArrayList<>());

    public MyMatrix() {

    }

    public MyMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        table = new ArrayList<>();
        initTable();
    }

    public MyMatrix(List<List<MyCell>> table) {
        this.table = table;
        this.rows = table.size();
        this.cols = table.get(0).size();
    }

    public void initTable() {
        for (int r = 0; r < this.rows; r++) {
            List<MyCell> row = new ArrayList<>();
            for (int c = 0; c < this.cols; c++) {
                row.add(new MyCell(c, r));
            }
            table.add(row);
        }
    }

    public void fillTable(List<List<Object>> rowsAndColData) {
        if (rowsAndColData == null) {
            return;
        }
        int dataRows = rowsAndColData.size();
        if (rows <= 0 || cols <= 0) {
            rows = dataRows;
            cols = rowsAndColData.get(0).size();
            initTable();
        }
        for (int r = 0; r < this.rows; r++) {
            if (r >= dataRows) {
                return;
            }
            List<MyCell> row = this.table.get(r);
            List<Object> dataRow = rowsAndColData.get(r);
            if (dataRow == null) {
                continue;
            }
            int dataCols = dataRow.size();
            for (int c = 0; c < this.cols; c++) {
                if (c >= dataCols) {
                    continue;
                }
                MyCell cell = row.get(c);
                Object data = dataRow.get(c);
                cell.setData(data);
            }
        }
    }

    public void addRow() {
        this.addRow(new ArrayList<>());
    }

    public void addRow(List<Object> rowdata) {
        if (rowdata == null) {
            return;
        }
        int r = rows;
        rows++;
        List<MyCell> row = new ArrayList<>();
        table.add(row);
        int length = rowdata.size();
        for (int c = 0; c < this.cols; c++) {
            MyCell cell = new MyCell(c, r);
            if (c < length) {
                cell.setData(rowdata.get(c));
            }
            row.add(cell);
        }
    }

    public void addColumn() {
        this.addColumn(new ArrayList<>());
    }

    public void addColumn(List<Object> coldata) {
        if (coldata == null) {
            return;
        }
        int c = cols;
        cols++;
        int length = coldata.size();
        for (int r = 0; r < this.rows; r++) {
            List<MyCell> row = table.get(r);
            MyCell cell = new MyCell(c, r);
            if (r < length) {
                cell.setData(coldata.get(r));
            }
            row.add(cell);
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.cols;
    }

    public MyCell getCell(int x, int y) {
        return table.get(y).get(x);
    }

    public void setCell(int x, int y, Object data) {
        MyCell myCell = this.getCell(x, y);
        myCell.setData(data);
    }

    public MyMatrix reverseRowColumn() {
        if (table == null) {
            return new MyMatrix(this.getCols(), this.getRows());
        }
        List<List<MyCell>> newTable = new ArrayList<>();
        for (int c = 0; c < this.cols; c++) {
            List<MyCell> row = new ArrayList<>();
            for (int r = 0; r < this.rows; r++) {
                MyCell myCell = this.getCell(c, r);
                MyCell newCell = (MyCell) BeanTool.clone(myCell);
                newCell.setX(myCell.getY());
                newCell.setY(myCell.getX());
                row.add(newCell);
            }
            newTable.add(row);
        }
        MyMatrix newMatrix = new MyMatrix(newTable);
        return newMatrix;
    }

    public List<List<Object>> toListList() {
        List<List<Object>> result = new ArrayList<>();
        for (int y = 0; y < this.rows; y++) {
            List<Object> row = new ArrayList<>();
            for (int x = 0; x < this.cols; x++) {
                MyCell cell = this.getCell(x, y);
                row.add(cell.getData());
            }
            result.add(row);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int r = 0; r < this.getRows(); r++) {
            for (int c = 0; c < this.getCols(); c++) {
                buffer.append(this.getCell(c, r));
                buffer.append("\t");
            }
            buffer.append(System.lineSeparator());
        }
        return buffer.toString();
    }


}
