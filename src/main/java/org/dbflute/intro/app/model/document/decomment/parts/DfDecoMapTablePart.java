package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapTablePart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String tableName;
    protected List<DfDecoMapColumnPart> columns;

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    public static DfDecoMapTablePart createPieceTablePart(Map.Entry<String, Map<String, Map<String, Object>>> tablePartEntry) {
        final List<DfDecoMapColumnPart> pieceColumns = tablePartEntry.getValue()
                .entrySet()
                .stream()
                .map(columnEntry -> DfDecoMapColumnPart.createPieceColumnPart(columnEntry))
                .collect(Collectors.toList());

        DfDecoMapTablePart table = new DfDecoMapTablePart();
        table.setTableName((String) tablePartEntry.getKey());
        table.setColumns(pieceColumns);
        return table;
    }

    public static DfDecoMapTablePart createPickupTablePart(Map.Entry<String, Map<String, List<Map<String, Object>>>> tablePartEntry) {
        final List<DfDecoMapColumnPart> pickupColumns = tablePartEntry.getValue()
                .entrySet()
                .stream()
                .map(columnEntry -> DfDecoMapColumnPart.createPickupColumnPart(columnEntry))
                .collect(Collectors.toList());

        DfDecoMapTablePart table = new DfDecoMapTablePart();
        table.setTableName((String) tablePartEntry.getKey());
        table.setColumns(pickupColumns);
        return table;
    }

    public Map<String, Object> convertPieceMap() {
        Map<String, Object> columnMap = columns.stream()
                .collect(Collectors.toMap(column -> column.getColumnName(), column -> column.convertPieceMap(), (c1, c2) -> c1));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(tableName, columnMap);
        return map;
    }

    public Map<String, Object> convertPickupMap() {
        Map<String, List<Map<String, Object>>> columnMap = columns.stream()
                .collect(Collectors.toMap(column -> column.getColumnName(), column -> column.convertPickupMap(), (c1, c2) -> c1));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(tableName, columnMap);
        return map;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DfDecoMapColumnPart> getColumns() {
        return columns;
    }

    public void setColumns(List<DfDecoMapColumnPart> columns) {
        this.columns = columns;
    }

}
