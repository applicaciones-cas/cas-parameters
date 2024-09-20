
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameters.Branch;
import org.guanzon.cas.parameters.Brand;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testBranch {

    static GRider instance;
    static Branch record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");

        instance = MiscUtil.Connect();
        record = new Branch(instance, false);
    }

    @Test
    public void testProgramFlow() {
        JSONObject loJSON;

         loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel() .setBranchCd("tst2");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel() .setDescription("OYA OYA");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setBranchNm("Banchnaem");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setContact("321");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setCompanyID("0002");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setTownID("0001");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setAddress("Address HERE");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setValue("cAutomate" , "1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setValue("cMainOffc" , "1");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setModifiedBy(instance.getUserID());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

    }
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
