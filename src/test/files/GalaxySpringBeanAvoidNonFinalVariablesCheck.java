import org.springframework.stereotype.Component;
import com.dcits.ensemble.util.BusiUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class test{
    int a ; // Noncompliant {{新核心应用不允许在服务Bean中声明非Final成员变量！}}
    String b = "AAA"; // Noncompliant {{新核心应用不允许在服务Bean中声明非Final成员变量！}}
    public static ThreadLocal<Map<String, Object>> LOCAL = new ThreadLocal<Map<String, Object>>() {
        protected Map<String, Object> initialValue() {
            return new HashMap();
        }
    };
    int m1(String var1 ,int var2){
        a=0;
        if(var1.equals(b)){
            a=a+var2;
        }
        return a;
    }
}

/**
 * @Description: 服务后处理工厂类
 * @author: Chengliang
 * @date: 2018/03/31 14:48
 */
@Component
public class AfterProcessFactory implements ApplicationContextAware {

    private static List<IAfterProcess> afterProcessList;

    public static List<IAfterProcess> getAfterProcessList() {
        return afterProcessList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IAfterProcess> map = applicationContext.getBeansOfType(IAfterProcess.class);
        if (BusiUtil.isNotNull(map)) {
            afterProcessList = Lists.newArrayList(map.values());
            // 按照OrderNo排序
            Collections.sort(afterProcessList, new Comparator<IAfterProcess>() {
                @Override
                public int compare(IAfterProcess o1, IAfterProcess o2) {
                    Integer process1 = o1.getOrderNo();
                    Integer process2 = o2.getOrderNo();
                    return process1.compareTo(process2);
                }
            });
        }
    }
}
