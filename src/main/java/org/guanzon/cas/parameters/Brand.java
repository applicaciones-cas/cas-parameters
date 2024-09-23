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
import org.guanzon.cas.model.parameters.Model_Brand;
import org.guanzon.cas.model.parameters.Model_Category;
import org.json.simple.JSONObject;

public class Brand implements GRecord {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psRecdStat;

    Model_Brand poModel;
    ArrayList<Model_Brand> poModelList;
    JSONObject poJSON;

    public Brand(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Brand(foGRider);
        
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
    public JSONObject searchRecord(String fsValue, boolean fbByCode){
        String lsCondition = "";
        
        poJSON = new JSONObject();

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }
        
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), lsCondition);
        
        if (fbByCode){
            ResultSet loRS = poGRider.executeQuery(MiscUtil.addCondition(getSQ_Browse(), "a.sBrandIDx = " + SQLUtil.toSQL(fsValue)));
            
            try {
                if (loRS.next()){
                    lsSQL = loRS.getString("sBrandIDx");
                    MiscUtil.close(loRS);
                    
                    return poModel.openRecord(lsSQL);
                } else {
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record to load.");
                    return poJSON;
                }
            } catch (SQLException e) {
                poJSON.put("result", "error");
                poJSON.put("message", e.getMessage());
                return poJSON;
            }
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "ID»Description»Category",
                "a.sBrandIDx»a.sDescript»xCategrNm",
                "a.sBrandIDx»a.sDescript»b.sDescript",
                fbByCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sBrandIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record to load.");
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
        
        if (fbByCode){
            ResultSet loRS = poGRider.executeQuery(MiscUtil.addCondition(getSQ_Browse(), "a.sBrandIDx = " + SQLUtil.toSQL(fsValue)));
            
            try {
                if (loRS.next()){
                    lsSQL = loRS.getString("sBrandIDx");
                    MiscUtil.close(loRS);
                    
                    return poModel.openRecord(lsSQL);
                } else {
                    poJSON.put("result", "error");
                    poJSON.put("message", "No record to load.");
                    return poJSON;
                }
            } catch (SQLException e) {
                poJSON.put("result", "error");
                poJSON.put("message", e.getMessage());
                return poJSON;
            }
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "ID»Description»Category",
                "a.sBrandIDx»a.sDescript»xCategrNm",
                "a.sBrandIDx»a.sDescript»b.sDescript",
                fbByCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sBrandIDx"));
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record to load.");
            return poJSON;
        }
    }

    //additional methods
    @Override
    public Model_Brand getModel() {
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
                Model_Brand List = new Model_Brand(poGRider);
                List.openRecord(loRS.getString("sBrandIDx"));
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
            Logger.getLogger(Brand.class.getName()).log(Level.SEVERE, null, ex);
            loJSON.put("result", "error");
            loJSON.put("message", ex.getMessage());
            return loJSON;
        }
    }

    public ArrayList<Model_Brand> getModelList() {
        return poModelList;
    }

    public JSONObject searchMaster(String fsColumn, String fsValue, boolean fbByCode) {

        JSONObject loJSON;

        switch (fsColumn) {
            case "sCategrCd":
                Category loCategory = new Category(poGRider, true);
                loCategory.setRecordStatus(psRecdStat);
                loJSON = loCategory.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    return poModel.setCategoryCode((String) loCategory.getMaster("sCategrCd"));
                } else {
                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No record found.");
                    return loJSON;
                }
            default:
                return null;

        }
    }

    public JSONObject searchMaster(int fnColumn, String fsValue, boolean fbByCode) {
        return searchMaster(poModel.getColumn(fnColumn), fsValue, fbByCode);

    }
    
    public Model_Category Category(){
        Model_Category loCategory = new Model_Category(poGRider);
        
        if (poModel.getCategoryCode().isEmpty()) 
            return loCategory;
        
        JSONObject loJSON = loCategory.openRecord(poModel.getCategoryCode());
        
        if (!"error".equals((String) loJSON.get("result"))){
            return loCategory;
        } else {
            return new Model_Category(poGRider);
        }
    }
    
    private String getSQ_Browse() {
        return "SELECT" +
                    "  a.sBrandIDx" +
                    ", a.sDescript" +
                    ", a.sCategrCd" +
                    ", a.cRecdStat" +
                    ", a.sModified" +
                    ", a.dModified" +
                    ", b.sDescript xCategrNm " +
                " FROM " + poModel.getTable() + " a" +
                    " LEFT JOIN Category b ON a.sCategrCd = b.sCategrCd";
    }
}
