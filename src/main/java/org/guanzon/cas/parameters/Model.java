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
import org.guanzon.cas.model.parameters.Model_Model;
import org.guanzon.cas.model.parameters.Model_Model_Series;
import org.guanzon.cas.model.parameters.Model_Model_Variant;
import org.json.simple.JSONObject;

public class Model implements GRecord {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psRecdStat;

    Model_Model poModel;
    ArrayList<Model_Model> poModelList;
    JSONObject poJSON;

    public Model(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Model(foGRider);
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

            lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = MiscUtil.addCondition(MiscUtil.makeSelect(poModel), " sModelNme LIKE "
                + SQLUtil.toSQL(fsValue + "%") + " AND " + lsCondition);

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "Code»Name",
                "sModelIDx»sModelNme",
                "sModelIDx»sModelNme",
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
    public Model_Model getModel() {
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

                lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
            } else {
                lsCondition = "cRecdStat = " + SQLUtil.toSQL(psRecdStat);
            }
            String lsSQL = MiscUtil.addCondition(MiscUtil.makeSelect(poModel), lsCondition);

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
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
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
                return searchBrand(fsValue, fbByCode);
            case "sSeriesID":
                return searchModelSeries(fsValue, fbByCode);
            case "sVrntIDxx":
                return searchModelVariant(fsValue, fbByCode);
            default:
                return null;

        }
    }

    public JSONObject searchMaster(int fnColumn, String fsValue, boolean fbByCode) {
        return searchMaster(poModel.getColumn(fnColumn), fsValue, fbByCode);
    }
    
    public JSONObject searchBrand(String fsValue, boolean fbByCode){
        Brand loBrand = new Brand(poGRider, true);
        
        JSONObject loJSON = loBrand.searchRecord(fsValue, fbByCode);

        if (loJSON != null) {
            return poModel.setBrandID(loBrand.getModel().getBrandID());
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record found.");
            return loJSON;
        }
    }
    
    public JSONObject searchModelSeries(String fsValue, boolean fbByCode){
        Model_Series loSeries = new Model_Series(poGRider, true);
        JSONObject loJSON = loSeries.searchRecord(fsValue, fbByCode);

        if (loJSON != null) {
            if (!((String) loSeries.getMaster("sBrandIDx")).isEmpty()){
                poModel.setBrandID((String) loSeries.getMaster("sBrandIDx"));
            }

            return poModel.setSeriesID((String) loSeries.getMaster("sSeriesID"));
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record found.");
            return loJSON;
        }
    }
    
    public JSONObject searchModelVariant(String fsValue, boolean fbByCode){
        Model_Variant loVariant = new Model_Variant(poGRider, true);
        JSONObject loJSON = loVariant.searchRecord(fsValue, fbByCode);

        if (loJSON != null) {
            if (!((String) loVariant.getMaster("sBrandIDx")).isEmpty()){
                poModel.setBrandID((String) loVariant.getMaster("sBrandIDx"));
            }

            return poModel.setSeriesID((String) loVariant.getMaster("sVrntIDxx"));
        } else {
            loJSON = new JSONObject();
            loJSON.put("result", "error");
            loJSON.put("message", "No record found.");
            return loJSON;
        }
    }
    
    
    public Model_Brand Brand(){
        Model_Brand loBrand = new Model_Brand(poGRider);
        
        if (poModel.getBrandID().isEmpty()) return loBrand;
        
        JSONObject loJSON = loBrand.openRecord(poModel.getBrandID());
        
        if (!"error".equals((String) loJSON.get("result"))){
            return loBrand;
        } else {
            return new Model_Brand(poGRider);
        }
    }
    
    public Model_Model_Series Series(){
        Model_Model_Series loSeries = new Model_Model_Series(poGRider);
        
        if (poModel.getSeriesID().isEmpty()) return loSeries;
        
        JSONObject loJSON = loSeries.openRecord(poModel.getSeriesID());
        
        if (!"error".equals((String) loJSON.get("result"))){
            return loSeries;
        } else {
            return new Model_Model_Series(poGRider);
        }
    }
    
    public Model_Model_Variant Variant(){
        Model_Model_Variant loVariant = new Model_Model_Variant(poGRider);
        
        if (poModel.getVariantID().isEmpty()) return loVariant;
        
        JSONObject loJSON = loVariant.openRecord(poModel.getSeriesID());
        
        if (!"error".equals((String) loJSON.get("result"))){
            return loVariant;
        } else {
            return new Model_Model_Variant(poGRider);
        }
    }
}

