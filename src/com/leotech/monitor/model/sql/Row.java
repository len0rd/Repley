package com.leotech.monitor.model.sql;

import com.google.visualization.datasource.datatable.TableRow;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Tuple> keyValues;

    public Row() {
        keyValues = new ArrayList<>();
    }

    public Row populate(ResultSet rs, List<String> columnNames, List<Integer> sqlTypes) {
        for (int i = 0; i < columnNames.size(); i++) {
            keyValues.add(addTuple(rs, i+1, columnNames.get(i), sqlTypes.get(i)));
        }
        return this;
    }

    private Tuple addTuple(ResultSet rs, int index, String columnName, int type) {
        try {
            switch(type) {
                case Types.INTEGER:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.BIGINT:
                case Types.NUMERIC:
                    return new Tuple(columnName, rs.getInt(index), Types.INTEGER);
                case Types.BOOLEAN:
                    return new Tuple(columnName, rs.getBoolean(index), Types.BOOLEAN);
                case Types.DECIMAL:
                case Types.DOUBLE:
                    return new Tuple(columnName, rs.getDouble(index), Types.DOUBLE);
                case Types.FLOAT:
                    return new Tuple(columnName, rs.getFloat(index), Types.FLOAT);
                case Types.NULL:
                    return new Tuple(columnName, null, Types.VARCHAR);
                default:
                    return new Tuple(columnName, rs.getString(index), Types.VARCHAR);
            }
        }
        catch (SQLException se) {
            System.out.println("ERR::Couldn't create a new Tuple");
            se.printStackTrace();
        }
        return null;
    }

    public List<Tuple> getTuples() {
        return keyValues;
    }

    public boolean isEmpty() {
        return keyValues == null || keyValues.isEmpty();
    }

    public List<TableRow> splitRowIntoGoogTableRows() {
        List<TableRow> trs = new ArrayList<>();
        if (!isEmpty()) {
            for (Tuple t : keyValues) {
                TableRow tr = new TableRow();
                tr.addCell(t.getKey());
                this.addCell(tr, t); //add the corresponding value
                trs.add(tr);
            }
        }
        return trs;
    }

    public TableRow getGoogTableRow() {
        TableRow tr = new TableRow();
        if (!isEmpty()) {
            for (Tuple t : keyValues) {
                this.addCell(tr, t);
            }
        }
        return tr;
    }

    public JSONObject toJSONObject() {
        JSONObject out = new JSONObject();
        for (Tuple t : keyValues) {
            addKeyValue(out, t);
        }
        return out;
    }

    private void addKeyValue(JSONObject jo, Tuple t) {
        switch (t.getBasicSqlType()) {
            case Types.INTEGER:
                jo.put(t.getKey(), (int)t.getValue());
                break;
            case Types.BOOLEAN:
                jo.put(t.getKey(), (boolean)t.getValue());
                break;
            case Types.DOUBLE:
                jo.put(t.getKey(), (double)t.getValue());
                break;
            case Types.FLOAT:
                jo.put(t.getKey(), (float)t.getValue());
                break;
            default:
                jo.put(t.getKey(), (String)t.getValue());
                break;
        }
    }

    //TODO:: THIS is soooo dumb. need better way. Generics
    private void addCell(TableRow tr, Tuple t) {
        switch (t.getBasicSqlType()) {
            case Types.INTEGER:
                tr.addCell((int)t.getValue());
                break;
            case Types.BOOLEAN:
                tr.addCell((boolean)t.getValue());
                break;
            case Types.DOUBLE:
                tr.addCell((double)t.getValue());
                break;
            case Types.FLOAT:
                tr.addCell((float)t.getValue());
                break;
            default:
                tr.addCell((String)t.getValue());
                break;
        }
    }
}
