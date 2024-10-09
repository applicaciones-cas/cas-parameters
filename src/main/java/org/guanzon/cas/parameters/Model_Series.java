package org.guanzon.cas.parameters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.model.parameters.Model_Model;
import org.guanzon.cas.model.parameters.Model_Model_Series;
import org.json.simple.JSONObject;

public class Model_Series implements GRecord {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psRecdStat;

    Model_Model_Series poModel;
    ArrayList<Model_Model> poModelList;
    JSONObject poJSON;

    public Model_Series(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Model_Series(foGRider);
        
        psRecdStat = "1";
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public void setRecordStatus(String fsValue) {
        psRecdStat = fsValue;
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }

    @Override
    public JSONObject setMaster(int fnCol, Object foData) {
        return poModel.setValue(fnCol, foData);
    }

    @Override
    public JSONObject setMaster(String fsCol, Object foData) {
        return poModel.setValue(fsCol, foData);
    }

    @Override
    public Object getMaster(int fnCol) {
        return poModel.getValue(fnCol);
    }

    @Override
    public Object getMaster(String fsCol) {
        return poModel.getValue(fsCol);
    }

    @Override
    public JSONObject newRecord() {
        return poModel.newRecord();
    }

    @Override
    public JSONObject openRecord(String fsValue) {
        return poModel.openRecord(fsValue);
    }

    @Override
    public JSONObject updateRecord() {
        JSONObject loJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.UPDATE) {
            loJSON.put("result", "success");
            loJSON.put("message", "Edit mode has changed to update.");
        } else {
            loJSON.put("result", "error");
            loJSON.put("message", "No record loaded to update.");
        }

        return loJSON;
    }

    @Override
    public JSONObject saveRecord() {
        if (!pbWthParent) {
            poGRider.beginTrans();
        }

        poJSON = poModel.saveRecord();

        if ("success".equals((String) poJSON.get("result"))) {
            if (!pbWthParent) {
                poGRider.commitTrans();
            }
        } else {
            if (!pbWthParent) {
                poGRider.rollbackTrans();
            }
        }

        return poJSON;
    }

    @Override
    public JSONObject deleteRecord(String fsValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject deactivateRecord(String fsValue) {
        poJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.UPDATE) {
            poJSON = poModel.setActive(false);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModel.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }
        return poJSON;
    }

    @Override
    public JSONObject activateRecord(String fsValue) {
        poJSON = new JSONObject();

        if (poModel.getEditMode() == EditMode.UPDATE) {
            poJSON = poModel.setActive(true);

            if ("error".equals((String) poJSON.get("result"))) {
                return poJSON;
            }

            poJSON = poModel.saveRecord();
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
        }

        return poJSON;
    }

    @Override
    public JSONObject searchRecord(String fsValue, boolean fbByCode) {
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), lsCondition);

        poJSON = ShowDialogFX.Search(poGRider,
                                lsSQL,
                                    fsValue,
                            "ID»Description»Brand",
                            "a.sSeriesID»a.sDescript»xBrandNme",
                            "a.sSeriesID»a.sDescript»b.sDescript",
                                    fbByCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sSeriesID"));
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecord(String fsValue, boolean fbByCode, String fsCondition) {
        if (fsCondition.isEmpty()) return searchRecord(fsValue, fbByCode);
        
        String lsCondition = "";
        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = getSQ_Browse();
        lsSQL = MiscUtil.addCondition(lsSQL, fsCondition);
        lsSQL = MiscUtil.addCondition(lsSQL, lsCondition);
        
        poJSON = ShowDialogFX.Search(poGRider,
                                lsSQL,
                                    fsValue,
                            "ID»Description»Brand",
                            "a.sSeriesID»a.sDescript»xBrandNme",
                            "a.sSeriesID»a.sDescript»b.sDescript",
                                    fbByCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sModelIDx"));
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }

    //additional methods
    @Override
    public Model_Model_Series getModel() {
        return poModel;
    }

    public JSONObject loadModelList() {
        poModelList = new ArrayList<>();
        JSONObject loJSON = new JSONObject();
        try {
            String lsCondition = "";
            if (psRecdStat.length() > 1) {
                for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                    lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
                }

                lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
            } else {
                lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
            }
            String lsSQL = MiscUtil.addCondition(getSQ_Browse(), lsCondition);

            ResultSet loRS = poGRider.executeQuery(lsSQL);

            while (loRS.next()) {
                Model_Model List = new Model_Model(poGRider);
                List.openRecord(loRS.getString("sModelIDx"));
                poModelList.add(List);

            }

            if (!poModelList.isEmpty()) {
                loJSON.put("result", "success");
                loJSON.put("message", "Record loaded successfully.");
                return loJSON;
            } else {
                loJSON.put("result", "error");
                loJSON.put("message", "No record loaded to the list");
                return loJSON;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Model_Series.class.getName()).log(Level.SEVERE, null, ex);
            loJSON.put("result", "error");
            loJSON.put("message", ex.getMessage());
            return loJSON;
        }
    }

    public ArrayList<Model_Model> getModelList() {
        return poModelList;
    }
    
    public JSONObject searchMaster(String fsColumn, String fsValue, boolean fbByCode) {
        JSONObject loJSON;

        switch (fsColumn) {
            case "sBrandIDx":
                Brand loBrand = new Brand(poGRider, true);
                loJSON = loBrand.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    poModel.setBrandID((String) loBrand.getMaster("sBrandIDx"));
                    
                    loJSON = new JSONObject();
                    loJSON.put("result", "success");
                } else {
                    poModel.setBrandID("");
                    
                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                }
                
                return loJSON;
            default:
                return null;

        }
    }

    public JSONObject searchMaster(int fnColumn, String fsValue, boolean fbByCode) {
        return searchMaster(poModel.getColumn(fnColumn), fsValue, fbByCode);
    }
    
    private String getSQ_Browse(){
        return "SELECT" +
                    "  a.sSeriesID" +
                    ", a.sDescript" +
                    ", a.sBrandIDx" +
                    ", a.cEndOfLfe" +
                    ", a.cRecdStat" +
                    ", a.sModified" +
                    ", a.dModified" +
                    ", b.sDescript xBrandNme" +
                " FROM " + poModel.getTable() + " a" +
                    " LEFT JOIN Brand b ON a.sBrandIDx = b.sBrandIDx";
    }
}

