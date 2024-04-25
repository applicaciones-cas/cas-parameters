
import org.guanzon.appdriver.base.GRider;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.parameters.Model;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testModel {
    static GRider instance;
    static Model record;
    
    @BeforeClass
    public static void setUpClass(){
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/");
        
        instance = MiscUtil.Connect();
        record = new Model(instance, false);
    }
    
   
    
    @Test
    public void testProgramFlow(){
        JSONObject loJSON;
        
        loJSON = record.newRecord();
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setCategoryCode("145109");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setModelName("SystemConfig");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setDescription("HelloWorld");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setBriefDescription("MarkPogi");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setBrandCode("43742");
        if ("error".equals((String) loJSON.get("result"))) Assert.fail((String) loJSON.get("message"));
        
        loJSON = record.getModel().setEndOfLife("1");
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
