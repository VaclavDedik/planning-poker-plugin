package ut.com.redhat.engineering.plugins;

import org.junit.Test;
import com.redhat.engineering.plugins.MyPluginComponent;
import com.redhat.engineering.plugins.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}