
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameters.Banks_Branches;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testBanks_Branches {

    static GRider instance;
    static Banks_Branches record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");

        instance = MiscUtil.Connect();
        record = new Banks_Branches(instance, false);
    }

    @Test
    public void testProgramFlow() {
        JSONObject loJSON;

         loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

//        loJSON = record.getModel().setBranchesBanksID("435");
//        if ("error".equals((String) loJSON.get("result"))) {
//            Assert.fail((String) loJSON.get("message"));
//        }

        loJSON = record.getModel().setBranchesBanksName("MMB");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
         loJSON = record.getModel().setBranchesBanksCoDe("4343");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
         loJSON = record.getModel().setBanksID("2");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setBranchesBanksID("3");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setAddress("Salapingao");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setTownID("Dagupan");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
        loJSON = record.getModel() .setTelephoneNumber("09708487992");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }
        
         loJSON = record.getModel() .setFaxNumber("4234");
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
