package workshop;

import org.testng.ISuite;
import org.testng.ISuiteListener;

public class GroupByInstanceEnabler implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        suite.getXmlSuite().setGroupByInstances(true);
    }

    @Override
    public void onFinish(ISuite suite) {
    }
}
