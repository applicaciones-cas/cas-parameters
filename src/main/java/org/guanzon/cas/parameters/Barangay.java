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
import org.guanzon.cas.model.parameters.Model_Barangay;
import org.json.simple.JSONObject;

public class Barangay implements GRecord {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psRecdStat;

    Model_Barangay poModel;
    ArrayList<Model_Barangay> poModelList;
    JSONObject poJSON;

    public Barangay(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Barangay(foGRider);
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

        String lsSQL = MiscUtil.addCondition(poModel.makeSelectSQL(), " sBrgyName LIKE "
                + SQLUtil.toSQL(fsValue + "%") + " AND " + lsCondition);

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "ID»Name",
                "sBrgyIDxx»sBrgyName",
                "sBrgyIDxx»sBrgyName",
                fbByCode ? 0 : 1);

        if (poJSON
                != null) {
            return poModel.openRecord((String) poJSON.get("sBrgyIDxx"));
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }

    //additional methods
    @Override
    public Model_Barangay getModel() {
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
            String lsSQL = MiscUtil.addCondition(poModel.makeSelectSQL(), lsCondition);

            ResultSet loRS = poGRider.executeQuery(lsSQL);

            while (loRS.next()) {
                Model_Barangay List = new Model_Barangay(poGRider);
                List.openRecord(loRS.getString("sBrgyIDxx"));
                poModelList.add(List);

            }

            if (poModelList.size() != 0) {
                loJSON.put("result", "success");
                loJSON.put("message", "Record loaded successfully.");
                return loJSON;
            } else {
                loJSON.put("result", "error");
                loJSON.put("message", "No record loaded to the list");
                return loJSON;
            }

        } catch (SQLException ex) {
            Logger.getLogger(Barangay.class.getName()).log(Level.SEVERE, null, ex);
            loJSON.put("result", "error");
            loJSON.put("message", ex.getMessage());
            return loJSON;
        }
    }

    public ArrayList<Model_Barangay> getModelList() {
        return poModelList;
    }
}
