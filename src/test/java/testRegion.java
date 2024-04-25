
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameters.Region;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testRegion {
    static GRider instance;
    static Region record;
    
    @BeforeClass
    public static void setUpClass(){
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        
        instance = MiscUtil.Connect();
        record = new Region(instance, false);
    }
    
   
    
    @Test
    public void testProgramFlow(){
        JSONObject loJSON;
        
        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setRegionID("10");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setRegionName("HELLO");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setMinimumWages(23457.0);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setColaAmount(23567.34);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setMinimumWage2(23522.0);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setColaAmount2(23435.35);
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
