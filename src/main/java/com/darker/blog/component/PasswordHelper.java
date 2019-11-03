package com.darker.blog.component;

import com.darker.blog.orm.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;


@Component
public class PasswordHelper {

    //实例化RandomNumberGenerator对象，用于生成一个随机数
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    @Getter
    @Setter
    private String algorithName = "MD5";
    @Getter
    @Setter
    private int hashInterations = 2;

    public RandomNumberGenerator getRandomNumberGenerator() {
        return randomNumberGenerator;
    }

    //加密算法
    public void encryptPassword(User sysUser) {
        if (sysUser.getPassword() != null) {
            sysUser.setSalt(randomNumberGenerator.nextBytes().toHex());
            //调用SimpleHash指定散列算法参数：1、算法名称；2、用户输入的密码；3、盐值（随机生成的）；4、散列迭代次数
            String newPassword = new SimpleHash(
                    algorithName,
                    sysUser.getPassword(),
                    ByteSource.Util.bytes(sysUser.getSalt()),
                    hashInterations).toHex();
            sysUser.setPassword(newPassword);
        }
    }
}
