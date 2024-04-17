package org.guanzon.cas.parameters;

import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.iface.GRecord;
import org.guanzon.cas.model.parameters.Model_Salesman;
import org.json.simple.JSONObject;

public class Salesman implements GRecord{
    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psRecdStat;
    
    Model_Salesman poModel;
    JSONObject poJSON;
    
    public Salesman(GRider foGRider, boolean fbWthParent){
        poGRider = foGRider;
        pbWthParent = fbWthParent;
        
        poModel = new Model_Salesman(foGRider);
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
        return poModel.openRecord("sEmployID = " + SQLUtil.toSQL(fsValue));
    }

    @Override
    public JSONObject updateRecord() {
        JSONObject loJSON = new JSONObject();
        
        if (poModel.getEditMode() == EditMode.UPDATE){
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
        if (!pbWthParent) poGRider.beginTrans();
        
        poJSON = poModel.saveRecord();
        
        if ("success".equals((String) poJSON.get("result"))){
            if (!pbWthParent) poGRider.commitTrans();
        } else {
            if (!pbWthParent) poGRider.rollbackTrans();
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
        
        if (poModel.getEditMode() == EditMode.UPDATE){
            poJSON = poModel.setActive(false);
            
            if ("error".equals((String) poJSON.get("result"))) return poJSON;
            
            poJSON =  poModel.saveRecord();
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
        
        if (poModel.getEditMode() == EditMode.UPDATE){
            poJSON = poModel.setActive(true);
            
            if ("error".equals((String) poJSON.get("result"))) return poJSON;
            
            poJSON =  poModel.saveRecord();
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
        
        if (psRecdStat.length() > 1){
            for (int lnCtr = 0; lnCtr <= psRecdStat.length()-1; lnCtr++){
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }
            
            lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else{            
            lsCondition = "cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }
        
        String lsSQL = MiscUtil.addCondition(poModel.makeSQL(), lsCondition);
        
        return ShowDialogFX.Search(poGRider
                                        , lsSQL
                                        , fsValue
                                        , "Code»Name"
                                        , "xClientNm»xBranchNm"
                                        , "xClientNm»xBranchNm"
                                        , fbByCode ? 0 : 1);
    }
    
    //additional methods
    @Override
    public Model_Salesman getModel(){
        return poModel;
    }
}
