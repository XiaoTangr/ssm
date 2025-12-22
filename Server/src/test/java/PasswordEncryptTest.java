
import cn.javat.ssm.service.UserService;
import cn.javat.ssm.util.PasswordEncryptUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * 密码加密测试（Spring 6.2.7 + JUnit 5）
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PasswordEncryptTest {


    private final PasswordEncryptUtil passwordEncryptUtil;

    @Autowired
    public PasswordEncryptTest(PasswordEncryptUtil passwordEncryptUtil) {
        this.passwordEncryptUtil = passwordEncryptUtil;
    }

    @Test
    public void testEncrypt() {
        // 测试加密123456
        String password = "123456";
        String encryptedPassword = passwordEncryptUtil.encryptPassword(password);
        System.out.println("加密后的密码：" + encryptedPassword);
    }
}