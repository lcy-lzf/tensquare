import com.tensquare.encrypt.EncryptApplication;
import com.tensquare.encrypt.rsa.RsaKeys;
import com.tensquare.encrypt.service.RsaService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EncryptApplication.class)
public class EncryptTest {

    @Autowired
    private RsaService rsaService;

    @Before
    public void before() throws Exception{
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void genEncryptDataByPubKey() {
        String data = "{\"title\":\"java\"}";

        try {

            String encData = rsaService.RSAEncryptDataPEM(data, RsaKeys.getServerPubKey());

            System.out.println("data: " + data);
            System.out.println("encData: " + encData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() throws Exception{
        String requestData = "KbhfadDcrQvEjWb1FPx7cATiddeWDW2Kpu+tncUsV7iwrFa8Giu6YPgRXKESCebyXkM6Tj64Bb8mtTck+MnjUvEsOcE7leJxAiEQTxmb96ZoEnDAgg8qWoFt24oyqOSlHCwFK0Kk8yjOwc78GdOEhvWVJNDz5PYa2nCnd0Eh5D/unz41EYdGdUEeNqJ+zfPxVMp0XkWFgYMu5QYRWYXzEq9Iwb/1z6x2eqTawZbBGyI4j6haJk4qqVa2VcgYPf0tuWgNub/aAyN3QntqKyZQ20Oi1JLtFFEaUCfmfj1zb0vXV88O1K/ffmuZdQByGIEcctvXwl8kM8Yh0+RSH45qSg==";

        String decryptDataPEM = rsaService.RSADecryptDataPEM(requestData, RsaKeys.getServerPrvKeyPkcs8());

        System.out.println(decryptDataPEM);
    }
}
