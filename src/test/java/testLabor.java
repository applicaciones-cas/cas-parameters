
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameters.Labor;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testLabor {
    static GRider instance;
    static Labor record;
    
    @BeforeClass
    public static void setUpClass(){
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        
        instance = MiscUtil.Connect();
        record = new Labor(instance, false);
    }
    
   
    
    @Test
    public void testProgramFlow(){
        JSONObject loJSON;
        
        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setLaborIDx("1257");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setLaborNme("Mark Manaois");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setPriceLv1(21345.65);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setPriceLv2(67894.23);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setPriceLv3(12347.58);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setInHousex("A");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setLaborTyp("B");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setModifiedBy(instance.getUserID());
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setModifiedDate(instance.getServerDate());
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.saveRecord();
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
    }
    
     @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
