import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 乐观锁 --- 版本号实现
 * 实现：
 * 两个线程去修改一个version的number值，修改的同时会去改变版本号
 * 提交版本号必须大于数据记录的版本号才能执行，这样避免了旧数据覆盖新数据的问题
 */
public class OptimisticVersionImpl {

    public static void main(String[] args) {
        //线程1
        Runnable run = () -> {
            //每次增加都加上一个版本号，如果版本号不大于当前版本号，则更新失败
            Version.number = Version.number + 1;
            Integer version = Version.version;
            if (version < 2) {
                Version.version = version + 1;
                System.out.println("更新成功" + "\n");
            } else {
                System.out.println("更新失败");
            }
            System.out.println(new Version().toString());
        };

//        List<String> list = new ArrayList<>();
//        list.add("1");
//        list.add("1");
//        list.add("1");
//        list.add("1");
//        int i = 0;
//        AtomicInteger a = new AtomicInteger(i);
//        list.forEach(v -> {
//            a.incrementAndGet();
//            System.out.println(a.get());
//            //原子类去进行++操作
//        });

        run.run();
        run.run();
    }

}

class Version {
    /**
     * 值
     */
    public static Integer number = 1;

    /**
     * 版本号
     */
    public static Integer version = 1;

    @Override
    public String toString() {
        return number + " ===== " + version;
    }
}
