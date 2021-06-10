import lombok.Data;

public class OptimisticCASImpl {

    public static void main(String[] args) {

    }
}

@Data
class A {
    /**
     * 预期值
     */
    private Integer a;

    /**
     * 内存值
     */
    private String b;

    /**
     * 修改的变量值
     */
    private Integer c;
}

class B {
    private Integer number;
}