
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameters.Department;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testDepartment {

    static GRider instance;
    static Department record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");

        instance = MiscUtil.Connect();
        record = new Department(instance, false);
    }

    @Test
    public void testProgramFlow() {
        JSONObject loJSON;

        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setDepartmentID("345");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setDepartmentName("Mark");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setDepartmentHead(" Manaois");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setMobileNumber("09432778361");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setEmailAddress("MarkManaoisHelloWorld");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setDepartmentCode("1345");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setHeadAssignID("12345");
        if ("error".equals((String) loJSON.get("result"))) {
            Assert.fail((String) loJSON.get("message"));
        }

        loJSON = record.getModel().setSupervisorAssignID("215");
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
