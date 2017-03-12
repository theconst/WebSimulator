///*
// * 
// */
//package ua.kpi.atep.model.dynamic.items;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import ua.kpi.atep.model.dynamic.object.DynamicModel;
//import ua.kpi.atep.model.dynamic.object.DynamicModelInput;
//import ua.kpi.atep.model.dynamic.object.DynamicModelOutput;
//
///**
// *
// * @author Home
// */
//public class FirstOrderLagTest {
//    
//    private final DynamicItemFactory factory;
//    
//    private DynamicItem lag;
//    
//    public FirstOrderLagTest() {
//        factory = DynamicItemFactory.newInstance(0.1);
//        
//        lag = factory.createFirstOrderLag(10, 100);
//        
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//        
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//    
//     @Test
//     public void isInitialConditonCorrect() {
//          List<Double> vals = new ArrayList<>();
//         
//          DynamicItem first = factory.createFirstOrderLag(1.0, 100.0);
//          DynamicItem second = factory.createFirstOrderLag(1.0, 100.0);
//          first.setInitialCondition(200.0);
//          second.setInitialCondition(200.0);
//          
//           lag = DynamicItems.sequentialConnection(
//                first,
//                second);
//          
//          DynamicModelInput input = new DynamicModelInput(new String[]{"in", "in1"}, new double[]{0.0, 0.0});
//          DynamicModelOutput output = new DynamicModelOutput(new String[]{"out", "out2"}, new double[]{0.0, 0.0});
//          DynamicModel model = DynamicModel.newInstance(input, output);
//          
//          model.setTransferFunction("in", lag, "out");
//          
//          double[] in = new double[]{1.0, 0.0};
//          
//          model.setTimespan(1.0);
//          
//          for (int i = 0; i < 20; ++i) {
//              vals.add(model.value(in, false)[0]);
//          }
//          
//          Double[] result = new Double[20];
//          
//          result[0] = 200.0;
//          
//          
//          Assert.assertArrayEquals(result, vals.toArray(new Double[0]));
//     }
//}
