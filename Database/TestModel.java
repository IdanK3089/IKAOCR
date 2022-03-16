package Database;

import org.zoodb.api.impl.ZooPC;

public class TestModel extends ZooPC {

    int id;

    String TestName;

    String TestPrice;

    public int getId() {
       zooActivateRead();
        return id;
    }

    public void setId(int id) {
     zooActivateWrite();
        this.id = id;
    }

    public String getTestName() {
       zooActivateRead();
        return TestName;

    }

    public void setTestName(String testName) {
      zooActivateWrite();
        TestName = testName;
    }

    public String getTestPrice() {
    zooActivateRead();
        return TestPrice;
    }

    public void setTestPrice(String testPrice) {
       zooActivateWrite();
        TestPrice = testPrice;
    }
}
