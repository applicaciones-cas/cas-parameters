
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

        loJSON = record.getModel().setRegionNm("HELLO");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setMinWages(23457.0);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setColaAmtx(23567.34);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setMinWage2(23522.0);
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setColaAmt2(23435.35);
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
