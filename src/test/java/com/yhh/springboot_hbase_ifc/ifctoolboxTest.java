package com.yhh.springboot_hbase_ifc;

import com.apstex.ifc4x2toolbox.ifc4x2.IfcBridge;
import com.apstex.ifc4x2toolbox.ifc4x2.IfcProduct;
import com.apstex.ifc4x2toolbox.ifcmodel.IfcModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest
public class ifctoolboxTest {

    @Test
    public void test() {
        IfcModel model = new IfcModel();
        File file = new File("D:\\model\\4.2\\bridge.ifc");
        try {
            model.readStepFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collection<IfcProduct> ifcProducts = model.getCollection(IfcProduct.class);
        ArrayList<String> direct_attributes = new ArrayList<String>();
        for (IfcProduct product : ifcProducts) {
            System.out.println(product.getClassName());
            direct_attributes.add(product.getClassName());
            //判断是否是IfcBridge
            if (product.getClassName().equals("IfcBridge")) {
                //转换为IfcBridge
                IfcBridge bridge = (IfcBridge) product;
                //获取IfcBridge的属性
                System.out.println(bridge.getGlobalId());
                System.out.println(bridge.getOwnerHistory());
                System.out.println(bridge.getOwnerHistory().getOwningUser());
                //获取IfcBridge的类型
                System.out.println(bridge.getPredefinedType());
            }
        }
    }
}
