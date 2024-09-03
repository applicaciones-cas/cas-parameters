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
import org.guanzon.cas.model.parameters.Model_Branch;
import org.json.simple.JSONObject;

/**
 *
 * @author luke
 */
public class Branch implements GRecord {

    GRider poGRider;
    boolean pbWthParent;
    int pnEditMode;
    String psRecdStat;

    Model_Branch poModel;
    ArrayList<Model_Branch> poModelList ;
    JSONObject poJSON;

    public Branch(GRider foGRider, boolean fbWthParent) {
        poGRider = foGRider;
        pbWthParent = fbWthParent;

        poModel = new Model_Branch(foGRider);
        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public int getEditMode() {
        return pnEditMode;
    }

    @Override
    public void setRecordStatus(String fsValue) {
        psRecdStat = fsValue;
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

    public JSONObject searchDetail(String fsColumn, String fsValue, boolean fbByCode) {

        String lsHeader = "";
        String lsColName = "";
        String lsColCrit = "";
        String lsSQL = "";
        String lsCondition = "";
        JSONObject loJSON;

        switch (fsColumn) {
            case "sProvIDxx": //sDescript
                Province loProvince = new Province(poGRider, true);
                loProvince.setRecordStatus(psRecdStat);
                loJSON = loProvince.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    System.out.println((String) loProvince.getMaster("sProvIDxx"));
                    System.out.println((String) loProvince.getMaster("sProvName"));
                    poModel.setTownID((String) loProvince.getMaster("sProvIDxx"));

                    return loJSON;

                } else {
                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No Transaction found.");
                    return loJSON;
                }

            case "sCompnyID": //3 //8-xCategrNm //9-xInvTypNm
                Company loCompany = new Company(poGRider, true);
                loCompany.setRecordStatus(psRecdStat);
                loJSON = loCompany.searchRecord(fsValue, fbByCode);

                if (loJSON != null) {
                    System.out.println((String) loCompany.getMaster("sCompnyID"));
                    System.out.println((String) loCompany.getMaster("sCompnyNm"));
                    poModel.setCompanyID((String) loCompany.getMaster("sCompnyID"));

                    return loJSON;

                } else {
                    loJSON = new JSONObject();
                    loJSON.put("result", "error");
                    loJSON.put("message", "No Transaction found.");
                    return loJSON;
                }

            default:
                return null;

        }
    }

    @Override
    public JSONObject deleteRecord(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
        if (fsValue == null) {
            fsValue = "";
        }
        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = MiscUtil.addCondition(poModel.makeSelectSQL(), " sBranchNm LIKE "
                + SQLUtil.toSQL(fsValue + "%") + " AND " + lsCondition);

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                fsValue,
                "Code»Name",
                "sBranchCd»sBranchNm",
                "sBranchCd»sBranchNm",
                fbByCode ? 0 : 1);

        if (poJSON
                != null) {
            return poModel.openRecord((String) poJSON.get("sBranchCd"));
        } else {
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded to update.");
            return poJSON;
        }
    }

    @Override
    public Model_Branch getModel() {
        return poModel;
    }

    public Province GetTownID(String fsPrimaryKey, boolean fbByCode) {
        Province instance = new Province(poGRider, fbByCode);
        instance.openRecord(fsPrimaryKey);
        return instance;
    }

    public Company GetCompanyID(String fsPrimaryKey, boolean fbByCode) {
        Company instance = new Company(poGRider, fbByCode);
        instance.openRecord(fsPrimaryKey);
        return instance;
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
                Model_Branch List = new Model_Branch(poGRider);
                List.openRecord(loRS.getString("sBranchCd"));
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
            Logger.getLogger(Branch.class.getName()).log(Level.SEVERE, null, ex);
            loJSON.put("result", "error");
            loJSON.put("message", ex.getMessage());
            return loJSON;
        }
    }

    public ArrayList<Model_Branch> getModelList() {
        return poModelList;
    }
}
