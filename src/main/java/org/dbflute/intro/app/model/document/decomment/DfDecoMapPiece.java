package org.dbflute.intro.app.model.document.decomment;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dbflute.helper.mapstring.MapListString;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapPiece {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String fileName;
    protected String formatVersion;
    protected String author;
    protected LocalDateTime decommentDatetime;
    protected boolean merged;
    protected DfDecoMapTablePart decoMap;

    // done cabos move to before accessor by jflute (2017/08/10)
    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    // map:{
    //    ; formatVersion = 1.0
    //    ; author = cabos
    //    ; decommentDatetime = 2017/12:31 12:34:56
    //    ; merged = false
    //    ; decoMap = map:{
    //        ; MEMBER = map:{
    //            ; MEMBER_NAME = map:{
    //                ; decomment = piari
    //                ; databaseComment = sea
    //                ; previousWholeComment = seasea
    //                ; commentVersion = 1
    //                ; authorList = list: { cabos }
    //            }
    //        }
    //    }
    // }
    public Map<String, Object> convertMap() {
        // done cabos Map by jflute (2017/08/10)
        // done cabos use Linked by jflute (2017/09/07)
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("formatVersion", this.formatVersion);
        map.put("author", this.author);
        map.put("decommentDatetime", this.decommentDatetime);
        map.put("merged", this.merged);
        map.put("decoMap", this.decoMap.convertPieceMap());
        return map;
    }

    // ===================================================================================
    //                                                                            Override
    //                                                                            ========
    @Override
    public String toString() {
        return new MapListString().buildMapString(this.convertMap());
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDecommentDatetime() {
        return decommentDatetime;
    }

    public void setDecommentDatetime(LocalDateTime decommentDatetime) {
        this.decommentDatetime = decommentDatetime;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public DfDecoMapTablePart getDecoMap() {
        return decoMap;
    }

    public void setDecoMap(DfDecoMapTablePart decoMap) {
        this.decoMap = decoMap;
    }
}