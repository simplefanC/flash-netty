package the.flash.attribute;

import io.netty.util.AttributeKey;

public interface Attributes {
    /**
     * 是否登录成功的标志位
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
